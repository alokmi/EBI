package uk.ac.ebi.ensembl.sequencevalidator.processors;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import uk.ac.ebi.ensembl.sequencevalidator.models.GeneSequence;

@Service
@PropertySource("classpath:chromosome-files.properties")
public class GeneSequenceProcessor {

	private static final String UTF_8_FORMAT = "UTF-8";
	private static final String NON_CODING_PATTERN = "N";
	private static final String COLON_SEPARATOR = ":";
	private static final String GZ_EXTENSION = ".gz";
	private static final String CALCULATED_GENE_SEQUENCE = "CALCULATED_GENE_SEQUENCE";
	private static final String HEADER_GENE_SEQUENCE = "HEADER_GENE_SEQUENCE";
	private static final int OFFSET = 1;
	private static final String CHROMOSOME = "chromosome:";

	@Value("#{'${chromosome.file.names}'.split(',')}")
	private List<String> chromosomeFileNames;

	final static Logger logger = Logger.getLogger(GeneSequenceProcessor.class);

	/**
	 * Processes the file and extracts the start and end of 
	 * coding sequence from header and body
	 * @param filePath
	 * @return Map of header and body gene sequence
	 */
	public Map<String, GeneSequence> fileProcessor(String filePath) {
		FileInputStream inputStream = null;
		Scanner sc = null;
		boolean startFlag = false;
		long counter = 1;
		long startTime = System.currentTimeMillis();
		Map<String, GeneSequence> map = new HashMap<String, GeneSequence>();
		GeneSequence calculatedGeneSequence = new GeneSequence();
		try {
			inputStream = new FileInputStream(filePath);
			sc = new Scanner(inputStream, UTF_8_FORMAT);

			if (sc.hasNext()) {
				String firstLine = sc.nextLine();
				extractEndpoints(firstLine, map);
			}
			calculatedGeneSequence.setChromosomeName(map.get(HEADER_GENE_SEQUENCE).getChromosomeName());

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				line = line.replaceAll(NON_CODING_PATTERN, "");
				if (startFlag && line.isEmpty()) {
					calculatedGeneSequence.setEndSegment(counter);
					startFlag = false;
				}
				counter++;
				if (line.length() > 0) {
					if (!startFlag && calculatedGeneSequence.getStartSegment() == 0) {
						calculatedGeneSequence.setStartSegment(counter);
					}
					startFlag = true;
				}
			}
			if (calculatedGeneSequence.getEndSegment() == 0)
				calculatedGeneSequence.setEndSegment(counter);
			if (sc.ioException() != null) {
				throw sc.ioException();
			}
			long endTime = System.currentTimeMillis();
			map.put(CALCULATED_GENE_SEQUENCE, calculatedGeneSequence);
			logger.debug("Time taken (in millis) -> " + (endTime - startTime));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (sc != null) {
				sc.close();
			}
		}
		return map;
	}

	/**
	 * Validates header with entire file sequence
	 * @param geneSeqMap
	 * @return
	 */
	public boolean validate(Map<String, GeneSequence> geneSeqMap) {
		if (geneSeqMap.get(HEADER_GENE_SEQUENCE).equals(geneSeqMap.get(CALCULATED_GENE_SEQUENCE))) {
			logger.info("Header Genes -> " + geneSeqMap.get(HEADER_GENE_SEQUENCE));
			logger.info("Calculated Genes -> " + geneSeqMap.get(CALCULATED_GENE_SEQUENCE));
			logger.info("Its a valid file");
			return true;
		} else {
			logger.info("Header Genes -> " + geneSeqMap.get(HEADER_GENE_SEQUENCE));
			logger.info("Calculated Genes -> " + geneSeqMap.get(CALCULATED_GENE_SEQUENCE));
			logger.info("Invalid file, sequence does not match");
			logger.info("****************************************************");
			return false;
		}
	}
	
	/**
	 * Extracts start and end endpoints from header
	 * @param line
	 * @param map
	 */
	private void extractEndpoints(String line, Map<String, GeneSequence> map) {
		String arr[] = line.split(CHROMOSOME)[1].split(COLON_SEPARATOR);
		GeneSequence headerGeneSequence = new GeneSequence();
		headerGeneSequence.setChromosomeName(arr[1]);
		float startSegLong = Long.parseLong(arr[2]);
		long startSeg = (new Double(Math.ceil(startSegLong / 60))).longValue() + OFFSET;
		float endSegLong = Long.parseLong(arr[3]);
		long endSeg = (new Double(Math.ceil(endSegLong / 60))).longValue() + OFFSET;
		headerGeneSequence.setStartSegment(startSeg);
		headerGeneSequence.setEndSegment(endSeg);
		map.put(HEADER_GENE_SEQUENCE, headerGeneSequence);

	}

	/**
	 * Downloads and unzips the files
	 * @param ftpURL
	 * @param localFilePath
	 * @return List of unzipped file names 
	 */
	public List<String> downloadUnzipFiles(String ftpURL, String localFilePath) {
		byte[] buffer = new byte[1024];
		List<String> unzippedFiles = new ArrayList<String>();
		for (String fileName : chromosomeFileNames) {
			String fullFTPFilePath = ftpURL + fileName;
			String localPath = localFilePath + fileName;
			String unzippedPath = localPath.replace(GZ_EXTENSION, "");
			logger.debug("fetching file : " + fullFTPFilePath + " to local path " + localPath);
			try (BufferedInputStream in = new BufferedInputStream(new URL(fullFTPFilePath).openStream());
					FileOutputStream fileOutputStream = new FileOutputStream(localPath)) {
				byte dataBuffer[] = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
					fileOutputStream.write(dataBuffer, 0, bytesRead);
				}
				GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(localPath));
				FileOutputStream out = new FileOutputStream(unzippedPath);
				logger.debug("unzippedPath : " + unzippedPath);
				int len;
				while ((len = gzis.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				gzis.close();
				out.close();
				unzippedFiles.add(unzippedPath);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return unzippedFiles;
	}

	
}

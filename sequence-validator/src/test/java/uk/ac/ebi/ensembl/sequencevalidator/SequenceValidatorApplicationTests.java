package uk.ac.ebi.ensembl.sequencevalidator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Map;

import org.junit.jupiter.api.Test;

import uk.ac.ebi.ensembl.sequencevalidator.models.GeneSequence;
import uk.ac.ebi.ensembl.sequencevalidator.processors.GeneSequenceProcessor;

class SequenceValidatorApplicationTests {

	private static final String CALCULATED_GENE_SEQUENCE = "CALCULATED_GENE_SEQUENCE";
	private static final String HEADER_GENE_SEQUENCE = "HEADER_GENE_SEQUENCE";

	@Test
	public void testFile1Processor() {
		GeneSequenceProcessor processor = new GeneSequenceProcessor();
		File file = new File("src/test/resources/chromosome.test1.fa");
		String filePath = file.getAbsolutePath();
		System.out.println(filePath);
		Map<String, GeneSequence> map = processor.fileProcessor(filePath);
		assertEquals(2, map.get(CALCULATED_GENE_SEQUENCE).getStartSegment());
		assertEquals(5, map.get(CALCULATED_GENE_SEQUENCE).getEndSegment());
		
		assertEquals(2, map.get(HEADER_GENE_SEQUENCE).getStartSegment());
		assertEquals(5, map.get(HEADER_GENE_SEQUENCE).getEndSegment());
	}
	
	@Test
	public void testFile2Processor() {
		GeneSequenceProcessor processor = new GeneSequenceProcessor();
		File file = new File("src/test/resources/chromosome.test2.fa");
		String filePath = file.getAbsolutePath();
		System.out.println(filePath);
		Map<String, GeneSequence> map = processor.fileProcessor(filePath);
		assertEquals(3, map.get(CALCULATED_GENE_SEQUENCE).getStartSegment());
		assertEquals(7, map.get(CALCULATED_GENE_SEQUENCE).getEndSegment());
		
		assertEquals(3, map.get(HEADER_GENE_SEQUENCE).getStartSegment());
		assertEquals(7, map.get(HEADER_GENE_SEQUENCE).getEndSegment());
	}
	
	@Test
	public void testFile3Processor() {
		GeneSequenceProcessor processor = new GeneSequenceProcessor();
		File file = new File("src/test/resources/chromosome.test3.fa");
		String filePath = file.getAbsolutePath();
		System.out.println(filePath);
		Map<String, GeneSequence> map = processor.fileProcessor(filePath);
		assertEquals(4, map.get(CALCULATED_GENE_SEQUENCE).getStartSegment());
		assertEquals(12, map.get(CALCULATED_GENE_SEQUENCE).getEndSegment());
		
		assertEquals(4, map.get(HEADER_GENE_SEQUENCE).getStartSegment());
		assertEquals(12, map.get(HEADER_GENE_SEQUENCE).getEndSegment());
	}
	
}

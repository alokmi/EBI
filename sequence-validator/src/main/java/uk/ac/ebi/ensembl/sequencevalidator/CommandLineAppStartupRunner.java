package uk.ac.ebi.ensembl.sequencevalidator;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import uk.ac.ebi.ensembl.sequencevalidator.models.GeneSequence;
import uk.ac.ebi.ensembl.sequencevalidator.processors.GeneSequenceProcessor;

@Component
@PropertySource("classpath:sequence-validator.properties")
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    private GeneSequenceProcessor processor;

    @Value("${ensembl.ftp.url}")
    private String ftpURL;
    
    @Value("${local.file.path}")
    private String localFilePath;
    
    final static Logger logger = Logger.getLogger(CommandLineAppStartupRunner.class);
    
    @Override
    public void run(String...args) throws Exception {
    	long startTime = System.currentTimeMillis();
		List<String> unzippedfiles = processor.downloadUnzipFiles(ftpURL, localFilePath);

		// loop over unzipped file
		for (String file : unzippedfiles) {
			Map<String, GeneSequence> geneSeqMap = processor.fileProcessor(file);
			processor.validate(geneSeqMap);
		}
		
		long endTime = System.currentTimeMillis();
		logger.info("Total time to process -> "+(endTime - startTime));

    }
}

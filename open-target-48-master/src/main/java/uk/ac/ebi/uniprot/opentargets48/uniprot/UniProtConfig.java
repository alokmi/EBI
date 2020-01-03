package uk.ac.ebi.uniprot.opentargets48.uniprot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.ServiceFactory;
import uk.ac.ebi.uniprot.opentargets48.common.models.OTARProteinEntry;
import uk.ac.ebi.uniprot.opentargets48.common.services.RetryStrategy;
import uk.ac.ebi.uniprot.opentargets48.interpro.services.InterproService;
import uk.ac.ebi.uniprot.opentargets48.uniprot.listeners.UniprotReadListener;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.OTARUniProtEntry;
import uk.ac.ebi.uniprot.opentargets48.uniprot.processors.UniProtEntryProcessor;
import uk.ac.ebi.uniprot.opentargets48.uniprot.readers.UniProtEntryReader;
import uk.ac.ebi.uniprot.opentargets48.uniprot.writers.JsonItemWriter;

@Configuration
@EnableBatchProcessing
public class UniProtConfig {
  public static final int DEFAULT_CHUNK_SIZE = 50;
  public static final String JOB_NAME = "processEntriesJob";
  public static final String STEP_NAME = "processEntriesStep";

  @Value("${spring.batch.outDir}")
  private String outDir;

  @Autowired private JobBuilderFactory jobs;

  @Autowired private StepBuilderFactory steps;

  @Bean
  protected Step processEntries(
      UniProtEntryReader reader,
      UniProtEntryProcessor processor,
      ItemWriter<OTARProteinEntry> writer) {
    return steps
        .get(STEP_NAME)
        .<OTARUniProtEntry, OTARProteinEntry>chunk(DEFAULT_CHUNK_SIZE)
        .reader(reader)
        .listener(new UniprotReadListener())
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public Job job() {
    return jobs.get(JOB_NAME)
        .start(processEntries(itemReader(), itemProcessor(), jsonFileItemWriter()))
        .build();
  }

  @Bean
  protected UniProtEntryReader itemReader() {
    ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();
    return new UniProtEntryReader(
        serviceFactoryInstance.getUniProtQueryService(), getRetryStrategy(), getRetryStrategy());
  }

  @Bean
  public UniProtEntryProcessor itemProcessor() {
    InterproService interproService = new InterproService(new RestTemplateBuilder());
    return new UniProtEntryProcessor(interproService, getRetryStrategy());
  }

  @Bean
  public JsonItemWriter jsonFileItemWriter() {
    String fileName = new SimpleDateFormat("yyyyMMdd'.json'").format(new Date());
    fileName = "OTAR02-48-" + fileName;
    Resource output = new FileSystemResource(new File(outDir + fileName));
    return new JsonItemWriter(output, new JacksonJsonObjectMarshaller<>());
  }

  private RetryStrategy getRetryStrategy() {
    return new RetryStrategy();
  }
}

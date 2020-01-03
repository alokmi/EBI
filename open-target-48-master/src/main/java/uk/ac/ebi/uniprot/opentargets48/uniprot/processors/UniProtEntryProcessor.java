package uk.ac.ebi.uniprot.opentargets48.uniprot.processors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import uk.ac.ebi.uniprot.opentargets48.common.models.OTARProteinEntry;
import uk.ac.ebi.uniprot.opentargets48.common.services.RetryStrategy;
import uk.ac.ebi.uniprot.opentargets48.interpro.models.ProteinFamilies;
import uk.ac.ebi.uniprot.opentargets48.interpro.models.InterProResponse;
import uk.ac.ebi.uniprot.opentargets48.interpro.services.InterproService;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.OTARUniProtEntry;

@Slf4j
public class UniProtEntryProcessor implements ItemProcessor<OTARUniProtEntry, OTARProteinEntry> {
  private final InterproService interpro;
  private RetryStrategy<InterProResponse> retryStrategy;

  public UniProtEntryProcessor(InterproService interpro, RetryStrategy retryStrategy) {
    this.interpro = interpro;
    this.retryStrategy = retryStrategy;
  }

  public OTARProteinEntry process(OTARUniProtEntry entry) {
    return createProteinEntry(entry);
  }

  private OTARProteinEntry createProteinEntry(OTARUniProtEntry entry) {
    InterProResponse familyResponse = retryStrategy.execute(context -> {
      if (context.getRetryCount() > 0) {
        log.warn("Retrying with count : " + context.getRetryCount());
      }
      return interpro.fetch(entry.getAccession());
    });
    ProteinFamilies families = ProteinFamilies.from(familyResponse);

    OTARProteinEntry.Builder builder =
        new OTARProteinEntry.Builder(entry.getId(), entry.getAccession())
            .withFunctions(entry.getFunctions())
            .withComplexes(entry.getComplexes())
            .withActivities(entry.getCatalyticActivities())
            .withEnzymeRegulations(entry.getEnzymeRegulations())
            .withBpcProperties(entry.getBpcProperties())
            .withCofactors(entry.getCofactorGroup())
            .withFamilies(families);
    return builder.build();
  }
}

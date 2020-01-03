package uk.ac.ebi.uniprot.opentargets48.uniprot.readers;

import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtField;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtQueryBuilder;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtService;
import uk.ac.ebi.uniprot.dataservice.query.Query;
import uk.ac.ebi.uniprot.opentargets48.common.services.RetryStrategy;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.OTARUniProtEntry;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.UniProtEntryBuilder;

@Slf4j
public class UniProtEntryReader implements ItemReader<OTARUniProtEntry> {
  static final String HUMAN_TAXONOMY_ID = "9606";
  private final UniProtService service;
  private Iterator<UniProtEntry> iterator;
  private RetryStrategy<OTARUniProtEntry> retryStrategy;
  private RetryStrategy<Iterator<UniProtEntry>> startRetry;
  private int total;

  public UniProtEntryReader(
      UniProtService service, RetryStrategy retryStrategy, RetryStrategy startRetry) {
    this.service = service;
    this.total = 0;
    this.retryStrategy = retryStrategy;
    this.startRetry = startRetry;
  }

  @Override
  public OTARUniProtEntry read() {
    if (iterator == null) {
      iterator = entries();
    }
    return retryStrategy.execute(
        context -> {
          if (context.getRetryCount() > 0) {
            log.warn("Retrying again with count :" + context.getRetryCount());
          }
          if (iterator.hasNext()) {
            total++;
            log.info("Entry count so far: " + total);
            return convert(iterator.next());
          } else {
            log.info("Finished reading " + total + " entries in UniProt");
            service.stop();
            return null;
          }
        });
  }

  public Query getQuery() {
    return UniProtQueryBuilder.query(UniProtField.Search.organism_tax_id, HUMAN_TAXONOMY_ID)
        .and(
            UniProtQueryBuilder.comments(CommentType.COFACTOR, "*")
                .or(UniProtQueryBuilder.comments(CommentType.FUNCTION, "*"))
                .or(UniProtQueryBuilder.comments(CommentType.CATALYTIC_ACTIVITY, "*"))
                .or(UniProtQueryBuilder.comments(CommentType.BIOPHYSICOCHEMICAL_PROPERTIES, "*")))
        .and(UniProtQueryBuilder.swissprot());
  }

  private Iterator<UniProtEntry> entries() {
    service.start();
    return startRetry.execute(
        context -> {
          if (context.getRetryCount() > 0) {
            log.warn("Retrying initial fetch again with count :" + context.getRetryCount());
          }
          return service.getEntries(getQuery());
        });
  }

  private OTARUniProtEntry convert(UniProtEntry entry) {
    String id = entry.getUniProtId().toString();
    String accession = entry.getPrimaryUniProtAccession().toString();

    return new UniProtEntryBuilder(id, accession)
        .withFunctions(entry)
        .withComplexIds(entry)
        .withCatalyticActivities(entry)
        .withEnzymeRegulations(entry)
        .withBpcProperties(entry)
        .withCofactors(entry)
        .build();
  }
}

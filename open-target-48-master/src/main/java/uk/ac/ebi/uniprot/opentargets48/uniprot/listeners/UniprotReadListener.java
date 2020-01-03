package uk.ac.ebi.uniprot.opentargets48.uniprot.listeners;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.OTARUniProtEntry;

@Slf4j
public class UniprotReadListener implements ItemReadListener<OTARUniProtEntry> {
  @Override
  public void beforeRead() {
    log.info("Reading UniProt entry");
  }

  @Override
  public void afterRead(OTARUniProtEntry item) {
    log.info("Done reading UniProt entry: " + item.getAccession());
  }

  @Override
  public void onReadError(Exception e) {
    log.error("Error occured while reading UniProt entry");
    log.error(e.getMessage());
  }
}

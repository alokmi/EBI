package uk.ac.ebi.uniprot.opentargets48.uniprot.models;

import lombok.Data;

@Data
public class EvidenceDescription {
  private final String code;
  private final String sourceId;
  private final String sourceName;
}

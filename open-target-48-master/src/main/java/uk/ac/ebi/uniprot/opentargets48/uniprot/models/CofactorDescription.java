package uk.ac.ebi.uniprot.opentargets48.uniprot.models;

import java.util.List;
import lombok.Data;

@Data
public class CofactorDescription {
  private final String value;
  private final List<EvidenceDescription> evidences;
  private final List<CrossRefDescription> xrefs;
}

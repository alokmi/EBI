package uk.ac.ebi.uniprot.opentargets48.uniprot.models;

import java.util.List;
import lombok.Data;

@Data
public class FunctionDescription {
  private final String text;
  private final List<EvidenceDescription> evidences;
}

package uk.ac.ebi.uniprot.opentargets48.uniprot.models;

import java.util.List;
import lombok.Data;

@Data
public class EnzymeRegulationDescription {
  private final String value;
  private final List<EvidenceDescription> evidences;
}

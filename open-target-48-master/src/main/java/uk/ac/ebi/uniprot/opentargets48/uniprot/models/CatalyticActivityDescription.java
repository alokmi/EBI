package uk.ac.ebi.uniprot.opentargets48.uniprot.models;

import java.util.List;
import lombok.Data;

@Data
public class CatalyticActivityDescription {
  private final String type;
  private final String name;
  private final String ecNumber;
  private final List<CrossRefDescription> xrefs;
}

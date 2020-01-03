package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.CatalyticActivityDescription;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CatalyticAcitivity {
  private final String type;
  private final String name;
  private final String ecNumber;
  @JsonUnwrapped private final CrossRefs crossRefs;

  public static CatalyticAcitivity from(CatalyticActivityDescription activity) {
    CrossRefs xrefs = CrossRefs.from(activity.getXrefs());
    String type = activity.getType();
    String name = activity.getName();
    String ecNumber = activity.getEcNumber();
    return new CatalyticAcitivity(type, name, ecNumber, xrefs);
  }
}

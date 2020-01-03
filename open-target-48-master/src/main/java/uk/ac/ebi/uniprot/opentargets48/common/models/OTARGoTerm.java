package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.interpro.models.GoTerm;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OTARGoTerm {
  private final String Id;
  private final String name;
  private final OTARGoCategory category;

  public static OTARGoTerm from(GoTerm item) {
    String Id = item.getIdentifier();
    String name = item.getName();
    OTARGoCategory category = OTARGoCategory.from(item.getCategory());
    return new OTARGoTerm(Id, name, category);
  }
}

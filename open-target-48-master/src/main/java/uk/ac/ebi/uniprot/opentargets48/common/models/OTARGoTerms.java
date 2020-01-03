package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.interpro.models.GoTerm;
import uk.ac.ebi.uniprot.opentargets48.interpro.models.GoTerms;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OTARGoTerms {
  private final List<OTARGoTerm> goTerms;

  public static OTARGoTerms from(List<GoTerm> items) {
    List<OTARGoTerm> goTerms = new ArrayList<>();
    if (items != null) {
      for (GoTerm item : items) {
        goTerms.add(OTARGoTerm.from(item));
      }
    }
    return new OTARGoTerms(goTerms);
  }
}

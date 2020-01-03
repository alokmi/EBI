package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.interpro.models.ProteinFamilies;
import uk.ac.ebi.uniprot.opentargets48.interpro.models.ProteinFamily;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OTARProteinFamilies {
  private final List<OTARProteinFamily> families;

  public static OTARProteinFamilies from(ProteinFamilies items) {
    List<OTARProteinFamily> families = new ArrayList<>();
    if (items != null) {
      for (ProteinFamily family : items.getFamilies()) {
        families.add(OTARProteinFamily.from(family));
      }
    }
    return new OTARProteinFamilies(families);
  }
}

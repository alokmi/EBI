package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.CatalyticActivityDescription;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CatalyticActivities {
  private final List<CatalyticAcitivity> catalyticAcitivities;

  public static CatalyticActivities from(List<CatalyticActivityDescription> activities) {
    List<CatalyticAcitivity> result = new ArrayList<>();
    if (activities != null) {
      for (CatalyticActivityDescription activity: activities) {
        result.add(CatalyticAcitivity.from(activity));
      }
    }
    return new CatalyticActivities(result);
  }
}

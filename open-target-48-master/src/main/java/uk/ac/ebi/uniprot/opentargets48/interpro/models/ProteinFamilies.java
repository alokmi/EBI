package uk.ac.ebi.uniprot.opentargets48.interpro.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProteinFamilies {
  private final List<ProteinFamily> families;

  public static ProteinFamilies from(InterProResponse response) {
    List<ProteinFamily> families = new ArrayList<>();
    if (response != null && response.getResults() != null) {
      List<Result> results = response.getResults();
      for (Result result : results) {
        families.add(ProteinFamily.from(result));
      }
    }
    return new ProteinFamilies(families);
  }
}

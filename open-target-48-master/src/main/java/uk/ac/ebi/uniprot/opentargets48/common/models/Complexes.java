package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Complexes {
  private final List<Complex> complexes;

  public static Complexes from(List<Map<String, String>> complexes) {
    List<Complex> result = new ArrayList<>();
    for (Map<String, String> complex : complexes) {
      result.add(new Complex(complex.get("Id"), complex.get("description")));
    }
    return new Complexes(result);
  }
}

package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BiophysicochemicalProperties {
  private final List<BiophysicochemicalProperty> biophysicochemicalProperties;

  public static BiophysicochemicalProperties from(List<Map<String, Object>> items) {
    List<BiophysicochemicalProperty> biophysicochemicalProperties = new ArrayList<>();
    if (items != null) {
      for (Map<String, Object> item : items) {
        biophysicochemicalProperties.add(BiophysicochemicalProperty.from(item));
      }
    }
    return new BiophysicochemicalProperties(biophysicochemicalProperties);
  }
}

package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OTARGoCategory {
  private final String code;
  private final String name;

  public static OTARGoCategory from(Map<String, String> item) {
    String code = item.get("code");
    String name = item.get("name");
    return new OTARGoCategory(code, name);
  }
}

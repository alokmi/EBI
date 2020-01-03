package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.EvidenceDescription;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BiophysicochemicalProperty {
  private final String type;
  private final Map<String, List<Kinetic>> kinetics;

  public static BiophysicochemicalProperty from(Map<String, Object> item) {
    String type = (String) item.get("type");
    List<Kinetic> km = getKinetics((List<Map<String, Object>>) item.get("km"));
    Map<String, List<Kinetic>> kinetics = new HashMap<>();
    kinetics.put("km", km);
    return new BiophysicochemicalProperty(type, kinetics);
  }

  private static List<Kinetic> getKinetics(List<Map<String, Object>> kinetics) {
    List<Kinetic> result = new ArrayList<>();
    for (Map<String, Object> kinetic : kinetics) {
      String value = (String) kinetic.get("value");
      Publications publications =
          Publications.from((List<EvidenceDescription>) kinetic.get("evidences"));
      result.add(new Kinetic(value, publications));
    }
    return result;
  }
}

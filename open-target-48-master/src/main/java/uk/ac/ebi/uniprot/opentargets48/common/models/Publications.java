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
public class Publications {
  private final List<Publication> publications;

  public static Publications from(List<EvidenceDescription> evidences) {
    List<Publication> publications = new ArrayList<>();
    if (evidences != null) {
      Map<String, List<EvidenceDescription>> publicationByCode = new HashMap<>();
      for (EvidenceDescription evidence : evidences) {
        publicationByCode.computeIfAbsent(evidence.getCode(), c -> new ArrayList<>()).add(evidence);
      }
      for (Map.Entry<String, List<EvidenceDescription>> entry : publicationByCode.entrySet()) {
        publications.add(Publication.from(entry.getKey(), entry.getValue()));
      }
    }
    return new Publications(publications);
  }
}

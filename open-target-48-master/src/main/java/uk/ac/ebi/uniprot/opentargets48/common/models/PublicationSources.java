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
public class PublicationSources {
  private final List<PublicationSource> sources;

  public static PublicationSources from(List<EvidenceDescription> descriptions) {
    List<PublicationSource> sources = new ArrayList<>();
    if (descriptions != null) {
      Map<String, List<String>> groupByName = new HashMap<>();
      for (EvidenceDescription description : descriptions) {
        String id = description.getSourceId();
        String name = description.getSourceName();
        groupByName.computeIfAbsent(name, i -> new ArrayList<>()).add(id);
      }
      for (Map.Entry<String, List<String>> entry : groupByName.entrySet()) {
        sources.add(PublicationSource.from(entry.getKey(), entry.getValue()));
      }
    }
    return new PublicationSources(sources);
  }
}

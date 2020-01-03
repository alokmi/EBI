package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PublicationSource {
  private final String name;
  private final List<String> Ids;

  public static PublicationSource from(String name, List<String> ids) {
    return new PublicationSource(name, ids);
  }
}

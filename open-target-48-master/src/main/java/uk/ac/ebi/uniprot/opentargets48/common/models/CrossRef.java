package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.CrossRefDescription;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Data
public class CrossRef {
  private final String Id;
  private final String name;
  private String url;
  private String alternativeUrl;

  public static CrossRef from(CrossRefDescription ref) {
    return new CrossRef(ref.getId(), ref.getName());
  }
}

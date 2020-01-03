package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.common.models.Publications;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class EnzymeRegulation {
  private String text;
  @JsonUnwrapped private Publications publications;
}

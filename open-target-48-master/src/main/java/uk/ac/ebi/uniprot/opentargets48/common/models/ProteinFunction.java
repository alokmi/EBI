package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProteinFunction {
  private final String text;
  @JsonUnwrapped private final Publications publications;
}

package uk.ac.ebi.uniprot.opentargets48.interpro.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class InterProResponse {
  @JsonUnwrapped
  private List<Result> results;
}

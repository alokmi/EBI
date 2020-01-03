package uk.ac.ebi.uniprot.opentargets48.interpro.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ExtraFields {
  private List<String> description = new ArrayList<>();
  private LinkedHashMap<String, Object> literature = new LinkedHashMap<>();
}

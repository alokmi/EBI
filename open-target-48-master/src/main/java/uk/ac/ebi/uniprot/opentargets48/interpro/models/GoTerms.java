package uk.ac.ebi.uniprot.opentargets48.interpro.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoTerms {
  private List<GoTerm> goTerms;
}

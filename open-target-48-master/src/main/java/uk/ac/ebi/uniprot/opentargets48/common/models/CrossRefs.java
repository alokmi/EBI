package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.CrossRefDescription;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Data
public class CrossRefs {
  private final List<CrossRef> xrefs;

  public static final CrossRefs from(List<CrossRefDescription> refs) {
    List<CrossRef> xrefs = new ArrayList<>();
    if (refs != null) {
      for (CrossRefDescription ref : refs) {
        xrefs.add(CrossRef.from(ref));
      }
    }
    return new CrossRefs(xrefs);
  }
}

package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.FunctionDescription;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProteinFunctions {
  private final List<ProteinFunction> functions;

  public static ProteinFunctions from(List<FunctionDescription> items) {
    List<ProteinFunction> functions = new ArrayList<>();
    if (items != null) {
      for (FunctionDescription function : items) {
        String text = function.getText();
        Publications publications = Publications.from(function.getEvidences());
        functions.add(new ProteinFunction(text, publications));
      }
    }
    return new ProteinFunctions(functions);
  }
}

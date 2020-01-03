package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.EnzymeRegulationDescription;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EnzymeRegulations {
  private final List<EnzymeRegulation> enzymeRegulations;

  public static EnzymeRegulations from(List<EnzymeRegulationDescription> items) {
    List<EnzymeRegulation> enzymeRegulations = new ArrayList<>();
    if (items != null) {
      for (EnzymeRegulationDescription item : items) {
        Publications publications = Publications.from(item.getEvidences());
        String text = item.getValue();
        enzymeRegulations.add(new EnzymeRegulation(text, publications));
      }
    }
    return new EnzymeRegulations(enzymeRegulations);
  }
}

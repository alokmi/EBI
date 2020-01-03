package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.interpro.models.ProteinFamily;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OTARProteinFamily {
  private final String Id;
  private final String description;
  @JsonUnwrapped
  private final OTARGoTerms goTerms;
  @JsonUnwrapped
  private final Publications publications;

  public static OTARProteinFamily from(ProteinFamily family) {
    String Id = family.getAccession();
    String description = family.getDescription();
    OTARGoTerms goTerms = OTARGoTerms.from(family.getGoTerms());
    Publications publications = family.getPublications();
    return new OTARProteinFamily(Id, description, goTerms, publications);
  }
}

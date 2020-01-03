package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.interpro.models.ProteinFamilies;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.CatalyticActivityDescription;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.CofactorGroupDescription;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.EnzymeRegulationDescription;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.FunctionDescription;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OTARProteinEntry {
  private String Id;
  private String accession;
  @JsonUnwrapped private BiophysicochemicalProperties biophysicochemicalProperties;
  @JsonUnwrapped private ProteinFunctions functions;
  @JsonUnwrapped private Complexes complexes;
  @JsonUnwrapped private EnzymeRegulations enzymeRegulations;
  @JsonUnwrapped private CatalyticActivities catalyticAcitivities;
  @JsonUnwrapped private OTARProteinFamilies families;
  private CofactorGroup cofactorGroups;

  @JsonIgnoreType
  public static class Builder {
    private String Id;
    private String accession;
    private List<Map<String, String>> complexes = new ArrayList<>();
    private List<CatalyticActivityDescription> activities = new ArrayList<>();
    private List<FunctionDescription> functions = new ArrayList<>();
    private List<EnzymeRegulationDescription> enzymeRegulations = new ArrayList<>();
    private List<Map<String, Object>> bpcProperties = new ArrayList<>();
    private CofactorGroupDescription cofactorGroup;
    private ProteinFamilies families;

    public Builder(String Id, String accession) {
      this.Id = Id;
      this.accession = accession;
    }

    public Builder withFunctions(List<FunctionDescription> functions) {
      this.functions.addAll(functions);
      return this;
    }

    public Builder withComplexes(List<Map<String, String>> complexes) {
      this.complexes.addAll(complexes);
      return this;
    }

    public Builder withActivities(List<CatalyticActivityDescription> activities) {
      this.activities.addAll(activities);
      return this;
    }

    public Builder withEnzymeRegulations(List<EnzymeRegulationDescription> regulations) {
      this.enzymeRegulations.addAll(regulations);
      return this;
    }

    public Builder withBpcProperties(List<Map<String, Object>> properties) {
      this.bpcProperties.addAll(properties);
      return this;
    }

    public Builder withCofactors(CofactorGroupDescription cofactorGroup) {
      this.cofactorGroup = cofactorGroup;
      return this;
    }

    public Builder withFamilies(ProteinFamilies families) {
      this.families = families;
      return this;
    }

    public OTARProteinEntry build() {
      OTARProteinEntry entry = new OTARProteinEntry();
      entry.Id = this.Id;
      entry.accession = this.accession;
      entry.biophysicochemicalProperties = BiophysicochemicalProperties.from(this.bpcProperties);
      entry.functions = ProteinFunctions.from(this.functions);
      entry.complexes = Complexes.from(this.complexes);
      entry.enzymeRegulations = EnzymeRegulations.from(this.enzymeRegulations);
      entry.catalyticAcitivities = CatalyticActivities.from(this.activities);
      entry.cofactorGroups = CofactorGroup.from(this.cofactorGroup);
      entry.families = OTARProteinFamilies.from(this.families);
      return entry;
    }
  }

  private OTARProteinEntry() {}
}

package uk.ac.ebi.uniprot.opentargets48.uniprot.models;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class OTARUniProtEntry {
  private final String Id;
  private final String accession;
  private final List<Map<String, String>> complexes;
  private final List<FunctionDescription> functions;
  private final List<CatalyticActivityDescription> catalyticActivities;
  private final List<EnzymeRegulationDescription> enzymeRegulations;
  private final List<Map<String, Object>> bpcProperties;
  private final CofactorGroupDescription cofactorGroup;
}

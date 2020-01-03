package uk.ac.ebi.uniprot.opentargets48.uniprot.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.ac.ebi.kraken.interfaces.uniprot.DatabaseCrossReference;
import uk.ac.ebi.kraken.interfaces.uniprot.DatabaseType;
import uk.ac.ebi.kraken.interfaces.uniprot.EvidencedValue;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Cofactor;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentText;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.MichaelisConstant;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.ReactionReference;
import uk.ac.ebi.kraken.interfaces.uniprot.evidences.EvidenceId;
import uk.ac.ebi.kraken.model.uniprot.comments.BioPhysicoChemicalPropertiesCommentImpl;
import uk.ac.ebi.kraken.model.uniprot.comments.CatalyticActivityCommentStructuredImpl;
import uk.ac.ebi.kraken.model.uniprot.comments.CofactorCommentStructuredImpl;
import uk.ac.ebi.kraken.model.uniprot.comments.EnzymeRegulationCommentImpl;
import uk.ac.ebi.kraken.model.uniprot.comments.FunctionCommentImpl;
import uk.ac.ebi.kraken.model.uniprot.comments.KineticParametersImpl;

public class UniProtEntryBuilder {
  private String Id;
  private String accession;
  private List<Map<String, String>> complexes = new ArrayList<>();
  private List<FunctionDescription> functions = new ArrayList<>();
  private List<CatalyticActivityDescription> catalyticActivities = new ArrayList<>();
  private List<EnzymeRegulationDescription> enzymeRegulations = new ArrayList<>();
  private CofactorGroupDescription cofactorGroup;
  private List<Map<String, Object>> bpcProperties = new ArrayList<>();

  public UniProtEntryBuilder(String Id, String accession) {
    this.Id = Id;
    this.accession = accession;
  }

  public UniProtEntryBuilder withFunctions(UniProtEntry entry) {
    for (Comment comment : entry.getComments(CommentType.FUNCTION)) {
      String text = ((FunctionCommentImpl) comment).getValue();
      List<EvidenceDescription> evidences = getEvidences(comment.getEvidenceIds());
      this.functions.add(new FunctionDescription(text, evidences));
    }
    return this;
  }

  public UniProtEntryBuilder withComplexIds(UniProtEntry entry) {
    List<DatabaseCrossReference> references =
        entry.getDatabaseCrossReferences(DatabaseType.COMPLEXPORTAL);
    for (DatabaseCrossReference r : references) {
      Map<String, String> complex = new HashMap<>();
      complex.put("Id", r.getPrimaryId().toString());
      complex.put("description", r.getDescription().toString());
      this.complexes.add(complex);
    }
    return this;
  }

  public UniProtEntryBuilder withCatalyticActivities(UniProtEntry entry) {
    for (Comment c : entry.getComments(CommentType.CATALYTIC_ACTIVITY)) {
      CatalyticActivityCommentStructuredImpl fc = (CatalyticActivityCommentStructuredImpl) c;
      String type = "reaction";
      String name = fc.getReaction().getName();
      String ecNumber = fc.getReaction().getECNumber();
      List<CrossRefDescription> xrefs = getReferences(fc.getReaction().getReactionReferences());
      this.catalyticActivities.add(new CatalyticActivityDescription(type, name, ecNumber, xrefs));
    }
    return this;
  }

  public UniProtEntryBuilder withEnzymeRegulations(UniProtEntry entry) {
    List<Comment> regulations = entry.getComments(CommentType.ACTIVITY_REGULATION);
    for (Comment comment : regulations) {
      List<CommentText> texts = ((EnzymeRegulationCommentImpl) comment).getTexts();
      for (CommentText text : texts) {
        List<EvidenceDescription> evidences = getEvidences(text.getEvidenceIds());
        this.enzymeRegulations.add(new EnzymeRegulationDescription(text.getValue(), evidences));
      }
    }
    return this;
  }

  public UniProtEntryBuilder withBpcProperties(UniProtEntry entry) {
    List<Comment> properties = entry.getComments(CommentType.BIOPHYSICOCHEMICAL_PROPERTIES);
    for (Comment comment : properties) {
      Map<String, Object> map = new HashMap<>();
      KineticParametersImpl kinetics =
          (KineticParametersImpl)
              ((BioPhysicoChemicalPropertiesCommentImpl) comment).getKineticParameters();
      map.put("type", "kinetics");
      map.put("km", new ArrayList<>());
      List<Map<String, Object>> list = (List) map.get("km");
      for (MichaelisConstant mc : kinetics.getMichaelisConstants()) {
        Map<String, Object> km = new HashMap<>();
        km.put(
            "value",
            mc.getConstant()
                + " "
                + mc.getUnit().getDisplayString()
                + " for "
                + mc.getSubstrate().getValue());
        km.put("evidences", getEvidences(mc.getEvidenceIds()));
        list.add(km);
      }
      map.put("km", list);
      this.bpcProperties.add(map);
    }
    return this;
  }

  public UniProtEntryBuilder withCofactors(UniProtEntry entry) {
    List<Comment> comments = entry.getComments(CommentType.COFACTOR);
    List<CofactorDescription> cofactorList = new ArrayList<>();
    for (Comment comment : comments) {
      for (Cofactor cofactor : ((CofactorCommentStructuredImpl) comment).getCofactors()) {
        List<CrossRefDescription> xrefs = new ArrayList<>();
        xrefs.add(new CrossRefDescription(
            cofactor.getCofactorReference().getReferenceId(),
            cofactor.getCofactorReference().getCofactorReferenceType().name()));
        String value = cofactor.getName();
        List<EvidenceDescription> evidences = getEvidences(cofactor.getEvidenceIds());
        cofactorList.add(new CofactorDescription(value, evidences, xrefs));
      }
      List<NoteDescription> notes = new ArrayList<>();
      for (EvidencedValue ev : ((CofactorCommentStructuredImpl) comment).getNote().getTexts()) {
        List<EvidenceDescription> evidences = getEvidences(ev.getEvidenceIds());
        notes.add(new NoteDescription(ev.getValue(), evidences));
      }
      this.cofactorGroup = new CofactorGroupDescription(cofactorList, notes);
    }
    return this;
  }

  public OTARUniProtEntry build() {
    return new OTARUniProtEntry(
        Id,
        accession,
        complexes,
        functions,
        catalyticActivities,
        enzymeRegulations,
        bpcProperties,
        cofactorGroup);
  }

  private List<EvidenceDescription> getEvidences(List<EvidenceId> evidenceIds) {
    List<EvidenceDescription> evidences = new ArrayList<>();
    for (EvidenceId evidence : evidenceIds) {
      String code = evidence.getEvidenceCode().getCodeValue();
      String name = evidence.getTypeValue();
      String Id = evidence.getAttribute().getValue();
      evidences.add(new EvidenceDescription(code, Id, name));
    }
    return evidences;
  }

  private List<CrossRefDescription> getReferences(List<ReactionReference> references) {
    List<CrossRefDescription> xrefs = new ArrayList<>();
    for (ReactionReference reference : references) {
      String Id = reference.getId();
      String name = reference.getType().name();
      xrefs.add(new CrossRefDescription(Id, name));
    }
    return xrefs;
  }
}

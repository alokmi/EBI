package uk.ac.ebi.uniprot.opentargets48.common.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.CofactorDescription;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.CofactorGroupDescription;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.NoteDescription;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CofactorGroup {
  private final List<Cofactor> cofactors;
  private final List<CofactorNote> notes;

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class Cofactor {
    private final String value;
    @JsonUnwrapped private final CrossRefs crossRefs;
    @JsonUnwrapped private final Publications publications;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class CofactorNote {
    private final String text;
    @JsonUnwrapped private final Publications publications;
  }

  public static CofactorGroup from(CofactorGroupDescription cofactorGroup) {
    if (cofactorGroup == null) {
      return new CofactorGroup(new ArrayList<>(), new ArrayList<>());
    }
    List<CofactorDescription> cofactors = cofactorGroup.getCofactors();
    List<NoteDescription> notes = cofactorGroup.getNotes();

    List<Cofactor> cfs = new ArrayList<>();
    if (cofactors != null) {
      for (CofactorDescription cofactor : cofactors) {
        String text = cofactor.getValue();
        CrossRefs xrefs = CrossRefs.from(cofactor.getXrefs());
        Publications publications = Publications.from(cofactor.getEvidences());
        cfs.add(new Cofactor(text, xrefs, publications));
      }
    }

    List<CofactorNote> cofactorNotes = new ArrayList<>();
    if (notes != null) {
      for (NoteDescription note : notes) {
        Publications publications = Publications.from(note.getEvidences());
        cofactorNotes.add(new CofactorNote(note.getText(), publications));
      }
    }
    return new CofactorGroup(cfs, cofactorNotes);
  }
}

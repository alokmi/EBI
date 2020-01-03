package uk.ac.ebi.uniprot.opentargets48.uniprot.models;

import java.util.List;
import lombok.Data;

@Data
public class CofactorGroupDescription {
  private final List<CofactorDescription> cofactors;
  private final List<NoteDescription> notes;
}

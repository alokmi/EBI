package uk.ac.ebi.uniprot.opentargets48.uniprot.writers;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonObjectMarshaller;
import org.springframework.core.io.Resource;
import uk.ac.ebi.uniprot.opentargets48.common.models.OTARProteinEntry;

@Slf4j
public class JsonItemWriter extends JsonFileItemWriter<OTARProteinEntry> {
  public JsonItemWriter(
      Resource resource, JsonObjectMarshaller<OTARProteinEntry> jsonObjectMarshaller) {
    super(resource, jsonObjectMarshaller);
  }

  @Override
  public String doWrite(List<? extends OTARProteinEntry> items) {
    return super.doWrite(items);
  }
}

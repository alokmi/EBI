package uk.ac.ebi.uniprot.opentargets48.interpro.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import uk.ac.ebi.uniprot.opentargets48.common.models.Publications;
import uk.ac.ebi.uniprot.opentargets48.uniprot.models.EvidenceDescription;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProteinFamily {
  private final String accession;
  private final String description;
  private final List<GoTerm> goTerms;
  private final Publications publications;

  public static ProteinFamily from(Result result) {
    String accession = result.getMetadata().getAccession();
    List<String> descriptions = result.getExtra_fields().getDescription();

    List<EvidenceDescription> evidences = new ArrayList<>();
    LinkedHashMap<String, Object> literature = result.getExtra_fields().getLiterature();
    Map<String, String> codeToPMId = new HashMap<>();
    if (literature != null) {
      for (Map.Entry<String, Object> entry : literature.entrySet()) {
        String code = entry.getKey();
        String name = "PMID";
        Integer Id = ((Map<String, Integer>) entry.getValue()).get(name);
        evidences.add(new EvidenceDescription(code, Id.toString(), name));
        codeToPMId.put(code, Id.toString());
      }
    }
    return new ProteinFamily(
        accession,
        buildDescription(descriptions, codeToPMId),
        result.getMetadata().getGo_terms(),
        Publications.from(evidences)
    );
  }

  static String buildDescription(List<String> list, Map<String, String> map) {
    return replaceCiteIds(String.join("", list), map);
  }

  static String replaceCiteIds(String description, Map<String, String> map) {
    Document document = Jsoup.parse(description);
    Elements citeIds = document.select("cite");
    for (Element element : citeIds) {
      String code = element.attr("id");
      element.replaceWith(new TextNode(map.getOrDefault(code, "")));
    }
    return document.body().text();
  }
}

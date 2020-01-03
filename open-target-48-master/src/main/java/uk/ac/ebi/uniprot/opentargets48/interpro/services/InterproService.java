package uk.ac.ebi.uniprot.opentargets48.interpro.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.uniprot.opentargets48.interpro.models.InterProResponse;

@Slf4j
@Component
public class InterproService {
  private final RestTemplate restTemplate;
  static final String URL =
      "https://www.ebi.ac.uk/interpro/beta/api/entry/interpro/protein/uniprot/%s";
  static final String Params = "?type=family&extra_fields=description,literature";

  public InterproService(RestTemplateBuilder restTemplateBuilder) {
    restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
  }

  public InterProResponse fetch(String accession) {
    String URI = String.format(URL + Params, accession);
    log.info("Calling Interpro with URI " + URI);
    ResponseEntity<InterProResponse> responseEntity =
        restTemplate.getForEntity(URI, InterProResponse.class);
    return responseEntity.getBody();
  }
}

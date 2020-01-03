package uk.ac.ebi.uniprot.opentargets48.interpro.services;

import java.io.IOException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Slf4j
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
  @Override
  public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
    HttpStatus.Series statusCode = httpResponse.getStatusCode().series();
    return (statusCode == HttpStatus.Series.CLIENT_ERROR || statusCode == Series.SERVER_ERROR);
  }

  @Override
  public void handleError(ClientHttpResponse httpResponse) throws IOException {
    HttpStatus.Series statusCode = httpResponse.getStatusCode().series();
    if (statusCode == HttpStatus.Series.SERVER_ERROR) {
      log.error("Server error");
    } else if (statusCode == HttpStatus.Series.CLIENT_ERROR) {
      log.error("Client error " + statusCode.toString());
      if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
        log.error("Client error not found " + statusCode.toString());
      }
    }
  }
}

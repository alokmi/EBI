package uk.ac.ebi.uniprot.opentargets48.uniprot

import spock.lang.Specification
import uk.ac.ebi.uniprot.dataservice.client.QueryResult
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtService
import uk.ac.ebi.uniprot.dataservice.query.Query
import uk.ac.ebi.uniprot.opentargets48.uniprot.readers.UniProtEntryReader;

class OTARUniProtEntryReaderSpec extends Specification {
    UniProtEntryReader reader
    UniProtService mockService;
    QueryResult mockQueryResult;

    def setup() {
        mockService = Mock(UniProtService.class)
        mockQueryResult = Mock(QueryResult.class)
    }

    def "should call uniprot service"() {
        given:
        reader = new UniProtEntryReader(mockService)
        Query query = reader.getQuery()

        when:
        reader.read()

        then:
        1 * mockService.getEntries(query) >> mockQueryResult
    }

    def "should return uniprot data"() {
        given:
        reader = new UniProtEntryReader(mockService)
        Query query = reader.getQuery()

        when:
        reader.read()

        then:
        1 * mockService.getEntries(query) >> mockQueryResult
    }
}

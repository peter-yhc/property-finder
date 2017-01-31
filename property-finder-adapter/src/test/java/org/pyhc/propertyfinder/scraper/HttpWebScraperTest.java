package org.pyhc.propertyfinder.scraper;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.AdapterTest;
import org.pyhc.propertyfinder.scraper.model.RealEstateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.client.ExpectedCount.between;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
@AdapterTest
public class HttpWebScraperTest {
    private static final String REALESTATE_DOMAIN = "http://www.realestate.com.au";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebScraper webScraper;

    private MockRestServiceServer mockServer;

    @Before
    public void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void canQueryRealEstate_WithSuburbParameter_AndMakeSubsequentCallsToGetDetailedPages() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/suburb-search.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/in-parramatta%2c+nsw+2150/list-1?numParkingSpaces=any&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));
        mockServer.expect(between(1, 20), new PropertyRequestMatcher()).andRespond(withSuccess());

        RealEstateQuery realEstateQuery = RealEstateQuery.builder().suburb("parramatta").postalCode(2150).build();
        webScraper.query(realEstateQuery);

        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndConfiguration() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/suburb-configuration-search.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/with-2-bedrooms-in-homebush+west%2c+nsw+2140/list-1?numParkingSpaces=1&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));
        mockServer.expect(between(1, 20), new PropertyRequestMatcher()).andRespond(withSuccess());

        RealEstateQuery realEstateQuery = RealEstateQuery.builder()
                .suburb("homebush west")
                .postalCode(2140)
                .minBeds(2)
                .carSpaces(1)
                .build();
        webScraper.query(realEstateQuery);

        mockServer.verify();
    }

    private String loadPageFromTestResources(String resourcePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Files.readAllLines(Paths.get(resourcePath)).forEach(stringBuilder::append);
        return stringBuilder.toString();
    }
}
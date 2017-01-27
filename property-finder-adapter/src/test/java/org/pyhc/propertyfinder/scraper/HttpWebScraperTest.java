package org.pyhc.propertyfinder.scraper;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.AdapterTest;
import org.pyhc.propertyfinder.scraper.model.PropertyResult;
import org.pyhc.propertyfinder.scraper.model.RealEstateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.MediaType.TEXT_HTML;
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
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/in-parramatta%2c+nsw+2150/list-1?source=location-search"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-unit-nsw-parramatta-124572450")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-unit-nsw-parramatta-124571682")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-parramatta-124561254")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-parramatta-124560930")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-parramatta-124560754")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-parramatta-124559470")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-parramatta-124439506")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/project-promenade-nsw-parramatta-600005175")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/project-altitude-nsw-parramatta-600004063")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-parramatta-124552474")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/project-8+phillip+st-nsw-parramatta-600014174")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-parramatta-124547214")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-unit-nsw-parramatta-123793898")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-parramatta-124402982")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-house-nsw-parramatta-124435254")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-unit-nsw-parramatta-123868322")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-parramatta-124534502")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-unit-nsw-parramatta-124424954")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-parramatta-124424934")).andRespond(withSuccess());
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-parramatta-124471342")).andRespond(withSuccess());

        RealEstateQuery realEstateQuery = RealEstateQuery.builder().suburb("parramatta").postalCode(2150).build();
        webScraper.query(realEstateQuery);

        mockServer.verify();
    }

    private String loadPageFromTestResources(String resourcePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Files.readAllLines(Paths.get(resourcePath)).forEach(stringBuilder::append);
        return stringBuilder.toString();
    }
}
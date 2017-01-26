package org.pyhc.propertyfinder.scraper;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.AdapterTest;
import org.pyhc.propertyfinder.scraper.model.RealEstateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
@AdapterTest
public class HttpWebScraperTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebScraper webScraper;

    private MockRestServiceServer mockRestServiceServer;

    @Before
    public void setup() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void canQueryRealEstate_WithSuburbParameter() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        Files.readAllLines(Paths.get("src/test/resources/stub/realestate/suburb-search.html")).forEach(stringBuilder::append);
        String htmlPage = stringBuilder.toString();
        mockRestServiceServer.expect(once(), requestTo("http://www.realestate.com.au/buy/in-parramatta%2c+nsw+2150/list-1?source=location-search"))
                .andRespond(withSuccess(htmlPage, MediaType.TEXT_HTML));

        RealEstateQuery realEstateQuery = RealEstateQuery.builder().withSuburb("parramatta", 2150).build();
        webScraper.query(realEstateQuery);

        mockRestServiceServer.verify();
    }
}
package org.pyhc.propertyfinder.scraper;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.AdapterTest;
import org.pyhc.propertyfinder.scraper.model.PropertyProfile;
import org.pyhc.propertyfinder.scraper.model.Query;
import org.pyhc.propertyfinder.scraper.model.RealEstateLink;
import org.pyhc.propertyfinder.scraper.model.RealEstateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/generic-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/in-parramatta%2c+nsw+2150/list-1?numBaths=any&numParkingSpaces=any&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));
        mockServer.expect(between(1, 20), new PropertyRequestMatcher()).andRespond(withSuccess());

        Query realEstateQuery = RealEstateQuery.builder().suburb("parramatta").postalCode(2150).build();
        webScraper.query(realEstateQuery);

        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMinBeds_AndCar() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/generic-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/with-2-bedrooms-in-homebush+west%2c+nsw+2140/list-1?numBaths=any&numParkingSpaces=1&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));
        mockServer.expect(between(1, 20), new PropertyRequestMatcher()).andRespond(withSuccess());

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("homebush west")
                .postalCode(2140)
                .minBeds(2)
                .carSpaces(1)
                .build();
        webScraper.query(realEstateQuery);

        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMaxBeds_AndCar() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/generic-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/with-studio-in-homebush+west%2c+nsw+2140/list-1?numBaths=any&numParkingSpaces=2&maxBeds=2"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));
        mockServer.expect(between(1, 20), new PropertyRequestMatcher()).andRespond(withSuccess());

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("homebush west")
                .postalCode(2140)
                .maxBeds(2)
                .carSpaces(2)
                .build();
        webScraper.query(realEstateQuery);

        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMinMaxBeds_AndCar() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/generic-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/with-1-bedrooms-in-homebush+west%2c+nsw+2140/list-1?numBaths=any&numParkingSpaces=2&maxBeds=3"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));
        mockServer.expect(between(1, 20), new PropertyRequestMatcher()).andRespond(withSuccess());

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("homebush west")
                .postalCode(2140)
                .minBeds(1)
                .maxBeds(3)
                .carSpaces(2)
                .build();
        webScraper.query(realEstateQuery);

        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMinPrice() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/generic-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/between-700000-any-in-parramatta%2c+nsw+2150/list-1?numBaths=any&numParkingSpaces=any&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));
        mockServer.expect(between(1, 20), new PropertyRequestMatcher()).andRespond(withSuccess());

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("parramatta")
                .postalCode(2150)
                .minPrice(700000)
                .build();
        webScraper.query(realEstateQuery);

        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMaxPrice() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/generic-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/between-0-650000-in-parramatta%2c+nsw+2150/list-1?numBaths=any&numParkingSpaces=any&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));
        mockServer.expect(between(1, 20), new PropertyRequestMatcher()).andRespond(withSuccess());

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("parramatta")
                .postalCode(2150)
                .maxPrice(650000)
                .build();
        webScraper.query(realEstateQuery);

        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMinMaxPrice() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/generic-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/between-500000-650000-in-parramatta%2c+nsw+2150/list-1?numBaths=any&numParkingSpaces=any&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));
        mockServer.expect(between(1, 20), new PropertyRequestMatcher()).andRespond(withSuccess());

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("parramatta")
                .postalCode(2150)
                .minPrice(500000)
                .maxPrice(650000)
                .build();
        webScraper.query(realEstateQuery);

        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndBathrooms() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/generic-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/in-homebush%2c+nsw+2140/list-1?numBaths=1&numParkingSpaces=any&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));
        mockServer.expect(between(1, 20), new PropertyRequestMatcher()).andRespond(withSuccess());

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("homebush")
                .postalCode(2140)
                .bathrooms(1)
                .build();
        webScraper.query(realEstateQuery);

        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMinMaxPrice_AndMinMaxBeds_AndCar() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/generic-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/with-2-bedrooms-between-500000-650000-in-parramatta%2c+nsw+2150/list-1?numBaths=2&numParkingSpaces=any&maxBeds=3"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));
        mockServer.expect(between(1, 20), new PropertyRequestMatcher()).andRespond(withSuccess());

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("parramatta")
                .postalCode(2150)
                .minBeds(2)
                .maxBeds(3)
                .bathrooms(2)
                .minPrice(500000)
                .maxPrice(650000)
                .build();
        webScraper.query(realEstateQuery);

        mockServer.verify();
    }

    @Test
    public void canQueryRealEstatePropertyProfile1() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/property-profile-page-1.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-hornsby-124578062"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateLink.builder()
                .propertyLink("/property-apartment-nsw-hornsby-124578062")
                .build();
        PropertyProfile propertyProfile = webScraper.queryProfilePage(realEstateQuery);

        assertThat(propertyProfile.getPropertyLink(), is("http://www.realestate.com.au/property-apartment-nsw-hornsby-124578062"));
        assertThat(propertyProfile.getAddress(), is("4/10 Albert Street"));
        assertThat(propertyProfile.getBed(), is(2));
        assertThat(propertyProfile.getBath(), is(1));
        assertThat(propertyProfile.getCar(), is(1));
        assertThat(propertyProfile.getSuburb(), is("Hornsby"));
        assertThat(propertyProfile.getPostalCode(), is(2077));
        assertThat(propertyProfile.getPropertyCode(), is("124578062"));
        assertThat(propertyProfile.getPriceEstimate(), is("640000-680000"));

        mockServer.verify();
    }

    private String loadPageFromTestResources(String resourcePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Files.readAllLines(Paths.get(resourcePath)).forEach(stringBuilder::append);
        return stringBuilder.toString();
    }
}
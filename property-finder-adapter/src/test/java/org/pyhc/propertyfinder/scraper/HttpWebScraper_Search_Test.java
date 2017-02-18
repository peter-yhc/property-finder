package org.pyhc.propertyfinder.scraper;

import org.junit.Test;
import org.pyhc.propertyfinder.scraper.realestate.query.Query;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateQuery;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class HttpWebScraper_Search_Test extends HttpWebScraper_Base_Test {

    @Test
    public void canQueryRealEstate_WithSuburbParameter_AndMakeSubsequentCallsToGetDetailedPages() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/single-page-result-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/in-parramatta%2c+nsw+2150/list-1?numBaths=any&numParkingSpaces=any&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateQuery.builder().suburb("parramatta").postalCode(2150).build();
        List<Query> queries = webScraper.search(realEstateQuery).get();
        assertThat(queries.size(), is(12));
        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMinBeds_AndCar() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/single-page-result-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/with-2-bedrooms-in-homebush+west%2c+nsw+2140/list-1?numBaths=any&numParkingSpaces=1&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("homebush west")
                .postalCode(2140)
                .minBeds(2)
                .carSpaces(1)
                .build();
        List<Query> queries = webScraper.search(realEstateQuery).get();
        assertThat(queries.size(), is(12));
        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMaxBeds_AndCar() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/single-page-result-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/with-studio-in-homebush+west%2c+nsw+2140/list-1?numBaths=any&numParkingSpaces=2&maxBeds=2"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("homebush west")
                .postalCode(2140)
                .maxBeds(2)
                .carSpaces(2)
                .build();
        List<Query> queries = webScraper.search(realEstateQuery).get();
        assertThat(queries.size(), is(12));
        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMinMaxBeds_AndCar() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/single-page-result-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/with-1-bedrooms-in-homebush+west%2c+nsw+2140/list-1?numBaths=any&numParkingSpaces=2&maxBeds=3"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("homebush west")
                .postalCode(2140)
                .minBeds(1)
                .maxBeds(3)
                .carSpaces(2)
                .build();
        List<Query> queries = webScraper.search(realEstateQuery).get();
        assertThat(queries.size(), is(12));
        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMinPrice() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/single-page-result-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/between-700000-any-in-parramatta%2c+nsw+2150/list-1?numBaths=any&numParkingSpaces=any&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("parramatta")
                .postalCode(2150)
                .minPrice(700000)
                .build();
        List<Query> queries = webScraper.search(realEstateQuery).get();
        assertThat(queries.size(), is(12));
        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMaxPrice() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/single-page-result-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/between-0-650000-in-parramatta%2c+nsw+2150/list-1?numBaths=any&numParkingSpaces=any&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("parramatta")
                .postalCode(2150)
                .maxPrice(650000)
                .build();
        List<Query> queries = webScraper.search(realEstateQuery).get();
        assertThat(queries.size(), is(12));
        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMinMaxPrice() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/single-page-result-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/between-500000-650000-in-parramatta%2c+nsw+2150/list-1?numBaths=any&numParkingSpaces=any&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("parramatta")
                .postalCode(2150)
                .minPrice(500000)
                .maxPrice(650000)
                .build();
        List<Query> queries = webScraper.search(realEstateQuery).get();
        assertThat(queries.size(), is(12));
        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndBathrooms() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/single-page-result-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/in-homebush%2c+nsw+2140/list-1?numBaths=1&numParkingSpaces=any&maxBeds=any"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("homebush")
                .postalCode(2140)
                .bathrooms(1)
                .build();
        List<Query> queries = webScraper.search(realEstateQuery).get();
        assertThat(queries.size(), is(12));
        mockServer.verify();
    }

    @Test
    public void canQueryRealEstate_WithSuburb_AndMinMaxPrice_AndMinMaxBeds_AndCar() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/single-page-result-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/with-2-bedrooms-between-500000-650000-in-parramatta%2c+nsw+2150/list-1?numBaths=2&numParkingSpaces=any&maxBeds=3"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("parramatta")
                .postalCode(2150)
                .minBeds(2)
                .maxBeds(3)
                .bathrooms(2)
                .minPrice(500000)
                .maxPrice(650000)
                .build();
        List<Query> queries = webScraper.search(realEstateQuery).get();
        assertThat(queries.size(), is(12));
        mockServer.verify();
    }

    @Test
    public void canGetNextPage() throws Exception {
        String firstpage = loadPageFromTestResources("src/test/resources/stub/realestate/multi-page-result-search-page.html");
        String secondpage = loadPageFromTestResources("src/test/resources/stub/realestate/single-page-result-search-page.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/in-parramatta%2c+nsw+2150/list-1?numBaths=any&numParkingSpaces=any&maxBeds=any"))
                .andRespond(withSuccess(firstpage, TEXT_HTML));
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/buy/in-parramatta%2c+nsw+2150/list-2"))
                .andRespond(withSuccess(secondpage, TEXT_HTML));

        Query realEstateQuery = RealEstateQuery.builder()
                .suburb("parramatta")
                .postalCode(2150)
                .build();
        List<Query> queries = webScraper.search(realEstateQuery).get();
        assertThat(queries.size(), is(32));
        mockServer.verify();
    }

}
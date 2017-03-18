package org.pyhc.propertyfinder.scraper;


import org.junit.Test;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class WebScraper_SoldProperties_Test  extends WebScraper_Base_Test {

    @Test
    public void canGetSoldPropertiesResultCount() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/sold-properties-page.html");
        mockServer.expect(once(), requestTo("https://www.realestate.com.au/sold/in-homebush%2c+2140/list-1?includeSurrounding=false&misc=ex-no-sale-price&activeSort=solddate"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        SearchOptions searchOptions = SearchOptions.builder()
                .suburb("homebush")
                .postcode(2140)
                .build();
        Integer count = webScraper.getSoldPropertiesCount(searchOptions).get();
        assertThat(count, is(943));
        mockServer.verify();
    }

    @Test
    public void canGetSoldPropertiesResultCount_WithBatchNumber() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/sold-properties-page.html");
        mockServer.expect(once(), requestTo("https://www.realestate.com.au/sold/in-homebush%2c+2140/list-2?includeSurrounding=false&misc=ex-no-sale-price&activeSort=solddate"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        SearchOptions searchOptions = SearchOptions.builder()
                .suburb("homebush")
                .postcode(2140)
                .build();
        List<SoldPropertyProfile> soldPropertyProfiles = webScraper.findSoldProperties(searchOptions, 2).get();
        assertThat(soldPropertyProfiles.size(), is(20));

        SoldPropertyProfile soldPropertyProfile = soldPropertyProfiles.get(19);

        assertThat(soldPropertyProfile.getPrice(), is(3850000));
        assertThat(soldPropertyProfile.getAddress(), is("14 The Crescent"));
        assertThat(soldPropertyProfile.getSuburb(), is("Homebush"));
        assertThat(soldPropertyProfile.getPostcode(), is(2140));
        assertThat(soldPropertyProfile.getBed(), is(8));
        assertThat(soldPropertyProfile.getBath(), is(4));
        assertThat(soldPropertyProfile.getCar(), is(4));
        assertThat(soldPropertyProfile.getPropertyCode(), is(123736030));
        assertThat(soldPropertyProfile.getPropertyLink(), is("https://www.realestate.com.au/sold/property-unitblock-nsw-homebush-123736030"));
        mockServer.verify();
    }
}

package org.pyhc.propertyfinder.scraper;


import org.junit.Test;
import org.pyhc.propertyfinder.scraper.model.PropertyProfile;
import org.pyhc.propertyfinder.scraper.model.Query;
import org.pyhc.propertyfinder.scraper.model.RealEstateLink;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class HttpWebScraper_PropertyPage_Test extends HttpWebScraper_Base_Test {

    @Test
    public void canQueryRealEstatePropertyProfile1() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/property-profile-page-1.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-hornsby-124578062"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateLink.builder()
                .propertyLink("/property-apartment-nsw-hornsby-124578062")
                .build();
        PropertyProfile propertyProfile = webScraper.queryProfilePage(realEstateQuery).get();

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

    @Test
    public void canQueryRealEstatePropertyProfile2() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/property-profile-page-2.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-unit-nsw-strathfield-124523042"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateLink.builder()
                .propertyLink("/property-unit-nsw-strathfield-124523042")
                .build();
        PropertyProfile propertyProfile = webScraper.queryProfilePage(realEstateQuery).get();

        assertThat(propertyProfile.getPropertyLink(), is("http://www.realestate.com.au/property-unit-nsw-strathfield-124523042"));
        assertThat(propertyProfile.getAddress(), is("102/5-7 Beresford Road"));
        assertThat(propertyProfile.getBed(), is(3));
        assertThat(propertyProfile.getBath(), is(2));
        assertThat(propertyProfile.getCar(), is(2));
        assertThat(propertyProfile.getSuburb(), is("Strathfield"));
        assertThat(propertyProfile.getPostalCode(), is(2135));
        assertThat(propertyProfile.getPropertyCode(), is("124523042"));
        assertThat(propertyProfile.getPriceEstimate(), is("Auction"));
        mockServer.verify();
    }

    @Test
    public void canQueryRealEstatePropertyProfile3() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/property-profile-page-3.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-apartment-nsw-naremburn-124506658"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateLink.builder()
                .propertyLink("/property-apartment-nsw-naremburn-124506658")
                .build();
        PropertyProfile propertyProfile = webScraper.queryProfilePage(realEstateQuery).get();

        assertThat(propertyProfile.getPropertyLink(), is("http://www.realestate.com.au/property-apartment-nsw-naremburn-124506658"));
        assertThat(propertyProfile.getAddress(), is("9/34 Station Street"));
        assertThat(propertyProfile.getBed(), is(3));
        assertThat(propertyProfile.getBath(), is(2));
        assertThat(propertyProfile.getCar(), is(2));
        assertThat(propertyProfile.getSuburb(), is("Naremburn"));
        assertThat(propertyProfile.getPostalCode(), is(2065));
        assertThat(propertyProfile.getPropertyCode(), is("124506658"));
        assertThat(propertyProfile.getPriceEstimate(), is("1250000"));
        mockServer.verify();
    }

    @Test
    public void canQueryRealEstatePropertyProfile4() throws Exception {
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/property-profile-page-4.html");
        mockServer.expect(once(), requestTo(REALESTATE_DOMAIN + "/property-studio-nsw-st+leonards-124640542"))
                .andRespond(withSuccess(htmlPage, TEXT_HTML));

        Query realEstateQuery = RealEstateLink.builder()
                .propertyLink("/property-studio-nsw-st+leonards-124640542")
                .build();
        PropertyProfile propertyProfile = webScraper.queryProfilePage(realEstateQuery).get();

        assertThat(propertyProfile.getPropertyLink(), is("http://www.realestate.com.au/property-studio-nsw-st+leonards-124640542"));
        assertThat(propertyProfile.getAddress(), is("102/38 Atchison Street"));
        assertThat(propertyProfile.getBed(), is(0));
        assertThat(propertyProfile.getBath(), is(1));
        assertThat(propertyProfile.getCar(), is(1));
        assertThat(propertyProfile.getSuburb(), is("St Leonards"));
        assertThat(propertyProfile.getPostalCode(), is(2065));
        assertThat(propertyProfile.getPropertyCode(), is("124640542"));
        assertThat(propertyProfile.getPriceEstimate(), is("Contact"));
        mockServer.verify();
    }

}
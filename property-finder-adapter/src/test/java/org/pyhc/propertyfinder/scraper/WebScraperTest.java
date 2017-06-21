package org.pyhc.propertyfinder.scraper;


import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pyhc.propertyfinder.scraper.publisher.ScraperResultPublisher;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateQuery;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateSoldQuery;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyLink;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;
import org.pyhc.propertyfinder.settings.SearchLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.hamcrest.CoreMatchers.is;
import static org.jsoup.Jsoup.parse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WebScraperTest {

    private static final SearchLocation HOMEBUSH = SearchLocation.builder().suburbName("homebush").postcode(2140).build();

    @InjectMocks
    private WebScraper webScraper;

    @Mock
    private CompletableRestTemplate completableRestTemplate;

    @Mock
    private ScraperResultPublisher scraperResultPublisher;

    @Test
    public void canQueryRealEstatePropertyProfile1() throws Exception {
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/property-profile-page-1.html");
        when(completableRestTemplate.performGet(queryCaptor.capture())).thenReturn(completedFuture(parse(htmlPage)));

        PropertyProfile propertyProfile = webScraper.queryProfilePage(new PropertyLink("http://www.realestate.com.au/property-apartment-nsw-hornsby-124578062")).get();
        assertThat(queryCaptor.getValue(), is("http://www.realestate.com.au/property-apartment-nsw-hornsby-124578062"));

        assertThat(propertyProfile.getPropertyLink(), is("http://www.realestate.com.au/property-apartment-nsw-hornsby-124578062"));
        assertThat(propertyProfile.getAddress(), is("4/10 Albert Street"));
        assertThat(propertyProfile.getBed(), is(2));
        assertThat(propertyProfile.getBath(), is(1));
        assertThat(propertyProfile.getCar(), is(1));
        assertThat(propertyProfile.getSuburb(), is("Hornsby"));
        assertThat(propertyProfile.getPostalCode(), is(2077));
        assertThat(propertyProfile.getPropertyCode(), is("124578062"));
        assertThat(propertyProfile.getPriceEstimate(), is("640000-680000"));
        verify(completableRestTemplate).performGet(any(String.class));
    }

    @Test
    public void canQueryRealEstatePropertyProfile2() throws Exception {
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/property-profile-page-2.html");
        when(completableRestTemplate.performGet(queryCaptor.capture())).thenReturn(completedFuture(parse(htmlPage)));

        PropertyProfile propertyProfile = webScraper.queryProfilePage(new PropertyLink("http://www.realestate.com.au/property-unit-nsw-strathfield-124523042")).get();
        assertThat(queryCaptor.getValue(), is("http://www.realestate.com.au/property-unit-nsw-strathfield-124523042"));

        assertThat(propertyProfile.getPropertyLink(), is("http://www.realestate.com.au/property-unit-nsw-strathfield-124523042"));
        assertThat(propertyProfile.getAddress(), is("102/5-7 Beresford Road"));
        assertThat(propertyProfile.getBed(), is(3));
        assertThat(propertyProfile.getBath(), is(2));
        assertThat(propertyProfile.getCar(), is(2));
        assertThat(propertyProfile.getSuburb(), is("Strathfield"));
        assertThat(propertyProfile.getPostalCode(), is(2135));
        assertThat(propertyProfile.getPropertyCode(), is("124523042"));
        assertThat(propertyProfile.getPriceEstimate(), is("Auction"));
    }

    @Test
    public void canQueryRealEstatePropertyProfile3() throws Exception {
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/property-profile-page-3.html");
        when(completableRestTemplate.performGet(queryCaptor.capture())).thenReturn(completedFuture(parse(htmlPage)));

        PropertyProfile propertyProfile = webScraper.queryProfilePage(new PropertyLink("http://www.realestate.com.au/property-apartment-nsw-naremburn-124506658")).get();
        assertThat(queryCaptor.getValue(), is("http://www.realestate.com.au/property-apartment-nsw-naremburn-124506658"));

        assertThat(propertyProfile.getPropertyLink(), is("http://www.realestate.com.au/property-apartment-nsw-naremburn-124506658"));
        assertThat(propertyProfile.getAddress(), is("9/34 Station Street"));
        assertThat(propertyProfile.getBed(), is(3));
        assertThat(propertyProfile.getBath(), is(2));
        assertThat(propertyProfile.getCar(), is(2));
        assertThat(propertyProfile.getSuburb(), is("Naremburn"));
        assertThat(propertyProfile.getPostalCode(), is(2065));
        assertThat(propertyProfile.getPropertyCode(), is("124506658"));
        assertThat(propertyProfile.getPriceEstimate(), is("1250000"));
    }

    @Test
    public void canQueryRealEstatePropertyProfile4() throws Exception {
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/property-profile-page-4.html");
        when(completableRestTemplate.performGet(queryCaptor.capture())).thenReturn(completedFuture(parse(htmlPage)));

        PropertyProfile propertyProfile = webScraper.queryProfilePage(new PropertyLink("http://www.realestate.com.au/property-studio-nsw-st+leonards-124640542")).get();
        assertThat(queryCaptor.getValue(), is("http://www.realestate.com.au/property-studio-nsw-st+leonards-124640542"));

        MatcherAssert.assertThat(propertyProfile.getPropertyLink(), is("http://www.realestate.com.au/property-studio-nsw-st+leonards-124640542"));
        MatcherAssert.assertThat(propertyProfile.getAddress(), is("102/38 Atchison Street"));
        MatcherAssert.assertThat(propertyProfile.getBed(), is(0));
        MatcherAssert.assertThat(propertyProfile.getBath(), is(1));
        MatcherAssert.assertThat(propertyProfile.getCar(), is(1));
        MatcherAssert.assertThat(propertyProfile.getSuburb(), is("St Leonards"));
        MatcherAssert.assertThat(propertyProfile.getPostalCode(), is(2065));
        MatcherAssert.assertThat(propertyProfile.getPropertyCode(), is("124640542"));
        MatcherAssert.assertThat(propertyProfile.getPriceEstimate(), is("Contact"));
    }

    @Test
    public void canGetSoldPropertiesResultCount() throws Exception {
        ArgumentCaptor<RealEstateSoldQuery> queryCaptor = ArgumentCaptor.forClass(RealEstateSoldQuery.class);
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/sold-properties-page.html");
        when(completableRestTemplate.performGet(queryCaptor.capture()))
                .thenReturn(completedFuture(parse(htmlPage)));

        Integer count = webScraper.getSoldPropertiesCount(HOMEBUSH).get();
        assertThat(count, is(14497));
        assertThat(queryCaptor.getValue().toString(), is("https://www.realestate.com.au/sold/in-homebush%2c+2140/list-1?includeSurrounding=false&misc=ex-no-sale-price&activeSort=solddate"));
        verify(completableRestTemplate).performGet(any(RealEstateQuery.class));
    }

    @Test
    public void canGetSoldPropertiesProfiles_AndArchive() throws Exception {
        ArgumentCaptor<RealEstateSoldQuery> queryCaptor = ArgumentCaptor.forClass(RealEstateSoldQuery.class);
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/sold-properties-page.html");
        when(completableRestTemplate.performGet(queryCaptor.capture())).thenReturn(completedFuture(parse(htmlPage)));

        webScraper.searchSoldProperties(HOMEBUSH, 2).get();
        assertThat(queryCaptor.getValue().toString(), is("https://www.realestate.com.au/sold/in-homebush%2c+2140/list-2?includeSurrounding=false&misc=ex-no-sale-price&activeSort=solddate"));

        ArgumentCaptor<SoldPropertyProfile> soldPropertyProfileArgumentCaptor = ArgumentCaptor.forClass(SoldPropertyProfile.class);
        verify(scraperResultPublisher, times(20)).publishProfileResult(soldPropertyProfileArgumentCaptor.capture());

        SoldPropertyProfile selectedProfileForTesting = soldPropertyProfileArgumentCaptor.getAllValues().get(19);

        assertThat(selectedProfileForTesting.getPrice(), is(975000));
        assertThat(selectedProfileForTesting.getAddress(), is("10/73 Underwood Road, Homebush"));
        assertThat(selectedProfileForTesting.getSuburb(), is("Homebush"));
        assertThat(selectedProfileForTesting.getPostcode(), is(2140));
        assertThat(selectedProfileForTesting.getBed(), is(4));
        assertThat(selectedProfileForTesting.getBath(), is(2));
        assertThat(selectedProfileForTesting.getCar(), is(2));
        assertThat(selectedProfileForTesting.getPropertyCode(), is(122627082));
        assertThat(selectedProfileForTesting.getPropertyLink(), is("https://www.realestate.com.au/sold/property-townhouse-nsw-homebush-122627082"));
    }

    @Test
    public void canParsePropertyPrice_WhenPriceIsDisplayedAsImage() throws Exception {
        ArgumentCaptor<RealEstateSoldQuery> queryCaptor = ArgumentCaptor.forClass(RealEstateSoldQuery.class);
        String htmlPage = loadPageFromTestResources("src/test/resources/stub/realestate/sold-properties-page-with-price-image.html");
        when(completableRestTemplate.performGet(queryCaptor.capture())).thenReturn(completedFuture(parse(htmlPage)));

        webScraper.searchSoldProperties(HOMEBUSH, 1).get();
        assertThat(queryCaptor.getValue().toString(), is("https://www.realestate.com.au/sold/in-homebush%2c+2140/list-1?includeSurrounding=false&misc=ex-no-sale-price&activeSort=solddate"));

        ArgumentCaptor<SoldPropertyProfile> soldPropertyProfileArgumentCaptor = ArgumentCaptor.forClass(SoldPropertyProfile.class);
        verify(scraperResultPublisher).publishProfileResult(soldPropertyProfileArgumentCaptor.capture());

        List<SoldPropertyProfile> capturedProfiles = soldPropertyProfileArgumentCaptor.getAllValues();
        SoldPropertyProfile soldPropertyProfile = capturedProfiles.get(0);
        assertThat(soldPropertyProfile.getPrice(), is(750000));
    }

    private String loadPageFromTestResources(String resourcePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Files.readAllLines(Paths.get(resourcePath)).forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

}

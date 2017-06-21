package org.pyhc.propertyfinder.scraper;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pyhc.propertyfinder.scraper.publisher.ScraperResultPublisher;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateQuery;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateSoldQuery;
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

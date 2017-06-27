package org.pyhc.propertyfinder.property;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pyhc.propertyfinder.scraper.Scraper;
import org.pyhc.propertyfinder.settings.SuburbDetails;
import org.pyhc.propertyfinder.settings.service.SearchLocationService;

import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PropertyProcessorTest {

    @InjectMocks
    private PropertyProcessor propertyProcessor;

    @Mock
    private Scraper scraper;

    @Mock
    private SearchLocationService searchLocationService;

    @Test
    public void canSearchSoldProperties() throws Exception {
        SuburbDetails suburbDetails = SuburbDetails.builder().suburbName("Homebush").postcode(2140).build();

        when(searchLocationService.getPreviousSearches()).thenReturn(singletonList(suburbDetails));
        when(scraper.getSoldPropertiesCount(suburbDetails)).thenReturn(completedFuture(3));

        propertyProcessor.searchForSoldProperties();

        verify(searchLocationService).getPreviousSearches();
        verify(scraper).getSoldPropertiesCount(suburbDetails);
        verify(scraper).searchSoldProperties(any(), any());
    }

}
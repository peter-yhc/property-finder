package org.pyhc.propertyfinder.property;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pyhc.propertyfinder.scraper.Scraper;
import org.pyhc.propertyfinder.settings.SearchLocation;
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
        SearchLocation searchLocation = SearchLocation.builder().suburbName("Homebush").postcode(2140).build();

        when(searchLocationService.getSavedSearchLocations()).thenReturn(singletonList(searchLocation));
        when(scraper.getSoldPropertiesCount(searchLocation)).thenReturn(completedFuture(3));

        propertyProcessor.searchForSoldProperties();

        verify(searchLocationService).getSavedSearchLocations();
        verify(scraper).getSoldPropertiesCount(searchLocation);
        verify(scraper).searchSoldProperties(any(), any());
    }

}
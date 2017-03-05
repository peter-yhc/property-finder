package org.pyhc.propertyfinder.property;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pyhc.propertyfinder.scraper.Scraper;
import org.pyhc.propertyfinder.scraper.SearchOptions;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyLink;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;

import java.util.concurrent.CompletableFuture;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PropertyProcessorTest {

    @InjectMocks
    private PropertyProcessor propertyProcessor;

    @Mock
    private Scraper scraper;

    @Mock
    private PropertyArchiver propertyArchiver;

    @Test
    public void canSearchSavedLocations_AndArchiveResults() throws Exception {
        SearchOptions searchOptions = SearchOptions.builder().minBeds(2).suburb("Homebush").postalCode(2140).build();
        when(scraper.search(searchOptions)).thenReturn(CompletableFuture.completedFuture(asList(
                new PropertyLink("some link 1"),
                new PropertyLink("some link 2")
        )));
        when(scraper.queryProfilePage(any())).thenReturn(CompletableFuture.completedFuture(TestPropertyProfile.randomProfile()));

        propertyProcessor.searchSavedLocations();

        verify(scraper).search(searchOptions);
        verify(scraper, times(2)).queryProfilePage(any(PropertyLink.class));
        verify(propertyArchiver, times(2)).archive(any(PropertyProfile.class));
    }


}
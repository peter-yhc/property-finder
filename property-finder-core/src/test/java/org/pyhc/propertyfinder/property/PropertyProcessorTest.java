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
import org.pyhc.propertyfinder.settings.model.SavedSearch;
import org.pyhc.propertyfinder.settings.model.SavedSearchRepository;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;
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

    @Mock
    private SavedSearchRepository savedSearchRepository;

    @Test
    public void canSearchSavedLocations_AndArchiveResults() throws Exception {
        SearchOptions searchOptions = SearchOptions.builder().minBeds(2).suburb("Homebush").postcode(2140).build();
        when(scraper.searchCurrentlyListed(searchOptions)).thenReturn(completedFuture(asList(
                new PropertyLink("some link 1"),
                new PropertyLink("some link 2")
        )));
        when(scraper.queryProfilePage(any())).thenReturn(completedFuture(TestPropertyProfile.randomProfile()));

        propertyProcessor.searchCurrentlyListedProperties();

        verify(scraper).searchCurrentlyListed(searchOptions);
        verify(scraper, times(2)).queryProfilePage(any(PropertyLink.class));
        verify(propertyArchiver, times(2)).archiveListedProperty(any(PropertyProfile.class));
    }

    @Test
    public void canSearchSoldProperties() throws Exception {
        SearchOptions searchOptions = SearchOptions.builder().suburb("Homebush").postcode(2140).build();
        SavedSearch savedSearch = SavedSearch.builder().name("Homebush").postcode(2140).build();

        when(savedSearchRepository.findAll()).thenReturn(singletonList(savedSearch));
        when(scraper.getSoldPropertiesCount(searchOptions)).thenReturn(completedFuture(3));

        propertyProcessor.searchSoldProperties();

        verify(savedSearchRepository).findAll();
        verify(scraper).getSoldPropertiesCount(searchOptions);
        verify(scraper).searchSoldProperties(any(), any());
    }

}
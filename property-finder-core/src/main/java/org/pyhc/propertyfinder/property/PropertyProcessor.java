package org.pyhc.propertyfinder.property;

import org.pyhc.propertyfinder.scraper.Scraper;
import org.pyhc.propertyfinder.scraper.SearchOptions;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyLink;
import org.pyhc.propertyfinder.settings.model.SavedSearch;
import org.pyhc.propertyfinder.settings.model.SavedSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

@Service
public class PropertyProcessor implements PropertyProcessorPort {

    @Autowired
    private Scraper scraper;

    @Autowired
    private PropertyArchiver propertyArchiver;

    @Autowired
    private SavedSearchRepository savedSearchRepository;

    public void searchCurrentlyListedProperties() throws ExecutionException, InterruptedException {
        SearchOptions searchOptions = SearchOptions.builder().suburb("Homebush").postcode(2140).minBeds(2).build();
        List<PropertyLink> propertyLinks = scraper.searchCurrentlyListed(searchOptions).get();

        propertyLinks.parallelStream().forEach(propertyLink ->
                scraper.queryProfilePage(propertyLink)
                        .thenAccept(propertyProfile -> propertyArchiver.archiveListedProperty(propertyProfile)));
    }

    @Override
    public void searchSoldProperties() {
        List<SavedSearch> searchLocations = savedSearchRepository.findAll();
        searchLocations.forEach(searchLocation -> {
            SearchOptions searchOptions = convertToSearchParameters(searchLocation);
            scraper.getSoldPropertiesCount(searchOptions).thenAccept(count -> {
                int totalPages = count / 20 + 1;
                IntStream.range(1, totalPages + 1).forEach(scrapePropertiesForPage(searchOptions));
            });
        });
    }

    private static SearchOptions convertToSearchParameters(SavedSearch searchLocation) {
        return SearchOptions.builder().suburb(searchLocation.getName()).postcode(searchLocation.getPostcode()).build();
    }

    private IntConsumer scrapePropertiesForPage(SearchOptions searchOptions) {
        return pageNumber -> scraper.searchSoldProperties(searchOptions, pageNumber);
    }
}

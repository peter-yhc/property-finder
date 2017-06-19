package org.pyhc.propertyfinder.property;

import org.apache.log4j.Logger;
import org.pyhc.propertyfinder.scraper.Scraper;
import org.pyhc.propertyfinder.scraper.SearchParameters;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyLink;
import org.pyhc.propertyfinder.settings.SearchLocation;
import org.pyhc.propertyfinder.settings.SearchLocationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

@Service
public class PropertyProcessor implements PropertyProcessorPort {

    private static final Logger LOG = Logger.getLogger(PropertyProcessor.class);

    @Autowired
    private Scraper scraper;

    @Autowired
    private PropertyArchiver propertyArchiver;

    @Autowired
    private SearchLocationPort searchLocationService;

    @Override
    public void searchSoldProperties() {
        List<SearchLocation> searchLocations = searchLocationService.getSavedSearchLocations();
        searchLocations.forEach(searchLocation -> {
            SearchParameters searchParameters = convertToSearchParameters(searchLocation);
            scraper.getSoldPropertiesCount(searchParameters).thenAccept(count -> {
                int totalPages = count / 20 + 1;
                IntStream.range(1, totalPages + 1).forEach(scrapePropertiesForPage(searchParameters));
            });
        });
    }

    private static SearchParameters convertToSearchParameters(SearchLocation searchLocation) {
        return SearchParameters.builder().suburb(searchLocation.getSuburbName()).postcode(searchLocation.getPostcode()).build();
    }

    private IntConsumer scrapePropertiesForPage(SearchParameters searchParameters) {
        return pageNumber -> {
            try {
                LOG.info("Processing page " + pageNumber);
                scraper.searchSoldProperties(searchParameters, pageNumber).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        };
    }
}

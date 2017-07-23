package org.pyhc.propertyfinder.property;

import org.apache.log4j.Logger;
import org.pyhc.propertyfinder.scraper.Scraper;
import org.pyhc.propertyfinder.suburb.SuburbDetails;
import org.pyhc.propertyfinder.suburb.SearchLocationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

@Service
public class PropertyProcessor implements PropertyProcessorPort {

    private static final Logger LOG = Logger.getLogger(PropertyProcessor.class);

    private Scraper scraper;
    private SearchLocationPort searchLocationService;

    @Autowired
    public PropertyProcessor(Scraper scraper, SearchLocationPort searchLocationService) {
        this.scraper = scraper;
        this.searchLocationService = searchLocationService;
    }

    @Override
    public void searchForSoldProperties() {
        List<SuburbDetails> suburbDetails = searchLocationService.getPreviousSearches();
        suburbDetails.forEach(searchLocation -> {
            scraper.getSoldPropertiesCount(searchLocation).thenAccept(count -> {
                int totalPages = count / 20 + 1;
                IntStream.range(1, totalPages + 1).forEach(scrapePropertiesForPage(searchLocation));
            });
        });
    }

    private IntConsumer scrapePropertiesForPage(SuburbDetails suburbDetails) {
        return pageNumber -> {
            try {
                LOG.info("Processing page " + pageNumber);
                scraper.searchSoldProperties(suburbDetails, pageNumber).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        };
    }
}

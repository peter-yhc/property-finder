package org.pyhc.propertyfinder.property;

import org.pyhc.propertyfinder.scraper.Scraper;
import org.pyhc.propertyfinder.scraper.SearchOptions;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PropertyProcessor {

    @Autowired
    private Scraper scraper;

    @Autowired
    private PropertyArchiver propertyArchiver;

    public void searchCurrentlyListedProperties() throws ExecutionException, InterruptedException {
        SearchOptions searchOptions = SearchOptions.builder().suburb("Homebush").postcode(2140).minBeds(2).build();
        List<PropertyLink> propertyLinks = scraper.searchCurrentlyListed(searchOptions).get();

        propertyLinks.parallelStream().forEach(propertyLink ->
                scraper.queryProfilePage(propertyLink)
                        .thenAccept(propertyProfile -> propertyArchiver.archiveCurrentlyListed(propertyProfile)));
    }

}

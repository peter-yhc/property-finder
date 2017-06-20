package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.realestate.result.PropertyLink;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.pyhc.propertyfinder.settings.SearchLocation;

import java.util.concurrent.CompletableFuture;

public interface Scraper {

    @Deprecated
    CompletableFuture<PropertyProfile> queryProfilePage(PropertyLink propertyLink);

    CompletableFuture<Integer> getSoldPropertiesCount(SearchLocation searchLocation);

    CompletableFuture<Void> searchSoldProperties(SearchLocation searchLocation, Integer batchNumber);
}

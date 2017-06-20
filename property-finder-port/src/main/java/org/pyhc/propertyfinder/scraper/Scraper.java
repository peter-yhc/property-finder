package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.realestate.result.PropertyLink;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Scraper {

    @Deprecated
    CompletableFuture<PropertyProfile> queryProfilePage(PropertyLink propertyLink);

    CompletableFuture<Integer> getSoldPropertiesCount(SearchParameters searchParameters);

    CompletableFuture<Void> searchSoldProperties(SearchParameters searchParameters, Integer batchNumber);
}

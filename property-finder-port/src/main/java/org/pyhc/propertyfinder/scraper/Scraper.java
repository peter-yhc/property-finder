package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.realestate.result.PropertyLink;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Scraper {

    static final Integer BATCH_SIZE = 20;

    CompletableFuture<List<PropertyLink>> searchCurrentlyListed(SearchOptions searchOptions);

    CompletableFuture<PropertyProfile> queryProfilePage(PropertyLink propertyLink);

    CompletableFuture<Integer> getSoldPropertiesCount(SearchOptions searchOptions);

    CompletableFuture<List<SoldPropertyProfile>> findSoldProperties(SearchOptions searchOptions, Integer batchNumber);
}

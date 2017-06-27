package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.realestate.result.PropertyLink;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.pyhc.propertyfinder.settings.SuburbDetails;

import java.util.concurrent.CompletableFuture;

public interface Scraper {

    @Deprecated
    CompletableFuture<PropertyProfile> queryProfilePage(PropertyLink propertyLink);

    CompletableFuture<Integer> getSoldPropertiesCount(SuburbDetails suburbDetails);

    CompletableFuture<Void> searchSoldProperties(SuburbDetails suburbDetails, Integer batchNumber);
}

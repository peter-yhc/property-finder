package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.model.PropertyProfile;
import org.pyhc.propertyfinder.scraper.model.Query;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface WebScraper {

    CompletableFuture<List<Query>> search(Query query);

    CompletableFuture<PropertyProfile> queryProfilePage(Query query);

}

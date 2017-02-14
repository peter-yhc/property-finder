package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.pyhc.propertyfinder.scraper.realestate.query.Query;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface WebScraper {

    CompletableFuture<List<Query>> search(Query query);

    CompletableFuture<PropertyProfile> queryProfilePage(Query query);

}

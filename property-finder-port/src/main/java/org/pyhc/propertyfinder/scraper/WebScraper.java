package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.model.Query;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface WebScraper {

    CompletableFuture<List<Query>> search(Query query);

    void queryProfilePage(Query query);

}

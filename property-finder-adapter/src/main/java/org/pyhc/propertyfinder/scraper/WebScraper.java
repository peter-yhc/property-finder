package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateQuery;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.pyhc.propertyfinder.scraper.realestate.query.Query;
import org.pyhc.propertyfinder.scraper.realestate.RealEstateProfileParser;
import org.pyhc.propertyfinder.scraper.realestate.RealEstateSearchParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class WebScraper implements Scraper {

    @Autowired
    private CompletableRestTemplate completableRestTemplate;

    @Override
    public CompletableFuture<List<Query>> search(SearchOptions searchOptions) {
        RealEstateQuery realEstateQuery = RealEstateQuery.fromSearchOptions(searchOptions);
        return search(realEstateQuery);
    }

    private CompletableFuture<List<Query>> search(Query query) {
        return completableRestTemplate.performGet(query)
                .thenApply(RealEstateSearchParser::parse)
                .thenApply(searchResult -> {
                    List<Query> queries = searchResult.getProfileLinks().stream().collect(Collectors.toList());
                    if (searchResult.hasNextPageLink()) {
                        search(searchResult.getNextPageLink()).thenAccept(queries::addAll).join();
                    }
                    return queries;
                });
    }

    @Override
    public CompletableFuture<PropertyProfile> queryProfilePage(Query query) {
        return completableRestTemplate.performGet(query)
                .thenApply(document -> RealEstateProfileParser.parse(document, query.toString()));
    }

}
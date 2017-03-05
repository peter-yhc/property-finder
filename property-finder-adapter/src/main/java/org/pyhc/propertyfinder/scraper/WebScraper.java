package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.realestate.RealEstateProfileParser;
import org.pyhc.propertyfinder.scraper.realestate.RealEstateSearchParser;
import org.pyhc.propertyfinder.scraper.realestate.query.Query;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateQuery;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyLink;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@Service
public class WebScraper implements Scraper {

    @Autowired
    private CompletableRestTemplate completableRestTemplate;

    @Override
    public CompletableFuture<List<PropertyLink>> search(SearchOptions searchOptions) {
        RealEstateQuery realEstateQuery = RealEstateQuery.fromSearchOptions(searchOptions);
        return search(realEstateQuery)
                .thenApply(results -> results
                        .stream()
                        .map(query -> new PropertyLink(query.toString()))
                        .collect(toList())
                );
    }

    private CompletableFuture<List<Query>> search(Query query) {
        return completableRestTemplate.performGet(query)
                .thenApply(RealEstateSearchParser::parse)
                .thenApply(searchResult -> {
                    List<Query> queries = searchResult.getProfileLinks().stream().collect(toList());
                    if (searchResult.hasNextPageLink()) {
                        search(searchResult.getNextPageLink()).thenAccept(queries::addAll).join();
                    }
                    return queries;
                });
    }

    @Override
    public CompletableFuture<PropertyProfile> queryProfilePage(PropertyLink propertyLink) {
        return completableRestTemplate.performGet(propertyLink.getLink())
                .thenApply(document -> RealEstateProfileParser.parse(document, propertyLink.getLink()));
    }

}
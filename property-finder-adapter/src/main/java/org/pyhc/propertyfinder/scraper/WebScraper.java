package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.publisher.ScraperResultPublisher;
import org.pyhc.propertyfinder.scraper.realestate.RealEstateProfileParser;
import org.pyhc.propertyfinder.scraper.realestate.RealEstateSearchParser;
import org.pyhc.propertyfinder.scraper.realestate.RealEstateSoldPropertiesParser;
import org.pyhc.propertyfinder.scraper.realestate.query.Query;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateQuery;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateSoldQuery;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyLink;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@Service
public class WebScraper implements Scraper {

    @Autowired
    private CompletableRestTemplate completableRestTemplate;

    @Autowired
    private ScraperResultPublisher scraperResultPublisher;

    @Override
    public CompletableFuture<Integer> getSoldPropertiesCount(SearchParameters searchParameters) {
        RealEstateSoldQuery realEstateSoldQuery = RealEstateSoldQuery.fromSearchOptions(searchParameters);
        return completableRestTemplate.performGet(realEstateSoldQuery)
                .thenApply(RealEstateSoldPropertiesParser::getSoldPropertiesCount);
    }

    @Override
    public CompletableFuture<Void> searchSoldProperties(SearchParameters searchParameters, Integer page) {
        RealEstateSoldQuery realEstateSoldQuery = RealEstateSoldQuery.fromSearchOptions(searchParameters, page);
        return completableRestTemplate.performGet(realEstateSoldQuery)
                .thenApply(document -> RealEstateSoldPropertiesParser.parseSoldProperties(document, searchParameters))
                .thenAccept(profiles -> profiles.forEach(profile -> scraperResultPublisher.publishProfileResult(profile)));
    }

    @Override
    @Deprecated
    public CompletableFuture<PropertyProfile> queryProfilePage(PropertyLink propertyLink) {
        return completableRestTemplate.performGet(propertyLink.getLink())
                .thenApply(document -> RealEstateProfileParser.parse(document, propertyLink.getLink()));
    }

}

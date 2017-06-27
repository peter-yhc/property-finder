package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.publisher.ScraperResultPublisher;
import org.pyhc.propertyfinder.scraper.realestate.RealEstateProfileParser;
import org.pyhc.propertyfinder.scraper.realestate.RealEstateSoldPropertiesParser;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateSoldQuery;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyLink;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.pyhc.propertyfinder.settings.SuburbDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class WebScraper implements Scraper {

    private CompletableRestTemplate completableRestTemplate;
    private ScraperResultPublisher scraperResultPublisher;

    @Autowired
    public WebScraper(CompletableRestTemplate completableRestTemplate, ScraperResultPublisher scraperResultPublisher) {
        this.completableRestTemplate = completableRestTemplate;
        this.scraperResultPublisher = scraperResultPublisher;
    }

    @Override
    public CompletableFuture<Integer> getSoldPropertiesCount(SuburbDetails suburbDetails) {
        RealEstateSoldQuery realEstateSoldQuery = RealEstateSoldQuery.fromSearchOptions(suburbDetails);
        return completableRestTemplate.performGet(realEstateSoldQuery)
                .thenApply(RealEstateSoldPropertiesParser::getSoldPropertiesCount);
    }

    @Override
    public CompletableFuture<Void> searchSoldProperties(SuburbDetails suburbDetails, Integer page) {
        RealEstateSoldQuery realEstateSoldQuery = RealEstateSoldQuery.fromSearchOptions(suburbDetails, page);
        return completableRestTemplate.performGet(realEstateSoldQuery)
                .thenApply(document -> RealEstateSoldPropertiesParser.parseSoldProperties(document, suburbDetails))
                .thenAccept(profiles -> profiles.forEach(profile -> scraperResultPublisher.publishProfileResult(profile)));
    }

    @Override
    @Deprecated
    public CompletableFuture<PropertyProfile> queryProfilePage(PropertyLink propertyLink) {
        return completableRestTemplate.performGet(propertyLink.getLink())
                .thenApply(document -> RealEstateProfileParser.parse(document, propertyLink.getLink()));
    }

}

package org.pyhc.propertyfinder.scraper;

import org.jsoup.Jsoup;
import org.pyhc.propertyfinder.archive.PropertyArchiverPort;
import org.pyhc.propertyfinder.scraper.model.PropertyProfile;
import org.pyhc.propertyfinder.scraper.model.Query;
import org.pyhc.propertyfinder.scraper.model.SearchResult;
import org.pyhc.propertyfinder.scraper.realestate.RealEstateProfileParser;
import org.pyhc.propertyfinder.scraper.realestate.RealEstateSearchParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class HttpWebScraper implements WebScraper {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertyArchiverPort propertyArchiverPort;

    @Override
    public void query(Query query) {
        String rawPageHtml = restTemplate.getForObject(URI.create(query.toString()), String.class);

        SearchResult searchResult = RealEstateSearchParser.parse(Jsoup.parse(rawPageHtml));
        if (searchResult.hasNextPageLink()) {
            query(searchResult.getNextPageLink());
        }
        searchResult.getProfileLinks().forEach(this::queryProfilePage);
    }

    @Override
    public void queryProfilePage(Query query) {
        String rawPageHtml = restTemplate.getForObject(URI.create(query.toString()), String.class);
        if (rawPageHtml == null) {
            return;
        }

        PropertyProfile profile = RealEstateProfileParser.parse(Jsoup.parse(rawPageHtml), query.toString());
        propertyArchiverPort.archive(profile);
    }

}
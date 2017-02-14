package org.pyhc.propertyfinder.archive;

import org.pyhc.propertyfinder.scraper.WebScraper;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyArchiver {

    @Autowired
    private WebScraper webScraper;

    public void archive() {
        webScraper.search(RealEstateQuery.builder().suburb("Homebush").build());
    }

}

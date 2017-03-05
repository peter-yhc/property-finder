package org.pyhc.propertyfinder.property;

import org.pyhc.propertyfinder.scraper.Scraper;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyArchiver {

    @Autowired
    private Scraper scraper;

    public void archive() {
//        scraper.search(RealEstateQuery.builder().suburb("Homebush").build());
    }

}

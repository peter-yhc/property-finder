package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.model.PropertyProfile;
import org.pyhc.propertyfinder.scraper.model.Query;

import java.util.List;

public interface WebScraper {

    List<PropertyProfile> query(Query query);

    PropertyProfile queryProfilePage(Query query);
}

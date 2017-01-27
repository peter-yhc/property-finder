package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.model.PropertyResult;
import org.pyhc.propertyfinder.scraper.model.Query;

import java.util.List;

public interface WebScraper {

    List<PropertyResult> query(Query query);

}

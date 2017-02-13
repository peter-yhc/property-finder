package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.model.Query;

public interface WebScraper {

    void query(Query query);

    void queryProfilePage(Query query);

}

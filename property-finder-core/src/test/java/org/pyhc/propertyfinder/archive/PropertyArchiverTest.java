package org.pyhc.propertyfinder.archive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.CoreTest;
import org.pyhc.propertyfinder.scraper.WebScraper;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@CoreTest
public class PropertyArchiverTest {

    @MockBean
    private WebScraper webScraper;

    @Autowired
    private PropertyArchiver propertyArchiver;

    @Test
    public void canQueuePropertyArchiveTask_ifWebScraperReturnsMatch() throws Exception {
        propertyArchiver.archive();

        RealEstateQuery homebushQuery = RealEstateQuery.builder().suburb("Homebush").build();
        verify(webScraper).search(homebushQuery);
    }
}
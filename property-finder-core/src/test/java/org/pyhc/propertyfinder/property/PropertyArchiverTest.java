package org.pyhc.propertyfinder.property;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.CoreConfiguration;
import org.pyhc.propertyfinder.scraper.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoreConfiguration.class})
@Ignore
public class PropertyArchiverTest {

    @MockBean
    private Scraper scraper;

    @Autowired
    private PropertyArchiver propertyArchiver;

}
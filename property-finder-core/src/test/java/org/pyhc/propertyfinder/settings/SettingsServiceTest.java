package org.pyhc.propertyfinder.settings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.DatabaseConfiguration;
import org.pyhc.propertyfinder.configuration.MongoExecutionListener;
import org.pyhc.propertyfinder.persistence.SavedSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DatabaseConfiguration.class, SettingsServiceTest.ContextConfiguration.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, MongoExecutionListener.class})
public class SettingsServiceTest {

    @Autowired
    private SavedSearchRepository savedSearchRepository;

    @Autowired
    private SettingsService settingsService;

    @Test
    public void canRetrieveSearchLocationsFromDatabase() throws Exception {
        settingsService.addSavedLocation(SearchLocation.builder().suburbName("Some suburb").state("some state").postcode(1111).build());

        assertThat(savedSearchRepository.findAll().size(), is(1));
    }

    @Configuration
    static class ContextConfiguration {

        @Bean
        public SettingsService settingsService() {
            return new SettingsService();
        }
    }
}
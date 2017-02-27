package org.pyhc.propertyfinder.settings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.DatabaseConfiguration;
import org.pyhc.propertyfinder.configuration.MongoExecutionListener;
import org.pyhc.propertyfinder.persistence.SavedSearch;
import org.pyhc.propertyfinder.persistence.SavedSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

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
    public void canSaveSearchLocations() throws Exception {
        settingsService.addSavedLocation(SearchLocation.builder().suburbName("Some suburb").state("some state").postcode(1111).build());

        assertThat(savedSearchRepository.findAll().size(), is(1));
    }

    @Test
    public void shouldNotSaveSameSearchLocationMoreThanOnce() throws Exception {
        SearchLocation searchLocation = SearchLocation.builder().suburbName("Some suburb").state("some state").postcode(1111).build();
        settingsService.addSavedLocation(searchLocation);
        settingsService.addSavedLocation(searchLocation);
        settingsService.addSavedLocation(searchLocation);
        settingsService.addSavedLocation(searchLocation);

        assertThat(savedSearchRepository.findAll().size(), is(1));
    }

    @Test
    public void canRetrieveSearchLocations() throws Exception {
        savedSearchRepository.save(SavedSearch.builder().name("A++ Suburb").state("Best State").postcode(1234).build());

        List<SearchLocation> savedSearches = settingsService.getSavedSearches();

        assertThat(savedSearches.size(), is(1));

        SearchLocation searchLocation = savedSearches.get(0);
        assertThat(searchLocation.getSuburbName(), is("A++ Suburb"));
        assertThat(searchLocation.getState(), is("Best State"));
        assertThat(searchLocation.getPostcode(), is(1234));
    }

    @Test
    public void canRemoveSavedSearchLocation() throws Exception {
        savedSearchRepository.save(SavedSearch.builder().name("A++ Suburb").state("Best State").postcode(1234).build());
        assertThat(savedSearchRepository.findAll().size(), is(1));

        settingsService.removeSavedLocation(
                SearchLocation.builder().suburbName("A++ Suburb").state("Best State").postcode(1234).build()
        );

        assertThat(savedSearchRepository.findAll().size(), is(0));
    }

    @Configuration
    static class ContextConfiguration {

        @Bean
        public SettingsService settingsService() {
            return new SettingsService();
        }
    }
}
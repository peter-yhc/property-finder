package org.pyhc.propertyfinder.settings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.DatabaseConfiguration;
import org.pyhc.propertyfinder.configuration.MongoExecutionListener;
import org.pyhc.propertyfinder.settings.model.SavedSearch;
import org.pyhc.propertyfinder.settings.model.SavedSearchRepository;
import org.pyhc.propertyfinder.settings.service.SavedSearchService;
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
@SpringBootTest(classes = {DatabaseConfiguration.class, SavedSearchServiceTest.ContextConfiguration.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, MongoExecutionListener.class})
public class SavedSearchServiceTest {

    @Autowired
    private SavedSearchRepository savedSearchRepository;

    @Autowired
    private SavedSearchService savedSearchService;

    @Test
    public void canSaveSearchLocations() throws Exception {
        savedSearchService.addSavedLocation(SearchLocation.builder().suburbName("Some suburb").state("some state").postcode(1111).build());

        assertThat(savedSearchRepository.findAll().size(), is(1));
    }

    @Test
    public void shouldNotSaveSameSearchLocationMoreThanOnce() throws Exception {
        SearchLocation searchLocation = SearchLocation.builder().suburbName("Some suburb").state("some state").postcode(1111).build();
        savedSearchService.addSavedLocation(searchLocation);
        savedSearchService.addSavedLocation(searchLocation);
        savedSearchService.addSavedLocation(searchLocation);
        savedSearchService.addSavedLocation(searchLocation);

        assertThat(savedSearchRepository.findAll().size(), is(1));
    }

    @Test
    public void canRetrieveSearchLocations() throws Exception {
        savedSearchRepository.save(SavedSearch.builder().name("A++ Suburb").state("Best State").postcode(1234).build());

        List<SearchLocation> savedSearches = savedSearchService.getSavedSearches();

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

        savedSearchService.removeSavedLocation(
                SearchLocation.builder().suburbName("A++ Suburb").state("Best State").postcode(1234).build()
        );

        assertThat(savedSearchRepository.findAll().size(), is(0));
    }

    @Configuration
    static class ContextConfiguration {

        @Bean
        public SavedSearchService settingsService() {
            return new SavedSearchService();
        }
    }
}
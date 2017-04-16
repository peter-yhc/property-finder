package org.pyhc.propertyfinder.settings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.DatabaseConfiguration;
import org.pyhc.propertyfinder.configuration.MongoExecutionListener;
import org.pyhc.propertyfinder.settings.model.SavedSearch;
import org.pyhc.propertyfinder.settings.model.SavedSearchRepository;
import org.pyhc.propertyfinder.settings.model.Suburb;
import org.pyhc.propertyfinder.settings.model.SuburbRepository;
import org.pyhc.propertyfinder.settings.service.SearchLocationService;
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
@SpringBootTest(classes = {DatabaseConfiguration.class, SearchLocationServiceTest.ContextConfiguration.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, MongoExecutionListener.class})
public class SearchLocationServiceTest {

    @Autowired
    private SavedSearchRepository savedSearchRepository;

    @Autowired
    private SuburbRepository suburbRepository;

    @Autowired
    private SearchLocationService searchLocationService;

    @Test
    public void canSaveSearchLocations() throws Exception {
        searchLocationService.addSavedLocation(SearchLocation.builder().suburbName("Some suburb").state("some state").postcode(1111).build());

        assertThat(savedSearchRepository.findAll().size(), is(1));
    }

    @Test
    public void shouldNotSaveSameSearchLocationMoreThanOnce() throws Exception {
        SearchLocation searchLocation = SearchLocation.builder().suburbName("Some suburb").state("some state").postcode(1111).build();
        searchLocationService.addSavedLocation(searchLocation);
        searchLocationService.addSavedLocation(searchLocation);
        searchLocationService.addSavedLocation(searchLocation);
        searchLocationService.addSavedLocation(searchLocation);

        assertThat(savedSearchRepository.findAll().size(), is(1));
    }

    @Test
    public void canRetrieveSearchLocations() throws Exception {
        savedSearchRepository.save(SavedSearch.builder().name("A++ Suburb").state("Best State").postcode(1234).build());

        List<SearchLocation> savedSearches = searchLocationService.getSavedSearchLocations();

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

        searchLocationService.removeSavedLocation(
                SearchLocation.builder().suburbName("A++ Suburb").state("Best State").postcode(1234).build()
        );

        assertThat(savedSearchRepository.findAll().size(), is(0));
    }

    @Test
    public void getSearchableLocations() throws Exception {
        suburbRepository.save(Suburb.builder().name("A1").state("B1").postcode(1).build());
        suburbRepository.save(Suburb.builder().name("A2").state("B2").postcode(2).build());
        suburbRepository.save(Suburb.builder().name("A3").state("B3").postcode(3).build());
        suburbRepository.save(Suburb.builder().name("A4").state("B4").postcode(4).build());
        suburbRepository.save(Suburb.builder().name("A5").state("B5").postcode(5).build());
        suburbRepository.save(Suburb.builder().name("A6").state("B6").postcode(6).build());

        List<SearchLocation> searchableLocations = searchLocationService.getSearchableLocations();
        assertThat(searchableLocations.size(), is(6));
        assertThat(searchableLocations.get(0).getSuburbName(), is("A1"));
        assertThat(searchableLocations.get(1).getSuburbName(), is("A2"));
        assertThat(searchableLocations.get(2).getSuburbName(), is("A3"));
        assertThat(searchableLocations.get(3).getSuburbName(), is("A4"));
        assertThat(searchableLocations.get(4).getSuburbName(), is("A5"));
        assertThat(searchableLocations.get(5).getSuburbName(), is("A6"));
    }

    @Configuration
    static class ContextConfiguration {

        @Bean
        public SearchLocationService searchLocationSettingsService() {
            return new SearchLocationService();
        }
    }
}
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

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
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
    public void canSave_andRetreive_Location() throws Exception {
        searchLocationService.addSavedLocation(SearchLocation.builder().suburbName("Toowoomba").state("QLD").postcode(4350).build());

        SavedSearch savedSearch = savedSearchRepository.findAll().get(0);
        assertThat(savedSearch.getName(), is("Toowoomba"));
        assertThat(savedSearch.getState(), is("QLD"));
        assertThat(savedSearch.getPostcode(), is(4350));
        assertThat(savedSearch.getUuid(), is(not(nullValue())));
    }

    @Test
    public void shouldNotSaveSameSearchLocationMoreThanOnce() throws Exception {
        SearchLocation searchLocation = SearchLocation.builder().suburbName("Toowoomba").state("QLD").postcode(4350).build();
        searchLocationService.addSavedLocation(searchLocation);
        searchLocationService.addSavedLocation(searchLocation);
        searchLocationService.addSavedLocation(searchLocation);
        searchLocationService.addSavedLocation(searchLocation);

        assertThat(savedSearchRepository.findAll().size(), is(1));
    }

    @Test
    public void canRemoveSavedSearchLocation() throws Exception {
        SavedSearch savedSearch = SavedSearch.builder().name("Toowoomba").state("QLD").postcode(4350).build();
        savedSearch = savedSearchRepository.save(savedSearch);

        searchLocationService.removeSavedLocation(savedSearch.getUuid());

        assertThat(savedSearchRepository.findAll().size(), is(0));
    }

    @Test
    public void canGetSearchableLocations() throws Exception {
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
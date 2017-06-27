package org.pyhc.propertyfinder.settings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.DatabaseConfiguration;
import org.pyhc.propertyfinder.configuration.MongoExecutionListener;
import org.pyhc.propertyfinder.model.PreviousSearch;
import org.pyhc.propertyfinder.model.PreviousSearchRepository;
import org.pyhc.propertyfinder.model.Suburb;
import org.pyhc.propertyfinder.model.SuburbRepository;
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
    private PreviousSearchRepository previousSearchRepository;

    @Autowired
    private SuburbRepository suburbRepository;

    @Autowired
    private SearchLocationService searchLocationService;

    @Test
    public void canSave_andRetreive_Location() throws Exception {
        searchLocationService.addSavedLocation(SearchLocation.builder().suburbName("Toowoomba").state("QLD").postcode(4350).build());

        PreviousSearch previousSearch = previousSearchRepository.findAll().get(0);
        assertThat(previousSearch.getName(), is("Toowoomba"));
        assertThat(previousSearch.getState(), is("QLD"));
        assertThat(previousSearch.getPostcode(), is(4350));
        assertThat(previousSearch.getUuid(), is(not(nullValue())));
    }

    @Test
    public void shouldNotSaveSameSearchLocationMoreThanOnce() throws Exception {
        SearchLocation searchLocation = SearchLocation.builder().suburbName("Toowoomba").state("QLD").postcode(4350).build();
        searchLocationService.addSavedLocation(searchLocation);
        searchLocationService.addSavedLocation(searchLocation);
        searchLocationService.addSavedLocation(searchLocation);
        searchLocationService.addSavedLocation(searchLocation);

        assertThat(previousSearchRepository.findAll().size(), is(1));
    }

    @Test
    public void canRemoveSavedSearchLocation() throws Exception {
        PreviousSearch previousSearch = PreviousSearch.builder().name("Toowoomba").state("QLD").postcode(4350).build();
        previousSearch = previousSearchRepository.save(previousSearch);

        searchLocationService.removeSavedLocation(previousSearch.getUuid());

        assertThat(previousSearchRepository.findAll().size(), is(0));
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
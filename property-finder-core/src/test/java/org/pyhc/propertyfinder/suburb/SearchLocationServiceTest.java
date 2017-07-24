package org.pyhc.propertyfinder.suburb;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pyhc.propertyfinder.model.PreviousSearch;
import org.pyhc.propertyfinder.model.PreviousSearchRepository;
import org.pyhc.propertyfinder.model.Suburb;
import org.pyhc.propertyfinder.model.SuburbRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class SearchLocationServiceTest {

    @InjectMocks
    private SearchLocationService searchLocationService;

    @Mock
    private PreviousSearchRepository previousSearchRepository;

    @Mock
    private SuburbRepository suburbRepository;

    @Test
    public void canRecordNewSearchLocation() throws Exception {
        when(previousSearchRepository.findByNameAndStateAndPostcode(any(), any(), any()))
                .thenReturn(Optional.empty());

        SuburbDetails suburbDetails = SuburbDetails.builder().suburbName("Toowoomba").state("QLD").postcode(4350).build();
        searchLocationService.recordSearch(suburbDetails);

        ArgumentCaptor<PreviousSearch> captor = ArgumentCaptor.forClass(PreviousSearch.class);
        verify(previousSearchRepository).save(captor.capture());

        PreviousSearch previousSearch = captor.getValue();
        assertThat(previousSearch.getName(), is("Toowoomba"));
        assertThat(previousSearch.getState(), is("QLD"));
        assertThat(previousSearch.getPostcode(), is(4350));
        assertThat(previousSearch.getUuid(), is(not(nullValue())));
    }

    @Test
    public void shouldNotRecordSameSearchLocationMoreThanOnce() throws Exception {
        when(previousSearchRepository.findByNameAndStateAndPostcode("Toowoomba", "QLD", 4350))
                .thenReturn(Optional.of(PreviousSearch.builder().build()));

        SuburbDetails suburbDetails = SuburbDetails.builder().suburbName("Toowoomba").state("QLD").postcode(4350).build();
        searchLocationService.recordSearch(suburbDetails);

        verify(previousSearchRepository, times(0)).save(any(PreviousSearch.class));
    }

    @Test
    public void canRemoveSavedSearchLocation() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(previousSearchRepository.findByNameAndStateAndPostcode("Toowoomba", "QLD", 4350))
                .thenReturn(Optional.of(PreviousSearch.builder().uuid(uuid).build()));

        SuburbDetails suburbDetails = SuburbDetails.builder().suburbName("Toowoomba").state("QLD").postcode(4350).build();
        searchLocationService.removeSavedSearch(suburbDetails);

        verify(previousSearchRepository).delete(any(PreviousSearch.class));
    }

    @Test
    public void canGetSearchableLocations() throws Exception {
        Pageable pageable = new PageRequest(0, 20);
        when(suburbRepository.findAll(pageable)).thenReturn(
                new PageImpl<>(singletonList(Suburb.builder()
                        .name("Wollongong")
                        .postcode(1000)
                        .state("NSW")
                        .build()))
        );

        Page<SuburbDetails> searchableLocations = searchLocationService.getSearchableLocations(pageable);
        verify(suburbRepository).findAll(pageable);
        assertThat(searchableLocations.getTotalElements(), is(1L));

        SuburbDetails suburbDetails = searchableLocations.getContent().get(0);
        assertThat(suburbDetails.getSuburbName(), is("Wollongong"));
        assertThat(suburbDetails.getState(), is("NSW"));
        assertThat(suburbDetails.getPostcode(), is(1000));
    }

    @Test
    public void canGetPageableResults() throws Exception {
        Pageable pageable = new PageRequest(0, 20);
        when(suburbRepository.findAll(pageable)).thenReturn(
                new PageImpl<>(Stream.generate(randomSuburb()).limit(20).collect(toList()), pageable, 250)
        );

        Page<SuburbDetails> searchableLocations = searchLocationService.getSearchableLocations(pageable);
        assertThat(searchableLocations.getTotalElements(), is(250L));
        assertThat(searchableLocations.getTotalPages(), is(13));
        assertThat(searchableLocations.isFirst(), is(true));
        assertThat(searchableLocations.hasNext(), is(true));
    }

    private static Supplier<Suburb> randomSuburb() {
        return () -> Suburb.builder()
                .name(randomAlphabetic(10))
                .postcode(nextInt())
                .state(randomAlphabetic(3))
                .build();
    }
}

package org.pyhc.propertyfinder.suburb;

import org.pyhc.propertyfinder.model.PreviousSearch;
import org.pyhc.propertyfinder.model.PreviousSearchRepository;
import org.pyhc.propertyfinder.model.SuburbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class SearchLocationService implements SearchLocationPort {

    @Autowired
    private PreviousSearchRepository previousSearchRepository;

    @Autowired
    private SuburbRepository suburbRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SuburbDetails> getPreviousSearches() {
        return previousSearchRepository.findAll()
            .stream()
            .map(DataObjectConverter::convertToSearchLocation)
            .collect(toList());
    }

    @Override
    public Page<SuburbDetails> getSearchableLocations(Pageable pageable) {
        return suburbRepository.findAll(pageable)
            .map(DataObjectConverter::convertToSearchLocation);
    }

    @Override
    @Transactional
    public void recordSearch(SuburbDetails suburbDetails) {
        Optional<PreviousSearch> savedSearchOptional = previousSearchRepository.findByNameAndStateAndPostcode(
            suburbDetails.getSuburbName(),
            suburbDetails.getState(),
            suburbDetails.getPostcode()
        );
        if (!savedSearchOptional.isPresent()) {
            PreviousSearch previousSearch = PreviousSearch.builder()
                    .name(suburbDetails.getSuburbName())
                    .state(suburbDetails.getState())
                    .postcode(suburbDetails.getPostcode())
                    .uuid(UUID.randomUUID())
                    .build();
            previousSearchRepository.save(previousSearch);
        }
    }

    @Override
    @Transactional
    public void removeSavedSearch(SuburbDetails suburbDetails) {
        previousSearchRepository.findByNameAndStateAndPostcode(
            suburbDetails.getSuburbName(),
            suburbDetails.getState(),
            suburbDetails.getPostcode()
        ).ifPresent((previousSearch) -> previousSearchRepository.delete(previousSearch));
    }
}

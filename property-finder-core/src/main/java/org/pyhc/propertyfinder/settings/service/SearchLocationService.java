package org.pyhc.propertyfinder.settings.service;

import org.pyhc.propertyfinder.model.PreviousSearch;
import org.pyhc.propertyfinder.settings.DataObjectConverter;
import org.pyhc.propertyfinder.settings.SuburbDetails;
import org.pyhc.propertyfinder.settings.SearchLocationPort;
import org.pyhc.propertyfinder.model.PreviousSearchRepository;
import org.pyhc.propertyfinder.model.SuburbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.pyhc.propertyfinder.settings.DataObjectConverter.convertToSavedSearch;

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
    public List<SuburbDetails> getSearchableLocations() {
        return suburbRepository.findAll()
                .stream()
                .map(DataObjectConverter::convertToSearchLocation)
                .collect(toList());
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
            previousSearchRepository.save(convertToSavedSearch(suburbDetails));
        }
    }

    @Override
    @Transactional
    public void removeSavedLocation(UUID savedLocationId) {
        previousSearchRepository.findByUuid(savedLocationId)
                .ifPresent((previousSearch) -> previousSearchRepository.delete(previousSearch));
    }
}

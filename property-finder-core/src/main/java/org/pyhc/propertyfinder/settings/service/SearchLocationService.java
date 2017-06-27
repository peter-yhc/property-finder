package org.pyhc.propertyfinder.settings.service;

import org.pyhc.propertyfinder.settings.DataObjectConverter;
import org.pyhc.propertyfinder.settings.SearchLocation;
import org.pyhc.propertyfinder.settings.SearchLocationPort;
import org.pyhc.propertyfinder.model.SavedSearch;
import org.pyhc.propertyfinder.model.SavedSearchRepository;
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
    private SavedSearchRepository savedSearchRepository;

    @Autowired
    private SuburbRepository suburbRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SearchLocation> getSavedSearchLocations() {
        return savedSearchRepository.findAll()
                .stream()
                .map(DataObjectConverter::convertToSearchLocation)
                .collect(toList());
    }

    @Override
    public List<SearchLocation> getSearchableLocations() {
        return suburbRepository.findAll()
                .stream()
                .map(DataObjectConverter::convertToSearchLocation)
                .collect(toList());
    }

    @Override
    @Transactional
    public void addSavedLocation(SearchLocation searchLocation) {
        Optional<SavedSearch> savedSearchOptional = savedSearchRepository.findByNameAndStateAndPostcode(
                searchLocation.getSuburbName(),
                searchLocation.getState(),
                searchLocation.getPostcode()
        );
        if (!savedSearchOptional.isPresent()) {
            savedSearchRepository.save(convertToSavedSearch(searchLocation));
        }
    }

    @Override
    @Transactional
    public void removeSavedLocation(UUID savedLocationId) {
        savedSearchRepository.findByUuid(savedLocationId)
                .ifPresent((savedSearch) -> savedSearchRepository.delete(savedSearch));
    }
}

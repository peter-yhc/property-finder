package org.pyhc.propertyfinder.settings.service;

import org.pyhc.propertyfinder.settings.DataObjectConverter;
import org.pyhc.propertyfinder.settings.SearchLocation;
import org.pyhc.propertyfinder.settings.SettingsPort;
import org.pyhc.propertyfinder.settings.model.SavedSearch;
import org.pyhc.propertyfinder.settings.model.SavedSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.pyhc.propertyfinder.settings.DataObjectConverter.convertToSavedSearch;

@Service
public class SavedSearchService implements SettingsPort {

    @Autowired
    private SavedSearchRepository savedSearchRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SearchLocation> getSavedSearches() {
        return savedSearchRepository.findAll()
                .stream()
                .map(DataObjectConverter::convertToSearchLocation)
                .collect(toList());
    }

    @Override
    public List<SearchLocation> getSearchableLocations() {
        return null;
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
    public void removeSavedLocation(SearchLocation searchLocation) {
        savedSearchRepository.findByNameAndStateAndPostcode(
                searchLocation.getSuburbName(),
                searchLocation.getState(),
                searchLocation.getPostcode()
        ).ifPresent((savedSearch) -> savedSearchRepository.delete(savedSearch));
    }
}

package org.pyhc.propertyfinder.settings;

import org.pyhc.propertyfinder.persistence.SavedSearch;
import org.pyhc.propertyfinder.persistence.SavedSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.pyhc.propertyfinder.settings.SettingsDataObjectConverter.convertToSavedSearch;

@Service
public class SettingsService implements SettingsPort {

    @Autowired
    private SavedSearchRepository savedSearchRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SearchLocation> getSavedSearches() {
        return savedSearchRepository.findAll()
                .stream()
                .map(SettingsDataObjectConverter::convertToSearchLocation)
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

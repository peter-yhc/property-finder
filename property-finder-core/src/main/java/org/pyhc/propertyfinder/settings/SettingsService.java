package org.pyhc.propertyfinder.settings;

import org.pyhc.propertyfinder.persistence.SavedSearch;
import org.pyhc.propertyfinder.persistence.SavedSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsService implements SettingsPort {

    @Autowired
    private SavedSearchRepository savedSearchRepository;

    @Override
    public List<SearchLocation> getSavedSearches() {
        return null;
    }

    @Override
    public List<SearchLocation> getSearchableLocations() {
        return null;
    }

    @Override
    public void addSavedLocation(SearchLocation searchLocation) {
        SavedSearch savedSearch = SavedSearch.builder()
                .name(searchLocation.getSuburbName())
                .state(searchLocation.getState())
                .postcode(searchLocation.getPostcode())
                .build();

        savedSearchRepository.save(savedSearch);
    }

    @Override
    public void removeSavedLocation(SearchLocation searchLocation) {

    }
}

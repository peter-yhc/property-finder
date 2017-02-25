package org.pyhc.propertyfinder.settings;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsService implements SettingsPort {

    @Override
    public List<SearchLocation> getSavedSearches() {
        return null;
    }

    @Override
    public List<SearchLocation> getSearchableLocations() {
        return null;
    }

    @Override
    public void removeSavedLocation(SearchLocation searchLocation) {

    }
}

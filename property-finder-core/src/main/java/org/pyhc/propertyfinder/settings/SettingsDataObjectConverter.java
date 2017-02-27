package org.pyhc.propertyfinder.settings;

import org.pyhc.propertyfinder.persistence.SavedSearch;

public interface SettingsDataObjectConverter {
    static SearchLocation convertToSearchLocation(SavedSearch savedSearch) {
        return SearchLocation.builder()
                .suburbName(savedSearch.getName())
                .state(savedSearch.getState())
                .postcode(savedSearch.getPostcode())
                .build();
    }

    static SavedSearch convertToSavedSearch(SearchLocation searchLocation) {
        return SavedSearch.builder()
                .name(searchLocation.getSuburbName())
                .state(searchLocation.getState())
                .postcode(searchLocation.getPostcode())
                .build();
    }
}
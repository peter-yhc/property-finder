package org.pyhc.propertyfinder.settings;

import org.pyhc.propertyfinder.settings.model.SavedSearch;
import org.pyhc.propertyfinder.settings.model.Suburb;

public interface DataObjectConverter {
    static SearchLocation convertToSearchLocation(SavedSearch savedSearch) {
        return SearchLocation.builder()
                .suburbName(savedSearch.getName())
                .state(savedSearch.getState())
                .postcode(savedSearch.getPostcode())
                .uuid(savedSearch.getUuid())
                .build();
    }
    static SearchLocation convertToSearchLocation(Suburb suburb) {
        return SearchLocation.builder()
                .suburbName(suburb.getName())
                .state(suburb.getState())
                .postcode(suburb.getPostcode())
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
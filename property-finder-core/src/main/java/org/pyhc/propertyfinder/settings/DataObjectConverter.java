package org.pyhc.propertyfinder.settings;

import org.pyhc.propertyfinder.model.PreviousSearch;
import org.pyhc.propertyfinder.model.Suburb;

public interface DataObjectConverter {
    static SearchLocation convertToSearchLocation(PreviousSearch previousSearch) {
        return SearchLocation.builder()
                .suburbName(previousSearch.getName())
                .state(previousSearch.getState())
                .postcode(previousSearch.getPostcode())
                .uuid(previousSearch.getUuid())
                .build();
    }
    static SearchLocation convertToSearchLocation(Suburb suburb) {
        return SearchLocation.builder()
                .suburbName(suburb.getName())
                .state(suburb.getState())
                .postcode(suburb.getPostcode())
                .build();
    }

    static PreviousSearch convertToSavedSearch(SearchLocation searchLocation) {
        return PreviousSearch.builder()
                .name(searchLocation.getSuburbName())
                .state(searchLocation.getState())
                .postcode(searchLocation.getPostcode())
                .build();
    }
}
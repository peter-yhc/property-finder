package org.pyhc.propertyfinder.settings;

import org.pyhc.propertyfinder.model.PreviousSearch;
import org.pyhc.propertyfinder.model.Suburb;

public interface DataObjectConverter {
    static SuburbDetails convertToSearchLocation(PreviousSearch previousSearch) {
        return SuburbDetails.builder()
                .suburbName(previousSearch.getName())
                .state(previousSearch.getState())
                .postcode(previousSearch.getPostcode())
                .uuid(previousSearch.getUuid())
                .build();
    }
    static SuburbDetails convertToSearchLocation(Suburb suburb) {
        return SuburbDetails.builder()
                .suburbName(suburb.getName())
                .state(suburb.getState())
                .postcode(suburb.getPostcode())
                .build();
    }

    static PreviousSearch convertToSavedSearch(SuburbDetails suburbDetails) {
        return PreviousSearch.builder()
                .name(suburbDetails.getSuburbName())
                .state(suburbDetails.getState())
                .postcode(suburbDetails.getPostcode())
                .build();
    }
}
package org.pyhc.propertyfinder.suburb;

import org.pyhc.propertyfinder.model.PreviousSearch;
import org.pyhc.propertyfinder.model.Suburb;

public interface DataObjectConverter {
    static SuburbDetails convertToSearchLocation(PreviousSearch previousSearch) {
        return SuburbDetails.builder()
                .name(previousSearch.getName())
                .state(previousSearch.getState())
                .postcode(previousSearch.getPostcode())
                .uuid(previousSearch.getUuid())
                .build();
    }

    static SuburbDetails convertToSearchLocation(Suburb suburb) {
        return SuburbDetails.builder()
                .name(suburb.getName())
                .state(suburb.getState())
                .postcode(suburb.getPostcode())
                .build();
    }

}

package org.pyhc.propertyfinder.settings;

import java.util.List;

public interface SearchLocationPort {

    List<SearchLocation> getSavedSearchLocations();

    List<SearchLocation> getSearchableLocations();

    void addSavedLocation(SearchLocation searchLocation);

    void removeSavedLocation(SearchLocation searchLocation);
}

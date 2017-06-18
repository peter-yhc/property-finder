package org.pyhc.propertyfinder.settings;

import java.util.List;
import java.util.UUID;

public interface SearchLocationPort {

    List<SearchLocation> getSavedSearchLocations();

    List<SearchLocation> getSearchableLocations();

    void addSavedLocation(SearchLocation searchLocation);

    void removeSavedLocation(UUID savedLocationId);
}

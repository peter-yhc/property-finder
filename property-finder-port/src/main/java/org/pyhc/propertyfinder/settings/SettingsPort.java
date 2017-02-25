package org.pyhc.propertyfinder.settings;

import java.util.List;

public interface SettingsPort {

    List<SearchLocation> getSavedSearches();

    List<SearchLocation> getSearchableLocations();

    void removeSavedLocation(SearchLocation searchLocation);
}

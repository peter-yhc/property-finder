package org.pyhc.propertyfinder.settings;

import java.util.List;

public interface SettingsPort {

    List<SearchLocation> getSavedSearches();

    List<SearchLocation> getSearchableLocations();

    void addSavedLocation(SearchLocation searchLocation);

    void removeSavedLocation(SearchLocation searchLocation);
}

package org.pyhc.propertyfinder.suburb;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SearchLocationPort {

    List<SuburbDetails> getPreviousSearches();

    Page<SuburbDetails> getSearchableLocations(Pageable pageable);

    void recordSearch(SuburbDetails suburbDetails);

    void removeSavedSearch(SuburbDetails suburbDetails);
}

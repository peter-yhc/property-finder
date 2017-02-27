package org.pyhc.propertyfinder.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedSearchRepository extends MongoRepository<SavedSearch, String> {

    Optional<SavedSearch> findByNameAndStateAndPostcode(String name, String state, Integer postcode);

}

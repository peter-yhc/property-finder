package org.pyhc.propertyfinder.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SavedSearchRepository extends MongoRepository<SavedSearch, String> {

    Optional<SavedSearch> findByNameAndStateAndPostcode(String name, String state, Integer postcode);

    Optional<SavedSearch> findByUuid(UUID uuid);

}

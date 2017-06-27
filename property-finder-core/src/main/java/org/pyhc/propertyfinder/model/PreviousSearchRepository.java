package org.pyhc.propertyfinder.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PreviousSearchRepository extends MongoRepository<PreviousSearch, String> {

    Optional<PreviousSearch> findByNameAndStateAndPostcode(String name, String state, Integer postcode);

    Optional<PreviousSearch> findByUuid(UUID uuid);

}

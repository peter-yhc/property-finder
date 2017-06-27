package org.pyhc.propertyfinder.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyProfileRepository extends MongoRepository<PropertyProfile, String> {

    Optional<PropertyProfile> findOneByPropertyCode(Integer propertyCode);

}

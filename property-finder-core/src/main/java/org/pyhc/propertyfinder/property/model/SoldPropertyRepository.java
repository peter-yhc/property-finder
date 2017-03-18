package org.pyhc.propertyfinder.property.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SoldPropertyRepository extends MongoRepository<SoldProperty, String> {

    Optional<SoldProperty> findOneByPropertyCode(Integer propertyCode);

}

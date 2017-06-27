package org.pyhc.propertyfinder.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuburbRepository extends MongoRepository<Suburb, String> {
}

package org.pyhc.propertyfinder.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedSearchRepository extends MongoRepository<SavedSearch, String> {
}

package org.pyhc.realestate.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuburbRepository extends JpaRepository<Property, Long> {
}

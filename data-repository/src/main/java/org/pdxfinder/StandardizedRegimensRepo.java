package org.pdxfinder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardizedRegimensRepo extends JpaRepository<StandardizedRegimens, Integer> {

}
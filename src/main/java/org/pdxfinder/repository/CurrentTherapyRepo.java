package org.pdxfinder.repository;

import org.pdxfinder.domain.CurrentTherapy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentTherapyRepo extends JpaRepository<CurrentTherapy, Integer> {

}
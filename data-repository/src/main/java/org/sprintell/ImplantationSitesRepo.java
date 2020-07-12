package org.sprintell;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.pdxfinder.ImplantationSites;

@Repository
public interface ImplantationSitesRepo extends JpaRepository<ImplantationSites, Integer> {

}
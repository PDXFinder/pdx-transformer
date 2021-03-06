package org.pdxfinder.data.model.repository;

import org.pdxfinder.data.model.TumorGradeStageTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TumorGradeStageTypesRepo extends JpaRepository<TumorGradeStageTypes, Integer> {

}

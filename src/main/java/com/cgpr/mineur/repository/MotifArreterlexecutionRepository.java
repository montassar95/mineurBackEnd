package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.CauseLiberation;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.MotifArreterlexecution;
import com.cgpr.mineur.models.TitreAccusation;

@Repository
public interface MotifArreterlexecutionRepository extends CrudRepository<MotifArreterlexecution, Long> {
	
	 
	 public List<MotifArreterlexecution> findAllByOrderByIdAsc();

}
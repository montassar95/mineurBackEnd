package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.CauseLiberation;
import com.cgpr.mineur.models.CauseMutation;
import com.cgpr.mineur.models.Liberation;


 

@Repository
public interface CauseLiberationRepository extends CrudRepository<CauseLiberation, Long> {
	 
	
	 public List<CauseLiberation> findAllByOrderByIdAsc();
}


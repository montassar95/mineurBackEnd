package com.cgpr.mineur.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Liberation;
import com.cgpr.mineur.models.LiberationId;


 

@Repository
public interface LiberationRepository extends CrudRepository<Liberation, LiberationId> {
	 
	
	 
}


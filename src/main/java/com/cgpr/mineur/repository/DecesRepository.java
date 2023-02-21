package com.cgpr.mineur.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Deces;


 

@Repository
public interface DecesRepository extends CrudRepository<Deces, Long> {
	
	 
}


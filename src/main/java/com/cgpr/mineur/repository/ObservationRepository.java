package com.cgpr.mineur.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.Observation;


 

@Repository
public interface ObservationRepository extends   CrudRepository<Observation, DocumentId> {
	 
	 @Query("select count(c) from Observation c where c.affaire.affaireId.idEnfant= ?1")
	    long countObservation(String idEnfant); 
}


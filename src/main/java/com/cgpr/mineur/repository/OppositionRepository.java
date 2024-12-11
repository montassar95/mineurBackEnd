package com.cgpr.mineur.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.AppelParquet;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.Opposition;


 

@Repository
public interface OppositionRepository extends   CrudRepository<Opposition, DocumentId> {
	 
	 @Query("select count(c) from Opposition c where c.affaire.affaireId.idEnfant= ?1")
	    long countOpposition(String idEnfant); 
}


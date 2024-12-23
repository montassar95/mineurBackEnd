package com.cgpr.mineur.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.AppelEnfant;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.Revue;


 

@Repository
public interface RevueRepository extends   CrudRepository<Revue, DocumentId> {
	 
	 @Query("select count(c) from Revue c where c.affaire.affaireId.idEnfant= ?1")
	    long countRevue(String idEnfant); 
}


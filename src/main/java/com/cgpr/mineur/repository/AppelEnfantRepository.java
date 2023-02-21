package com.cgpr.mineur.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.AppelEnfant;
import com.cgpr.mineur.models.DocumentId;


 

@Repository
public interface AppelEnfantRepository extends   CrudRepository<AppelEnfant, DocumentId> {
	 
	 @Query("select count(c) from AppelEnfant c where c.affaire.affaireId.idEnfant= ?1")
	    long countAppelEnfant(String idEnfant); 
}


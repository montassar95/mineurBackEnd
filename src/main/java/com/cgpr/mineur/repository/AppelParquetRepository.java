package com.cgpr.mineur.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.AppelParquet;
import com.cgpr.mineur.models.DocumentId;


 

@Repository
public interface AppelParquetRepository extends   CrudRepository<AppelParquet, DocumentId> {
	 
	 @Query("select count(c) from AppelParquet c where c.affaire.affaireId.idEnfant= ?1")
	    long countAppelParquet(String idEnfant); 
}


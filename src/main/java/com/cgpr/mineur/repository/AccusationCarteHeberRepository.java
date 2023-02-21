package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.AccusationCarteHeberId;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.TitreAccusation;

 
 

@Repository
public interface AccusationCarteHeberRepository extends CrudRepository<AccusationCarteHeber, AccusationCarteHeberId> {
	
	@Query("SELECT a.titreAccusation FROM AccusationCarteHeber a WHERE a.carteHeber.documentId = ?1 ")
    List<TitreAccusation>  getTitreAccusationbyDocument  (DocumentId documentId);
 
	
	 @Query("SELECT a  FROM AccusationCarteHeber a WHERE a.carteHeber.documentId = ?1  ")
	 List<AccusationCarteHeber> findByCarteHeber(DocumentId documentId);
	 
	 
}


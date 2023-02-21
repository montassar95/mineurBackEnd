package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.AccusationCarteDepotId;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.TitreAccusation;

 
 

@Repository
public interface AccusationCarteDepotRepository extends CrudRepository<AccusationCarteDepot, AccusationCarteDepotId> {
	
	@Query("SELECT a.titreAccusation FROM AccusationCarteDepot a WHERE a.carteDepot.documentId = ?1 ")
    List<TitreAccusation>  getTitreAccusationbyDocument  (DocumentId documentId);
 
	
	 @Query("SELECT a  FROM AccusationCarteDepot a WHERE a.carteDepot.documentId = ?1  ")
	 List<AccusationCarteDepot> findByCarteDepot(DocumentId documentId);
}


package com.cgpr.mineur.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.AccusationCarteRecupId;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.TitreAccusation;

 
 
                                                                                               
@Repository
public interface AccusationCarteRecupRepository extends CrudRepository<AccusationCarteRecup, AccusationCarteRecupId> {
	
	@Query("SELECT a.titreAccusation FROM AccusationCarteRecup a WHERE a.carteRecup.documentId = ?1 ")
     List<TitreAccusation>  getTitreAccusationbyDocument (DocumentId documentId); 
 
	 @Query("SELECT max(a.dateDebut)  FROM AccusationCarteRecup a WHERE a.carteRecup.documentId.idEnfant = ?1  and a.carteRecup.documentId.numOrdinalArrestation = ?2" )
	 Date  getDateDebutPunition  (long idEnfant,long numOrdinalArrestation );

	 
	 @Query("SELECT a  FROM AccusationCarteRecup a WHERE a.carteRecup.documentId = ?1 order by a.dateDebut ")
	 List<AccusationCarteRecup> findByCarteRecup(DocumentId documentId);
}


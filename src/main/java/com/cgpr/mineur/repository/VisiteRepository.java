package com.cgpr.mineur.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Visite;

 
 

@Repository
public interface VisiteRepository extends CrudRepository<Visite, Long> {
	
	 @Query("select v from Visite v where v.enfant.id = ?1 and v.anneeVisite = ?2 and v.moisVisite = ?3")
	 Optional <Visite> findbyEnfantandDate(String idEnfant,int anneeVisite,int moisVisite); 
	 
	 
//	 @Query("SELECT COALESCE(v.nbrVisite, 0) FROM Visite v WHERE v.enfant.id = ?1 AND v.anneeVisite = ?2 AND v.moisVisite = ?3")
//	 Optional <Visite> countByEnfantandDate(String idEnfant, int anneeVisite, int moisVisite);


	 
}


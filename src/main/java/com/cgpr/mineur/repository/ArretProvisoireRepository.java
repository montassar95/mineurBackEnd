package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.ArretProvisoireId;
import com.cgpr.mineur.models.DocumentId;


 

@Repository
public interface ArretProvisoireRepository extends CrudRepository<ArretProvisoire, ArretProvisoireId> {
	
 	@Query("SELECT a  FROM ArretProvisoire a WHERE a.carteRecup.documentId.idEnfant = ?1  and a.carteRecup.documentId.numOrdinalArrestation = ?2"
 			+ " and a.carteRecup.documentId.numOrdinalDoc = (select min(c.documentId.numOrdinalDoc) from CarteRecup c where c.documentId.idEnfant = ?1 and c.documentId.numOrdinalArrestation = ?2)"
 			 )
     List<ArretProvisoire>  getArretProvisoirebyArrestation  (String idEnfant,long numOrdinalArrestation );
 	
 	
	 @Query("SELECT a  FROM ArretProvisoire a WHERE a.carteRecup.documentId = ?1 order by a.dateDebut ")
	 List<ArretProvisoire> findArretProvisoireByCarteRecup(DocumentId documentId);

}


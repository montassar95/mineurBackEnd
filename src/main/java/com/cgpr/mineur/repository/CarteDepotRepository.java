package com.cgpr.mineur.repository;


 
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.DocumentId;
 
 
 
@Repository
public interface CarteDepotRepository  extends CrudRepository<CarteDepot, DocumentId> {
	 
	 @Query("select count(c) from CarteDepot c where c.affaire.affaireId.idEnfant= ?1")
	    long countCarteDepot(String idEnfant); 
}
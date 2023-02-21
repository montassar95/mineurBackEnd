package com.cgpr.mineur.repository;


 
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.TitreAccusation;
 
 
 
@Repository
public interface CarteHeberRepository  extends CrudRepository<CarteHeber, DocumentId> {
	 
	 @Query("select count(c) from CarteHeber c where c.affaire.affaireId.idEnfant= ?1")
	    long countCarteHeber(String idEnfant); 
	 
	 
}
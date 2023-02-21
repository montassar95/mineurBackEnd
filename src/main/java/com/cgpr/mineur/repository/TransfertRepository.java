package com.cgpr.mineur.repository;


 
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.Transfert;
 
 
 
@Repository
public interface TransfertRepository  extends CrudRepository<Transfert, DocumentId> {
	 
	 @Query("select count(c) from Transfert c where c.affaire.affaireId.idEnfant= ?1")
	    long countTransfert(String idEnfant); 
}
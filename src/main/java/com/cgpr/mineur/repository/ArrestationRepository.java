package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.ArrestationId;


 

@Repository
public interface ArrestationRepository extends CrudRepository<Arrestation, ArrestationId> {
	
	List<Arrestation> findByArrestationIdIdEnfant(String idEnfant);

    List<Arrestation> findByArrestationIdNumOrdinale(long numOrdinale);
    
    
    @Query("SELECT a FROM Arrestation a WHERE a.enfant.id = ?1 and a.arrestationId.numOrdinale = (select max(a.arrestationId.numOrdinale) from Arrestation a where a.enfant.id = ?1) ")
    Arrestation findByIdEnfantAndStatut0 (String idEnfant);
   
    		
    		
    @Query("select count(a) from Arrestation a where a.arrestationId.idEnfant = ?1")
    long countByEnfant(String idEnfant);

    
    @Query("SELECT a FROM Arrestation a WHERE a.arrestationId.idEnfant = ?1 order by arrestationId.numOrdinale desc")
    List<Arrestation> findByIdEnfant (String idEnfant);
    
    
    @Query("SELECT a FROM Arrestation a ")
    List<Arrestation> findAllEnfantWithExistArr ( );
}


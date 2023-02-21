package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.TitreAccusation;

 
 

@Repository
public interface TitreAccusationRepository extends CrudRepository<TitreAccusation, Long> {
	
 
	
	 @Query("SELECT a FROM TitreAccusation a WHERE a.typeAffaire.id = ?1 order by a.id ")
	 List<TitreAccusation> findTitreAccusationByIdTypeAffaire (long id);
	
	 public List<TitreAccusation> findAllByOrderByIdAsc();
}


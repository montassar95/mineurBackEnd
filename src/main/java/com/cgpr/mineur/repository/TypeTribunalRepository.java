package com.cgpr.mineur.repository;

import java.util.List;
 
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Gouvernorat;
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.models.TypeJuge;
import com.cgpr.mineur.models.TypeTribunal;


 

@Repository
public interface TypeTribunalRepository extends CrudRepository<TypeTribunal, Long> {
	
	 public List<TypeTribunal> findAllByOrderByIdAsc();
	 
}


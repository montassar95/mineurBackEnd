package com.cgpr.mineur.repository;

import java.util.List;
 
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.ClassePenale;
import com.cgpr.mineur.models.Gouvernorat;
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.models.TypeJuge;


 

@Repository
public interface GouvernoratRepository extends CrudRepository<Gouvernorat, Long> {
	
	public List<Gouvernorat> findAllByOrderByIdAsc();
	 
}


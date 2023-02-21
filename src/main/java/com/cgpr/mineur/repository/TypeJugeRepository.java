package com.cgpr.mineur.repository;

import java.util.List;
 
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.models.TypeJuge;


 

@Repository
public interface TypeJugeRepository extends CrudRepository<TypeJuge, Long> {
	
	 public List<TypeJuge> findAllByOrderByIdAsc();
}

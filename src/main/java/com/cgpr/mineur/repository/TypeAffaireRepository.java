package com.cgpr.mineur.repository;


 
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Delegation;
import com.cgpr.mineur.models.TypeAffaire;


 

@Repository
public interface TypeAffaireRepository extends CrudRepository<TypeAffaire, Long> {
	
	public List<TypeAffaire> findAllByOrderByIdAsc();
}


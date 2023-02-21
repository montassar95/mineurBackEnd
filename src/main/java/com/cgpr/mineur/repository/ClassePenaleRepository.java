package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.ClassePenale;
import com.cgpr.mineur.models.SituationFamiliale;


 

@Repository
public interface ClassePenaleRepository extends CrudRepository<ClassePenale, Long> {
	 
	public List<ClassePenale> findAllByOrderByIdAsc();
	 
}


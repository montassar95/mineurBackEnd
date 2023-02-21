package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Nationalite;
import com.cgpr.mineur.models.NiveauEducatif;

 
 

@Repository
public interface NiveauEducatifRepository extends CrudRepository<NiveauEducatif, Long> {

	public List<NiveauEducatif> findAllByOrderByIdAsc();
}


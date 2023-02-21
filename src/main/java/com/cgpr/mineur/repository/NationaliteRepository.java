package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
 
import com.cgpr.mineur.models.Nationalite;

 
 

@Repository
public interface NationaliteRepository extends CrudRepository<Nationalite, Long> {

	public List<Nationalite> findAllByOrderByIdAsc();
}


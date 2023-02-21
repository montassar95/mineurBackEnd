package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.CauseDeces;
import com.cgpr.mineur.models.LieuDeces;


 

@Repository
public interface CauseDecesRepository extends CrudRepository<CauseDeces, Long> {
	
	 public List<CauseDeces> findAllByOrderByIdAsc();
	 
}


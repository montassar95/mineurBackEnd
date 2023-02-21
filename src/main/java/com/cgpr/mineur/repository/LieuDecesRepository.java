package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.LieuDeces;
import com.cgpr.mineur.models.MotifArreterlexecution;

 
 

@Repository
public interface LieuDecesRepository extends CrudRepository<LieuDeces, Long> {
	
	 public List<LieuDeces> findAllByOrderByIdAsc();

}


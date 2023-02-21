package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.CommentEchapper;

 
 

@Repository
public interface CommentEchapperRepository extends CrudRepository<CommentEchapper, Long> {
	
 
	 public List<CommentEchapper> findAllByOrderByIdAsc();
	 

}


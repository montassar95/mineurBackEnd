package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.CommentTrouver;

 
 

@Repository
public interface CommentTrouverRepository extends CrudRepository<CommentTrouver, Long> {
	
	 public List<CommentTrouver> findAllByOrderByIdAsc();
	
	 

}


package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.CauseMutation;
import com.cgpr.mineur.models.CommentEchapper;


 

@Repository
public interface CauseMutationRepository extends CrudRepository<CauseMutation, Long> {

	 public List<CauseMutation> findAllByOrderByIdAsc();
}


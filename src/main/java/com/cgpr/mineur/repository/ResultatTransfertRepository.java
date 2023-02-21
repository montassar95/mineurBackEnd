package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
 
import com.cgpr.mineur.models.NiveauEducatif;
import com.cgpr.mineur.models.ResultatTransfert;
import com.cgpr.mineur.models.Tribunal;

 
 

@Repository
public interface ResultatTransfertRepository  extends CrudRepository<ResultatTransfert, Long> {

	
	 public List<ResultatTransfert> findAllByOrderByIdAsc();
}


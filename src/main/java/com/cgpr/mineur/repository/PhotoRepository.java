package com.cgpr.mineur.repository;

 

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.AccusationCarteHeberId;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.Photo;
import com.cgpr.mineur.models.PhotoId;
import com.cgpr.mineur.models.TitreAccusation;

 
 

@Repository
public interface PhotoRepository extends CrudRepository<Photo , PhotoId> {
	 
	 
	 
}


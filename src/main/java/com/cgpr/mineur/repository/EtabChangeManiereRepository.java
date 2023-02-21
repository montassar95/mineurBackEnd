package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.EtabChangeManiere;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Residence;

 
 

@Repository
public interface EtabChangeManiereRepository extends CrudRepository<EtabChangeManiere, String> {
	 

}


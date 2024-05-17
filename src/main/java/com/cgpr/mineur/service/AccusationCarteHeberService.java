package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.AccusationCarteHeberId;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.TitreAccusation;

 
 


public interface AccusationCarteHeberService   {
	
	 
	public  AccusationCarteHeber  save(  AccusationCarteHeber accusationCarteHeber);

	 
	public  List<TitreAccusation>  findTitreAccusationbyCarteHeber( CarteHeber carteHeber) ;
	 
	 
}


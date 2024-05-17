package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.TitreAccusation;

 
 


public interface AccusationCarteDepotService   {
	

	 
	public  AccusationCarteDepot  save(  AccusationCarteDepot accusationCarteDepot);

	 
	public  List<TitreAccusation>  findTitreAccusationbyCarteDepot( CarteDepot carteDepot);
 
}


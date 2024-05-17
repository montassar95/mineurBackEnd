package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Liberation;
import com.cgpr.mineur.models.LiberationId;
import com.cgpr.mineur.repository.LiberationRepository;
import com.cgpr.mineur.service.LiberationService;


 

@Service 
public class LiberationServiceImpl  implements  LiberationService{

	
	@Autowired
	private LiberationRepository liberationRepository;

	 
	@Override
	public  List<Liberation>  listLiberation() {
		return   (List<Liberation>) liberationRepository.findAll() ;
	}
 

	@Override
	public Liberation  getLiberationById(  String idEnfant,  long numOrdinale) {

		LiberationId liberationId = new LiberationId(idEnfant, numOrdinale);
		Optional<Liberation> cData = liberationRepository.findById(liberationId);
		if (cData.isPresent()) {
			return   cData.get() ;
		} else {
			return   null ;
		}
	}
	
	 
}


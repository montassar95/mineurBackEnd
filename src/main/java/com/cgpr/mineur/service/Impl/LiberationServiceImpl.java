package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.GouvernoratConverter;
import com.cgpr.mineur.converter.LiberationConverter;
import com.cgpr.mineur.dto.LiberationDto;
import com.cgpr.mineur.models.Gouvernorat;
import com.cgpr.mineur.models.Liberation;
import com.cgpr.mineur.models.LiberationId;
import com.cgpr.mineur.repository.LiberationRepository;
import com.cgpr.mineur.service.LiberationService;


 

@Service 
public class LiberationServiceImpl  implements  LiberationService{

	
	@Autowired
	private LiberationRepository liberationRepository;

	 
	@Override
	public  List<LiberationDto>  listLiberation() {
		
		List<Liberation> list =  (List<Liberation>) liberationRepository.findAll();
		return  list. stream().map(LiberationConverter::entityToDto).collect(Collectors.toList())  ;
		
		 
	}
 

	@Override
	public LiberationDto  getLiberationById(  String idEnfant,  long numOrdinale) {

		LiberationId liberationId = new LiberationId(idEnfant, numOrdinale);
		Optional<Liberation> cData = liberationRepository.findById(liberationId);
		if (cData.isPresent()) {
			return   LiberationConverter .entityToDto(cData.get()) ;
		} else {
			return   null ;
		}
	}
	
	 
}


package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.CauseDecesConverter;
import com.cgpr.mineur.converter.CauseLiberationConverter;
import com.cgpr.mineur.dto.CauseLiberationDto;
import com.cgpr.mineur.models.CauseDeces;
import com.cgpr.mineur.models.CauseLiberation;
import com.cgpr.mineur.repository.CauseLiberationRepository;
import com.cgpr.mineur.service.CauseLiberationService;


 

@Service
public class CauseLiberationServiceImpl implements  CauseLiberationService {

	

	@Autowired
	private CauseLiberationRepository causeLiberationRepository;

	 
	 

	@Override
	public  List<CauseLiberationDto>  listCauseLiberation() {
	 
		List<CauseLiberation > list =  causeLiberationRepository.findAllByOrderByIdAsc() ;
		return  list.stream().map(CauseLiberationConverter::entityToDto).collect(Collectors.toList())  ;
	}


	@Override
	public  CauseLiberationDto  getTypeAffaireById(  long id) {
		Optional<CauseLiberation> causeLiberation = causeLiberationRepository.findById(id);
		if (causeLiberation.isPresent()) {
			return  CauseLiberationConverter.entityToDto(causeLiberation.get())   ;
		} else {
			return   null ;
		}
	}

	@Override
	public  CauseLiberationDto  save( CauseLiberationDto causeLiberationDto) {

		try {
			CauseLiberation causeLiberation =	causeLiberationRepository.save(CauseLiberationConverter.dtoToEntity(causeLiberationDto)) ;
			return    CauseLiberationConverter.entityToDto(causeLiberation) ;
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public  CauseLiberationDto  update( CauseLiberationDto causeLiberationDto) {
		try {

			CauseLiberation causeLiberation =	causeLiberationRepository.save(CauseLiberationConverter.dtoToEntity(causeLiberationDto)) ;
			return    CauseLiberationConverter.entityToDto(causeLiberation) ;
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public  Void  delete( long id) {
		try {
			causeLiberationRepository.deleteById(id);
			return  null ;
		} catch (Exception e) {
			return  null ;
		}
	}
	 
	
	 
}


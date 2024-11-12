package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.CarteRecupConverter;
import com.cgpr.mineur.converter.CauseDecesConverter;
import com.cgpr.mineur.dto.CauseDecesDto;
import com.cgpr.mineur.models.CauseDeces;
import com.cgpr.mineur.repository.CauseDecesRepository;
import com.cgpr.mineur.service.CauseDecesService;


 

@Service
public class CauseDecesServiceImpl implements CauseDecesService {


	@Autowired
	private CauseDecesRepository causeDecesRepository;

	 
	 

	@Override
	public  List<CauseDecesDto>  listCauseMutation() {
		List<CauseDeces > list =  causeDecesRepository.findAllByOrderByIdAsc() ;
		return  list.stream().map(CauseDecesConverter::entityToDto).collect(Collectors.toList())  ;
	}

 

	@Override
	public  CauseDecesDto  getTypeAffaireById( long id) {
		Optional<CauseDeces> causeDeces = causeDecesRepository.findById(id);
		if (causeDeces.isPresent()) {
			return   CauseDecesConverter.entityToDto(causeDeces.get())  ;
		} else {
			return   null ;
		}
	}

	@Override
	public  CauseDecesDto  save( CauseDecesDto causeDecesDto) {

		try {
			CauseDeces causeDeces = causeDecesRepository.save(CauseDecesConverter.dtoToEntity(causeDecesDto) );
			return  CauseDecesConverter.entityToDto (causeDeces)   ;
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public  CauseDecesDto  update( CauseDecesDto causeDecesDto) {
		try {

			CauseDeces causeDeces = causeDecesRepository.save(CauseDecesConverter.dtoToEntity(causeDecesDto) );
			return  CauseDecesConverter.entityToDto (causeDeces)   ;
		} catch (Exception e) {
			return  null ;
		}

	}

	@Override
	public  Void  delete( long id) {
		try {
			causeDecesRepository.deleteById(id);
			return   null ;
		} catch (Exception e) {
			return   null ;
		}
	}
	
 
	 
}


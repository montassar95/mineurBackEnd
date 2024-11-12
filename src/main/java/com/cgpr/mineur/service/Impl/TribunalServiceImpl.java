package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.TitreAccusationConverter;
import com.cgpr.mineur.converter.TribunalConverter;
import com.cgpr.mineur.dto.TribunalDto;
import com.cgpr.mineur.models.Tribunal;
import com.cgpr.mineur.repository.TribunalRepository;
import com.cgpr.mineur.service.TribunalService;

 
 
@Service
public class TribunalServiceImpl implements  TribunalService {

	
	@Autowired
	private TribunalRepository tribunalRepository;

	@Override
	public List<TribunalDto> listTribunal() {
		List<Tribunal > list = tribunalRepository.findAllByOrderByIdAsc();
		
		return list. stream().map(TribunalConverter::entityToDto).collect(Collectors.toList());
	}

	@Override
	public TribunalDto getTribunalById( long id) {
		Optional<Tribunal> tribunalData = tribunalRepository.findById(id);
		if (tribunalData.isPresent()) {
			return TribunalConverter.entityToDto(tribunalData.get()) ;
		} else {
			return  null;
		}
	}
	
	@Override
	public List<TribunalDto> searchTribunal(long idGouv, long idType) {
		List<Tribunal> tribunalData;
		if(idGouv>0 && idType==0) {
			tribunalData = tribunalRepository.findByIdGouv(idGouv);
		}
		else if(idType>0 && idGouv==0) {
			
			tribunalData = tribunalRepository.findByIdType(idType);
		}
		else {
			tribunalData = tribunalRepository.findByIdGouvAndIdType(idGouv,idType);
		}
		 
	  
		if (tribunalData.isEmpty()) {
			return null;
		} else {
			
			return  tribunalData. stream().map(TribunalConverter::entityToDto).collect(Collectors.toList()); 
		}
	}

	@Override
	public TribunalDto save( TribunalDto tribunalDto) {

		try {
			return TribunalConverter.entityToDto(tribunalRepository.save(TribunalConverter.dtoToEntity(tribunalDto)  )) ;
		} catch (Exception e) {
			return  null ;
		}
	}

	@Override
	public  TribunalDto  update( TribunalDto tribunalDto) {
		try {

			return TribunalConverter.entityToDto(tribunalRepository.save(TribunalConverter.dtoToEntity(tribunalDto)  )) ;
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public  Void  delete(  long id) {
		try {
			tribunalRepository.deleteById(id);
			return   null ;
		} catch (Exception e) {
			return  null ;
		}
	}
	
 
}


package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.CauseLiberationConverter;
import com.cgpr.mineur.converter.CauseMutationConverter;
import com.cgpr.mineur.dto.CauseMutationDto;
import com.cgpr.mineur.models.CauseLiberation;
import com.cgpr.mineur.models.CauseMutation;
import com.cgpr.mineur.repository.CauseMutationRepository;
import com.cgpr.mineur.service.CauseMutationService;


 

@Service
public class CauseMutationServiceImpl implements  CauseMutationService {

	
	@Autowired
	private CauseMutationRepository causeMutationRepository;

	 
	 

	@Override
	public List<CauseMutationDto> listCauseMutation() {
		
		List<CauseMutation  > list =  causeMutationRepository.findAllByOrderByIdAsc();
		return  list.stream().map(CauseMutationConverter::entityToDto).collect(Collectors.toList())  ;
	 
	}

	@Override
	public CauseMutationDto getTypeAffaireById( long id) {
		Optional<CauseMutation> causeMutation = causeMutationRepository.findById(id);
		if (causeMutation.isPresent()) {
			return  CauseMutationConverter.entityToDto(causeMutation.get())   ;
		} else {
			return   null ;
		}
	}

	@Override
	public  CauseMutationDto  save( CauseMutationDto causeMutationDto) {

		try {
			
			CauseMutation causeMutation =	causeMutationRepository.save(CauseMutationConverter.dtoToEntity(causeMutationDto)) ;
			return    CauseMutationConverter.entityToDto(causeMutation) ;
			 
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public  CauseMutationDto  update( CauseMutationDto causeMutationDto) {
		try {

			CauseMutation causeMutation =	causeMutationRepository.save(CauseMutationConverter.dtoToEntity(causeMutationDto)) ;
			return    CauseMutationConverter.entityToDto(causeMutation) ;
		} catch (Exception e) {
			return  null ;
		}

	}

	@Override
	public  Void  delete( long id) {
		try {
			causeMutationRepository.deleteById(id);
			return   null ;
		} catch (Exception e) {
			return  null ;
		}
	}
 

	 
}


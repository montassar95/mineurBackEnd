package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.NiveauEducatifConverter;
import com.cgpr.mineur.converter.ResidenceConverter;
import com.cgpr.mineur.converter.SituationFamilialeConverter;
import com.cgpr.mineur.dto.SituationFamilialeDto;
import com.cgpr.mineur.models.SituationFamiliale;
import com.cgpr.mineur.repository.SituationFamilialeRepository;
import com.cgpr.mineur.service.SituationFamilialeService;

 
 

@Service
public class SituationFamilialeServiceImpl  implements SituationFamilialeService{

	
	@Autowired
	private SituationFamilialeRepository situationFamilialeRepository;

	@Override
	public List<SituationFamilialeDto> listNationalite() {
		List<SituationFamiliale> list = situationFamilialeRepository.findAllByOrderByIdAsc();
		
		return list. stream().map(SituationFamilialeConverter::entityToDto).collect(Collectors.toList()); 
	}

	@Override
	public SituationFamilialeDto getById( long id) {
		Optional<SituationFamiliale> Data = situationFamilialeRepository.findById(id);
		if (Data.isPresent()) {
			return  SituationFamilialeConverter.entityToDto(Data.get()) ;
		} else {
			return  null;
		}
	}

	@Override
	public SituationFamilialeDto save(SituationFamilialeDto situationFamilialeDto) {

		try {
			
			return SituationFamilialeConverter.entityToDto(situationFamilialeRepository.save(SituationFamilialeConverter.dtoToEntity(situationFamilialeDto) ))  ;
		 
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public SituationFamilialeDto update( SituationFamilialeDto situationFamilialeDto) {
		try {

			return SituationFamilialeConverter.entityToDto(situationFamilialeRepository.save(SituationFamilialeConverter.dtoToEntity(situationFamilialeDto) ))  ;
			 
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete( long id) {
		try {
			situationFamilialeRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return  null;
		}
	}

	 
}


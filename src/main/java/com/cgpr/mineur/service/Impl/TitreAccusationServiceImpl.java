package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.SituationFamilialeConverter;
import com.cgpr.mineur.converter.TitreAccusationConverter;
import com.cgpr.mineur.dto.TitreAccusationDto;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.repository.TitreAccusationRepository;
import com.cgpr.mineur.service.TitreAccusationService;

 
 

@Service
public class TitreAccusationServiceImpl  implements TitreAccusationService{

	

	@Autowired
	private TitreAccusationRepository titreAccusationRepository;

	@Override
	public List<TitreAccusationDto> findTitreAccusationByIdTypeAffaire( long id) {
		List<TitreAccusation> titreAccusations = titreAccusationRepository.findTitreAccusationByIdTypeAffaire(id);
		if (titreAccusations != null) {
			return    titreAccusations. stream().map(TitreAccusationConverter::entityToDto).collect(Collectors.toList()); 
		} else {
			return  null;
		}
	}

	@Override
	public List<TitreAccusationDto> list() {
		List<TitreAccusation> titreAccusations = titreAccusationRepository.findAllByOrderByIdAsc();
		return   titreAccusations. stream().map(TitreAccusationConverter::entityToDto).collect(Collectors.toList());
	}

	@Override
	public TitreAccusationDto getById( long id) {
		Optional<TitreAccusation> titreAccusation = titreAccusationRepository.findById(id);
		if (titreAccusation.isPresent()) {
			return TitreAccusationConverter.entityToDto(titreAccusation.get()) ;
		} else {
			return  null;
		}
	}

	@Override
	public TitreAccusationDto save(TitreAccusationDto titreAccusationDto) {

		try {
			return TitreAccusationConverter.entityToDto(
					titreAccusationRepository.save(
					TitreAccusationConverter.dtoToEntity (titreAccusationDto))
					);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public TitreAccusationDto update(TitreAccusationDto titreAccusationDto) {
		try {

			return TitreAccusationConverter.entityToDto(
					titreAccusationRepository.save(
					TitreAccusationConverter.dtoToEntity (titreAccusationDto))
					);
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete( long id) {
		try {
			titreAccusationRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return null;
		}
	}

	
	 
}


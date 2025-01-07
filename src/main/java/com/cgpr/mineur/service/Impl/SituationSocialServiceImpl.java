package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.cgpr.mineur.converter.PersonelleConverter;
import com.cgpr.mineur.converter.SituationFamilialeConverter;
import com.cgpr.mineur.converter.SituationSocialConverter;
import com.cgpr.mineur.dto.SituationSocialDto;
import com.cgpr.mineur.models.SituationFamiliale;
import com.cgpr.mineur.models.SituationSocial;
import com.cgpr.mineur.repository.SituationSocialRepository;
import com.cgpr.mineur.service.SituationSocialService;

 
 

@Service
public class SituationSocialServiceImpl implements SituationSocialService{

	
	@Autowired
	private SituationSocialRepository situationSocialRepository;

	@Override
	public List<SituationSocialDto> listNationalite() {
		
    List<SituationSocial> list = situationSocialRepository.findAllByOrderByIdAsc() ;
		
		return list. stream().map(SituationSocialConverter::entityToDto).collect(Collectors.toList()); 
		 
	}

	@Override
	public SituationSocialDto getById( long id) {
		Optional<SituationSocial > Data = situationSocialRepository.findById(id);
		if (Data.isPresent()) {
			return SituationSocialConverter.entityToDto (Data.get()) ;
		} else {
			return  null;
		}
	}

	@Override
	public SituationSocialDto save( SituationSocialDto situationSocialDto) {

		try {
			return	SituationSocialConverter.entityToDto(situationSocialRepository.save(SituationSocialConverter.dtoToEntity(situationSocialDto) ))  ;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SituationSocialDto update( SituationSocialDto situationSocialDto) {
		try {
			return	SituationSocialConverter.entityToDto(situationSocialRepository.save(SituationSocialConverter.dtoToEntity(situationSocialDto) ))  ;
			 
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Void delete( long id) {
		try {
			situationSocialRepository.deleteById(id);
			return null;
		} catch (Exception e) {
			 
		}
		return null;
	}

	 
}


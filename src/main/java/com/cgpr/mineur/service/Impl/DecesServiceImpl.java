package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.CommentTrouverConverter;
import com.cgpr.mineur.converter.DecesConverter;
import com.cgpr.mineur.dto.DecesDto;
import com.cgpr.mineur.models.CommentTrouver;
import com.cgpr.mineur.models.Deces;
import com.cgpr.mineur.repository.DecesRepository;
import com.cgpr.mineur.service.DecesService;


 

@Service
public class DecesServiceImpl implements DecesService{

	
	@Autowired
	private DecesRepository decesRepository;

	@Override
	public List<DecesDto> list() {
		
		List<Deces> list = (List<Deces>) decesRepository.findAll();
		return  list.stream().map(DecesConverter::entityToDto).collect(Collectors.toList())  ;
		 
	}

	@Override
	public DecesDto getById( long id) {
		Optional<Deces> deces = decesRepository.findById(id);
		if (deces.isPresent()) {
			return DecesConverter.entityToDto(deces.get()) ;
		} else {
			return  null;
		}
	}
	
	@Override
	public DecesDto save( DecesDto decesDto) {

		try {
			Deces deces  =  decesRepository.save(DecesConverter.dtoToEntity(decesDto)  );
			return  DecesConverter.entityToDto(deces)   ;
		} catch (Exception e) {
			return  null;
		}
	}
	
	

	 

	 

	@Override
	public DecesDto update(DecesDto decesDto) {
		try {

			Deces deces  =  decesRepository.save(DecesConverter.dtoToEntity(decesDto)  );
			return  DecesConverter.entityToDto(deces)   ;
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete( long id) {
		try {
			decesRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return null;
		}
	}

	 
}


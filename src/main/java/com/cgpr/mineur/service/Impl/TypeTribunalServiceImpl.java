package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.TypeJugeConverter;
import com.cgpr.mineur.converter.TypeTribunalConverter;
import com.cgpr.mineur.dto.TypeTribunalDto;
import com.cgpr.mineur.models.TypeTribunal;
import com.cgpr.mineur.repository.TypeTribunalRepository;
import com.cgpr.mineur.service.TypeTribunalService;


 

@Service
public class TypeTribunalServiceImpl implements TypeTribunalService{

	
	@Autowired
	private TypeTribunalRepository typeTribunalRepository;

	@Override
	public List<TypeTribunalDto> list() {
		 
		List<TypeTribunal> list = typeTribunalRepository.findAllByOrderByIdAsc();
		
		return	list. stream().map(TypeTribunalConverter::entityToDto).collect(Collectors.toList());
	 
	}

	@Override
	public TypeTribunalDto getById( long id) {
		Optional<TypeTribunal> Data = typeTribunalRepository.findById(id);
		if (Data.isPresent()) {
			return  TypeTribunalConverter.entityToDto(Data.get()) ;
		} else {
			return  null;
		}
	}
	
	
	
	 

	@Override
	public TypeTribunalDto save( TypeTribunalDto typeTribunalDto) {

		try {
			return    TypeTribunalConverter.entityToDto(typeTribunalRepository.save(TypeTribunalConverter.dtoToEntity(typeTribunalDto))) ;
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public TypeTribunalDto update(TypeTribunalDto typeTribunalDto) {
		try {

			return    TypeTribunalConverter.entityToDto(typeTribunalRepository.save(TypeTribunalConverter.dtoToEntity(typeTribunalDto))) ;
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete(long id) {
		try {
			typeTribunalRepository.deleteById(id);
			return null;
		} catch (Exception e) {
			return  null;
		}
	}
 
	
 
	 
}


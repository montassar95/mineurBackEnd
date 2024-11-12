package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.TypeAffaireConverter;
import com.cgpr.mineur.converter.TypeJugeConverter;
import com.cgpr.mineur.dto.TypeJugeDto;
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.models.TypeJuge;
import com.cgpr.mineur.repository.TypeJugeRepository;
import com.cgpr.mineur.service.TypeJugeService;


 

@Service
public class TypeJugeServiceImpl implements  TypeJugeService{

	
	@Autowired
	private TypeJugeRepository typeJugeRepository;

	@Override
	public List<TypeJugeDto> listTypeJuge() {
		
		 List<TypeJuge > list =typeJugeRepository.findAllByOrderByIdAsc();
			
			return list. stream().map(TypeJugeConverter::entityToDto).collect(Collectors.toList());
		 
	}

	@Override
	public TypeJugeDto getTypeJugeById( long id) {
		Optional<TypeJuge> typeData = typeJugeRepository.findById(id);
		if (typeData.isPresent()) {
			return TypeJugeConverter.entityToDto(typeData.get()) ;
		} else {
			return  null;
		}
	}

	@Override
	public TypeJugeDto save(TypeJugeDto typeJugeDto) {

		try {
			return TypeJugeConverter.entityToDto(typeJugeRepository.save( TypeJugeConverter.dtoToEntity(typeJugeDto))     );
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public TypeJugeDto update(TypeJugeDto typeJugeDto) {
		try {

			return TypeJugeConverter.entityToDto(typeJugeRepository.save( TypeJugeConverter.dtoToEntity(typeJugeDto))     );
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Void delete(long id) {
		try {
			typeJugeRepository.deleteById(id);
			return null;
		} catch (Exception e) {
			return  null;
		}
	}
	 
}


package com.cgpr.mineur.service.Impl;


 
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.TribunalConverter;
import com.cgpr.mineur.converter.TypeAffaireConverter;
import com.cgpr.mineur.dto.TypeAffaireDto;
import com.cgpr.mineur.models.Tribunal;
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.repository.TypeAffaireRepository;
import com.cgpr.mineur.service.TypeAffaireService;


 

@Service
public class TypeAffaireServiceImpl implements TypeAffaireService{

	
	@Autowired
	private TypeAffaireRepository typeAffaireRepository;

	@Override
	public List<TypeAffaireDto> listTypeAffaire() {
		
       List<TypeAffaire > list = typeAffaireRepository.findAllByOrderByIdAsc();
		
		return list. stream().map(TypeAffaireConverter::entityToDto).collect(Collectors.toList());
		
	 
	}

	@Override
	public TypeAffaireDto getTypeAffaireById(long id) {
		Optional<TypeAffaire> typeData = typeAffaireRepository.findById(id);
		if (typeData.isPresent()) {
			return TypeAffaireConverter.entityToDto(typeData.get()) ;
		} else {
			return  null;
		}
	}

	@Override
	public TypeAffaireDto save( TypeAffaireDto typeAffaireDto) {

		try {
			return TypeAffaireConverter.entityToDto(typeAffaireRepository.save(TypeAffaireConverter.dtoToEntity(typeAffaireDto))) ;
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public TypeAffaireDto update( TypeAffaireDto typeAffaireDto) {
		try {

			return TypeAffaireConverter.entityToDto(typeAffaireRepository.save(TypeAffaireConverter.dtoToEntity(typeAffaireDto))) ;
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public  Void  delete(  long id) {
		try {
			
 		  
			if (typeAffaireRepository.existsById(id)) {
				
				typeAffaireRepository.deleteById(id);
			}
			else {
				System.out.println("non pas");
		 
			}
			return   null ;
		} catch (Exception e) {
			System.err.println(e.toString());		
			
			return   null ;
		}
	}
	
 
}


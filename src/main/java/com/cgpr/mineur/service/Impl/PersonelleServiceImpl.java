package com.cgpr.mineur.service.Impl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.NiveauEducatifConverter;
import com.cgpr.mineur.converter.PersonelleConverter;
import com.cgpr.mineur.dto.PersonelleDto;
import com.cgpr.mineur.models.NiveauEducatif;
import com.cgpr.mineur.models.Personelle;
import com.cgpr.mineur.repository.PersonelleRepository;
import com.cgpr.mineur.service.PersonelleService;
 
 
@Service
public class PersonelleServiceImpl implements PersonelleService {

	
	@Autowired
	private PersonelleRepository personelleRepository;

	@Override
	public List<PersonelleDto> listPersonelle() {
		 List<Personelle > list = (List<Personelle>) personelleRepository.findAll();
		 
			return list. stream().map(PersonelleConverter::entityToDto).collect(Collectors.toList()); 
		 
	}
	 
	@Override
	public PersonelleDto save( PersonelleDto personelleDto) {

		try {
			return	PersonelleConverter.entityToDto(personelleRepository.save(PersonelleConverter.dtoToEntity(personelleDto) ))  ;
			 
		} catch (Exception e) {
			return null;
		}
	}
	
 
	 
	
	 
}
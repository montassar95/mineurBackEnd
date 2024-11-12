package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.EtablissementConverter;
import com.cgpr.mineur.converter.GouvernoratConverter;
import com.cgpr.mineur.dto.GouvernoratDto;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Gouvernorat;
import com.cgpr.mineur.repository.GouvernoratRepository;
import com.cgpr.mineur.service.GouvernoratService;


 

@Service
public class GouvernoratServiceImpl implements  GouvernoratService {

	
	@Autowired
	private GouvernoratRepository gouvernoratRepository;

	@Override
	public List<GouvernoratDto> list() {
		
		List<Gouvernorat> list =  gouvernoratRepository.findAllByOrderByIdAsc();
		return  list. stream().map(GouvernoratConverter::entityToDto).collect(Collectors.toList())  ;
	 
	}

	@Override
	public GouvernoratDto getById( long id) {
		Optional<Gouvernorat> Data = gouvernoratRepository.findById(id);
		if (Data.isPresent()) {
			return GouvernoratConverter.entityToDto(Data.get()) ;
		} else {
			return  null;
		}
	}

	
	
	
	@Override
	public GouvernoratDto save(GouvernoratDto gouvernoratDto) {

		try {
			
			Gouvernorat  gouvernorat  = gouvernoratRepository.save( GouvernoratConverter.dtoToEntity(gouvernoratDto) );
			return   GouvernoratConverter.entityToDto(gouvernorat)  ;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public GouvernoratDto update( GouvernoratDto gouvernoratDto) {
try {
			
			Gouvernorat  gouvernorat  = gouvernoratRepository.save( GouvernoratConverter.dtoToEntity(gouvernoratDto) );
			return   GouvernoratConverter.entityToDto(gouvernorat)  ;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Void delete( long id) {
		try {
			gouvernoratRepository.deleteById(id);
			return null;
		} catch (Exception e) {
			return  null;
		}
	}
 
	
	 
	 
}


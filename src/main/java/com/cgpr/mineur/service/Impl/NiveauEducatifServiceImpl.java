package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.NationaliteConverter;
import com.cgpr.mineur.converter.NiveauEducatifConverter;
import com.cgpr.mineur.dto.NiveauEducatifDto;
import com.cgpr.mineur.models.Nationalite;
import com.cgpr.mineur.models.NiveauEducatif;
import com.cgpr.mineur.repository.NiveauEducatifRepository;
import com.cgpr.mineur.service.NiveauEducatifService;

 
 

@Service
public class NiveauEducatifServiceImpl implements NiveauEducatifService{

	@Autowired
	private NiveauEducatifRepository niveauEducatifRepository;

	@Override
	public  List<NiveauEducatifDto>  listNiveauEducatif() {
		 List<NiveauEducatif > list =niveauEducatifRepository.findAllByOrderByIdAsc();
		 
			return list. stream().map(NiveauEducatifConverter::entityToDto).collect(Collectors.toList()); 
	 
	}

	@Override
	public  NiveauEducatifDto  getNiveauEducatifById(long id) {
		Optional<NiveauEducatif> Data = niveauEducatifRepository.findById(id);
		if (Data.isPresent()) {
			return NiveauEducatifConverter.entityToDto( Data.get());
		} else {
			return  null ;
		}
	}

	@Override
	public  NiveauEducatifDto  save( NiveauEducatifDto niveauEducatifDto) {

		try {
			return NiveauEducatifConverter.entityToDto(niveauEducatifRepository.save(NiveauEducatifConverter.dtoToEntity(niveauEducatifDto) ))  ;
		} catch (Exception e) {
			return  null ;
		}
	}

	@Override
	public   NiveauEducatifDto   update(  NiveauEducatifDto niveauEducatifDto) {
		try {
			return NiveauEducatifConverter.entityToDto(niveauEducatifRepository.save(NiveauEducatifConverter.dtoToEntity(niveauEducatifDto) ))  ;
		 
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public Void  delete(  long id) {
		try {
			niveauEducatifRepository.deleteById(id);
			return   null ;
		} catch (Exception e) {
			return   null ;
		}
	}

	 
}


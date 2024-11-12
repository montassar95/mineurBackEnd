package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.MetierConverter;
import com.cgpr.mineur.converter.NationaliteConverter;
import com.cgpr.mineur.dto.NationaliteDto;
import com.cgpr.mineur.models.Metier;
import com.cgpr.mineur.models.Nationalite;
import com.cgpr.mineur.repository.NationaliteRepository;
import com.cgpr.mineur.service.NationaliteService;

 
 

@Service
public class NationaliteServiceImpl implements NationaliteService{

	
	@Autowired
	private NationaliteRepository nationaliteRepository;

	@Override
	public List<NationaliteDto> listNationalite() {
		
		 List<Nationalite > list =nationaliteRepository.findAllByOrderByIdAsc();
		 
			return list. stream().map(NationaliteConverter::entityToDto).collect(Collectors.toList());  
		 
	}

	@Override
	public NationaliteDto getNationaliteById( long id) {
		Optional<Nationalite> Data = nationaliteRepository.findById(id);
		if (Data.isPresent()) {
			return  NationaliteConverter .entityToDto(Data.get());
		} else {
			return  null;
		}
	}

	@Override
	public  NationaliteDto  save(  NationaliteDto nationaliteDto) {

		try {
			
			return NationaliteConverter.entityToDto(nationaliteRepository.save(NationaliteConverter.dtoToEntity(nationaliteDto) ))  ;
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public NationaliteDto  update(  NationaliteDto nationaliteDto) {
		try {

			return NationaliteConverter.entityToDto(nationaliteRepository.save(NationaliteConverter.dtoToEntity(nationaliteDto) ))  ;
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public  Void  delete(  long id) {
		try {
			nationaliteRepository.deleteById(id);
			return   null ;
		} catch (Exception e) {
			return   null ;
		}
	}

 
}


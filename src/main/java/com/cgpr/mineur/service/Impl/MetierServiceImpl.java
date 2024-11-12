package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.cgpr.mineur.converter.LieuDecesConverter;
import com.cgpr.mineur.converter.MetierConverter;
import com.cgpr.mineur.dto.MetierDto;
import com.cgpr.mineur.models.LieuDeces;
import com.cgpr.mineur.models.Metier;
import com.cgpr.mineur.repository.MetierRepository;
import com.cgpr.mineur.service.MetierService;
 
@Service
public class MetierServiceImpl implements MetierService{

	
	@Autowired
	private MetierRepository metierRepository;

	@Override
	public  List<MetierDto>  list() {
		
		 List<Metier > list = metierRepository.findAllByOrderByIdAsc( );
		 
			return list. stream().map(MetierConverter::entityToDto).collect(Collectors.toList());  
		 
	}

	@Override
	public  MetierDto  getById(@PathVariable("id") long id) {
		Optional<Metier> Data = metierRepository.findById(id);
		if (Data.isPresent()) {
			return   MetierConverter.entityToDto(Data.get()) ;
		} else {
			return  null ;
		}
	}

	
	
	
	@Override
	public  MetierDto  save(@RequestBody MetierDto metierDto) {

		try {
			return  MetierConverter.entityToDto(metierRepository.save(MetierConverter.dtoToEntity(metierDto) ))  ;
		} catch (Exception e) {
			return  null ;
		}
	}

	@Override
	public  MetierDto  update(@RequestBody MetierDto metierDto) {
		try {

			return  MetierConverter.entityToDto(metierRepository.save(MetierConverter.dtoToEntity(metierDto) ))  ;
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public  Void   delete( long id) {
		try {
			metierRepository.deleteById(id);
			return  null ;
		} catch (Exception e) {
			return   null ;
		}
	}
 
	 
	 
}


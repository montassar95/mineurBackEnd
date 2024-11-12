package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.CauseMutationConverter;
import com.cgpr.mineur.converter.ClassePenaleConverter;
import com.cgpr.mineur.dto.ClassePenaleDto;
import com.cgpr.mineur.models.CauseMutation;
import com.cgpr.mineur.models.ClassePenale;
import com.cgpr.mineur.repository.ClassePenaleRepository;
import com.cgpr.mineur.service.ClassePenaleService;


 

@Service
public class ClassePenaleServiceImpl implements ClassePenaleService{

	
	@Autowired
	private ClassePenaleRepository classePenaleRepository;

	@Override
	public  List<ClassePenaleDto>  list() {
		
		List<ClassePenale > list =  classePenaleRepository.findAllByOrderByIdAsc();
		return  list.stream().map(ClassePenaleConverter::entityToDto).collect(Collectors.toList())  ;
		
		
		 
	}

	@Override
	public  ClassePenaleDto  getById( long id) {
		Optional<ClassePenale> classePenale = classePenaleRepository.findById(id);
		if (classePenale.isPresent()) {
			return  ClassePenaleConverter.entityToDto(classePenale.get())  ;
		} else {
			return   null  ;
		}
	}
	
	
 

	@Override
	public  ClassePenaleDto  save( ClassePenaleDto classePenaleDto) {

		try {
			
			ClassePenale classePenale = classePenaleRepository.save(ClassePenaleConverter.dtoToEntity(classePenaleDto) ) ;
			return   ClassePenaleConverter.entityToDto(classePenale);
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public  ClassePenaleDto  update( ClassePenaleDto classePenaleDto) {
		try {

			ClassePenale classePenale = classePenaleRepository.save(ClassePenaleConverter.dtoToEntity(classePenaleDto) ) ;
			return   ClassePenaleConverter.entityToDto(classePenale);
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public Void  delete( long id) {
		try {
			classePenaleRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return null;
		}
	}

	 
 
	 
 
}


package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.ClassePenale;
import com.cgpr.mineur.repository.ClassePenaleRepository;
import com.cgpr.mineur.service.ClassePenaleService;


 

@Service
public class ClassePenaleServiceImpl implements ClassePenaleService{

	
	@Autowired
	private ClassePenaleRepository classePenaleRepository;

	@Override
	public  List<ClassePenale>  list() {
		return  classePenaleRepository.findAllByOrderByIdAsc() ;
	}

	@Override
	public  ClassePenale  getById( long id) {
		Optional<ClassePenale> Data = classePenaleRepository.findById(id);
		if (Data.isPresent()) {
			return   Data.get() ;
		} else {
			return   null  ;
		}
	}
	
	
 

	@Override
	public  ClassePenale  save( ClassePenale causeDeces) {

		try {
			return   classePenaleRepository.save(causeDeces) ;
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public  ClassePenale  update( ClassePenale causeDeces) {
		try {

			return  classePenaleRepository.save(causeDeces) ;
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


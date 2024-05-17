package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.CauseDeces;
import com.cgpr.mineur.repository.CauseDecesRepository;
import com.cgpr.mineur.service.CauseDecesService;


 

@Service
public class CauseDecesServiceImpl implements CauseDecesService {


	@Autowired
	private CauseDecesRepository causeDecesRepository;

	 
	 

	@Override
	public  List<CauseDeces>  listCauseMutation() {
		return   causeDecesRepository.findAllByOrderByIdAsc() ;
	}

 

	@Override
	public  CauseDeces  getTypeAffaireById( long id) {
		Optional<CauseDeces> typeData = causeDecesRepository.findById(id);
		if (typeData.isPresent()) {
			return   typeData.get() ;
		} else {
			return   null ;
		}
	}

	@Override
	public  CauseDeces  save( CauseDeces causeDeces) {

		try {
			return  causeDecesRepository.save(causeDeces) ;
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public  CauseDeces  update( CauseDeces causeDeces) {
		try {

			return  causeDecesRepository.save(causeDeces );
		} catch (Exception e) {
			return  null ;
		}

	}

	@Override
	public  Void  delete( long id) {
		try {
			causeDecesRepository.deleteById(id);
			return   null ;
		} catch (Exception e) {
			return   null ;
		}
	}
	
 
	 
}


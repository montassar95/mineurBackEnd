package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.CauseLiberation;
import com.cgpr.mineur.repository.CauseLiberationRepository;
import com.cgpr.mineur.service.CauseLiberationService;


 

@Service
public class CauseLiberationServiceImpl implements  CauseLiberationService {

	

	@Autowired
	private CauseLiberationRepository causeLiberationRepository;

	 
	 

	@Override
	public  List<CauseLiberation>  listCauseLiberation() {
		return   causeLiberationRepository.findAllByOrderByIdAsc() ;
	}


	@Override
	public  CauseLiberation  getTypeAffaireById(  long id) {
		Optional<CauseLiberation> typeData = causeLiberationRepository.findById(id);
		if (typeData.isPresent()) {
			return   typeData.get()  ;
		} else {
			return   null ;
		}
	}

	@Override
	public  CauseLiberation  save( CauseLiberation causeDeces) {

		try {
			return  causeLiberationRepository.save(causeDeces) ;
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public  CauseLiberation  update( CauseLiberation causeDeces) {
		try {

			return causeLiberationRepository.save(causeDeces );
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public  Void  delete( long id) {
		try {
			causeLiberationRepository.deleteById(id);
			return  null ;
		} catch (Exception e) {
			return  null ;
		}
	}
	 
	
	 
}


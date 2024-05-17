package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.CauseMutation;
import com.cgpr.mineur.repository.CauseMutationRepository;
import com.cgpr.mineur.service.CauseMutationService;


 

@Service
public class CauseMutationServiceImpl implements  CauseMutationService {

	
	@Autowired
	private CauseMutationRepository causeMutationRepository;

	 
	 

	@Override
	public List<CauseMutation> listCauseMutation() {
		return causeMutationRepository.findAllByOrderByIdAsc();
	}

	@Override
	public CauseMutation getTypeAffaireById( long id) {
		Optional<CauseMutation> typeData = causeMutationRepository.findById(id);
		if (typeData.isPresent()) {
			return   typeData.get() ;
		} else {
			return   null ;
		}
	}

	@Override
	public  CauseMutation  save( CauseMutation causeDeces) {

		try {
			return  causeMutationRepository.save(causeDeces) ;
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public  CauseMutation  update( CauseMutation causeDeces) {
		try {

			return  causeMutationRepository.save(causeDeces) ;
		} catch (Exception e) {
			return  null ;
		}

	}

	@Override
	public  Void  delete( long id) {
		try {
			causeMutationRepository.deleteById(id);
			return   null ;
		} catch (Exception e) {
			return  null ;
		}
	}
 

	 
}


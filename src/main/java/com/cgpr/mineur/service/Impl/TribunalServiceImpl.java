package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Tribunal;
import com.cgpr.mineur.repository.TribunalRepository;
import com.cgpr.mineur.service.TribunalService;

 
 
@Service
public class TribunalServiceImpl implements  TribunalService {

	
	@Autowired
	private TribunalRepository tribunalRepository;

	@Override
	public List<Tribunal> listTribunal() {
		return tribunalRepository.findAllByOrderByIdAsc();
	}

	@Override
	public Tribunal getTribunalById( long id) {
		Optional<Tribunal> tribunalData = tribunalRepository.findById(id);
		if (tribunalData.isPresent()) {
			return tribunalData.get();
		} else {
			return  null;
		}
	}
	
	@Override
	public List<Tribunal> searchTribunal(long idGouv, long idType) {
		List<Tribunal> tribunalData;
		if(idGouv>0 && idType==0) {
			tribunalData = tribunalRepository.findByIdGouv(idGouv);
		}
		else if(idType>0 && idGouv==0) {
			
			tribunalData = tribunalRepository.findByIdType(idType);
		}
		else {
			tribunalData = tribunalRepository.findByIdGouvAndIdType(idGouv,idType);
		}
		 
	  
		if (tribunalData.isEmpty()) {
			return null;
		} else {
			
			return  tribunalData;
		}
	}

	@Override
	public Tribunal save( Tribunal tribunal) {

		try {
			return tribunalRepository.save(tribunal);
		} catch (Exception e) {
			return  null ;
		}
	}

	@Override
	public  Tribunal  update( Tribunal tribunal) {
		try {

			return  tribunalRepository.save(tribunal);
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public  Void  delete(  long id) {
		try {
			tribunalRepository.deleteById(id);
			return   null ;
		} catch (Exception e) {
			return  null ;
		}
	}
	
 
}


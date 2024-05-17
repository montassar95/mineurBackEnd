package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.NiveauEducatif;
import com.cgpr.mineur.repository.NiveauEducatifRepository;
import com.cgpr.mineur.service.NiveauEducatifService;

 
 

@Service
public class NiveauEducatifServiceImpl implements NiveauEducatifService{

	@Autowired
	private NiveauEducatifRepository niveauEducatifRepository;

	@Override
	public  List<NiveauEducatif>  listNiveauEducatif() {
		return  niveauEducatifRepository.findAllByOrderByIdAsc();
	}

	@Override
	public  NiveauEducatif  getNiveauEducatifById(long id) {
		Optional<NiveauEducatif> Data = niveauEducatifRepository.findById(id);
		if (Data.isPresent()) {
			return  Data.get();
		} else {
			return  null ;
		}
	}

	@Override
	public  NiveauEducatif  save( NiveauEducatif niveauEducatif) {

		try {
			return niveauEducatifRepository.save(niveauEducatif );
		} catch (Exception e) {
			return  null ;
		}
	}

	@Override
	public   NiveauEducatif   update(  NiveauEducatif niveauEducatif) {
		try {

			return  niveauEducatifRepository.save(niveauEducatif);
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


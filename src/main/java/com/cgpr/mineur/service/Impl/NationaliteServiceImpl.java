package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Nationalite;
import com.cgpr.mineur.repository.NationaliteRepository;
import com.cgpr.mineur.service.NationaliteService;

 
 

@Service
public class NationaliteServiceImpl implements NationaliteService{

	
	@Autowired
	private NationaliteRepository nationaliteRepository;

	@Override
	public List<Nationalite> listNationalite() {
		return nationaliteRepository.findAllByOrderByIdAsc();
	}

	@Override
	public Nationalite getNationaliteById( long id) {
		Optional<Nationalite> Data = nationaliteRepository.findById(id);
		if (Data.isPresent()) {
			return  Data.get();
		} else {
			return  null;
		}
	}

	@Override
	public  Nationalite  save(  Nationalite nationalite) {

		try {
			return nationaliteRepository.save(nationalite );
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public Nationalite  update(  Nationalite nationalite) {
		try {

			return  nationaliteRepository.save(nationalite );
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


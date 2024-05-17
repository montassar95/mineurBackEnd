package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Deces;
import com.cgpr.mineur.repository.DecesRepository;
import com.cgpr.mineur.service.DecesService;


 

@Service
public class DecesServiceImpl implements DecesService{

	
	@Autowired
	private DecesRepository decesRepository;

	@Override
	public List<Deces> list() {
		return (List<Deces>) decesRepository.findAll();
	}

	@Override
	public Deces getById( long id) {
		Optional<Deces> Data = decesRepository.findById(id);
		if (Data.isPresent()) {
			return Data.get();
		} else {
			return  null;
		}
	}
	
	@Override
	public Deces save( Deces deces) {

		try {
			return decesRepository.save(deces);
		} catch (Exception e) {
			return  null;
		}
	}
	
	

	 

	 

	@Override
	public Deces update(Deces causeDeces) {
		try {

			return decesRepository.save(causeDeces);
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete( long id) {
		try {
			decesRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return null;
		}
	}

	 
}


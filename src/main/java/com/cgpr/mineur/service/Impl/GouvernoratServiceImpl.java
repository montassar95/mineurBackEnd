package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Gouvernorat;
import com.cgpr.mineur.repository.GouvernoratRepository;
import com.cgpr.mineur.service.GouvernoratService;


 

@Service
public class GouvernoratServiceImpl implements  GouvernoratService {

	
	@Autowired
	private GouvernoratRepository gouvernoratRepository;

	@Override
	public List<Gouvernorat> list() {
		return gouvernoratRepository.findAllByOrderByIdAsc();
	}

	@Override
	public Gouvernorat getById( long id) {
		Optional<Gouvernorat> Data = gouvernoratRepository.findById(id);
		if (Data.isPresent()) {
			return Data.get();
		} else {
			return  null;
		}
	}

	
	
	
	@Override
	public Gouvernorat save(Gouvernorat gouv) {

		try {
			return gouvernoratRepository.save(gouv);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Gouvernorat update( Gouvernorat gouv) {
		try {

			return gouvernoratRepository.save(gouv);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Void delete( long id) {
		try {
			gouvernoratRepository.deleteById(id);
			return null;
		} catch (Exception e) {
			return  null;
		}
	}
 
	
	 
	 
}


package com.cgpr.mineur.service.Impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Personelle;
import com.cgpr.mineur.repository.PersonelleRepository;
import com.cgpr.mineur.service.PersonelleService;
 
 
@Service
public class PersonelleServiceImpl implements PersonelleService {

	
	@Autowired
	private PersonelleRepository personelleRepository;

	@Override
	public List<Personelle> listPersonelle() {
		return (List<Personelle>) personelleRepository.findAll();
	}
	 
	@Override
	public Personelle save( Personelle personelle) {

		try {
			return personelleRepository.save(personelle);
		} catch (Exception e) {
			return null;
		}
	}
	
 
	 
	
	 
}
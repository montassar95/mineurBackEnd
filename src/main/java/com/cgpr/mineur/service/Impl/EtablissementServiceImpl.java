package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.repository.EtablissementRepository;
import com.cgpr.mineur.service.EtablissementService;

 
 

@Service
public class EtablissementServiceImpl  implements  EtablissementService {

	
	@Autowired
	private EtablissementRepository etablissementRepository;

	@Override
	public  List<Etablissement>   listEtablissement() {
		
		return  (List<Etablissement>) etablissementRepository.findAll();
	}
	@Override
	public List<Etablissement> listEtablissementCentre() {
		
		List<Etablissement> allCentre =  etablissementRepository.listEtablissementCentre();
		return allCentre;
	}
	@Override
	public Etablissement getEtablissementById(String id) {
		Optional<Etablissement> etablissementData = etablissementRepository.findById(id);
		if (etablissementData.isPresent()) {
			return etablissementRepository.findById(id).get();
		} else {
			return  null;
		}
	}

	@Override
	public Etablissement save(Etablissement etablissement) {
		System.out.print(etablissement.toString());
		try {
			return etablissementRepository.save(etablissement);
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public Etablissement update( Etablissement etablissement) {
		try {

			return etablissementRepository.save(etablissement);
		} catch (Exception e) {
			return   null;
		}

	}

	@Override
	public  Void  delete( String id) {
		try {
			etablissementRepository.deleteById(id);
			return  null ;
		} catch (Exception e) {
			return  null ;
		}
	}
 

}


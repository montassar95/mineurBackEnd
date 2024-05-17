package com.cgpr.mineur.service.Impl;


 
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.repository.TypeAffaireRepository;
import com.cgpr.mineur.service.TypeAffaireService;


 

@Service
public class TypeAffaireServiceImpl implements TypeAffaireService{

	
	@Autowired
	private TypeAffaireRepository typeAffaireRepository;

	@Override
	public List<TypeAffaire> listTypeAffaire() {
		return typeAffaireRepository.findAllByOrderByIdAsc();
	}

	@Override
	public TypeAffaire getTypeAffaireById(long id) {
		Optional<TypeAffaire> typeData = typeAffaireRepository.findById(id);
		if (typeData.isPresent()) {
			return typeData.get();
		} else {
			return  null;
		}
	}

	@Override
	public TypeAffaire save( TypeAffaire typeAffaire) {

		try {
			return typeAffaireRepository.save(typeAffaire);
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public TypeAffaire update( TypeAffaire typeAffaire) {
		try {

			return typeAffaireRepository.save(typeAffaire);
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public  Void  delete(  long id) {
		try {
			
 		  
			if (typeAffaireRepository.existsById(id)) {
				
				typeAffaireRepository.deleteById(id);
			}
			else {
				System.out.println("non pas");
		 
			}
			return   null ;
		} catch (Exception e) {
			System.err.println(e.toString());		
			
			return   null ;
		}
	}
	
 
}


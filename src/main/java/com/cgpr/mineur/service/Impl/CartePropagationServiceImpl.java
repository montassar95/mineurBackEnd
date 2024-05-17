package com.cgpr.mineur.service.Impl;


 
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
 
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CartePropagation;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.CartePropagationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.CartePropagationService;
 
 
 
@Service
public class CartePropagationServiceImpl  implements CartePropagationService {

	
	@Autowired
	private CartePropagationRepository cartePropagationRepository;

	 
	
	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;
 
	
	@Autowired
	private  DocumentRepository documentRepository;
	
	
	
	
	
	
	@Override
	public  CartePropagation  save(  CartePropagation cartePropagation) {

		 
		 
		System.out.println("================================debut affaire ===========================");
		System.out.println(cartePropagation.getAffaire().toString());
		cartePropagation.getAffaire().setTypeDocument("CP");
//	 	 cartePropagation.getAffaire().setTypeAffaire(cartePropagation.getTypeAffaire());
	 
  	affaireRepository.save(cartePropagation.getAffaire());
		System.out.println("==================================fin affaire=========================");
		
		
		cartePropagation.setTypeAffaire(cartePropagation.getAffaire().getTypeAffaire());
		CartePropagation c = cartePropagationRepository.save(cartePropagation);
		 
		
 
		try {
			return   c ;
		} catch (Exception e) {
			return   null ;
		}

	}
	 
	 
	 
	 
	
}
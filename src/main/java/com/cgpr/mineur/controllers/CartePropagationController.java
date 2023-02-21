package com.cgpr.mineur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.CartePropagation;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.CartePropagationRepository;
import com.cgpr.mineur.repository.DocumentRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cartePropagation")
public class CartePropagationController {

	@Autowired
	private CartePropagationRepository cartePropagationRepository;

	 
	
	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;
 
	
	@Autowired
	private  DocumentRepository documentRepository;
	
	
	
	
	
	
	@PostMapping("/add")
	public ApiResponse<CartePropagation> save(@RequestBody CartePropagation cartePropagation) {

		 
		 
		System.out.println("================================debut affaire ===========================");
		System.out.println(cartePropagation.getAffaire().toString());
		cartePropagation.getAffaire().setTypeDocument("CP");
//	 	 cartePropagation.getAffaire().setTypeAffaire(cartePropagation.getTypeAffaire());
	 
  	affaireRepository.save(cartePropagation.getAffaire());
		System.out.println("==================================fin affaire=========================");
		
		
		cartePropagation.setTypeAffaire(cartePropagation.getAffaire().getTypeAffaire());
		CartePropagation c = cartePropagationRepository.save(cartePropagation);
		 
		
 
		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", c);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

 
	
 
}

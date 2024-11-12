package com.cgpr.mineur.service.Impl;


 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.CartePropagationConverter;
import com.cgpr.mineur.dto.CartePropagationDto;
import com.cgpr.mineur.models.CartePropagation;
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
	public  CartePropagationDto  save(  CartePropagationDto cartePropagationDto) {

		 
		 
		System.out.println("================================debut affaire ===========================");
		System.out.println(cartePropagationDto.getAffaire().toString());
		cartePropagationDto.getAffaire().setTypeDocument("CP");
//	 	 cartePropagation.getAffaire().setTypeAffaire(cartePropagation.getTypeAffaire());
	 
		
		affaireRepository.save(AffaireConverter.dtoToEntity(cartePropagationDto.getAffaire()));
		
		
  	 
		System.out.println("==================================fin affaire=========================");
		
		
		cartePropagationDto.setTypeAffaire(cartePropagationDto.getAffaire().getTypeAffaire());
		CartePropagation cartePropagation = cartePropagationRepository.save(CartePropagationConverter.dtoToEntity(cartePropagationDto));
		 
		
 
		try {
			return   CartePropagationConverter.entityToDto(cartePropagation );
		} catch (Exception e) {
			return   null ;
		}

	}
	 
	 
	 
	 
	
}
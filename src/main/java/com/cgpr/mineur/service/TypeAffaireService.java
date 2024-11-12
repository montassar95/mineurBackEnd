package com.cgpr.mineur.service;


 
import java.util.List;

import com.cgpr.mineur.dto.TypeAffaireDto;


 


public interface TypeAffaireService {
	

	public  List<TypeAffaireDto> listTypeAffaire();
	
	public TypeAffaireDto getTypeAffaireById( long id);

	
	public TypeAffaireDto save( TypeAffaireDto typeAffaire) ;

	
	public TypeAffaireDto update(TypeAffaireDto typeAffaire) ;

	
	public Void delete( long id);
	
}


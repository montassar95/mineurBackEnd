package com.cgpr.mineur.service;


 
import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.TypeAffaire;


 


public interface TypeAffaireService {
	

	public  List<TypeAffaire> listTypeAffaire();
	
	public TypeAffaire getTypeAffaireById( long id);

	
	public TypeAffaire save( TypeAffaire typeAffaire) ;

	
	public TypeAffaire update(TypeAffaire typeAffaire) ;

	
	public Void delete( long id);
	
}


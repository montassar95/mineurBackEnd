package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.TypeJuge;


 


public interface TypeJugeService  {
	
	
	public List<TypeJuge> listTypeJuge() ;
	 
	public TypeJuge getTypeJugeById( long id);


	public TypeJuge save(TypeJuge typeJuge) ;

	
	public TypeJuge update(TypeJuge typeJuge);

	
	public Void delete(long id) ;
}


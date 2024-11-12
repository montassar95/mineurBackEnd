package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.TypeJugeDto;


 


public interface TypeJugeService  {
	
	
	public List<TypeJugeDto> listTypeJuge() ;
	 
	public TypeJugeDto getTypeJugeById( long id);


	public TypeJugeDto save(TypeJugeDto typeJuge) ;

	
	public TypeJugeDto update(TypeJugeDto typeJuge);

	
	public Void delete(long id) ;
}


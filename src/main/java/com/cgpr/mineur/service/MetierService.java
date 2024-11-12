package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.MetierDto;


 


public interface MetierService {
	
	 

	public List<MetierDto> list();

	public MetierDto getById( long id) ;

	
	
	

	public MetierDto save(MetierDto gouvDto);


	public MetierDto update( MetierDto gouvDto);


	public Void delete( long id) ;
}


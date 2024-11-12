package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.MotifArreterlexecutionDto;


public interface MotifArreterlexecutionService  {
	
	 

	public List<MotifArreterlexecutionDto> listMotifArreterlexecution() ;

	public MotifArreterlexecutionDto getById(long id);


	public MotifArreterlexecutionDto save(MotifArreterlexecutionDto causeDeces);

	
	public MotifArreterlexecutionDto update( MotifArreterlexecutionDto causeDeces) ;
	
	public Void delete( long id) ;

}
package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.MotifArreterlexecution;


public interface MotifArreterlexecutionService  {
	
	 

	public List<MotifArreterlexecution> listMotifArreterlexecution() ;

	public MotifArreterlexecution getById(long id);


	public MotifArreterlexecution save(MotifArreterlexecution causeDeces);

	
	public MotifArreterlexecution update( MotifArreterlexecution causeDeces) ;
	
	public Void delete( long id) ;

}
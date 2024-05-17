package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.CauseMutation;


 


public interface CauseMutationService   {

	
	public  List<CauseMutation>  listCauseMutation();
	
	public  CauseMutation  getTypeAffaireById(  long id);

	
	public  CauseMutation  save( CauseMutation causeDeces) ;
	
	public  CauseMutation  update(  CauseMutation causeDeces);

	
	public  Void  delete(  long id);
	 
}


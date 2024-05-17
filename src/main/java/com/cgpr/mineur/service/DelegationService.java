package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Delegation;


 


public interface DelegationService {
	 
	
	public List<Delegation> list() ;


	public Delegation getById(long id) ;


	public List<Delegation> getDelegationByGouv( long id) ;


	public Delegation findByGouvernorat(long idG,long idD) ;


	public Delegation save(Delegation delegation) ;

	
	
	public Delegation update( Delegation causeDeces);

	
	public Void delete(long id) ;
	 
}


package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.DelegationDto;


 


public interface DelegationService {
	 
	
	public List<DelegationDto> list() ;


	public DelegationDto getById(long id) ;


	public List<DelegationDto> getDelegationByGouv( long id) ;


	public DelegationDto findByGouvernorat(long idG,long idD) ;


	public DelegationDto save(DelegationDto delegationDto) ;

	
	
	public DelegationDto update( DelegationDto causeDecesDto);

	
	public Void delete(long id) ;
	 
}


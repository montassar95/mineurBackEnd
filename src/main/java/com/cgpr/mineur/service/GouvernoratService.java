package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.GouvernoratDto;


 


public interface GouvernoratService  {
	

	
	public List<GouvernoratDto> list();
	
	public GouvernoratDto getById( long id);

	
	
	
	
	public GouvernoratDto save( GouvernoratDto gouvDto) ;

	
	public GouvernoratDto update( GouvernoratDto gouvDto);

	
	public Void delete( long id);
	 
}


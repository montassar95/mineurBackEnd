package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.LieuDecesDto;

 
 


public interface LieuDecesService  {
	
	
	public List<LieuDecesDto> listCauseMutation() ;
	
	public LieuDecesDto getById( long id) ;

	
	public LieuDecesDto save(LieuDecesDto causeDecesDto) ;


	public LieuDecesDto update(LieuDecesDto causeDecesDto);


	public Void delete( long id);


}


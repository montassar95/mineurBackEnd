package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.ResultatTransfertDto;

 
 


public interface ResultatTransfertService  {

	
	
	public List<ResultatTransfertDto> listTypeJuge() ;
	

	public ResultatTransfertDto getTypeJugeById( long id);


	public ResultatTransfertDto save(ResultatTransfertDto res) ;


	public ResultatTransfertDto update( ResultatTransfertDto res) ;
	
	public Void delete( long id) ;
}


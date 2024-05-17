package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.ResultatTransfert;

 
 


public interface ResultatTransfertService  {

	
	
	public List<ResultatTransfert> listTypeJuge() ;
	

	public ResultatTransfert getTypeJugeById( long id);


	public ResultatTransfert save(ResultatTransfert res) ;


	public ResultatTransfert update( ResultatTransfert res) ;
	
	public Void delete( long id) ;
}


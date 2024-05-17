package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.LieuDeces;

 
 


public interface LieuDecesService  {
	
	
	public List<LieuDeces> listCauseMutation() ;
	
	public LieuDeces getById( long id) ;

	
	public LieuDeces save(LieuDeces causeDeces) ;


	public LieuDeces update(LieuDeces causeDeces);


	public Void delete( long id);


}


package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.CommentTrouverDto;

 
 


public interface CommentTrouverService   {
	
 


	
	public List<CommentTrouverDto> listTrouver();

	
	public CommentTrouverDto getTypeAffaireById( long id);


	public CommentTrouverDto save( CommentTrouverDto causeDecesDto) ;


	public CommentTrouverDto update(CommentTrouverDto causeDecesDto);


	public Void delete(long id) ;

}


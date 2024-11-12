package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.CommentEchapperDto;

 
 


public interface CommentEchapperService   {
	
 
	public List<CommentEchapperDto> listCommentEchapper() ;
	
	
	public CommentEchapperDto getTypeAffaireById( long id) ;

	
	public CommentEchapperDto save( CommentEchapperDto causeDecesDto);

 
	public CommentEchapperDto update( CommentEchapperDto causeDecesDto);
 
	public Void delete( long id);
	 

}


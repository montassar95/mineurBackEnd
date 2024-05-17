package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.CommentEchapper;

 
 


public interface CommentEchapperService   {
	
 
	public List<CommentEchapper> listCommentEchapper() ;
	
	
	public CommentEchapper getTypeAffaireById( long id) ;

	
	public CommentEchapper save( CommentEchapper causeDeces);

 
	public CommentEchapper update( CommentEchapper causeDeces);
 
	public Void delete( long id);
	 

}


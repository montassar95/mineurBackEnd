package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.CommentTrouver;

 
 


public interface CommentTrouverService   {
	
 


	
	public List<CommentTrouver> listTrouver();

	
	public CommentTrouver getTypeAffaireById( long id);


	public CommentTrouver save( CommentTrouver causeDeces) ;


	public CommentTrouver update(CommentTrouver causeDeces);


	public Void delete(long id) ;

}


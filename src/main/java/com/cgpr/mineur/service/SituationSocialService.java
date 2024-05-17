package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.SituationSocial;

 
 

public interface SituationSocialService {

	
	public List<SituationSocial> listNationalite();

	
	public SituationSocial getById( long id) ;

	
	public SituationSocial save( SituationSocial situationFamiliale);

	
	public SituationSocial update( SituationSocial situationFamiliale);

	
	public Void delete( long id);
}


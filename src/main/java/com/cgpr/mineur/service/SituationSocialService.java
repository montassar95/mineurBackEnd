package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.SituationSocialDto;

 
 

public interface SituationSocialService {

	
	public List<SituationSocialDto> listNationalite();

	
	public SituationSocialDto getById( long id) ;

	
	public SituationSocialDto save( SituationSocialDto situationFamiliale);

	
	public SituationSocialDto update( SituationSocialDto situationFamiliale);

	
	public Void delete( long id);
}


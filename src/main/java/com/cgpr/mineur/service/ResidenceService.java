package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.ResidenceDto;
import com.cgpr.mineur.dto.ResidenceIdDto;
import com.cgpr.mineur.models.Residence;


public interface ResidenceService {


	public ResidenceDto trouverDerniereResidenceParNumDetentionEtIdDetenu( String idEnfant,long numOrdinale) ;
	public List<ResidenceDto> trouverResidencesDetentionActiveParIdDetenu(String idEnfant) ;
	public ResidenceDto  save(ResidenceDto  residanceDto)  ;
	public ResidenceDto accepterDemandeMutation(ResidenceDto residance) ;
	public ResidenceDto supprimerDemandeMutation(ResidenceIdDto residanceId);
	public ResidenceDto supprimerAcceptationMutation( ResidenceIdDto residanceId) ;
	public Boolean validerNumeroEcrou(String numeroEcrou, String etablissementId);
 
	
	
	 

	

}

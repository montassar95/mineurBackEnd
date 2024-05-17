package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.ResidenceId;


public interface ResidenceService {



	public  List<Residence>  list() ;

	
	public Residence geById( String id, long numOrdinale);

	
	public List<Residence> findByEnfantAndArrestation(String id, long numOrdinale) ;

	
	public Residence findByArrestationAndStatut0( String idEnfant, long numOrdinale);

	
	public Residence findByArrestationAndMaxResidence( String idEnfant,long numOrdinale) ;


	public List<Residence> findByIdEnfantAndStatutArrestation0(String idEnfant) ;


	public Residence save(Residence residance)  ;

	
	public Residence accepterResidence(Residence residance) ;


	public Residence update(Residence residance);
	
	public Object countTotaleRecidence(String idEnfant, long numOrdinaleArrestation);
	
	public Object countTotaleRecidenceWithetabChangeManiere(String idEnfant, long numOrdinaleArrestation) ;

	public Residence deleteResidenceStatut2(ResidenceId residanceId);
 


	public Residence deleteResidenceStatut0( ResidenceId residanceId) ;

}

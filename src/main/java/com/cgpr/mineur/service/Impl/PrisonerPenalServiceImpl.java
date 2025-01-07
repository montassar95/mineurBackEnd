package com.cgpr.mineur.service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.config.Simplification;
import com.cgpr.mineur.dto.PrisonerDto;
import com.cgpr.mineur.dto.PrisonerPenaleDto;
import com.cgpr.mineur.dto.SearchDetenuDto;
//import com.cgpr.mineur.repository.PersonelleRepository;
import com.cgpr.mineur.repository.PrisonerPenalRepository;
import com.cgpr.mineur.resource.EnfantDTO;
import com.cgpr.mineur.service.PrisonerPenalService;

@Service
public class PrisonerPenalServiceImpl implements PrisonerPenalService {

	@Autowired
	private PrisonerPenalRepository prisonerPenalRepository;

	Simplification simplifier = new Simplification();
	
	 private String simplifyOrNull(String value) {
		    String simplified =  value != null ? value.trim() : "" ;
		    return simplified.isEmpty() ? null : simplified;
		}
	@Override
	public List<SearchDetenuDto> findPrisonerPenalByCriteria(EnfantDTO enfantDTO) {
	    if (enfantDTO == null) {
	        throw new IllegalArgumentException("Le paramètre enfantDTO ne peut pas être nul.");
	    }

	    // Simplification des valeurs d'entrée
	    String nomSimplifie = simplifier.simplify(enfantDTO.getNom());
	    String prenomSimplifie = simplifier.simplify(enfantDTO.getPrenom());
	    String nomPereSimplifie = simplifier.simplify(enfantDTO.getNomPere());
	    LocalDate dateNaissance = enfantDTO.getDateNaissance();

	    // Conversion du sexe en valeur utilisable (1 pour "masculin", 0 pour "féminin")
	    String sexe;
	    if ("ذكر".equals(enfantDTO.getSexe())) {
	        sexe = "1";
	    } else if ("أنثى".equals(enfantDTO.getSexe())) {
	        sexe = "0";
	    } else {
	        sexe = null; // Sexe inconnu ou non fourni
	    }

	      nomSimplifie = simplifyOrNull(nomSimplifie);
	      prenomSimplifie = simplifyOrNull(prenomSimplifie);
	      nomPereSimplifie = simplifyOrNull(nomPereSimplifie);
	    
	    
	    System.out.println("firstname : " + nomSimplifie);
	    System.out.println("father_name : " + nomPereSimplifie);
	    System.out.println("lastname : " + prenomSimplifie);
	    System.out.println("birth_date : " + dateNaissance);
	    System.out.println("sex : " + sexe);
	    
	    
	    
	    // Appel au repository
	    List<SearchDetenuDto> prisonerList = prisonerPenalRepository.findPrisonerPenalByCriteria(
	        nomSimplifie,
	        prenomSimplifie,
	        nomPereSimplifie,
	        dateNaissance,
	        sexe
	    );

	    // Retour de la liste des résultats
	    return prisonerList;
	}
	@Override
	public PrisonerPenaleDto findPrisonerPenalByPrisonerId(String prisonerId) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.findPrisonerPenalByPrisonerId(prisonerId);
	}


}

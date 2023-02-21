package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Echappes;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.EchappesRepository;
import com.cgpr.mineur.repository.ResidenceRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/echappes")
public class EchappesController {

	@Autowired
	private EchappesRepository echappesRepository;
	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;
 

	@PostMapping("/add")
	public ApiResponse<Echappes> save(@RequestBody Echappes echappes) {
		
		
		System.out.print(echappes.toString());
		
		
		try {

			Echappes eData  = echappesRepository.findByIdEnfantAndResidenceTrouverNull(echappes.getEchappesId().getIdEnfant());
			
			if(eData == null) {
				echappes.getResidenceEchapper().setNombreEchappes(echappes.getResidenceEchapper().getNombreEchappes()+1);
				residenceRepository.save(echappes.getResidenceEchapper());
		 	}  		
			
			else {
				 if(echappes.getResidenceTrouver() != null  ) {
						if( !(echappes.getResidenceEchapper().getEtablissement().getId().toString().trim()
								.equals(echappes.getResidenceTrouver().getEtablissement().getId().toString().trim()))  ) {
							
							System.err.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
							System.err.println(echappes.getResidenceEchapper().getEtablissement().getId());
							System.err.println(echappes.getResidenceTrouver().getEtablissement().getId());
							System.err.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
							echappes.getResidenceEchapper().setStatut(1);
							echappes.getResidenceEchapper().setDateSortie(echappes.getDateTrouver()); 
							residenceRepository.save(echappes.getResidenceEchapper());
							echappes.getResidenceTrouver().setStatut(0);
							echappes.getResidenceTrouver().setDateEntree(echappes.getDateTrouver()); 
							echappes.getResidenceTrouver().setNombreEchappes(0); 
							echappes.getResidenceTrouver().getResidenceId().setNumOrdinaleResidence(echappes.getResidenceEchapper().getResidenceId().getNumOrdinaleResidence()+1);
							residenceRepository.save(echappes.getResidenceTrouver());
							
							
							
						
						 
							
						}
				 }
			
			}
			
			
				Echappes  e = echappesRepository.save(echappes);
				
			 
						
				return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully",e);
 
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}
	
	
	@GetMapping("/countByEnfantAndArrestation/{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<Object> countByEnfant(@PathVariable("idEnfant") String idEnfant,@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {
		
		Echappes eData  = echappesRepository.findByIdEnfantAndResidenceTrouverNull(idEnfant);
		
		if(eData == null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "ok",echappesRepository.countByEnfantAndArrestation(
					idEnfant,numOrdinaleArrestation) +1);
		
				 
	} else {
		return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully",eData.getEchappesId().getNumOrdinaleEchappes());
		
 	}
		

	}
	
	
	@GetMapping("/countTotaleEchappes/{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<Object> countTotaleEchappes(@PathVariable("idEnfant") String idEnfant,@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {
		
	 
		
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "ok",echappesRepository.countByEnfantAndArrestation(
					idEnfant,numOrdinaleArrestation)  );
		
	 
		

	}
	
	
	
	@GetMapping("/findByIdEnfantAndResidenceTrouverNull/{idEnfant}")
	public ApiResponse<Echappes> findByIdEnfantAndResidenceTrouverNull(@PathVariable("idEnfant") String idEnfant) {
		 
		Echappes  cData = echappesRepository.findByIdEnfantAndResidenceTrouverNull(idEnfant);
		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}
	
	@GetMapping("/findEchappesByIdEnfantAndNumOrdinaleArrestation/{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<Echappes> findEchappesByIdEnfant(@PathVariable("idEnfant") String idEnfant,@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {
		 
		List<Echappes>   cData = echappesRepository.findEchappesByIdEnfantAndNumOrdinaleArrestation(idEnfant,numOrdinaleArrestation);
		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}
	
 
}

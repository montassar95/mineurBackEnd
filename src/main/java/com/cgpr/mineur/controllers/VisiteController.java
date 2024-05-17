package com.cgpr.mineur.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.NiveauEducatif;
import com.cgpr.mineur.models.Visite;
import com.cgpr.mineur.repository.NiveauEducatifRepository;
import com.cgpr.mineur.repository.VisiteRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/visite")
public class VisiteController {
 
	@Autowired
	private VisiteRepository visiteRepository;

 

//	@GetMapping("/getone/{id}")
//	public ApiResponse<Etablissement> getNiveauEducatifById(@PathVariable("id") long id) {
//		Optional<NiveauEducatif> Data = niveauEducatifRepository.findById(id);
//		if (Data.isPresent()) {
//			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);
//		} else {
//			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
//		}
//	}

	@PostMapping("/add")
	public ApiResponse<Visite> save(@RequestBody Visite visite) {
		System.out.println(visite.toString());
		try {
			Optional<Visite> v =visiteRepository.findbyEnfantandDate(visite.getEnfant().getId(),visite.getAnneeVisite(),visite.getMoisVisite());
			if(v.isPresent()) {
				 
				Visite visiteUpdate = v.get() ;
				if(visite.getNbrVisite()==0) {
					visiteRepository.deleteById(visiteUpdate.getEnfantIdVisite());
				}
				else {
					visiteUpdate.setNbrVisite(visite.getNbrVisite());
					visiteRepository.save(visiteUpdate);
				}
				
				
				 
				System.out.println("exist");
			}
			else {
				System.out.println("first");
				
				if(visite.getNbrVisite()>0) {
					visiteRepository.save(visite);
				}
				
			}
			
//			visiteRepository.save(visite)
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully",
					null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Etablissement not saved", null);
		}
	}

 

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			visiteRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "   Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not Deleted", null);
		}
	}
	
	 
	
		@GetMapping("/getVisite/{id}/{anneeVisite}/{moisVisite}")
		public ApiResponse<Visite> getVisite(@PathVariable("id") String id,
														  @PathVariable("anneeVisite") int anneeVisite,
						
														  @PathVariable("moisVisite") int moisVisite) {
			Optional<Visite> v =visiteRepository.findbyEnfantandDate(id, anneeVisite,moisVisite);
		 
			if (v.isPresent()) {
				return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", v.get());
			} else {
				return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
			}
		}
	
	
}

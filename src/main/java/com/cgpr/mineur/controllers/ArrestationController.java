package com.cgpr.mineur.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.ArrestationId;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Liberation;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.LiberationRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.service.ArrestationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/arrestation")
public class ArrestationController {
	@Autowired
	private ArrestationService arrestationService;

	 

	@GetMapping("/all")
	public ApiResponse<List<Arrestation>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Arrestation List Fetched Successfully.",
				arrestationService.list());
	}

	@GetMapping("/getArrestationById/{idEnfant}/{numOrdinale}")
	public ApiResponse<Arrestation> getArrestationById(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {
	 
		Arrestation cData = arrestationService.getArrestationById(idEnfant, numOrdinale);

		 
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		 
	}

	@GetMapping("/findByIdEnfantAndStatut0/{id}")
	public ApiResponse<Arrestation> findByIdEnfantAndStatut0(@PathVariable("id") String id) {
		
		
		
		Arrestation arrestationData = arrestationService.findByIdEnfantAndStatut0(id);

		 
			return new ApiResponse<>(HttpStatus.OK.value(), "arrestationData fetched suucessfully", arrestationData);
		 
	}
 
	@GetMapping("/findByIdEnfant/{id}")
	public ApiResponse<List<Arrestation>> findByIdEnfant(@PathVariable("id") String id) {

		List<Arrestation> output = arrestationService.findByIdEnfant(id);
			return new ApiResponse<>(HttpStatus.OK.value(), "arrestationData fetched suucessfully", output);
		 
	}

	@GetMapping("/countByEnfant/{id}")
	public ApiResponse<Object> countByEnfant(@PathVariable("id") String id) {

		return new ApiResponse<>(HttpStatus.OK.value(), "ok", arrestationService.countByEnfant(id));

	}

	@PostMapping("/add")
	public ApiResponse<Arrestation> save(@RequestBody Arrestation arrestation) {

		Arrestation a = arrestationService.save(arrestation);
		
			return new ApiResponse<>(HttpStatus.OK.value(), "Arrestation saved Successfully", a);
		 
	}

	@PutMapping("/update")
	public ApiResponse<Arrestation> update(@RequestBody Arrestation arrestation) {
	 
			return new ApiResponse<>(HttpStatus.OK.value(), "Arrestation updated successfully.",
					arrestationService.save(arrestation));
		 

	}

	@PostMapping("/deleteLiberation")
	public ApiResponse<Arrestation> delete(@RequestBody Arrestation arrestation) {

		 
		 
			Arrestation a = arrestationService.delete(arrestation);
			 
			return new ApiResponse<>(HttpStatus.OK.value(), "Arrestation saved Successfully", a);
		 
	}

 
}

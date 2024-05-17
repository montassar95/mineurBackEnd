package com.cgpr.mineur.controllers;

import java.util.List;
import java.util.Optional;

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

 
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.repository.ArretProvisoireRepository;
import com.cgpr.mineur.service.ArretProvisoireService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/arretProvisoire")
public class ArretProvisoireController {
	
	
	
	@Autowired
	private ArretProvisoireService arretProvisoireService;

	@GetMapping("/all")
	public ApiResponse<List<ArretProvisoire>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "ArretProvisoire List Fetched Successfully.",
				arretProvisoireService.list());
	}

 

	@PostMapping("/add")
	public ApiResponse<ArretProvisoire> save(@RequestBody ArretProvisoire arretProvisoire) {

		 
			return new ApiResponse<>(HttpStatus.OK.value(), "ArretProvisoire saved Successfully",
					arretProvisoireService.save(arretProvisoire));
		 
	}

	@PutMapping("/update")
	public ApiResponse<ArretProvisoire> update(@RequestBody ArretProvisoire arretProvisoire) {
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "ArretProvisoire updated successfully.",
					arretProvisoireService.save(arretProvisoire));
		 

	}
	
	
	
	
	@GetMapping("/getArretProvisoirebyArrestation/{idEnfant}/{numOrdinalArrestation}")
	public ApiResponse< List<ArretProvisoire>> getDocumentByArrestation(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation) {
	
		
		List<ArretProvisoire> list = arretProvisoireService.getDocumentByArrestation(idEnfant, numOrdinalArrestation);
	 	
		return new ApiResponse<>(HttpStatus.OK.value(), "ok",list);
 	
	}
	
	

	@PostMapping("/findArretProvisoireByCarteRecup")
	public ApiResponse<List<ArretProvisoire>> findArretProvisoireByCarteRecup(@RequestBody  CarteRecup carteRecup) {
	
	System.out.println(carteRecup.toString());
	
	
	
	List<ArretProvisoire> list = arretProvisoireService.findArretProvisoireByCarteRecup(carteRecup );
	 
			return new ApiResponse<>(HttpStatus.OK.value(), "    Successfully", list);
	 
	
	}
	
 
 
}

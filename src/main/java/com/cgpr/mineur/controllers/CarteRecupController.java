package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.ArretProvisoireRepository;
import com.cgpr.mineur.repository.CarteRecupRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.TypeAffaireRepository;
import com.cgpr.mineur.service.CarteRecupService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/carteRecup")
public class CarteRecupController {

	@Autowired
	private CarteRecupService carteRecupService;

 
	
	@GetMapping("/all")
	public ApiResponse<List<CarteRecup>> listEtablissement() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", carteRecupService.listEtablissement());
	}

 

 
	@PostMapping("/add")
	public ApiResponse<CarteRecup> save(@RequestBody CarteRecup carteRecup) {

		 
		CarteRecup c =carteRecupService.save(carteRecup);
 
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", c);
		 

	}

	@PutMapping("/update")
	public ApiResponse<CarteRecup> update(@RequestBody CarteRecup carteRecup) {
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					carteRecupService.save(carteRecup));
		 

	}
 
}

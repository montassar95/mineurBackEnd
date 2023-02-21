package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Personelle;
import com.cgpr.mineur.repository.EtablissementRepository;
import com.cgpr.mineur.repository.PersonelleRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/personelle")
public class PersonelleController {
	
	
	@Autowired
	private PersonelleRepository personelleRepository;

	@GetMapping("/all")
	public ApiResponse<List<Personelle>> listPersonelle() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Personelle List Fetched Successfully.",
				personelleRepository.findAll());
	}
	@PostMapping("/add")
	
	public ApiResponse<Personelle> save(@RequestBody Personelle personelle) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "personelle saved Successfully",
					personelleRepository.save(personelle));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "personelle not saved", null);
		}
	}
	
//	@DeleteMapping("/delete/{id}")
//	public ApiResponse<Void> delete(@PathVariable("id") String id) {
//		try {
//			personelleRepository.deleteById(id);
//			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "etablissement  Deleted", null);
//		} catch (Exception e) {
//			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "etablissement not Deleted", null);
//		}
//	}
	
	
	
}

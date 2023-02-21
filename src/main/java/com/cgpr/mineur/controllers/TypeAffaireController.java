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
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.repository.TypeAffaireRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/typeAffaire")
public class TypeAffaireController {

	@Autowired
	private TypeAffaireRepository typeAffaireRepository;

	@GetMapping("/all")
	public ApiResponse<List<TypeAffaire>> listTypeAffaire() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement List Fetched Successfully.",
				typeAffaireRepository.findAllByOrderByIdAsc());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<TypeAffaire> getTypeAffaireById(@PathVariable("id") long id) {
		Optional<TypeAffaire> typeData = typeAffaireRepository.findById(id);
		if (typeData.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement fetched suucessfully", typeData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "typeData Not FOund", null);
		}
	}

	@PostMapping("/add")
	public ApiResponse<TypeAffaire> save(@RequestBody TypeAffaire typeAffaire) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					typeAffaireRepository.save(typeAffaire));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<Etablissement> update(@RequestBody TypeAffaire typeAffaire) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire updated successfully.",
					typeAffaireRepository.save(typeAffaire));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			
 		  
			if (typeAffaireRepository.existsById(id)) {
				
				typeAffaireRepository.deleteById(id);
			}
			else {
				System.out.println("non pas");
		 
			}
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			System.err.println(e.toString());		
			
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}
	
	
 
}

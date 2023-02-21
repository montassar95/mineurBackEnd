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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/arretProvisoire")
public class ArretProvisoireController {
	@Autowired
	private ArretProvisoireRepository arretProvisoireRepository;

	@GetMapping("/all")
	public ApiResponse<List<ArretProvisoire>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "ArretProvisoire List Fetched Successfully.",
				arretProvisoireRepository.findAll());
	}

//	@GetMapping("/getone/{id}")
//	public ApiResponse<ArretProvisoire> getArretProvisoireById(@PathVariable("id") long id) {
//		Optional<ArretProvisoire> arretProvisoireData = arretProvisoireRepository.findById(id);
//		if (arretProvisoireData.isPresent()) {
//			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", arretProvisoireData);
//		} else {
//			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
//		}
//	}

	@PostMapping("/add")
	public ApiResponse<ArretProvisoire> save(@RequestBody ArretProvisoire arretProvisoire) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "ArretProvisoire saved Successfully",
					arretProvisoireRepository.save(arretProvisoire));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "ArretProvisoire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<ArretProvisoire> update(@RequestBody ArretProvisoire arretProvisoire) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "ArretProvisoire updated successfully.",
					arretProvisoireRepository.save(arretProvisoire));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "ArretProvisoire not Saved", null);
		}

	}
	
	
	
	
	@GetMapping("/getArretProvisoirebyArrestation/{idEnfant}/{numOrdinalArrestation}")
	public ApiResponse< List<ArretProvisoire>> getDocumentByArrestation(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation) {
		 List<ArretProvisoire> list = arretProvisoireRepository.getArretProvisoirebyArrestation(idEnfant, numOrdinalArrestation);
		 
		 if(list.isEmpty()) {
			 return new ApiResponse<>(HttpStatus.OK.value(), "nn",null);
		 }
		 else {
			 System.out.println(list.toString());
			 return new ApiResponse<>(HttpStatus.OK.value(), "ok",list);
			
		 }
		 
		 
		
	}
	
	

	@PostMapping("/findArretProvisoireByCarteRecup")
	public ApiResponse<List<ArretProvisoire>> findArretProvisoireByCarteRecup(@RequestBody  CarteRecup carteRecup) {
	
	System.out.println(carteRecup.toString());
	
	
	
	List<ArretProvisoire> list = arretProvisoireRepository.findArretProvisoireByCarteRecup(carteRecup.getDocumentId());
	System.out.println(list.toString());
	if(list.isEmpty()) {
		
		
		return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not ", null);
		
		}
	else {
			try {
			return new ApiResponse<>(HttpStatus.OK.value(), "    Successfully", list);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not  ", null);
		}
	
	}
	
	
	}
	
	
	
	
	
//	@DeleteMapping("/delete/{id}")
//	public ApiResponse<Void> delete(@PathVariable("id") long id) {
//		try {
//			arretProvisoireRepository.deleteById(id);
//			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "ArretProvisoire  Deleted", null);
//		} catch (Exception e) {
//			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "ArretProvisoire not Deleted", null);
//		}
//	}
}

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
import com.cgpr.mineur.models.Tribunal;
import com.cgpr.mineur.repository.TribunalRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tribunal")
public class TribunalController {
	@Autowired
	private TribunalRepository tribunalRepository;

	@GetMapping("/all")
	public ApiResponse<List<Tribunal>> listTribunal() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Tribunal List Fetched Successfully.",
				tribunalRepository.findAllByOrderByIdAsc());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<Tribunal> getTribunalById(@PathVariable("id") long id) {
		Optional<Tribunal> tribunalData = tribunalRepository.findById(id);
		if (tribunalData.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "Tribunal fetched suucessfully", tribunalData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "tribunal Not FOund", null);
		}
	}
	
	@GetMapping("/searchTribunal/{idGouv}/{idType}")
	public ApiResponse<Tribunal> searchTribunal(@PathVariable("idGouv") long idGouv,@PathVariable("idType") long idType) {
		List<Tribunal> tribunalData;
		if(idGouv>0 && idType==0) {
			tribunalData = tribunalRepository.findByIdGouv(idGouv);
		}
		else if(idType>0 && idGouv==0) {
			
			tribunalData = tribunalRepository.findByIdType(idType);
		}
		else {
			tribunalData = tribunalRepository.findByIdGouvAndIdType(idGouv,idType);
		}
		 
	  
		if (tribunalData.isEmpty()) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "tribunal Not FOund", null);
		} else {
			
			return new ApiResponse<>(HttpStatus.OK.value(), "Tribunal fetched suucessfully", tribunalData);
		}
	}

	@PostMapping("/add")
	public ApiResponse<Tribunal> save(@RequestBody Tribunal tribunal) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "Tribunal saved Successfully",
					tribunalRepository.save(tribunal));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "tribunal not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<Tribunal> update(@RequestBody Tribunal tribunal) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "tribunal updated successfully.",
					tribunalRepository.save(tribunal));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "tribunal not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			tribunalRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "tribunal  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "tribunal not Deleted", null);
		}
	}
}

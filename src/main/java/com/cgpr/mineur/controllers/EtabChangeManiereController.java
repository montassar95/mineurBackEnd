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
import com.cgpr.mineur.models.EtabChangeManiere;
import com.cgpr.mineur.repository.EtabChangeManiereRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/etabChangeManiere")
public class EtabChangeManiereController {
	@Autowired
	private EtabChangeManiereRepository etabChangeManiereRepository;

	@GetMapping("/all")
	public ApiResponse<List<EtabChangeManiere>> listEtablissement() {
		
		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement List Fetched Successfully.",
				etabChangeManiereRepository.findAll());
	}
	 
	@GetMapping("/getone/{id}")
	public ApiResponse<EtabChangeManiere> getEtablissementById(@PathVariable("id") String id) {
		Optional<EtabChangeManiere> etablissementData = etabChangeManiereRepository.findById(id);
		if (etablissementData.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement fetched suucessfully",
					etabChangeManiereRepository.findById(id));
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "ETablissement Not FOund", null);
		}
	}

	@PostMapping("/add")
	public ApiResponse<EtabChangeManiere> save(@RequestBody EtabChangeManiere etablissement) {
		System.out.print(etablissement.toString());
		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement saved Successfully",
					etabChangeManiereRepository.save(etablissement));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Etablissement not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<EtabChangeManiere> update(@RequestBody EtabChangeManiere etablissement) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "etablissement updated successfully.",
					etabChangeManiereRepository.save(etablissement));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "etablissement not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") String id) {
		try {
			etabChangeManiereRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "etablissement  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "etablissement not Deleted", null);
		}
	}
}

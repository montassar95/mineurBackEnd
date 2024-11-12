package com.cgpr.mineur.controllers;

import java.util.List;

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

import com.cgpr.mineur.dto.EtabChangeManiereDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.EtabChangeManiereService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/etabChangeManiere")
public class EtabChangeManiereController {
	@Autowired
	private EtabChangeManiereService etabChangeManiereService;

	@GetMapping("/all")
	public ApiResponse<List<EtabChangeManiereDto>> listEtablissement() {

		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement List Fetched Successfully.",
				etabChangeManiereService.listEtablissement());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<EtabChangeManiereDto> getEtablissementById(@PathVariable("id") String id) {
		EtabChangeManiereDto etablissementData = etabChangeManiereService.getEtablissementById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement fetched suucessfully", etablissementData);

	}

	@PostMapping("/add")
	public ApiResponse<EtabChangeManiereDto> save(@RequestBody EtabChangeManiereDto etablissement) {
		System.out.print(etablissement.toString());
		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement saved Successfully",
					etabChangeManiereService.save(etablissement));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Etablissement not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<EtabChangeManiereDto> update(@RequestBody EtabChangeManiereDto etablissement) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "etablissement updated successfully.",
					etabChangeManiereService.update(etablissement));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "etablissement not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") String id) {
		try {
			etabChangeManiereService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "etablissement  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "etablissement not Deleted", null);
		}
	}
}

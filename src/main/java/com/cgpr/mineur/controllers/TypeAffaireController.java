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

import com.cgpr.mineur.dto.TypeAffaireDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.TypeAffaireService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/typeAffaire")
public class TypeAffaireController {

	@Autowired
	private TypeAffaireService typeAffaireService;

	@GetMapping("/all")
	public ApiResponse<List<TypeAffaireDto>> listTypeAffaire() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement List Fetched Successfully.",
				typeAffaireService.listTypeAffaire());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<TypeAffaireDto> getTypeAffaireById(@PathVariable("id") long id) {
		TypeAffaireDto typeData = typeAffaireService.getTypeAffaireById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement fetched suucessfully", typeData);

	}

	@PostMapping("/add")
	public ApiResponse<TypeAffaireDto> save(@RequestBody TypeAffaireDto typeAffaire) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					typeAffaireService.save(typeAffaire));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<TypeAffaireDto> update(@RequestBody TypeAffaireDto typeAffaire) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire updated successfully.",
					typeAffaireService.save(typeAffaire));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {

			typeAffaireService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			System.err.println(e.toString());

			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}

}

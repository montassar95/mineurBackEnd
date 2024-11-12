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

import com.cgpr.mineur.dto.NiveauEducatifDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.NiveauEducatifService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/niveauEducatif")
public class NiveauEducatifController {
	@Autowired
	private NiveauEducatifService niveauEducatifService;

	@GetMapping("/all")
	public ApiResponse<List<NiveauEducatifDto>> listNiveauEducatif() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				niveauEducatifService.listNiveauEducatif());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<NiveauEducatifDto> getNiveauEducatifById(@PathVariable("id") long id) {
		NiveauEducatifDto Data = niveauEducatifService.getNiveauEducatifById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);

	}

	@PostMapping("/add")
	public ApiResponse<NiveauEducatifDto> save(@RequestBody NiveauEducatifDto niveauEducatif) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully",
					niveauEducatifService.save(niveauEducatif));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Etablissement not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<NiveauEducatifDto> update(@RequestBody NiveauEducatifDto niveauEducatif) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					niveauEducatifService.save(niveauEducatif));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			niveauEducatifService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "   Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not Deleted", null);
		}
	}
}

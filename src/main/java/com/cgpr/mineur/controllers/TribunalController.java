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

import com.cgpr.mineur.dto.TribunalDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.TribunalService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tribunal")
public class TribunalController {
	@Autowired
	private TribunalService tribunalService;

	@GetMapping("/all")
	public ApiResponse<List<TribunalDto>> listTribunal() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Tribunal List Fetched Successfully.",
				tribunalService.listTribunal());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<TribunalDto> getTribunalById(@PathVariable("id") long id) {
		TribunalDto tribunalData = tribunalService.getTribunalById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "Tribunal fetched suucessfully", tribunalData);

	}

	@GetMapping("/chercherTribunalParGouvernoratEtTypeTribunal/{idGouv}/{idType}")
	public ApiResponse<List<TribunalDto>> chercherTribunalParGouvernoratEtTypeTribunal(
			@PathVariable("idGouv") long idGouv, @PathVariable("idType") long idType) {
		List<TribunalDto> tribunalData;

		tribunalData = tribunalService.searchTribunal(idGouv, idType);

		return new ApiResponse<>(HttpStatus.OK.value(), "Tribunal fetched suucessfully", tribunalData);

	}

	@PostMapping("/add")
	public ApiResponse<TribunalDto> save(@RequestBody TribunalDto tribunal) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "Tribunal saved Successfully",
					tribunalService.save(tribunal));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "tribunal not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<TribunalDto> update(@RequestBody TribunalDto tribunal) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "tribunal updated successfully.",
					tribunalService.save(tribunal));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "tribunal not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			tribunalService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "tribunal  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "tribunal not Deleted", null);
		}
	}
}

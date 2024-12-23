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

import com.cgpr.mineur.dto.TypeTribunalDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.TypeTribunalService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/typeTribunal")
public class TypeTribunalController {
	@Autowired
	private TypeTribunalService typeTribunalService;

	@GetMapping("/all")
	public ApiResponse<List<TypeTribunalDto>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", typeTribunalService.list());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<TypeTribunalDto> getById(@PathVariable("id") long id) {
		TypeTribunalDto Data = typeTribunalService.getById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);

	}

	@PostMapping("/add")
	public ApiResponse<TypeTribunalDto> save(@RequestBody TypeTribunalDto causeDeces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					typeTribunalService.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<TypeTribunalDto> update(@RequestBody TypeTribunalDto causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					typeTribunalService.update(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			typeTribunalService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}

}

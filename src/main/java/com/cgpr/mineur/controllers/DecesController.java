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

import com.cgpr.mineur.dto.DecesDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.DecesService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/deces")
public class DecesController {
	@Autowired
	private DecesService decesService;

	@GetMapping("/all")
	public ApiResponse<List<DecesDto>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", decesService.list());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<DecesDto> getById(@PathVariable("id") long id) {
		DecesDto Data = decesService.getById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);

	}

	@PostMapping("/add")
	public ApiResponse<DecesDto> save(@RequestBody DecesDto deces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant saved Successfully", decesService.save(deces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Enfant not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<DecesDto> update(@RequestBody DecesDto causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.", decesService.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			decesService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}

}

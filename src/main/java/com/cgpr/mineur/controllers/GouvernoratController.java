package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.GouvernoratDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.GouvernoratService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/gouvernorat")
public class GouvernoratController {
	@Autowired
	private GouvernoratService gouvernoratService;
//	, produces = MediaType.APPLICATION_JSON_VALUE
	@GetMapping(  "/all")
	public ApiResponse<List<GouvernoratDto>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", gouvernoratService.list());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<GouvernoratDto> getById(@PathVariable("id") long id) {
		GouvernoratDto Data = gouvernoratService.getById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);

	}

	@PostMapping("/add")
	public ApiResponse<GouvernoratDto> save(@RequestBody GouvernoratDto gouv) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					gouvernoratService.save(gouv));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<GouvernoratDto> update(@RequestBody GouvernoratDto gouv) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.", gouvernoratService.save(gouv));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			gouvernoratService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}

}

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

import com.cgpr.mineur.dto.MotifArreterlexecutionDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.MotifArreterlexecutionService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/motifArreterlexecution")
public class MotifArreterlexecutionController {

	@Autowired
	private MotifArreterlexecutionService motifArreterlexecutionService;

	@GetMapping("/all")
	public ApiResponse<List<MotifArreterlexecutionDto>> listMotifArreterlexecution() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				motifArreterlexecutionService.listMotifArreterlexecution());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<MotifArreterlexecutionDto> getById(@PathVariable("id") long id) {
		MotifArreterlexecutionDto Data = motifArreterlexecutionService.getById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);

	}

	@PostMapping("/add")
	public ApiResponse<MotifArreterlexecutionDto> save(@RequestBody MotifArreterlexecutionDto causeDeces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					motifArreterlexecutionService.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<MotifArreterlexecutionDto> update(@RequestBody MotifArreterlexecutionDto causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					motifArreterlexecutionService.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			motifArreterlexecutionService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}

}
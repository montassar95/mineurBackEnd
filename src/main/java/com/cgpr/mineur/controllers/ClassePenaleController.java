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

import com.cgpr.mineur.dto.ClassePenaleDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.ClassePenaleService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/classePenale")
public class ClassePenaleController {
	@Autowired
	private ClassePenaleService classePenaleService;

	@GetMapping("/all")
	public ApiResponse<List<ClassePenaleDto>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", classePenaleService.list());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<ClassePenaleDto> getById(@PathVariable("id") long id) {
		ClassePenaleDto Data = classePenaleService.getById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);

	}

	@PostMapping("/add")
	public ApiResponse<ClassePenaleDto> save(@RequestBody ClassePenaleDto causeDeces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					classePenaleService.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<ClassePenaleDto> update(@RequestBody ClassePenaleDto causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					classePenaleService.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			classePenaleService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}

}

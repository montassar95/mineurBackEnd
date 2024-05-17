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
import com.cgpr.mineur.models.TypeJuge;
import com.cgpr.mineur.repository.TypeJugeRepository;
import com.cgpr.mineur.service.TypeJugeService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/typeJuge")
public class TypeJugeController {

	@Autowired
	private TypeJugeService typeJugeService;

	@GetMapping("/all")
	public ApiResponse<List<TypeJuge>> listTypeJuge() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", typeJugeService.listTypeJuge());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<TypeJuge> getTypeJugeById(@PathVariable("id") long id) {
	 TypeJuge  typeData = typeJugeService.getTypeJugeById(id);
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement fetched suucessfully", typeData);
		 
	}

	@PostMapping("/add")
	public ApiResponse<TypeJuge> save(@RequestBody TypeJuge typeJuge) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeJuge saved Successfully",
					typeJugeService.save(typeJuge));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeJuge not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<TypeJuge> update(@RequestBody TypeJuge typeJuge) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "TypeJuge updated successfully.",
					typeJugeService.save(typeJuge));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeJuge not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			typeJugeService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeJuge  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeJuge not Deleted", null);
		}
	}
}

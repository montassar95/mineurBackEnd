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
import com.cgpr.mineur.models.ResultatTransfert;
import com.cgpr.mineur.repository.ResultatTransfertRepository;
import com.cgpr.mineur.service.ResultatTransfertService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/resultatTransfert")
public class ResultatTransfertController{

	@Autowired
	private ResultatTransfertService resultatTransfertService;

	@GetMapping("/all")
	public ApiResponse<List<ResultatTransfert>> listTypeJuge() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", resultatTransfertService.listTypeJuge());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<ResultatTransfert> getTypeJugeById(@PathVariable("id") long id) {
		 ResultatTransfert  typeData = resultatTransfertService.getTypeJugeById(id);
	 
			return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement fetched suucessfully", typeData);
		 
	}

	@PostMapping("/add")
	public ApiResponse<ResultatTransfert> save(@RequestBody ResultatTransfert res) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeJuge saved Successfully",
					resultatTransfertService.save(res));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeJuge not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<ResultatTransfert> update(@RequestBody ResultatTransfert res) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "TypeJuge updated successfully.",
					resultatTransfertService.update(res));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeJuge not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			resultatTransfertService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeJuge  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeJuge not Deleted", null);
		}
	}
}

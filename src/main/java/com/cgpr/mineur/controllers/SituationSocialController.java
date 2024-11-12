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

import com.cgpr.mineur.dto.SituationSocialDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.SituationSocialService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/situationSocial")
public class SituationSocialController {
	@Autowired
	private SituationSocialService situationSocialService;

	@GetMapping("/all")
	public ApiResponse<List<SituationSocialDto>> listNationalite() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				situationSocialService.listNationalite());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<SituationSocialDto> getById(@PathVariable("id") long id) {
		SituationSocialDto Data = situationSocialService.getById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);

	}

	@PostMapping("/add")
	public ApiResponse<SituationSocialDto> save(@RequestBody SituationSocialDto situationFamiliale) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully",
					situationSocialService.save(situationFamiliale));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<SituationSocialDto> update(@RequestBody SituationSocialDto situationFamiliale) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					situationSocialService.update(situationFamiliale));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			situationSocialService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "   Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not Deleted", null);
		}
	}
}

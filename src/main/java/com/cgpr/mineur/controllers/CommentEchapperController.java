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

import com.cgpr.mineur.dto.CommentEchapperDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.CommentEchapperService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/commentEchapper")
public class CommentEchapperController {

	@Autowired
	private CommentEchapperService commentEchapperService;

	@GetMapping("/all")
	public ApiResponse<List<CommentEchapperDto>> listCommentEchapper() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				commentEchapperService.listCommentEchapper());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<CommentEchapperDto> getTypeAffaireById(@PathVariable("id") long id) {
		CommentEchapperDto typeData = commentEchapperService.getTypeAffaireById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", typeData);

	}

	@PostMapping("/add")
	public ApiResponse<CommentEchapperDto> save(@RequestBody CommentEchapperDto causeDeces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					commentEchapperService.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<CommentEchapperDto> update(@RequestBody CommentEchapperDto causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					commentEchapperService.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			commentEchapperService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}

}

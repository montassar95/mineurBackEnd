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
 
import com.cgpr.mineur.models.CommentEchapper;
import com.cgpr.mineur.repository.CommentEchapperRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/commentEchapper")
public class CommentEchapperController {
	
	
	@Autowired
	private CommentEchapperRepository commentEchapperRepository;

	
	
	@GetMapping("/all")
	public ApiResponse<List<CommentEchapper>> listCommentEchapper() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				commentEchapperRepository.findAllByOrderByIdAsc());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<CommentEchapper> getTypeAffaireById(@PathVariable("id") long id) {
		Optional<CommentEchapper> typeData = commentEchapperRepository.findById(id);
		if (typeData.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", typeData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "typeData Not FOund", null);
		}
	}

	@PostMapping("/add")
	public ApiResponse<CommentEchapper> save(@RequestBody CommentEchapper causeDeces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					commentEchapperRepository.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<CommentEchapper> update(@RequestBody CommentEchapper causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					commentEchapperRepository.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			commentEchapperRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}
 
}

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
 
import com.cgpr.mineur.models.CommentTrouver;
import com.cgpr.mineur.repository.CommentEchapperRepository;
import com.cgpr.mineur.repository.CommentTrouverRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/commentTrouver")
public class CommentTrouverController {
	@Autowired
	private CommentTrouverRepository commentTrouverRepository;

	@GetMapping("/all")
	public ApiResponse<List<CommentTrouver>> listTrouver() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				commentTrouverRepository.findAllByOrderByIdAsc());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<CommentTrouver> getTypeAffaireById(@PathVariable("id") long id) {
		Optional<CommentTrouver> typeData = commentTrouverRepository.findById(id);
		if (typeData.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", typeData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "typeData Not FOund", null);
		}
	}

	@PostMapping("/add")
	public ApiResponse<CommentTrouver> save(@RequestBody CommentTrouver causeDeces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					commentTrouverRepository.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<CommentTrouver> update(@RequestBody CommentTrouver causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					commentTrouverRepository.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			commentTrouverRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}
}

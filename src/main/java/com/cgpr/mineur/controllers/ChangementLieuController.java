package com.cgpr.mineur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.ChangementLieuDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.ChangementLieuService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/changementLieu")
public class ChangementLieuController {

	@Autowired
	private ChangementLieuService changementLieuService;

	@PostMapping("/add")
	public ApiResponse<ChangementLieuDto> save(@RequestBody ChangementLieuDto changementLieu) {

		ChangementLieuDto c = changementLieuService.save(changementLieu);

		return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);

	}

}

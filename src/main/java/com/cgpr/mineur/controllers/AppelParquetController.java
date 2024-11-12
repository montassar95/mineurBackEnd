package com.cgpr.mineur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.AppelParquetDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.AppelParquetService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/appelParquet")
public class AppelParquetController {

	@Autowired
	private AppelParquetService appelParquetService;

	@PostMapping("/add")
	public ApiResponse<AppelParquetDto> save(@RequestBody AppelParquetDto appelParquet) {

		appelParquetService.save(appelParquet);

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

}

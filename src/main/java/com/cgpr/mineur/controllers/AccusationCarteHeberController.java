package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.service.AccusationCarteHeberService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/accusationCarteHeber")
public class AccusationCarteHeberController {


	
	@Autowired
	private AccusationCarteHeberService accusationCarteHeberService;

	
	
	@PostMapping("/add")
	public ApiResponse<AccusationCarteHeber> save(@RequestBody AccusationCarteHeber accusationCarteHeber) {

		accusationCarteHeberService.save(accusationCarteHeber);

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

	@PostMapping("/findTitreAccusationbyCarteHeber")
	public ApiResponse<List<TitreAccusation>> findTitreAccusationbyCarteHeber(@RequestBody CarteHeber carteHeber) {

		List<TitreAccusation> list = accusationCarteHeberService
				.findTitreAccusationbyCarteHeber(carteHeber );
		System.out.println(list.toString());
		if (list.isEmpty()) {

			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not ", null);

		} else {
			try {
				return new ApiResponse<>(HttpStatus.OK.value(), "    Successfully", list);
			} catch (Exception e) {
				return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not  ", null);
			}

		}

	}

}

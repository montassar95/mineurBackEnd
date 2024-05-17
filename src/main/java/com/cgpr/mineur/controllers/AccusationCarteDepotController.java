package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.service.AccusationCarteDepotService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/accusationCarteDepot")
public class AccusationCarteDepotController {

 

	@Autowired
	private AccusationCarteDepotService accusationCarteDepotService;

	
	@PostMapping("/add")
	public ApiResponse<AccusationCarteDepot> save(@RequestBody AccusationCarteDepot accusationCarteDepot) {

		accusationCarteDepotService.save(accusationCarteDepot);

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

	@PostMapping("/findTitreAccusationbyCarteDepot")
	public ApiResponse<List<TitreAccusation>> findTitreAccusationbyCarteDepot(@RequestBody CarteDepot carteDepot) {

		List<TitreAccusation> list = accusationCarteDepotService
				.findTitreAccusationbyCarteDepot(carteDepot );
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

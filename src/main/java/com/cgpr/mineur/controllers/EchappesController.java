package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Echappes;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.EchappesRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.service.EchappesService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/echappes")
public class EchappesController {

	@Autowired
	private EchappesService echappesService;
 

	@PostMapping("/add")
	public ApiResponse<Echappes> save(@RequestBody Echappes echappes) {

		System.out.print(echappes.toString());

		try {
 
			Echappes e = echappesService.save(echappes);

			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully", e);

		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

	@GetMapping("/countByEnfantAndArrestation/{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<Object> countByEnfant(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {

		Object eData = echappesService.countByEnfant(idEnfant, numOrdinaleArrestation);

		 
			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully", eData);
 

	}

	@GetMapping("/countTotaleEchappes/{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<Object> countTotaleEchappes(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {

		return new ApiResponse<>(HttpStatus.OK.value(), "ok",
				echappesService.countTotaleEchappes(idEnfant, numOrdinaleArrestation));

	}

	@GetMapping("/findByIdEnfantAndResidenceTrouverNull/{idEnfant}")
	public ApiResponse<Echappes> findByIdEnfantAndResidenceTrouverNull(@PathVariable("idEnfant") String idEnfant) {

		Echappes cData = echappesService.findByIdEnfantAndResidenceTrouverNull(idEnfant);
		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}

	@GetMapping("/findEchappesByIdEnfantAndNumOrdinaleArrestation/{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<List<Echappes>> findEchappesByIdEnfant(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {

		List<Echappes> cData = echappesService.findEchappesByIdEnfant(idEnfant, numOrdinaleArrestation);
		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}

}

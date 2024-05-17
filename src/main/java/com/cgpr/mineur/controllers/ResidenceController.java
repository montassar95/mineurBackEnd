package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.ResidenceId;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.service.ResidenceService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/residence")
public class ResidenceController {

	@Autowired
	private ResidenceService residenceService;

	 
	@GetMapping("/all")
	public ApiResponse<List<Residence>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement List Fetched Successfully.",
				residenceService.list());
	}

	@GetMapping("/findByIdEnfantAndStatutEnCour/{id}/{numOrdinale}")
	public ApiResponse<Residence> geById(@PathVariable("id") String id, @PathVariable("numOrdinale") long numOrdinale) {
		Residence aData = residenceService.geById(id, numOrdinale);
		if (aData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", aData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", null);
		}
	}

	@GetMapping("/findByEnfantAndArrestation/{id}/{numOrdinale}")
	public ApiResponse<List<Residence>> findByEnfantAndArrestation(@PathVariable("id") String id,
			@PathVariable("numOrdinale") long numOrdinale) {
		List<Residence> cData = residenceService.findByEnfantAndArrestation(id, numOrdinale);
		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}

	}

	@GetMapping("/findByIdEnfantAndStatut0/{idEnfant}/{numOrdinale}")
	public ApiResponse<Residence> findByArrestationAndStatut0(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		// Residence cData = residenceRepository.findMaxResidence(idEnfant,numOrdinale);
		Residence cData = residenceService.findByArrestationAndStatut0(idEnfant, numOrdinale);
		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}

	@GetMapping("/findByIdEnfantAndMaxResidence/{idEnfant}/{numOrdinale}")
	public ApiResponse<Residence> findByArrestationAndMaxResidence(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		Residence cData = residenceService.findByArrestationAndMaxResidence(idEnfant, numOrdinale);
		// Residence cData =
		// residenceRepository.findByIdEnfantAndStatut0(idEnfant,numOrdinale);
		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}

	@GetMapping("/findByIdEnfantAndStatutArrestation0/{idEnfant}")
	public ApiResponse<List<Residence>> findByIdEnfantAndStatutArrestation0(@PathVariable("idEnfant") String idEnfant) {

		List<Residence> cData = residenceService.findByIdEnfantAndStatutArrestation0(idEnfant);
		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}

	@PostMapping("/add")
	public ApiResponse<Residence> save(@RequestBody Residence residance) {
		System.out.println(residance.toString());

		try {
			Residence cData = residenceService.save(residance);
			 

				return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully",cData);
			 

		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

	@PostMapping("/accepterResidence")
	public ApiResponse<Residence> accepterResidence(@RequestBody Residence residance) {
		System.out.println(residance.toString());

		 
			Residence cData = residenceService.accepterResidence(residance );
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully", cData);
		 
	}

	@PutMapping("/update")
	public ApiResponse<Residence> update(@RequestBody Residence residance) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					residenceService.save(residance));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  not Saved", null);
		}

	}

	@GetMapping("/countTotaleRecidence/{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<Object> countTotaleRecidence(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {

		int total = (int) residenceService.countTotaleRecidence(idEnfant, numOrdinaleArrestation);
		 

		return new ApiResponse<>(HttpStatus.OK.value(), "ok", total);
	}

	@GetMapping("/countTotaleRecidenceWithetabChangeManiere/{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<Object> countTotaleRecidenceWithetabChangeManiere(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {

		 

		return new ApiResponse<>(HttpStatus.OK.value(), "ok",
				residenceService.countTotaleRecidenceWithetabChangeManiere(idEnfant, numOrdinaleArrestation));
	}

	@PostMapping("/deleteResidenceStatut2")
	public ApiResponse<Residence> deleteResidenceStatut2(@RequestBody ResidenceId residanceId) {

		try {
			residenceService.deleteResidenceStatut2(residanceId);
			 
			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

	@PostMapping("/deleteResidenceStatut0")
	public ApiResponse<Residence> deleteResidenceStatut0(@RequestBody ResidenceId residanceId) {

		try {

			Residence cData = residenceService.deleteResidenceStatut0(residanceId );
			 

			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

//	@DeleteMapping("/delete/{id}")
//	public ApiResponse<Void> delete(@PathVariable("id") long id) {
//		try {
//			residenceRepository.deleteById(id);
//			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "   Deleted", null);
//		} catch (Exception e) {
//			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not Deleted", null);
//		}
//	}
}

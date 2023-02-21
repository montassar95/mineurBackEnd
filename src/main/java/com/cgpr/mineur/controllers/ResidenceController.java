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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/residence")
public class ResidenceController {

	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;
	
	@GetMapping("/all")
	public ApiResponse<List<Residence>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement List Fetched Successfully.",
				residenceRepository.findAll());
	}

	
	@GetMapping("/findByIdEnfantAndStatutEnCour/{id}/{numOrdinale}")
	public ApiResponse<Residence> geById(@PathVariable("id") String id,@PathVariable("numOrdinale") long numOrdinale) {
		 Residence  aData = residenceRepository.findByIdEnfantAndStatutEnCour(id,numOrdinale);
		if (aData!= null ) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", aData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", null);
		   }
		}
	
	
	 
	
	
	@GetMapping("/findByEnfantAndArrestation/{id}/{numOrdinale}")
	public ApiResponse<Residence> findByEnfantAndArrestation(@PathVariable("id") String id,@PathVariable("numOrdinale") long numOrdinale) {
		 List<Residence>  cData = residenceRepository.findByEnfantAndArrestation(id,numOrdinale);
			if (cData != null) {
				return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
			} else {
				return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
			}
	
	}
	
	
	
	@GetMapping("/findByIdEnfantAndStatut0/{idEnfant}/{numOrdinale}")
	public ApiResponse<Residence> findByArrestationAndStatut0(@PathVariable("idEnfant") String idEnfant,@PathVariable("numOrdinale") long numOrdinale) {
		 
		// Residence  cData = residenceRepository.findMaxResidence(idEnfant,numOrdinale);
		Residence  cData = residenceRepository.findByIdEnfantAndStatut0(idEnfant,numOrdinale);
		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}
	
	@GetMapping("/findByIdEnfantAndMaxResidence/{idEnfant}/{numOrdinale}")
	public ApiResponse<Residence> findByArrestationAndMaxResidence(@PathVariable("idEnfant") String idEnfant,@PathVariable("numOrdinale") long numOrdinale) {
		 
		 Residence  cData = residenceRepository.findMaxResidence(idEnfant,numOrdinale);
		 // Residence  cData = residenceRepository.findByIdEnfantAndStatut0(idEnfant,numOrdinale);
		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}
	
	@GetMapping("/findByIdEnfantAndStatutArrestation0/{idEnfant}")
	public ApiResponse<Residence> findByIdEnfantAndStatutArrestation0(@PathVariable("idEnfant") String idEnfant) {
		 
		 List<Residence>  cData = residenceRepository.findByIdEnfantAndStatutArrestation0(idEnfant);
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
			Residence  cData = residenceRepository.findByIdEnfantAndStatut0(residance.getResidenceId().getIdEnfant(),residance.getArrestation().getArrestationId().getNumOrdinale());
		if(cData== null) {
			 
			Residence  r = residenceRepository.save(residance);
			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully",r);
					 
		} else {
			cData.setDateSortie(residance.getDateEntree());
			cData.setEtablissementSortie(residance.getEtablissement());
			cData.setCauseMutationSortie(residance.getCauseMutation());
			residenceRepository.save(cData);
			residance.getResidenceId().setNumOrdinaleResidence(cData.getResidenceId().getNumOrdinaleResidence()+1);
			residance.setStatut(2);
			residance.setDateEntree(null);
			residance.setEtablissementEntree(cData.getEtablissement());
			
			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully",
					residenceRepository.save(residance));
		}
			
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}
	
	@PostMapping("/accepterResidence")
	public ApiResponse<Residence> accepterResidence(@RequestBody Residence residance) {
		System.out.println(residance.toString());

		try {
			Residence  cData = residenceRepository.findByIdEnfantAndStatut0(residance.getResidenceId().getIdEnfant(),residance.getArrestation().getArrestationId().getNumOrdinale());
			cData.setStatut(1);
			residance.setStatut(0);
		 	residenceRepository.save(cData );
			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully",
					residenceRepository.save(residance));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}
	
	

	@PutMapping("/update")
	public ApiResponse<Residence> update(@RequestBody Residence residance) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					residenceRepository.save(residance));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  not Saved", null);
		}

	}
	
	
	@GetMapping("/countTotaleRecidence/{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<Object> countTotaleRecidence(@PathVariable("idEnfant") String idEnfant,@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {
		
	 int total =residenceRepository.countTotaleRecidence(
				idEnfant,numOrdinaleArrestation);
	 if(total==0) {
		 total =0;
	 }
	 else {total=total-1;}
		
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "ok", total);
	 }
	@GetMapping("/countTotaleRecidenceWithetabChangeManiere/{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<Object> countTotaleRecidenceWithetabChangeManiere(@PathVariable("idEnfant") String idEnfant,@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {
		
	 
		System.out.println(residenceRepository.countTotaleRecidenceWithetabChangeManiere(
					idEnfant,numOrdinaleArrestation)+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "ok",residenceRepository.countTotaleRecidenceWithetabChangeManiere(
					idEnfant,numOrdinaleArrestation)  );
	 }
	@PostMapping("/deleteResidenceStatut2")
	public ApiResponse<Residence> deleteResidenceStatut2(@RequestBody ResidenceId residanceId) {
	 

		try {
			residenceRepository.deleteById(residanceId);
			Residence  cData = residenceRepository.findByIdEnfantAndStatut0(residanceId.getIdEnfant(),residanceId.getNumOrdinaleArrestation());
			cData.setDateSortie(null);
			cData.setEtablissementSortie(null);
			cData.setCauseMutationSortie(null);
		 	residenceRepository.save(cData );
			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully",
					null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}
	
	
	@PostMapping("/deleteResidenceStatut0")
	public ApiResponse<Residence> deleteResidenceStatut0(@RequestBody ResidenceId residanceId) {
	 

		try {
			 
			Residence  cData = residenceRepository.findByIdEnfantAndStatut0(residanceId.getIdEnfant(),residanceId.getNumOrdinaleArrestation());
			cData.setDateSortie(null);
			cData.setDateEntree(null);
			cData.setStatut(2);
			cData.setNumArrestation(null);
			
			Residence  lastData1 = residenceRepository.findMaxResidenceWithStatut1(residanceId.getIdEnfant(),residanceId.getNumOrdinaleArrestation());
			
			lastData1.setStatut(0);
			residenceRepository.save(lastData1 );
			residenceRepository.save(cData );
			
			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully",
					null);
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

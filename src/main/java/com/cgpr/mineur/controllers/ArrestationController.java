package com.cgpr.mineur.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.ArrestationId;
import com.cgpr.mineur.models.Liberation;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.LiberationRepository;
import com.cgpr.mineur.repository.ResidenceRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/arrestation")
public class ArrestationController {
	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private LiberationRepository liberationRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private ResidenceRepository residenceRepository;

	@GetMapping("/all")
	public ApiResponse<List<Arrestation>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Arrestation List Fetched Successfully.",
				arrestationRepository.findAll());
	}

	@GetMapping("/getArrestationById/{idEnfant}/{numOrdinale}")
	public ApiResponse<Arrestation> getArrestationById(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {
		ArrestationId arrestationId = new ArrestationId(idEnfant, numOrdinale);
		Optional<Arrestation> cData = arrestationRepository.findById(arrestationId);

		if (cData.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}

	@GetMapping("/findByIdEnfantAndStatut0/{id}")
	public ApiResponse<Arrestation> findByIdEnfantAndStatut0(@PathVariable("id") String id) {
		Arrestation arrestationData = arrestationRepository.findByIdEnfantAndStatut0(id);

		if (arrestationData.getLiberation() == null) {

			List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(
					arrestationData.getArrestationId().getIdEnfant(),
					arrestationData.getArrestationId().getNumOrdinale());
			boolean allSameName = affprincipale.stream().allMatch(x -> x.getTypeDocument().equals("AEX"));
			if (allSameName) {
				arrestationData.setEtatJuridique("isAEX");

			} else {
				List<Affaire> aData = documentRepository
						.findStatutJurByArrestation(arrestationData.getArrestationId().getIdEnfant());

				if (aData.isEmpty()) {
					arrestationData.setEtatJuridique("juge");
				} else {
					arrestationData.setEtatJuridique("arret");
				}

			}
		} else {
			arrestationData.setEtatJuridique("libre");
		}

		if (arrestationData != null) {
			System.out.print(arrestationData.toString());
			return new ApiResponse<>(HttpStatus.OK.value(), "arrestationData fetched suucessfully", arrestationData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "arrestationData Not FOund", null);
		}
	}

	@GetMapping("/findByIdEnfant/{id}")
	public ApiResponse<Arrestation> findByIdEnfant(@PathVariable("id") String id) {

		System.out.println("****************************************************************************");
		List<Arrestation> arrestationData = arrestationRepository.findByIdEnfant(id);

		List<Arrestation> output = arrestationData.stream().map(s -> {

			if (s.getLiberation() == null) {

				List<Affaire> aData = documentRepository.findStatutJurByArrestation(s.getArrestationId().getIdEnfant());

				if (aData.isEmpty()) {
					s.setEtatJuridique("juge");
				} else {
					s.setEtatJuridique("arret");
				}
				System.out.println(s.getEtatJuridique().toString()+"20022023");
			} else {
				s.setEtatJuridique("libre");
			}

			 

			List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(s.getArrestationId().getIdEnfant(),	s.getArrestationId().getNumOrdinale());
				
			 
			boolean allSameName = affprincipale.stream().allMatch(x -> x.getTypeDocument().toString().equals("AEX".toString()));
					
			if (allSameName && s.getLiberation() == null) {
				s.setEtatJuridique("isAEX");
			}
		 
//			for (Affaire affaire : affprincipale) {
//				System.out.println(affaire.getAffaireId() + " " + affaire.getTypeAffaire().getStatutException() + " "
//						+ affaire.getTribunal().getTypeTribunal().getStatutNiveau() + " " + affaire.getDaysDiffJuge()
//						+ " " + affaire.getTypeAffaire().getStatutNiveau() + " " + affaire.getTypeDocument());
//
//			}
			Affaire a = affprincipale.stream().peek(num -> System.out.println("aff filter " + num.getTypeDocument()))
					.filter(x -> x.getTypeDocument().equals("AP") || x.getTypeDocument().equals("CD")
													|| x.getTypeDocument().equals("CH") || x.getTypeDocument().equals("CJA")
													|| x.getTypeDocument().equals("T") || x.getTypeDocument().equals("AE")
													|| x.getTypeDocument().equals("CP")

					)
 
 				.findFirst().orElse(null);

			 
			if (!(affprincipale.isEmpty())) {
				if (a != null) {

					s.setNumAffairePricipale(a.getAffaireId().getNumAffaire());
					s.setTribunalPricipale(a.getTribunal());
					s.setNumOrdinalAffairePricipale(a.getNumOrdinalAffaire());
					s.setTypeAffairePricipale(a.getTypeAffaire());

				} else {
					System.out.println(affprincipale.size()+"ouioui");
					a = affprincipale.stream().peek(num -> System.out.println("aff filter " + num.getTypeDocument()))
							.filter(x -> ( x.getTypeDocument().equals("CJ") &&(x.getAffaireAffecter() == null)) ).findFirst()
							
							.orElse(affprincipale.stream().peek(num -> System.out.println("aff filter " + num.getTypeDocument()))
									.filter(x -> ((x.getAffaireAffecter() == null)) ).findFirst().orElse(null));
					
					
					s.setNumAffairePricipale(a.getAffaireId().getNumAffaire());
					s.setTribunalPricipale(a.getTribunal());
					s.setNumOrdinalAffairePricipale(a.getNumOrdinalAffaire());
					s.setTypeAffairePricipale(a.getTypeAffaire());

				}
			}
 
			return s;
		}).collect(Collectors.toList());

		if (arrestationData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "arrestationData fetched suucessfully", output);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "arrestationData Not FOund", null);
		}
	}

	@GetMapping("/countByEnfant/{id}")
	public ApiResponse<Object> countByEnfant(@PathVariable("id") String id) {

		return new ApiResponse<>(HttpStatus.OK.value(), "ok", arrestationRepository.countByEnfant(id));

	}

	@PostMapping("/add")
	public ApiResponse<Arrestation> save(@RequestBody Arrestation arrestation) {

		try {
			System.out.println(arrestation.toString());
			Arrestation a = null;
//			arrestation.getArrestationId().setNumOrdinale(arrestationRepository.countByEnfant(arrestation.getArrestationId().getIdEnfant())+1);  
			if (arrestation.getLiberation() != null) {
				arrestation.setStatut(1);
				arrestation.setEtatJuridique("libre");
				liberationRepository.save(arrestation.getLiberation());
				Residence r = residenceRepository.findByIdEnfantAndStatut0(arrestation.getArrestationId().getIdEnfant(),
						arrestation.getArrestationId().getNumOrdinale());
				r.setStatut(1);
				r.setDateSortie(arrestation.getLiberation().getDate());
				if (arrestation.getLiberation().getEtabChangeManiere() != null) {
					r.setEtabChangeManiere(arrestation.getLiberation().getEtabChangeManiere());

				}
				residenceRepository.save(r);
			}
			a = arrestationRepository.save(arrestation);
			return new ApiResponse<>(HttpStatus.OK.value(), "Arrestation saved Successfully", a);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Arrestation not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<Arrestation> update(@RequestBody Arrestation arrestation) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "Arrestation updated successfully.",
					arrestationRepository.save(arrestation));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Arrestation not Saved", null);
		}

	}

	@PostMapping("/deleteLiberation")
	public ApiResponse<Arrestation> delete(@RequestBody Arrestation arrestation) {

		try {
			System.out.println(arrestation.toString());
			Arrestation a = null;
			Liberation l = null;
			if (arrestation.getLiberation() != null) {
				arrestation.setStatut(0);
				arrestation.setEtatJuridique(null);
				l = arrestation.getLiberation();
				arrestation.setLiberation(null);
				arrestationRepository.save(arrestation);

				liberationRepository.delete(l);
				Residence r = residenceRepository.findMaxResidence(arrestation.getArrestationId().getIdEnfant(),
						arrestation.getArrestationId().getNumOrdinale());
				r.setStatut(0);
				r.setEtabChangeManiere(null);
				r.setDateSortie(null);
				residenceRepository.save(r);
			}

			return new ApiResponse<>(HttpStatus.OK.value(), "Arrestation saved Successfully", a);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Arrestation not saved", null);
		}
	}

//	@DeleteMapping("/delete/{id}")
//	public ApiResponse<Void> delete(@PathVariable("id") long id) {
//		try {
//			arrestationRepository.deleteById(id);
//			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Arrestation  Deleted", null);
//		} catch (Exception e) {
//			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Arrestation not Deleted", null);
//		}
//	}
}

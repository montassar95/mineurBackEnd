package com.cgpr.mineur.controllers;

import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import com.cgpr.mineur.models.AffaireId;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.Revue;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.DocumentRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/affaire")
public class AffaireController {

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private AccusationCarteRecupRepository accusationCarteRecupRepository;

	@Autowired
	private AccusationCarteDepotRepository accusationCarteDepotRepository;

	@Autowired
	private AccusationCarteHeberRepository accusationCarteHeberRepository;

	@GetMapping("/all")
	public ApiResponse<List<Affaire>> listAffaire() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement List Fetched Successfully.",
				affaireRepository.findAll());
	}

	@GetMapping("/getAffaireById/{idEnfant}/{numAffaire}/{idTribunal}/{numOrdinaleArrestation}")
	public ApiResponse<Affaire> getAffaireById(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numAffaire") String numAffaire, @PathVariable("idTribunal") long idTribunal,
			@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {
		System.out.println("numAffaire : ");
		System.out.println(numAffaire);
		AffaireId id = new AffaireId(idEnfant, numAffaire, idTribunal, numOrdinaleArrestation);
		Optional<Affaire> aData = affaireRepository.findById(id);
		if (aData.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", aData);
		} else {
			List<Affaire> eData = affaireRepository.findAffaireByAnyArrestation(idEnfant, numAffaire, idTribunal);
			if (eData.isEmpty()) {
				return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "0", null);
			} else {
				return new ApiResponse<>(HttpStatus.OK.value(), "1", null);

			}

		}
	}

	@GetMapping("/findAffaireByAnyArrestation/{idEnfant}/{numAffaire}/{idTribunal}")
	public ApiResponse<Affaire> findAffaireByAnyArrestation(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numAffaire") String numAffaire, @PathVariable("idTribunal") long idTribunal) {

		List<Affaire> aData = affaireRepository.findAffaireByAnyArrestation(idEnfant, numAffaire, idTribunal);
		if (aData.isEmpty()) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Not Found", null);
		} else {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", aData);

		}
	}

	@GetMapping("/findAffaireByAffaireLien/{idEnfant}/{numAffaire}/{idTribunal}")
	public ApiResponse<Affaire> findAffaireByAffaireLien(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numAffaire") String numAffaire, @PathVariable("idTribunal") long idTribunal) {

		Affaire aData = affaireRepository.findAffaireByAffaireLien(idEnfant, numAffaire, idTribunal);
		if (aData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  deja lien", aData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Not Found", null);
		}
	}

	@GetMapping("/findByArrestation/{idEnfant}/{numOrdinale}")
	public ApiResponse<Affaire> findByArrestation(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		List<Affaire> lesAffaires = affaireRepository.findByArrestation(idEnfant, numOrdinale);

		System.out.println(idEnfant);
		System.out.println(numOrdinale);
		System.out.println(lesAffaires.size());

		List<Affaire> output = lesAffaires.stream().map(s -> {

			Document doc = documentRepository.getLastDocumentByAffaireforAccusation(idEnfant, numOrdinale,
					s.getNumOrdinalAffaire());
			if (doc!= null) {
			s.setTypeDocument(doc.getTypeDocument());
			 
			s.setDateEmissionDocument(doc.getDateEmission());
			  if (doc instanceof Transfert) {
				Transfert t= (Transfert) doc  ;
			 
				s.setTypeFile(t.getTypeFile());
				System.out.println("7777777777777777777777777777777777777777777777");
				System.out.println(t.getTypeFile());
			}
			  if (doc instanceof Arreterlexecution) {
				  Arreterlexecution t= (Arreterlexecution) doc  ;
				 
					s.setTypeFile(t.getTypeFile());
					System.out.println("7777777777777777777777777777777777777777777777");
					System.out.println(t.getTypeFile());
				}

			List<Document> accData = documentRepository.getDocumentByAffaireforAccusation(idEnfant, numOrdinale,
					s.getNumOrdinalAffaire(), PageRequest.of(0, 1));

			List<TitreAccusation> titreAccusations = null;

			if (accData.size() > 0) {
				if (accData.get(0) instanceof CarteRecup) {

					titreAccusations = accusationCarteRecupRepository
							.getTitreAccusationbyDocument(accData.get(0).getDocumentId());

					s.setTitreAccusations(titreAccusations);
					s.setDateEmission(accData.get(0).getDateEmission());
					System.err.print(accData.get(0));
					CarteRecup c = (CarteRecup) accData.get(0);
					s.setAnnee(c.getAnnee());
					s.setMois(c.getMois());
					s.setJour(c.getJour());
					s.setAnneeArret(c.getAnneeArretProvisoire());
					s.setMoisArret(c.getMoisArretProvisoire());
					s.setJourArret(c.getJourArretProvisoire());
					s.setTypeJuge(c.getTypeJuge());

				} else if (accData.get(0) instanceof CarteDepot) {
					titreAccusations = accusationCarteDepotRepository
							.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
					s.setTitreAccusations(titreAccusations);
					s.setDateEmission(accData.get(0).getDateEmission());
				} else if (accData.get(0) instanceof CarteHeber) {
					titreAccusations = accusationCarteHeberRepository
							.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
					s.setTitreAccusations(titreAccusations);
					s.setDateEmission(accData.get(0).getDateEmission());
					System.out.println("CarteHeber.." + accData.get(0).getDocumentId());
				}
				

			}
			
		}
			return s;
		}).collect(Collectors.toList());

		return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "okk", output);

	}

	@GetMapping("/findByArrestationToTransfert/{idEnfant}/{numOrdinale}")
	public ApiResponse<Affaire> findByArrestationToTransfert(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		List<Affaire> lesAffaires = affaireRepository.findByArrestationToTransfert(idEnfant, numOrdinale);
		if (lesAffaires.isEmpty()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", null);
		} else {

			List<Affaire> output = lesAffaires.stream().map(s -> {

				Document doc = documentRepository.getLastDocumentByAffaireforAccusation(idEnfant, numOrdinale,
						s.getNumOrdinalAffaire());

				s.setTypeDocument(doc.getTypeDocument());

				s.setDateEmissionDocument(doc.getDateEmission());

				List<Document> accData = documentRepository.getDocumentByAffaireforAccusation(idEnfant, numOrdinale,
						s.getNumOrdinalAffaire(), PageRequest.of(0, 1));

				List<TitreAccusation> titreAccusations = null;

				if (accData.size() > 0) {
					if (accData.get(0) instanceof CarteRecup) {

						titreAccusations = accusationCarteRecupRepository
								.getTitreAccusationbyDocument(accData.get(0).getDocumentId());

						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
						System.err.print(accData.get(0));
						CarteRecup c = (CarteRecup) accData.get(0);
						s.setAnnee(c.getAnnee());
						s.setMois(c.getMois());
						s.setJour(c.getJour());
						s.setAnneeArret(c.getAnneeArretProvisoire());
						s.setMoisArret(c.getMoisArretProvisoire());
						s.setJourArret(c.getJourArretProvisoire());
						s.setTypeJuge(c.getTypeJuge());

					} else if (accData.get(0) instanceof CarteDepot) {
						titreAccusations = accusationCarteDepotRepository
								.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
					} else if (accData.get(0) instanceof CarteHeber) {
						titreAccusations = accusationCarteHeberRepository
								.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
						System.out.println("CarteHeber.." + accData.get(0).getDocumentId());
					}

				}
				return s;
			}).collect(Collectors.toList());
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", output);
		}
	}

	@GetMapping("/findByArrestationToArret/{idEnfant}/{numOrdinale}")
	public ApiResponse<Affaire> findByArrestationToArret(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		List<Affaire> lesAffaires = affaireRepository.findByArrestationToArret(idEnfant, numOrdinale);
		if (lesAffaires.isEmpty()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", null);
		} else {
			List<Affaire> output = lesAffaires.stream().map(s -> {

				Document doc = documentRepository.getLastDocumentByAffaireforAccusation(idEnfant, numOrdinale,
						s.getNumOrdinalAffaire());
				s.setTypeDocument(doc.getTypeDocument());

				s.setDateEmissionDocument(doc.getDateEmission());

				List<Document> accData = documentRepository.getDocumentByAffaireforAccusation(idEnfant, numOrdinale,
						s.getNumOrdinalAffaire(), PageRequest.of(0, 1));

				List<TitreAccusation> titreAccusations = null;

				if (accData.size() > 0) {
					if (accData.get(0) instanceof CarteRecup) {

						titreAccusations = accusationCarteRecupRepository
								.getTitreAccusationbyDocument(accData.get(0).getDocumentId());

						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
						System.err.print(accData.get(0));
						CarteRecup c = (CarteRecup) accData.get(0);
						s.setAnnee(c.getAnnee());
						s.setMois(c.getMois());
						s.setJour(c.getJour());
						s.setAnneeArret(c.getAnneeArretProvisoire());
						s.setMoisArret(c.getMoisArretProvisoire());
						s.setJourArret(c.getJourArretProvisoire());
						s.setTypeJuge(c.getTypeJuge());

					} else if (accData.get(0) instanceof CarteDepot) {
						titreAccusations = accusationCarteDepotRepository
								.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
					} else if (accData.get(0) instanceof CarteHeber) {
						titreAccusations = accusationCarteHeberRepository
								.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
						System.out.println("CarteHeber.." + accData.get(0).getDocumentId());
					}

				}
				return s;
			}).collect(Collectors.toList());
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", output);
		}
	}

	@GetMapping("/findByArrestationByCJorCR/{idEnfant}/{numOrdinale}")
	public ApiResponse<Affaire> findByArrestationByCJorCR(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		List<Affaire> lesAffaires = affaireRepository.findByArrestationByCJorCR(idEnfant, numOrdinale);
		if (lesAffaires.isEmpty()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", null);
		} else {
			List<Affaire> output = lesAffaires.stream().map(s -> {

				Document doc = documentRepository.getLastDocumentByAffaireforAccusation(idEnfant, numOrdinale,
						s.getNumOrdinalAffaire());

				s.setTypeDocument(doc.getTypeDocument());

				s.setDateEmissionDocument(doc.getDateEmission());

				List<Document> accData = documentRepository.getDocumentByAffaireforAccusation(idEnfant, numOrdinale,
						s.getNumOrdinalAffaire(), PageRequest.of(0, 1));

				List<TitreAccusation> titreAccusations = null;

				if (accData.size() > 0) {
					if (accData.get(0) instanceof CarteRecup) {

						titreAccusations = accusationCarteRecupRepository
								.getTitreAccusationbyDocument(accData.get(0).getDocumentId());

						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
						System.err.print(accData.get(0));
						CarteRecup c = (CarteRecup) accData.get(0);
						s.setAnnee(c.getAnnee());
						s.setMois(c.getMois());
						s.setJour(c.getJour());
						s.setAnneeArret(c.getAnneeArretProvisoire());
						s.setMoisArret(c.getMoisArretProvisoire());
						s.setJourArret(c.getJourArretProvisoire());
						s.setTypeJuge(c.getTypeJuge());

					} else if (accData.get(0) instanceof CarteDepot) {
						titreAccusations = accusationCarteDepotRepository
								.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
					} else if (accData.get(0) instanceof CarteHeber) {
						titreAccusations = accusationCarteHeberRepository
								.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
						System.out.println("CarteHeber.." + accData.get(0).getDocumentId());
					}

				}
				return s;
			}).collect(Collectors.toList());
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", output);
		}
	}

	@GetMapping("/findByArrestationToPropaga/{idEnfant}/{numOrdinale}")
	public ApiResponse<Affaire> findByArrestationByCDorCHorCP(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		List<Affaire> lesAffaires = affaireRepository.findByArrestationToPropaga(idEnfant, numOrdinale);
		if (lesAffaires.isEmpty()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", null);
		} else {
			List<Affaire> output = lesAffaires.stream().map(s -> {

				Document doc = documentRepository.getLastDocumentByAffaireforAccusation(idEnfant, numOrdinale,
						s.getNumOrdinalAffaire());

				s.setTypeDocument(doc.getTypeDocument());

				s.setDateEmissionDocument(doc.getDateEmission());

				List<Document> accData = documentRepository.getDocumentByAffaireforAccusation(idEnfant, numOrdinale,
						s.getNumOrdinalAffaire(), PageRequest.of(0, 1));

				List<TitreAccusation> titreAccusations = null;

				if (accData.size() > 0) {
					if (accData.get(0) instanceof CarteRecup) {

						titreAccusations = accusationCarteRecupRepository
								.getTitreAccusationbyDocument(accData.get(0).getDocumentId());

						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
						System.err.print(accData.get(0));
						CarteRecup c = (CarteRecup) accData.get(0);
						s.setAnnee(c.getAnnee());
						s.setMois(c.getMois());
						s.setJour(c.getJour());
						s.setAnneeArret(c.getAnneeArretProvisoire());
						s.setMoisArret(c.getMoisArretProvisoire());
						s.setJourArret(c.getJourArretProvisoire());
						s.setTypeJuge(c.getTypeJuge());

					} else if (accData.get(0) instanceof CarteDepot) {
						titreAccusations = accusationCarteDepotRepository
								.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
					} else if (accData.get(0) instanceof CarteHeber) {
						titreAccusations = accusationCarteHeberRepository
								.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
						System.out.println("CarteHeber.." + accData.get(0).getDocumentId());
					}

				}
				return s;
			}).collect(Collectors.toList());
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", output);
		}
	}

	@GetMapping("/findByNumOrdinalAffaire/{idEnfant}/{numOrdinale}/{numOrdinalAffaire}")
	public ApiResponse<Affaire> findByNumOrdinalAffaire(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale, @PathVariable("numOrdinalAffaire") long numOrdinalAffaire) {

		List<Affaire> aData = affaireRepository.findByNumOrdinalAffaire(idEnfant, numOrdinale, numOrdinalAffaire);
		if (aData.isEmpty()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", null);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", aData);
		}
	}

	@PostMapping("/verifierNumOrdinalAffaire/{numOrdinaleArrestationActuelle}")
	public ApiResponse<Affaire> verifierNumOrdinalAffaire(@RequestBody Affaire affaire,
			@PathVariable("numOrdinaleArrestationActuelle") long numOrdinaleArrestationActuelle) {

		int sommeAffaire = affaireRepository.countAffaire(affaire.getArrestation().getArrestationId());
		System.out.println("sommeAffaire");
		System.out.println(sommeAffaire);
		int maxAffaire = 0;
		if (sommeAffaire > 0) {
			maxAffaire = affaireRepository.maxAffaire(affaire.getArrestation().getArrestationId());
		}

		try {
			System.out.println(affaire.toString());
			System.out.println(numOrdinaleArrestationActuelle);
//------------------------------------------ test si l'affaire origine existe ou nn------------------------------------------------------------------------------------------
			Optional<Affaire> affaireChercher = affaireRepository.findById(affaire.getAffaireId());

//------------------------------------------l'affaire origine existe ------------------------------------------------------------------------------------------
			if (affaireChercher.isPresent()) {
				System.out.println("j'ai trouvé l'affaire principale");

//------------------------------------------tester  ------------------------------------------------------------------------------------------
				if (affaireChercher.get().getArrestation().getArrestationId()
						.getNumOrdinale() == numOrdinaleArrestationActuelle) {
					System.out.println("l'affaire d origine mm aresstation");
					affaire.setNumOrdinalAffaire(affaireChercher.get().getNumOrdinalAffaire());
				} else {
					System.out.println("l'affaire d origine   pas  mm aresstation");
					affaire.setNumOrdinalAffaire(maxAffaire + 1);
				}

				return new ApiResponse<>(HttpStatus.OK.value(), " Affaire saved Successfully", affaire);
			} else {
				System.out.println("j'ai pas trouvé l'affaire principale");

				if (affaire.getAffaireLien() != null) {

					System.out.println("l'affaire de lien n'est pas null");
					Optional<Affaire> affaireLienChercher = affaireRepository
							.findById(affaire.getAffaireLien().getAffaireId());

					if (affaireLienChercher.isPresent()) {

						if (affaireLienChercher.get().getArrestation().getArrestationId().getNumOrdinale() == affaire
								.getArrestation().getArrestationId().getNumOrdinale()) {
							System.out.println("l'affaire de lien mm aresstation");
							affaire.setNumOrdinalAffaire(affaire.getAffaireLien().getNumOrdinalAffaire());
						} else {
							System.out.println("l'affaire de lien pas  mm aresstation");
							affaire.setNumOrdinalAffaire(maxAffaire + 1);
						}

					} else {
						affaire.getAffaireLien().setNumOrdinalAffaire(maxAffaire + 1);
						affaire.setNumOrdinalAffaire(maxAffaire + 1);
					}

				} else {
					System.out.println("l'affaire de lien est null");
					affaire.setNumOrdinalAffaire(maxAffaire + 1);
					System.out.println(maxAffaire + 1);
					System.out.println(affaire.getArrestation());
				}

				affaire.getAffaireId().setNumOrdinaleArrestation(numOrdinaleArrestationActuelle);
				// Affaire affaireEnregistrer =affaireRepository.save(affaire);

				return new ApiResponse<>(HttpStatus.OK.value(), " Affaire saved Successfully", affaire);

			}

		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<Affaire> update(@RequestBody Affaire affaire) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.", affaireRepository.save(affaire));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  not Saved", null);
		}

	}

	@GetMapping("/getDateDebutPunition/{idEnfant}/{numOrdinale}")
	public ApiResponse<Object> getDateDebutPunition(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		Date aData = affaireRepository.getDateDebutPunition(idEnfant, numOrdinale);
		if (aData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", aData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", null);
		}
	}

	@GetMapping("/getDateFinPunition/{idEnfant}/{numOrdinale}")
	public ApiResponse<Object> getDateFinPunition(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		Date aData = affaireRepository.getDateFinPunition(idEnfant, numOrdinale);
		if (aData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", aData);
		} else {

			List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(idEnfant, numOrdinale);
			if (affprincipale.isEmpty()) {
				return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", null);
			} else {
				boolean allSameName = affprincipale.stream().allMatch(x -> x.getTypeDocument().equals("AEX"));
				if (allSameName) {
					System.err.println(allSameName);
					aData = affprincipale.get(0).getDocuments().get(affprincipale.get(0).getDocuments().size() - 1)
							.getDateEmission();
					return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", aData);
				} else {
					return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", null);
				}
			}

		}
	}

//	@DeleteMapping("/delete/{id}")
//	public ApiResponse<Void> delete(@PathVariable("id") long id) {
//		try {
//			affaireRepository.deleteById(id);
//			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "   Deleted", null);
//		} catch (Exception e) {
//			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not Deleted", null);
//		}
//	}
}

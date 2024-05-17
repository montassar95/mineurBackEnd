package com.cgpr.mineur.service.Impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.cgpr.mineur.dto.CalculeAffaireDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.AffaireId;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.ChangementLieu;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.models.Tribunal;
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArretProvisoireRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.EchappesRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.service.AffaireService;
import com.cgpr.mineur.tools.AffaireUtils;

import lombok.Data;

@Service
public class AffaireServiceImpl implements  AffaireService{

	
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

	
	

	@Autowired
	private ResidenceRepository residenceRepository;
	
	@Autowired
	private EchappesRepository echappesRepository;
	
	
	@Autowired
	private ArretProvisoireRepository arretProvisoireRepository;
	
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public  List<Affaire>  listAffaire() {
		return (List<Affaire>) affaireRepository.findAll();
	}

	@Override
	public  Affaire  getAffaireById(  String idEnfant,  String numAffaire,   long idTribunal,  long numOrdinaleArrestation) {
		System.out.println("numAffaire : ");
		System.out.println(numAffaire);
		AffaireId id = new AffaireId(idEnfant, numAffaire, idTribunal, numOrdinaleArrestation);
		Optional<Affaire> aData = affaireRepository.findById(id);
		if (aData.isPresent()) {
			return aData.get();
		} else {
			List<Affaire> eData = affaireRepository.findAffaireByAnyArrestation(idEnfant, numAffaire, idTribunal);
			if (eData.isEmpty()) {
				return   null ; // message 0
			} else {
				return  null;  // message 1

			}

		}
	}

	@Override
	public  List<Affaire>  findAffaireByAnyArrestation(  String idEnfant,  String numAffaire,  long idTribunal) {

		List<Affaire> aData = affaireRepository.findAffaireByAnyArrestation(idEnfant, numAffaire, idTribunal);
		if (aData.isEmpty()) {
			return   null ;
		} else {
			return   aData ;

		}
	}

	@Override
	public  Affaire  findAffaireByAffaireLien(  String idEnfant,  String numAffaire,  long idTribunal) {

		Affaire aData = affaireRepository.findAffaireByAffaireLien(idEnfant, numAffaire, idTribunal);
		if (aData != null) {
			return  aData ;
		} else {
			return   null ;
		}
	}

	

	@Override
	public  List<Affaire>  findByArrestationToTransfert( String idEnfant, long numOrdinale) {

		List<Affaire> lesAffaires = affaireRepository.findByArrestationToTransfert(idEnfant, numOrdinale);
		if (lesAffaires.isEmpty()) {
			return  null ;
		} else {

			List<Affaire> output = lesAffaires.stream().map(s -> {

				Document doc = documentRepository.getLastDocumentByAffaire(idEnfant, numOrdinale,
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
			return   output ;
		}
	}

	@Override
	public  List<Affaire>  findByArrestationToArret(  String idEnfant,  long numOrdinale) {

		List<Affaire> lesAffaires = affaireRepository.findByArrestationToArret(idEnfant, numOrdinale);
		if (lesAffaires.isEmpty()) {
			return   null ;
		} else {
			List<Affaire> output = lesAffaires.stream().map(s -> {

				Document doc = documentRepository.getLastDocumentByAffaire(idEnfant, numOrdinale,
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
			return output ;
		}
	}

	@Override
	public  List<Affaire>  findByArrestationByCJorCR( String idEnfant, long numOrdinale) {

		List<Affaire> lesAffaires = affaireRepository.findByArrestationByCJorCR(idEnfant, numOrdinale);
		if (lesAffaires.isEmpty()) {
			return   null ;
		} else {
			List<Affaire> output = lesAffaires.stream().map(s -> {

				Document doc = documentRepository.getLastDocumentByAffaire(idEnfant, numOrdinale,
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
			return  output ;
		}
	}

	@Override
	public  List<Affaire>  findByArrestationByCDorCHorCP(  String idEnfant, long numOrdinale) {

		List<Affaire> lesAffaires = affaireRepository.findByArrestationToPropaga(idEnfant, numOrdinale);
		if (lesAffaires.isEmpty()) {
			return  null ;
		} else {
			List<Affaire> output = lesAffaires.stream().map(s -> {

				Document doc = documentRepository.getLastDocumentByAffaire(idEnfant, numOrdinale,
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
			return   output ;
		}
	}

	@Override
	public  List<Affaire>  findByNumOrdinalAffaire(  String idEnfant, long numOrdinale,  long numOrdinalAffaire) {

		List<Affaire> aData = affaireRepository.findByNumOrdinalAffaire(idEnfant, numOrdinale, numOrdinalAffaire);
		if (aData.isEmpty()) {
			return  null ;
		} else {
			return   aData ;
		}
	}

	@Override
	public  Affaire  verifierNumOrdinalAffaire(@RequestBody Affaire affaire,
			@PathVariable("numOrdinaleArrestationActuelle") long numOrdinaleArrestationActuelle) {

		int sommeAffaire = affaireRepository.countAffaire(affaire.getArrestation().getArrestationId());
		System.out.println("sommeAffaire");

		System.out.println(affaire.toString());
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

				return   affaire ;
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

				return   affaire ;

			}

		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public  Affaire  update(@RequestBody Affaire affaire) {
		try {

			return   affaireRepository.save(affaire) ;
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public  Object  getDateDebutPunition(  String idEnfant,  long numOrdinale) {

		Date aData = affaireRepository.getDateDebutPunition(idEnfant, numOrdinale);
		if (aData != null) {
			return  aData ;
		} else {
			return   null ;
		}
	}

	@Override
	public  Object  getDateFinPunition( String idEnfant,  long numOrdinale) {

		Date aData = affaireRepository.getDateFinPunition(idEnfant, numOrdinale);
		if (aData != null) {
			return   aData ;
		} else {

			List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(idEnfant, numOrdinale);
			if (affprincipale.isEmpty()) {
				return   null ;
			} else {
				boolean allSameName = affprincipale.stream().allMatch(x -> x.getTypeDocument().equals("AEX"));
				if (allSameName) {
					
					aData = affprincipale.get(0).getDocuments().get(affprincipale.get(0).getDocuments().size() - 1)
							.getDateEmission();
					return  aData ;
				} else {
					return   null ;
				}
			}

		}
	}
	
	
	@Override
//	public List<Affaire> findByArrestation(String idEnfant, long numOrdinale) {
//	    // Récupère les affaires de la base de données
//	    List<Affaire> lesAffaires = affaireRepository.findByArrestation(idEnfant, numOrdinale);
//
//	    // Liste pour stocker les affaires enrichies
//	    List<Affaire> output = new ArrayList<>();
//
//	    // Pour chaque affaire récupérée
//	    for (Affaire affaire : lesAffaires) {
//	        try {
//	            // Récupère le dernier document associé à l'affaire
//	            Document lastDocument = documentRepository.getLastDocumentByAffaire(idEnfant, numOrdinale, affaire.getNumOrdinalAffaire());
//	            if (lastDocument != null) {
//	                // Enrichit l'affaire avec les données du document
//	                affaire.setTypeDocument(lastDocument.getTypeDocument());
//	                affaire.setDateEmissionDocument(lastDocument.getDateEmission());
//
//	                // Si le document est un Transfert ou une Arreterlexecution, met à jour le type de fichier
//	                if (lastDocument instanceof Transfert) {
//	                    Transfert t = (Transfert) lastDocument;
//	                    affaire.setTypeFile(t.getTypeFile());
//	                } else if (lastDocument instanceof Arreterlexecution) {
//	                    Arreterlexecution t = (Arreterlexecution) lastDocument;
//	                    affaire.setTypeFile(t.getTypeFile());
//	                }
//
//	                // Récupère les documents d'accusation associés à l'affaire
//	                List<Document> accData = documentRepository.getDocumentByAffaireforAccusation(idEnfant, numOrdinale, affaire.getNumOrdinalAffaire(), PageRequest.of(0, 1));
//
//	                // Si des documents d'accusation sont présents
//	                if (!accData.isEmpty()) {
//	                    Document lastDocAvecAccusation = accData.get(0);
//	                    // Enrichit l'affaire avec les données du document d'accusation
//	                    affaire.setTitreAccusations(accusationCarteRecupRepository.getTitreAccusationbyDocument(lastDocAvecAccusation.getDocumentId()));
//	                    affaire.setDateEmission(lastDocAvecAccusation.getDateEmission());
//	                    if (lastDocAvecAccusation instanceof CarteRecup) {
//	                        CarteRecup c = (CarteRecup) lastDocAvecAccusation;
//	                        affaire.setAnnee(c.getAnnee());
//	                        affaire.setMois(c.getMois());
//	                        affaire.setJour(c.getJour());
//	                        affaire.setAnneeArret(c.getAnneeArretProvisoire());
//	                        affaire.setMoisArret(c.getMoisArretProvisoire());
//	                        affaire.setJourArret(c.getJourArretProvisoire());
//	                        affaire.setTypeJuge(c.getTypeJuge());
//	                    }
//	                }
//	            }
//	        } catch (Exception e) {
//	            // Gestion des exceptions
//	            e.printStackTrace();
//	            // Continue avec la prochaine affaire
//	            continue;
//	        }
//	        // Ajoute l'affaire enrichie à la liste de sortie
//	        output.add(affaire);
//	    }
//
//	    // Retourne la liste d'affaires enrichies
//	    return output;
//	}

	public  List<Affaire>  findByArrestation(  String idEnfant,  long numOrdinale) {
	
		List<Affaire> lesAffaires = affaireRepository.findByArrestation(idEnfant, numOrdinale);

		List<Affaire> output = lesAffaires.stream().map(s -> {

			Document lastDocument = documentRepository.getLastDocumentByAffaire(idEnfant, numOrdinale, s.getNumOrdinalAffaire());
			if (lastDocument != null) {
				s.setTypeDocument(lastDocument.getTypeDocument());

				s.setDateEmissionDocument(lastDocument.getDateEmission());
				
		//pour definire le type de Transfert exactement  ("إحــــــالة "=> value: "T") ("تخلــــــي"=> value: "A")  ( "تعهــــــد"=> value: "G")			
				if (lastDocument instanceof Transfert) { 
					  
					Transfert t = (Transfert) lastDocument;

					s.setTypeFile(t.getTypeFile());
					
				}
		//pour definire le type de Arreterlexecution exactement  ("إيقاف تنفيذ الحكم " => value: "AEX") ("ســــــــــــراح"=> value: "L")
				if (lastDocument instanceof Arreterlexecution) {
					Arreterlexecution t = (Arreterlexecution) lastDocument;

					s.setTypeFile(t.getTypeFile());
				
				}

				List<Document> accData = documentRepository.getDocumentByAffaireforAccusation(idEnfant, numOrdinale,
						s.getNumOrdinalAffaire(), PageRequest.of(0, 1));

				List<TitreAccusation> titreAccusations = null;

				if (accData.size() > 0) {
				
					Document lastDocAvecAccusation = accData.get(0);
					
					if (lastDocAvecAccusation instanceof CarteRecup) {

						titreAccusations = accusationCarteRecupRepository
								.getTitreAccusationbyDocument(lastDocAvecAccusation.getDocumentId());

						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(lastDocAvecAccusation.getDateEmission());
					
						CarteRecup c = (CarteRecup) lastDocAvecAccusation;
						s.setAnnee(c.getAnnee());
						s.setMois(c.getMois());
						s.setJour(c.getJour());
						s.setAnneeArret(c.getAnneeArretProvisoire());
						s.setMoisArret(c.getMoisArretProvisoire());
						s.setJourArret(c.getJourArretProvisoire());
						s.setTypeJuge(c.getTypeJuge());

					} else if (lastDocAvecAccusation instanceof CarteDepot) {

						titreAccusations = accusationCarteDepotRepository
								.getTitreAccusationbyDocument(lastDocAvecAccusation.getDocumentId());
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(lastDocAvecAccusation.getDateEmission());

					} else if (lastDocAvecAccusation instanceof CarteHeber) {
						titreAccusations = accusationCarteHeberRepository
								.getTitreAccusationbyDocument(lastDocAvecAccusation.getDocumentId());
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(lastDocAvecAccusation.getDateEmission());
						 
					}

				}

			}
			return s;
		}).collect(Collectors.toList());

		return  output ;

	}
	


 
	@Override
	public CalculeAffaireDto calculerAffaire(String idEnfant, long numOrdinale) {
		
		
		 
	   
	    List<Affaire> lesAffaires = findByArrestation(idEnfant, numOrdinale);
	    CalculeAffaireDto dto = new CalculeAffaireDto();
	    
	    
	    
	    
	    
	    if (lesAffaires.isEmpty()) {
	        dto.setSansAffaire(true);
	        return dto;
	    }
	    else {
 	    	dto.setAffaires(lesAffaires);
	    	 dto.setSansAffaire(false);
	    }
	    
	    Arrestation arrestationCourant = AffaireUtils.processArrestationToGetAffairPrincipal(lesAffaires.get(0).getArrestation(), affaireRepository);
		
	    dto.setNbrAffaires(lesAffaires.size());
	    dto.setDisplayArrestation(true);
	   // dto.setDisplayAffaire(true);
	 	   
	    for (Affaire element : lesAffaires) {
	        if (element.getAffaireAffecter() == null &&
	            !"AEX".equals(element.getTypeDocument()) &&
	            (element.getTypeJuge() == null || element.getTypeJuge().getId() != 29)) {
	            AffaireUtils.calculerPenal(element, dto);
	        }

	        AffaireUtils.calculerArret(element, dto);
	        dto.setDateJugementPrincipale(dateFormat.format(element.getDateEmission()));
	      
	        
	        dto.setEtatJuridique(arrestationCourant.getEtatJuridique());
	       
	        
	        if(arrestationCourant != null) {
	        	
	        	
	        	
	        	
	        	if (element.getNumOrdinalAffaire() ==  arrestationCourant.getNumOrdinalAffairePricipale()) {
			 		 
	        		element.setAffairePrincipale(true);
					
			 	}
	        }
	        
	        if ("CJ".equals(element.getTypeDocument()) || "CJA".equals(element.getTypeDocument())) {
				dto.setDateJuge(true);
			}
	    	
	        if ("AP".equals(element.getTypeDocument())) {
	            dto.setDateAppelParquet(dateFormat.format(element.getDateEmissionDocument()));
	            dto.setAppelParquet(true);
	            dto.setDateJuge(true);
	        } else if ("AE".equals(element.getTypeDocument())) {
	            dto.setDateAppelEnfant(dateFormat.format(element.getDateEmissionDocument()));
	            dto.setAppelEnfant(true);
	            dto.setDateJuge(true);
	        }

	       
		 
	        
	        if ("CHL".equals(element.getTypeDocumentActuelle())) {
	            AffaireUtils.traiterChangementLieu(element, dto, documentRepository, residenceRepository);
	        }

	        if (element.getTypeJuge() != null && element.getTypeJuge().getId() == 4) {
	            dto.setAgeAdulte(true);
	        }
	    }
	    
		
	    dto.setTotaleRecidenceWithetabChangeManiere(residenceRepository.countTotaleRecidenceWithetabChangeManiere(idEnfant, numOrdinale));
	    int total = residenceRepository.countTotaleRecidence(idEnfant, numOrdinale);
		if (total == 0) {
			total = 0;
		} else {
			total = total - 1;
		}
		

	      dto.setTotaleRecidence(total);
          dto.setLiberation(arrestationCourant.getLiberation());
          
          dto.setTotaleEchappe(echappesRepository.countByEnfantAndArrestation(idEnfant, numOrdinale));
          
          List<Residence> cData = residenceRepository.findByEnfantAndArrestation(idEnfant, numOrdinale);
  		if (cData != null) {
  			dto.setResidences(cData);   
  		}  
  	//----------------------------------------
  		Date dateDebut = affaireRepository.getDateDebutPunition(idEnfant, numOrdinale);
  		
		if (dateDebut!= null) {
			dto.setDateDebut(dateFormat.format(dateDebut)); 
		}  
		
		 
		
		//----------------------------------------
  		
  		Date dateFin = affaireRepository.getDateFinPunition(idEnfant, numOrdinale);
		if (dateFin == null) {
		 
	

			List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(idEnfant, numOrdinale);
			if (!affprincipale.isEmpty()) {
				
				boolean allSameName = affprincipale.stream().allMatch(x -> x.getTypeDocument().equals("AEX"));
				if (allSameName) {
				
					dateFin = affprincipale.get(0).getDocuments().get(affprincipale.get(0).getDocuments().size() - 1)
							.getDateEmission();
					 
				}

			}

		}
		
		if (dateFin != null) {
			dto.setDateFin(dateFormat.format(dateFin)); 
		}
		
		
		 
		//----------------------------------------------------------
		
		 List<ArretProvisoire> list = arretProvisoireRepository.getArretProvisoirebyArrestation(idEnfant, numOrdinale);
		 if(!list.isEmpty()) {
			 dto.setArretProvisoires(list);
		 }
	    return dto;
	}

//	public CalculeAffaireDto calculerAffaire(String idEnfant,  long numOrdinale) {
//		List<Affaire> lesAffaires =  findByArrestation(idEnfant, numOrdinale);
//		 CalculeAffaireDto dto = new CalculeAffaireDto();
//		if(lesAffaires.isEmpty()) {
//			dto.setSansAffaire(true);
//			return dto;
//		}
//		 
//		
//		dto.setNbrAffaires(lesAffaires.size()) ;
//		dto.setDisplayArrestation(true);
//		dto.setDisplayAffaire(true);
//        
//         
//         
//		    for(Affaire element : lesAffaires) {  
//		    	if (
//			        element.getAffaireAffecter() == null &&
//			        !"AEX".equals(element.getTypeDocument()) &&
//			        (element.getTypeJuge() == null || element.getTypeJuge().getId() != 29)
//			    ) {
//			        dto.setJourPenal(dto.getJourPenal() + element.getJour());
//			        dto.setMoisPenal(dto.getMoisPenal() + element.getMois());
//			        dto.setMoisPenal(dto.getMoisPenal() + (int) Math.floor(dto.getJourPenal() / 30) * 1);
//			        dto.setJourPenal(dto.getJourPenal() - (int) Math.floor((dto.getJourPenal() % 365) / 30) * 30);
//			        dto.setJourPenal(dto.getJourPenal() - (int) Math.floor(dto.getJourPenal() / 365) * 365);
//			        dto.setAnneePenal(dto.getAnneePenal() + (int) Math.floor(element.getAnnee()));
//			        dto.setAnneePenal(dto.getAnneePenal() + (int) Math.floor(dto.getMoisPenal() / 12));
//			        dto.setMoisPenal(dto.getMoisPenal() - (int) Math.floor(dto.getMoisPenal() / 12) * 12);
//			    }
//
//			    dto.setJourArret(dto.getJourArret() + element.getJourArret());
//			    dto.setMoisArret(dto.getMoisArret() + element.getMoisArret());
//			    dto.setMoisArret(dto.getMoisArret() + (int) Math.floor(dto.getJourArret() / 30) * 1);
//			    dto.setJourArret(dto.getJourArret() - (int) Math.floor((dto.getJourArret() % 365) / 30) * 30);
//			    dto.setJourArret(dto.getJourArret() - (int) Math.floor(dto.getJourArret() / 365) * 365);
//			    dto.setAnneeArret(dto.getAnneeArret() + (int) Math.floor(element.getAnneeArret()));
//			    dto.setAnneeArret(dto.getAnneeArret() + (int) Math.floor(dto.getMoisArret() / 12));
//			    dto.setMoisArret(dto.getMoisArret() - (int) Math.floor(dto.getMoisArret() / 12) * 12);
//
//			    dto.setDateJugementPrincipale(dateFormat.format(element.getDateEmission()));
//			    if ("AP".equals(element.getTypeDocument())) {
//			        dto.setDateAppelParquet(dateFormat.format(element.getDateEmissionDocument() ));
//			        dto.setAppelParquet(true);
//			        dto.setDateJuge(true);
//			    }
//
//			    if ("AE".equals(element.getTypeDocument())) {
//			    	dto.setDateAppelEnfant(dateFormat.format(element.getDateEmissionDocument()));
//			        dto.setAppelEnfant(true);
//			        dto.setDateJuge(true);
//			      
//			    }
//		    
//
//		    if ("CHL".equals(element.getTypeDocumentActuelle())) {
//		        List<Document> documents = documentRepository.getDocumentByAffaire(
//		            element.getArrestation().getArrestationId().getIdEnfant(),
//		            element.getArrestation().getArrestationId().getNumOrdinale(),
//		            element.getNumOrdinalAffaire()
//		        );
//
//		        if (!documents.isEmpty()) {
//		            Document document1 = documents.get(0);
//		            Document document2 = documents.get(1);
//
//		            if (document1 instanceof ChangementLieu) {
//		                ChangementLieu changementLieu = (ChangementLieu) document1;
//
//		                if ("changementEtab".equals(changementLieu.getType()) &&
//		                    element.getArrestation() != null &&
//		                    element.getArrestation().getLiberation() == null) {
//		                    dto.setChangementLieuCh(true);
//		                } else if ("mutation".equals(changementLieu.getType())) {
//		                    Residence residence = residenceRepository.findMaxResidence(
//		                        element.getArrestation().getArrestationId().getIdEnfant(),
//		                        element.getArrestation().getArrestationId().getNumOrdinale()
//		                    );
//
//		                    if (residence != null &&
//		                        residence.getStatut() != 2 &&
//		                        residence.getEtablissement().getId() != changementLieu.getEtablissementtMutation().getId()) {
//		                        dto.setChangementLieuMu(true);
//		                    }
//		                }
//		            }
//
//		            if ("AP".equals(document2.getTypeDocument())) {
//		                dto.setDateAppelParquet(dateFormat.format(element.getDateEmissionDocument()));
//		                dto.setAppelParquet(true);
//		                dto.setDateJuge(true);
//		            }
//
//		            if ("AE".equals(document2.getTypeDocument())) {
//		                dto.setDateAppelEnfant(dateFormat.format(element.getDateEmissionDocument()));
//		                dto.setAppelEnfant(true);
//		                dto.setDateJuge(true);
//		            }
//		        }
//		    }
//		    if (element.getTypeJuge() != null && element.getTypeJuge().getId() == 4) {
//	 	    	dto.setAgeAdulte(true);;
//	     }
//		    }
//		 
//	    return   dto ;
//	}
	
 
}

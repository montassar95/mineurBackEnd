package com.cgpr.mineur.serviceReporting;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.ResidenceConverter;
import com.cgpr.mineur.dto.DocumentSearchCriteriaDto;
import com.cgpr.mineur.dto.SimpleTuple;
import com.cgpr.mineur.dto.TitreAccusationDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.Echappes;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.RapportQuotidien;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.ResidenceWithAffaires;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.models.Visite;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.EchappesRepository;
import com.cgpr.mineur.repository.EtablissementRepository;
import com.cgpr.mineur.repository.RapportQuotidienRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.repository.StatistcsRepository;
import com.cgpr.mineur.repository.VisiteRepository;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.tools.ToolsForReporting;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChargeAllEnfantService   {

	private final AffaireRepository affaireRepository;

	private final DocumentRepository documentRepository;

	private final AccusationCarteRecupRepository accusationCarteRecupRepository;

	private final AccusationCarteDepotRepository accusationCarteDepotRepository;

	private final AccusationCarteHeberRepository accusationCarteHeberRepository;

	private final ResidenceRepository residenceRepository;

	private final EchappesRepository echappesRepository;

	private final EtablissementRepository etablissementRepository;
	
	private final StatistcsRepository statistcsRepository;
	
	private final VisiteRepository visiteRepository;
	
	
	private final RapportQuotidienRepository rapportQuotidienRepository;
	
	
//	public int masculinEtranger=0;
//	public  int femininEtranger=0;
	
	@Autowired
	public ChargeAllEnfantService(
			AffaireRepository affaireRepository, 
			DocumentRepository documentRepository,
			AccusationCarteRecupRepository accusationCarteRecupRepository,
			AccusationCarteDepotRepository accusationCarteDepotRepository,
			AccusationCarteHeberRepository accusationCarteHeberRepository, 
			ResidenceRepository residenceRepository,
			EchappesRepository echappesRepository, 
			EtablissementRepository etablissementRepository,
			StatistcsRepository statistcsRepository,
			 RapportQuotidienRepository rapportQuotidienRepository,
			 VisiteRepository  visiteRepository ) {
		this.affaireRepository = affaireRepository;

		this.documentRepository = documentRepository;

		this.accusationCarteRecupRepository = accusationCarteRecupRepository;

		this.accusationCarteDepotRepository = accusationCarteDepotRepository;

		this.accusationCarteHeberRepository = accusationCarteHeberRepository;

		this.residenceRepository = residenceRepository;

		this.echappesRepository = echappesRepository;

		this.etablissementRepository = etablissementRepository;
		this.statistcsRepository = statistcsRepository;
		this.rapportQuotidienRepository =rapportQuotidienRepository;
		this.visiteRepository= visiteRepository;
	}
	
	
	
	
	
	public List<List<Residence>> chargeList() {
	    List<List<Residence>> enfantAffiches = new ArrayList<>();

	    try {
	        Date premierJourDuMois = ToolsForReporting.getFirstDayOfMonth();
	        System.err.println(premierJourDuMois.toString()+"-------------------------------- ");
	        List<Etablissement> allCentre = etablissementRepository.listEtablissementCentre();

	        List<CompletableFuture<List<Residence>>> futures = getAllResidencesAsync(allCentre, premierJourDuMois);

	        enfantAffiches = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
	                .thenApply(v -> futures.stream()
	                        .map(CompletableFuture::join)
	                        .collect(Collectors.toList()))
	                .join();

	        updateResidences(enfantAffiches);
	        
	        

	    } catch (Exception e) {
	        // Handle exceptions here, you can log or perform appropriate error handling
	        e.printStackTrace();
	    }
 
	    return enfantAffiches;
	}

	
      
	private List<CompletableFuture<List<Residence>>> getAllResidencesAsync(List<Etablissement> allCentre, Date premierJourDuMois) {
	    return allCentre.stream()
	            .flatMap(e -> Stream.of(
 	                       residenceRepository.findByAllEnfantExistJugeAsync(e.getId(),ToolsForReporting.getFirstDayOfNextMonth())
                           ,residenceRepository.findByAllEnfantExistArretAsync(e.getId(),ToolsForReporting.getFirstDayOfNextMonth())
	                      , residenceRepository.findByAllEnfantLibereAsync(
	                            0, 0, 0, 0, 0, 0, 0, e, 0, 0, 0, null, null, premierJourDuMois, new Date(), null)
	            ))
	            .collect(Collectors.toList());
	}

	private void updateResidences(List<List<Residence>> enfantAffiches) {
	    enfantAffiches.forEach(enfantAfficheCentre -> {
	        enfantAfficheCentre.forEach(residence -> {
	            try {
	            	
	            	//tres important  findByArrestationCoroissant 
	    	        List<Affaire> lesAffaires = affaireRepository.findAffairePrincipale(
	    	                residence.getArrestation().getArrestationId().getIdEnfant(),
	    	                residence.getArrestation().getArrestationId().getNumOrdinale());
	    	   
	    	     
	                updateResidence(residence,lesAffaires);
	            } catch (Exception e) {
	                // Handle exceptions here, you can log or perform appropriate error handling
	                e.printStackTrace();
	            }
	        });
	    });
	    System.err.println("fin de test");
	}

	
 

	public void updateResidence(Residence residence, List<Affaire> lesAffaires) {
	    if (isValidResidence(residence)) {
	        // Créez une liste de Runnable pour les mises à jour
	        List<Runnable> tasks = Arrays.asList(
 
	            () -> updateAgeEnfant(residence),
 	            () -> updateInfoEchappement(residence),
 	            () -> updateDatesPunition(residence,lesAffaires)
	        );

	        // Exécutez les tâches en parallèle
	        tasks.parallelStream().forEach(Runnable::run);

	        if (lesAffaires.isEmpty()) {
	            System.out.println("La liste des affaires est vide."+residence.getResidenceId().getIdEnfant());
	            
	        } else {
	            updateAffairePrincipale(lesAffaires);
	        }
				
	        
	        
	        updateArrestationDocuments(residence, lesAffaires);
 
	         String   situationJudiciaire = determineSituationJudiciaire(residence.getArrestation(),lesAffaires);
	        residence.getArrestation().setSituationJudiciaire(situationJudiciaire); 
	    }
	}


	private boolean isValidResidence(Residence residence) {
	    return residence.getArrestation() != null && residence.getArrestation().getEnfant() != null;
	}
	private void updateAffairePrincipale(List<Affaire> lesAffaires) {
	    boolean isAffairePrincipaleMiseAJour = false;

	    for (Affaire affaire : lesAffaires) {
	        if (affaire.getTypeDocument() != null && (affaire.getTypeDocument().equals("AP")
	                || affaire.getTypeDocument().equals("CD")
	                || affaire.getTypeDocument().equals("CH")
	                || affaire.getTypeDocument().equals("CJA")
	                || affaire.getTypeDocument().equals("T")
	                || affaire.getTypeDocument().equals("AE")
	                || affaire.getTypeDocument().equals("CP"))) {
	            affaire.setAffairePrincipale(true);
	            isAffairePrincipaleMiseAJour = true;
	            break; // Sortir de la boucle après la première mise à jour
	        }
	    }

	    if (!isAffairePrincipaleMiseAJour) {
	        for (Affaire affaire : lesAffaires) {
	            if (affaire.getTypeDocument() != null && affaire.getTypeDocument().equals("CJ")
	                    && affaire.getAffaireAffecter() == null && (affaire.getDaysDiffJuge() > 0)) {
	                affaire.setAffairePrincipale(true);
	                isAffairePrincipaleMiseAJour = true;
	                break; // Sortir de la boucle après la mise à jour
	            }
	        }
	    }

	    if (!isAffairePrincipaleMiseAJour) {
	        for (Affaire affaire : lesAffaires) {
	            if (affaire.getAffaireAffecter() == null) {
	                affaire.setAffairePrincipale(true);
	                break; // Sortir de la boucle après la mise à jour
	            }
	        }
	    }
	  
	}

 

	private void updateArrestationDocuments(Residence residence, List<Affaire> lesAffaires) {
	    
		 Map<String, Document> documentsMap = new HashMap<>();

		    // Récupérer tous les documents d'arrestation
		    lesAffaires.stream()
 		        .filter(s -> s.getArrestation() != null && s.getArrestation().getArrestationId() != null)
		        .forEach(s -> {
		            String key = s.getArrestation().getArrestationId().getIdEnfant() + "-" +
		                         s.getArrestation().getArrestationId().getNumOrdinale() + "-" +
		                         s.getNumOrdinalAffaire() + "-arrestation";
		            DocumentSearchCriteriaDto criteria = new DocumentSearchCriteriaDto(
		                s.getArrestation().getArrestationId().getIdEnfant(),
		                s.getArrestation().getArrestationId().getNumOrdinale(),
		                s.getNumOrdinalAffaire()
		            );
		            documentsMap.put(key, documentRepository.getLastDocumentByAffaire2(criteria));
		        });

		    // Récupérer tous les documents d'accusation
		    lesAffaires.stream()
 		        .filter(s -> s.getAffaireId() != null)
		        .forEach(s -> {
		            String accusationKey = s.getAffaireId().getIdEnfant() + "-" +
		                                   s.getAffaireId().getNumOrdinaleArrestation() + "-" +
		                                   s.getNumOrdinalAffaire() + "-accusation";
		            DocumentSearchCriteriaDto accusationCriteria = new DocumentSearchCriteriaDto(
		                s.getAffaireId().getIdEnfant(),
		                s.getAffaireId().getNumOrdinaleArrestation(),
		                s.getNumOrdinalAffaire()
		            );
		            documentsMap.put(accusationKey, documentRepository.getDocumentByAffaireforAccusation(accusationCriteria));
		        });
		    
		    
	    // Mettre à jour les affaires avec les documents récupérés
	    lesAffaires.stream().forEach(s -> {
	        // Mettre à jour les documents d'arrestation
	        String key = s.getArrestation().getArrestationId().getIdEnfant() + "-" +
	                     s.getArrestation().getArrestationId().getNumOrdinale() + "-" +
	                     s.getNumOrdinalAffaire() + "-arrestation";
	        Document doc = documentsMap.get(key);
	        if (doc != null) {
	            s.setTypeDocument(doc.getTypeDocument());
	            s.setDateEmissionDocument(doc.getDateEmission());
	            // Gérer le type de document...
	        }

	        // Mettre à jour les documents d'accusation
	        String accusationKey = s.getAffaireId().getIdEnfant() + "-" +
	                               s.getAffaireId().getNumOrdinaleArrestation() + "-" +
	                               s.getNumOrdinalAffaire() + "-accusation";
	        Document docForAccusation = documentsMap.get(accusationKey);

	        if (docForAccusation != null) {
	            s.setDateEmission(docForAccusation.getDateEmission());
	            if (docForAccusation instanceof CarteRecup) {
	                CarteRecup c = (CarteRecup) docForAccusation;
	                s.setTitreAccusations(getTitreAccusationsFromCarteRecup(c));
	                populateCarteRecupFields(c,s);
	            } else if (docForAccusation instanceof CarteDepot) {
	                CarteDepot c = (CarteDepot) docForAccusation;
	                s.setTitreAccusations(getTitreAccusationsFromCarteDepot(c));
	            } else if (docForAccusation instanceof CarteHeber) {
	                CarteHeber c = (CarteHeber) docForAccusation;
	                s.setTitreAccusations(getTitreAccusationsFromCarteHeber(c));
	            }
	        }
	    });

	    // Trier les affaires par date de début de punition
	    lesAffaires.sort(Comparator.comparing(Affaire::getDateDebutPunition, Comparator.nullsLast(Comparator.naturalOrder())));

	    residence.getArrestation().setAffaires(filterLesAffaires(lesAffaires));
	}
	
	
	
	private void populateCarteRecupFields(CarteRecup carteRecup, Affaire affaire) {
		affaire.setAnnee(carteRecup.getAnnee());
		affaire.setMois(carteRecup.getMois());
		affaire.setJour(carteRecup.getJour());
		affaire.setAnneeArret(carteRecup.getAnneeArretProvisoire());
		affaire.setMoisArret(carteRecup.getMoisArretProvisoire());
		affaire.setJourArret(carteRecup.getJourArretProvisoire());
		affaire.setTypeJuge(carteRecup.getTypeJuge());
	}
	
	
	
	//JsonProcessingException
	private List<Affaire> filterLesAffaires(List<Affaire> lesAffaires) {
	    return lesAffaires.stream().map(a -> {
	    	 a.setArrestation(null);
	        if (a.getAffaireLien() != null) {
	            a.getAffaireLien().setArrestation(null);
	            a.getAffaireLien().setAffaireLien(null);
	            a.getAffaireLien().setAffaireAffecter(null);
	        }
	        if (a.getAffaireAffecter() != null) {
	            a.getAffaireAffecter().setArrestation(null);
	            a.getAffaireAffecter().setAffaireLien(null);
	            a.getAffaireAffecter().setAffaireAffecter(null);
	        }
	        return a; // Retourne l'élément modifié ou non modifié
	    }).collect(Collectors.toList());
	}

	
	
	private String determineSituationJudiciaire(Arrestation arrestation, List<Affaire> affaires) {
	    if (affaires.isEmpty()) {
	        return "vide";
	    }

	    List<String> typesDocumentsCibles = Arrays.asList("CD", "CH", "T", "CJA", "AP", "CP", "AE");
	    
	    if (arrestation.getLiberation() == null) {
	        List<Affaire> affaireArret = affaires.stream()
	                .filter(a -> typesDocumentsCibles.contains(a.getTypeDocument()))
	                .collect(Collectors.toList());

	        if (affaireArret.isEmpty()) {
	            boolean toutLibre = affaires.stream()
	                    .allMatch(x -> x.getTypeDocument().equals("AEX") || x.getTypeDocument().equals("L"));

	            if (toutLibre) {
	                return "pasInsertinLiberable";
	            }
	            return "juge";
	        } else {
	            return "arret";
	        }
	    } else {
	        return "libre";
	    }
	}

	
 

	private void updateAgeEnfant(Residence residence) {
	    LocalDate dob = LocalDate.parse(residence.getArrestation().getEnfant().getDateNaissance().toString());
	    int age = Period.between(dob, LocalDate.now()).getYears();
	    residence.getArrestation().setAge(age);
	}

 
	private void updateInfoEchappement(Residence residence) {
	   

	    Optional<Echappes> echappeOptional = getFirstEchappesWithoutResidenceTrouver(residence);
	    
	    if (echappeOptional.isPresent()) {
	       residence.getArrestation().setEchappe(echappeOptional.get());
	         
	    }  
	}

	private Optional<Echappes> getFirstEchappesWithoutResidenceTrouver(Residence residence) {
	    if (residence == null || residence.getEchappes() == null || residence.getEchappes().isEmpty()) {
	        return Optional.empty(); // Retourne un Optional vide si residence ou la liste est null
	    }
	    return residence.getEchappes().stream()
	    	.filter(echappe -> echappe != null && echappe.getResidenceTrouver() == null) // Vérification supplémentaire
	        .findFirst(); // Renvoie un Optional d'Echappes
	}

	private void updateDatesPunition(Residence residence, List<Affaire> lesAffaires) {
	    residence.getArrestation().setDateDebut(getMinDateDebutPunition(lesAffaires));

	    residence.getArrestation().setDateFin(getMaxDateFinPunition(lesAffaires));
	}
	
	public Date getMinDateDebutPunition(List<Affaire> affaires) {
        Optional<java.sql.Date> minDate = affaires.stream()
            .filter(a -> a.getAffaireAffecter() == null && a.getDaysDiffJuge() > 0)
            .map(Affaire::getDateDebutPunition)
            .filter(date -> date != null) // Exclure les dates null
            .min(Date::compareTo);

        return minDate.orElse(null); // Retourne null si aucune date ne correspond
    }
	
    public Date getMaxDateFinPunition(List<Affaire> affaires) {
        Optional<java.sql.Date> maxDate = affaires.stream()
            .filter(a -> a.getAffaireAffecter() == null 
                       && !("AEX".equals(a.getTypeDocument())) 
                       && !("CJA".equals(a.getTypeDocument())))
            .map(Affaire::getDateFinPunition)
            .filter(date -> date != null) // Exclure les dates null
            .max(Date::compareTo); // Trouver la date maximale

        return maxDate.orElse(null); // Retourner null si aucune date ne correspond
    }
    
	public List<List<Residence>> chargeListByDate(LocalDate date) {
		System.out.println("base "+date.toString());
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<List<Residence>> residences = new ArrayList<>(); // Créez une liste vide pour stocker les résidences
	    
	    try {
	    	System.out.println("debut base");
	        Optional<RapportQuotidien> dernierRapport = rapportQuotidienRepository.findLatestByDate(date);
	        System.out.println("fin base");
	        if (dernierRapport.isPresent()) {
	            RapportQuotidien rapport = dernierRapport.get();
	            String jsonString = rapport.getListResidance();

	            // Conversion de la chaîne JSON en un objet Java
	            residences = objectMapper.readValue(jsonString, new TypeReference<List<List<Residence>>>() {});
	            System.out.println("On a dejé transferer");
	        } else {
	            System.out.println("Aucun rapport trouvé pour la date spécifiée");
	        }

	        // Faites ce que vous voulez avec la liste de résidences convertie
	        // ...
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        // Gérez les erreurs de conversion
	        // ...
	    }
	    
	    return residences; // Retourne la liste de résidences récupérée
	}


	private void updateNombreVisites(Residence residence, VisiteRepository visiteRepository) {
	    Optional<Visite> v = visiteRepository.findbyEnfantandDate(residence.getResidenceId().getIdEnfant(), 2024,  10);

	    if (v.isPresent()) {
	    	System.err.println(v.get().toString()+"visittttttttt111");
	        residence.setNbVisite(v.get().getNbrVisite()+"");
	    } else {
	    	System.err.println(v.get().toString()+"visittttttttt222");
	        residence.setNbVisite("0.");
	    }
	}
	public List<TitreAccusation> getTitreAccusationsFromCarteDepot(CarteDepot carteDepot) {
 		return accusationCarteDepotRepository.getTitreAccusationbyDocument(carteDepot.getDocumentId());
 
	}
	public List<TitreAccusation> getTitreAccusationsFromCarteHeber(CarteHeber carteHeber) {
 	return accusationCarteHeberRepository.getTitreAccusationbyDocument(carteHeber.getDocumentId());
 
	}
	public List<TitreAccusation> getTitreAccusationsFromCarteRecup(CarteRecup carteRecup) {
 		return accusationCarteRecupRepository.getTitreAccusationbyDocument(carteRecup.getDocumentId());
 
	}
	
	
	
	
	
	// Définir la date limite comme LocalDate
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	List<String> DEFAULT_DOCUMENT_TYPES = Arrays.asList("CH", "CD", "CJA", "T", "CP", "AP", "AE");
	List<String> Sera_Libre_DOCUMENT_TYPES = Arrays.asList("CH", "CD", "CJA", "T", "CP", "AP" );
	List<String> APPEL_PARQUET_DOCUMENT_TYPES = Arrays.asList(  "AP" );
	List<String> APPEL_ENFANT_DOCUMENT_TYPES = Arrays.asList(  "AE" );
	List<String> TRANFERT_DOCUMENT_TYPES = Arrays.asList(  "T" );
	
	
	public List<Residence> chargeSpecialList(PDFListExistDTO pDFListExistDTO) {
		Date  start =   ToolsForReporting.calculateStartDate(pDFListExistDTO);
		Date   end   =   ToolsForReporting.calculateEndDate(pDFListExistDTO);

		Date dateDebutGlobale = ToolsForReporting.parseDate(pDFListExistDTO.getDateDebutGlobale());
		Date dateFinGlobale = ToolsForReporting.parseDate(pDFListExistDTO.getDateFinGlobale());

		
		
		System.out.println("dateDebutGlobale : "+dateDebutGlobale); 
		 
		System.out.println("dateFinGlobale : "+dateFinGlobale); 
	  
		List<Residence> listResidences = null;
		
 
		
		 
		switch (pDFListExistDTO.getEtatJuridiue()) {
		case "arret":

			listResidences = (List<Residence>) residenceRepository.listDetenusArretes(
					 DEFAULT_DOCUMENT_TYPES,
					 pDFListExistDTO.getClassePenale(),
						pDFListExistDTO.getNiveauEducatif(),
						pDFListExistDTO.getGouvernorat(),
						pDFListExistDTO.getSituationFamiliale(), 
						pDFListExistDTO.getSituationSocial(),
						pDFListExistDTO.getMetier(), 
						pDFListExistDTO.getDelegation(),
						(float) pDFListExistDTO.getAge1(), 
						(float) pDFListExistDTO.getAge2(),
						pDFListExistDTO.getCheckEtranger()==null, 
						pDFListExistDTO.getNationalite(),
						pDFListExistDTO.getGouvernoratTribunal(),
						pDFListExistDTO.getTypeTribunal(), pDFListExistDTO.getTypeAffaire(),
						pDFListExistDTO.getEtablissements() ,dateDebutGlobale, dateFinGlobale
                        );
			 
			break;
		case "juge":

			listResidences = (List<Residence>) residenceRepository.listDetenusJuges(
					DEFAULT_DOCUMENT_TYPES,
					pDFListExistDTO.getClassePenale(),
					pDFListExistDTO.getNiveauEducatif(),
					pDFListExistDTO.getGouvernorat(),
					pDFListExistDTO.getSituationFamiliale(), 
					pDFListExistDTO.getSituationSocial(),
					pDFListExistDTO.getMetier(), 
					pDFListExistDTO.getDelegation(),
					(float) pDFListExistDTO.getAge1(), 
					(float) pDFListExistDTO.getAge2(),
					pDFListExistDTO.getCheckEtranger()==null, 
					pDFListExistDTO.getNationalite(),
					pDFListExistDTO.getGouvernoratTribunal(),
					pDFListExistDTO.getTypeTribunal(), pDFListExistDTO.getTypeAffaire(),
					pDFListExistDTO.getEtablissements()
					);

			break;
			
			
		case "libere":
			  // Utiliser Calendar pour ajouter un jour
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(dateFinGlobale); // Définir la date à modifier
	        calendar.add(Calendar.DAY_OF_MONTH, 1); // Ajouter un jour

	        // Récupérer la nouvelle date
	        Date newDate = calendar.getTime();

	        listResidences = (List<Residence>) residenceRepository.listDetenusLiberes(
					pDFListExistDTO.getClassePenale(),
					pDFListExistDTO.getNiveauEducatif(),
					pDFListExistDTO.getGouvernorat(),
					pDFListExistDTO.getSituationFamiliale(), 
					pDFListExistDTO.getSituationSocial(),
					pDFListExistDTO.getMetier(), 
					pDFListExistDTO.getDelegation(),
					(float) pDFListExistDTO.getAge1(), 
					(float) pDFListExistDTO.getAge2(),
					pDFListExistDTO.getCheckEtranger()==null, 
					pDFListExistDTO.getNationalite(),
					pDFListExistDTO.getGouvernoratTribunal(),
					pDFListExistDTO.getTypeTribunal(), pDFListExistDTO.getTypeAffaire(),
					pDFListExistDTO.getEtablissements(),
					
					
					  dateDebutGlobale,
					  newDate,
				  pDFListExistDTO.getCauseLiberation( ));

			break;
			
			

		case "audience":

			 
			listResidences = (List<Residence>) residenceRepository.listAudiences(
					pDFListExistDTO.getClassePenale().getId(), pDFListExistDTO.getNiveauEducatif().getId(),
					pDFListExistDTO.getGouvernorat().getId(), pDFListExistDTO.getSituationFamiliale().getId(),
					pDFListExistDTO.getSituationSocial().getId(), pDFListExistDTO.getMetier().getId(),
					pDFListExistDTO.getDelegation().getId(), pDFListExistDTO.getEtablissements(),
					pDFListExistDTO.getGouvernoratTribunal().getId(), pDFListExistDTO.getTypeTribunal().getId(),
					pDFListExistDTO.getTypeAffaire().getId(), start, end, dateDebutGlobale, dateFinGlobale,
					pDFListExistDTO.getCheckEtranger(), pDFListExistDTO.getNationalite().getId());
			break;

		case "attetAP":

			listResidences = (List<Residence>) residenceRepository.listDetenusArretes(
					APPEL_PARQUET_DOCUMENT_TYPES,
					 pDFListExistDTO.getClassePenale(),
						pDFListExistDTO.getNiveauEducatif(),
						pDFListExistDTO.getGouvernorat(),
						pDFListExistDTO.getSituationFamiliale(), 
						pDFListExistDTO.getSituationSocial(),
						pDFListExistDTO.getMetier(), 
						pDFListExistDTO.getDelegation(),
						(float) pDFListExistDTO.getAge1(), 
						(float) pDFListExistDTO.getAge2(),
						pDFListExistDTO.getCheckEtranger()==null, 
						pDFListExistDTO.getNationalite(),
						pDFListExistDTO.getGouvernoratTribunal(),
						pDFListExistDTO.getTypeTribunal(), pDFListExistDTO.getTypeAffaire(),
						pDFListExistDTO.getEtablissements()  , null , null 
                       );
			break;

		case "attetAE":

			listResidences = (List<Residence>) residenceRepository.listDetenusArretes(
					APPEL_ENFANT_DOCUMENT_TYPES,
					 pDFListExistDTO.getClassePenale(),
						pDFListExistDTO.getNiveauEducatif(),
						pDFListExistDTO.getGouvernorat(),
						pDFListExistDTO.getSituationFamiliale(), 
						pDFListExistDTO.getSituationSocial(),
						pDFListExistDTO.getMetier(), 
						pDFListExistDTO.getDelegation(),
						(float) pDFListExistDTO.getAge1(), 
						(float) pDFListExistDTO.getAge2(),
						pDFListExistDTO.getCheckEtranger()==null, 
						pDFListExistDTO.getNationalite(),
						pDFListExistDTO.getGouvernoratTribunal(),
						pDFListExistDTO.getTypeTribunal(), pDFListExistDTO.getTypeAffaire(),
						pDFListExistDTO.getEtablissements() , null , null 
                       );
			break;
		case "attetT":

			listResidences = (List<Residence>) residenceRepository.listDetenusArretes(
					TRANFERT_DOCUMENT_TYPES,
					 pDFListExistDTO.getClassePenale(),
						pDFListExistDTO.getNiveauEducatif(),
						pDFListExistDTO.getGouvernorat(),
						pDFListExistDTO.getSituationFamiliale(), 
						pDFListExistDTO.getSituationSocial(),
						pDFListExistDTO.getMetier(), 
						pDFListExistDTO.getDelegation(),
						(float) pDFListExistDTO.getAge1(), 
						(float) pDFListExistDTO.getAge2(),
						pDFListExistDTO.getCheckEtranger()==null, 
						pDFListExistDTO.getNationalite(),
						pDFListExistDTO.getGouvernoratTribunal(),
						pDFListExistDTO.getTypeTribunal(), pDFListExistDTO.getTypeAffaire(),
						pDFListExistDTO.getEtablissements() , null , null 
                       );
			break;

		case "jugeR":

			listResidences = (List<Residence>) residenceRepository.listDetenusJugeRevus(
					pDFListExistDTO.getClassePenale().getId(), pDFListExistDTO.getNiveauEducatif().getId(),
					pDFListExistDTO.getGouvernorat().getId(), pDFListExistDTO.getSituationFamiliale().getId(),
					pDFListExistDTO.getSituationSocial().getId(), pDFListExistDTO.getMetier().getId(),
					pDFListExistDTO.getDelegation().getId(), pDFListExistDTO.getEtablissement().getId(),
					pDFListExistDTO.getGouvernoratTribunal().getId(), pDFListExistDTO.getTypeTribunal().getId(),
					pDFListExistDTO.getTypeAffaire().getId(), start, end, pDFListExistDTO.getCheckEtranger(),
					pDFListExistDTO.getNationalite().getId());
			break;

		case "all":
			 
		 
			listResidences = residenceRepository.listAllDetenus(pDFListExistDTO.getClassePenale(),
					pDFListExistDTO.getNiveauEducatif(), pDFListExistDTO.getGouvernorat(),
					pDFListExistDTO.getSituationFamiliale(), pDFListExistDTO.getSituationSocial(),
					pDFListExistDTO.getMetier(), pDFListExistDTO.getDelegation(),
					pDFListExistDTO.getEtablissements(), pDFListExistDTO.getGouvernoratTribunal(),
					pDFListExistDTO.getTypeTribunal(), pDFListExistDTO.getTypeAffaire(), (float) pDFListExistDTO.getAge1(), (float) pDFListExistDTO.getAge2(),
					pDFListExistDTO.getCheckEtranger()==null,  pDFListExistDTO.getNationalite());

			break;

		case "devenuMajeur":

			if (dateDebutGlobale == null && dateFinGlobale == null) {

				dateDebutGlobale = new Date();
				dateFinGlobale = new Date();
				dateDebutGlobale.setYear(dateDebutGlobale.getYear() - 18);
				dateFinGlobale.setYear(dateFinGlobale.getYear() - 18);
				dateFinGlobale.setMonth(dateFinGlobale.getMonth() + 1);
				
			} else {

				dateDebutGlobale.setYear(dateDebutGlobale.getYear() - 18);
				dateFinGlobale.setYear(dateFinGlobale.getYear() - 18);
				 

			}
			
		 

			listResidences = (List<Residence>) residenceRepository.listDetenusMajeurs( 
					pDFListExistDTO.getClassePenale(),
					pDFListExistDTO.getNiveauEducatif(),
					pDFListExistDTO.getGouvernorat(),
					pDFListExistDTO.getSituationFamiliale(), 
					pDFListExistDTO.getSituationSocial(),
					pDFListExistDTO.getMetier(), 
					pDFListExistDTO.getDelegation(),
					(float) pDFListExistDTO.getAge1(), 
					(float) pDFListExistDTO.getAge2(),
					pDFListExistDTO.getCheckEtranger()==null, 
					pDFListExistDTO.getNationalite(),
					pDFListExistDTO.getGouvernoratTribunal(),
					pDFListExistDTO.getTypeTribunal(), 
					pDFListExistDTO.getTypeAffaire(),
					pDFListExistDTO.getEtablissements() ,
					dateDebutGlobale, dateFinGlobale);

			break;
		case "entreReelle":

//					 
			listResidences = (List<Residence>) residenceRepository.listDetenusEntreReelles(
					 pDFListExistDTO.getClassePenale(),
						pDFListExistDTO.getNiveauEducatif(),
						pDFListExistDTO.getGouvernorat(),
						pDFListExistDTO.getSituationFamiliale(), 
						pDFListExistDTO.getSituationSocial(),
						pDFListExistDTO.getMetier(), 
						pDFListExistDTO.getDelegation(),
						(float) pDFListExistDTO.getAge1(), 
						(float) pDFListExistDTO.getAge2(),
						pDFListExistDTO.getCheckEtranger()==null, 
						pDFListExistDTO.getNationalite(),
						pDFListExistDTO.getGouvernoratTribunal(),
						pDFListExistDTO.getTypeTribunal(), 
						pDFListExistDTO.getTypeAffaire(),
						pDFListExistDTO.getEtablissements(),
						 dateDebutGlobale,
						  dateFinGlobale );

			break;

	

		case "seraLibere":

			listResidences = (List<Residence>) residenceRepository.listDetenusSeraLiberes( 	
					Sera_Libre_DOCUMENT_TYPES,
					pDFListExistDTO.getClassePenale(),
					pDFListExistDTO.getNiveauEducatif(),
					pDFListExistDTO.getGouvernorat(),
					pDFListExistDTO.getSituationFamiliale(), 
					pDFListExistDTO.getSituationSocial(),
					pDFListExistDTO.getMetier(), 
					pDFListExistDTO.getDelegation(),
					(float) pDFListExistDTO.getAge1(), 
					(float) pDFListExistDTO.getAge2(),
					pDFListExistDTO.getCheckEtranger()==null, 
					pDFListExistDTO.getNationalite(),
					pDFListExistDTO.getGouvernoratTribunal(),
					pDFListExistDTO.getTypeTribunal(),
					pDFListExistDTO.getTypeAffaire(),
					pDFListExistDTO.getEtablissements(),
					dateDebutGlobale, dateFinGlobale);

			listResidences.stream().map(s -> {

				s.setDateFin(affaireRepository.getDateFinPunition(s.getArrestation().getArrestationId().getIdEnfant(),
						s.getArrestation().getArrestationId().getNumOrdinale()));

				if (s.getDateFin() == null) {
					List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(
							s.getArrestation().getArrestationId().getIdEnfant(),
							s.getArrestation().getArrestationId().getNumOrdinale());
					boolean allSameName = affprincipale.stream().allMatch(x -> x.getTypeDocument().equals("AEX"));
					if (allSameName) {
						s.setDateFin(affprincipale.get(0).getDocuments()
								.get(affprincipale.get(0).getDocuments().size() - 1).getDateEmission());

					}
				}

				return s;

			}).collect(Collectors.toList());

			listResidences.sort(Comparator.comparing(Residence::getDateFin));
			break;

		case "sortieMutation":

//					 
			listResidences = (List<Residence>) residenceRepository.listDetenusSortieMutations(
					 pDFListExistDTO.getClassePenale(),
						pDFListExistDTO.getNiveauEducatif(),
						pDFListExistDTO.getGouvernorat(),
						pDFListExistDTO.getSituationFamiliale(), 
						pDFListExistDTO.getSituationSocial(),
						pDFListExistDTO.getMetier(), 
						pDFListExistDTO.getDelegation(),
						(float) pDFListExistDTO.getAge1(), 
						(float) pDFListExistDTO.getAge2(),
						pDFListExistDTO.getCheckEtranger()==null, 
						pDFListExistDTO.getNationalite(),
						pDFListExistDTO.getGouvernoratTribunal(),
						pDFListExistDTO.getTypeTribunal(), pDFListExistDTO.getTypeAffaire(),
						pDFListExistDTO.getEtablissements() ,
						dateDebutGlobale, 
						dateFinGlobale );

			break;

		case "entreeMutation":

//					 
			listResidences = (List<Residence>) residenceRepository.listDetenusEntreMutations(
					 pDFListExistDTO.getClassePenale(),
						pDFListExistDTO.getNiveauEducatif(),
						pDFListExistDTO.getGouvernorat(),
						pDFListExistDTO.getSituationFamiliale(), 
						pDFListExistDTO.getSituationSocial(),
						pDFListExistDTO.getMetier(), 
						pDFListExistDTO.getDelegation(),
						(float) pDFListExistDTO.getAge1(), 
						(float) pDFListExistDTO.getAge2(),
						pDFListExistDTO.getCheckEtranger()==null, 
						pDFListExistDTO.getNationalite(),
						pDFListExistDTO.getGouvernoratTribunal(),
						pDFListExistDTO.getTypeTribunal(), pDFListExistDTO.getTypeAffaire(),
						pDFListExistDTO.getEtablissements() ,
						dateDebutGlobale, 
						dateFinGlobale );

			break;

		case "nonAff":

//					 
			listResidences = (List<Residence>) residenceRepository.listDetenusSansAffaires( pDFListExistDTO.getEtablissements() );

			break;
		default:

			listResidences = new ArrayList<>();
		}
		
		 
 
		// Étape 2 : Collecte des identifiants nécessaires pour la récupération des affaires
		Set<String> idsEnfant = listResidences.stream()
			    .map(residence -> {
			        Arrestation arrestation = residence.getArrestation();
			        return (arrestation != null && arrestation.getArrestationId() != null) 
			            ? arrestation.getArrestationId().getIdEnfant() 
			            : null;
			    })
			    .filter(Objects::nonNull) // Filtrer les valeurs nulles
			    .collect(Collectors.toSet());

			Set<Long> numOrdinales = listResidences.stream()
			    .map(residence -> {
			        Arrestation arrestation = residence.getArrestation();
			        return (arrestation != null && arrestation.getArrestationId() != null) 
			            ? arrestation.getArrestationId().getNumOrdinale() 
			            : null;
			    })
			    .filter(Objects::nonNull) // Filtrer les valeurs nulles
			    .collect(Collectors.toSet());

			// Étape 3 : Récupération de toutes les affaires en une seule requête
			List<Affaire> affaires = affaireRepository.findAffairesPrincipales(
			    new ArrayList<>(idsEnfant), 
			    new ArrayList<>(numOrdinales)
			);

			// Étape 4 : Mappage des affaires aux résidences
			Map<String, List<Affaire>> affairesMap = affaires.stream()
			    .collect(Collectors.groupingBy(affaire -> 
			        affaire.getAffaireId().getIdEnfant() + "-" + 
			        affaire.getAffaireId().getNumOrdinaleArrestation()
			    ));

			// Traitement des résidences avec un stream parallèle
			listResidences = listResidences.parallelStream()
			    .map(residence -> {
			        Arrestation arrestation = residence.getArrestation();
			        if (arrestation != null) {
			            String key = arrestation.getArrestationId().getIdEnfant() + "-" +
			                         arrestation.getArrestationId().getNumOrdinale();
			            List<Affaire> lesAffaires = affairesMap.getOrDefault(key, Collections.emptyList());
			            
			            // Mettez à jour la résidence avec les affaires
			            updateResidence(residence, lesAffaires);
			        }
			        return residence;
			    })
			    .filter(residence -> pDFListExistDTO.getTypeJuge() == null || 
			        residence.getArrestation() != null && 
			        residence.getArrestation().getAffaires().stream()
			            .anyMatch(ToolsForReporting.isTypeJugeMatch(pDFListExistDTO.getTypeJuge().getId())))
			    .collect(Collectors.toList());
		 
 
		  
		return  listResidences;
	}
	
	
	public List<Residence> processAndUpdateResidences(List<Residence> enfantAffiche) {
	    // Étape 1 : Collecte des identifiants nécessaires pour la récupération des affaires
	    Set<String> idsEnfant = enfantAffiche.stream()
	        .map(residence -> {
	            Arrestation arrestation = residence.getArrestation();
	            return (arrestation != null && arrestation.getArrestationId() != null)
	                ? arrestation.getArrestationId().getIdEnfant()
	                : null;
	        })
	        .filter(Objects::nonNull) // Filtrer les valeurs nulles
	        .collect(Collectors.toSet());

	    Set<Long> numOrdinales = enfantAffiche.stream()
	        .map(residence -> {
	            Arrestation arrestation = residence.getArrestation();
	            return (arrestation != null && arrestation.getArrestationId() != null)
	                ? arrestation.getArrestationId().getNumOrdinale()
	                : null;
	        })
	        .filter(Objects::nonNull) // Filtrer les valeurs nulles
	        .collect(Collectors.toSet());

	    // Étape 2 : Récupération de toutes les affaires en une seule requête
	    List<Affaire> affaires = affaireRepository.findAffairesPrincipales(
	        new ArrayList<>(idsEnfant),
	        new ArrayList<>(numOrdinales)
	    );

	    // Étape 3 : Mappage des affaires aux résidences
	    Map<String, List<Affaire>> affairesMap = affaires.stream()
	        .collect(Collectors.groupingBy(affaire ->
	            affaire.getAffaireId().getIdEnfant() + "-" +
	            affaire.getAffaireId().getNumOrdinaleArrestation()
	        ));

	    // Étape 4 : Traitement des résidences avec un stream parallèle
	    return enfantAffiche.parallelStream()
	        .map(residence -> {
	            Arrestation arrestation = residence.getArrestation();
	            if (arrestation != null) {
	                String key = arrestation.getArrestationId().getIdEnfant() + "-" +
	                             arrestation.getArrestationId().getNumOrdinale();
	                List<Affaire> lesAffaires = affairesMap.getOrDefault(key, Collections.emptyList());
	                
	                // Mettez à jour la résidence avec les affaires
	                updateResidence(residence, lesAffaires);
	            }
	            return residence;
	        })
	        .collect(Collectors.toList());
	}

}


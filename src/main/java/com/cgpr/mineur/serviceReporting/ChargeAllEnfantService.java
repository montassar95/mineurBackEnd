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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.RapportQuotidien;
import com.cgpr.mineur.models.Residence;
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
	
	
	public int masculinEtranger=0;
	public  int femininEtranger=0;
	
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

		List<Residence> enfantAffiche = null;

		switch (pDFListExistDTO.getEtatJuridiue()) {
		case "arret":

			enfantAffiche = (List<Residence>) residenceRepository.findByAllEnfantExistArret(
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
						pDFListExistDTO.getEtablissement()
                        );

			break;
		case "juge":

			enfantAffiche = (List<Residence>) residenceRepository.findByAllEnfantExistJuge(
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
					pDFListExistDTO.getEtablissement()
					);

			break;
			
			
		case "libere":
			 

			enfantAffiche = (List<Residence>) residenceRepository.findByAllEnfantLibere(
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
					pDFListExistDTO.getEtablissement(),
					
					
					  dateDebutGlobale,
					  dateFinGlobale,
				  pDFListExistDTO.getCauseLiberation( ));

			break;
			
			

		case "audience":

			System.out.println(dateDebutGlobale.toString() + " " + dateFinGlobale.toString());
			enfantAffiche = (List<Residence>) residenceRepository.findByAllEnfantAudience(
					pDFListExistDTO.getClassePenale().getId(), pDFListExistDTO.getNiveauEducatif().getId(),
					pDFListExistDTO.getGouvernorat().getId(), pDFListExistDTO.getSituationFamiliale().getId(),
					pDFListExistDTO.getSituationSocial().getId(), pDFListExistDTO.getMetier().getId(),
					pDFListExistDTO.getDelegation().getId(), pDFListExistDTO.getEtablissement(),
					pDFListExistDTO.getGouvernoratTribunal().getId(), pDFListExistDTO.getTypeTribunal().getId(),
					pDFListExistDTO.getTypeAffaire().getId(), start, end, dateDebutGlobale, dateFinGlobale,
					pDFListExistDTO.getCheckEtranger(), pDFListExistDTO.getNationalite().getId());
			break;

		case "attetAP":

			enfantAffiche = (List<Residence>) residenceRepository.findByAllEnfantExistArret(
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
						pDFListExistDTO.getEtablissement()
                       );
			break;

		case "attetAE":

			enfantAffiche = (List<Residence>) residenceRepository.findByAllEnfantExistArret(
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
						pDFListExistDTO.getEtablissement()
                       );
			break;
		case "attetT":

			enfantAffiche = (List<Residence>) residenceRepository.findByAllEnfantExistArret(
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
						pDFListExistDTO.getEtablissement()
                       );
			break;

		case "jugeR":

			enfantAffiche = (List<Residence>) residenceRepository.findByAllEnfantExistJugeR(
					pDFListExistDTO.getClassePenale().getId(), pDFListExistDTO.getNiveauEducatif().getId(),
					pDFListExistDTO.getGouvernorat().getId(), pDFListExistDTO.getSituationFamiliale().getId(),
					pDFListExistDTO.getSituationSocial().getId(), pDFListExistDTO.getMetier().getId(),
					pDFListExistDTO.getDelegation().getId(), pDFListExistDTO.getEtablissement().getId(),
					pDFListExistDTO.getGouvernoratTribunal().getId(), pDFListExistDTO.getTypeTribunal().getId(),
					pDFListExistDTO.getTypeAffaire().getId(), start, end, pDFListExistDTO.getCheckEtranger(),
					pDFListExistDTO.getNationalite().getId());
			break;

		case "all":
			 
			System.out.println(pDFListExistDTO.toString());
			enfantAffiche = residenceRepository.findResidencesForExistingChildren(pDFListExistDTO.getClassePenale(),
					pDFListExistDTO.getNiveauEducatif(), pDFListExistDTO.getGouvernorat(),
					pDFListExistDTO.getSituationFamiliale(), pDFListExistDTO.getSituationSocial(),
					pDFListExistDTO.getMetier(), pDFListExistDTO.getDelegation(),
					pDFListExistDTO.getEtablissement(), pDFListExistDTO.getGouvernoratTribunal(),
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
			
			System.out.println(dateDebutGlobale);
			System.out.println(dateFinGlobale);

			enfantAffiche = (List<Residence>) residenceRepository.findByAllEnfantDevenuMajeur( 
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
					pDFListExistDTO.getEtablissement() ,
					dateDebutGlobale, dateFinGlobale);

			break;
		case "entreReelle":

//					 
			enfantAffiche = (List<Residence>) residenceRepository.findByAllEnfantEntreReelle(
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
						pDFListExistDTO.getEtablissement(),
						 dateDebutGlobale,
						  dateFinGlobale );

			break;

	

		case "seraLibere":

			enfantAffiche = (List<Residence>) residenceRepository.findByAllEnfantSeraLibere( 	
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
					pDFListExistDTO.getEtablissement(),
					dateDebutGlobale, dateFinGlobale);

			enfantAffiche.stream().map(s -> {

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

			enfantAffiche.sort(Comparator.comparing(Residence::getDateFin));
			break;

		case "sortieMutation":

//					 
			enfantAffiche = (List<Residence>) residenceRepository.findBySortieMutation(
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
						pDFListExistDTO.getEtablissement() ,
						dateDebutGlobale, 
						dateFinGlobale );

			break;

		case "entreeMutation":

//					 
			enfantAffiche = (List<Residence>) residenceRepository.findByEntreeMutation(
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
						pDFListExistDTO.getEtablissement() ,
						dateDebutGlobale, 
						dateFinGlobale );

			break;

		case "nonAff":

//					 
			enfantAffiche = (List<Residence>) residenceRepository.findByAllEnfantNonExist( pDFListExistDTO.getEtablissement() );

			break;
		default:

			enfantAffiche = new ArrayList<>();
		}

		
		enfantAffiche = enfantAffiche.stream()
				 
				.peek(residence ->  updateResidence(residence ))
				
				.filter(residence -> pDFListExistDTO.getTypeJuge()  == null || residence.getArrestation().getAffaires()
						.stream().anyMatch(ToolsForReporting.isTypeJugeMatch(pDFListExistDTO.getTypeJuge().getId())))
				.collect(Collectors.toList());
		return enfantAffiche;
	}
	
	
	public List<List<Residence>> chargeList() {
	    List<List<Residence>> enfantAffiches = new ArrayList<>();

	    try {
	        Date premierJourDuMois = getFirstDayOfMonth();
	        System.err.println(premierJourDuMois.toString()+"--------------------------------**************************");
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

	private Date getFirstDayOfMonth() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.DAY_OF_MONTH, 1);  
		
 
	    return calendar.getTime();
	}
	
	
	private java.sql.Date  getFirstDayOfNextMonth() {
	    // Get the current date
	    LocalDate currentDate = LocalDate.now();

	    // Get the first day of the next month
	    LocalDate firstDayOfNextMonth = currentDate.with(TemporalAdjusters.firstDayOfNextMonth());

	    // Convert LocalDate to java.sql.Date
	    return java.sql.Date.valueOf(firstDayOfNextMonth);
	}
	
	// Définir la date limite comme LocalDate
	 
      
	private List<CompletableFuture<List<Residence>>> getAllResidencesAsync(List<Etablissement> allCentre, Date premierJourDuMois) {
	    return allCentre.stream()
	            .flatMap(e -> Stream.of(
 	                    residenceRepository.findByAllEnfantExistJugeAsync(e.getId(),getFirstDayOfNextMonth()),
                     residenceRepository.findByAllEnfantExistArretAsync(e.getId(),getFirstDayOfNextMonth()),
	                    residenceRepository.findByAllEnfantLibereAsync(
	                            0, 0, 0, 0, 0, 0, 0, e, 0, 0, 0, null, null, premierJourDuMois, new Date(), null)
	            ))
	            .collect(Collectors.toList());
	}

	private void updateResidences(List<List<Residence>> enfantAffiches) {
	    enfantAffiches.forEach(enfantAfficheCentre -> {
	        enfantAfficheCentre.forEach(residence -> {
	            try {
	                updateResidence(residence);
	            } catch (Exception e) {
	                // Handle exceptions here, you can log or perform appropriate error handling
	                e.printStackTrace();
	            }
	        });
	    });
	}

	public void updateResidence(Residence residence) {
	    if (residence.getArrestation() != null && residence.getArrestation().getEnfant() != null) {
	        residence.getArrestation().getEnfant().setImg(null);
	        LocalDate dob = LocalDate.parse(residence.getArrestation().getEnfant().getDateNaissance().toString());
	        LocalDate curDate = LocalDate.now();
	        Period period = Period.between(dob, curDate);
	        residence.getArrestation().setAge(period.getYears());

	        residence.getArrestation().setEchappe(echappesRepository.findByIdEnfantAndResidenceTrouverNull(
	                residence.getArrestation().getArrestationId().getIdEnfant()));

	        residence.getArrestation().setDateDebut(affaireRepository.getDateDebutPunition(
	                residence.getArrestation().getArrestationId().getIdEnfant(),
	                residence.getArrestation().getArrestationId().getNumOrdinale()));

	        residence.getArrestation().setDateFin(affaireRepository.getDateFinPunition(
	                residence.getArrestation().getArrestationId().getIdEnfant(),
	                residence.getArrestation().getArrestationId().getNumOrdinale()));

	        
	        
	        if (residence.getArrestation().getEnfant().getNationalite().getId() != 1) {
	            if (residence.getArrestation().getEnfant().getSexe().toString().equals("ذكر")) {
	                masculinEtranger++;
	               System.err.println(residence.getArrestation().getEnfant().getId()+" etrnger");
	            } else {
	                femininEtranger++;
	            }
	        }
	    //tres important  findByArrestationCoroissant 
	        List<Affaire> lesAffaires = affaireRepository.findAffairePrincipale(
	                residence.getArrestation().getArrestationId().getIdEnfant(),
	                residence.getArrestation().getArrestationId().getNumOrdinale());

	        if (lesAffaires.isEmpty()) {
	            System.out.println("La liste des affaires est vide.");
	        } else {
	            updateAffaires(lesAffaires);
	        }

	        updateArrestationDocuments(residence, lesAffaires);

	        updateArrestationSituation(residence);
	        
	        //Debut traitement
	        mettreAJourNombreVisites(residence, visiteRepository);
			//fint
	    }
	}
	private void mettreAJourNombreVisites(Residence residence, VisiteRepository visiteRepository) {
	    Optional<Visite> v = visiteRepository.findbyEnfantandDate(residence.getResidenceId().getIdEnfant(), 2024,  4);

	    if (v.isPresent()) {
	    	System.err.println(v.get().toString());
	        residence.setNbVisite(v.get().getNbrVisite()+"");
	    } else {
	        residence.setNbVisite("0.");
	    }
	}
	private void updateAffaires(List<Affaire> lesAffaires) {
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
	    lesAffaires = lesAffaires.stream()
	            .peek(s -> {
	            	com.cgpr.mineur.models.Document doc = null;
	            	if (s.getArrestation() != null && s.getArrestation().getArrestationId() != null) {
	            	    doc = documentRepository.getLastDocumentByAffaire (
	            	        s.getArrestation().getArrestationId().getIdEnfant(),
	            	        s.getArrestation().getArrestationId().getNumOrdinale(),
	            	        s.getNumOrdinalAffaire()
	            	    );
	            	    System.out.println(s.getArrestation().getArrestationId().getIdEnfant());
	            	   
	            	}
	            	 
	             
	            	if (doc != null) {
	            		 s.setTypeDocument(doc.getTypeDocument());
	 	                s.setDateEmissionDocument(doc.getDateEmission());

	 	                if (doc instanceof Transfert) {
	 	                    Transfert t = (Transfert) doc;
	 	                    s.setTypeFile(t.getTypeFile());
	 	                } else if (doc instanceof Arreterlexecution) {
	 	                    Arreterlexecution t = (Arreterlexecution) doc;
	 	                    s.setTypeFile(t.getTypeFile());
	 	                }
	            	}
	               
	            })
	            .map(s -> {
	                List<com.cgpr.mineur.models.Document> accData = documentRepository.getDocumentByAffaireforAccusation(
	                        s.getArrestation().getArrestationId().getIdEnfant(),
	                        s.getArrestation().getArrestationId().getNumOrdinale(),
	                        s.getNumOrdinalAffaire(),
	                        PageRequest.of(0, 1)
	                );

	                if (!accData.isEmpty()) {
	                    com.cgpr.mineur.models.Document accDocument = accData.get(0);
	                    s.setDateEmission(accDocument.getDateEmission());

	                    if (accDocument instanceof CarteRecup) {
	                        CarteRecup c = (CarteRecup) accDocument;
	                        s.setTitreAccusations(accusationCarteRecupRepository.getTitreAccusationbyDocument(c.getDocumentId()));
	                        s.setAnnee(c.getAnnee());
	                        s.setMois(c.getMois());
	                        s.setJour(c.getJour());
	                        s.setAnneeArret(c.getAnneeArretProvisoire());
	                        s.setMoisArret(c.getMoisArretProvisoire());
	                        s.setJourArret(c.getJourArretProvisoire());
	                        s.setTypeJuge(c.getTypeJuge());
	                    } else if (accDocument instanceof CarteDepot) {
	                        s.setTitreAccusations(accusationCarteDepotRepository.getTitreAccusationbyDocument(accDocument.getDocumentId()));
	                    } else if (accDocument instanceof CarteHeber) {
	                        s.setTitreAccusations(accusationCarteHeberRepository.getTitreAccusationbyDocument(accDocument.getDocumentId()));
	                    }
	                }

	                return s;
	            })
	            .collect(Collectors.toList());

	    Comparator<Affaire> dateComparator = Comparator.comparing(Affaire::getDateDebutPunition, Comparator.nullsLast(Comparator.naturalOrder()));

	    // Trier la liste 'lesAffaires' en utilisant le comparateur 'dateComparator'
	    Collections.sort(lesAffaires, dateComparator);
	    
	     
	 
	    residence.getArrestation().setAffaires(filterLesAffaires(lesAffaires));
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

	private void updateArrestationSituation(Residence residence) {
	    if (residence.getArrestation().getLiberation() == null) {
	        List<Affaire> aData = documentRepository.findStatutJurByArrestation(residence.getArrestation().getArrestationId().getIdEnfant());

	        if (aData.isEmpty()) {
	            if (residence.getArrestation().getAffaires().size() > 0) {
	                residence.getArrestation().setSituationJudiciaire("juge");
	                boolean allSameName = residence.getArrestation().getAffaires().stream()
	                        .allMatch(x -> x.getTypeDocument().equals("AEX"));
	                if (allSameName) {
	                    residence.getArrestation().setSituationJudiciaire("pasInsertinLiberable");
	                }
	            } else {
	                residence.getArrestation().setSituationJudiciaire("...");
	            }
	        } else {
	            residence.getArrestation().setSituationJudiciaire("arret");
	        }
	    } else {
	        residence.getArrestation().setSituationJudiciaire("libre");
	    }
	}

	
 
	
	public List<List<Residence>> chargeListByDate(LocalDate date) {
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


 

}

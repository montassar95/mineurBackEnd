package com.cgpr.mineur.service.Impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.config.EnfantPDFExporter;
import com.cgpr.mineur.dto.EnfantVerifieDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.ArrestationId;
import com.cgpr.mineur.models.Deces;
import com.cgpr.mineur.models.Echappes;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.models.Liberation;
import com.cgpr.mineur.models.LiberationId;
import com.cgpr.mineur.models.Photo;
import com.cgpr.mineur.models.PhotoId;
import com.cgpr.mineur.models.RapportQuotidien;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.ResidenceId;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DecesRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.EchappesRepository;
import com.cgpr.mineur.repository.EnfantRepository;
import com.cgpr.mineur.repository.LiberationRepository;
import com.cgpr.mineur.repository.RapportQuotidienRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.resource.EnfantAddDTO;
import com.cgpr.mineur.resource.EnfantDTO;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.resource.PDFPenaleDTO;
import com.cgpr.mineur.service.ArrestationService;
import com.cgpr.mineur.service.EnfantService;
import com.cgpr.mineur.service.PhotoService;
import com.cgpr.mineur.service.ResidenceService;
import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;
import com.cgpr.mineur.serviceReporting.EnfantAllCentreService;
import com.cgpr.mineur.serviceReporting.EnfantService1;
import com.cgpr.mineur.serviceReporting.EnfantService2;
import com.cgpr.mineur.tools.AffaireUtils;
import com.cgpr.mineur.tools.ToolsForReporting;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.DocumentException;
 

@Service
public class EnfantServiceImpl implements EnfantService {

	
	 
	@Autowired
	private EnfantRepository enfantRepository;

	private final EnfantService1 enfantService1;

	private final EnfantService2 enfantService2;

	private final EnfantAllCentreService enfantAllCentreService;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private ChargeAllEnfantService chargeAllEnfantService;

	@Autowired
	private RapportQuotidienRepository rapportQuotidienRepository;
	
	@Autowired
	private DecesRepository decesRepository;
	
	
	@Autowired
	private ArrestationRepository  arrestationRepository;
	
	
	
	@Autowired
	private EchappesRepository     echappesRepository ;   
	
	@Autowired
	private LiberationRepository liberationRepository;
	
	@Autowired
	private ResidenceRepository residenceRepository;
	
	
	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private PhotoService photoService;
	
	
	@Autowired
	private ArrestationService  arrestationService;
	@Autowired
	private ResidenceService residenceService;
	
	
	
	
	private @NonNull Cache<Object, Object> cache;

	@Autowired
	public EnfantServiceImpl(EnfantService1 enfanttService1, EnfantService2 enfanttService2,
			EnfantAllCentreService enfantAllCentreService) {
		this.enfantService1 = enfanttService1;
		this.enfantService2 = enfanttService2;
		this.enfantAllCentreService = enfantAllCentreService;

	    this.cache = Caffeine.newBuilder().build(); // Créez votre cache selon vos besoins
	    }

//	 @Scheduled(cron = "0 0 8-17 * * *") // Exécute la méthode toutes les heures de 8h à 17h
	 @Scheduled(cron = "0 0/30 8-17 * * *") // Exécute la méthode toutes les 30 minutes de 8h à 16h
	    public void refreshCache() {
	        try {
	            byte[] data = enfantAllCentreService.exportEtatAllCentre(null);
	            cache.put("monta", data);
	            // Vous pouvez ajouter des journaux pour vérifier si le rafraîchissement fonctionne correctement.
	        } catch (Exception e) {
	            // Gérer les exceptions ici
	        }
	    }
//	private byte[] fetchData(String cacheKey) throws DocumentException, ArabicShapingException, IOException {
//		return enfantAllCentreService.exportEtatAllCentre(null);
//	}

	@Override
	public ResponseEntity<InputStreamResource> exportAllEtat( PDFListExistDTO pDFListExistDTO) {
		String dateString = pDFListExistDTO.getDatePrintAllCentre();
		// Créer un formateur de date avec le format spécifié
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Obtenir la date actuelle
        LocalDate aujourdhui = LocalDate.now();
        
        // Formater la date au format spécifié
         String  dateAujourdhui = aujourdhui.format(formatter);
		if (dateString == null || dateString.equals(dateAujourdhui) ) {
			String cacheKey = "monta";
			byte[] cachedResponse = (byte[]) cache.getIfPresent(cacheKey);
			if (cachedResponse != null) {
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Disposition", "inline; filename=enfnat.pdf");

				ResponseEntity<InputStreamResource> response = ResponseEntity.ok().headers(headers)
						.contentType(MediaType.APPLICATION_PDF)
						.body(new InputStreamResource(new ByteArrayInputStream(cachedResponse)));
				return response;
			}

			try {
				byte[] bais = enfantAllCentreService.exportEtatAllCentre(null);

				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Disposition", "inline; filename=enfnat.pdf");

				ResponseEntity<InputStreamResource> response = ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_PDF)
						.body(new InputStreamResource(new ByteArrayInputStream(bais)));

				cache.put(cacheKey, bais); // Mettre le résultat dans le cache

				return response;
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (ArabicShapingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				byte[] baisBase = null;
				try {
					baisBase = enfantAllCentreService.exportEtatAllCentre(dateString);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Disposition", "inline; filename=enfnat.pdf");
				// headers.setContentLength(bais.available());
				ResponseEntity<InputStreamResource> response = ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_PDF)
						.body(new InputStreamResource(new ByteArrayInputStream(baisBase)));

				return response;
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (ArabicShapingException e) {
				e.printStackTrace();
			}
	 	}
		return ResponseEntity.status(444).build();
	}

	@Override
	public List<Residence> getEnfants( EnfantDTO enfantDTO) {

		List<Residence> enfantData = null;
		if (enfantDTO != null) {
			if (enfantDTO.getDateNaissance() != null) {
				enfantData = enfantRepository.search(enfantDTO.getNom().trim(), enfantDTO.getPrenom().trim(),
						enfantDTO.getNomPere().trim(), enfantDTO.getNomGrandPere().trim(),
						enfantDTO.getNomMere().trim(), enfantDTO.getPrenomMere().trim(), enfantDTO.getDateNaissance(),
						enfantDTO.getSexe().trim());
			} else {
				enfantData = enfantRepository.searchSansDate(enfantDTO.getNom().trim().toString(),
						enfantDTO.getPrenom().trim().toString(), enfantDTO.getNomPere().trim().toString(),
						enfantDTO.getNomGrandPere().trim().toString(), enfantDTO.getNomMere().trim().toString(),
						enfantDTO.getPrenomMere().trim().toString(),

						enfantDTO.getSexe().trim().toString());

			}
		}

		if (enfantData != null) {

			return enfantData;
		} else {
			return   null ;
		}
	}

//    
//	@PostMapping("/search")
//	public ApiResponse<List<Enfant>> search(@RequestBody Enfant enfant) {
//
//		List<Enfant> enfantData = enfantRepository.search(enfant.getNom(), enfant.getPrenom(), enfant.getNomPere(),
//				enfant.getNomGrandPere());
//		if (enfantData != null) {
//			return new ApiResponse<>(HttpStatus.OK.value(), "enfantData fetched suucessfully", enfantData);
//		} else {
//			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "enfantData Not FOund", null);
//		}
//	}

	@Override
	public ResponseEntity<InputStreamResource> exportToPDF( PDFPenaleDTO pDFPenaleDTO)  {

		// Optional<Enfant> enfanttData = enfantRepository.findById(id);

		try {

			EnfantPDFExporter exporter = new EnfantPDFExporter();
			ByteArrayInputStream bais = enfantService1.export(pDFPenaleDTO);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=enfnat.pdf");

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bais));

		} catch (IOException e) {

			e.printStackTrace();
		} catch (ArabicShapingException e) {

			e.printStackTrace();
		} catch (com.itextpdf.text.DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (ResponseEntity<InputStreamResource>) ResponseEntity.status(444);

	}

	@Override
	public ResponseEntity<InputStreamResource> exportEtatToPDF(PDFListExistDTO pDFListExistDTO)
			throws IOException {

		try {

//           	 EnfantPDFExporter exporter = new EnfantPDFExporter( );
			ByteArrayInputStream bais = enfantService2.exportEtat(pDFListExistDTO);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=enfnat.pdf");

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bais));

		} catch (IOException e) {

			e.printStackTrace();
		} catch (ArabicShapingException e) {

			e.printStackTrace();
		} catch (com.itextpdf.text.DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (ResponseEntity<InputStreamResource>) ResponseEntity.status(444);

	}



	@Override
	public List<Enfant> listEtablissement() {
		return enfantRepository.findAll();
	}

	@Override
	public List<List<Residence>> listCharge() {
		try {
//			System.out.println("hi");

			List<List<Residence>> chargeList = chargeAllEnfantService.chargeList();
			ObjectMapper objectMapper = new ObjectMapper();
			String residenceJson = objectMapper.writeValueAsString(chargeList);

			RapportQuotidien rapportQuotidien = new RapportQuotidien();
			rapportQuotidien.setDateSauvgarde(LocalDateTime.now());
			rapportQuotidien.setListResidance(residenceJson);

			rapportQuotidienRepository.save(rapportQuotidien);
			return null;

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			System.out.println("Une erreur s'est produite JsonProcessingException." + e);
			return  null;
		} catch (Exception e) {
			// Journalisez l'erreur à des fins de débogage
			System.out.println("Une erreur s'est produite lors du traitement de la demande." + e);

			// Renvoyez une réponse d'erreur au client
			return  null;
		}
	}



	@Override
	public Enfant getEnfantById(String id) {
		Optional<Enfant> enfanttData = enfantRepository.findById(id);
		if (enfanttData.isPresent()) {
			List<Affaire> aData = documentRepository.findByArrestation(id);
			
			
			try {
				Affaire a = aData.stream().peek(num -> System.out.println("will filter " + num.getTypeDocument()))
						.filter(x -> x.getTypeDocument().equals("CD") || x.getTypeDocument().equals("CH")
								|| x.getTypeDocument().equals("T") || x.getTypeDocument().equals("AP")
								|| x.getTypeDocument().equals("AE") || x.getTypeDocument().equals("CP"))
						.findFirst().orElse(null);
				if (a == null) {
					System.out.println("ma7koum");
					enfanttData.get().setEtat("محكوم");
				} else {
					System.out.println("maw9ouf");
					enfanttData.get().setEtat("موقوف");
				}
            } catch (NullPointerException e) {
                throw new RuntimeException("typeDocument is null " + aData.get(0).getArrestation().getEnfant().getId());
            }
			

			
			return  enfanttData.get();
		} else {
			return  null;
		}
	}

	@Override
	public Residence getoneInResidence( String id) {
		System.out.println(id);
		Optional<Residence> enfantData = enfantRepository.getoneInResidence(id);
		System.out.println("getoneInResidence");

		if (enfantData.isPresent()) {
			return enfantData.get();
		}

		else {
			return  null;
		}
	}

	@Override
	public List<Residence> getResidenceByNum( String numArr) {
		List<Residence> enfantData = enfantRepository.getResidenceByNum(numArr);

		if (enfantData != null) {
			return  enfantData;
		}

		else {
			return  null;
		}
	}

	@Override
	public Enfant save( Enfant enfant,  String idEta) {

		try {

			String id = enfantRepository.maxId(idEta);
			if (id != null) {

				String aux = (Long.parseLong(id) + 1) + "";
				String idResult = (aux.length() == 7) ? aux = "0" + aux : aux;

				enfant.setId(idResult);
			} else {
				enfant.setId(idEta + "000001");
			}

			Enfant e = enfantRepository.save(enfant);

			return  e;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Residence save(EnfantAddDTO enfantAddDTO) {
	Enfant enfant= 	save(  enfantAddDTO.getEnfant() ,  enfantAddDTO.getEtablissement().getId());
	ArrestationId arrestationId = new ArrestationId();
	arrestationId.setIdEnfant(enfant.getId());
	arrestationId.setNumOrdinale(1);
	Arrestation arrestation  = enfantAddDTO.getArrestation();
	arrestation.setArrestationId(arrestationId);
	arrestation.setEnfant(enfant);
	Arrestation newArrestation = arrestationService.save(arrestation);
	
	ResidenceId residenceId = new ResidenceId();
	residenceId.setIdEnfant(enfant.getId());
	residenceId.setNumOrdinaleArrestation(1);
	residenceId.setNumOrdinaleResidence(1);
	
	Residence residence = enfantAddDTO.getResidence();
	
	residence.setResidenceId(residenceId);
	residence.setArrestation(arrestation);
	
	
	if ( enfantAddDTO.getImg()!=null) {
	    Photo photo = new Photo();
	    PhotoId photoId =  new PhotoId();
	    photoId.setIdEnfant(newArrestation.getArrestationId().getIdEnfant());
	    photoId.setNumOrdinaleArrestation(newArrestation.getArrestationId().getNumOrdinale());
	   
	    photo.setPhotoId(photoId); // Assignation manuelle de l'identifiant
	    photo.setImg(enfantAddDTO.getImg());
	    photo.setArrestation(newArrestation);
	    photoService.save(photo);
	}
	
	
	Residence newnew   = residenceService.save(residence);
	
	System.out.println(newnew.toString());
//		 mise a jour code add enfant 
  return newnew;
	}

	@Override
	public Enfant update( Enfant enfant) {
		try {

			Enfant e = enfantRepository.save(enfant);
			return  e;
		} catch (Exception e) {

			return null;
		}

	}

	@Override
	public Void delete( String id) {
		try {
			enfantRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public EnfantVerifieDto chercherEnfantAvecVerification( String id) {
		String mes = " ";
		
		 
		Optional<Enfant> enfanttData = enfantRepository.findById(id);
		Optional<Deces> deces = decesRepository.findById(Long.parseLong(id));
		Echappes echappe = echappesRepository.findByIdEnfantAndResidenceTrouverNull(id);
		Arrestation arrestation = arrestationRepository.findByIdEnfantAndStatut0(id);
		LiberationId liberationId = new LiberationId(arrestation.getArrestationId().getIdEnfant(), arrestation.getArrestationId().getNumOrdinale());
		Optional<Liberation> liberation = liberationRepository.findById(liberationId);
		Residence residence = residenceRepository.findByIdEnfantAndStatut0(arrestation.getArrestationId().getIdEnfant(), arrestation.getArrestationId().getNumOrdinale());
		Residence residenceEncour = residenceRepository.findByIdEnfantAndStatutEnCour(arrestation.getArrestationId().getIdEnfant(), arrestation.getArrestationId().getNumOrdinale());
	
		if (deces.isPresent()) {
	        mes = "طفل فــي ذمــــــة اللـــه";
	    } else if (echappe != null) {
	        mes = "طفل في حالــــــة فـــرار";
	    } else if (liberation.isPresent()) {
	    	 
	        mes = "طفل  في حالـــة ســراح";
	    }
	      else if (residenceEncour != null ) {
	            mes = "نقلـــة جـــارية إلـــى مركــز    " + residenceEncour.getEtablissement().getLibelle_etablissement();
	      }
	      else  {
	            mes = "طفــل مقيــم بمركــز     " + residence.getEtablissement().getLibelle_etablissement();
	      }
		
		List<Arrestation> allArrestation = arrestationRepository.findByIdEnfant(id);

		if (allArrestation != null && !allArrestation.isEmpty()) {
			allArrestation=	allArrestation.stream()
            .map(arr -> AffaireUtils.processArrestationToGetAffairPrincipal(arr, affaireRepository))
            .collect(Collectors.toList());
		}

		
		 
		System.err.println(enfanttData.get().getDateNaissance().toString());
		 LocalDate dob = LocalDate
			      .parse(enfanttData.get().getDateNaissance().toString());

			    LocalDate curDate = LocalDate.now();
			    Period period = Period.between(dob, curDate);
			    
			   String arabicMajorityAgeDate = ToolsForReporting.getArabicMajorityAgeDate(enfanttData.get().getDateNaissance().toString());
		return EnfantVerifieDto.builder().enfant(enfanttData.get()).situation(mes).age(period.getYears()+"").
				arrestations(allArrestation).
		                   adultDate(arabicMajorityAgeDate).build();
				
		 
	

	}

	@Override
	public Residence update(EnfantAddDTO enfantAddDTO) {
		 System.out.println(enfantAddDTO.getEnfant().toString());
	       System.out.println(enfantAddDTO.getEtablissement().toString());
	       System.out.println(enfantAddDTO.getArrestation().toString());
	       System.out.println(enfantAddDTO.getResidence().toString());
  //  System.out.println(enfantAddDTO.getImg().toString().length());
	       Enfant enfant = enfantRepository.save(enfantAddDTO.getEnfant());
	    
	       Arrestation arrestation  = enfantAddDTO.getArrestation();
	       arrestation.setEnfant(enfant);
	       arrestation = arrestationRepository.save(arrestation);
	   	
	   
	   	
	   	Residence residence = enfantAddDTO.getResidence();
	   	
	   	 
	   	residence.setArrestation(arrestation);
	   	residence=	residenceRepository.save(residence);
	   	
	   	if ( enfantAddDTO.getImg()!=null) {
	   	    Photo photo = new Photo();
	   	    PhotoId photoId =  new PhotoId();
	   	    photoId.setIdEnfant(arrestation.getArrestationId().getIdEnfant());
	   	    photoId.setNumOrdinaleArrestation(arrestation.getArrestationId().getNumOrdinale());
	   	   
	   	    photo.setPhotoId(photoId); // Assignation manuelle de l'identifiant
	   	    photo.setImg(enfantAddDTO.getImg());
	   	    photo.setArrestation(arrestation);
	   	    photoService.save(photo);
	}
		return residence;
	}

}


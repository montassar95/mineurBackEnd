package com.cgpr.mineur.service.Impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.ArrestationConverter;
import com.cgpr.mineur.converter.EnfantConverter;
import com.cgpr.mineur.converter.ResidenceConverter;
import com.cgpr.mineur.dto.ArrestationDto;
import com.cgpr.mineur.dto.EnfantDto;
import com.cgpr.mineur.dto.EnfantVerifieDto;
import com.cgpr.mineur.dto.ResidenceDto;
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
import com.cgpr.mineur.service.ArrestationService;
import com.cgpr.mineur.service.EnfantService;
import com.cgpr.mineur.service.PhotoService;
import com.cgpr.mineur.service.ResidenceService;
import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;
import com.cgpr.mineur.serviceReporting.GenererRapportPdfMensuelService;
import com.cgpr.mineur.tools.AffaireUtils;
import com.cgpr.mineur.tools.ToolsForReporting;

@Service
public class EnfantServiceImpl implements EnfantService {

	@Autowired
	private EnfantRepository enfantRepository;

	 

 

  
	@Autowired
	private DocumentRepository documentRepository;

	 

	@Autowired
	private RapportQuotidienRepository rapportQuotidienRepository;

	@Autowired
	private DecesRepository decesRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private EchappesRepository echappesRepository;

	@Autowired
	private LiberationRepository liberationRepository;

	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private ArrestationService arrestationService;
	@Autowired
	private ResidenceService residenceService;

	 

 
 

  
	@Override
	public List<ResidenceDto> trouverResidencesParCriteresDetenu(EnfantDTO enfantDTO) {

		List<Residence> residences = null;
		if (enfantDTO != null) {
			if (enfantDTO.getDateNaissance() != null) {
				residences = enfantRepository.search(enfantDTO.getNom().trim(), enfantDTO.getPrenom().trim(),
						enfantDTO.getNomPere().trim(), enfantDTO.getNomGrandPere().trim(),
						enfantDTO.getNomMere().trim(), enfantDTO.getPrenomMere().trim(), enfantDTO.getDateNaissance(),
						enfantDTO.getSexe().trim());
			} else {
				residences = enfantRepository.searchSansDate(enfantDTO.getNom().trim().toString(),
						enfantDTO.getPrenom().trim().toString(), enfantDTO.getNomPere().trim().toString(),
						enfantDTO.getNomGrandPere().trim().toString(), enfantDTO.getNomMere().trim().toString(),
						enfantDTO.getPrenomMere().trim().toString(),

						enfantDTO.getSexe().trim().toString());

			}
		}

		if (residences != null) {

			return residences.stream().map(ResidenceConverter::entityToDto).collect(Collectors.toList());
		} else {
			return null;
		}
	}

 
	 

 

	 
	 

	 
	 

	@Override
	public EnfantDto getEnfantById(String id) {
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

			return EnfantConverter.entityToDto(enfanttData.get());
		} else {
			return null;
		}
	}

	@Override
	public ResidenceDto trouverDerniereResidenceParIdDetenu(String id) {
		System.out.println(id);
		Optional<Residence> residence = enfantRepository.getoneInResidence(id);
		System.out.println("getoneInResidence");

		if (residence.isPresent()) {
			return ResidenceConverter.entityToDto(residence.get());
		}

		else {
			return null;
		}
	}

	@Override
	public List<ResidenceDto> trouverResidencesParNumeroEcrou(String numArr) {
		List<Residence> residences = enfantRepository.getResidenceByNum(numArr);

		if (residences != null) {
			return residences.stream().map(ResidenceConverter::entityToDto).collect(Collectors.toList());
		}

		else {
			return null;
		}
	}

	 

	@Override
	public EnfantVerifieDto trouverDetenuAvecSonStatutActuel(String idEnfant, String idEtab) {
		String mes = " ";
		boolean allowNewAddArrestation = false;
		boolean allowNewCarte = false;
		boolean alerte = false;

		Optional<Enfant> enfanttData = enfantRepository.findById(idEnfant);
		Optional<Deces> deces = decesRepository.findById(Long.parseLong(idEnfant));
		Echappes echappe = echappesRepository.findByIdEnfantAndResidenceTrouverNull(idEnfant);
		Arrestation arrestation = arrestationRepository.findByIdEnfantAndStatut0(idEnfant);
		LiberationId liberationId = new LiberationId(arrestation.getArrestationId().getIdEnfant(),
				arrestation.getArrestationId().getNumOrdinale());
		Optional<Liberation> liberation = liberationRepository.findById(liberationId);
		Residence residence = residenceRepository.findByIdEnfantAndStatut0(arrestation.getArrestationId().getIdEnfant(),
				arrestation.getArrestationId().getNumOrdinale());
		Residence residenceEncour = residenceRepository.findByIdEnfantAndStatutEnCour(
				arrestation.getArrestationId().getIdEnfant(), arrestation.getArrestationId().getNumOrdinale());

		if (deces.isPresent()) {
			mes = "طفل فــي ذمــــــة اللـــه";
			alerte = true;
		} else if (echappe != null) {
			mes = "طفل في حالــــــة فـــرار";
			alerte = true;
		} else if (liberation.isPresent()) {

			mes = "طفل  في حالـــة ســراح";
			allowNewAddArrestation = true;
		} else if (residenceEncour != null) {
			mes = "نقلـــة جـــارية إلـــى مركــز    " + residenceEncour.getEtablissement().getLibelle_etablissement();

			alerte = true;
		} else if (!residence.getEtablissement().getId().equals(idEtab)) {
			mes = "طفــل مقيــم بمركــز     " + residence.getEtablissement().getLibelle_etablissement();
			allowNewCarte = false;
			alerte = true;
		} else {
			allowNewCarte = true;
		}

		List<Arrestation> allArrestation = arrestationRepository.findByIdEnfant(idEnfant);

		if (allArrestation != null && !allArrestation.isEmpty()) {

			List<ArrestationDto> allArrestationDto = allArrestation.stream().map(ArrestationConverter::entityToDto)
					.collect(Collectors.toList());
			allArrestationDto.stream()
					.map(arr -> AffaireUtils.processArrestationToGetAffairPrincipal(arr, affaireRepository))
					.collect(Collectors.toList());
			allArrestation = allArrestationDto.stream().map(ArrestationConverter::dtoToEntity)
					.collect(Collectors.toList());
		}

		System.err.println("allArrestation = " + allArrestation + " allowNewAddArrestation " + allowNewAddArrestation
				+ " allowNewCarte " + allowNewCarte + " alerte " + alerte);

		LocalDate dob = LocalDate.parse(enfanttData.get().getDateNaissance().toString());

		LocalDate curDate = LocalDate.now();
		Period period = Period.between(dob, curDate);

		String arabicMajorityAgeDate = ToolsForReporting
				.getArabicMajorityAgeDate(enfanttData.get().getDateNaissance().toString());

		return EnfantVerifieDto.builder().enfant(enfanttData.get()).situation(mes).age(period.getYears() + "")
				.arrestations(allArrestation).allowNewAddArrestation(allowNewAddArrestation)
				.allowNewCarte(allowNewCarte).alerte(alerte)
				.residenceEncour(ResidenceConverter.entityToDto(residenceEncour)).adultDate(arabicMajorityAgeDate)
				.residence(ResidenceConverter.entityToDto(residence)).build();

	}

 
	 
	private EnfantDto save(EnfantDto enfantDto, String idEta) {
		try {
			// Génération de l'ID pour l'enfant
			String id = enfantRepository.maxId(idEta);
			String idResult = (id != null) ? generateNewId(id) : idEta + "000001";

			enfantDto.setId(idResult);

			// Enregistrement de l'enfant et conversion
			Enfant savedEnfant = enfantRepository.save(EnfantConverter.dtoToEntity(enfantDto));
			return EnfantConverter.entityToDto(savedEnfant);
		} catch (Exception e) {
			// Considérer la journalisation de l'erreur
			// logger.error("Erreur lors de l'enregistrement de l'enfant", e);
			return null;
		}
	}

	private String generateNewId(String id) {
		long newId = Long.parseLong(id) + 1;
		return String.format("%07d", newId); // Formatage de l'ID avec des zéros devant
	}

	@Override
	public ResidenceDto creerAdmissionDetenu(EnfantAddDTO enfantAddDTO) {
		EnfantDto enfantDto = save(EnfantConverter.entityToDto(enfantAddDTO.getEnfant()),
				enfantAddDTO.getEtablissement().getId());

		if (enfantDto == null) {
			return null; // Gestion de l'échec de l'enregistrement
		}

		System.out.println("New enfant: " + enfantDto.toString());

		ArrestationDto newArrestation = createArrestation(enfantAddDTO, enfantDto);
		if (newArrestation == null) {
			return null; // Gestion de l'échec de l'enregistrement
		}

		ResidenceDto newResidence = createResidence(enfantAddDTO, enfantDto, newArrestation);
		if (newResidence == null) {
			return null; // Gestion de l'échec de l'enregistrement
		}

		System.out.println("New Residence: " + newResidence.toString());
		return newResidence;
	}

	private ArrestationDto createArrestation(EnfantAddDTO enfantAddDTO, EnfantDto enfantDto) {
		ArrestationId arrestationId = new ArrestationId();
		arrestationId.setIdEnfant(enfantDto.getId());
		arrestationId.setNumOrdinale(1);

		Arrestation arrestation = enfantAddDTO.getArrestation();
		arrestation.setArrestationId(arrestationId);
		arrestation.setEnfant(EnfantConverter.dtoToEntity(enfantDto));

		return arrestationService.save(ArrestationConverter.entityToDto(arrestation));
	}

	private ResidenceDto createResidence(EnfantAddDTO enfantAddDTO, EnfantDto enfantDto,
			ArrestationDto newArrestation) {
		ResidenceId residenceId = new ResidenceId();
		residenceId.setIdEnfant(enfantDto.getId());
		residenceId.setNumOrdinaleArrestation(1);
		residenceId.setNumOrdinaleResidence(1);

		Residence residence = enfantAddDTO.getResidence();
		residence.setResidenceId(residenceId);
		residence.setArrestation(ArrestationConverter.dtoToEntity(newArrestation));

		if (enfantAddDTO.getImg() != null) {
			savePhoto(enfantAddDTO.getImg(), newArrestation);
		}

		return residenceService.save(ResidenceConverter.entityToDto(residence));
	}

	private void savePhoto(String img, ArrestationDto newArrestation) {
		Photo photo = new Photo();
		PhotoId photoId = new PhotoId();
		photoId.setIdEnfant(newArrestation.getArrestationId().getIdEnfant());
		photoId.setNumOrdinaleArrestation(newArrestation.getArrestationId().getNumOrdinale());

		photo.setPhotoId(photoId);
		photo.setImg(img);
		photo.setArrestation(ArrestationConverter.dtoToEntity(newArrestation));
		photoService.save(photo);
	}

	@Override
	public ResidenceDto mettreAJourAdmissionDetenu(EnfantAddDTO enfantAddDTO) {
		// Journaliser les données reçues
		System.out.println(enfantAddDTO.toString());

		Enfant enfant = enfantRepository.save(enfantAddDTO.getEnfant());
		Arrestation arrestation = enfantAddDTO.getArrestation();
		arrestation.setEnfant(enfant);
		arrestation = arrestationRepository.save(arrestation);

		Residence residence = enfantAddDTO.getResidence();
		residence.setArrestation(arrestation);
		residence = residenceRepository.save(residence);

		if (enfantAddDTO.getImg() != null) {
			// arrestation est de type ArrestationDto
			savePhoto(enfantAddDTO.getImg(), ArrestationConverter.entityToDto(arrestation));
		}

		return ResidenceConverter.entityToDto(residence);
	}

}

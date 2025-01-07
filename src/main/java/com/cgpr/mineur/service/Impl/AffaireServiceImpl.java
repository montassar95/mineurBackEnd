package com.cgpr.mineur.service.Impl;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.AffaireIdConverter;
import com.cgpr.mineur.converter.ArrestationConverter;
import com.cgpr.mineur.converter.ArretProvisoireConverter;
import com.cgpr.mineur.converter.ResidenceConverter;
import com.cgpr.mineur.dto.AffaireData;
import com.cgpr.mineur.dto.AffaireDto;
import com.cgpr.mineur.dto.ArrestationDto;
import com.cgpr.mineur.dto.ArretProvisoireDto;
import com.cgpr.mineur.dto.DocumentSearchCriteriaDto;
import com.cgpr.mineur.dto.FicheDeDetentionDto;
import com.cgpr.mineur.dto.ResidenceDto;
import com.cgpr.mineur.dto.TribunalDto;
import com.cgpr.mineur.dto.VerifierAffaireDto;
import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.AffaireId;
import com.cgpr.mineur.models.ArrestationId;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.ArretProvisoireRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.EchappesRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.service.AffaireService;
import com.cgpr.mineur.tools.AffaireUtils;

@Service
public class AffaireServiceImpl implements AffaireService {

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private EchappesRepository echappesRepository;
	
	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private ArretProvisoireRepository arretProvisoireRepository;

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public Object calculerDateFin(String date, int duree) {
		java.util.Date dateC = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateDtring = "";

		try {
			dateC = simpleDateFormat.parse(date);

			Calendar cal = Calendar.getInstance();
			cal.setTime(dateC);

			cal.add(Calendar.DATE, duree);
			java.util.Date modifiedDate = cal.getTime();

			dateDtring = simpleDateFormat.format(modifiedDate);
			System.out.println(dateDtring);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateDtring;

	}

	@Override
	public AffaireDto mettreAJourNumeroOrdinal(AffaireDto affaireDto) {
		// Conversion du DTO en entité
		Affaire affaire = AffaireConverter.dtoToEntitybasic(affaireDto);

		
		
		// Récupération du numéro ordinal maximum pour l'arrestation active
		Integer result = affaireRepository
				.trouverMaxAffaireParArrestationIdEtStatutActif(affaire.getArrestation().getArrestationId());
		
		int maxAffaire = (result != null) ? result : 0; 

		try {

			// Vérification de l'existence de l'affaire dans la base de données
			Optional<Affaire> affaireExistante = affaireRepository.findById(affaire.getAffaireId());

			if (affaireExistante.isPresent()) {
				// L'affaire existe déjà

				// Vérification du lien de l'affaire
				if (affaire.getAffaireLien() == null) {
					System.out.println("L'affaire  n'a pas de lien proposé . Utilisation du numéro ordinal existant.");
					affaire.setNumOrdinalAffaire(affaireExistante.get().getNumOrdinalAffaire());
				} else {
					System.out.println("Erreur : l'affaire existe déjà et un lien ne peut pas être ajouté.");
					// Gestion de l'exception ici si nécessaire
				}

				return AffaireConverter.entityToDto(affaire);
			} else {
				// L'affaire n'existe pas
				System.out.println("Aucune affaire principale trouvée.");

				if (affaire.getAffaireLien() == null) {
					System.out.println("Aucune affaire de lien proposé. Attribution d'un nouveau numéro ordinal.");
					affaire.setNumOrdinalAffaire(maxAffaire + 1);
				} else {
					System.out.println("Affaire de lien proposé.");

					// Vérification de l'existence de l'affaire de lien
					Optional<Affaire> affaireLienExistante = affaireRepository
							.findById(affaire.getAffaireLien().getAffaireId());

					if (affaireLienExistante.isPresent()) {
						// L'affaire de lien existe
						System.out.println(
								"L'affaire  de lien proposé existe . Utilisation du numéro ordinal de l''affaire  de lien existant.");
						affaire.setNumOrdinalAffaire(affaire.getAffaireLien().getNumOrdinalAffaire());
					} else {
						// L'affaire de lien n'existe pas
						System.out.println(
								"Aucune affaire de lien trouvée. Attribution du même nouveau numéro ordinal que l'affaire principale.");
						affaire.getAffaireLien().setNumOrdinalAffaire(maxAffaire + 1);
						affaire.setNumOrdinalAffaire(maxAffaire + 1);
					}
				}

				return AffaireConverter.entityToDto(affaire);
			}

		} catch (Exception e) {
			// Gestion de l'exception, enregistrement de l'erreur
			System.err.println("Erreur lors de la mise à jour de l'affaire : " + e.getMessage());
			return null;
		}
	}

	@Override
	public FicheDeDetentionDto obtenirInformationsDeDetentionParIdDetention(String idEnfant, long numOrdinale) {

		
		System.out.println("*********************** debut fiche detention **************");
		// Récupérer l'arrestation
		ArrestationId arrestationId = new ArrestationId(idEnfant, numOrdinale);
		ArrestationDto arrestation = ArrestationConverter.entityToDto(arrestationRepository.findById(arrestationId).get());
          System.out.println(arrestation.toString());
		// Récupérer la liste des affaires en fonction des paramètres fournis
		List<AffaireDto> lesAffairesDto = trouverAffairesParAction("general", idEnfant, numOrdinale);
		
		 System.out.println(lesAffairesDto.toString());
		FicheDeDetentionDto dto = new FicheDeDetentionDto();

		// Si aucune affaire n'est trouvée, retourner un DTO vide
		if (lesAffairesDto.isEmpty()) {
			System.out.println("no");
		 
			return dto;
		}
		else {
			System.out.println("yes");
		}

		dto.setArrestation(arrestation);
		
		
		System.out.println(lesAffairesDto.size());
		// Mettre à jour les informations de l'affaire principale
		AffaireUtils.updateAffairePrincipale(lesAffairesDto);
		System.out.println(lesAffairesDto.size());
		// Déterminer l'état juridique en fonction de l'affaire principale
		dto.setEtatJuridique(
				AffaireUtils.determineEtatJuridique(arrestation, lesAffairesDto));

		// Trier les affaires par numéro ordinal en ordre décroissant
		lesAffairesDto = lesAffairesDto.stream()
				.sorted(Comparator.comparing(AffaireDto::getNumOrdinalAffaire).reversed()).collect(Collectors.toList());
		dto.setAffaires(lesAffairesDto);

		// Traiter chaque affaire pour les calculs pénaux et les détails de jugement
		for (AffaireDto element : lesAffairesDto) {
			// Effectuer les calculs pénaux si l'affaire n'est pas affectée et répond aux
			// critères
			if (element.getAffaireAffecter() == null && !"AEX".equals(element.getTypeDocument())
					&& (element.getTypeJuge() == null || element.getTypeJuge().getId() != 29)) {
				AffaireUtils.calculerPenal(element, dto);
			}
			// Calculer les détails du jugement provisoire
			AffaireUtils.calculerArret(element, dto);

			// Définir la date de jugement principale
			dto.setDateJugementPrincipale(dateFormat.format(element.getDateEmission()));

			// Vérifier les types de documents spécifiques et définir les indicateurs
			// appropriés
			if ("CJ".equals(element.getTypeDocument()) || "CJA".equals(element.getTypeDocument())) {
				dto.setDateJuge(true);
			}

			// Traiter les documents d'appel
			if ("AP".equals(element.getTypeDocument())) {
				dto.setDateAppelParquet(dateFormat.format(element.getDateEmissionDocument()));
				dto.setAppelParquet(true);
				dto.setDateJuge(true);
			} else if ("AE".equals(element.getTypeDocument())) {
				dto.setDateAppelEnfant(dateFormat.format(element.getDateEmissionDocument()));
				dto.setAppelEnfant(true);
				dto.setDateJuge(true);
			}

			// Traiter les changements de lieu si applicable
			if ("CHL".equals(element.getTypeDocumentActuelle())) {
				AffaireUtils.traiterChangementLieu(element, dto, documentRepository, residenceRepository);
			}

			// Vérifier les types de juge spécifiques et définir l'indicateur d'âge adulte
			if (element.getTypeJuge() != null && element.getTypeJuge().getId() == 4) {
				dto.setAgeAdulte(true);
			}
		}

		// Totaliser les résidences selon des critères spécifiques
		dto.setTotaleRecidenceWithetabChangeManiere(
				residenceRepository.countTotaleRecidenceWithetabChangeManiere(idEnfant, numOrdinale));

		// Totaliser les échappés
		dto.setTotaleEchappe(echappesRepository.countByEnfantAndArrestation(idEnfant, numOrdinale));

		// Totaliser les résidences
		dto.setTotaleRecidence(residenceRepository.countTotaleRecidence(idEnfant, numOrdinale));

		// Définir les informations de libération à partir de la première affaire
		dto.setLiberation(arrestation.getLiberation());

		// Récupérer les résidences associées à l'enfant et à l'arrestation
		List<Residence> residences = residenceRepository.findByEnfantAndArrestation(idEnfant, numOrdinale);
		if (residences != null) {
			List<ResidenceDto> residencesDto = residences.stream().map(ResidenceConverter::entityToDto)
					.collect(Collectors.toList());
			dto.setResidences(residencesDto);
		}

		// Déterminer la date de début de punition
		Date dateDebut = affaireRepository.getDateDebutPunition(idEnfant, numOrdinale);
		if (dateDebut != null) {
			dto.setDateDebut(dateFormat.format(dateDebut));
		}

		// Déterminer la date de fin de punition
		Date dateFin = affaireRepository.getDateFinPunition(idEnfant, numOrdinale);
		if (dateFin == null) {
			boolean allSameName = dto.getEtatJuridique().toString().equals("pasInsertionLiberable");
			if (allSameName) {
				dateFin = lesAffairesDto.get(0).getDocuments().get(0).getDateEmission();
			}
		}

		if (dateFin != null) {
			dto.setDateFin(dateFormat.format(dateFin));
		}
 System.out.println("dateDebut "+dateDebut +"dateFin "+dateFin );
		// Récupérer les arrêts provisoires associés à l'arrestation
		List<ArretProvisoire> list = arretProvisoireRepository.getArretProvisoirebyArrestation(idEnfant, numOrdinale);
		if (!list.isEmpty()) {
			List<ArretProvisoireDto> listDto = list.stream().map(ArretProvisoireConverter::entityToDto)
					.collect(Collectors.toList());
			dto.setArretProvisoires(listDto);
		}
		System.out.println("*********************** debut fiche detention **************");
		return dto;
	}

	@Override
	public VerifierAffaireDto validerAffaire(AffaireData affaireData) {
		System.err.println("------------------------------------------------------------------------");
		System.out.println(affaireData.toString());
		String idEnfant = affaireData.getIdEnfant();
		ArrestationDto arrestationDto = affaireData.getArrestation();
		String numAffaire1 = affaireData.getNumAffaire1();
		TribunalDto tribunal1 = affaireData.getTribunal1();
		String numAffaire2 = affaireData.getNumAffaire2();
		TribunalDto tribunal2 = affaireData.getTribunal2();
		AffaireDto affaireOrigine = affaireData.getAffaireOrigine();
		VerifierAffaireDto verifierAffaireDto = new VerifierAffaireDto();

		AffaireId idAffaire = new AffaireId(idEnfant, numAffaire1, tribunal1.getId(),
				arrestationDto.getArrestationId().getNumOrdinale());
		
		Optional<Affaire> affaireDemmande = affaireRepository.findById(idAffaire);

		// ---------------------------Chercher l'affaire origine existe ou n'exist
		// pas-------------------------------------------------------------------------------------
		if (affaireDemmande.isPresent()) {
			// ------------------- l'affaire origine existe
			// -----------------------------------------------------------------------------------------
			Affaire affaireExist = affaireDemmande.get();

			System.out.println(affaireExist.toString());

			affaireOrigine = AffaireConverter.entityToDto(affaireExist);
			// --------------------Chercher si l'affaire origine est un lien d'autre affaire
			// ou n'est pas un lien d'une aucune affaire
			// -------------------------------------------------------------------------------------
			Optional<Affaire> affairelienResult = affaireRepository.findAffaireByAffaireLien(
					affaireExist.getAffaireId().getIdEnfant(), affaireExist.getAffaireId().getNumAffaire(),
					affaireExist.getAffaireId().getIdTribunal());

			if (affairelienResult.isPresent()) {
				// -------------------- l'affaire origine est un lien d'autre affaire
				// ------------------------ ----------------------------------------------------
				Affaire affairelien = affairelienResult.get();
				verifierAffaireDto.setDisplayAlertAffaireOrigineLier(true);

			}
			// -------------------- l'affaire origine n'est pas un lien d'une aucune affaire
			// ----------
			else {
				// -------------------- Tester si l'affaire d'origine avoir un lien avec un
				// autre affaire ou n'avoir pas un lien avec un affaire ------------------------
				// --------------------------------------------------------------------------------------------

				// -------------------- l'affaire d'origine avoir un lien avec un autre affaire
				// ------------------------ -------------------------------------------
				if (affaireExist.getAffaireLien() != null) {
					// -------------------- Tester si les champs d'affaire de lien sont remplis ou
					// ne sont pas remplis -----------------
					if (numAffaire2 != null && tribunal2 != null) {
						// -------------------- les champs d'affaire de lien sont remplis et l'affaire
						// d'origine avoir un lien avec un autre affaire-------------------
						// ------------------------
						// --------------------------------------------------------------------------------------------

						// -------------------- Tester si les champs d'affaire de lien à saisir sont les
						// memes que l'affaire de lien reel ou nn ------------------------
						// --------------------------------------------------------------------------------------------

						if (affaireExist.getAffaireLien().getAffaireId().getNumAffaire() != numAffaire2
								&& affaireExist.getAffaireLien().getAffaireId().getIdTribunal() != tribunal2.getId()) {

							// -------------------- les champs d'affaire de lien à saisir sont les memes que
							// l'affaire de lien reel ------------------------
							// --------------------------------------------------------------------------------------------

							verifierAffaireDto.setDisplayAlertLienAutre(true);
						} else {
							// -------------------- les champs d'affaire de lien à saisir ne sont pas les
							// memes que l'affaire de lien reel ------------------------
							// --------------------------------------------------------------------------------------------

							verifierAffaireDto.setDisplayAlertLienMeme(true);
						}

					} else {
						// -------------------- les champs d'affaire de lien ne sont pas remplis et
						// l'affaire d'origine avoir un lien avec un autre affaire-------------------
						// ------------------------
						// --------------------------------------------------------------------------------------------

						verifierAffaireDto.setDisplayAlertOrigineExistAvecLien(true);
					}
				}
				// -------------------- l'affaire d'origine n'avoir pas un lien avec un affaire
				// ------------------------
				// --------------------------------------------------------------------------------------------

				else {
					verifierAffaireDto.setDisplayAlertOrigineExistSansLien(true);

					verifierAffaireDto.setNextBoolean(true);
				}
			}
		} else {
			verifierAffaireDto.setDisplayNewAffaireOrigine(true);
			// -----------------------lien-----------------
			// -------------------- assuerer que l'affaire d'origine avoir l'arrestation
			// actuel ------------------------
			// --------------------------------------------------------------------------------------------
			// this.affaireOrigine.arrestation = this.arrestation;

			// -------------------- Tester si les champs d'affaire de lien sont remplis ou
			// ne sont pas remplis ------------------------
			// --------------------------------------------------------------------------------------------

			// -------------------- --les champs d'affaire de lien sont remplis
			// ------------------------
			// --------------------------------------------------------------------------------------------

			if (numAffaire2 != null && tribunal2 != null) {
				AffaireId idAffaireLien = new AffaireId(idEnfant, numAffaire2, tribunal2.getId(),
						arrestationDto.getArrestationId().getNumOrdinale());
				// -------------------- --Chercher l'affaire de lien exisit ou n'existe pas
				// ------------------------
				// --------------------------------------------------------------------------------------------

				Optional<Affaire> affaireLienDemmande = affaireRepository.findById(idAffaireLien);

				if (affaireLienDemmande.isPresent()) {
					Affaire affaireLienExist = affaireLienDemmande.get();

					affaireOrigine.setAffaireLien(AffaireConverter.entityToDto(affaireLienExist));

					// -------------------- -l'affaire de lien exisit ------------------------
					// --------------------------------------------------------------------------------------------

					Optional<Affaire> affairelienResult = affaireRepository.findAffaireByAffaireLien(
							affaireLienExist.getAffaireId().getIdEnfant(),
							affaireLienExist.getAffaireId().getNumAffaire(),
							affaireLienExist.getAffaireId().getIdTribunal());
					// --------------------Chercher si l'affaire de lien est un lien d'autre affaire
					// ou n'est pas un lien d'une aucune affaire
					// -------------------------------------------------------------------------------------

					if (affairelienResult.isPresent()) {

						// -------------------- l'affaire de lien est un lien d'autre affaire
						// ------------------------
						// -------------------------------------------------------------------------------------

						verifierAffaireDto.setDisplayAlertAffaireLienLier(true);
					} else {
						// -------------------- l'affaire de lien n'est pas un lien d'autre affaire
						// ------------------------
						// -------------------------------------------------------------------------------------

						affaireOrigine = mettreAJourNumeroOrdinal(affaireOrigine);
						verifierAffaireDto.setNextBoolean(true);
					}

				} else {
					// -------------------- -- l'affaire de lien n'existe pas
					// ----------------------------------------------------------------------------------------------------------------------

					List<Affaire> eData = affaireRepository.findAffaireByAnyArrestation(idEnfant, numAffaire2,
							tribunal2.getId());
					if (!eData.isEmpty()) {
						verifierAffaireDto.setDisplayAlertLienAutreArrestation(true);
					} else {
						verifierAffaireDto.setDisplayNext(true); // accepter()

					}

					AffaireDto affaireLien = new AffaireDto();
					affaireLien.setAffaireId(AffaireIdConverter.entityToDto(idAffaireLien));
					affaireLien.setArrestation(arrestationDto);
					affaireLien.setTribunal(tribunal2);
					affaireOrigine.setAffaireLien(affaireLien);
					affaireOrigine = mettreAJourNumeroOrdinal(affaireOrigine);
				}

			}

			// -------------------- les champs d'affaire de lien ne sont pas remplis
			// ------------------------
			// --------------------------------------------------------------------------------------------

			else {
				affaireOrigine.setAffaireLien(null);

				affaireOrigine = mettreAJourNumeroOrdinal(affaireOrigine);

				verifierAffaireDto.setNextBoolean(true);
			}
		}
		affaireOrigine.setAffaireId(AffaireIdConverter.entityToDto(idAffaire));
		verifierAffaireDto.setAffaire(affaireOrigine);
		System.out.println(verifierAffaireDto.toString());
		return verifierAffaireDto;
	}

	@Override
	public List<AffaireDto> trouverAffairesParAction(String action, String idEnfant, long numOrdinale) {
		switch (action) {
		case "general":
			return trouverAffairesGeneral(idEnfant, numOrdinale);
		case "transferer":
			return trouverAffairesATransferer(idEnfant, numOrdinale);
		case "arreter":
			return trouverAffairesAArreter(idEnfant, numOrdinale);
		case "appelerOuReviser":
			return trouverAffairesAAppelerOuReviser(idEnfant, numOrdinale);
		case "prolonger":
			return trouverAffairesAProlonger(idEnfant, numOrdinale);
		default:
			throw new IllegalArgumentException("Action non reconnue");
		}
	}

	private List<AffaireDto> trouverAffairesGeneral(String idEnfant, long numOrdinale) {
		 System.out.println("rqt"+affaireRepository.findAffairePrincipale(idEnfant, numOrdinale).size());
		return processAffaires(affaireRepository.findAffairePrincipale(idEnfant, numOrdinale));

	}

	public List<AffaireDto> trouverAffairesATransferer(String idEnfant, long numOrdinale) {
		return processAffaires(affaireRepository.findByArrestationToTransfert(idEnfant, numOrdinale));
	}

	public List<AffaireDto> trouverAffairesAArreter(String idEnfant, long numOrdinale) {
		return processAffaires(affaireRepository.findByArrestationToArret(idEnfant, numOrdinale));
	}

	public List<AffaireDto> trouverAffairesAAppelerOuReviser(String idEnfant, long numOrdinale) {
		return processAffaires(affaireRepository.trouverAffairesAAppelerOuReviser(idEnfant, numOrdinale));
	}

	public List<AffaireDto> trouverAffairesAProlonger(String idEnfant, long numOrdinale) {
		return processAffaires(affaireRepository.findByArrestationToPropaga(idEnfant, numOrdinale));
	}

	public List<AffaireDto> processAffaires(List<Affaire> lesAffaires) {
	System.out.println("avant "+lesAffaires.size()); 
	
		List<Affaire> output = lesAffaires.stream().map(s -> {

			Document lastDocument = documentRepository.getLastDocumentByAffaire(s.getAffaireId().getIdEnfant(),
					s.getAffaireId().getNumOrdinaleArrestation(), s.getNumOrdinalAffaire());

			if (lastDocument != null) {
				s.setTypeDocument(lastDocument.getTypeDocument());

				s.setDateEmissionDocument(lastDocument.getDateEmission());

				// pour definire le type de Transfert exactement ("إحــــــالة "=> value: "T")
				// ("تخلــــــي"=> value: "A") ( "تعهــــــد"=> value: "G")
				if (lastDocument instanceof Transfert) {

					Transfert t = (Transfert) lastDocument;
                    s.setDateEmission(t.getDateEmission());
					s.setTypeFile(t.getTypeFile());

				}
				// pour definire le type de Arreterlexecution exactement ("إيقاف تنفيذ الحكم "
				// => value: "AEX") ("ســــــــــــراح"=> value: "L")
				if (lastDocument instanceof Arreterlexecution) {
					Arreterlexecution t = (Arreterlexecution) lastDocument;

					s.setTypeFile(t.getTypeFile());

				}

				DocumentSearchCriteriaDto criteria = new DocumentSearchCriteriaDto(s.getAffaireId().getIdEnfant(),
						s.getAffaireId().getNumOrdinaleArrestation(), s.getNumOrdinalAffaire());
				Document accData = documentRepository.getDocumentByAffaireforAccusation(criteria);
				// pour difinier les accusation de l'affaire
				List<TitreAccusation> titreAccusations = new ArrayList<TitreAccusation>();

				if (accData != null) {

					Document lastDocAvecAccusation = accData;

					if (lastDocAvecAccusation instanceof CarteRecup) {

						CarteRecup c = (CarteRecup) lastDocAvecAccusation;
						for (AccusationCarteRecup accusation : c.getAccusationCarteRecups()) {
							titreAccusations.add(accusation.getTitreAccusation());
						}

						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(lastDocAvecAccusation.getDateEmission());

						s.setAnnee(c.getAnnee());
						s.setMois(c.getMois());
						s.setJour(c.getJour());
						s.setAnneeArret(c.getAnneeArretProvisoire());
						s.setMoisArret(c.getMoisArretProvisoire());
						s.setJourArret(c.getJourArretProvisoire());
						s.setTypeJuge(c.getTypeJuge());

					} else if (lastDocAvecAccusation instanceof CarteDepot) {

						CarteDepot c = (CarteDepot) lastDocAvecAccusation;
						for (AccusationCarteDepot accusation : c.getAccusationCarteDepots()) {
							titreAccusations.add(accusation.getTitreAccusation());
						}
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(lastDocAvecAccusation.getDateEmission());

					} else if (lastDocAvecAccusation instanceof CarteHeber) {

						CarteHeber c = (CarteHeber) lastDocAvecAccusation;
						for (AccusationCarteHeber accusation : c.getAccusationCarteHebers()) {
							titreAccusations.add(accusation.getTitreAccusation());
						}

						s.setTitreAccusations(titreAccusations);

						s.setDateEmission(lastDocAvecAccusation.getDateEmission());

					}

				}

			}
			return s;
		}).collect(Collectors.toList());
		System.out.println("apres "+output.stream().map(AffaireConverter::entityToDto).collect(Collectors.toList()).size());
		return output.stream().map(AffaireConverter::entityToDto).collect(Collectors.toList());

	}

}

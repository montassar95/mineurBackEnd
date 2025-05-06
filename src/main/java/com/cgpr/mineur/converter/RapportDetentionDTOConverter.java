// package com.cgpr.mineur.converter;
//
//
//
// import java.time.LocalDate;
// import java.time.Period;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
// import java.util.concurrent.atomic.AtomicInteger;
// import java.util.stream.Collectors;
//
// import com.cgpr.mineur.dto.RapportDetentionDTO;
// import com.cgpr.mineur.dto.RapportDetentionDTO.RapportEvenementJuridiqueDto;
// import com.cgpr.mineur.models.CauseLiberation;
// import com.cgpr.mineur.models.CauseMutation;
// import com.cgpr.mineur.models.ClassePenale;
// import com.cgpr.mineur.models.Delegation;
// import com.cgpr.mineur.models.Enfant;
// import com.cgpr.mineur.models.Etablissement;
// import com.cgpr.mineur.models.Gouvernorat;
// import com.cgpr.mineur.models.Metier;
// import com.cgpr.mineur.models.Nationalite;
// import com.cgpr.mineur.models.NiveauEducatif;
// import com.cgpr.mineur.models.Residence;
// import com.cgpr.mineur.models.SituationFamiliale;
// import com.cgpr.mineur.models.SituationSocial;
// import com.cgpr.mineur.models.TitreAccusation;
// import com.cgpr.mineur.models.Tribunal;
// import com.cgpr.mineur.models.TypeJuge;
// import com.cgpr.mineur.models.rapport.RapportAccusation;
// import com.cgpr.mineur.models.rapport.RapportAffaire;
// import com.cgpr.mineur.models.rapport.RapportDetention;
// import com.cgpr.mineur.tools.ToolsForReporting;
//
// public class RapportDetentionDTOConverter {
// public static RapportDetentionDTO
// rapportDetentionToRapportDetentionDTO(RapportDetention rapportDetention) {
// if (rapportDetention == null) {
// return null;
// }
//
// // Récupérer les valeurs courantes pour éviter de multiples appels
// Enfant enfant = rapportDetention.getEnfant();
// Etablissement etablissementActuel =
// rapportDetention.getEtablissementActuel();
// CauseLiberation causeLiberation = rapportDetention.getCauseLiberation();
// CauseMutation causeMouvArrive = rapportDetention.getCauseMouvArrive();
// CauseMutation causeMouvSortie = rapportDetention.getCauseMouvSortie();
// Etablissement etablissementMouvArrive = rapportDetention.getEtabMouvArrive();
// Etablissement etablissementMouvSortie = rapportDetention.getEtabMouvSortie();
// Delegation delegation = enfant.getDelegation();
// Gouvernorat gouvernorat = enfant.getGouvernorat();
// SituationSocial situationSocial = enfant.getSituationSocial();
// SituationFamiliale situationFamiliale = enfant.getSituationFamiliale();
// Metier metier = enfant.getMetier();
// NiveauEducatif niveauEducatif = enfant.getNiveauEducatif();
// Nationalite nationalite = enfant.getNationalite();
//
// // Utilisation StringBuilder pour les concaténations longues
// StringBuilder nomComplet = new StringBuilder();
// nomComplet.append(enfant.getNom()).append(" بن
// ").append(enfant.getNomPere()).append(" بن ")
// .append(enfant.getNomGrandPere()).append(" ").append(enfant.getPrenom());
//
// String adresse = String.format("%s %s %s",
// enfant.getAdresse().toString().trim(),
// delegation.getLibelle_delegation().toString(),
// gouvernorat.getLibelle_gouvernorat().toString());
//
// String situationFamilialeEtSociale = String.format("%s %s",
// situationSocial.getLibelle_situation_social().toString().trim(),
// situationFamiliale.getLibelle_situation_familiale().toString().trim());
//
// String niveauDeFormationEtProfession = String.format("%s %s",
// metier.getLibelle_metier().toString().trim(),
// niveauEducatif.getLibelle_niveau_educatif().toString().trim());
//
// // Gestion des mouvements avec StringBuilder pour une concaténation
// conditionnelle
// String mouvementArrive = (rapportDetention.getDateMouvArrive() != null &&
// etablissementMouvArrive != null && causeMouvArrive != null)
// ? new StringBuilder().append(rapportDetention.getDateMouvArrive().toString())
// .append(" قدم من
// ").append(etablissementMouvArrive.getLibelle_etablissement())
// .append(": ").append(causeMouvArrive.getLibelle_causeMutation()).toString()
// : "";
//
// String mouvementSortie = (rapportDetention.getDateMouvSortie() != null &&
// etablissementMouvSortie != null && causeMouvSortie != null)
// ? new StringBuilder().append(rapportDetention.getDateMouvSortie().toString())
// .append(" نقل إلى
// ").append(etablissementMouvSortie.getLibelle_etablissement())
// .append(": ").append(causeMouvSortie.getLibelle_causeMutation()).toString()
// : "";
//
// // Gestion de la libération
// String liberation = (rapportDetention.getDateLiberation() != null &&
// causeLiberation != null)
// ? new StringBuilder().append(rapportDetention.getDateLiberation().toString())
// .append(" ").append(causeLiberation.getLibelleCauseLiberation()).toString()
// : "";
//
// // Retourner le DTO
// return RapportDetentionDTO.builder()
// .detentionId(enfant.getId())
// .nomComplet(nomComplet.toString())
// .detailsNaissance("بـــــــ" + enfant.getLieuNaissance() + " " +
// enfant.getDateNaissance())
// .adresse(adresse)
// .situationFamilialeEtSociale(situationFamilialeEtSociale)
// .niveauDeFormationEtProfession(niveauDeFormationEtProfession)
// .dateArrestation(rapportDetention.getDateArrestation())
// .dateDebutPeine(rapportDetention.getDateDebutPeine())
// .dateFinPeine(rapportDetention.getDateFinPeine())
// .mouvementArrive(mouvementArrive)
// .mouvementSortie(mouvementSortie)
// .mutationEnCours(rapportDetention.isMutationEnCours())
// .liberation(liberation)
// .etablissementActuel(etablissementActuel.getLibelle_etablissement())
// .classePenale(calculerClassePenale(enfant.getClassePenale(),
// rapportDetention.getNumOrdinaleArrestation()))
// .age(calculerAge(enfant.getDateNaissance().toString()))
// .nombreVisites(rapportDetention.getNombreVisites())
// .nationalite(nationalite.getLibelle_nationalite())
// .situationJudiciaire(determineStatus(rapportDetention.getSituationJudiciaire()))
// .numeroEcrou(rapportDetention.getNumeroEcrou())
// .evasion(rapportDetention.isEvasion())
// .affaires(toRapportAffaireeList(rapportDetention.getRapportAffaires())) //
// Convertir la liste des affaires
// .build();
// }
//
//
// public static String determineStatus(String situationJudiciaire) {
//// String status;
// if(situationJudiciaire!=null) {
// switch (situationJudiciaire.toString()) {
// case "juge":
// return"محكـــوم" ;
//
// case "arret":
// return "موقـــوف" ;
//
// case "libre":
// return "في حالـــة ســراح" ;
//
//
// case "pasInsertionLiberable":
// return "لم يتم إدراج السراح" ;
//
// default:
// return "...";
//
// }
// };
// return "vide";
//
//
//
// }
//
// private static List< RapportDetentionDTO.RapportAffaire>
// toRapportAffaireeList(List<RapportAffaire> affaires) {
// if (affaires == null) {
// return null;
// }
//
// // Utilisation d'AtomicInteger pour incrémenter un numéro pour chaque affaire
// AtomicInteger counter = new AtomicInteger(1); // Initialisation à 1 pour le
// premier numéro
//
// return affaires.stream()
// .map(affaire -> RapportDetentionDTO.RapportAffaire.builder()
// .numeroOrdinalAffaire(counter.getAndIncrement()) // Incrément du numéro sans
// formatage
// .numeroAffaire(affaire.getNumeroAffaire())
// .tribunal(affaire.getTribunal().getNom_tribunal())
// .typeAffaire(affaire.getTypeAffaire().getLibelle_typeAffaire())
// .accusations(getTitreAccusationString(affaire )) // Accusations
// .evenementJuridiqueList(convertToDTOList(affaire.getRapportEvenementJuridiques()))
// // Liste des événements juridiques
// .jugement( buildTitreRemarque(affaire.getJour(), affaire.getMois() ,
// affaire.getAnnee(), affaire.getTypeJuge() ,
// affaire.getNumeroAffaireAffecter(), affaire.getTribunalAffecter(),
// affaire.isArretExecution()))// Jugement
// .build())
// .collect(Collectors.toList());
// }
//
//
//
//
//
// public static String getTitreAccusationString(RapportAffaire affaire) {
// // Utilisation de StringBuilder pour éviter les concaténations coûteuses de
// chaînes
// StringBuilder titreAccusationStringBuilder = new StringBuilder();
//
// // Parcourir la liste des rapports d'accusation
// for (RapportAccusation rapportAccusation : affaire.getRapportAccusations()) {
// // Récupérer l'objet TitreAccusation à partir de l'objet RapportAccusation
// TitreAccusation titreAccusation = rapportAccusation.getTitreAccusation();
//
// // Ajouter le titre d'accusation à la chaîne
// if (titreAccusation != null &&
// !titreAccusation.getTitreAccusation().isEmpty()) {
// if (titreAccusationStringBuilder.length() > 0) {
// titreAccusationStringBuilder.append(" و ");
// }
// titreAccusationStringBuilder.append(titreAccusation.getTitreAccusation());
// }
// }
//
// // Retourner la chaîne complète
// return titreAccusationStringBuilder.toString();
// }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
// // Méthode optimisée de conversion d'une liste d'entités en une liste de DTO
// public static List<RapportDetentionDTO.RapportEvenementJuridiqueDto>
// convertToDTOList(
// List<com.cgpr.mineur.models.rapport.RapportEvenementJuridique>
// evenementJuridiqueList) {
//
// if (evenementJuridiqueList == null || evenementJuridiqueList.isEmpty()) {
// return Collections.emptyList();
// }
//
// // Utilisation de parallelStream pour des performances accrues sur de grandes
// listes
// return evenementJuridiqueList.parallelStream()
// .map(evenementJuridique -> new
// RapportDetentionDTO.RapportEvenementJuridiqueDto(
// evenementJuridique.getEvenement(),
// evenementJuridique.getEvenementDate()
// ))
// .collect(Collectors.toList());
// }
//
//
//
//
//
//
//
//
//
// // Méthode principale pour construire le titre de la remarque
// public static String buildTitreRemarque(int jour, int mois, int annee,
// TypeJuge typeJuge,
// String numeroAffaireAffecter, Tribunal tribunalAffecter, boolean
// arretExecution) {
//
//
//
//
// return generateLegalCaseString(annee, mois, jour, typeJuge,
// numeroAffaireAffecter, tribunalAffecter, arretExecution);
// }
//
// public static String generateLegalCaseString(int annee, int mois, int jour,
// TypeJuge typeJuge,
// String numeroAffaireAffecter, Tribunal tribunalAffecter, boolean
// arretExecution) {
//
// StringBuilder result = new StringBuilder();
//
// // Si l'année, le mois et le jour ne sont pas tous à zéro, on génère la durée
// du jugement
// if (!(annee == 0 && mois == 0 && jour == 0)) {
// result.append("مدة الحكم: ");
//
// // Ajouter les unités de temps (année, mois, jour)
// appendTimeUnit(result, annee, "عام", "عامين", "أعوام");
// appendTimeUnit(result, mois, "شهر", "شهرين", "أشهر");
// appendTimeUnit(result, jour, "يوم", "يومين", "أيام");
// // Retirer la dernière conjonction "و"
// if (result.toString().endsWith(" و ")) {
// result.setLength(result.length() - 2);
// }
// }
//
// // Si un numéro d'affaire est fourni, l'ajouter à la chaîne
// if (numeroAffaireAffecter != null && !numeroAffaireAffecter.isEmpty()) {
// result.append(" تم الضم إلى القضية عدد :
// ").append(numeroAffaireAffecter).append(" ");
// } else {
// // Si aucun numéro d'affaire, ajouter le type de juge ou indiquer l'arrêt de
// l'exécution
// if (typeJuge != null || arretExecution) {
// if (typeJuge != null) {
// result.append(typeJuge.getLibelle_typeJuge()).append(" ");
// }
// if (arretExecution) {
// result.append("تم إيقاف الحكم (سراح) ");
// }
// }
// else {
// // Indiquer un défaut si aucun des deux (typeJuge ou arretExecution) n'est
// présent
// result.append("---");
// }
// }
//
// return result.toString().trim();
// }
//
//
//// // Génère la remarque lorsque la date est zéro
//// public static String generateRemarqueString(TypeJuge typeJuge, String
// numeroAffaireAffecter, Tribunal tribunalAffecter, boolean arretExecution) {
//// StringBuilder remarque = new StringBuilder();
////
//// if (arretExecution) {
//// remarque.append("تم إيقاف الحكم : سراح");
//// } else {
//// if (numeroAffaireAffecter != null && !numeroAffaireAffecter.isEmpty()) {
//// remarque.append(" تم الضم إلى القضية عدد :
// ").append(numeroAffaireAffecter);
//// } else if (typeJuge != null) {
//// remarque.append(typeJuge.getLibelle_typeJuge());
//// } else {
//// remarque.append("---");
//// }
//// }
////
//// return remarque.toString();
//// }
//
// // Méthode pour ajouter les unités de temps dans le résultat
// private static void appendTimeUnit(StringBuilder result, int value, String
// singular, String dual, String plural) {
// if (value > 0) {
// result.append(generateTimeUnitString(value, singular, dual, plural));
// result.append(" و ");
// }
// }
//
// // Génère la chaîne d'unité de temps (singulier, dual, pluriel)
// public static String generateTimeUnitString(int value, String singular,
// String dual, String plural) {
// if (value == 1 ) {
// return singular;
// }
// else if ( value > 10 ) {
// return value + " " + singular;
// }
// else if (value == 2) {
// return dual;
// } else if (value > 2) {
// return value + " " + plural;
// } else {
// return ""; // Si la valeur est 0, on ne retourne rien
// }
// }
//
//
//
//
//
//
// // Méthode pour convertir un objet de type Detenu en RapportDetentionDTO
// public static RapportDetentionDTO residenceToRapportDetentionDTO(Residence
// residence) {
// if (residence == null) {
// return null;
// }
//
// return RapportDetentionDTO.builder()
// .detentionId(residence.getResidenceId().getIdEnfant())
// .nomComplet(residence.getArrestation().getEnfant().getNom() + " بن " +
// residence.getArrestation().getEnfant().getNomPere() + " بن " +
// residence.getArrestation().getEnfant().getNomGrandPere() + " " +
// residence.getArrestation().getEnfant().getPrenom()) // Combiner nom et prénom
//
// .detailsNaissance("بـــــــ" +
// residence.getArrestation().getEnfant().getLieuNaissance() + " " +
// residence.getArrestation().getEnfant().getDateNaissance())
//
// .adresse(residence.getArrestation().getEnfant().getAdresse().toString().trim()
// + " " +
// residence.getArrestation().getEnfant().getDelegation().getLibelle_delegation().toString()
// +
// " " +
// residence.getArrestation().getEnfant().getGouvernorat().getLibelle_gouvernorat().toString())
//
//
//
// .situationFamilialeEtSociale(residence.getArrestation().getEnfant().getSituationSocial().getLibelle_situation_social().toString().trim()
// + " " +
// residence.getArrestation().getEnfant().getSituationFamiliale().getLibelle_situation_familiale().toString().trim())
//
// .niveauDeFormationEtProfession(residence.getArrestation().getEnfant().getMetier().getLibelle_metier().toString().trim()
// + " " +
// residence.getArrestation().getEnfant().getNiveauEducatif().getLibelle_niveau_educatif().toString().trim())
//
//
// .dateArrestation(residence.getArrestation().getDate())
//
// .dateDebutPeine(residence.getArrestation().getDateDebut())
// .dateFinPeine(residence.getArrestation().getDateFin())
//
//
// .mouvementArrive(residence.getDateEntree() != null &&
// residence.getEtablissementEntree() != null && residence.getCauseMutation() !=
// null
// ? residence.getDateEntree().toString() + " " + "قدم من" + " " +
// residence.getEtablissementEntree().getLibelle_etablissement() + ": " +
// residence.getCauseMutation().getLibelle_causeMutation() + " "
// : "")
// .mouvementSortie(residence.getDateSortie() != null &&
// residence.getEtablissementSortie() != null &&
// residence.getCauseMutationSortie() != null
// ? residence.getDateSortie().toString() + " " + "نقل إلى" + " " +
// residence.getEtablissementSortie().getLibelle_etablissement() + ": " +
// residence.getCauseMutationSortie().getLibelle_causeMutation() + " "
// : "")
//
// .mutationEnCours(residence.getStatut()==2)
//
// .liberation(
// (residence.getArrestation() != null &&
// residence.getArrestation().getLiberation() != null
// && residence.getArrestation().getLiberation().getDate() != null
// && residence.getArrestation().getLiberation().getCauseLiberation() != null
// &&
// residence.getArrestation().getLiberation().getCauseLiberation().getLibelleCauseLiberation()
// != null)
// ? residence.getArrestation().getLiberation().getDate().toString() + " " +
// residence.getArrestation().getLiberation().getCauseLiberation().getLibelleCauseLiberation()
// : "")
//
//
// .etablissementActuel(residence.getEtablissement().getLibelle_etablissement())
// .classePenale(calculerClassePenale(residence.getArrestation().getEnfant().getClassePenale(),residence.getArrestation().getArrestationId().getNumOrdinale()
// ))
// .age(calculerAge(residence
// .getArrestation().getEnfant().getDateNaissance().toString()))
// .nombreVisites( residence.getNbVisite())
//
// .nationalite(
// residence.getArrestation().getEnfant().getNationalite().getLibelle_nationalite())
//
//
//
// .situationJudiciaire(determineStatus(residence.getArrestation().getSituationJudiciaire()))
//
//
//
// .numeroEcrou(residence.getNumArrestation())
//
//
//
//
// // Assurez-vous que ce champ existe dans Detenu
// .evasion(
// residence != null &&
// residence.getArrestation() != null &&
// residence.getArrestation().getEchappe() != null &&
// residence.getArrestation().getEchappe().getDateTrouver() != null
// )
// .affaires(toAffaireList(residence.getArrestation().getAffaires())) //
// Convertir la liste des affaires
// .build();
// }
//
// private static List<RapportDetentionDTO.RapportAffaire>
// toAffaireList(List<com.cgpr.mineur.models.Affaire> affaires) {
// if (affaires == null) {
// return null;
// }
//
// // Utilisation d'AtomicInteger pour incrémenter un numéro pour chaque affaire
// AtomicInteger counter = new AtomicInteger(1); // Initialisation à 1 pour le
// premier numéro
//
// return affaires.stream()
// .map(affaire -> RapportDetentionDTO.RapportAffaire.builder()
// .numeroOrdinalAffaire(counter.getAndIncrement()) // Incrément du numéro sans
// formatage
// .numeroAffaire(affaire.getAffaireId().getNumAffaire().toString())
// .tribunal(affaire.getTribunal().getNom_tribunal())
// .typeAffaire(affaire.getTypeAffaire().getLibelle_typeAffaire())
// .accusations(ToolsForReporting.buildTitreAccusationString(affaire)) //
// Accusations
// .evenementJuridiqueList(processEvenementsJuridique(affaire)) // Liste des
// événements juridiques
// .jugement(ToolsForReporting.buildTitreRemarque(affaire)) // Jugement
// .build())
// .collect(Collectors.toList());
// }
//
//
//
// // Méthode pour convertir une liste d'événements juridiques
// private static List<RapportDetentionDTO.RapportEvenementJuridiqueDto>
// toEvenementJuridiqueList(List<RapportEvenementJuridiqueDto>
// evenementJuridiqueList) {
// if (evenementJuridiqueList == null) {
// return null;
// }
//
// return evenementJuridiqueList.stream()
// .map(event -> RapportDetentionDTO.RapportEvenementJuridiqueDto.builder()
// .evenement(event.getEvenement())
// .date(event.getDate())
// .build())
// .collect(Collectors.toList());
// }
//
//
// public static String calculerClassePenale(ClassePenale classePenale, long
// nbrRetour ) {
// String classe = "--";
//
//
//
//
// // Si nbrRetour est supérieur à 1, construire la classe pénale spécifique
// if (nbrRetour > 1) {
// classe = "عــ" + "0" + nbrRetour + "" + "ـــــــــائـد";
// } else {
//
// classe = classePenale.getLibelle_classe_penale();
//
// }
//
//
// return classe;
// }
//
// public static int calculerAge(String dateNaissance) {
// // Analyser la chaîne de caractères de date de naissance en LocalDate
// LocalDate dob = LocalDate.parse(dateNaissance);
//
// // Obtenir la date actuelle
// LocalDate curDate = LocalDate.now();
//
// // Calculer la période entre la date de naissance et la date actuelle
// Period period = Period.between(dob, curDate);
//
// // Retourner l'âge en années
// return period.getYears();
// }
//
//
//
//
// public static List<RapportEvenementJuridiqueDto>
// processEvenementsJuridique(com.cgpr.mineur.models.Affaire affaire) {
// List<RapportEvenementJuridiqueDto> evenementsJuridique = new ArrayList<>();
//
// try {
// // Traitement du type de document
// switch (affaire.getTypeDocument()) {
// case "CD":
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("بطاقة إيداع",
// affaire.getDateEmission()));
// break;
//
// case "CH":
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("بطاقة إيواء",
// affaire.getDateEmission()));
// break;
//
// case "ArretEx":
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("تاريخ الحكم",
// affaire.getDateEmissionDocument()));
// String typeFileArretEx = affaire.getTypeFile();
// String typeArretEx = (typeFileArretEx == null ||
// typeFileArretEx.equals("ArretEx")) ? "إيقاف تنفيذ الحكم" :
// "ســــــــــــراح";
// evenementsJuridique.add(new RapportEvenementJuridiqueDto(typeArretEx,
// affaire.getDateEmissionDocument()));
// break;
//
// case "CJ":
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("مضمون حكم",
// affaire.getDateEmissionDocument()));
// break;
//
// case "T":
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("صدور البطاقة",
// affaire.getDateEmission()));
// evenementsJuridique.add(new
// RapportEvenementJuridiqueDto(getTypeFileStatusAbondantAffaire(affaire.getTypeFile()),
// affaire.getDateEmissionDocument()));
// break;
//
// case "AE":
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("تاريخ الحكم",
// affaire.getDateEmission()));
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("استئناف الطفل",
// affaire.getDateEmissionDocument()));
// break;
//
// case "AP":
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("تاريخ الحكم",
// affaire.getDateEmission()));
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("استئناف النيابة",
// affaire.getDateEmissionDocument()));
// break;
//
// case "CR":
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("تاريخ الحكم",
// affaire.getDateEmission()));
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("مراجعة",
// affaire.getDateEmissionDocument()));
// break;
//
// case "CRR":
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("تاريخ الحكم",
// affaire.getDateEmission()));
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("رفض المراجعة",
// affaire.getDateEmissionDocument()));
// break;
//
// case "CP":
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("صدور البطاقة",
// affaire.getDateEmission()));
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("قرار تمديد",
// affaire.getDateEmissionDocument()));
// break;
//
// case "CHL":
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("تاريخ القضية",
// affaire.getDateEmission()));
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("تغير مكان الإيداع",
// affaire.getDateEmissionDocument()));
// break;
//
// default:
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("-----",
// affaire.getDateEmission()));
// break;
// }
//
// // Ajouter les événements supplémentaires selon la date de début et de fin de
// la punition
// if (affaire.getDateDebutPunition() != null) {
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("تاريخ البداية",
// affaire.getDateDebutPunition()));
// }
//
// if (affaire.getDateFinPunition() != null &&
// !"AP".equals(affaire.getTypeDocument())) {
// evenementsJuridique.add(new RapportEvenementJuridiqueDto("تاريخ النهاية",
// affaire.getDateFinPunition()));
// }
// } catch (Exception e) {
// e.printStackTrace();
// }
//
// return evenementsJuridique;
// }
// public static String getTypeFileStatusAbondantAffaire(String typeFile) {
// if (typeFile != null) {
// switch (typeFile.trim()) {
// case "T":
// return "إحــــــالة";
// case "A":
// return "تخلــــــي";
// case "G":
// return "تعهــــــد";
// default:
// return "yyy";
// }
// }
//
// return "إحــــــالة";
// }
//
//
//
//
//
//
//
//
// }

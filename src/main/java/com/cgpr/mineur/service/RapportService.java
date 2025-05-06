// package com.cgpr.mineur.service;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
// import com.cgpr.mineur.models.rapport.RapportEvenementJuridique;
// import com.cgpr.mineur.models.Affaire;
// import com.cgpr.mineur.models.Residence;
// import com.cgpr.mineur.models.TitreAccusation;
// import com.cgpr.mineur.models.rapport.RapportAccusation;
// import com.cgpr.mineur.models.rapport.RapportAffaire;
// import com.cgpr.mineur.models.rapport.RapportDetention;
// import com.cgpr.mineur.models.rapport.RapportDetentionId;
// import com.cgpr.mineur.repository.rapport.RapportAccusationRepository;
// import com.cgpr.mineur.repository.rapport.RapportAffaireRepository;
// import com.cgpr.mineur.repository.rapport.RapportDetentionRepository;
// import
// com.cgpr.mineur.repository.rapport.RapportEvenementJuridiqueRepository;
//
// import javax.transaction.Transactional;
// import java.time.LocalDate;
// import java.sql.Date;
// import java.time.Period;
// import java.util.ArrayList;
//
// import java.util.List;
//
// @Service
// public class RapportService {
//
// @Autowired
// private RapportDetentionRepository rapportDetentionRepository;
//
// @Autowired
// private RapportAffaireRepository rapportAffaireRepository;
//
// @Autowired
// private RapportAccusationRepository rapportAccusationRepository;
//
// @Autowired
// private RapportEvenementJuridiqueRepository
// rapportEvenementJuridiqueRepository;
//
// @Transactional
// public void insertData(Residence residence, LocalDate dateSauvegarde) {
// // Création de l'instance RapportDetention
// RapportDetentionId rapportDetentionId = new
// RapportDetentionId(residence.getResidenceId().getIdEnfant(), dateSauvegarde);
//
//
// RapportDetention rapportDetention = new RapportDetention();
// rapportDetention.setRapportDetentionId(rapportDetentionId);
// rapportDetention.setEnfant(residence.getArrestation().getEnfant());
// rapportDetention.setSituationJudiciaire(residence.getArrestation().getSituationJudiciaire());
// rapportDetention.setDateArrestation(residence.getArrestation().getDate());
// if(residence.getArrestation().getDateDebut() != null) {
// rapportDetention.setDateDebutPeine(new
// java.sql.Date(residence.getArrestation().getDateDebut().getTime()));
// }
// if(residence.getArrestation().getDateFin() != null) {
// rapportDetention.setDateFinPeine(new
// java.sql.Date(residence.getArrestation().getDateFin().getTime()));
// }
//
//
//
//
// rapportDetention.setDateMouvArrive(residence.getDateEntree());
// rapportDetention.setEtabMouvArrive(residence.getEtablissementEntree());
// rapportDetention.setCauseMouvArrive(residence.getCauseMutation() );
//
// rapportDetention.setDateMouvSortie(residence.getDateSortie());
// rapportDetention.setEtabMouvSortie(residence.getEtablissementSortie());
// rapportDetention.setCauseMouvSortie(residence.getCauseMutationSortie() );
//
// rapportDetention.setMutationEnCours(residence.getStatut()==2);
//
//
// if(residence.getArrestation().getLiberation()!= null) {
// rapportDetention.setDateLiberation(residence.getArrestation().getLiberation().getDate());
// rapportDetention.setCauseLiberation(residence.getArrestation().getLiberation().getCauseLiberation());
// }
//
//
// rapportDetention.setEtablissementActuel(residence.getEtablissement());
// rapportDetention.setNumOrdinaleArrestation(residence.getArrestation().getArrestationId().getNumOrdinale());
//// rapportDetention.setAge(calculerAge(residence
// .getArrestation().getEnfant().getDateNaissance().toString(),dateSauvegarde));
// rapportDetention.setNombreVisites( residence.getNbVisite());
// rapportDetention.setNumeroEcrou(residence.getNumArrestation());
// rapportDetention.setEvasion(residence != null &&
// residence.getArrestation() != null &&
// residence.getArrestation().getEchappe() != null &&
// residence.getArrestation().getEchappe().getDateTrouver() != null);
// rapportDetentionRepository.save(rapportDetention);
//
// for(Affaire affaire : residence.getArrestation().getAffaires()) {
//
// // Création de l'instance RapportAffaire
// RapportAffaire rapportAffaire = new RapportAffaire();
// rapportAffaire.setNumeroOrdinalAffaire((int) affaire.getNumOrdinalAffaire());
// rapportAffaire.setNumeroAffaire( affaire.getAffaireId().getNumAffaire());
// rapportAffaire.setTypeAffaire(affaire.getTypeAffaire());
// rapportAffaire.setTribunal(affaire.getTribunal());
// rapportAffaire.setRapportDetention(rapportDetention);
//
// rapportAffaire.setJour(affaire.getJour());
// rapportAffaire.setMois(affaire.getMois());
// rapportAffaire.setAnnee(affaire.getAnnee());
// rapportAffaire.setTypeJuge(affaire.getTypeJuge());
// if(affaire.getAffaireAffecter()!= null) {
// rapportAffaire.setNumeroAffaireAffecter(affaire.getAffaireAffecter().getAffaireId().getNumAffaire());
// rapportAffaire.setTribunalAffecter(affaire.getAffaireAffecter().getTribunal());
// }
//
// rapportAffaire.setArretExecution(affaire.getTypeDocument().toString().equals("ArretEx"));
//
// rapportAffaireRepository.save(rapportAffaire);
//
//
// for (TitreAccusation titreAccusation : affaire.getTitreAccusations()) {
//
// // Création de l'instance RapportAccusation
// RapportAccusation rapportAccusation = new RapportAccusation();
// rapportAccusation.setTitreAccusation(titreAccusation); // Set actual
// TitreAccusation entity
// rapportAccusation.setRapportAffaire(rapportAffaire);
// rapportAccusationRepository.save(rapportAccusation);
// }
//
// // Traitement du type de document
// switch (affaire.getTypeDocument()) {
// case "CD":
// saveRapportEvenementJuridique("بطاقة إيداع",
// affaire.getDateEmission(),rapportAffaire);
// break;
//
// case "CH":
// saveRapportEvenementJuridique("بطاقة إيواء",
// affaire.getDateEmission(),rapportAffaire);
// break;
//
// case "ArretEx":
// saveRapportEvenementJuridique("تاريخ الحكم",
// affaire.getDateEmission(),rapportAffaire);
// String typeFileArretEx = affaire.getTypeFile();
// String typeArretEx = (typeFileArretEx == null ||
// typeFileArretEx.equals("ArretEx")) ? "إيقاف تنفيذ الحكم" :
// "ســــــــــــراح";
// saveRapportEvenementJuridique(typeArretEx,
// affaire.getDateEmissionDocument(),rapportAffaire);
// break;
//
// case "CJ":
// saveRapportEvenementJuridique("مضمون حكم",
// affaire.getDateEmission(),rapportAffaire);
// break;
//
// case "T":
// saveRapportEvenementJuridique("صدور البطاقة",
// affaire.getDateEmission(),rapportAffaire);
// saveRapportEvenementJuridique(getTypeFileStatusAbondantAffaire(affaire.getTypeFile()),
// affaire.getDateEmissionDocument(),rapportAffaire);
// break;
//
// case "AE":
// saveRapportEvenementJuridique("تاريخ الحكم",
// affaire.getDateEmission(),rapportAffaire);
// saveRapportEvenementJuridique("استئناف الطفل",
// affaire.getDateEmissionDocument(),rapportAffaire);
// break;
//
// case "AP":
// saveRapportEvenementJuridique("تاريخ الحكم",
// affaire.getDateEmission(),rapportAffaire);
// saveRapportEvenementJuridique("استئناف النيابة",
// affaire.getDateEmissionDocument(),rapportAffaire);
// break;
//
// case "CR":
// saveRapportEvenementJuridique("تاريخ الحكم",
// affaire.getDateEmission(),rapportAffaire);
// saveRapportEvenementJuridique("مراجعة",
// affaire.getDateEmissionDocument(),rapportAffaire);
// break;
//
// case "CRR":
// saveRapportEvenementJuridique("تاريخ الحكم",
// affaire.getDateEmission(),rapportAffaire);
// saveRapportEvenementJuridique("رفض المراجعة",
// affaire.getDateEmissionDocument(),rapportAffaire);
// break;
//
// case "CP":
// saveRapportEvenementJuridique("صدور البطاقة",
// affaire.getDateEmission(),rapportAffaire);
// saveRapportEvenementJuridique("قرار تمديد",
// affaire.getDateEmissionDocument(),rapportAffaire);
// break;
//
// case "CHL":
// saveRapportEvenementJuridique("تاريخ القضية",
// affaire.getDateEmission(),rapportAffaire);
// saveRapportEvenementJuridique("تغير مكان الإيداع",
// affaire.getDateEmissionDocument(),rapportAffaire);
// break;
//
// default:
// saveRapportEvenementJuridique("-----",
// affaire.getDateEmission(),rapportAffaire);
// break;
// }
//
// // Ajouter les événements supplémentaires selon la date de début et de fin de
// la punition
// if (affaire.getDateDebutPunition() != null) {
// saveRapportEvenementJuridique("تاريخ البداية",
// affaire.getDateDebutPunition(),rapportAffaire);
// }
//
// if (affaire.getDateFinPunition() != null &&
// !"AP".equals(affaire.getTypeDocument())) {
// saveRapportEvenementJuridique("تاريخ النهاية",
// affaire.getDateFinPunition(),rapportAffaire);
// }
//
// }
//
//
//
//
//
//
// }
//
// public void saveRapportEvenementJuridique(String evenement , Date date,
// RapportAffaire rapportAffaire) {
// // Création de l'instance RapportEvenementJuridique
// RapportEvenementJuridique rapportEvenementJuridique = new
// RapportEvenementJuridique();
// rapportEvenementJuridique.setEvenement( evenement);
// rapportEvenementJuridique.setEvenementDate(date);
// rapportEvenementJuridique.setRapportAffaire(rapportAffaire);
// rapportEvenementJuridiqueRepository.save(rapportEvenementJuridique);
// }
//
// public static int calculerAge(String dateNaissance,LocalDate dateSauvegarde )
// {
// // Analyser la chaîne de caractères de date de naissance en LocalDate
// LocalDate dob = LocalDate.parse(dateNaissance);
//
//
//
// // Calculer la période entre la date de naissance et la date actuelle
// Period period = Period.between(dob, dateSauvegarde);
//
// // Retourner l'âge en années
// return period.getYears();
// }
//
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
// }
//

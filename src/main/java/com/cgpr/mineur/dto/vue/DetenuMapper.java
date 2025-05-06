//package com.cgpr.mineur.dto.vue;
// 
//
// 
//import com.cgpr.mineur.models.rapport.DetenuView;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class DetenuMapper {
//
//    public List<FicheDetenuDTO> mapToFicheDetenuDTOList(List<DetenuView> detenuViews) {
//        // Map pour regrouper les affaires par détenu
//        Map<String, FicheDetenuDTO> ficheDetenuMap = new HashMap<>();
//
//        for (DetenuView detenuView : detenuViews) {
//            String idDetenue = detenuView.getIdDetenue();
//
//            // Si le détenu n'est pas encore dans la map, on l'ajoute
//            if (!ficheDetenuMap.containsKey(idDetenue)) {
//                FicheDetenuDTO ficheDetenuDTO = new FicheDetenuDTO();
//                ficheDetenuDTO.setIdDetenue(detenuView.getIdDetenue());
//                ficheDetenuDTO.setNomComplet(detenuView.getNomComplet());
//                ficheDetenuDTO.setDateLieuNaissance(detenuView.getDateLieuNaissance());
//                ficheDetenuDTO.setAge(detenuView.getAge());
//                ficheDetenuDTO.setAdresseComplete(detenuView.getAdresseComplete());
//                ficheDetenuDTO.setFormationMetier(detenuView.getFormationMetier());
//                ficheDetenuDTO.setSituationFamilialeSociale(detenuView.getSituationFamilialeSociale());
//                ficheDetenuDTO.setNationalite(detenuView.getNationalite());
//                ficheDetenuDTO.setClassePenale(detenuView.getClassePenale());
//                ficheDetenuDTO.setNumeroOrdArr(detenuView.getNumeroOrdArr());
//                ficheDetenuDTO.setNumeroOrdRes(detenuView.getNumeroOrdRes());
//                ficheDetenuDTO.setNumeroArrestation(detenuView.getNumeroArrestation());
//                ficheDetenuDTO.setNomEtablissement(detenuView.getNomEtablissement());
//                ficheDetenuDTO.setDecision(detenuView.getDecision());
//                ficheDetenuDTO.setDateArrestation(detenuView.getDateArrestation());
//                ficheDetenuDTO.setDateDebutPunition(detenuView.getDateDebutPunition());
//                ficheDetenuDTO.setDateFinPunition(detenuView.getDateFinPunition());
//                ficheDetenuDTO.setAffaires(new ArrayList<>()); // Initialiser la liste des affaires
//
//                ficheDetenuMap.put(idDetenue, ficheDetenuDTO);
//            }
//
//            // Ajouter l'affaire à la liste des affaires du détenu
//            AffaireDTO affaireDTO = new AffaireDTO();
//            affaireDTO.setNumeroAffaire(detenuView.getNumeroAffaire());
//            affaireDTO.setNomTribunal(detenuView.getNomTribunal());
//            affaireDTO.setTypeAffaire(detenuView.getTypeAffaire());
//            affaireDTO.setDateDebutPunitionAffaire(detenuView.getDateDebutPunitionAffaire());
//            affaireDTO.setDateFinPunitionAffaire(detenuView.getDateFinPunitionAffaire());
//            affaireDTO.setTypeDocumentDernier(detenuView.getTypeDocumentDernier());
//            affaireDTO.setTypeDocumentDernierAlias(detenuView.getTypeDocumentDernierAlias());
//            affaireDTO.setDateEmissionDocumentDernier(detenuView.getDateEmissionDocumentDernier());
//            affaireDTO.setTypeDocumentAssocie(detenuView.getTypeDocumentAssocie());
//            affaireDTO.setTypeDocumentAssocieAlias(detenuView.getTypeDocumentAssocieAlias());
//            affaireDTO.setDateEmissionDocumentAssocie(detenuView.getDateEmissionDocumentAssocie());
//            affaireDTO.setTypeJuge(detenuView.getTypeJuge());
//            affaireDTO.setJourPunition(detenuView.getJourPunition());
//            affaireDTO.setMoisPunition(detenuView.getMoisPunition());
//            affaireDTO.setAnneePunition(detenuView.getAnneePunition());
//            affaireDTO.setAccusations(detenuView.getAccusations());
//
//            ficheDetenuMap.get(idDetenue).getAffaires().add(affaireDTO);
//        }
//
//        // Retourner la liste des fiches détenus
//        return new ArrayList<>(ficheDetenuMap.values());
//    }
//}

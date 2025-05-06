package com.cgpr.mineur.repository.rapport;

import org.springframework.stereotype.Repository;

import com.cgpr.mineur.config.DetenuStatutDTO;
import com.cgpr.mineur.config.SQLLoader;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DetenuRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public List<DetenuStatutDTO> findDetenuStatut(LocalDate dateDebut, LocalDate dateFin , String etablissementReside, String situationJudiciaireId) throws IOException {
        // Charger la requête SQL depuis le fichier
        
//    	src/main/resources/
    	String sql = SQLLoader.loadSQL("sql/get_detenu_statut.sql");

        // Créer une requête native
        Query query = entityManager.createNativeQuery(sql);

        // Passer les paramètres à la requête
        query.setParameter("dateDebut", dateDebut);
        query.setParameter("dateFin", dateFin);
        query.setParameter("etablissement_reside", etablissementReside);
        query.setParameter("situation_judiciaire_id", situationJudiciaireId);
       
        // Exécuter la requête et récupérer les résultats sous forme d'une liste d'Object[]
        List<Object[]> results = query.getResultList();

        // Créer une Map pour regrouper les détenus par ID
        Map<String, DetenuStatutDTO> detenuMap = new HashMap<>();

        // Transformer les résultats en une liste de DetenuStatutDTO
        for (Object[] result : results) {
        	// Extraire les valeurs des colonnes retournées
            String detentionId = (String) result[0];
            String nomComplet = (String) result[1];
            String detailsNaissance = (String) result[2];
            String nationalite = (String) result[3];
            String niveauDeFormationEtProfession = (String) result[4];
            String situationFamilialeEtSociale = (String) result[5];
            String adresse = (String) result[6];
            String classePenale = (String) result[7];
            String situationJudiciaire = (String) result[8];
            String numeroEcrou = (String) result[9];
            String dateArrestation = (String) result[10];
            Date dateDebutPeine = (Date) result[11];
            Date dateFinPeine = (Date) result[12];
            String etablissementActuel = (String) result[13];
            String evenementEntree = (String) result[14];
            String age = (String) result[15];
            String typeAffaire = (String) result[16];
            String numeroAffaire = (String) result[17];
            String tribunal = (String) result[18];
            String accusations = (String) result[19];
            String debutPunitionAffaire = (String) result[20];
            String finPunitionAffaire = (String) result[21];
            String documentAssocie = (String) result[22];
            String  documentDernier = (String) result[23];
            String punition = (String) result[24];
            String nbrVisite = (String) result[25];
            
            // Vérifier si le détenu existe déjà dans la Map
            DetenuStatutDTO detenuStatutDTO = detenuMap.getOrDefault(detentionId, new DetenuStatutDTO());
            
            // Remplir les informations du détenu
            detenuStatutDTO.setDetentionId(detentionId);
            detenuStatutDTO.setNomComplet(nomComplet);
            detenuStatutDTO.setDetailsNaissance(detailsNaissance);
            detenuStatutDTO.setNationalite(nationalite);
            detenuStatutDTO.setNiveauDeFormationEtProfession(niveauDeFormationEtProfession);
            detenuStatutDTO.setSituationFamilialeEtSociale(situationFamilialeEtSociale);
            detenuStatutDTO.setAdresse(adresse);
            detenuStatutDTO.setClassePenale(classePenale);
            detenuStatutDTO.setSituationJudiciaire(situationJudiciaire);
            detenuStatutDTO.setNumeroEcrou(numeroEcrou);
            detenuStatutDTO.setDateArrestation(dateArrestation);
            detenuStatutDTO.setDateDebutPeine(dateDebutPeine);
            detenuStatutDTO.setDateFinPeine(dateFinPeine);
            detenuStatutDTO.setEtablissementActuel(etablissementActuel);
            detenuStatutDTO.setEvenementEntree(evenementEntree);
            detenuStatutDTO.setAge(age);
            detenuStatutDTO.setNbrVisite(nbrVisite);
            
            // Ajouter l'affaire à la liste des affaires du détenu
            detenuStatutDTO.addAffaire(
            		numeroAffaire,
            		tribunal, 
            		typeAffaire ,
            		accusations , 
            		documentAssocie ,   
            		documentDernier ,
            		debutPunitionAffaire ,
            		finPunitionAffaire,
            		punition);

            // Mettre à jour ou ajouter le détenu dans la Map
            detenuMap.put(detentionId, detenuStatutDTO);
        }

        // Retourner la liste des détenus avec leurs affaires
        return new ArrayList<>(detenuMap.values());
    }
}

package com.cgpr.mineur.repository.rapport;

import org.springframework.stereotype.Repository;

import com.cgpr.mineur.config.DetenuStatutDTO;
import com.cgpr.mineur.config.SQLLoader;
import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;
import com.cgpr.mineur.dto.StatistiqueGlobaleDTO;

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
public class StatisticsGlobaleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public List<StatistiqueGlobaleDTO> findStatisticsGlobale(LocalDate dateDebut, LocalDate dateFin) throws IOException {
        // Charger la requête SQL depuis le fichier
        String sql = SQLLoader.loadSQL("sql/get_statistics_globale.sql");

        // Créer une requête native
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("dateFin", dateFin);

        // Exécuter la requête et récupérer les résultats sous forme de liste d'Object[]
        List<Object[]> results = query.getResultList();

        // Utiliser une Map pour garantir que chaque établissement est unique
        Map<String, StatistiqueGlobaleDTO> statistiquesMap = new HashMap<>();

        for (Object[] row : results) {
            String libelleEtablissement = (String) row[0];  
            String statut = (String) row[1];        
            String type = (String) row[2];          
            String sexe = (String) row[3];          
            Integer count = (row[4] == null) ? 0 : ((Number) row[4]).intValue(); 

            // Créer la clé unique pour chaque type de statistique
            String key = statut + "_" + type + "_" + (sexe.equals("ذكر") ? "homme" : "femme");

            // Récupérer ou créer un objet StatistiqueGlobaleDTO pour l'établissement
            StatistiqueGlobaleDTO existingDto = statistiquesMap.get(libelleEtablissement);

            if (existingDto == null) {
                // Si l'établissement n'existe pas encore, on le crée et ajoute la statistique
                Map<String, Integer> statisticsMap = new HashMap<>();
                statisticsMap.put(key, count);
                // IMPORTANT: Ajouter la nouvelle StatistiqueGlobaleDTO dans le Map avec la clé libelleEtablissement
                statistiquesMap.put(libelleEtablissement, new StatistiqueGlobaleDTO(libelleEtablissement, statisticsMap));
            } else {
                // Si l'établissement existe, on met à jour ses statistiques
                existingDto.getStatistics().put(key, count);
            }
        }

        System.err.println(statistiquesMap.size() + " taille établissement ...");
        // Convertir la Map en liste pour le retour
        return new ArrayList<>(statistiquesMap.values());
    }
}

 

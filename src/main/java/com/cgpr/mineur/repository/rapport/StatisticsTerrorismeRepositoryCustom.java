package com.cgpr.mineur.repository.rapport;

import org.springframework.stereotype.Repository;

import com.cgpr.mineur.config.DetenuStatutDTO;
import com.cgpr.mineur.config.SQLLoader;
import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;
import com.cgpr.mineur.dto.StatistiqueNationaliteDTO;
import com.cgpr.mineur.dto.StatistiqueTerrorismeDTO;

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
public class StatisticsTerrorismeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public  List<StatistiqueTerrorismeDTO> findStatisticTerrorismes(LocalDate dateDebut, LocalDate dateFin  ) throws IOException {
        // Charger la requête SQL depuis le fichier
//    	src/main/resources/
        String sql = SQLLoader.loadSQL("sql/get_detenu_statut_terrorisme.sql");

        // Créer une requête native
        Query query = entityManager.createNativeQuery(sql);

        // Passer les paramètres à la requête
        query.setParameter("dateDebut", dateDebut);
        query.setParameter("dateFin", dateFin);
       
       
        // Exécuter la requête et récupérer les résultats sous forme d'une liste d'Object[]
        List<Object[]> results = query.getResultList();
       
        List<StatistiqueTerrorismeDTO> statistiques = new ArrayList<>();
        
        // Transformer les résultats en objets DTO
        for (Object[] row : results) {
        	String etablissement = (String) row[0];
            String nationalite = (String) row[1];
            String typeAffaire = (String) row[2];
            String situationJudiciaire = (String) row[3];
            Long totale = ((Number) row[4]).longValue();
            
            statistiques.add(new StatistiqueTerrorismeDTO(etablissement, nationalite, typeAffaire, situationJudiciaire, totale));
        };
        return statistiques;
    }
}

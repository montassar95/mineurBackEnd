package com.cgpr.mineur.repository.rapport;

import org.springframework.stereotype.Repository;

import com.cgpr.mineur.config.DetenuStatutDTO;
import com.cgpr.mineur.config.SQLLoader;
import com.cgpr.mineur.dto.StatTypeTribunalEtalissementDTO;
import com.cgpr.mineur.dto.StatTypeTribunalPeriodeDTO;
import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;
import com.cgpr.mineur.dto.StatistiqueGlobaleDTO;
import com.cgpr.mineur.dto.StatistiqueGouvernoratDTO;
import com.cgpr.mineur.dto.StatistiqueMouvementDTO;
import com.cgpr.mineur.dto.StatistiqueTribunalDTO;

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
public class StatTypeTribunalPeriodeRepository  {

    @PersistenceContext
    private EntityManager entityManager;

    public List<StatTypeTribunalPeriodeDTO> findStatistics( LocalDate dateFin) throws IOException {
        // Charger la requête SQL depuis le fichier
        String sql = SQLLoader.loadSQL("sql/get_statistics_type_tribunal_periode.sql");

        // Créer une requête native
        Query query = entityManager.createNativeQuery(sql);

        // Passer les paramètres à la requête
   
        query.setParameter("dateFin", dateFin);

        // Exécuter la requête et récupérer les résultats sous forme d'une liste d'Object[]
        List<Object[]> results = query.getResultList();

        List<StatTypeTribunalPeriodeDTO> statistiques = new ArrayList<>();

        // Mapper les résultats dans des objets DTO
        for (Object[] row : results) {
            String typeTribunal     = (String) row[0];
            
           
              Long moins_3_mois = ((Number) row[1]).longValue();
              Long entre_3_6_mois = ((Number) row[2]).longValue();
              Long entre_6_9_mois = ((Number) row[3]).longValue(); 
              Long entre_9_12_mois = ((Number) row[4]).longValue();
              Long plus_12_mois = ((Number) row[5]).longValue();
            
            Long totalTypeTribunal = ((Number) row[6]).longValue();
            
              
            
            
              StatTypeTribunalPeriodeDTO dto = new StatTypeTribunalPeriodeDTO(
            		typeTribunal, 
            		
            		moins_3_mois ,  
            		entre_3_6_mois  ,
            		entre_6_9_mois ,
            		entre_9_12_mois ,
            		plus_12_mois  ,
            		totalTypeTribunal
            );
            statistiques.add(dto);
        }

        return statistiques;
    }
}

 

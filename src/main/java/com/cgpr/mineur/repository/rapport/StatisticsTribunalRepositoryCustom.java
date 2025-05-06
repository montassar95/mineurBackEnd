package com.cgpr.mineur.repository.rapport;

import org.springframework.stereotype.Repository;

import com.cgpr.mineur.config.DetenuStatutDTO;
import com.cgpr.mineur.config.SQLLoader;
import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;
import com.cgpr.mineur.dto.StatistiqueGlobaleDTO;
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
public class StatisticsTribunalRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public List<StatistiqueTribunalDTO> findStatistics( LocalDate dateFin) throws IOException {
        // Charger la requête SQL depuis le fichier
        String sql = SQLLoader.loadSQL("sql/get_detenu_tribunal.sql");

        // Créer une requête native
        Query query = entityManager.createNativeQuery(sql);

        // Passer les paramètres à la requête
   
        query.setParameter("dateFin", dateFin);

        // Exécuter la requête et récupérer les résultats sous forme d'une liste d'Object[]
        List<Object[]> results = query.getResultList();

        List<StatistiqueTribunalDTO> statistiques = new ArrayList<>();

        // Mapper les résultats dans des objets DTO
        for (Object[] row : results) {
            String gouvernorat     = (String) row[1];
            String tribunal     = (String) row[2];
            Long  nombreArrete  = ((Number) row[3]).longValue();
            Long nombreJuge = ((Number) row[4]).longValue();
            Long nombreAutre = ((Number) row[5]).longValue();
           
            StatistiqueTribunalDTO dto = new StatistiqueTribunalDTO(
            		gouvernorat, tribunal, 
            		nombreArrete, nombreJuge, 
            		nombreAutre 
            );
            statistiques.add(dto);
        }

        return statistiques;
    }
}

 

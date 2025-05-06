package com.cgpr.mineur.repository.rapport;

import org.springframework.stereotype.Repository;

import com.cgpr.mineur.config.DetenuStatutDTO;
import com.cgpr.mineur.config.SQLLoader;
import com.cgpr.mineur.dto.StatTypeTribunalEtalissementDTO;
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
public class StatTypeTribunalEtablissementRepository  {

    @PersistenceContext
    private EntityManager entityManager;

    public List<StatTypeTribunalEtalissementDTO> findStatistics( LocalDate dateFin) throws IOException {
        // Charger la requête SQL depuis le fichier
        String sql = SQLLoader.loadSQL("sql/get_statistics_type_tribunal_etablissement.sql");

        // Créer une requête native
        Query query = entityManager.createNativeQuery(sql);

        // Passer les paramètres à la requête
   
        query.setParameter("dateFin", dateFin);

        // Exécuter la requête et récupérer les résultats sous forme d'une liste d'Object[]
        List<Object[]> results = query.getResultList();

        List<StatTypeTribunalEtalissementDTO> statistiques = new ArrayList<>();

        // Mapper les résultats dans des objets DTO
        for (Object[] row : results) {
            String typeTribunal     = (String) row[0];
            
           
            
              Long sommeMourouj = ((Number) row[1]).longValue();
              Long sommeSidiHeni= ((Number) row[2]).longValue();
              Long sommeSoukjdid= ((Number) row[3]).longValue();
              Long sommeMghira= ((Number) row[4]).longValue();
              Long sommeMjazbeb= ((Number) row[5]).longValue();
              Long sommeTypeTribunal= ((Number) row[6]).longValue();
            
            
            StatTypeTribunalEtalissementDTO dto = new StatTypeTribunalEtalissementDTO(
            		typeTribunal, 
            		
            		    sommeMourouj ,  
                     sommeSidiHeni  ,
                     sommeSoukjdid ,
                     sommeMghira ,
                     sommeMjazbeb  ,
                     sommeTypeTribunal
            );
            statistiques.add(dto);
        }

        return statistiques;
    }
}

 

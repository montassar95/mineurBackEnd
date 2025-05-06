package com.cgpr.mineur.repository.rapport;

import org.springframework.stereotype.Repository;

import com.cgpr.mineur.config.DetenuStatutDTO;
import com.cgpr.mineur.config.SQLLoader;
import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;
import com.cgpr.mineur.dto.StatistiqueNationaliteDTO;
import com.cgpr.mineur.dto.StatistiqueTypeAffaireDTO;

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
public class StatisticsTypeAffaireRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public  List<StatistiqueTypeAffaireDTO> findStatisticTypeAffaires( LocalDate dateFin  ) throws IOException {
        // Charger la requête SQL depuis le fichier
//    	src/main/resources/
        String sql = SQLLoader.loadSQL("sql/get_statistics_type_affaire.sql");

        // Créer une requête native
        Query query = entityManager.createNativeQuery(sql);

        // Passer les paramètres à la requête
       
        query.setParameter("dateFin", dateFin);
       
       
        // Exécuter la requête et récupérer les résultats sous forme d'une liste d'Object[]
        List<Object[]> results = query.getResultList();
       
        List<StatistiqueTypeAffaireDTO> statistiques = new ArrayList<>();
        
        // Transformer les résultats en objets DTO
        for (Object[] row : results) {
        	
        	 
        	    
        	       String typeAffaire=(String) row[0];
        	       long arrete= ((Number) row[1]).longValue();
        	       long juge= ((Number) row[2]).longValue();
        	       long maxStatut = (row[3] != null) ? ((Number) row[3]).longValue() : null; // Vérification de null
            
            statistiques.add(new StatistiqueTypeAffaireDTO(typeAffaire, arrete, juge,maxStatut));
        };
        return statistiques;
    }
}

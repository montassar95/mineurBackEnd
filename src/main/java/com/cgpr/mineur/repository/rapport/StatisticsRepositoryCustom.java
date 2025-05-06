package com.cgpr.mineur.repository.rapport;

import org.springframework.stereotype.Repository;

import com.cgpr.mineur.config.DetenuStatutDTO;
import com.cgpr.mineur.config.SQLLoader;
import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;

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
public class StatisticsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public  List<StatistiqueEtablissementDTO> findStatistics(LocalDate dateDebut, LocalDate dateFin  ) throws IOException {
        // Charger la requête SQL depuis le fichier
//    	src/main/resources/
        String sql = SQLLoader.loadSQL("sql/get_statistics_statut.sql");

        // Créer une requête native
        Query query = entityManager.createNativeQuery(sql);

        // Passer les paramètres à la requête
      //  query.setParameter("dateDebut", dateDebut);
        query.setParameter("dateFin", dateFin);
       
       
        // Exécuter la requête et récupérer les résultats sous forme d'une liste d'Object[]
        List<Object[]> results = query.getResultList();
       
        List<StatistiqueEtablissementDTO> statistiques = new ArrayList<>();
 
        for (Object[] row : results) {
     
            String etablissementId = (String) row[0];
            
            String libelle_etablissement = (String) row[1];
            String situationJudiciaire = (String) row[2];
            Long count = ((Number) row[3]).longValue();
            String ids_enfants = (String) row[4];
            System.out.println(row[4]);
            StatistiqueEtablissementDTO dto = statistiques.stream()
                .filter(s -> s.getId().equals(etablissementId))
                .findFirst()
                .orElseGet(() -> {
                    StatistiqueEtablissementDTO newDto = new StatistiqueEtablissementDTO(etablissementId, libelle_etablissement, 0L , 0L, 0L, 0L, "");
                    statistiques.add(newDto);
                    return newDto;
                });

            switch (situationJudiciaire) {
                case "juge":
                    dto.setNbrStatutPenalJuge(dto.getNbrStatutPenalJuge() + count);
                    break;
                case "arrete":
                    dto.setNbrStatutPenalArrete(dto.getNbrStatutPenalArrete() + count);
                    break;
                case "Libre":
                    dto.setNbrStatutPenalLibre(dto.getNbrStatutPenalLibre() + count);
                    break;
                
               
                default:
                	dto.setNbrStatutProbleme(dto.getNbrStatutProbleme() + count);
                	dto.setIdsEnfants(ids_enfants);
                   // throw new IllegalArgumentException("Situation judiciaire inconnue : " + situationJudiciaire);
            }
        }

        return statistiques;
    }
}

package com.cgpr.mineur.repository.rapport;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cgpr.mineur.config.SQLLoader;
import com.cgpr.mineur.dto.StatTypeTribunalPeriodeResidenceDTO;
import com.cgpr.mineur.dto.StatTypeTribunalPeriodeResidenceGenericDTO;
@Repository
public class StatTypeTribunalPeriodeResidenceGenericRepository  {

    @PersistenceContext
    private EntityManager entityManager;

    public List<StatTypeTribunalPeriodeResidenceGenericDTO> findStatistics( LocalDate dateFin) throws IOException {
        // Charger la requête SQL depuis le fichier
        String sql = SQLLoader.loadSQL("sql/get_statistics_type_tribunal_periode_residence_generic.sql");

        // Créer une requête native
        Query query = entityManager.createNativeQuery(sql);

        // Passer les paramètres à la requête
   
        query.setParameter("dateFin", dateFin);

        // Exécuter la requête et récupérer les résultats sous forme d'une liste d'Object[]
        List<Object[]> results = query.getResultList();

        List<StatTypeTribunalPeriodeResidenceGenericDTO> statistiques = new ArrayList<>();

        // Mapper les résultats dans des objets DTO
        for (Object[] row : results) {
            String typeTribunal     = (String) row[1];
            
            String libelleEtablissement = (String) row[2];
              Long moins_3_mois = ((Number) row[3]).longValue();
              Long entre_3_6_mois = ((Number) row[4]).longValue();
              Long entre_6_9_mois = ((Number) row[5]).longValue(); 
              Long entre_9_12_mois = ((Number) row[6]).longValue();
              Long entre_12_15_mois = ((Number) row[7]).longValue();
              Long entre_15_18_mois = ((Number) row[8]).longValue();
              Long plus_18_mois = ((Number) row[9]).longValue();
            
            Long totalTypeTribunal = ((Number) row[10]).longValue();
            
              
            
            
            StatTypeTribunalPeriodeResidenceGenericDTO dto = new StatTypeTribunalPeriodeResidenceGenericDTO(
            		typeTribunal, 
            		libelleEtablissement,
            		moins_3_mois ,  
            		entre_3_6_mois  ,
            		entre_6_9_mois ,
            		entre_9_12_mois ,
            		entre_12_15_mois  ,
            		entre_15_18_mois,
            		plus_18_mois,
            		totalTypeTribunal
            );
            statistiques.add(dto);
        }

        return statistiques;
    }
}

 

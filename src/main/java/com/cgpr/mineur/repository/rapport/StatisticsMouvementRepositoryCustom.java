package com.cgpr.mineur.repository.rapport;

import org.springframework.stereotype.Repository;

import com.cgpr.mineur.config.DetenuStatutDTO;
import com.cgpr.mineur.config.SQLLoader;
import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;
import com.cgpr.mineur.dto.StatistiqueMouvementDTO;

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
public class StatisticsMouvementRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public List<StatistiqueMouvementDTO> findStatistics(LocalDate dateDebut, LocalDate dateFin) throws IOException {
        // Charger la requête SQL depuis le fichier
        String sql = SQLLoader.loadSQL("sql/get_statistics_mouvements.sql");

        // Créer une requête native
        Query query = entityManager.createNativeQuery(sql);

        // Passer les paramètres à la requête
        query.setParameter("dateDebut", dateDebut);
        query.setParameter("dateFin", dateFin);

        // Exécuter la requête et récupérer les résultats sous forme d'une liste d'Object[]
        List<Object[]> results = query.getResultList();

        List<StatistiqueMouvementDTO> statistiques = new ArrayList<>();

        // Mapper les résultats dans des objets DTO
        for (Object[] row : results) {
            String libelleEtablissement = (String) row[0];
            Long nombrePremiereEntree = ((Number) row[1]).longValue();
            Long nombreMutationEntrant = ((Number) row[2]).longValue();
            Long nombreMutationSortant = ((Number) row[3]).longValue();
            Long nombreLiberation = ((Number) row[4]).longValue();
            Long nombreChangementEtablissement = ((Number) row[5]).longValue();
            StatistiqueMouvementDTO dto = new StatistiqueMouvementDTO(
                libelleEtablissement, nombrePremiereEntree, 
                nombreLiberation, nombreMutationEntrant, 
                nombreMutationSortant, nombreChangementEtablissement 
            );
            statistiques.add(dto);
        }

        return statistiques;
    }
}

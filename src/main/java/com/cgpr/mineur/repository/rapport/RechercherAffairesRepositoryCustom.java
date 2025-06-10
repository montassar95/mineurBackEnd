package com.cgpr.mineur.repository.rapport;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cgpr.mineur.config.SQLLoader;
import com.cgpr.mineur.dto.PenalAffaireDTO;
import com.cgpr.mineur.dto.PenalSyntheseDto;
import com.cgpr.mineur.tools.ToolsForReporting2;
import java.math.BigDecimal;
@Repository
public class RechercherAffairesRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
   
    
    public List<PenalAffaireDTO> rechercherAffaires(String tnumide, String tcoddet, int minPage , int maxPage) {
     
        try {
            String sql = SQLLoader.loadSQL("sql/recherche_affaires.sql");
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("tnumide", tnumide);
            query.setParameter("tcoddet", tcoddet);
            
            query.setParameter("min_page", minPage);
            query.setParameter("max_page", maxPage);
            
            @SuppressWarnings("unchecked")
            List<Object[]> results = query.getResultList();

           
            List<PenalAffaireDTO> dtoList = new ArrayList<>();

        
           
           
            for (Object[] row : results) {
                PenalAffaireDTO dto = new PenalAffaireDTO();
                String tnumseqaffStr = row[0] != null ? row[0].toString() : null;
                dto.setTnumseqaff(tnumseqaffStr);
                dto.setLibelleNature((String) row[1]);
                dto.setLibelleTribunal((String) row[2]);
                dto.setTnumjafFormatte((String) row[3]);
                dto.setAccusationsConcatenees((String) row[4]);
                dto.setEtatAffaire((String) row[5]);
                dto.setTypeMandat((String) row[6]);
                dto.setTdatdep((String) row[7]);

                int a = row[8] != null ? ((Number) row[8]).intValue() : 0;
                int m = row[9] != null ? ((Number) row[9]).intValue() : 0;
                int j = row[10] != null ? ((Number) row[10]).intValue() : 0;

                dto.setTotalAnnees(a);
                dto.setTotalMois(m);
                dto.setTotalJours(j);
                dto.setLibelleJugement((String) row[11]);
                dto.setNumeroEcrou((String) row[12]);
                dto.setDateDebutMin((String) row[13]);
                dto.setDateFinMax((String) row[14]);
                dto.setTypeDocument((String) row[15]);         
                dto.setTcodsit((String) row[16]);
                dto.setTypeJugement((String) row[17]);
                dto.setNatureJugement((String) row[18]);
                dto.setNatureTribunal((String) row[19]);
                dto.setTypeAffaire((String) row[20]);
                dto.setRowAffairePrincipale(((BigDecimal) row[21]).intValue());
                dto.setRowOrder(((BigDecimal) row[22]).intValue());
                
               
//             
//                

//                if ((a + m + j) == 0 || 
//                	    "1".equals(dto.getTypeDocument()) || 
//                	    "2".equals(dto.getTypeDocument()) 
//                	    || 
//                	    "3".equals(dto.getTypeDocument())) {
//                	    
//                	    dto.setTextJugement("موقوف");
                	   // dto.setLibelleJugement("---");  

//                	} else {
                	    dto.setTextJugement(ToolsForReporting2.generateLegalCaseString(a, m, j));
//                	}

                
                if (
                	    // Exclure les cas où tcodsit est 'F', 'T', ou 'L'
                	    dto.getTcodsit() != null &&
                	    !"F".equals(dto.getTcodsit()) &&
                	    !"T".equals(dto.getTcodsit()) &&
                	    !"L".equals(dto.getTcodsit()) && (

                	        // Cas 1 : tcodsit = 'O' et typeDocument est '1', '2', ou 'T'
                	        ("O".equals(dto.getTcodsit()) &&
                	         ("1".equals(dto.getTypeDocument()) ||
                	          "2".equals(dto.getTypeDocument()) ||
                	          "T".equalsIgnoreCase(dto.getTypeDocument()))
                	        )

                	        ||

                	        // Cas 2 : typeDocument = '3' et (tcodsit = 'ط' ou 'ن' ou typeJugement = '0')
                	        ("3".equals(dto.getTypeDocument()) &&
                	         ("ط".equals(dto.getTcodsit()) ||
                	          "ن".equals(dto.getTcodsit()) ||
                	          "0".equals(dto.getTypeJugement()))
                	        )
                	    )
                	) {
                    
                    
                    
                   
                }
               
                dtoList.add(dto);
            }

             
           
           
          
          
            return dtoList;

        } catch (IOException e) {
            throw new RuntimeException("Erreur de chargement du fichier SQL", e);
        }
    }

    
    public PenalSyntheseDto rechercherPenalSyntheseDetenu(String tnumide, String tcoddet) {
        try {
            // --- 1. Charger les données principales de synthèse ---
            String sql1 = SQLLoader.loadSQL("sql/penalSyntheseDetenu.sql");
            Query query1 = entityManager.createNativeQuery(sql1);
            query1.setParameter("tnumide", tnumide);
            query1.setParameter("tcoddet", tcoddet);

            @SuppressWarnings("unchecked")
            List<Object[]> results1 = query1.getResultList();

            PenalSyntheseDto penalSyntheseDto = new PenalSyntheseDto();

            for (Object[] row : results1) {
                String tnumseqaffStr = row[0] != null ? row[0].toString() : null;
                penalSyntheseDto.setTnumseqaff(tnumseqaffStr);
                penalSyntheseDto.setTribunal((String) row[1]);
                penalSyntheseDto.setNumAffaire((String) row[2]);
                penalSyntheseDto.setAccusation((String) row[3]);
                penalSyntheseDto.setDateJugement((String) row[4]);
                penalSyntheseDto.setTypeAffaire((String) row[5]);
            }

            // --- 2. Charger le total des affaires ---
            String sql2 = SQLLoader.loadSQL("sql/get_total_affaire.sql");
            Query query2 = entityManager.createNativeQuery(sql2);
            query2.setParameter("tnumide", tnumide);
            query2.setParameter("tcoddet", tcoddet);

            Object result = query2.getSingleResult();
            if (result != null) {
                int totalCount = ((BigDecimal) result).intValue();
                penalSyntheseDto.setTotalCount(totalCount);
                System.err.println("Total count: " + totalCount);
            }

            return penalSyntheseDto;

        } catch (IOException e) {
            throw new RuntimeException("Erreur de chargement du fichier SQL", e);
        }
    }

    
    
  
    
}

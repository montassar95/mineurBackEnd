package com.cgpr.mineur.repository;

import static com.cgpr.mineur.tools.ToolsForReporting2.generateTimeUnitString;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.dto.AccusationExtraitJugementDTO;
import com.cgpr.mineur.dto.ActeJudiciaire;
import com.cgpr.mineur.dto.AffairePenaleDto;
import com.cgpr.mineur.dto.ArretExecutionPenalDTO;
import com.cgpr.mineur.dto.PenalContestationDto;
import com.cgpr.mineur.dto.PenalJugementDTO;
import com.cgpr.mineur.dto.PenalMandatDepotDTO;
import com.cgpr.mineur.dto.PenalTransfertDto;
import com.cgpr.mineur.dto.PenaleDetentionInfoDto;
import com.cgpr.mineur.dto.PrisonerPenaleDto;
import com.cgpr.mineur.dto.SearchDetenuDto;


//---------------------------------------------------------
//SELECT  simp.TNUMIDE as prisoner_id, simp.TPNOMSA as firstname ,  simp.TPPERSA as father_name, simp.TNOMSA as lastname,  simp.TDATN as birth_date, simp.TCODSEX as sex 
//FROM TIDENSIMP@DBLINKMINEURPROD simp
//WHERE (simp.TPNOMSA LIKE '%' || 'سف' || '%' OR 'سف' IS NULL)
//AND (simp.TNOMSA LIKE '%' || 'عل' || '%' OR 'عل' IS NULL)
//AND (simp.TPPERSA LIKE '%' || 'محمد' || '%' OR 'محمد' IS NULL)
//AND (simp.TDATN = TO_DATE('2007-12-03', 'YYYY-MM-DD') OR '2007-12-03' IS NULL)
//AND (simp.TCODSEX = '1' OR '1' IS NULL);
//
//------------------------------------------------------------------------------


@Repository
public class PrisonerPenalRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    
    public SearchDetenuDto trouverDetenusParPrisonerIdDansPrisons(String prisonerId) {
        String sql = "SELECT " + 
                "    iden.TNUMIDE AS prisoner_id, " + 
                "    iden.TPNOMA AS firstname, " + 
                "    iden.TPPERA AS father_name, " + 
                "    iden.TPGPERA AS grandfather_name, " + 
                "    iden.TNOMA AS lastname, " + 
                "    iden.TDATN AS birth_date, " + 
                "    iden.TPMER AS mother_name, " + 
                "    iden.TNOMMER AS maternal_grandmother_name, " + 
                "    simp.TCODSEX AS sex, " + 
                "    GETLIBELLE_GOUVERNORAT@DBLINKMINEURPROD(iden.TCODGOUN) AS place_of_birth, " + 
                "    res.TTYPRES || '  ' || res.TCODRES || '  ' || res.TANNRES AS numero_ecrou, " + 
                "    TO_CHAR(res.TDATDR, 'YYYY-MM-DD') AS date_entre, " + 
                "    GETLIBELLEPRISON@DBLINKMINEURPROD(res.TCODGOU, res.TCODPR) AS prision, " + 
                "    res.TCODDET AS numroDetention " + 
                "FROM TIDENSIMP@DBLINKMINEURPROD simp " + 
                "JOIN TIDENTITE@DBLINKMINEURPROD iden ON simp.TNUMIDE = iden.TNUMIDE " + 
                "JOIN TRESIDENCE@DBLINKMINEURPROD res ON iden.TNUMIDE = res.TNUMIDE " + 
                "WHERE res.TCODDET = (SELECT MAX(r2.TCODDET) FROM TRESIDENCE@DBLINKMINEURPROD r2 WHERE r2.TNUMIDE = res.TNUMIDE) " +
                "AND res.TDATDR = (SELECT MAX(r2.TDATDR) FROM TRESIDENCE@DBLINKMINEURPROD r2 WHERE r2.TNUMIDE = iden.TNUMIDE AND r2.TCODDET = res.TCODDET) " + 
                "AND iden.TNUMIDE = ? " +
                "ORDER BY " + 
                "        CASE WHEN res.TDATFR IS NULL THEN 1 ELSE 0 END DESC,   " + 
                "        res.TDATFR DESC    " + 
                "          " + 
                "    FETCH FIRST 1 ROWS ONLY";  
      //priorité aux résidences en cours 
        // ensuite les résidences terminées les plus récentes 
        try {
            return jdbcTemplate.queryForObject(
                sql,
                new Object[]{prisonerId},
                (rs, rowNum) -> {
                    SearchDetenuDto prisoner = new SearchDetenuDto();
                    prisoner.setDetenuId(rs.getString("prisoner_id"));
                    prisoner.setNom(rs.getString("firstname"));
                    prisoner.setPrenom(rs.getString("lastname"));
                    prisoner.setNomPere(rs.getString("father_name"));
                    prisoner.setNomGrandPere(rs.getString("grandfather_name"));
                    prisoner.setNomMere(rs.getString("mother_name"));
                    prisoner.setPrenomMere(rs.getString("maternal_grandmother_name"));
                    prisoner.setDateNaissance(rs.getDate("birth_date") != null ? rs.getDate("birth_date").toLocalDate() : null);
                    prisoner.setSexe(rs.getString("sex"));
                    prisoner.setLieuNaissance(rs.getString("place_of_birth"));
                    prisoner.setNumeroEcrou(rs.getString("numero_ecrou"));
                    prisoner.setDateEntree(rs.getString("date_entre"));
                    prisoner.setNomEtablissement(rs.getString("prision"));
                    return prisoner;
                }
            );
        } catch (EmptyResultDataAccessException e) {
            return null; // Aucun résultat trouvé
        }
    }

    public List<SearchDetenuDto> findPrisonerPenalByCriteria(String nom, String prenom, String nomPere, LocalDate dateDeNaissance, String sexe) {
        // Construction de la requête SQL avec jointure
    	 System.out.println("----------------- Recherche simple ------------------------------");
    	String sql = " SELECT " + 
                "    iden.TNUMIDE AS prisoner_id, " + 
                "    iden.TPNOMA AS firstname, " + 
                "    iden.TPPERA AS father_name, " + 
                "    iden.TPGPERA AS grandfather_name, " + 
                "    iden.TNOMA AS lastname, " + 
                "    iden.TDATN AS birth_date, " + 
                "    iden.TPMER AS mother_name, " + 
                "    iden.TNOMMER AS maternal_grandmother_name, " + 
                "    simp.TCODSEX AS sex, " + 
                "    GETLIBELLE_GOUVERNORAT@DBLINKMINEURPROD(iden.TCODGOUN) AS place_of_birth, " + 
//                "    GETLIBELLE_DELEGATION@DBLINKMINEURPROD(iden.TCODGOUN, iden.TCODDELN) || ' - ' || GETLIBELLE_GOUVERNORAT@DBLINKMINEURPROD(iden.TCODGOUN) AS place_of_birth, " + 
                "    res.TTYPRES || '  ' || res.TCODRES || '  ' || res.TANNRES AS numero_ecrou, " + 
                "    TO_CHAR(res.TDATDR, 'YYYY-MM-DD') AS date_entre, " + 
                "    GETLIBELLEPRISON@DBLINKMINEURPROD(res.TCODGOU, res.TCODPR) AS prision, " + 
                "    res.TCODDET AS numroDetention " + 
                "FROM " + 
                "    TIDENSIMP@DBLINKMINEURPROD simp " + 
                "JOIN " + 
                "    TIDENTITE@DBLINKMINEURPROD iden " + 
                "    ON simp.TNUMIDE = iden.TNUMIDE " + 
                "JOIN " + 
                "    TRESIDENCE@DBLINKMINEURPROD res " + 
                "    ON iden.TNUMIDE = res.TNUMIDE " + 
                "WHERE " + 
                "    res.TCODDET = (" + 
                "        SELECT MAX(r2.TCODDET) " + 
                "        FROM TRESIDENCE@DBLINKMINEURPROD r2 " + 
                "        WHERE r2.TNUMIDE = res.TNUMIDE " + 
                "    ) " +
                "AND res.TDATDR = (" + 
                "        SELECT MAX(r2.TDATDR) " + 
                "        FROM TRESIDENCE@DBLINKMINEURPROD r2 " + 
                "        WHERE r2.TNUMIDE = iden.TNUMIDE " + 
                "          AND r2.TCODDET = res.TCODDET " + 
                "    ) " + 
                "AND (? IS NULL OR simp.TPNOMSA = ? ) " +
                "AND (? IS NULL OR simp.TNOMSA = ? ) " +
                "AND (? IS NULL OR simp.TPPERSA =  ? ) " +
                
                
               // "AND (? IS NULL OR simp.TPPERSA =  ? ) " +
      //      "AND (? IS NULL OR simp.TDATN = ?) " +
                "AND ( " + 
                "  ? IS NULL " + 
                "  OR ( " + 
                "    ( " + 
                "      TO_CHAR(simp.TDATN, 'MM-DD') = TO_CHAR(?, 'MM-DD') " + 
                "      OR TO_CHAR(simp.TDATN, 'MM-DD') = TO_CHAR(?, 'DD-MM') " + 
                "    ) " + 
                "    AND ABS(TO_NUMBER(TO_CHAR(simp.TDATN, 'YYYY')) - TO_NUMBER(TO_CHAR(?, 'YYYY'))) <= 1 " + 
                "  ) " + 
                ") " + 
                 
              
                "AND (? IS NULL OR simp.TCODSEX = ?) " +
               // "ORDER BY date_entre DESC";  
               "ORDER BY " + 
               "        CASE WHEN res.TDATFR IS NULL THEN 1 ELSE 0 END DESC,   " + 
               "        res.TDATFR DESC    " ; 
//               "          " + 
//               +"    FETCH FIRST 1 ROWS ONLY";  
     //priorité aux résidences en cours 
       // ensuite les résidences terminées les plus récentes 
//    	
//    	 "AND (? IS NULL OR simp.TPNOMSA LIKE '%' || ? || '%') " +
//         "AND (? IS NULL OR simp.TNOMSA LIKE '%' || ? || '%') " +
//         "AND (? IS NULL OR simp.TPPERSA LIKE '%' || ? || '%') " +
//         "AND (? IS NULL OR simp.TDATN = ?) " +
//         "AND (? IS NULL OR simp.TCODSEX = ?)";


        // Exécution de la requête
         List<SearchDetenuDto> detenuTrouve =  jdbcTemplate.query(
            sql,
            new Object[]{nom, nom, prenom, prenom, nomPere, nomPere, dateDeNaissance, dateDeNaissance, dateDeNaissance, dateDeNaissance, sexe, sexe},
            (rs, rowNum) -> {
                // Mapping du résultat de la requête vers un objet SearchDetenuDto
                SearchDetenuDto prisoner = new SearchDetenuDto();
                prisoner.setDetenuId(rs.getString("prisoner_id"));
                prisoner.setNom(rs.getString("firstname"));
                prisoner.setPrenom(rs.getString("lastname"));
                prisoner.setNomPere(rs.getString("father_name"));
                prisoner.setNomGrandPere(rs.getString("grandfather_name"));
                prisoner.setNomMere(rs.getString("mother_name"));
                prisoner.setPrenomMere(rs.getString("maternal_grandmother_name"));
                prisoner.setDateNaissance(rs.getDate("birth_date").toLocalDate());  // Conversion de Date SQL en LocalDate
                prisoner.setSexe(rs.getString("sex"));
                prisoner.setLieuNaissance(rs.getString("place_of_birth"));
                prisoner.setNumeroEcrou(rs.getString("numero_ecrou"));
                prisoner.setDateEntree(rs.getString("date_entre"));
                prisoner.setNomEtablissement(rs.getString("prision"));
                return prisoner;
            }
        );
//         && dateDeNaissance != null && sexe != null
         if (detenuTrouve.isEmpty() ) {
        	 System.out.println("----------------- Recherche details ------------------------------");
             String sqlByDateAndSex = sql.replace(
            		  "AND (? IS NULL OR simp.TPNOMSA = ? ) " +
            	                "AND (? IS NULL OR simp.TNOMSA = ? ) " +
            	                "AND (? IS NULL OR simp.TPPERSA =  ? ) " , 
            	                
            	                
            	                        "AND (  " + 
            	                        "   ( (? IS NULL OR simp.TPNOMSA LIKE  ? )    " + 
            	                        "    AND (? IS NULL OR simp.TNOMSA LIKE  ? ) ) " + 
            	                        "    OR ( (? IS NULL OR simp.TNOMSA LIKE  ? )    " + 
            	                        "    AND (? IS NULL OR simp.TPNOMSA LIKE  ? ) ) " + 
            	                        ") "  
             );

             detenuTrouve =  jdbcTemplate.query(
            		 sqlByDateAndSex,
                  //   new Object[]{ nom, nom, prenom, prenom, nom, nom, prenom, prenom , dateDeNaissance, dateDeNaissance, sexe, sexe},
            		 new Object[] {
            				    nom == null ? null : "%" + nom + "%",
            				    nom == null ? null : "%" + nom + "%",
            				    prenom == null ? null : "%" + prenom + "%",
            				    prenom == null ? null : "%" + prenom + "%",
            				    nom == null ? null : "%" + nom + "%",
            		            nom == null ? null : "%" + nom + "%" , 		
            				    prenom == null ? null : "%" + prenom + "%",
            				    prenom == null ? null : "%" + prenom + "%",
            				  
            				    dateDeNaissance == null ? null : dateDeNaissance ,
            				    dateDeNaissance == null ? null : dateDeNaissance,
            				    dateDeNaissance == null ? null : dateDeNaissance,
            				    dateDeNaissance == null ? null : dateDeNaissance,
            				    sexe == null ? null : sexe,
            				    sexe == null ? null : sexe
            				},
                     (rs, rowNum) -> {
                         // Mapping du résultat de la requête vers un objet SearchDetenuDto
                         SearchDetenuDto prisoner = new SearchDetenuDto();
                         prisoner.setDetenuId(rs.getString("prisoner_id"));
                         prisoner.setNom(rs.getString("firstname"));
                         prisoner.setPrenom(rs.getString("lastname"));
                         prisoner.setNomPere(rs.getString("father_name"));
                         prisoner.setNomGrandPere(rs.getString("grandfather_name"));
                         prisoner.setNomMere(rs.getString("mother_name"));
                         prisoner.setPrenomMere(rs.getString("maternal_grandmother_name"));
                         prisoner.setDateNaissance(rs.getDate("birth_date").toLocalDate());  // Conversion de Date SQL en LocalDate
                         prisoner.setSexe(rs.getString("sex"));
                         prisoner.setLieuNaissance(rs.getString("place_of_birth"));
                         prisoner.setNumeroEcrou(rs.getString("numero_ecrou"));
                         prisoner.setDateEntree(rs.getString("date_entre"));
                         prisoner.setNomEtablissement(rs.getString("prision"));
                         return prisoner;
                     }
                 );
         }
         
         return detenuTrouve;
    }
    
    public List<SearchDetenuDto> trouverDetenusParNumeroEcrouDansPrisons(String numArr ) {
        // Construction de la requête SQL avec jointure
    	String sql = " SELECT " + 
                "    iden.TNUMIDE AS prisoner_id, " + 
                "    iden.TPNOMA AS firstname, " + 
                "    iden.TPPERA AS father_name, " + 
                "    iden.TPGPERA AS grandfather_name, " + 
                "    iden.TNOMA AS lastname, " + 
                "    iden.TDATN AS birth_date, " + 
                "    iden.TPMER AS mother_name, " + 
                "    iden.TNOMMER AS maternal_grandmother_name, " + 
                "    simp.TCODSEX AS sex, " + 
                "    GETLIBELLE_GOUVERNORAT@DBLINKMINEURPROD(iden.TCODGOUN) AS place_of_birth, " + 
                "    res.TTYPRES || '  ' || res.TCODRES || '  ' || res.TANNRES AS numero_ecrou, " + 
                "    TO_CHAR(res.TDATDR, 'YYYY-MM-DD') AS date_entre, " + 
                "    GETLIBELLEPRISON@DBLINKMINEURPROD(res.TCODGOU, res.TCODPR) AS prision, " + 
                "    res.TCODDET AS numroDetention " + 
                "FROM " + 
                "    TIDENSIMP@DBLINKMINEURPROD simp " + 
                "JOIN " + 
                "    TIDENTITE@DBLINKMINEURPROD iden " + 
                "    ON simp.TNUMIDE = iden.TNUMIDE " + 
                "JOIN " + 
                "    TRESIDENCE@DBLINKMINEURPROD res " + 
                "    ON iden.TNUMIDE = res.TNUMIDE " + 
                "WHERE " + 
               
                "  res.TCODRES = ?    "  + 
              
 
                "ORDER BY date_entre DESC";  
    	
 


        // Exécution de la requête
        return jdbcTemplate.query(
            sql,
            new Object[]{numArr},
            (rs, rowNum) -> {
                // Mapping du résultat de la requête vers un objet SearchDetenuDto
                SearchDetenuDto prisoner = new SearchDetenuDto();
                prisoner.setDetenuId(rs.getString("prisoner_id"));
                prisoner.setNom(rs.getString("firstname"));
                prisoner.setPrenom(rs.getString("lastname"));
                prisoner.setNomPere(rs.getString("father_name"));
                prisoner.setNomGrandPere(rs.getString("grandfather_name"));
                prisoner.setNomMere(rs.getString("mother_name"));
                prisoner.setPrenomMere(rs.getString("maternal_grandmother_name"));
                prisoner.setDateNaissance(rs.getDate("birth_date").toLocalDate());  // Conversion de Date SQL en LocalDate
                prisoner.setSexe(rs.getString("sex"));
                prisoner.setLieuNaissance(rs.getString("place_of_birth"));
                prisoner.setNumeroEcrou(rs.getString("numero_ecrou"));
                prisoner.setDateEntree(rs.getString("date_entre"));
                prisoner.setNomEtablissement(rs.getString("prision"));
                return prisoner;
            }
        );
    }
    
    public PrisonerPenaleDto findPrisonerPenalByPrisonerId(String prisonerId) {
    	// Construction de la requête SQL avec jointure
    	
//    	String sql = 
//    	    "    SELECT  " + 
//    	    "        iden.TNUMIDE AS prisoner_id,  " + 
//    	    "        iden.TPNOMA AS firstname,  " + 
//    	    "        iden.TPPERA AS father_name,  " + 
//    	    "        iden.TPGPERA AS grandfather_name,  " + 
//    	    "        iden.TNOMA AS lastname,  " + 
//    	    "        iden.TDATN AS birth_date,  " + 
//    	    "        iden.TPMER AS mother_name,  " + 
//    	    "        iden.TNOMMER AS maternal_grandmother_name,  " + 
//    	    "        iden.TCODSEX AS sex,  " + 
//    	    "        iden.tadr AS adresse,  " + 
//    	    "        GETLIBELLE_GOUVERNORAT@DBLINKMINEURPROD(iden.TCODGOUN) AS place_of_birth,  " + 
//    	    "        res.TTYPRES || '  ' || res.TCODRES || '  ' || res.TANNRES AS numero_ecrou,  " + 
//    	    "        TO_CHAR(res.TDATDR, 'YYYY-MM-DD') AS date_entre,  " + 
//    	    "        GETLIBELLEPRISON@DBLINKMINEURPROD(res.TCODGOU, res.TCODPR) AS prision,  " + 
//    	    "        res.TCODDET AS numroDetention,  " + 
//    	    "        ja.TETAT,  " + 
//    	    "        na.LIBELLE_NATURE as natureAffaire,  " + 
//    	    "        ja.TCODTYP,  " + 
//    	    "        ja.TDATDPE AS debutPunition,  " + 
//    	    "        ja.TDATLIB AS finPunition,  " + 
//    	    "        ja.TETAT AS etat,  " + 
//    	    "        ja.TCODTYP AS typeClassementPenal,  " + 
//    	    "        ja.TAGE AS age,   " + 
//    	    "        ja.TDURTPEA as annee,   " + 
//    	    "        ja.TDURTPEM as mois,   " + 
//    	    "        ja.TDURTPEJ as jour,  " + 
//    	    "        ja.TDURARA as anneeArret,  " + 
//    	    "        ja.TDURARM as moisArret,  " + 
//    	    "        ja.TDURARJ as jourArret ,   " + 
//    	    "        ja.TCONDANNE as condanne,     " + 
//    	    "        contestGlobale.TDATCO AS dateContestation,  " + 
//    	    "        contest.TLIBTCO AS typeContestation   " + 
//    	    "    FROM  " + 
//    	    "        TIDENTITE@DBLINKMINEURPROD iden  " + 
//    	    "    LEFT JOIN  " + 
//    	    "        TRESIDENCE@DBLINKMINEURPROD res  " + 
//    	    "        ON iden.TNUMIDE = res.TNUMIDE  " + 
//    	    "    LEFT JOIN  " + 
//    	    "        tjugearret@DBLINKMINEURPROD ja  " + 
//    	    "        ON iden.TNUMIDE = ja.TNUMIDE  " + 
//    	    "    LEFT JOIN  " + 
//    	    "        natureaffaire@DBLINKMINEURPROD na  " + 
//    	    "        ON ja.TCODTAF = na.CODE_NATURE  " + 
//    	    "    LEFT JOIN  " + 
//    	    "        tcontestation@DBLINKMINEURPROD contestGlobale  " + 
//    	    "        ON res.TNUMIDE = contestGlobale.TNUMIDE AND res.TCODDET = contestGlobale.TCODDET " + 
//    	    "    LEFT JOIN  " + 
//    	    "        TTYPECONTESTATION@DBLINKMINEURPROD contest  " + 
//    	    "        ON contestGlobale.tcodtco = contest.TCODTCO  " + 
//    	    "    WHERE  " + 
//    	    "        res.tclores = 'O' AND  " + 
//    	    "        res.TCODDET = (  " + 
//    	    "            SELECT MAX(r2.TCODDET)  " + 
//    	    "            FROM TRESIDENCE@DBLINKMINEURPROD r2  " + 
//    	    "            WHERE r2.TNUMIDE = res.TNUMIDE  " + 
//    	    "        ) AND  " + 
//    	    "        res.TDATDR = (  " + 
//    	    "            SELECT MAX(r2.TDATDR)  " + 
//    	    "            FROM TRESIDENCE@DBLINKMINEURPROD r2  " + 
//    	    "            WHERE r2.TNUMIDE = iden.TNUMIDE  " + 
//    	    "              AND r2.TCODDET = res.TCODDET  " + 
//    	    "        ) AND  " + 
//    	    "        iden.TNUMIDE = ? " + 
//    	    "    ORDER BY date_entre DESC ";

 
        String sql = "WITH Latest_Contest AS ( " + 
        		"    SELECT c1.* " + 
        		"    FROM tcontestation@DBLINKMINEURPROD c1 " + 
        		"    WHERE c1.TDATCO = ( " + 
        		"        SELECT MAX(c2.TDATCO)  " + 
        		"        FROM tcontestation@DBLINKMINEURPROD c2 " + 
        		"        WHERE c2.TNUMIDE = c1.TNUMIDE  " + 
        		"        AND c2.TCODDET = c1.TCODDET " + 
        		"    ) " + 
        		"    AND c1.TNUMSEQAFF = ( " + 
        		"        SELECT MAX(c3.TNUMSEQAFF)  " + 
        		"        FROM tcontestation@DBLINKMINEURPROD c3 " + 
        		"        WHERE c3.TNUMIDE = c1.TNUMIDE  " + 
        		"        AND c3.TCODDET = c1.TCODDET " + 
        		"    ) " + 
        		") " + 
        		"SELECT   " + 
        		"    iden.TNUMIDE AS prisoner_id,   " + 
        		 "nationalite.LIBELLE_NATIONALITE AS nationalite ,  "+
        		 "niveauculturel.tlibnc AS niveau_culturel, "+
        		 "profession.LIBELLE_PROFESSION AS profession , "+
        		"    iden.TPNOMA AS firstname,   " + 
        		"    iden.TPPERA AS father_name,   " + 
        		"    iden.TPGPERA AS grandfather_name,   " + 
        		"    iden.TNOMA AS lastname,   " + 
        		"    iden.TDATN AS birth_date,   " + 
        		"    iden.TPMER AS mother_name,   " + 
        		"    iden.TNOMMER AS maternal_grandmother_name,   " + 
        		"    iden.TCODSEX AS sex,   " + 
        		"    iden.tadr AS adresse,   " + 
        		" iden.TNOMPCJ AS partenaire, "+
        	   " iden.TNBRNF AS nombre_enfant, " +
        		"    GETLIBELLE_GOUVERNORAT@DBLINKMINEURPROD(iden.TCODGOUN) AS place_of_birth,   " + 
        		"    res.TTYPRES || '  ' || res.TCODRES || '  ' || res.TANNRES AS numero_ecrou,   " + 
        		"    TO_CHAR(res.TDATDR, 'YYYY-MM-DD') AS date_entre,   " + 
        		"    GETLIBELLEPRISON@DBLINKMINEURPROD(res.TCODGOU, res.TCODPR) AS prision,   " +
        		"     res.TCODGOU ||'' || res.TCODPR||  '' ||  TO_CHAR(res.TDATDR, 'YYYYMMDD') ||'' || res.TCODRES    as code_image, "+
        		"    res.TCODDET AS numroDetention,   " + 
        		"    ja.TETAT,   " + 
        		"    na.LIBELLE_NATURE AS natureAffaire,   " + 
        		"    ja.TCODTYP,   " + 
        		"    ja.TDATDPE AS debutPunition,   " + 
        		"    ja.TDATLIB AS finPunition,   " + 
        		"    ja.TETAT AS etat,   " + 
        		"    ja.TCODTYP AS typeClassementPenal,   " + 
        		"    ja.TAGE AS age,    " + 
        		"  NVL(  ja.TDURTPEA , 0) AS annee ,    " + 
        		"   NVL(  ja.TDURTPEM ,  0) AS mois ,   " + 
        		"   NVL(  ja.TDURTPEJ ,  0) AS jour ,  " + 
        		"    NVL( ja.TDURARA ,  0) AS anneeArret ,  " + 
        		"    NVL( ja.TDURARM ,  0) AS moisArret ,  " + 
        		"   NVL(  ja.TDURARJ ,  0) AS jourArret ,   " + 
        		"    NVL( ja.TCONDANNE ,  0)  AS condanne ,      " + 
        		"    contestGlobale.TDATCO AS dateContestation,   " + 
        		"    contest.TLIBTCO AS typeContestation,    " + 
        		" detention.TDATLIBE AS dateLiberation , " + 
        		"     motifLiberationTab.tlibmot AS motifLiberation   "+
        		"FROM   " + 
        		"    TIDENTITE@DBLINKMINEURPROD iden   " + 
        		"LEFT JOIN   " + 
        		"    TRESIDENCE@DBLINKMINEURPROD res   " + 
        		"    ON iden.TNUMIDE = res.TNUMIDE   " + 
        		" LEFT JOIN "+
        		  "  PROFESSION@DBLINKMINEURPROD profession "+
        		  "  ON iden.CODE_PROFESSION = profession.CODE_PROFESSION   "+
        		 " LEFT JOIN "+
        		 "   niveauculturel@DBLINKMINEURPROD niveauculturel "+
        		 "   ON iden.tcodnc = niveauculturel.tcodnc   "+
        		"LEFT JOIN "+
        	    "NATIONALITE@DBLINKMINEURPROD nationalite "+
        	    "ON iden.CODE_NATIONALITE = nationalite.CODE_NATIONALITE "+
        		"LEFT JOIN   " + 
        		"    tjugearret@DBLINKMINEURPROD ja   " + 
        		"    ON iden.TNUMIDE = ja.TNUMIDE   " + 
        		"LEFT JOIN   " + 
        		"    natureaffaire@DBLINKMINEURPROD na   " + 
        		"    ON ja.TCODTAF = na.CODE_NATURE   " + 
        		"LEFT JOIN   " + 
        		"    Latest_Contest contestGlobale   " + 
        		"    ON res.TNUMIDE = contestGlobale.TNUMIDE   " + 
        		"    AND res.TCODDET = contestGlobale.TCODDET   " + 
        		"LEFT JOIN   " + 
        		"    TTYPECONTESTATION@DBLINKMINEURPROD contest   " + 
        		"    ON contestGlobale.TCODTCO = contest.TCODTCO   " + 
        		"LEFT JOIN   TDETENTION@DBLINKMINEURPROD detention " + 
        		"  ON  detention.tnumide = res.tnumide and detention. tcoddet = res.TCODDET " + 
        	 
        		" LEFT JOIN   tmotif@DBLINKMINEURPROD motifLiberationTab  on motifLiberationTab.tcodmot = detention.tcodmot "+
        		"WHERE   " + 
         
        		"    res.TCODDET = (   " + 
        		"        SELECT MAX(r2.TCODDET)   " + 
        		"        FROM TRESIDENCE@DBLINKMINEURPROD r2   " + 
        		"        WHERE r2.TNUMIDE = res.TNUMIDE   " + 
        		"    ) AND   " + 
        		"    res.TDATDR = (   " + 
        		"        SELECT MAX(r2.TDATDR)   " + 
        		"        FROM TRESIDENCE@DBLINKMINEURPROD r2   " + 
        		"        WHERE r2.TNUMIDE = iden.TNUMIDE   " + 
        		"          AND r2.TCODDET = res.TCODDET   " + 
        		"    ) AND   " + 
        		"    iden.TNUMIDE = ?   " + 
        		 "ORDER BY " + 
                 "        CASE WHEN res.TDATFR IS NULL THEN 1 ELSE 0 END DESC,   " + 
                 "        res.TDATFR DESC    " + 
                 "          " + 
                 "    FETCH FIRST 1 ROWS ONLY";  
 

        try {
            // Exécution de la requête
            return jdbcTemplate.queryForObject(
                sql,
                new Object[]{prisonerId},
                (rs, rowNum) -> {
                    // Mapping du résultat de la requête vers un objet SearchDetenuDto
                	PrisonerPenaleDto prisoner = new PrisonerPenaleDto();
                    prisoner.setDetenuId(rs.getString("prisoner_id"));
                    prisoner.setNationalite(rs.getString("nationalite"));
                    prisoner.setNiveauCulturel(rs.getString("niveau_culturel"));
                    prisoner.setProfession (rs.getString("profession"));
                    prisoner.setNom(rs.getString("firstname"));
                    prisoner.setPrenom(rs.getString("lastname"));
                    prisoner.setNomPere(rs.getString("father_name"));
                    prisoner.setNomGrandPere(rs.getString("grandfather_name"));
                    prisoner.setNomMere(rs.getString("mother_name"));
                    prisoner.setPrenomMere(rs.getString("maternal_grandmother_name"));
                    prisoner.setDateNaissance(rs.getDate("birth_date").toLocalDate()); // Conversion de Date SQL en LocalDate
                    prisoner.setAge(rs.getString("age"));
                    prisoner.setSexe(rs.getString("sex"));
                    prisoner.setLieuNaissance(rs.getString("place_of_birth"));
                    prisoner.setNumeroEcrou(rs.getString("numero_ecrou"));
                    prisoner.setDateEntree(rs.getString("date_entre"));
                    prisoner.setNomEtablissement(rs.getString("prision"));
                    prisoner.setAdresse(rs.getString("adresse"));
                    prisoner.setCodeImage(rs.getString("code_image"));
                    
                    prisoner.setNombreEnfant(rs.getString("nombre_enfant"));
                     
                    if(rs.getString("partenaire") != null) {
                    	 prisoner.setNomPartenaire(rs.getString("partenaire"));
                    }
             	   
                    prisoner.setNumOrdinaleArrestation(rs.getString("numroDetention"));
                    if(rs.getString("debutPunition") != null) {
                    	prisoner.setDebutPunition(rs.getString("debutPunition").substring(0, 10));
                    }
                    
                    
                    
                    if(rs.getString("finPunition") != null) {
                    	prisoner.setFinPunition(rs.getString("finPunition").substring(0, 10));
                    }
                    
                    prisoner.setNatureAffaire(rs.getString("natureAffaire"));
                    
                    
                    if(rs.getString("dateContestation") != null) {
                    	 prisoner.setDateContestation(rs.getString("dateContestation").substring(0, 10) );
                    }
                    if(rs.getString("typeContestation") != null) {
                    	 prisoner.setTypeContestation(rs.getString("typeContestation") );
                    }
                   
                   
                    
                    if(rs.getString("etat") != null) {
                    	 String etat=rs.getString("etat");
                         
                 		if(etat.equals("A") ) {
                 			etat="مــــوقــــوف";
                 		} 
                 		else if(etat.equals("J") ) {
                 			etat="محــــــــكوم ";
                 		} 
                 		else if(etat.equals(null) ) {
                 			etat="ســــــــــراح ";
                 		}
                 		
                 		prisoner.setEtat(etat);
                   }
                    
                   
            		
                    if(rs.getString("typeClassementPenal") != null) {
                    	
                    	 String typeClassementPenal=rs.getString("typeClassementPenal");
                         
                  		if(typeClassementPenal.equals("م") ) {
                  			typeClassementPenal="مبتــدئ";
                  		} 
                  		else if(typeClassementPenal.equals("ع") ) {
                  			typeClassementPenal="عــائــــد";
                  		} 
                  		 
                 		prisoner.setTypeClassementPenal(typeClassementPenal);
                    }
            		
                    String condanne=rs.getString("condanne");
                    if(rs.getString("condanne") != null) {
                    	
                         
                 		if(condanne.equals("1") ) {
                 			condanne="الاعـــــــــدام";
                 		} 
                 		else if(condanne.equals("2") ) {
                 			condanne=" السجــن مـدى الحيـاة ";
                 		} 
                 		 
                 		prisoner.setCondanne(condanne);
                    }
            		
                    if(rs.getString("motifLiberation") != null) {
                    	System.out.println((rs.getString("motifLiberation").toString()+"---" ));
                    	prisoner.setMotifLiberation(rs.getString("motifLiberation"));
                    }
                    
            		 
                    if(rs.getString("dateLiberation") != null) {
                    	prisoner.setDateLiberation(rs.getString("dateLiberation").substring(0, 10));
                    }	
            		StringBuilder result = new StringBuilder();
            		result.append("");
            		if (!rs.getString("annee").equals("0")) {
            			result.append(generateTimeUnitString(Integer.parseInt(rs.getString("annee")) , "عام", "عامين", "أعوام", "عام"));
            			if (Integer.parseInt(rs.getString("mois"))!= 0 || Integer.parseInt(rs.getString("jour")) != 0) {
            				result.append(" و ");
            			}
            		}

            		if (!rs.getString("mois").equals("0")) {
            			result.append(generateTimeUnitString(Integer.parseInt(rs.getString("mois")) , "شهر", "شهرين", "أشهر", "شهر"));
            			if (Integer.parseInt(rs.getString("jour")) != 0) {
            				result.append(" و ");
            			}
            		}

            		if (!rs.getString("jour").equals("0")) {
            			result.append(generateTimeUnitString(Integer.parseInt(rs.getString("jour")) , "يوم", "يومين", "أيام", "يوم"));
            		}
//            		
            		if(result.toString().isEmpty() && !condanne.equals("1") && !condanne.equals("2")) {
            			prisoner.setPunition("لا يوجد");
            		}
            		else {
            			prisoner.setPunition(result.toString());
            		}
            		
            		
            		
            		StringBuilder arretProvisoire = new StringBuilder();
            		result.append("");
            		if (!rs.getString("anneeArret").equals("0")) {
            			arretProvisoire.append(generateTimeUnitString(Integer.parseInt(rs.getString("anneeArret")) , "عام", "عامين", "أعوام", "عام"));
            			if (Integer.parseInt(rs.getString("moisArret"))!= 0 || Integer.parseInt(rs.getString("jourArret")) != 0) {
            				arretProvisoire.append(" و ");
            			}
            		}

            		if (!rs.getString("moisArret").equals("0")) {
            			arretProvisoire.append(generateTimeUnitString(Integer.parseInt(rs.getString("moisArret")) , "شهر", "شهرين", "أشهر", "شهر"));
            			if (Integer.parseInt(rs.getString("jourArret")) != 0) {
            				arretProvisoire.append(" و ");
            			}
            		}

            		if (!rs.getString("jourArret").equals("0")) {
            			arretProvisoire.append(generateTimeUnitString(Integer.parseInt(rs.getString("jourArret")) , "يوم", "يومين", "أيام", "يوم"));
            		}
//            		
            		if(arretProvisoire.toString().isEmpty()) {
            			prisoner.setArretProvisoire("لا يوجد");
            		}
            		else {
            			prisoner.setArretProvisoire(arretProvisoire.toString());
            		}
            		
            		
            		
            		
            		
              		
              		
                    return prisoner;
                }
            );
        } catch (EmptyResultDataAccessException e) {
        	System.out.println(e.getMessage());
            // Si aucun résultat n'est trouvé
            return null;
        } catch (DataAccessException e) {
            // Gestion des autres erreurs d'accès aux données
            throw new RuntimeException("Erreur lors de l'exécution de la requête pour l'ID prisonnier : " + prisonerId, e);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public List<AffairePenaleDto> findAffairesByNumideAndCoddet(String tnumide, String tcoddet) {
 //   	String sql = 
//    		    "SELECT " +
//    		    "  TAFF.tnumseqaff, " +
//    		    "  TR.libelle_tribunal, " +
//    		    "  SUBSTR(TAFF.tnumjaf, 4, 6) || ' - ' || SUBSTR(TAFF.tnumjaf, 1, 3) AS tnumjaf_formatte, " +
//    		    "  CASE " +
//    		    "    WHEN TAFF.TCODSIT = 'F' THEN 'مغـلـقـة' " +
//    		    "    WHEN TAFF.TCODSIT = 'O' THEN 'جـاريـة' " +
//    		    "    WHEN TAFF.TCODSIT = 'T' THEN 'محـالــــة' " +
//    		    "    WHEN TAFF.TCODSIT = 'ط' THEN 'طعــــــن' " +
//    		    "    WHEN TAFF.TCODSIT = 'ن' THEN 'إستئناف النيابة' " +
//    		    "    WHEN TAFF.TCODSIT = 'ت' THEN 'تعقيـــب' " +
//    		    "    WHEN TAFF.TCODSIT = 'L' THEN 'ســـراح' " +
//    		    "    ELSE TAFF.TCODSIT " +
//    		    "  END AS etat_affaire, " +
//    		    "  NA.libelle_nature, " +
//    		    "  TAFF.TTYPRES || ' ' || TAFF.TCODRES || ' ' || TAFF.TANNRES AS numero_ecrou, " +
//    		    "  CASE " +
//    		    "    WHEN TAFF.typema = '1' THEN 'بطاقة إيداع' " +
//    		    "    WHEN TAFF.typema = '2' THEN 'بطاقة جلب' " +
//    		    "    WHEN TAFF.typema = '3' THEN 'مضمون حكم' " +
//    		    "    WHEN TAFF.typema = '4' THEN 'جابر بالسجن' " +
//    		    "    WHEN TAFF.typema = 'T' THEN 'إحـــالة ' " +
//    		    "    ELSE TAFF.typema " +
//    		    "  END AS type_mandat, " +
//    		    "  TAFF.tntypema " +
//    		    "FROM " +
//    		    "  TIDEAFF@DBLINKMINEURPROD TAFF " +
//    		    "JOIN " +
//    		    "  tribunal@DBLINKMINEURPROD TR ON TAFF.tcodtri = TR.code_tribunal " +
//    		    "JOIN " +
//    		    "  natureAffaire@DBLINKMINEURPROD NA ON TAFF.tcodtaf = NA.code_nature " +
//    		    "WHERE " +
//    		    "  TAFF.tnumide = ? AND TAFF.tcoddet = ? " +
//    		    "ORDER BY " +
//    		    "  TAFF.tnumseqaff , TAFF.tdatdep";
    	String sql = "SELECT * FROM ( " +
    		    "SELECT TAFF.tnumseqaff, TR.libelle_tribunal, " +
    		    "SUBSTR(TAFF.tnumjaf, 4, 6) || ' - ' || SUBSTR(TAFF.tnumjaf, 1, 3) AS tnumjaf_formatte, " +
    		    "CASE " +
    		    "WHEN TAFF.TCODSIT = 'F' THEN 'مغـلـقـة' " +
    		    "WHEN TAFF.TCODSIT = 'O' THEN 'جـاريـة' " +
    		    "WHEN TAFF.TCODSIT = 'T' THEN 'محـالــــة' " +
    		    "WHEN TAFF.TCODSIT = 'ط' THEN 'طعــــــن' " +
    		    "WHEN TAFF.TCODSIT = 'ن' THEN 'إستئناف النيابة' " +
    		    "WHEN TAFF.TCODSIT = 'ت' THEN 'تعقيـــب' " +
    		    "WHEN TAFF.TCODSIT = 'L' THEN 'ســـراح' " +
    		    "ELSE TAFF.TCODSIT END AS etat_affaire, " +
    		    "NA.libelle_nature, " +
    		    "TAFF.TTYPRES || ' ' || TAFF.TCODRES || ' ' || TAFF.TANNRES AS numero_ecrou, " +
    		    "CASE " +
    		    "WHEN TAFF.typema = '1' THEN 'بطاقة إيداع' " +
    		    "WHEN TAFF.typema = '2' THEN 'بطاقة جلب' " +
    		    "WHEN TAFF.typema = '3' THEN 'مضمون حكم' " +
    		    "WHEN TAFF.typema = '4' THEN 'جابر بالسجن' " +
    		    "WHEN TAFF.typema = 'T' THEN 'إحـــالة ' " +
    		    "ELSE TAFF.typema END AS type_mandat, " +
    		    "TAFF.tntypema, " +
    		    "TAFF.tdatdep, "  +
    	        "TAFF.TCODSIT, " +
    		    "ROW_NUMBER() OVER ( " + 
    		    "            PARTITION BY TAFF.tnumseqaff  " + 
    		    "            ORDER BY  " + 
    		    "                TAFF.tdatdep DESC, " + 
    		    "                CASE TAFF.TCODSIT  " + 
    		    "                    WHEN 'O' THEN 1 " + 
    		    "                    WHEN 'L' THEN 2 " + 
    		    "                    WHEN 'ط' THEN 3 " + 
    		    "                    WHEN 'ن' THEN 4 " + 
    		    "                    WHEN 'ت' THEN 5 " + 
    		    "                    WHEN 'F' THEN 6 " + 
    		    "                    ELSE 7 " + 
    		    "                END " + 
    		    "        ) AS rn " +
    		    "FROM TIDEAFF@DBLINKMINEURPROD TAFF " +
    		    "JOIN tribunal@DBLINKMINEURPROD TR ON TAFF.tcodtri = TR.code_tribunal " +
    		    "JOIN natureAffaire@DBLINKMINEURPROD NA ON TAFF.tcodtaf = NA.code_nature " +
    		    "WHERE TAFF.tnumide = ? AND TAFF.tcoddet = ? " +
    		    ") WHERE rn = 1 ORDER BY tnumseqaff";

 
        return jdbcTemplate.query(sql, new Object[]{tnumide, tcoddet}, mapToDto());
    }

    private RowMapper<AffairePenaleDto> mapToDto() {
        return (rs, rowNum) -> AffairePenaleDto.builder()
            .tnumseqaff(rs.getString("tnumseqaff"))
            .libelleTribunal(rs.getString("libelle_tribunal"))
            .tnumjafFormatte(rs.getString("tnumjaf_formatte"))
            .etatAffaire(rs.getString("etat_affaire"))
            .libelleNature(rs.getString("libelle_nature"))
            .numeroEcrou(rs.getString("numero_ecrou"))
            .typeMandat(rs.getString("type_mandat"))
            .tntypema(rs.getString("tntypema"))
            .build();
    }
    
    
//    public String getTextMandatDepot(String tnumide, String tcoddet, String tnumseqaff, String tcodma) {
//        System.out.println(tnumide + " " + tcoddet + " " + tnumseqaff + " " + tcodma);
//
//        String sql = "SELECT ttextma FROM TMANDATDEPOT@DBLINKMINEURPROD " +
//                     "WHERE tnumide = ? AND tcoddet = ? AND tnumseqaff = ? AND tcodma = ?";
//
//        return jdbcTemplate.queryForObject(
//            sql,
//            new Object[]{tnumide, tcoddet, tnumseqaff, tcodma},
//            String.class
//        );
//    }
//    
    public PenalMandatDepotDTO getMandatDepot(String tnumide, String tcoddet, String tnumseqaff, String tcodma) {
        System.out.println(tnumide + " " + tcoddet + " " + tnumseqaff + " " + tcodma);

        String sql = "SELECT tnumide, tcoddet, tnumseqaff, tcodma, TO_CHAR(TDATDMA, 'YYYY-MM-DD') AS TDATDMA, "
        		+ "TO_CHAR(TDATAMA, 'YYYY-MM-DD') AS TDATAMA,"
        		+ " ttextma " +
                     "FROM TMANDATDEPOT@DBLINKMINEURPROD " +
                     "WHERE tnumide = ? AND tcoddet = ? AND tnumseqaff = ? AND tcodma = ?";

        return jdbcTemplate.queryForObject(
            sql,
            new Object[]{tnumide, tcoddet, tnumseqaff, tcodma},
            (rs, rowNum) -> {
            	PenalMandatDepotDTO dto = new PenalMandatDepotDTO();
                dto.setTnumide(rs.getString("tnumide"));
                dto.setTcoddet(rs.getString("tcoddet"));
                dto.setTnumseqaff(rs.getString("tnumseqaff"));
                dto.setTcodma(rs.getString("tcodma"));
                dto.setTdatdma(rs.getString("tdatdma"));
                dto.setTdatama(rs.getString("tdatama"));
                dto.setTtextma(rs.getString("ttextma"));
                return dto;
            }
        );
    }

    
    public List<AccusationExtraitJugementDTO> getAccusationsParDetenu(String numIde, String codDet, String codExtj) {
    	String sql = 
    		    "SELECT " +
    		    "    t.tcodacc, " +
    		    "    f.libelle_famille_acc, " +
    		    "    TO_CHAR(t.tdatdacc, 'YYYY-MM-DD') AS date_debut, " + 
    		    "    TO_CHAR(t.tdatfacc, 'YYYY-MM-DD') AS date_fin, " + 
    		    "    t.tduraccj, " +
    		    "    t.tduraccm, " +
    		    "    t.tduracca " +
    		    "FROM " +
    		    "    taccusation@DBLINKMINEURPROD t " +
    		    "JOIN " +
    		    "    famille_accusation@DBLINKMINEURPROD f ON t.tcodfac = f.code_famille_acc " +
    		    "WHERE " +
    		    "    t.tnumide = ? AND " +
    		    "    t.tcoddet = ? AND " +
    		    "    t.tcodextj = ?";


        return jdbcTemplate.query(sql, new Object[]{numIde, codDet, codExtj}, (rs, rowNum) -> {
        	AccusationExtraitJugementDTO dto = new AccusationExtraitJugementDTO();
            dto.setTcodacc(rs.getString("tcodacc"));
            dto.setLibelleFamilleAcc(rs.getString("libelle_famille_acc"));
            dto.setTdatdacc(rs.getString("date_debut"));
            dto.setTdatfacc(rs.getString("date_fin"));
            dto.setTduraccj(rs.getInt("tduraccj"));
            dto.setTduraccm(rs.getInt("tduraccm"));
            dto.setTduracca(rs.getInt("tduracca"));
            return dto;
        });
    }

    
    public Optional<PenalJugementDTO> getSinglePenalJugement(String tnumide, String tcoddet,  String tcodextj) {
//    	String tnumseqaff,
        String sql = 
            "SELECT " +
            "  tnumide, " +
            "  tcoddet, " +
            "  tnumseqaff, " +
            "  tcodextj, " +
            "  TO_CHAR(tdatjug, 'YYYY-MM-DD') AS date_jugement, " + 
            "  TO_CHAR(tdataex, 'YYYY-MM-DD') AS date_depot, " + 
            "  tj.ttexjug, " +
            "  tjt.libelle_tjugement, " +
            "  TO_CHAR(tdatdpe, 'YYYY-MM-DD') AS date_debut_punition, " + 
            "  TO_CHAR(tdatfpe, 'YYYY-MM-DD') AS date_fin_punition " +
            "FROM tjugement@DBLINKMINEURPROD tj " +
            "JOIN typejugement@DBLINKMINEURPROD tjt ON tj.tcodtju = tjt.code_tjugement " +
            "WHERE tnumide = ? AND tcoddet = ? AND tcodextj = ?";
//        AND tnumseqaff = ? 
        List<PenalJugementDTO> result = jdbcTemplate.query(sql, new Object[]{tnumide, tcoddet,  tcodextj},
//        		tnumseqaff,
            (rs, rowNum) -> {
                PenalJugementDTO dto = new PenalJugementDTO();
                dto.setTnumide(rs.getString("tnumide"));
                dto.setTcoddet(rs.getString("tcoddet"));
                dto.setTnumseqaff(rs.getString("tnumseqaff"));
                dto.setTcodextj(rs.getString("tcodextj"));
                dto.setDateJugement(rs.getString("date_jugement"));
                dto.setDateDepot(rs.getString("date_depot"));
                dto.setTtexjug(rs.getString("ttexjug"));
                dto.setLibelleTjugement(rs.getString("libelle_tjugement"));
                dto.setDateDebutPunition(rs.getString("date_debut_punition"));
                dto.setDateFinPunition(rs.getString("date_fin_punition"));
                return dto;
            }
        );

        return result.stream().findFirst(); // Optional<PenalJugementDTO>
    }


    public PenalTransfertDto getTransfert(String tnumide, String tcoddet, String tnumseqaff, String tcodtraf) {
        System.out.println(tnumide + " " + tcoddet + " " + tnumseqaff + " " + tcodtraf);

        String sql = "SELECT " +
                     "t.tnumide, " +
                     "t.tcoddet, " +
                     "t.tnumseqaff, " +
                     "t.tcodtraf, " +
                     "TO_CHAR(t.tdattran, 'YYYY-MM-DD') AS tdattran, " +
                     "t.tnumjaf, " +
                     "trib1.LIBELLE_TRIBUNAL AS libelle_tribunal_depart, " +
                     "t.tnumjafn, " +
                     "trib2.LIBELLE_TRIBUNAL AS libelle_tribunal_arrivee " +
                     "FROM TTRANSFERT@DBLINKMINEURPROD t " +
                     "LEFT JOIN TRIBUNAL@DBLINKMINEURPROD trib1 ON t.tcodtri = trib1.CODE_TRIBUNAL " +
                     "LEFT JOIN TRIBUNAL@DBLINKMINEURPROD trib2 ON t.tcodtrin = trib2.CODE_TRIBUNAL " +
                     "WHERE t.tnumide = ? " +
                     "AND t.tcoddet = ? " +
                     "AND t.tnumseqaff = ? " +
                     "AND t.tcodtraf = ?";

        List<PenalTransfertDto> result = jdbcTemplate.query(
            sql,
            new Object[]{tnumide, tcoddet, tnumseqaff, tcodtraf},
            (rs, rowNum) -> {
                PenalTransfertDto dto = new PenalTransfertDto();
                dto.setTnumide(rs.getString("tnumide"));
                dto.setTcoddet(rs.getString("tcoddet"));
                dto.setTnumseqaff(rs.getString("tnumseqaff"));
                dto.setTcodtraf(rs.getString("tcodtraf"));
                dto.setTdattran(rs.getString("tdattran"));
                dto.setTnumjaf(rs.getString("tnumjaf"));
                dto.setLibelleTribunalDepart(rs.getString("libelle_tribunal_depart"));
                dto.setTnumjafn(rs.getString("tnumjafn"));
                dto.setLibelleTribunalArrivee(rs.getString("libelle_tribunal_arrivee"));
                return dto;
            }
        );

        return result.isEmpty() ? null : result.get(0);
    }

    
    
    public PenalContestationDto getContestation(String tnumide, String tcoddet, String tnumseqaff, String TCODEXTJ , String codeDocumentSecondaire) {
        String sql = "SELECT " +
                     "t.tnumide, " +
                     "t.tcoddet, " +
                     "t.tnumseqaff, " +
                     "t.tcodco, " +
                     "TO_CHAR(t.tdatco, 'YYYY-MM-DD') AS tdatco, " +
                     "SUBSTR(t.tnumjaf, 4, 6) || ' - ' || SUBSTR(t.tnumjaf, 1, 3) AS num_affaire, " +
                     "trib1.LIBELLE_TRIBUNAL AS libelle_tribunal, " +
                     "type_contestation.tlibtco " +
                     "FROM TCONTESTATION@DBLINKMINEURPROD t " +
                     "LEFT JOIN TRIBUNAL@DBLINKMINEURPROD trib1 ON t.tcodtri = trib1.CODE_TRIBUNAL " +
                     "LEFT JOIN TTYPECONTESTATION@DBLINKMINEURPROD type_contestation ON t.tcodtco = type_contestation.tcodtco " +
                     "WHERE t.tnumide = ? " +
                     "AND t.tcoddet = ? " +
                     "AND t.tnumseqaff = ? " +
                     "AND t.TCODEXTJ = ? " +
                     
                     "AND t.tcodco = ?";

        List<PenalContestationDto> result = jdbcTemplate.query(
            sql,
            new Object[]{tnumide, tcoddet, tnumseqaff, TCODEXTJ ,codeDocumentSecondaire},
            (rs, rowNum) -> {
                PenalContestationDto dto = new PenalContestationDto();
                dto.setTnumide(rs.getString("tnumide"));
                dto.setTcoddet(rs.getString("tcoddet"));
                dto.setTnumseqaff(rs.getString("tnumseqaff"));
                dto.setTcodco(rs.getString("tcodco"));
                dto.setTdatco(rs.getString("tdatco"));
                dto.setNumAffaire(rs.getString("num_affaire"));
                dto.setLibelleTribunal(rs.getString("libelle_tribunal"));
                dto.setTlibtco(rs.getString("tlibtco"));
                return dto;
            }
        );

        return result.isEmpty() ? null : result.get(0);
    }

    
    

    public List<ActeJudiciaire> getActesJudiciaires(String tnumide, String tcoddet, String tnumseqaff) {
    	String sql =
    		    "SELECT t.tnumide, t.tcoddet, t.tnumseqaff, t.TCODEXTJ AS code_document, ' ' as code_document_secondaire, 'tjugement' AS type_acte, " +
    		    "TO_CHAR(t.tdatjug, 'YYYY-MM-DD') AS date_acte, SUBSTR(t.tnumjaf, 4, 6) || ' - ' || SUBSTR(t.tnumjaf, 1, 3) AS num_affaire, " +
    		    "tr.libelle_tribunal, 'مضمون حكم' AS type_document, ' ' AS type_motif, '3' AS priorite " +
    		    "FROM tjugement@DBLINKMINEURPROD t " +
    		    "LEFT JOIN tribunal@DBLINKMINEURPROD tr ON t.tcodtri = tr.code_tribunal " +
    		    "LEFT JOIN tmotif@DBLINKMINEURPROD motif ON motif.tcodmot = t.tcodmot " +
    		    "WHERE t.tnumide = ? AND t.tcoddet = ? AND t.tnumseqaff = ? " +

    		    "UNION ALL " +

    		    "SELECT t.tnumide, t.tcoddet, t.tnumseqaff, t.tcodma AS code_document, ' ' as code_document_secondaire, 'tmandatdepot' AS type_acte, " +
    		    "TO_CHAR(t.TDATAMA, 'YYYY-MM-DD') AS date_acte, SUBSTR(t.tnumjaf, 4, 6) || ' - ' || SUBSTR(t.tnumjaf, 1, 3) AS num_affaire, " +
    		    "tr.libelle_tribunal, 'بطاقة إيداع', ' ' AS type_motif, '1' AS priorite " +
    		    "FROM TMANDATDEPOT@DBLINKMINEURPROD t " +
    		    "LEFT JOIN tribunal@DBLINKMINEURPROD tr ON t.tcodtri = tr.code_tribunal " +
    		    "WHERE t.tnumide = ? AND t.tcoddet = ? AND t.tnumseqaff = ? " +

    		    "UNION ALL " +

    		    "SELECT t.tnumide, t.tcoddet, t.tnumseqaff, t.tcodtraf AS code_document, ' ' as code_document_secondaire, 'ttransfert' AS type_acte, " +
    		    "TO_CHAR(t.tdattran, 'YYYY-MM-DD') AS date_acte, SUBSTR(t.tnumjafn, 4, 6) || ' - ' || SUBSTR(t.tnumjafn, 1, 3) AS num_affaire, " +
    		    "tr.libelle_tribunal, tty.TLIBTYPTR, ' ' AS type_motif, '2' AS priorite " +
    		    "FROM TTRANSFERT@DBLINKMINEURPROD t " +
    		    "LEFT JOIN tribunal@DBLINKMINEURPROD tr ON t.tcodtrin = tr.code_tribunal " +
    		    "LEFT JOIN ttypetransfert@DBLINKMINEURPROD tty ON t.TCODREST = tty.TCODTYPTR " +
    		    "WHERE t.tnumide = ? AND t.tcoddet = ? AND t.tnumseqaff = ? " +

    		    "UNION ALL " +

    		    "SELECT t.tnumide, t.tcoddet, t.tnumseqaff, t.TCODEXTJ AS code_document, t.TCODCO as code_document_secondaire, 'tcontestation' AS type_acte, " +
    		    "TO_CHAR(t.TDATCO, 'YYYY-MM-DD') AS date_acte, SUBSTR(t.tnumjaf, 4, 6) || ' - ' || SUBSTR(t.tnumjaf, 1, 3) AS num_affaire, " +
    		    "tr.libelle_tribunal, tty.TLIBTCO, ' ' AS type_motif, '4' AS priorite " +
    		    "FROM tcontestation@DBLINKMINEURPROD t " +
    		    "LEFT JOIN tribunal@DBLINKMINEURPROD tr ON t.tcodtri = tr.code_tribunal " +
    		    "LEFT JOIN ttypecontestation@DBLINKMINEURPROD tty ON t.TCODTCO = tty.TCODTCO " +
    		    "WHERE t.tnumide = ? AND t.tcoddet = ? AND t.tnumseqaff = ? " +

    		    "UNION ALL " +

    		    "SELECT t.tnumide, t.tcoddet, t.tnumseqaff, t.TCODEXTJ AS code_document, ' ' as code_document_secondaire, 'tjugementLibre' AS type_acte, " +
    		    "TO_CHAR(t.TDATFPE, 'YYYY-MM-DD') AS date_acte, SUBSTR(aff.tnumjaf, 4, 6) || ' - ' || SUBSTR(t.tnumjaf, 1, 3) AS num_affaire, " +
    		    "tr.libelle_tribunal, motif.tlibmot, motif.TYPE_MOTIF AS type_motif, '7' AS priorite " +
    		    "FROM tjugement@DBLINKMINEURPROD t " +
    		    "LEFT JOIN tmotif@DBLINKMINEURPROD motif ON motif.tcodmot = t.tcodmot " +
    		    "LEFT JOIN tideaff@DBLINKMINEURPROD aff ON t.tnumide = aff.tnumide AND t.tcoddet = aff.tcoddet AND t.tnumseqaff = aff.tnumseqaff AND t.TDATFPE = aff.tdatdep " +
    		    "LEFT JOIN tribunal@DBLINKMINEURPROD tr ON aff.TCODTRI = tr.code_tribunal " +
    		    "WHERE t.tnumide = ? AND t.tcoddet = ? AND t.tnumseqaff = ? AND t.tcodmot IS NOT NULL " +

    		    "UNION ALL " +

    		    "SELECT t.tnumide, t.tcoddet, t.tnumseqaff, t.tcodma AS code_document,  ' ' as code_document_secondaire, 'tmandatdepotLibre' AS type_acte, " +
    		    "TO_CHAR(t.tdatfma, 'YYYY-MM-DD') AS date_acte, SUBSTR(aff.tnumjaf, 4, 6) || ' - ' || SUBSTR(t.tnumjaf, 1, 3) AS num_affaire, " +
    		    "tr.libelle_tribunal, motif.tlibmot, motif.TYPE_MOTIF AS type_motif, '5' AS priorite " +
    		    "FROM tmandatdepot@DBLINKMINEURPROD t " +
    		    "LEFT JOIN tmotif@DBLINKMINEURPROD motif ON motif.tcodmot = t.tcodmot " +
    		    "LEFT JOIN tideaff@DBLINKMINEURPROD aff ON t.tnumide = aff.tnumide AND t.tcoddet = aff.tcoddet AND t.tnumseqaff = aff.tnumseqaff AND t.tdatfma = aff.tdatdep " +
    		    "LEFT JOIN tribunal@DBLINKMINEURPROD tr ON aff.TCODTRI = tr.code_tribunal " +
    		    "WHERE t.tnumide = ? AND t.tcoddet = ? AND t.tnumseqaff = ? AND t.tcodmot IS NOT NULL " +

    		    "UNION ALL " +

    		    "SELECT t.tnumide, t.tcoddet, t.tnumseqaff, t.tcodtraf AS code_document,  ' ' as code_document_secondaire,  'ttransfertLibre' AS type_acte, " +
    		    "TO_CHAR(t.tdatfma, 'YYYY-MM-DD') AS date_acte, SUBSTR(aff.tnumjaf, 4, 6) || ' - ' || SUBSTR(t.tnumjaf, 1, 3) AS num_affaire, " +
    		    "tr.libelle_tribunal, motif.tlibmot, motif.TYPE_MOTIF AS type_motif, '7' AS priorite " +
    		    "FROM TTRANSFERT@DBLINKMINEURPROD t " +
    		    "LEFT JOIN tmotif@DBLINKMINEURPROD motif ON motif.tcodmot = t.tcodmot " +
    		    "LEFT JOIN tideaff@DBLINKMINEURPROD aff ON t.tnumide = aff.tnumide AND t.tcoddet = aff.tcoddet AND t.tnumseqaff = aff.tnumseqaff AND t.tdatfma = aff.tdatdep " +
    		    "LEFT JOIN tribunal@DBLINKMINEURPROD tr ON aff.TCODTRI = tr.code_tribunal " +
    		    "WHERE t.tnumide = ? AND t.tcoddet = ? AND t.tnumseqaff = ? AND t.tcodmot IS NOT NULL "+
    		    "ORDER BY date_acte ASC , priorite";

    	    Object[] params = {
    	        tnumide, tcoddet, tnumseqaff,  // 1. jugement
    	        tnumide, tcoddet, tnumseqaff,  // 2. mandat
    	        tnumide, tcoddet, tnumseqaff,  // 3. transfert
    	        tnumide, tcoddet, tnumseqaff,  // 4. contestation
    	        tnumide, tcoddet, tnumseqaff,  // 5. jugement (TDATFPE)
    	        tnumide, tcoddet, tnumseqaff,  // 6. mandat (tdatfma)
    	        tnumide, tcoddet, tnumseqaff   // 7. transfert (tdatfma)
    	    };

        return jdbcTemplate.query(sql, params,
            (rs, rowNum) -> {
                ActeJudiciaire acte = new ActeJudiciaire();
              
               
                
                
                
                

           
          	 acte.setTnumide(rs.getString("tnumide") );
          	  acte.setTcoddet(rs.getString("tcoddet") );
          	   acte.setTnumseqaff(rs.getString("tnumseqaff"));
          	  acte.setCodeDocument(rs.getString("code_document"));
          	  acte.setTypeActe(rs.getString("type_acte") );
          	    
          	  acte.setDateActe(rs.getString("date_acte"));
          	 acte.setNumAffaire(rs.getString("num_affaire"));
          	acte.setLibelleTribunal(rs.getString("libelle_tribunal"));
          	acte.setTypeDocument(rs.getString("type_document"));
          	 
          	acte.setTypeMotif(rs.getString("type_motif") );
          	acte.setCodeDocumentSecondaire(rs.getString("code_document_secondaire"));
          	   
                return acte;
            }
        );
    }
    
    
    

    public ArretExecutionPenalDTO getArretExecutionParTypeActe(String tnumide, String tcoddet, String tnumseqaff, String typeActe) {
        String sql;

        switch (typeActe) {
            case "tjugementLibre":
                sql = "SELECT t.tnumide, t.tcoddet, t.tnumseqaff, t.TCODEXTJ AS code_document, " +
                      "' ' AS code_document_secondaire, 'tjugementLibre' AS type_acte, " +
                      "TO_CHAR(t.TDATFPE, 'YYYY-MM-DD') AS date_acte, " +
                      "SUBSTR(aff.tnumjaf, 4, 6) || ' - ' || SUBSTR(t.tnumjaf, 1, 3) AS num_affaire, " +
                      "tr.libelle_tribunal, motif.tlibmot, motif.TYPE_MOTIF AS type_motif, '7' AS priorite " +
                      "FROM tjugement@DBLINKMINEURPROD t " +
                      "LEFT JOIN tmotif@DBLINKMINEURPROD motif ON motif.tcodmot = t.tcodmot " +
                      "LEFT JOIN tideaff@DBLINKMINEURPROD aff ON t.tnumide = aff.tnumide AND t.tcoddet = aff.tcoddet AND t.tnumseqaff = aff.tnumseqaff AND t.TDATFPE = aff.tdatdep " +
                      "LEFT JOIN tribunal@DBLINKMINEURPROD tr ON aff.TCODTRI = tr.code_tribunal " +
                      "WHERE t.tnumide = ? AND t.tcoddet = ? AND t.tnumseqaff = ? AND t.tcodmot IS NOT NULL";
                break;

            case "tmandatdepotLibre":
                sql = "SELECT t.tnumide, t.tcoddet, t.tnumseqaff, t.tcodma AS code_document, " +
                      "' ' AS code_document_secondaire, 'tmandatdepotLibre' AS type_acte, " +
                      "TO_CHAR(t.tdatfma, 'YYYY-MM-DD') AS date_acte, " +
                      "SUBSTR(aff.tnumjaf, 4, 6) || ' - ' || SUBSTR(t.tnumjaf, 1, 3) AS num_affaire, " +
                      "tr.libelle_tribunal, motif.tlibmot, motif.TYPE_MOTIF AS type_motif, '5' AS priorite " +
                      "FROM tmandatdepot@DBLINKMINEURPROD t " +
                      "LEFT JOIN tmotif@DBLINKMINEURPROD motif ON motif.tcodmot = t.tcodmot " +
                      "LEFT JOIN tideaff@DBLINKMINEURPROD aff ON t.tnumide = aff.tnumide AND t.tcoddet = aff.tcoddet AND t.tnumseqaff = aff.tnumseqaff AND t.tdatfma = aff.tdatdep " +
                      "LEFT JOIN tribunal@DBLINKMINEURPROD tr ON aff.TCODTRI = tr.code_tribunal " +
                      "WHERE t.tnumide = ? AND t.tcoddet = ? AND t.tnumseqaff = ? AND t.tcodmot IS NOT NULL";
                break;

            case "ttransfertLibre":
                sql = "SELECT t.tnumide, t.tcoddet, t.tnumseqaff, t.tcodtraf AS code_document, " +
                      "' ' AS code_document_secondaire, 'ttransfertLibre' AS type_acte, " +
                      "TO_CHAR(t.tdatfma, 'YYYY-MM-DD') AS date_acte, " +
                      "SUBSTR(aff.tnumjaf, 4, 6) || ' - ' || SUBSTR(t.tnumjaf, 1, 3) AS num_affaire, " +
                      "tr.libelle_tribunal, motif.tlibmot, motif.TYPE_MOTIF AS type_motif, '7' AS priorite " +
                      "FROM TTRANSFERT@DBLINKMINEURPROD t " +
                      "LEFT JOIN tmotif@DBLINKMINEURPROD motif ON motif.tcodmot = t.tcodmot " +
                      "LEFT JOIN tideaff@DBLINKMINEURPROD aff ON t.tnumide = aff.tnumide AND t.tcoddet = aff.tcoddet AND t.tnumseqaff = aff.tnumseqaff AND t.tdatfma = aff.tdatdep " +
                      "LEFT JOIN tribunal@DBLINKMINEURPROD tr ON aff.TCODTRI = tr.code_tribunal " +
                      "WHERE t.tnumide = ? AND t.tcoddet = ? AND t.tnumseqaff = ? AND t.tcodmot IS NOT NULL";
                break;

            default:
                throw new IllegalArgumentException("Type acte inconnu : " + typeActe);
        }

        return jdbcTemplate.queryForObject(
            sql,
            new Object[]{tnumide, tcoddet, tnumseqaff},
            (rs, rowNum) -> {
            	ArretExecutionPenalDTO dto = new ArretExecutionPenalDTO();
                dto.setTnumide(rs.getString("tnumide"));
                dto.setTcoddet(rs.getString("tcoddet"));
                dto.setTnumseqaff(rs.getString("tnumseqaff"));
                dto.setCodeDocument(rs.getString("code_document"));
                dto.setCodeDocumentSecondaire(rs.getString("code_document_secondaire"));
                dto.setTypeActe(rs.getString("type_acte"));
                dto.setDateActe(rs.getString("date_acte"));
                dto.setNumAffaire(rs.getString("num_affaire"));
                dto.setLibelleTribunal(rs.getString("libelle_tribunal"));
                dto.setLibelleMotif(rs.getString("tlibmot"));
                dto.setTypeMotif(rs.getString("type_motif"));
              
                return dto;
            }
        );
    }


    public List<PenaleDetentionInfoDto> trouverToutDetentionInfosParPrisonerIdDansPrisons(String prisonerId) {
        String sql = 
            "SELECT " +
            "  res.TCODDET AS numroDetention, " +
            "  iden.TNUMIDE AS prisoner_id, " +
            "  GETLIBELLEPRISON@DBLINKMINEURPROD(res.TCODGOU, res.TCODPR) AS prision, " +
            "  res.TTYPRES || '  ' || res.TCODRES || '  ' || res.TANNRES AS numero_ecrou, " +
            "  TO_CHAR(res.TDATDR, 'YYYY-MM-DD') AS date_entre, " +
            "  TO_CHAR(res.TDATFR, 'YYYY-MM-DD') AS date_sortie " +
            "FROM TIDENSIMP@DBLINKMINEURPROD simp " +
            "JOIN TIDENTITE@DBLINKMINEURPROD iden ON simp.TNUMIDE = iden.TNUMIDE " +
            "JOIN TRESIDENCE@DBLINKMINEURPROD res ON iden.TNUMIDE = res.TNUMIDE " +
            "WHERE res.TDATDR = ( " +
            "    SELECT MAX(r2.TDATDR) " +
            "    FROM TRESIDENCE@DBLINKMINEURPROD r2 " +
            "    WHERE r2.TNUMIDE = iden.TNUMIDE " +
            "    AND r2.TCODDET = res.TCODDET " +
            ") " +
            "AND iden.TNUMIDE = ? " +
            "ORDER BY " +
            "  CASE WHEN res.TDATFR IS NULL THEN 1 ELSE 0 END DESC, " +
            "  res.TDATFR DESC";

        return jdbcTemplate.query(sql, new Object[]{prisonerId}, (rs, rowNum) -> {
            PenaleDetentionInfoDto info = new PenaleDetentionInfoDto();
            info.setNumroDetention(rs.getString("numroDetention"));
            info.setPrisonerId(rs.getString("prisoner_id"));
            info.setPrision(rs.getString("prision"));
            info.setNumeroEcrou(rs.getString("numero_ecrou"));
            info.setDateEntre(rs.getString("date_entre"));
            info.setDateSortie(rs.getString("date_sortie"));
            return info;
        });
    }

}

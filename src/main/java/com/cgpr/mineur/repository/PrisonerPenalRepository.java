package com.cgpr.mineur.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

    public List<SearchDetenuDto> findPrisonerPenalByCriteria(String nom, String prenom, String nomPere, LocalDate dateDeNaissance, String sexe) {
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
                "AND (? IS NULL OR simp.TDATN = ?) " +
                "AND (? IS NULL OR simp.TCODSEX = ?) " +
                "ORDER BY date_entre DESC";  
    	
//    	
//    	 "AND (? IS NULL OR simp.TPNOMSA LIKE '%' || ? || '%') " +
//         "AND (? IS NULL OR simp.TNOMSA LIKE '%' || ? || '%') " +
//         "AND (? IS NULL OR simp.TPPERSA LIKE '%' || ? || '%') " +
//         "AND (? IS NULL OR simp.TDATN = ?) " +
//         "AND (? IS NULL OR simp.TCODSEX = ?)";


        // Exécution de la requête
        return jdbcTemplate.query(
            sql,
            new Object[]{nom, nom, prenom, prenom, nomPere, nomPere, dateDeNaissance, dateDeNaissance, sexe, sexe},
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
    	String sql = 
    	    "SELECT " +
    	    "    iden.TNUMIDE AS prisoner_id, " +
    	    "    iden.TPNOMA AS firstname, " +
    	    "    iden.TPPERA AS father_name, " +
    	    "    iden.TPGPERA AS grandfather_name, " +
    	    "    iden.TNOMA AS lastname, " +
    	    "    iden.TDATN AS birth_date, " +
    	    "    iden.TPMER AS mother_name, " +
    	    "    iden.TNOMMER AS maternal_grandmother_name, " +
    	    "    iden.TCODSEX AS sex, " +
    	    "    iden.tadr AS adresse, " +
    	    "    GETLIBELLE_GOUVERNORAT@DBLINKMINEURPROD(iden.TCODGOUN) AS place_of_birth, " +
    	    "    res.TTYPRES || '  ' || res.TCODRES || '  ' || res.TANNRES AS numero_ecrou, " +
    	    "    TO_CHAR(res.TDATDR, 'YYYY-MM-DD') AS date_entre, " +
    	    "    GETLIBELLEPRISON@DBLINKMINEURPROD(res.TCODGOU, res.TCODPR) AS prision, " +
    	    "    res.TCODDET AS numroDetention, " +
    	    "    ja.TETAT, " +
    	    "    na.LIBELLE_NATURE as natureAffaire, " +
    	    "    ja.TCODTYP, " +
    	    "    ja.TDATDPE AS debutPunition, " +
    	    "    ja.TDATLIB AS finPunition " +
    	    "FROM " +
    	    "    TIDENTITE@DBLINKMINEURPROD iden " +
    	    "JOIN " +
    	    "    TRESIDENCE@DBLINKMINEURPROD res " +
    	    "    ON iden.TNUMIDE = res.TNUMIDE " +
    	    "JOIN " +
    	    "    tjugearret@DBLINKMINEURPROD ja " +
    	    "    ON iden.TNUMIDE = ja.TNUMIDE " +
    	    "JOIN " +
    	    "    natureaffaire@DBLINKMINEURPROD na " +
    	    "    ON ja.TCODTAF = na.CODE_NATURE " +
    	    "WHERE " +
    	     "    res.tclores = 'O' AND " +
    	    "    res.TCODDET = ( " +
    	    "        SELECT MAX(r2.TCODDET) " +
    	    "        FROM TRESIDENCE@DBLINKMINEURPROD r2 " +
    	    "        WHERE r2.TNUMIDE = res.TNUMIDE " +
    	    "    ) AND " +
    	    "    res.TDATDR = ( " +
    	    "        SELECT MAX(r2.TDATDR) " +
    	    "        FROM TRESIDENCE@DBLINKMINEURPROD r2 " +
    	    "        WHERE r2.TNUMIDE = iden.TNUMIDE " +
    	    "          AND r2.TCODDET = res.TCODDET " +
    	    "    ) AND " +
    	    "    iden.TNUMIDE = ? " +
    	    "ORDER BY date_entre DESC";

//        // Construction de la requête SQL avec jointure
//        String sql = 
//            "SELECT "+
//                "iden.TNUMIDE AS prisoner_id, "+
//                "iden.TPNOMA AS firstname, "+
//                "iden.TPPERA AS father_name, "+
//                "iden.TPGPERA AS grandfather_name, "+
//                "iden.TNOMA AS lastname, "+
//                "iden.TDATN AS birth_date, "+
//                "iden.TPMER AS mother_name, "+
//                "iden.TNOMMER AS maternal_grandmother_name, "+
//                "iden.TCODSEX AS sex, "+
//                "iden.tadr AS adresse,"+
//                "GETLIBELLE_GOUVERNORAT@DBLINKMINEURPROD(iden.TCODGOUN) AS place_of_birth, "+
//                "res.TTYPRES || '  ' || res.TCODRES || '  ' || res.TANNRES AS numero_ecrou, "+
//                "TO_CHAR(res.TDATDR, 'YYYY-MM-DD') AS date_entre, "+
//                "GETLIBELLEPRISON@DBLINKMINEURPROD(res.TCODGOU, res.TCODPR) AS prision, "+
//                "res.TCODDET AS numroDetention "+
//            "FROM "+
//                "TIDENTITE@DBLINKMINEURPROD iden "+
//            "JOIN "+
//                "TRESIDENCE@DBLINKMINEURPROD res "+
//                "ON iden.TNUMIDE = res.TNUMIDE "+
//            "WHERE "+
//                "res.TCODDET = ( "+
//                    "SELECT MAX(r2.TCODDET) "+
//                    "FROM TRESIDENCE@DBLINKMINEURPROD r2 "+
//                    "WHERE r2.TNUMIDE = res.TNUMIDE "+
//                ") "+
//                "AND res.TDATDR = ( "+
//                    "SELECT MAX(r2.TDATDR) "+
//                    "FROM TRESIDENCE@DBLINKMINEURPROD r2 "+
//                    "WHERE r2.TNUMIDE = iden.TNUMIDE "+
//                      "AND r2.TCODDET = res.TCODDET "+
//                ") "+
//                "AND iden.TNUMIDE = ? "+
//            "ORDER BY date_entre DESC  ";
        

        try {
            // Exécution de la requête
            return jdbcTemplate.queryForObject(
                sql,
                new Object[]{prisonerId},
                (rs, rowNum) -> {
                    // Mapping du résultat de la requête vers un objet SearchDetenuDto
                	PrisonerPenaleDto prisoner = new PrisonerPenaleDto();
                    prisoner.setDetenuId(rs.getString("prisoner_id"));
                    prisoner.setNom(rs.getString("firstname"));
                    prisoner.setPrenom(rs.getString("lastname"));
                    prisoner.setNomPere(rs.getString("father_name"));
                    prisoner.setNomGrandPere(rs.getString("grandfather_name"));
                    prisoner.setNomMere(rs.getString("mother_name"));
                    prisoner.setPrenomMere(rs.getString("maternal_grandmother_name"));
                    prisoner.setDateNaissance(rs.getDate("birth_date").toLocalDate()); // Conversion de Date SQL en LocalDate
                    prisoner.setSexe(rs.getString("sex"));
                    prisoner.setLieuNaissance(rs.getString("place_of_birth"));
                    prisoner.setNumeroEcrou(rs.getString("numero_ecrou"));
                    prisoner.setDateEntree(rs.getString("date_entre"));
                    prisoner.setNomEtablissement(rs.getString("prision"));
                    prisoner.setAdresse(rs.getString("adresse"));
                    prisoner.setDebutPunition(rs.getString("debutPunition"));
                    prisoner.setFinPunition(rs.getString("finPunition"));
                    prisoner.setNatureAffaire(rs.getString("natureAffaire"));
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
}

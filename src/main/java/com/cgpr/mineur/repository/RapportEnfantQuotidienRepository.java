package com.cgpr.mineur.repository;

 
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;
import com.cgpr.mineur.models.RapportEnfantQuotidien;
import com.cgpr.mineur.models.RapportEnfantQuotidienId;
import com.cgpr.mineur.models.TypeAffaire;

 
 

@Repository
public interface RapportEnfantQuotidienRepository  extends CrudRepository<RapportEnfantQuotidien, RapportEnfantQuotidienId> {
	
	
	  
    @Query("SELECT r FROM RapportEnfantQuotidien r WHERE  r.statutPenal = 'juge' and TRUNC(r.rapportEnfantQuotidienId.dateSauvgarde) = TRUNC(:date) ")
    List<RapportEnfantQuotidien> findByDate(@Param("date") LocalDate date);
	   
    @Query("SELECT r FROM RapportEnfantQuotidien r " +
    	       "WHERE r.etablissement.id IN :etablissementIds " +
    	       "AND r.statutPenal IN :statutPenals " +
    	       "AND TRUNC(r.rapportEnfantQuotidienId.dateSauvgarde) = TRUNC(:date)")
    	List<RapportEnfantQuotidien> findByDateAndStatutPenalAndEtablissements(
    	    @Param("date") LocalDate date,
    	    @Param("statutPenals") List<String> statutPenals,
    	    @Param("etablissementIds") List<String> etablissementIds);

 // Exemple de requête pour obtenir le nombre de rapports en fonction de certains filtres
//    @Query("SELECT COUNT(r) " + 
//    	       "FROM RapportEnfantQuotidien r " + 
//    	       "WHERE (  r.etablissement.id IN :etablissementIds) " +
//    	       "AND (  r.nationalite.id IN :nationalites) " + 
//    	       "AND (  r.typeAffaire.id IN :typeAffaires) " + 
//    	       "AND (  r.statutPenal IN :statutPenals) " + 
//    	       "AND (  r.sexe IN :sexes) " + 
//    	       "AND (  TRUNC(r.rapportEnfantQuotidienId.dateSauvgarde) = TRUNC(:date))")
//    	int countByFilters(
//    	    @Param("sexes") List<String> sexes,
//    	    @Param("typeAffaires") List<Long> typeAffaires,
//    	    @Param("nationalites") List<Long> nationalites,
//    	    @Param("statutPenals") List<String> statutPenals,
//    	    @Param("etablissementIds") List<String> etablissementIds,
//    	    @Param("date") LocalDate date );

    @Query("SELECT COUNT(r) " +
    	       "FROM RapportEnfantQuotidien r " +
    	       "WHERE (  r.etablissement.id IN :etablissementIds) " +
    	       "AND ( r.nationalite.id IN :nationalites) " +
    	       "AND ( r.typeAffaire.id IN :typeAffaires) " +
    	       "AND ( r.statutPenal IN :statutPenals) " +
    	       "AND ( r.sexe IN :sexes) " +
    	       "AND ( TRUNC(r.rapportEnfantQuotidienId.dateSauvgarde) = TRUNC(:date))")
    	int countByFilters(
    	    @Param("sexes") List<String> sexes,
    	    @Param("typeAffaires") List<Long> typeAffaires,
    	    @Param("nationalites") List<Long> nationalites,
    	    @Param("statutPenals") List<String> statutPenals,
    	    @Param("etablissementIds") List<String> etablissementIds,
    	    @Param("date") LocalDate date);


    @Query("SELECT new com.cgpr.mineur.dto.StatistiqueEtablissementDTO( " +
    	       " r.etablissement.id, "+
    	       " r.etablissement.libelle_etablissement , " +
    	       " SUM(CASE WHEN r.statutPenal = 'juge' THEN 1 ELSE 0 END), " +
    	       " SUM(CASE WHEN r.statutPenal = 'arret' THEN 1 ELSE 0 END), " +
    	       " SUM(CASE WHEN r.statutPenal = 'libre' THEN 1 ELSE 0 END), " +
    	       " SUM(CASE WHEN r.nationalite.id != 1 AND r.sexe = 'ذكر' THEN 1 ELSE 0 END), " +  // Male, Nationalite != 1
    	       " SUM(CASE WHEN r.typeAffaire.id = 5 AND r.sexe = 'ذكر' THEN 1 ELSE 0 END), " +      // Male, TypeAffaire.id = 5
    	       " SUM(CASE WHEN r.nationalite.id != 1 AND r.sexe = 'أنثى' THEN 1 ELSE 0 END), " +  // Female, Nationalite != 1
    	       " SUM(CASE WHEN r.typeAffaire.id = 5 AND r.sexe = 'أنثى' THEN 1 ELSE 0 END)) " +      // Female, TypeAffaire.id = 5
    	       "FROM RapportEnfantQuotidien r " +
    	       "WHERE TRUNC(r.rapportEnfantQuotidienId.dateSauvgarde) = TRUNC(:date) " + // Filtrage sur une seule date
    	       "GROUP BY r.etablissement.id, r.etablissement.libelle_etablissement")
    	List<StatistiqueEtablissementDTO> getStatistiquesParDate(@Param("date") LocalDate date);

//    @Query("SELECT new com.cgpr.mineur.dto.StatistiqueEtablissementDTO( " +
//    	       " r.etablissement.id, "+
//    	       " r.etablissement.libelle_etablissement , " +
//    	       " SUM(CASE WHEN r.statutPenal = 'juge' THEN 1 ELSE 0 END), " +
//    	       " SUM(CASE WHEN r.statutPenal = 'arret' THEN 1 ELSE 0 END), " +
//    	       " SUM(CASE WHEN r.statutPenal = 'libre' THEN 1 ELSE 0 END), " +
//    	       " SUM(CASE WHEN r.nationalite.id != 1 THEN 1 ELSE 0 END), " +
//    	       " SUM(CASE WHEN r.typeAffaire.id = 5 THEN 1 ELSE 0 END), " +
//    	       " SUM(CASE WHEN r.nationalite.id != 1 AND r.sexe = 'ذكر' THEN 1 ELSE 0 END), " +  // Male, Nationalite != 1
//    	       " SUM(CASE WHEN r.typeAffaire.id = 5 AND r.sexe = 'ذكر' THEN 1 ELSE 0 END), " +      // Male, TypeAffaire.id = 5
//    	       " SUM(CASE WHEN r.nationalite.id != 1 AND r.sexe = 'أنثى' THEN 1 ELSE 0 END), " +  // Female, Nationalite != 1
//    	       " SUM(CASE WHEN r.typeAffaire.id = 5 AND r.sexe = 'أنثى' THEN 1 ELSE 0 END)) " +      // Female, TypeAffaire.id = 5
//    	       "FROM RapportEnfantQuotidien r " +
//    	       "WHERE TRUNC(r.rapportEnfantQuotidienId.dateSauvgarde) = TRUNC(:date) " + // Filtrage sur une seule date
//    	       "GROUP BY r.etablissement.id, r.etablissement.libelle_etablissement")
//    	List<StatistiqueEtablissementDTO> getStatistiquesParDate(@Param("date") LocalDate date);



}


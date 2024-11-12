package com.cgpr.mineur.repository;

 
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.CauseLiberation;
import com.cgpr.mineur.models.ClassePenale;
import com.cgpr.mineur.models.Delegation;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Gouvernorat;
import com.cgpr.mineur.models.Metier;
import com.cgpr.mineur.models.Nationalite;
import com.cgpr.mineur.models.NiveauEducatif;
import com.cgpr.mineur.models.RapportEnfantQuotidien;
import com.cgpr.mineur.models.RapportEnfantQuotidienId;
import com.cgpr.mineur.models.RapportQuotidien;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.ResidenceId;
import com.cgpr.mineur.models.SituationFamiliale;
import com.cgpr.mineur.models.SituationSocial;
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.models.TypeTribunal;
import com.cgpr.mineur.tools.CommonConditions;

 
 

@Repository
public interface RapportEnfantQuotidienRepository extends CrudRepository<RapportEnfantQuotidien, RapportEnfantQuotidienId> {
	
	
	  
    @Query("SELECT r FROM RapportEnfantQuotidien r WHERE  r.statutPenal = 'juge' and TRUNC(r.rapportEnfantQuotidienId.dateSauvgarde) = TRUNC(:date) ")
    List<RapportEnfantQuotidien> findByDate(@Param("date") LocalDate date);
	   
    @Query("SELECT r FROM RapportEnfantQuotidien r WHERE  r.etablissement IN :etablissements and r.statutPenal = :statutPenal and TRUNC(r.rapportEnfantQuotidienId.dateSauvgarde) = TRUNC(:date) ")
    List<RapportEnfantQuotidien> findByDateAndstatutPenalAndEtablissements(@Param("date") LocalDate date,
    		                                                               @Param("statutPenal") String statutPenal,
    		                                                               @Param("etablissements") List<Etablissement> etablissements);
	 		}


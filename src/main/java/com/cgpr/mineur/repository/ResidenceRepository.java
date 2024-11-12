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
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.ResidenceId;
import com.cgpr.mineur.models.ResidenceWithAffaires;
import com.cgpr.mineur.models.SituationFamiliale;
import com.cgpr.mineur.models.SituationSocial;
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.models.TypeTribunal;
import com.cgpr.mineur.tools.CommonConditions;

 
 

@Repository
public interface ResidenceRepository extends CrudRepository<Residence, ResidenceId> {
	
	
	   @Query("SELECT a.arrestation FROM Residence a WHERE   a.statut = 0 and a.etablissement.id  = ?1   ")
	   List<Arrestation>findByEtablissement(String et);
	
	
	
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1  and a.statut = 0 and a.arrestation.arrestationId.numOrdinale  = ?2")
	   Residence findByIdEnfantAndStatut0 (String idEnfant, long numOrdinale);
	   
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1  and a.statut = 2 and a.arrestation.arrestationId.numOrdinale  = ?2")
	   Residence findByIdEnfantAndStatutEnCour (String idEnfant, long numOrdinale);
	   
	  
	   
	   
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1  and a.arrestation.arrestationId.numOrdinale  = ?2"
			   + "and a.residenceId.numOrdinaleResidence = (select max(a.residenceId.numOrdinaleResidence) from Residence a "
				
		+ "    where a.residenceId.idEnfant = ?1   and a.arrestation. arrestationId.numOrdinale  = ?2 and a.statut = 1)")
	   Residence findMaxResidenceWithStatut1 (String idEnfant, long numOrdinale);
	   
	  
//	   and (a.statut = 0 or  a.statut = 1)
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1  and a.arrestation.statut = 0 "
	   		+ " order by a.residenceId.numOrdinaleResidence desc")
	   List<Residence>findByIdEnfantAndStatutArrestation0(String idEnfant );
	   
	   
	   @Query("SELECT a FROM Residence a WHERE a.arrestation.arrestationId.idEnfant = ?1  and a.arrestation.arrestationId.numOrdinale  = ?2 order by a.residenceId.numOrdinaleResidence desc")
	   List<Residence>findByEnfantAndArrestation(String idEnfant, long numOrdinale );
	   
	   
	   @Query("select count(a) from Residence a where a.residenceId.idEnfant  = ?1 and  a.arrestation.arrestationId.numOrdinale = ?2 and a.etablissementSortie is not null")
	    int countTotaleRecidence(String idEnfant,long numOrdinaleArrestation);
	   
	   @Query("select count(a) from Residence a where a.residenceId.idEnfant  = ?1 and  a.arrestation.arrestationId.numOrdinale = ?2 and a.etabChangeManiere is not  null")
	    int countTotaleRecidenceWithetabChangeManiere(String idEnfant,long numOrdinaleArrestation);
	   
	   
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1  and a.arrestation.arrestationId.numOrdinale  = ?2 and a.etabChangeManiere is not  null " 
			   + "and a.residenceId.numOrdinaleResidence = (select max(a.residenceId.numOrdinaleResidence) from Residence a "
				
		+ "    where a.residenceId.idEnfant = ?1   and a.arrestation. arrestationId.numOrdinale  = ?2 and a.etabChangeManiere is not  null)")
	   Residence findMaxResidenceWithEtabChangeManiere (String idEnfant, long numOrdinale);
	   
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1  and a.arrestation.arrestationId.numOrdinale  = ?2"
			   + "and a.residenceId.numOrdinaleResidence = (select max(a.residenceId.numOrdinaleResidence) from Residence a "
				
		+ "    where a.residenceId.idEnfant = ?1   and a.arrestation. arrestationId.numOrdinale  = ?2)")
	   Residence findMaxResidence (String idEnfant, long numOrdinale);
	   
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1   and "
	   
		+ "                          a.arrestation. arrestationId.numOrdinale  = ?2  and"
		
		+ "                               a.residenceId.numOrdinaleResidence = (select max(a.residenceId.numOrdinaleResidence) from Residence a "
		
		+ "                                                                 where a.residenceId.idEnfant = ?1   and a.arrestation. arrestationId.numOrdinale  = ?2)")
         Residence findByIdEnfantAndStatut0 (long idEnfant, long numOrdinale);
	   
	   
	   @Query("SELECT a  FROM Residence a WHERE a.residenceId = ?1 ")
	   Residence retourChangeManier (ResidenceId residenceId);
	   
	   
	   
	   
	  


	   


	   
	   @Query("SELECT a FROM Residence a WHERE "
			   + CommonConditions.buildChildrenCriteria +" and "
			   + "(((:dateDebutGlobale is null) and (:dateFinGlobale is null)) or (a.arrestation.enfant.dateNaissance between  :dateDebutGlobale and  :dateFinGlobale)) and "
		   		+ "  a.statut = 0 and a.etablissement   IN :etablissements and a.arrestation in "
		   		+ CommonConditions.buildDetentionCriteria  
		   		 
		   		+ "    order by a.arrestation.enfant.dateNaissance")
		List<Residence> listDetenusMajeurs(@Param("classePenale") ClassePenale classePenale,
		        @Param("niveauEducatif") NiveauEducatif niveauEducatif,
		        @Param("gouvernorat") Gouvernorat gouvernorat,
		        @Param("situationFamiliale") SituationFamiliale situationFamiliale,
		        @Param("situationSocial") SituationSocial situationSocial,
		        @Param("metier") Metier metier,
		        @Param("delegation") Delegation delegation,
		        
		        @Param("minAge") float minAge,
		        @Param("maxAge") float maxAge,
		        @Param("foreign") boolean foreign,
		        @Param("nationalite") Nationalite nationalite,

		        
		        @Param("gouvernoratTribunal") Gouvernorat gouvernoratTribunal,
		        @Param("typeTribunal") TypeTribunal typeTribunal,
		        @Param("typeAffaire") TypeAffaire typeAffaire,

		        
		        
				  @Param("etablissements") List<Etablissement> etablissements ,
				  @Param("dateDebutGlobale") @Temporal Date dateDebutGlobale,
			      @Param("dateFinGlobale")@Temporal Date dateFinGlobale);
	
	   
	   
//		   @Query("SELECT a FROM Residence a WHERE ("
//		   		    + "?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
//				   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
//				   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
//				   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
//				   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
//				   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
//				   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
//				   		+ "(?16 =  null or a.arrestation.enfant.nationalite.id != 1) and "
//				   		+ "(?17 =  0L or a.arrestation.enfant.nationalite.id = ?17) and "
//				   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
//				   		+ "(((?14 = null) and (?15 = null)) or (a.dateEntree between  ?14 and  ?15)) and "
//				   		+ "   a.etablissement   = ?8 and a.etablissementEntree != ?8 and a.arrestation in "
//				   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
//				   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
//				   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
//				   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
//				   		 
//				   		+ " )  order by a.numArrestation")
//		List<Residence> findByEntreeMutation(long classePenale,  long niveauEducatif, long gouvernorat, 
//		long situationFamiliale, long situationSocial, long metier, 
//		long delegation, Etablissement etablissement, long gouvernoratTribunal  ,long typeTribunal  ,
//		long typeAffaire ,@Temporal Date start, @Temporal Date end ,@Temporal Date dateDebutGlobale, 
//		@Temporal Date dateFinGlobale,String etranger, long nationalite);
//	   
//	   
//	   
//	   @Query("SELECT a FROM Residence a WHERE    (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
//		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
//		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
//		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
//		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
//		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
//		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
//		   		+ "(?16 = null or a.arrestation.enfant.nationalite.id != 1) and "
//		   		+ "(?17 =  0L or a.arrestation.enfant.nationalite.id = ?17) and "
//		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
//		   		+ "(((?14 = null) and (?15 = null)) or (a.dateSortie between  ?14 and  ?15)) and "
//		   		+ "   a.etablissement   = ?8 and a.etablissementSortie   != ?8 and a.arrestation in "
//		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
//		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
//		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
//		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
//		   		 
//		   		+ " )  order by a.numArrestation")
//List<Residence> findBySortieMutation(long classePenale,  long niveauEducatif, long gouvernorat, 
//long situationFamiliale, long situationSocial, long metier, 
//long delegation, Etablissement etablissement, long gouvernoratTribunal  ,long typeTribunal  ,
//long typeAffaire ,@Temporal Date start, @Temporal Date end ,@Temporal Date dateDebutGlobale,
//@Temporal Date dateFinGlobale,String etranger, long nationalite);
	 
	   @Query("SELECT a FROM Residence a WHERE "
	   		 + CommonConditions.buildChildrenCriteria +" and "
		   		+ "(((:dateDebutGlobale is null) and (:dateFinGlobale is null)) or (a.arrestation.date between  :dateDebutGlobale and  :dateFinGlobale)) and "
		   		+ " a.residenceId.numOrdinaleResidence = 1  and  "
		   		+ "  a.etablissement   IN :etablissements and a.arrestation in "
		   		+ CommonConditions.buildDetentionCriteria  
		   		+ "  order by a.arrestation.date, a.numArrestation")
List<Residence> listDetenusEntreReelles(
		@Param("classePenale") ClassePenale classePenale,
        @Param("niveauEducatif") NiveauEducatif niveauEducatif,
        @Param("gouvernorat") Gouvernorat gouvernorat,
        @Param("situationFamiliale") SituationFamiliale situationFamiliale,
        @Param("situationSocial") SituationSocial situationSocial,
        @Param("metier") Metier metier,
        @Param("delegation") Delegation delegation,
        
        @Param("minAge") float minAge,
        @Param("maxAge") float maxAge,
        @Param("foreign") boolean foreign,
        @Param("nationalite") Nationalite nationalite,

        
        @Param("gouvernoratTribunal") Gouvernorat gouvernoratTribunal,
        @Param("typeTribunal") TypeTribunal typeTribunal,
        @Param("typeAffaire") TypeAffaire typeAffaire,

        
        
		  @Param("etablissements") List<Etablissement> etablissements, 
		  @Param("dateDebutGlobale") @Temporal Date dateDebutGlobale,
	      @Param("dateFinGlobale")@Temporal Date dateFinGlobale);

	   
	   @Query("SELECT a FROM Residence a WHERE    "
			   + CommonConditions.buildChildrenCriteria +" and "
			   + "(((:dateDebutGlobale is NULL ) and (:dateFinGlobale is NULL)) or (a.dateEntree between  :dateDebutGlobale and  :dateFinGlobale)) and "
			   		+ "   a.etablissement   IN :etablissements and "			   	
			   		+ " a.etablissementEntree   != :etablissement "
		   		+ "and a.arrestation IN  "+ CommonConditions.buildDetentionCriteria +" ORDER BY a.etablissement.id , a.numArrestation ")
		List<Residence> listDetenusEntreMutations  ( @Param("classePenale") ClassePenale classePenale,
		           @Param("niveauEducatif") NiveauEducatif niveauEducatif,
		           @Param("gouvernorat") Gouvernorat gouvernorat,
		           @Param("situationFamiliale") SituationFamiliale situationFamiliale,
		           @Param("situationSocial") SituationSocial situationSocial,
		           @Param("metier") Metier metier,
		           @Param("delegation") Delegation delegation,
		           
		           @Param("minAge") float minAge,
		           @Param("maxAge") float maxAge,
		           @Param("foreign") boolean foreign,
		           @Param("nationalite") Nationalite nationalite,
	  
		           
		           @Param("gouvernoratTribunal") Gouvernorat gouvernoratTribunal,
		           @Param("typeTribunal") TypeTribunal typeTribunal,
		           @Param("typeAffaire") TypeAffaire typeAffaire,
	  
		           
		           
	    		  @Param("etablissements")List<Etablissement> etablissements, 
				  @Param("dateDebutGlobale") @Temporal Date dateDebutGlobale,
			      @Param("dateFinGlobale")@Temporal Date dateFinGlobale);
	   
	   
	   
	   @Query("SELECT a FROM Residence a WHERE    "
			   + CommonConditions.buildChildrenCriteria +" and "
			   + "(((:dateDebutGlobale is NULL ) and (:dateFinGlobale is NULL)) or (a.dateSortie between  :dateDebutGlobale and  :dateFinGlobale)) and "
			   		+ "   a.etablissement   IN :etablissements and "			   	
			   		+ " a.etablissementSortie   != :etablissement "
		   		+ "and a.arrestation IN  "+ CommonConditions.buildDetentionCriteria +" ORDER BY a.etablissement.id , a.numArrestation ")
	   
      List<Residence> listDetenusSortieMutations(
    		  @Param("classePenale") ClassePenale classePenale,
	           @Param("niveauEducatif") NiveauEducatif niveauEducatif,
	           @Param("gouvernorat") Gouvernorat gouvernorat,
	           @Param("situationFamiliale") SituationFamiliale situationFamiliale,
	           @Param("situationSocial") SituationSocial situationSocial,
	           @Param("metier") Metier metier,
	           @Param("delegation") Delegation delegation,
	           
	           @Param("minAge") float minAge,
	           @Param("maxAge") float maxAge,
	           @Param("foreign") boolean foreign,
	           @Param("nationalite") Nationalite nationalite,
  
	           
	           @Param("gouvernoratTribunal") Gouvernorat gouvernoratTribunal,
	           @Param("typeTribunal") TypeTribunal typeTribunal,
	           @Param("typeAffaire") TypeAffaire typeAffaire,
  
	           
	           
    		  @Param("etablissements")List<Etablissement> etablissements, 
			  @Param("dateDebutGlobale") @Temporal Date dateDebutGlobale,
		      @Param("dateFinGlobale")@Temporal Date dateFinGlobale);
	   
	   
	   
	   @Query("SELECT a FROM Residence a WHERE "
			   
	+ "a.arrestation   in (SELECT aff.arrestation FROM Affaire aff where "
		+ "(EXISTS (   SELECT d FROM aff.documents d WHERE d.dateEmission between ?14 and  ?15 )  )"
		+ " ) and"
	
			   
			   
			   
	   		    + "(?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?16 = null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+ "(?17 =  0L or a.arrestation.enfant.nationalite.id = ?17) and "
		   		 
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		 
		   		+ " a.residenceId.numOrdinaleResidence = 1  and  "
		   		+ "  a.etablissement   IN ?8 and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		 
			   		+ " )  order by a.arrestation.date, a.numArrestation")
	List<Residence> listAudiences(long classePenale,  long niveauEducatif, long gouvernorat, 
	long situationFamiliale, long situationSocial, long metier, 
	long delegation, List<Etablissement> etablissements, long gouvernoratTribunal  ,long typeTribunal  ,long typeAffaire ,
	@Temporal Date start, @Temporal Date end ,@Temporal Date dateDebutGlobale, @Temporal Date dateFinGlobale,
	String etranger , long nationalite );
	   
	   
	   
	   
	   @Query("SELECT a FROM Residence a WHERE "
 
		   		+ "  a.statut = 0 and a.etablissement   IN :etablissements and a.arrestation not in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff"
		   	 
		   		 
		   		+ " )  order by a.numArrestation")
List<Residence> listDetenusSansAffaires(  List<Etablissement> etablissements  );

	   
	   
	   /**
	    * Récupère la liste des résidences pour les enfants existants en fonction de différents critères.
	    *
	    * @param classePenale          La classe pénale de l'enfant.
	    * @param niveauEducatif        Le niveau éducatif de l'enfant.
	    * @param gouvernorat           Le gouvernorat de l'enfant.
	    * @param situationFamiliale    La situation familiale de l'enfant.
	    * @param situationSocial       La situation sociale de l'enfant.
	    * @param metier                Le métier de l'enfant.
	    * @param delegation            La délégation de l'enfant.
	    * @param etablissement         L'établissement lié à la résidence.
	    * @param gouvernoratTribunal   Le gouvernorat du tribunal lié à l'affaire.
	    * @param typeTribunal          Le type de tribunal lié à l'affaire.
	    * @param typeAffaire           Le type d'affaire lié à l'affaire.
	    * @param minAge                L'âge minimum en années.
	    * @param maxAge                L'âge maximum en années.
	    * @param foreign               Indique si l'enfant a une nationalité étrangère.
	    * @param nationalite           La nationalité de l'enfant.
	    * @return                      La liste des résidences pour les enfants existants.
	    */
	   @Query("SELECT a FROM Residence a WHERE "
	           + "(:classePenale IS NULL OR a.arrestation.enfant.classePenale = :classePenale) AND "
	           + "(:niveauEducatif IS NULL OR a.arrestation.enfant.niveauEducatif = :niveauEducatif) AND "
	           + "(:gouvernorat IS NULL OR a.arrestation.enfant.gouvernorat = :gouvernorat) AND "
	           + "(:situationFamiliale IS NULL OR a.arrestation.enfant.situationFamiliale = :situationFamiliale) AND "
	           + "(:situationSocial IS NULL OR a.arrestation.enfant.situationSocial = :situationSocial) AND "
	           + "(:metier IS NULL OR a.arrestation.enfant.metier = :metier) AND "
	           + "(:delegation IS NULL OR a.arrestation.enfant.delegation = :delegation) AND "
	           + "(:foreign = true OR a.arrestation.enfant.nationalite.id != 1) AND "
	           + "(:nationalite IS NULL OR a.arrestation.enfant.nationalite = :nationalite) AND "
	           + "((:minAge = 0.0F OR :maxAge = 0.0F) OR (TRUNC(months_between(CURRENT_DATE,a.arrestation.enfant.dateNaissance)/12) BETWEEN :minAge AND :maxAge)) AND "
	           + "a.statut = 0 AND a.etablissement IN :etablissements AND a.arrestation IN "
	           + "("
	           + "    SELECT aff.arrestation FROM Affaire aff WHERE aff.statut = 0 AND "
	           + "    (:gouvernoratTribunal IS NULL OR aff.tribunal.gouvernorat = :gouvernoratTribunal) AND "
	           + "    (:typeTribunal IS NULL OR aff.tribunal.typeTribunal = :typeTribunal) AND "
	           + "    (:typeAffaire IS NULL OR aff.typeAffaire = :typeAffaire)"
	           + ") "
	           + "ORDER BY a.etablissement.id , a.numArrestation")
	   List<Residence> listAllDetenus(
	           @Param("classePenale") ClassePenale classePenale,
	           @Param("niveauEducatif") NiveauEducatif niveauEducatif,
	           @Param("gouvernorat") Gouvernorat gouvernorat,
	           @Param("situationFamiliale") SituationFamiliale situationFamiliale,
	           @Param("situationSocial") SituationSocial situationSocial,
	           @Param("metier") Metier metier,
	           @Param("delegation") Delegation delegation,
	           @Param("etablissements") List<Etablissement> etablissements,
	           @Param("gouvernoratTribunal") Gouvernorat gouvernoratTribunal,
	           @Param("typeTribunal") TypeTribunal typeTribunal,
	           @Param("typeAffaire") TypeAffaire typeAffaire,
	           @Param("minAge") float minAge,
	           @Param("maxAge") float maxAge,
	           @Param("foreign") boolean foreign,
	           @Param("nationalite") Nationalite nationalite);

	   
	   
	   @Query("SELECT a FROM Residence a WHERE "
			   + "a.arrestation not in (SELECT aff.arrestation FROM Affaire aff where "
			   		   		+ "(aff.typeDocument IN :documentTypes) and aff.statut = 0)"
			   		   		+ "  and a.arrestation in (SELECT aff.arrestation FROM Affaire aff ) and "
                              + CommonConditions.buildChildrenCriteria +" and "
			   	             + "a.statut = 0 AND a.etablissement IN :etablissements AND a.arrestation IN  "+CommonConditions.buildDetentionCriteria
			   	           
			   	           + "ORDER BY a.etablissement.id , a.numArrestation")
		   List<Residence> listDetenusJuges (
				   @Param("documentTypes") List<String> documentTypes,
		           @Param("classePenale") ClassePenale classePenale,
		           @Param("niveauEducatif") NiveauEducatif niveauEducatif,
		           @Param("gouvernorat") Gouvernorat gouvernorat,
		           @Param("situationFamiliale") SituationFamiliale situationFamiliale,
		           @Param("situationSocial") SituationSocial situationSocial,
		           @Param("metier") Metier metier,
		           @Param("delegation") Delegation delegation,
		           
		           @Param("minAge") float minAge,
		           @Param("maxAge") float maxAge,
		           @Param("foreign") boolean foreign,
		           @Param("nationalite") Nationalite nationalite,
	   
		           
		           @Param("gouvernoratTribunal") Gouvernorat gouvernoratTribunal,
		           @Param("typeTribunal") TypeTribunal typeTribunal,
		           @Param("typeAffaire") TypeAffaire typeAffaire,
	   
		           @Param("etablissements") List<Etablissement> etablissements );
		        
	   
	   
	   
	   
//	   @Query("SELECT new com.cgpr.mineur.models.ResidenceWithAffaires(a, " +
//		       "(SELECT   aff  FROM Affaire aff WHERE aff.arrestation.arrestationId = a.arrestation.arrestationId AND " +  
//		       "aff.typeDocument IN :documentTypes AND aff.statut = 0)) " +  
//		       "FROM Residence a WHERE "  
//		       + "a.arrestation  in (SELECT aff.arrestation FROM Affaire aff where "
//  		   		+ "(aff.typeDocument IN :documentTypes )"
//  		   		+ " and aff.statut = 0) and"
//  		   	+ CommonConditions.buildChildrenCriteria +" and "
//  	             + "a.statut = 0 AND  (a.etablissement IN :etablissements )  AND  "
//  	          + "(((:dateDebutGlobale is NULL ) and (:dateFinGlobale is NULL)) or (a.arrestation.date between  :dateDebutGlobale and  :dateFinGlobale)) and "
//  	             + " a.arrestation IN " +CommonConditions.buildDetentionCriteria
//  	         
//  	           + "ORDER BY a.etablissement.id , a.numArrestation")
//	   List<ResidenceWithAffaires> findByAllEnfantExistArretWithAffaire(
//			   @Param("documentTypes") List<String> documentTypes,
//	           @Param("classePenale") ClassePenale classePenale,
//	           @Param("niveauEducatif") NiveauEducatif niveauEducatif,
//	           @Param("gouvernorat") Gouvernorat gouvernorat,
//	           @Param("situationFamiliale") SituationFamiliale situationFamiliale,
//	           @Param("situationSocial") SituationSocial situationSocial,
//	           @Param("metier") Metier metier,
//	           @Param("delegation") Delegation delegation,
//	           
//	           @Param("minAge") float minAge,
//	           @Param("maxAge") float maxAge,
//	           @Param("foreign") boolean foreign,
//	           @Param("nationalite") Nationalite nationalite,
//   
//	           
//	           @Param("gouvernoratTribunal") Gouvernorat gouvernoratTribunal,
//	           @Param("typeTribunal") TypeTribunal typeTribunal,
//	           @Param("typeAffaire") TypeAffaire typeAffaire,
//   
//	           @Param("etablissements") List<Etablissement> etablissements ,
//	           @Param("dateDebutGlobale")	@Temporal Date dateDebutGlobale,
//	           @Param("dateFinGlobale")	@Temporal Date dateFinGlobale  );  
//	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
   //   a.arrestation.date <= :targetDate AND
	   @Query("SELECT a FROM Residence a WHERE "
			   + "a.arrestation  in (SELECT aff.arrestation FROM Affaire aff where "
			   		   		+ "(aff.typeDocument IN :documentTypes )"
			   		   		+ " and aff.statut = 0) and"
			   		   	+ CommonConditions.buildChildrenCriteria +" and "
			   	             + "a.statut = 0 AND  (a.etablissement IN :etablissements )  AND  "
			   	          + "(((:dateDebutGlobale is NULL ) and (:dateFinGlobale is NULL)) or (a.arrestation.date between  :dateDebutGlobale and  :dateFinGlobale)) and "
			   	             + " a.arrestation IN " +CommonConditions.buildDetentionCriteria
			   	         
			   	           + "ORDER BY a.etablissement.id , a.numArrestation")
		   List<Residence> listDetenusArretes(
				   @Param("documentTypes") List<String> documentTypes,
		           @Param("classePenale") ClassePenale classePenale,
		           @Param("niveauEducatif") NiveauEducatif niveauEducatif,
		           @Param("gouvernorat") Gouvernorat gouvernorat,
		           @Param("situationFamiliale") SituationFamiliale situationFamiliale,
		           @Param("situationSocial") SituationSocial situationSocial,
		           @Param("metier") Metier metier,
		           @Param("delegation") Delegation delegation,
		           
		           @Param("minAge") float minAge,
		           @Param("maxAge") float maxAge,
		           @Param("foreign") boolean foreign,
		           @Param("nationalite") Nationalite nationalite,
	   
		           
		           @Param("gouvernoratTribunal") Gouvernorat gouvernoratTribunal,
		           @Param("typeTribunal") TypeTribunal typeTribunal,
		           @Param("typeAffaire") TypeAffaire typeAffaire,
	   
		           @Param("etablissements") List<Etablissement> etablissements ,
		           @Param("dateDebutGlobale")	@Temporal Date dateDebutGlobale,
		           @Param("dateFinGlobale")	@Temporal Date dateFinGlobale  ); // Ajoutez ce paramètre


	   
	   @Query("SELECT  a FROM Residence a WHERE "
				+ CommonConditions.buildChildrenCriteria +" and "
		   		
		   		+"(a.arrestation.liberation is not null) and " 
		   		+ "(:causeLiberation is NULL OR a.arrestation.liberation.causeLiberation  = :causeLiberation) and "
		   		+ "(((:dateDebutGlobale is NULL ) and (:dateFinGlobale is NULL)) or (a.arrestation.liberation.date between  :dateDebutGlobale and  :dateFinGlobale)) and "
		   		
		   		
		   		+ "  a.statut = 1 and a.etablissement   IN :etablissements and a.etablissementSortie = null "
		   		
		   		+ "and a.arrestation in" +CommonConditions.buildDetentionCriteria  
		   		+"order by a.arrestation.liberation.date , a.numArrestation")
	   
	   
	List<Residence> listDetenusLiberes(
			 
	           @Param("classePenale") ClassePenale classePenale,
	           @Param("niveauEducatif") NiveauEducatif niveauEducatif,
	           @Param("gouvernorat") Gouvernorat gouvernorat,
	           @Param("situationFamiliale") SituationFamiliale situationFamiliale,
	           @Param("situationSocial") SituationSocial situationSocial,
	           @Param("metier") Metier metier,
	           @Param("delegation") Delegation delegation,
	           
	           @Param("minAge") float minAge,
	           @Param("maxAge") float maxAge,
	           @Param("foreign") boolean foreign,
	           @Param("nationalite") Nationalite nationalite,

	           
	           @Param("gouvernoratTribunal") Gouvernorat gouvernoratTribunal,
	           @Param("typeTribunal") TypeTribunal typeTribunal,
	           @Param("typeAffaire") TypeAffaire typeAffaire,

	           @Param("etablissements") List<Etablissement> etablissements,
	           
	           
	           @Param("dateDebutGlobale")	@Temporal Date dateDebutGlobale,
	           @Param("dateFinGlobale")	@Temporal Date dateFinGlobale,
			   @Param("causeLiberation")	CauseLiberation causeLiberation );
	   
	   
	   
	   
	   
	   
	   
//	   @Query("SELECT a FROM Residence a WHERE "
//		   		+ "a.arrestation   in (SELECT aff.arrestation FROM Affaire aff where "
//		   		+ "(aff.typeDocument = 'AP'  )"
//		   		+ " and aff.statut = 0) and"
//		   		+" (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
//		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
//		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
//		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
//		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
//		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
//		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
//		   		+ "(?14 =  null or a.arrestation.enfant.nationalite.id != 1) and "
//		   		+ "(?15 =  0L or a.arrestation.enfant.nationalite.id = ?15) and "
//		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
//		   		+ " a.statut = 0 and a.etablissement.id  = ?8 "
//		   		+"and a.arrestation in "
//		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
//		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
//		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
//		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
//		   		+ " )" 
//		   		+ " order by a.numArrestation")
//		   List<Residence> findByAllEnfantExistArretAP(long classePenale,  long niveauEducatif, long gouvernorat, 
//				   long situationFamiliale, long situationSocial, long metier, 
//				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,long typeTribunal  ,
//				   long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger, long nationalite);
	   
//	   @Query("SELECT a FROM Residence a WHERE "
//		   		+ "a.arrestation   in (SELECT aff.arrestation FROM Affaire aff where "
//		   		+ "(aff.typeDocument = 'AE'  )"
//		   		+ " and aff.statut = 0) and"
//		   		+" (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
//		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
//		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
//		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
//		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
//		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
//		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
//		   		+ "(?14 =  null or a.arrestation.enfant.nationalite.id != 1) and "
//		   		+ "(?15 =  0L or a.arrestation.enfant.nationalite.id = ?15) and "
//		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
//		   		+ " a.statut = 0 and a.etablissement.id  = ?8 "
//		   		+"and a.arrestation in "
//		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
//		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
//		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
//		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
//		   		+ " )" 
//		   		+ " order by a.numArrestation")
//		   List<Residence> findByAllEnfantExistArretAE(long classePenale,  long niveauEducatif, long gouvernorat, 
//				   long situationFamiliale, long situationSocial, long metier, 
//				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,long typeTribunal  ,
//				   long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger, long nationalite);
	   
//	   @Query("SELECT a FROM Residence a WHERE "
//		   		+ "a.arrestation  in (SELECT aff.arrestation FROM Affaire aff where "
//		   		+ "(aff.typeDocument = 'T' )"
//		   		+ " and aff.statut = 0) and"
//		   		+" (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
//		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
//		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
//		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
//		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
//		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
//		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
//		   		+ "(?14 =  null or a.arrestation.enfant.nationalite.id != 1) and "
//		   		+ "(?15 =  0L or a.arrestation.enfant.nationalite.id = ?15) and "
//		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
//		   		+ " a.statut = 0 and a.etablissement.id  = ?8 "
//		   		+"and a.arrestation in "
//		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
//		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
//		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
//		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
//		   		+ " )" 
//		   		+ " order by a.numArrestation")
//		   List<Residence> findByAllEnfantExistArretT(long classePenale,  long niveauEducatif, long gouvernorat, 
//				   long situationFamiliale, long situationSocial, long metier, 
//				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,
//				   long typeTribunal  ,long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger , long nationalite);
	   
	   @Query("SELECT a FROM Residence a WHERE "
		   		+ "a.arrestation  in (SELECT aff.arrestation FROM Affaire aff where "
		   		+ "(aff.typeDocument = 'CR')"
		   		+ " and aff.statut = 0) and"
		   		+" (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?14 =  null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+ "(?15 =  0L or a.arrestation.enfant.nationalite.id = ?15) and "
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ " a.statut = 0 and a.etablissement.id  = ?8 "
		   		+"and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		+ " )" 
		   		+ " order by a.numArrestation")
		   List<Residence> listDetenusJugeRevus(long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,
				   long typeTribunal  ,long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger, long nationalite);
	   
	   
		
	   
	   
	   
	   
//	   + "(((?14 = null) and (?15 = null)) or (max(aff.dateFinPunition) between  ?14 and  ?15)) and "
	   @Query("SELECT a FROM Residence a WHERE "
		   		+ "a.arrestation not in (SELECT aff.arrestation FROM Affaire aff where "
		   		+ "(aff.typeDocument in :documentTypes  )"
		   		+ " and aff.statut = 0) and "
		   		+ "a.arrestation  in (SELECT aff.arrestation FROM Affaire aff where "
		   		+ "(((:dateDebutGlobale is null) and (:dateFinGlobale is null)) or ((select max(a.dateFinPunition)  FROM Affaire a WHERE a.arrestation = aff.arrestation) between :dateDebutGlobale and :dateFinGlobale))"
		   		+ " and aff.statut = 0 ) and "
		   		+ "a.arrestation in (SELECT aff.arrestation FROM Affaire aff ) and "
		   		+ CommonConditions.buildChildrenCriteria +" and "
		   		
		   		+ " a.statut = 0 and a.etablissement   IN :etablissements "
		   		
		   		+"and a.arrestation in "+CommonConditions.buildDetentionCriteria  
		   		
		   		+ " order by a.numArrestation")
		   List<Residence> listDetenusSeraLiberes (
				   @Param("documentTypes") List<String> documentTypes,
				   @Param("classePenale") ClassePenale classePenale,
		           @Param("niveauEducatif") NiveauEducatif niveauEducatif,
		           @Param("gouvernorat") Gouvernorat gouvernorat,
		           @Param("situationFamiliale") SituationFamiliale situationFamiliale,
		           @Param("situationSocial") SituationSocial situationSocial,
		           @Param("metier") Metier metier,
		           @Param("delegation") Delegation delegation,
		           
		           @Param("minAge") float minAge,
		           @Param("maxAge") float maxAge,
		           @Param("foreign") boolean foreign,
		           @Param("nationalite") Nationalite nationalite,

		           
		           @Param("gouvernoratTribunal") Gouvernorat gouvernoratTribunal,
		           @Param("typeTribunal") TypeTribunal typeTribunal,
		           @Param("typeAffaire") TypeAffaire typeAffaire,

		           @Param("etablissements") List<Etablissement> etablissements,
		           
		           
		           @Param("dateDebutGlobale")	@Temporal Date dateDebutGlobale,
		           @Param("dateFinGlobale")	@Temporal Date dateFinGlobale);
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
//	   
//	   @Async
//	   @Query("SELECT r FROM Residence r WHERE r.residenceId IN (SELECT v.residenceId FROM ResidenceJugeView v WHERE v.etaFK = :etablissementId) ")
//	   CompletableFuture<List<Residence>> findByAllEnfantExistJugeAsync(@Param("etablissementId") String etablissementId);
//	    @Async
//	   @Query("SELECT r FROM Residence r WHERE r.residenceId IN (SELECT v.residenceId FROM ResidenceArretView v WHERE v.etaFK = :etablissementId) ")
//	   CompletableFuture<List<Residence>> findByAllEnfantExistArretAsync(String etablissementId);
//	   
//	   
	   
	   
	   
//	   @Async
//	   @Query("SELECT r FROM Residence r JOIN ResidenceJugeView v ON r.residenceId = v.residenceId WHERE v.etaFK = :etablissementId")
//	   CompletableFuture<List<Residence>> findByAllEnfantExistJugeAsync(@Param("etablissementId") String etablissementId);
//	   
	   
	   
	   
	   @Async
	   @Query("SELECT a FROM Residence a WHERE " +
	           "a.arrestation NOT IN (SELECT aff.arrestation FROM Affaire aff WHERE " +
	           "(aff.typeDocument IN ('CH', 'CD', 'CJA', 'T', 'CP', 'AP', 'AE')) " +
	           "AND aff.statut = 0) " +
	           "AND a.arrestation IN (SELECT aff.arrestation FROM Affaire aff) " +
	           "AND a.statut = 0 " +
	           "AND a.etablissement.id = :etablissementId " +
	           "AND a.arrestation IN (SELECT aff.arrestation FROM Affaire aff WHERE aff.statut = 0) and a.arrestation.date < :dateLimite " +
	           "ORDER BY a.etablissement.id , a.numArrestation")
	   CompletableFuture<List<Residence>> findByAllEnfantExistJugeAsync(@Param("etablissementId") String etablissementId, @Param("dateLimite") java.sql.Date dateLimite);
	   
	   
	   
	   
	   
	   
//	   @Async
//	   @Query("SELECT r FROM Residence r JOIN ResidenceArretView v ON r.residenceId = v.residenceId WHERE v.etaFK = :etablissementId")
//	   CompletableFuture<List<Residence>> findByAllEnfantExistArretAsync(@Param("etablissementId") String etablissementId);
	   
	   @Async
	   @Query("SELECT a FROM Residence a WHERE " +
	           "a.arrestation IN (SELECT aff.arrestation FROM Affaire aff WHERE " +
	           "(aff.typeDocument IN ('CH', 'CD', 'CJA', 'T', 'CP', 'AP', 'AE')) " +
	           "AND aff.statut = 0) " +
	           "AND a.statut = 0 " +
	           "AND a.etablissement.id = :etablissementId " +
	           "AND a.arrestation IN (SELECT aff.arrestation FROM Affaire aff WHERE aff.statut = 0) and a.arrestation.date < :dateLimite " +
	           "ORDER BY a.etablissement.id , a.numArrestation")
	   CompletableFuture<List<Residence>> findByAllEnfantExistArretAsync(@Param("etablissementId") String etablissementId, @Param("dateLimite") java.sql.Date dateLimite);
	   
	   
	   @Async
	   @Query("SELECT  a FROM Residence a WHERE (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?16 =  null or a.arrestation.enfant.nationalite.id != 1) and "
		   	//	+ "(a.etabChangeManiere is null) and "
		   		+"(a.arrestation.liberation is not null) and " 
		   		+ "(((?14 = null) and (?15 = null)) or (a.arrestation.liberation.date between  ?14 and  ?15)) and "
		   		
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ "  a.statut = 1 and a.etablissement   = ?8 and a.etablissementSortie = null and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		 
		   		+ " )  order by a.arrestation.liberation.date , a.numArrestation")
	   CompletableFuture<List<Residence>>  findByAllEnfantLibereAsync(long classePenale,  long niveauEducatif, long gouvernorat, 
	long situationFamiliale, long situationSocial, long metier, 
	long delegation, Etablissement etablissement, long gouvernoratTribunal  , 
	long typeTribunal  ,long typeAffaire ,
	@Temporal Date start, @Temporal Date end ,@Temporal Date dateDebutGlobale, @Temporal Date dateFinGlobale,String etranger);



	   
	   @Query("SELECT COUNT(a) FROM Residence a WHERE a.statut = 0 " 
			   + "AND a.arrestation.liberation IS NULL "
	           + "AND a.numArrestation = :numeroEcrou "
	           + "AND a.etablissement.id = :etablissementId "
		       )
		long validerNumeroEcrou(@Param("numeroEcrou") String numeroEcrou,
		                                 @Param("etablissementId") String etablissementId);


	 
	   
	   
	   
	   
	   
	   
	   
	 		}


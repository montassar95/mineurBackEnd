package com.cgpr.mineur.repository;

 
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.ClassePenale;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.ResidenceId;

 
 

@Repository
public interface ResidenceRepository extends CrudRepository<Residence, ResidenceId> {
	
	
	   @Query("SELECT a.arrestation FROM Residence a WHERE   a.statut = 0 and a.etablissement.id  = ?1   ")
	   List<Arrestation>findByEtablissement(String et);
	
	
	
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1  and a.statut = 0 and a.arrestation.arrestationId.numOrdinale  = ?2")
	   Residence findByIdEnfantAndStatut0 (String idEnfant, long numOrdinale);
	   
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1  and a.arrestation.arrestationId.numOrdinale  = ?2"
			   + "and a.residenceId.numOrdinaleResidence = (select max(a.residenceId.numOrdinaleResidence) from Residence a "
				
		+ "    where a.residenceId.idEnfant = ?1   and a.arrestation. arrestationId.numOrdinale  = ?2)")
	   Residence findMaxResidence (String idEnfant, long numOrdinale);
	   
	   
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1  and a.arrestation.arrestationId.numOrdinale  = ?2"
			   + "and a.residenceId.numOrdinaleResidence = (select max(a.residenceId.numOrdinaleResidence) from Residence a "
				
		+ "    where a.residenceId.idEnfant = ?1   and a.arrestation. arrestationId.numOrdinale  = ?2 and a.statut = 1)")
	   Residence findMaxResidenceWithStatut1 (String idEnfant, long numOrdinale);
	   
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1  and a.statut = 2 and a.arrestation.arrestationId.numOrdinale  = ?2")
	   Residence findByIdEnfantAndStatutEnCour (String idEnfant, long numOrdinale);
//	   and (a.statut = 0 or  a.statut = 1)
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1  and a.arrestation.statut = 0 "
	   		+ " order by a.residenceId.numOrdinaleResidence desc")
	   List<Residence>findByIdEnfantAndStatutArrestation0(String idEnfant );
	   
	   
	   @Query("SELECT a FROM Residence a WHERE a.arrestation.arrestationId.idEnfant = ?1  and a.arrestation.arrestationId.numOrdinale  = ?2 order by a.residenceId.numOrdinaleResidence desc")
	   List<Residence>findByEnfantAndArrestation(String idEnfant, long numOrdinale );
	   
	   
	   @Query("select count(a) from Residence a where a.residenceId.idEnfant  = ?1 and  a.arrestation.arrestationId.numOrdinale = ?2 and a.etabChangeManiere = null")
	    int countTotaleRecidence(String idEnfant,long numOrdinaleArrestation);
	   
	   @Query("select count(a) from Residence a where a.residenceId.idEnfant  = ?1 and  a.arrestation.arrestationId.numOrdinale = ?2 and a.etabChangeManiere is not  null")
	    int countTotaleRecidenceWithetabChangeManiere(String idEnfant,long numOrdinaleArrestation);
	   
	   
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1  and a.arrestation.arrestationId.numOrdinale  = ?2 and a.etabChangeManiere is not  null " 
			   + "and a.residenceId.numOrdinaleResidence = (select max(a.residenceId.numOrdinaleResidence) from Residence a "
				
		+ "    where a.residenceId.idEnfant = ?1   and a.arrestation. arrestationId.numOrdinale  = ?2 and a.etabChangeManiere is not  null)")
	   Residence findMaxResidenceWithEtabChangeManiere (String idEnfant, long numOrdinale);
	   
	   
	   
	   @Query("SELECT a FROM Residence a WHERE a.residenceId.idEnfant = ?1   and "
	   
		+ "                          a.arrestation. arrestationId.numOrdinale  = ?2  and"
		
		+ "                               a.residenceId.numOrdinaleResidence = (select max(a.residenceId.numOrdinaleResidence) from Residence a "
		
		+ "                                                                 where a.residenceId.idEnfant = ?1   and a.arrestation. arrestationId.numOrdinale  = ?2)")
         Residence findByIdEnfantAndStatut0 (long idEnfant, long numOrdinale);
	   
	   
	   @Query("SELECT a  FROM Residence a WHERE a.residenceId = ?1 ")
	   Residence retourChangeManier (ResidenceId residenceId);
	   
	   @Query("SELECT a FROM Residence a WHERE (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
									   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
									   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
									   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
									   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
									   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
									   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
									   		+ "(?14 =  null or a.arrestation.enfant.nationalite.id != 1) and "
									   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
									   		+ "  a.statut = 0 and a.etablissement   = ?8 and a.arrestation in "
									   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
									   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
									   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
									   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
									   		 
									   		+ " )  order by a.numArrestation")
	   List<Residence> findByAllEnfantExist(long classePenale,  long niveauEducatif, long gouvernorat, 
			   long situationFamiliale, long situationSocial, long metier, 
			   long delegation, Etablissement etablissement, long gouvernoratTribunal  ,long typeTribunal  ,
			   long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger );
	   
	   
	   @Query("SELECT a FROM Residence a WHERE (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?14 = null or a.arrestation.enfant.nationalite.id = 1) and "
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ "  a.statut = 0 and a.etablissement   = ?8 and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		 
		   		+ " )  order by a.arrestation.enfant.dateNaissance")
		List<Residence> findByAllEnfantDevenuMajeur(long classePenale,  long niveauEducatif, long gouvernorat, 
		long situationFamiliale, long situationSocial, long metier, 
		long delegation, Etablissement etablissement, long gouvernoratTribunal  ,long typeTribunal  ,
		long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger );
	
	   
	   
		   @Query("SELECT a FROM Residence a WHERE (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
				   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
				   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
				   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
				   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
				   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
				   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
				   		+ "(?16 =  null or a.arrestation.enfant.nationalite.id != 1) and "
				   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
				   		+ "(((?14 = null) and (?15 = null)) or (a.dateEntree between  ?14 and  ?15)) and "
				   		+ "   a.etablissement   = ?8 and a.etablissementEntree != ?8 and a.arrestation in "
				   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
				   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
				   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
				   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
				   		 
				   		+ " )  order by a.numArrestation")
		List<Residence> findByEntreeMutation(long classePenale,  long niveauEducatif, long gouvernorat, 
		long situationFamiliale, long situationSocial, long metier, 
		long delegation, Etablissement etablissement, long gouvernoratTribunal  ,long typeTribunal  ,
		long typeAffaire ,@Temporal Date start, @Temporal Date end ,@Temporal Date dateDebutGlobale, @Temporal Date dateFinGlobale,String etranger);
	   
	   
	   
	   @Query("SELECT a FROM Residence a WHERE    (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?16 = null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ "(((?14 = null) and (?15 = null)) or (a.dateSortie between  ?14 and  ?15)) and "
		   		+ "   a.etablissement   = ?8 and a.etablissementSortie   != ?8 and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		 
		   		+ " )  order by a.numArrestation")
List<Residence> findBySortieMutation(long classePenale,  long niveauEducatif, long gouvernorat, 
long situationFamiliale, long situationSocial, long metier, 
long delegation, Etablissement etablissement, long gouvernoratTribunal  ,long typeTribunal  ,
long typeAffaire ,@Temporal Date start, @Temporal Date end ,@Temporal Date dateDebutGlobale, @Temporal Date dateFinGlobale,String etranger);
	 
	   @Query("SELECT a FROM Residence a WHERE (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?16 = null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ "(((?14 = null) and (?15 = null)) or (a.arrestation.date between  ?14 and  ?15)) and "
		   		+ " a.residenceId.numOrdinaleResidence = 1  and  "
		   		+ "  a.etablissement   = ?8 and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		 
		   		+ " )  order by a.arrestation.date, a.numArrestation")
List<Residence> findByAllEnfantEntreReelle(long classePenale,  long niveauEducatif, long gouvernorat, 
long situationFamiliale, long situationSocial, long metier, 
long delegation, Etablissement etablissement, long gouvernoratTribunal  ,long typeTribunal  ,long typeAffaire ,
@Temporal Date start, @Temporal Date end ,@Temporal Date dateDebutGlobale, @Temporal Date dateFinGlobale,String etranger);

	   
	   @Query("SELECT a FROM Residence a WHERE (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(((?9 = null) and (?10 = null)) or (a.arrestation.enfant.dateNaissance between  ?9 and  ?10)) and "
		   		+ "  a.statut = 0 and a.etablissement   = ?8 and a.arrestation not in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff"
		   	 
		   		 
		   		+ " )  order by a.numArrestation")
List<Residence> findByAllEnfantNonExist(long classePenale,  long niveauEducatif, long gouvernorat, 
long situationFamiliale, long situationSocial, long metier, 
long delegation, Etablissement etablissement  ,@Temporal Date start, @Temporal Date end );

	   @Query("SELECT a FROM Residence a WHERE "
		   		+ "a.arrestation not in (SELECT aff.arrestation FROM Affaire aff where "
		   		+ "(aff.typeDocument = 'CH' or aff.typeDocument = 'CD' or "
		   		+ "aff.typeDocument = 'CJA'  or aff.typeDocument = 'T' or "
		   		+ " aff.typeDocument = 'CP' or "
		   		+ "aff.typeDocument = 'AP' or aff.typeDocument = 'AE')"
		   		+ " and aff.statut = 0) and a.arrestation in (SELECT aff.arrestation FROM Affaire aff ) and "
		   		+" (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?14 =  null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ " a.statut = 0 and a.etablissement.id  = ?8 "
		   		
		   		+"and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		+ " )" 
		   		
		   		+ " order by a.numArrestation")
		   List<Residence> findByAllEnfantExistJuge (long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,long typeTribunal  ,
				   long typeAffaire ,@Temporal Date start, @Temporal Date end, String etranger );
	   
	   @Query("SELECT a FROM Residence a WHERE "
		   		+ "a.arrestation  in (SELECT aff.arrestation FROM Affaire aff where "
		   		+ "(aff.typeDocument = 'CH' or aff.typeDocument = 'CD' or "
		   		+ "aff.typeDocument = 'CJA' or aff.typeDocument = 'T' or"
		   		+ " aff.typeDocument = 'CP' or "
		   		+ " aff.typeDocument = 'AP' or aff.typeDocument = 'AE')"
		   		+ " and aff.statut = 0) and"
		   		+" (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?14 =  null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ " a.statut = 0 and a.etablissement.id  = ?8 "
		   		+"and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		+ " )" 
		   		+ " order by a.numArrestation")
		   List<Residence> findByAllEnfantExistArret(long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,long typeTribunal  ,
				   long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger );
	   
	   @Query("SELECT a FROM Residence a WHERE "
		   		+ "a.arrestation   in (SELECT aff.arrestation FROM Affaire aff where "
		   		+ "(aff.typeDocument = 'AP'  )"
		   		+ " and aff.statut = 0) and"
		   		+" (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?14 =  null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ " a.statut = 0 and a.etablissement.id  = ?8 "
		   		+"and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		+ " )" 
		   		+ " order by a.numArrestation")
		   List<Residence> findByAllEnfantExistArretAP(long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,long typeTribunal  ,
				   long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger);
	   
	   @Query("SELECT a FROM Residence a WHERE "
		   		+ "a.arrestation   in (SELECT aff.arrestation FROM Affaire aff where "
		   		+ "(aff.typeDocument = 'AE'  )"
		   		+ " and aff.statut = 0) and"
		   		+" (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?14 =  null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ " a.statut = 0 and a.etablissement.id  = ?8 "
		   		+"and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		+ " )" 
		   		+ " order by a.numArrestation")
		   List<Residence> findByAllEnfantExistArretAE(long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,long typeTribunal  ,
				   long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger);
	   
	   @Query("SELECT a FROM Residence a WHERE "
		   		+ "a.arrestation  in (SELECT aff.arrestation FROM Affaire aff where "
		   		+ "(aff.typeDocument = 'T' )"
		   		+ " and aff.statut = 0) and"
		   		+" (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?14 =  null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ " a.statut = 0 and a.etablissement.id  = ?8 "
		   		+"and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		+ " )" 
		   		+ " order by a.numArrestation")
		   List<Residence> findByAllEnfantExistArretT(long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,
				   long typeTribunal  ,long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger);
	   
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
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ " a.statut = 0 and a.etablissement.id  = ?8 "
		   		+"and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		+ " )" 
		   		+ " order by a.numArrestation")
		   List<Residence> findByAllEnfantExistJugeR(long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,
				   long typeTribunal  ,long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger);
	   
	   
	   
	   @Query("SELECT  a FROM Residence a WHERE (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?16 =  null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+ "(a.etabChangeManiere is null) and "
		   		+"(a.arrestation.liberation is not null) and " 
		   		+ "(((?14 = null) and (?15 = null)) or (a.arrestation.liberation.date between  ?14 and  ?15)) and "
		   		
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ "  a.statut = 1 and a.etablissement   = ?8 and a.etablissementSortie = null and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		 
		   		+ " )  order by a.arrestation.liberation.date , a.numArrestation")
	List<Residence> findByAllEnfantLibere(long classePenale,  long niveauEducatif, long gouvernorat, 
	long situationFamiliale, long situationSocial, long metier, 
	long delegation, Etablissement etablissement, long gouvernoratTribunal  , 
	long typeTribunal  ,long typeAffaire ,
	@Temporal Date start, @Temporal Date end ,@Temporal Date dateDebutGlobale, @Temporal Date dateFinGlobale,String etranger);
//	   + "(((?14 = null) and (?15 = null)) or (max(aff.dateFinPunition) between  ?14 and  ?15)) and "
	   @Query("SELECT a FROM Residence a WHERE "
		   		+ "a.arrestation not in (SELECT aff.arrestation FROM Affaire aff where "
		   		+ "(aff.typeDocument = 'CH' or aff.typeDocument = 'CD' or aff.typeDocument = 'T' or aff.typeDocument = 'AP')"
		   		+ " and aff.statut = 0) and "
		   		+ "a.arrestation  in (SELECT aff.arrestation FROM Affaire aff where "
		   		+ "(((?14 = null) and (?15 = null)) or ((select max(a.dateFinPunition)  FROM Affaire a WHERE a.arrestation = aff.arrestation) between ?14 and ?15))"
		   		+ " and aff.statut = 0 ) and "
		   		+ "a.arrestation in (SELECT aff.arrestation FROM Affaire aff ) and "
		   		+" (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?16 = null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ " a.statut = 0 and a.etablissement.id  = ?8 "
		   		
		   		+"and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		+ " )" 
		   		
		   		+ " order by a.numArrestation")
		   List<Residence> findByAllEnfantSeraLibere (long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,
				   long typeTribunal  ,long typeAffaire 
				   ,@Temporal Date start, @Temporal Date end,
				   @Temporal Date dateDebutGlobale, @Temporal Date dateFinGlobale,String etranger);
	 		}


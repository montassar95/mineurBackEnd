package com.cgpr.mineur.repository;

 
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.ResidenceId;

 
 

@Repository
public interface StatistcsRepository  extends CrudRepository<Residence, ResidenceId> {
	
	      
	   
	     
	   
	   
	 	   @Query("SELECT count(a) FROM Residence a where"
	 	   		 						   		+ "  a.statut = 0 and a.etablissement.id  = ?1 and a.arrestation.enfant.nationalite.id = 1" )
	 	  int findByAllEnfantExist( String etablissementId );
	 	   
	 	   
	 	  @Query("SELECT count(a) FROM Residence a where"
				   		+ "  a.statut = 0 and a.etablissement.id  = ?1 and (a.arrestation.enfant.dateNaissance between  ?2 and  ?3)" )
              int findByAllByAge( String etablissementId ,@Temporal Date start, @Temporal Date end);
	 
	 	   
	 	  @Query("SELECT count(a) FROM Residence a where"
				   		+ "  a.statut = 0 and a.etablissement.id  = ?1 and (a.arrestation.enfant.classePenale.id = 1"
				   		+ "  and a.residenceId.numOrdinaleArrestation = 1)" )
         int findByAllEnfantDebutant( String etablissementId );
	 	  
	 	 @Query("SELECT count(a) FROM Residence a where"
				   		+ "  a.statut = 0 and a.etablissement.id  = ?1 and (a.arrestation.enfant.classePenale.id = 2"
				   		+ "or a.residenceId.numOrdinaleArrestation > 1)" )
           int findByAllEnfantAncien( String etablissementId );
	 	 
	 	 
	 	 //-----------------------------------------------------------------------------------------------------------------
	 	 
	 	 @Query("SELECT count(a) FROM Residence a where"
			   		+ "  a.statut = 0 and a.etablissement.id  = ?1 and (a.arrestation.enfant.niveauEducatif.id >= ?2)"
			   		+ "and (a.arrestation.enfant.niveauEducatif.id <= ?3)" )
           int findByAllEnfantNiveauEducatif( String etablissementId ,long a,long b);
	 	 //------------------------------------------------------------------------------------------------------------------
	 	 
//-----------------------------------------------------------------------------------------------------------------
	 	 
	 	 @Query("SELECT count(a) FROM Residence a where"
			   		+ "  a.statut = 0 and a.etablissement.id  = ?1 and (a.arrestation.enfant.situationFamiliale.id = ?2)" )
           int findByAllEnfantSituationFamiliale( String etablissementId ,long a);
	 	 //------------------------------------------------------------------------------------------------------------------
	 	 
	 	  @Query("SELECT count(a) FROM Residence a where"
				   		+ "  a.statut = 0 and a.etablissement.id  = ?1 and a.arrestation.enfant.nationalite.id != 1" )
             int findByAllEnfantExistEtranger( String etablissementId );
	   
	   
	   @Query("SELECT count(a) FROM Residence a WHERE (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?14 = null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ "  a.statut = 0 and a.etablissement   = ?8 and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		 
		   		+ " )  order by a.arrestation.enfant.dateNaissance")
		int findByAllEnfantDevenuMajeur(long classePenale,  long niveauEducatif, long gouvernorat, 
		long situationFamiliale, long situationSocial, long metier, 
		long delegation, Etablissement etablissement, long gouvernoratTribunal  ,long typeTribunal  ,
		long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger );
	
	   
	   
		   @Query("SELECT count(a) FROM Residence a WHERE   a.arrestation.liberation is not null   and "
		   		 	  	+ "  a.etablissement.id   = ?1 and a.etablissementEntree.id != ?1 " )
		int findByEntreeMutation(String etablissementId);
	   
	   
	   
	   @Query("SELECT count(a) FROM Residence a WHERE   a.arrestation.liberation is not null   and "
  		 	  	+ "   a.etablissement.id   = ?1 and a.etablissementSortie.id != ?1 ")
	int findBySortieMutation(String etablissementId);;
	 
	   @Query("SELECT count(a)  FROM Residence a WHERE (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
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
int findByAllEnfantEntreReelle(long classePenale,  long niveauEducatif, long gouvernorat, 
long situationFamiliale, long situationSocial, long metier, 
long delegation, Etablissement etablissement, long gouvernoratTribunal  ,long typeTribunal  ,long typeAffaire ,
@Temporal Date start, @Temporal Date end ,@Temporal Date dateDebutGlobale, @Temporal Date dateFinGlobale,String etranger);

	   
	   @Query("SELECT  count(a)  FROM Residence a WHERE (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
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
int findByAllEnfantNonExist(long classePenale,  long niveauEducatif, long gouvernorat, 
long situationFamiliale, long situationSocial, long metier, 
long delegation, Etablissement etablissement  ,@Temporal Date start, @Temporal Date end );

	   @Query("SELECT  count(a)  FROM Residence a WHERE "
		   		+ "a.arrestation not in (SELECT aff.arrestation FROM Affaire aff where "
		   		+ "(aff.typeDocument = 'CH' or "
		   		+ "aff.typeDocument = 'CD'  or "
		   		+ "aff.typeDocument = 'CJA' or "
		   		+ "aff.typeDocument = 'T'   or "
		   		+ "aff.typeDocument = 'AP'  or "
		   		+ "aff.typeDocument = 'CP'  or "
		   		+ "aff.typeDocument = 'AE')"
		   		+ " and aff.statut = 0)  and " 
		   		+ " a.statut = 0 and a.etablissement.id  = ?1 " )
		   int findByAllEnfantExistJuge ( String etablissementId );
	   
	   @Query("SELECT  count(a)  FROM Residence a WHERE "
		   		+ "a.arrestation  in (SELECT aff.arrestation FROM Affaire aff where "
		   		+ "(aff.typeDocument = 'CH' or "
		   		+ "aff.typeDocument = 'CD'  or "
		   		+ "aff.typeDocument = 'CJA' or "
		   		+ "aff.typeDocument = 'T'   or "
		   		+ "aff.typeDocument = 'AP'  or "
		   		+ "aff.typeDocument = 'CP'  or "
		   		+ "aff.typeDocument = 'AE')"
		   		+ " and aff.statut = 0) and"
   
		   		+ " a.statut = 0 and a.etablissement.id  = ?1 " )
		   int findByAllEnfantExistArret( String etablissementId  );
	   
	   @Query("SELECT  count(a)  FROM Residence a WHERE "
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
		   int findByAllEnfantExistArretAP(long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,long typeTribunal  ,
				   long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger);
	   
	   @Query("SELECT  count(a)  FROM Residence a WHERE "
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
		    int findByAllEnfantExistArretAE(long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,long typeTribunal  ,
				   long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger);
	   
	   @Query("SELECT  count(a)  FROM Residence a WHERE "
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
		   int findByAllEnfantExistArretT(long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,
				   long typeTribunal  ,long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger);
	   
	   @Query("SELECT  count(a)  FROM Residence a WHERE "
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
		  int findByAllEnfantExistJugeR(long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,
				   long typeTribunal  ,long typeAffaire ,@Temporal Date start, @Temporal Date end,String etranger);
	   
	   
	   
	   @Query("SELECT  count(a)  FROM Residence a WHERE (?1 =  0L or a.arrestation.enfant.classePenale.id = ?1) and"
		   		+ "(?2 =  0L or a.arrestation.enfant.niveauEducatif.id = ?2) and"
		   		+ "(?3 =  0L or a.arrestation.enfant.gouvernorat.id = ?3) and"
		   		+ "(?4 =  0L or a.arrestation.enfant.situationFamiliale.id = ?4) and"
		   		+ "(?5 =  0L or a.arrestation.enfant.situationSocial.id = ?5) and"
		   		+ "(?6 =  0L or a.arrestation.enfant.metier.id = ?6) and"
		   		+ "(?7 =  0L or a.arrestation.enfant.delegation.id = ?7) and "
		   		+ "(?16 =  null or a.arrestation.enfant.nationalite.id != 1) and "
		   		+"(a.arrestation.liberation is not null) and " 
		   		+ "(((?14 = null) and (?15 = null)) or (a.arrestation.liberation.date between  ?14 and  ?15)) and "
		   		
		   		+ "(((?12 = null) and (?13 = null)) or (a.arrestation.enfant.dateNaissance between  ?12 and  ?13)) and "
		   		+ "  a.statut = 1 and a.etablissement   = ?8 and a.etablissementSortie = null and a.arrestation in "
		   		+ "(SELECT aff.arrestation FROM Affaire aff where aff.statut = 0 and "
		   		+ "(?9 = 0L or aff.tribunal.gouvernorat.id = ?9) "
		   		+ "and (?10 = 0L or aff.tribunal.typeTribunal.id = ?10) "
		   		+ "and (?11 = 0L or aff.typeAffaire.id = ?11) "
		   		 
		   		+ " )  order by a.arrestation.liberation.date , a.numArrestation")
	int findByAllEnfantLibere(long classePenale,  long niveauEducatif, long gouvernorat, 
	long situationFamiliale, long situationSocial, long metier, 
	long delegation, Etablissement etablissement, long gouvernoratTribunal  , 
	long typeTribunal  ,long typeAffaire ,
	@Temporal Date start, @Temporal Date end ,@Temporal Date dateDebutGlobale, @Temporal Date dateFinGlobale,String etranger);
 
	   @Query("SELECT  count(a)  FROM Residence a WHERE "
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
		   int findByAllEnfantSeraLibere (long classePenale,  long niveauEducatif, long gouvernorat, 
				   long situationFamiliale, long situationSocial, long metier, 
				   long delegation  ,String etablissementId, long gouvernoratTribunal  ,
				   long typeTribunal  ,long typeAffaire 
				   ,@Temporal Date start, @Temporal Date end,
				   @Temporal Date dateDebutGlobale, @Temporal Date dateFinGlobale,String etranger);
	 		}


package com.cgpr.mineur.repository;


 
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.dto.DocumentSearchCriteriaDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.DocumentId;
 
 


 

@Repository
public interface DocumentRepository extends PagingAndSortingRepository<Document, DocumentId> {

	@Query("SELECT d FROM Document d WHERE d.documentId.idEnfant = ?1 "
																+ "and d.documentId.numOrdinalArrestation = ?2 "
																+ "and d.documentId.numOrdinalAffaire = ?3 "
																+ "order by d.documentId.numOrdinalDocByAffaire desc")
	List<Document>  getDocumentByAffaire (String idEnfant,long numOrdinalArrestation,long numOrdinalAffaire); 
	
	
	@Query("SELECT d FROM Document d WHERE d.documentId.idEnfant = ?1 "
			                                  + "and d.documentId.numOrdinalArrestation = ?2 order by d.documentId.numOrdinalDoc desc")
          List<Document>  findDocumentByArrestation (String idEnfant,long numOrdinalArrestation); 
	
	@Query("select count(d) from Document d WHERE d.documentId.idEnfant = ?1 and d.documentId.numOrdinalArrestation = ?2 and d.documentId.numOrdinalAffaire = ?3")
    long countDocumentByAffaire (String idEnfant, long numOrdinalArrestation, long numOrdinalAffaire); 
	
	@Query("select count(d) from Document d WHERE d.documentId.idEnfant = ?1 and d.documentId.numOrdinalArrestation = ?2")
    long getDocumentByArrestation (String idEnfant,long numOrdinalArrestation ); 
	
//	 List<Document> findByAffaire (Affaire affaire);
	 
	 
	 @Query("SELECT a FROM Affaire a WHERE a.arrestation.arrestationId.idEnfant = ?1  "
	 		+ "and  a.statut = 0"
	 		+ "and  a.arrestation.arrestationId.numOrdinale = (select max(ar.arrestationId.numOrdinale) from Arrestation ar where ar.enfant.id = ?1) order by a.numOrdinalAffaire desc ")
	 List<Affaire> findByArrestation (String idEnfant );
	 
	 
	 @Query("SELECT a FROM Affaire a WHERE a.arrestation.arrestationId.idEnfant = ?1  "
		 		+ "and  a.statut = 0"
		 		+ "and  (a.typeDocument = 'CD'"
		 		+ "or  a.typeDocument = 'CH'"
		 		+ "or  a.typeDocument = 'T'"
		 		+ "or  a.typeDocument = 'CJA'"
		 		+ "or  a.typeDocument = 'AP'"
		 		+ "or  a.typeDocument = 'CP'"
		 		+ "or  a.typeDocument = 'AE')"
		 		+ "and  a.arrestation.arrestationId.numOrdinale = (select max(ar.arrestationId.numOrdinale) from Arrestation ar where ar.enfant.id = ?1) order by a.numOrdinalAffaire desc ")
		 List<Affaire> findStatutJurByArrestation (String idEnfant );
	      
	 
	 @Query("SELECT d FROM Document d WHERE d.documentId.idEnfant = ?1 "
				+ " and d.documentId.numOrdinalArrestation = ?2 "
				+ " and d.documentId.numOrdinalAffaire = ?3 "
				 
				+ " and d.documentId.numOrdinalDocByAffaire =  ?4" )
Document   getDocument (String idEnfant,long numOrdinalArrestation,long numOrdinalAffaire , long numOrdinalDocByAffaire ); 
	 
	 
	 
	 
	 
	 
	 
//	 
//	 @Query("SELECT d FROM Document d WHERE d.documentId.idEnfant = ?1 "
//				+ " and d.documentId.numOrdinalArrestation = ?2 "
//				+ " and d.documentId.numOrdinalAffaire = ?3 "
//				+ " and (d.typeDocument = 'CJ' or d.typeDocument = 'CJA' or d.typeDocument = 'CD'  or d.typeDocument = 'CH')"
//				+ " and d.documentId.numOrdinalDocByAffaire = (select max"
//				+ "( do.documentId.numOrdinalDocByAffaire)FROM Document do"
//				+ " WHERE do.documentId.idEnfant = ?1 " + 
//				"  and do.documentId.numOrdinalArrestation = ?2 "  
//				+"  and do.documentId.numOrdinalAffaire = ?3  "
//				+ " and (do.typeDocument = 'CJ' or do.typeDocument = 'CJA' or do.typeDocument = 'CD'  or do.typeDocument = 'CH')"
//			                                            	+ " )" )
//List<Document>  getDocumentByAffaireforAccusation (String idEnfant,long numOrdinalArrestation,long numOrdinalAffaire,  Pageable pageable ); 
//
//@Query("SELECT d FROM Document d WHERE d.documentId.idEnfant = ?1 "
//	+ " and d.documentId.numOrdinalArrestation = ?2 "
//	+ " and d.documentId.numOrdinalAffaire = ?3 "
//	 
//	+ " and d.documentId.numOrdinalDocByAffaire = (select max"
//	+ "( do.documentId.numOrdinalDocByAffaire)FROM Document do"
//	+ " WHERE do.documentId.idEnfant = ?1 " + 
//	"  and do.documentId.numOrdinalArrestation = ?2 "  
//	+"  and do.documentId.numOrdinalAffaire = ?3  "
//
//                                         	+ " )" )
//Document   getLastDocumentByAffaireforAccusation (String idEnfant,long numOrdinalArrestation,long numOrdinalAffaire  ); 
	 
	 
	 
//	 @Query("SELECT d FROM Document d WHERE d.documentId.idEnfant = ?1 "
//		        + "AND d.documentId.numOrdinalArrestation = ?2 "
//		        + "AND d.documentId.numOrdinalAffaire = ?3 "
//		        + "AND (               (d.typeDocument = 'CJ'   AND  d.typeDocumentActuelle is null  ) "
//				                 + "OR (d.typeDocument = 'CJA'  AND d.typeDocumentActuelle is null)"
//				                 + "OR (d.typeDocument = 'CD'   AND d.typeDocumentActuelle is null) "
//				                 + "OR (d.typeDocument = 'CH'   AND d.typeDocumentActuelle is null)"
//		             + ") "
//		        
//		        + "AND d.documentId.numOrdinalDocByAffaire = (SELECT MAX(do.documentId.numOrdinalDocByAffaire) FROM Document do "
//		        + "WHERE do.documentId.idEnfant = ?1 "
//		        + "AND do.documentId.numOrdinalArrestation = ?2 "
//		        + "AND do.documentId.numOrdinalAffaire = ?3 "
//		        + "AND (               (do.typeDocument = 'CJ'     AND do.typeDocumentActuelle is null ) "
//					                + "OR (do.typeDocument = 'CJA'  AND do.typeDocumentActuelle is null)"
//					                + "OR (do.typeDocument = 'CD'   AND do.typeDocumentActuelle is null) "
//					                + "OR (do.typeDocument = 'CH'   AND do.typeDocumentActuelle is null)"
//    + ") "
//		        + ")")
//		List<Document> getDocumentByAffaireforAccusation(String idEnfant, long numOrdinalArrestation, long numOrdinalAffaire, Pageable pageable);
//
//	 
	 
//	 @Query("SELECT d FROM Document d WHERE "
//		       + "d.documentId.idEnfant = :#{#criteria.idEnfant} "
//		       + "AND d.documentId.numOrdinalArrestation = :#{#criteria.numOrdinalArrestation} "
//		       + "AND d.documentId.numOrdinalAffaire = :#{#criteria.numOrdinalAffaire} "
//		       + "AND d.typeDocument IN ('CJ', 'CJA', 'CD', 'CH') "   
//		       + "AND d.typeDocumentActuelle IS NULL "
//		       + "AND d.documentId.numOrdinalDocByAffaire = (SELECT MAX(do.documentId.numOrdinalDocByAffaire) FROM Document do "
//		       + "WHERE "
//		       + "do.documentId.idEnfant = :#{#criteria.idEnfant} "
//		       + "AND do.documentId.numOrdinalArrestation = :#{#criteria.numOrdinalArrestation} "
//		       + "AND do.documentId.numOrdinalAffaire = :#{#criteria.numOrdinalAffaire} "
//		       + "AND do.typeDocument IN ('CJ', 'CJA', 'CD', 'CH') "  
//		       + "AND do.typeDocumentActuelle IS NULL "
//		       + ")")
//		List<Document> findMostRecentDocument(@Param("criteria") DocumentSearchCriteriaDto criteria , Pageable pageable );
//	 default   Document   getDocumentByAffaireforAccusation( DocumentSearchCriteriaDto criteria ) {
//		    Pageable pageable = PageRequest.of(0, 1); // Pour obtenir le premier résultat
//		    List<Document> documents = findMostRecentDocument(criteria, pageable);
//		    return documents.isEmpty() ? null : documents.get(0) ;
//		}
	 
	 
	 @Query("SELECT d FROM Document d WHERE "  
			 + "d.documentId.idEnfant = :#{#criteria.idEnfant} "
		     + "AND d.documentId.numOrdinalArrestation = :#{#criteria.numOrdinalArrestation} "
	         + "AND d.documentId.numOrdinalAffaire = :#{#criteria.numOrdinalAffaire} "

		      + "AND d.typeDocument IN ('CJ', 'CJA', 'CD', 'CH') " +
		       "AND d.typeDocumentActuelle IS NULL " +
		       "ORDER BY d.documentId.numOrdinalDocByAffaire DESC")
		List<Document> findMostRecentDocument(@Param("criteria") DocumentSearchCriteriaDto criteria , Pageable pageable);
	 default   Document   getDocumentByAffaireforAccusation( DocumentSearchCriteriaDto criteria ) {
		    Pageable pageable = PageRequest.of(0, 1); // Pour obtenir le premier résultat
		    List<Document> documents = findMostRecentDocument(criteria, pageable);
		    return documents.isEmpty() ? null : documents.get(0) ;
		}
	 
	 
	 
	 //methode A
	 @Query("SELECT d FROM Document d WHERE d.documentId.idEnfant = ?1 "
		        + "AND d.documentId.numOrdinalArrestation = ?2 "
		        + "AND d.documentId.numOrdinalAffaire = ?3 "
		        + "AND d.documentId.numOrdinalDocByAffaire = (SELECT MAX(do.documentId.numOrdinalDocByAffaire) FROM Document do "
		        + "WHERE do.documentId.idEnfant = ?1 "
		        + "AND do.documentId.numOrdinalArrestation = ?2 "
		        + "AND do.documentId.numOrdinalAffaire = ?3)")
		Document getLastDocumentByAffaire (String idEnfant, long numOrdinalArrestation, long numOrdinalAffaire);
	 
	 
	//methode B
	 @Query("SELECT d FROM Document d WHERE "  
			 + "d.documentId.idEnfant = :#{#criteria.idEnfant} "
		     + "AND d.documentId.numOrdinalArrestation = :#{#criteria.numOrdinalArrestation} "
	         + "AND d.documentId.numOrdinalAffaire = :#{#criteria.numOrdinalAffaire} "
  
		      + "ORDER BY d.documentId.numOrdinalDocByAffaire DESC")
		List<Document> findMostRecentDocumentByAffaire2(@Param("criteria") DocumentSearchCriteriaDto criteria , Pageable pageable);
	 default   Document   getLastDocumentByAffaire2( DocumentSearchCriteriaDto criteria ) {
		    Pageable pageable = PageRequest.of(0, 1); // Pour obtenir le premier résultat
		    List<Document> documents = findMostRecentDocumentByAffaire2(criteria, pageable);
		    return documents.isEmpty() ? null : documents.get(0) ;
		}



}


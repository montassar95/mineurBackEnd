package com.cgpr.mineur.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.dto.SearchDetenuDto;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.models.Residence;
 

 
 

@Repository
public interface EnfantRepository extends  JpaRepository<Enfant, String>, JpaSpecificationExecutor<Enfant>, QueryByExampleExecutor<Enfant> {

	
	 @Query("select max(e.id) from Enfant e where e.id like :idEta%")
  	 String  maxId(@Param("idEta")String idEta);
	 
	 @Query("SELECT new com.cgpr.mineur.dto.SearchDetenuDto(" 
		        + "r.arrestation.enfant.id, "  // String
		        + "r.arrestation.enfant.nom, " // String 
		        + "r.arrestation.enfant.prenom, " // String 
		        + "r.arrestation.enfant.nomPere, " // String 
		        + "r.arrestation.enfant.nomGrandPere, " // String 
		        + "r.arrestation.enfant.nomMere, " // String 
		        + "r.arrestation.enfant.prenomMere, " // String 
		       
		        + "r.arrestation.enfant.lieuNaissance, " // String
		        + "r.arrestation.enfant.sexe, " // String 
		        + "r.numArrestation, " //String
		        + "r.etablissement.libelle_etablissement, " //String
		        
 		        + "r.arrestation.enfant.dateNaissance, "  // LocalDate
 		        + "TO_CHAR(r.dateEntree, 'YYYY/MM/DD'), " // date sql
 		        + "r.statut "// int
		        + ") " 
		        + "FROM Residence r WHERE "
		        
		        // Filtrage sur les noms et autres critères
		        + "(:nom IS NULL OR r.arrestation.enfant.simplifierCriteria.nomSimplifie LIKE %:nom%) "
		        + "AND (:prenom IS NULL OR r.arrestation.enfant.simplifierCriteria.prenomSimplifie LIKE %:prenom%) "
		        + "AND (:nomPere IS NULL OR r.arrestation.enfant.simplifierCriteria.nomPereSimplifie LIKE %:nomPere%) "
		        + "AND (:nomGrandPere IS NULL OR r.arrestation.enfant.simplifierCriteria.nomGrandPereSimplifie LIKE %:nomGrandPere%) "
		        + "AND (:nomMere IS NULL OR r.arrestation.enfant.simplifierCriteria.nomMereSimplifie LIKE %:nomMere%) "
		        + "AND (:prenomMere IS NULL OR r.arrestation.enfant.simplifierCriteria.prenomMereSimplifie LIKE %:prenomMere%) " 
		        
		        // Condition de date de naissance avec TRUNC pour ignorer l'heure
 		        + "AND (:dateNaissance IS NULL OR TRUNC(r.arrestation.enfant.simplifierCriteria.dateNaissance) = :dateNaissance) "
		        
		        // Condition de sexe
 		        + "AND (:sexe IS NULL OR r.arrestation.enfant.simplifierCriteria.sexe LIKE %:sexe%) "
		        
		        // Sélectionner le maximum du numéro d'ordre d'arrestation pour l'enfant
		        + "AND r.residenceId.numOrdinaleArrestation = ( "
		        + "    SELECT MAX(ar.residenceId.numOrdinaleArrestation) "
		        + "    FROM Residence ar "
		        + "    WHERE ar.residenceId.idEnfant = r.residenceId.idEnfant "
		        + ") "
		        
		        // Sélectionner le maximum du numéro d'ordre de résidence pour l'arrestation
		        + "AND r.residenceId.numOrdinaleResidence = ("
		        + "    SELECT MAX(res.residenceId.numOrdinaleResidence) "
		        + "    FROM Residence res "
		        + "    WHERE res.residenceId.idEnfant = r.residenceId.idEnfant "
		        + "    AND res.residenceId.numOrdinaleArrestation = r.residenceId.numOrdinaleArrestation "
		        + ") "
		        
		        // Tri par ID de l'enfant, décroissant
 		        + "ORDER BY r.arrestation.enfant.id DESC")
		List<SearchDetenuDto> searchSimplified(
		        @Param("nom") String nom,  
		        @Param("prenom") String prenom, 
		        @Param("nomPere") String nomPere,
		        @Param("nomGrandPere") String nomGrandPere,
		        @Param("nomMere") String nomMere,
		        @Param("prenomMere") String prenomMere,
 		        @Param("dateNaissance") LocalDate dateNaissance,
 		        @Param("sexe") String sexe
		        );




//	 
//	 String rqe="select r from Residence r where "
//				+ "   (nom =  null or r.arrestation.enfant.nomSimplifie like %:nom%) "
//				+ "and (prenom =  null or r.arrestation.enfant.prenomSimplifie like %:prenom%) "
//				+ "and (nomPere =  null or r.arrestation.enfant.nomPereSimplifie like %:nomPere%)"
//				+ "and (nomGrandPere =  null or r.arrestation.enfant.nomGrandPereSimplifie like %:nomGrandPere%) "
//				+ "and (nomMere =  null or r.arrestation.enfant.nomMereSimplifie like %:nomMere% )"
//				+ "and (prenomMere =  null or r.arrestation.enfant.prenomMereSimplifie like %:prenomMere% )"
//				+ "and (dateNaissance =  null or r.arrestation.enfant.dateNaissance like :dateNaissance) "
//				+ "and (sexe =  null or r.arrestation.enfant.sexe like %:sexe%) "
//	            + "and  r.residenceId.numOrdinaleResidence = (select max(a.residenceId.numOrdinaleResidence) from Residence a where"
//	        	+ "    (nom =  null or a.arrestation.enfant.nomSimplifie like %:nom%) "
//				+ "and (prenom =  null or a.arrestation.enfant.prenomSimplifie like %:prenom%) "
//				+ "and (nomPere =  null or a.arrestation.enfant.nomPereSimplifie like %:nomPere% )"
//				+ "and (nomGrandPere =  null or a.arrestation.enfant.nomGrandPereSimplifie like %:nomGrandPere%) "
//				+ "and (nomMere =  null or a.arrestation.enfant.nomMereSimplifie like %:nomMere%) "
//				+ "and (prenomMere =  null or a.arrestation.enfant.prenomMereSimplifie like %:prenomMere%) "
//				+ "and (dateNaissance =  null or a.arrestation.enfant.dateNaissance like :dateNaissance) "
//				+ "and (sexe =  null or a.arrestation.enfant.sexe like %:sexe%) ) "
//				+ "order by r.arrestation.enfant.id desc";
//	@Query(rqe)
//	List<Residence>  search(@Param("nom") String nom, 
//			             @Param("prenom") String prenom, 
//			             @Param("nomPere") String nomPere,
//			             @Param("nomGrandPere") String nomGrandPere,
//			             @Param("nomMere") String nomMere,
//			             @Param("prenomMere") String prenomMere,
//			             @Param("dateNaissance") Date dateNaissance,
//			             @Param("sexe") String sexe); 
//	
//	
//	
//	 String rqeSansDate="select r from Residence r where "
//				+ "(r.arrestation.enfant.nomSimplifie like %:nom%)  "
//				+ "and (r.arrestation.enfant.prenomSimplifie like %:prenom%)  "
//				+ "and (r.arrestation.enfant.nomPereSimplifie like %:nomPere%)  "
//				+ "and (r.arrestation.enfant.nomGrandPereSimplifie like %:nomGrandPere%)  "
//				+ "and (r.arrestation.enfant.nomMereSimplifie like %:nomMere%)  "
//				+ "and (r.arrestation.enfant.prenomMereSimplifie like %:prenomMere%)  " 
//				+ "and (r.arrestation.enfant.sexe like %:sexe%)  "
//	          
//				+ "and  r.residenceId.numOrdinaleArrestation = "
//							+ "("
//							+ "select max(a.residenceId.numOrdinaleArrestation) from Residence a where"
//				        	+ " (a.arrestation.enfant.nomSimplifie like %:nom%) "
//							+ "and (a.arrestation.enfant.prenomSimplifie like %:prenom% ) "
//							+ "and (a.arrestation.enfant.nomPereSimplifie like %:nomPere%) "
//							+ "and (a.arrestation.enfant.nomGrandPereSimplifie like %:nomGrandPere%) "
//							+ "and (a.arrestation.enfant.nomMereSimplifie like %:nomMere%) "
//							+ "and (a.arrestation.enfant.prenomMereSimplifie like %:prenomMere% ) "
//						  	+ "and (a.arrestation.enfant.sexe like %:sexe%) "
//							+ ")"
//				 
//				+ "and  r.residenceId.numOrdinaleResidence = "
//							+ "("
//							+ "select max(a.residenceId.numOrdinaleResidence) from Residence a where " 
//							
//						
//							+ "(a.arrestation.enfant.nomSimplifie like %:nom%) "
//							+ "and (a.arrestation.enfant.prenomSimplifie like %:prenom%) "
//							+ "and (a.arrestation.enfant.nomPereSimplifie like %:nomPere%) "
//							+ "and (a.arrestation.enfant.nomGrandPereSimplifie like %:nomGrandPere% ) " 
//							+ "and (a.arrestation.enfant.nomMereSimplifie like %:nomMere%) "
//							+ "and (a.arrestation.enfant.prenomMereSimplifie like %:prenomMere%) "
//							
//							+ "and (a.arrestation.enfant.sexe like %:sexe% ) "
//							+ "and  a.residenceId.numOrdinaleArrestation = "
//									+ "("
//									+ "select max(aa.residenceId.numOrdinaleArrestation) from Residence aa where " + 
//									" (aa.arrestation.enfant.nomSimplifie like %:nom%)  " + 
//									" and (aa.arrestation.enfantSimplifie.prenom like %:prenom% ) " + 
//									" and (aa.arrestation.enfantSimplifie.nomPere like %:nomPere% ) " + 
//									" and (aa.arrestation.enfantSimplifie.nomGrandPere like %:nomGrandPere%)  " + 
//									" and (aa.arrestation.enfantSimplifie.nomMere like %:nomMere%)   " + 
//									" and (aa.arrestation.enfantSimplifie.prenomMere like %:prenomMere%)  " + 
//									 
//									" and (aa.arrestation.enfant.sexe like %:sexe%) "
//									+ " )  "
//							+ ")  order by r.arrestation.enfant.id desc ";
//				   
//	@Query(rqeSansDate)
//	List<Residence>  searchSansDate(@Param("nom") String nom, 
//			             @Param("prenom") String prenom, 
//			             @Param("nomPere") String nomPere,
//			             @Param("nomGrandPere") String nomGrandPere,
//			             @Param("nomMere") String nomMere,
//			             @Param("prenomMere") String prenomMere,
//			            
//			             @Param("sexe") String sexe); 
//	
	
	
	@Query("SELECT new com.cgpr.mineur.dto.SearchDetenuDto( " 
	        + "r.arrestation.enfant.id, "  // String
	        + "r.arrestation.enfant.nom, " // String 
	        + "r.arrestation.enfant.prenom, " // String 
	        + "r.arrestation.enfant.nomPere, " // String 
	        + "r.arrestation.enfant.nomGrandPere, " // String 
	        + "r.arrestation.enfant.nomMere, " // String 
	        + "r.arrestation.enfant.prenomMere, " // String 
	       
	        + "r.arrestation.enfant.lieuNaissance, " // String
	        + "r.arrestation.enfant.sexe, " // String 
	        + "r.numArrestation, " //String
	        + "r.etablissement.libelle_etablissement, " //String
	        
 	        + "r.arrestation.enfant.dateNaissance, "  // LocalDate
  	        + "TO_CHAR(r.dateEntree, 'YYYY/MM/DD'), " // date sql
 	        + "r.statut "// int
	        + ") " 
			+ "from Residence r where r.residenceId.idEnfant =  :id "
			+ " and r.residenceId.numOrdinaleArrestation =(select max(a.residenceId.numOrdinaleArrestation) from Residence a where a.residenceId.idEnfant =  :id ) "
			
	 + "and  r.residenceId.numOrdinaleResidence =("
	 + "select max(x.residenceId.numOrdinaleResidence) from Residence x where x.residenceId.idEnfant =  :id  and x.residenceId.numOrdinaleArrestation =(select max(h.residenceId.numOrdinaleArrestation) from Residence h where h.residenceId.idEnfant =  :id )"
	 + ")")
      Optional<SearchDetenuDto> getoneInResidence(@Param("id") String id);
	
	
	@Query("select r from Residence r where r.numArrestation =  :numArr ")
	List<Residence>  getResidenceByNum(@Param("numArr") String numArr);
	//+ "and e.dateNaissance like %:dateNaissance% "
	 
}


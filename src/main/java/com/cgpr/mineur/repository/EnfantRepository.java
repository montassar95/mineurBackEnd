package com.cgpr.mineur.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.ArrestationId;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.models.Residence;
 

 
 

@Repository
public interface EnfantRepository extends  JpaRepository<Enfant, String>, JpaSpecificationExecutor<Enfant>{

	
	 @Query("select max(e.id) from Enfant e where e.id like :idEta%")
  	 String  maxId(@Param("idEta")String idEta);
	 
	 
	 
	 String rqe="select r from Residence r where "
				+ "   (nom =  null or r.arrestation.enfant.nom like %:nom%) "
				+ "and (prenom =  null or r.arrestation.enfant.prenom like %:prenom%) "
				+ "and (nomPere =  null or r.arrestation.enfant.nomPere like %:nomPere%)"
				+ "and (nomGrandPere =  null or r.arrestation.enfant.nomGrandPere like %:nomGrandPere%) "
				+ "and (nomMere =  null or r.arrestation.enfant.nomMere like %:nomMere% )"
				+ "and (prenomMere =  null or r.arrestation.enfant.prenomMere like %:prenomMere% )"
				+ "and (dateNaissance =  null or r.arrestation.enfant.dateNaissance like :dateNaissance) "
				+ "and (sexe =  null or r.arrestation.enfant.sexe like %:sexe%) "
	            + "and  r.residenceId.numOrdinaleResidence = (select max(a.residenceId.numOrdinaleResidence) from Residence a where"
	        	+ "    (nom =  null or a.arrestation.enfant.nom like %:nom%) "
				+ "and (prenom =  null or a.arrestation.enfant.prenom like %:prenom%) "
				+ "and (nomPere =  null or a.arrestation.enfant.nomPere like %:nomPere% )"
				+ "and (nomGrandPere =  null or a.arrestation.enfant.nomGrandPere like %:nomGrandPere%) "
				+ "and (nomMere =  null or a.arrestation.enfant.nomMere like %:nomMere%) "
				+ "and (prenomMere =  null or a.arrestation.enfant.prenomMere like %:prenomMere%) "
				+ "and (dateNaissance =  null or a.arrestation.enfant.dateNaissance like :dateNaissance) "
				+ "and (sexe =  null or a.arrestation.enfant.sexe like %:sexe%) ) "
				+ "order by r.arrestation.enfant.id desc";
	@Query(rqe)
	List<Residence>  search(@Param("nom") String nom, 
			             @Param("prenom") String prenom, 
			             @Param("nomPere") String nomPere,
			             @Param("nomGrandPere") String nomGrandPere,
			             @Param("nomMere") String nomMere,
			             @Param("prenomMere") String prenomMere,
			             @Param("dateNaissance") Date dateNaissance,
			             @Param("sexe") String sexe); 
	
	
	
	 String rqeSansDate="select r from Residence r where "
				+ "(r.arrestation.enfant.nom like %:nom%)  "
				+ "and (r.arrestation.enfant.prenom like %:prenom%)  "
				+ "and (r.arrestation.enfant.nomPere like %:nomPere%)  "
				+ "and (r.arrestation.enfant.nomGrandPere like %:nomGrandPere%)  "
				+ "and (r.arrestation.enfant.nomMere like %:nomMere%)  "
				+ "and (r.arrestation.enfant.prenomMere like %:prenomMere%)  " 
				+ "and (r.arrestation.enfant.sexe like %:sexe%)  "
	          
				+ "and  r.residenceId.numOrdinaleArrestation = "
							+ "("
							+ "select max(a.residenceId.numOrdinaleArrestation) from Residence a where"
				        	+ " (a.arrestation.enfant.nom like %:nom%) "
							+ "and (a.arrestation.enfant.prenom like %:prenom% ) "
							+ "and (a.arrestation.enfant.nomPere like %:nomPere%) "
							+ "and (a.arrestation.enfant.nomGrandPere like %:nomGrandPere%) "
							+ "and (a.arrestation.enfant.nomMere like %:nomMere%) "
							+ "and (a.arrestation.enfant.prenomMere like %:prenomMere% ) "
						  	+ "and (a.arrestation.enfant.sexe like %:sexe%) "
							+ ")"
				 
				+ "and  r.residenceId.numOrdinaleResidence = "
							+ "("
							+ "select max(a.residenceId.numOrdinaleResidence) from Residence a where " 
							
						
							+ "(a.arrestation.enfant.nom like %:nom%) "
							+ "and (a.arrestation.enfant.prenom like %:prenom%) "
							+ "and (a.arrestation.enfant.nomPere like %:nomPere%) "
							+ "and (a.arrestation.enfant.nomGrandPere like %:nomGrandPere% ) " 
							+ "and (a.arrestation.enfant.nomMere like %:nomMere%) "
							+ "and (a.arrestation.enfant.prenomMere like %:prenomMere%) "
							
							+ "and (a.arrestation.enfant.sexe like %:sexe% ) "
							+ "and  a.residenceId.numOrdinaleArrestation = "
									+ "("
									+ "select max(aa.residenceId.numOrdinaleArrestation) from Residence aa where " + 
									" (aa.arrestation.enfant.nom like %:nom%)  " + 
									" and (aa.arrestation.enfant.prenom like %:prenom% ) " + 
									" and (aa.arrestation.enfant.nomPere like %:nomPere% ) " + 
									" and (aa.arrestation.enfant.nomGrandPere like %:nomGrandPere%)  " + 
									" and (aa.arrestation.enfant.nomMere like %:nomMere%)   " + 
									" and (aa.arrestation.enfant.prenomMere like %:prenomMere%)  " + 
									 
									" and (aa.arrestation.enfant.sexe like %:sexe%) "
									+ " )  "
							+ ")  order by r.arrestation.enfant.id desc ";
				   
	@Query(rqeSansDate)
	List<Residence>  searchSansDate(@Param("nom") String nom, 
			             @Param("prenom") String prenom, 
			             @Param("nomPere") String nomPere,
			             @Param("nomGrandPere") String nomGrandPere,
			             @Param("nomMere") String nomMere,
			             @Param("prenomMere") String prenomMere,
			            
			             @Param("sexe") String sexe); 
	
	
	
	@Query("select r from Residence r where r.residenceId.idEnfant =  :id "
			+ " and r.residenceId.numOrdinaleArrestation =(select max(a.residenceId.numOrdinaleArrestation) from Residence a where a.residenceId.idEnfant =  :id ) "
			
	 + "and  r.residenceId.numOrdinaleResidence =("
	 + "select max(x.residenceId.numOrdinaleResidence) from Residence x where x.residenceId.idEnfant =  :id  and x.residenceId.numOrdinaleArrestation =(select max(h.residenceId.numOrdinaleArrestation) from Residence h where h.residenceId.idEnfant =  :id )"
	 + ")")
      Optional<Residence> getoneInResidence(@Param("id") String id);
	
	
	@Query("select r from Residence r where r.numArrestation =  :numArr ")
	List<Residence>  getResidenceByNum(@Param("numArr") String numArr);
	//+ "and e.dateNaissance like %:dateNaissance% "
	 
}


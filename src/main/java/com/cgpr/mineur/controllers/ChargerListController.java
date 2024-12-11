

package com.cgpr.mineur.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.config.Simplification;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.models.RapportEnfantQuotidien;
import com.cgpr.mineur.models.RapportEnfantQuotidienId;
import com.cgpr.mineur.models.RapportQuotidien;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.SimplifierCriteria;
import com.cgpr.mineur.repository.EnfantRepository;
import com.cgpr.mineur.repository.RapportEnfantQuotidienRepository;
import com.cgpr.mineur.repository.RapportQuotidienRepository;
import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


//SELECT DISTINCT 
//MAX(date_sauvgarde) AS dernier_jour
//FROM rapport_quotidien
//GROUP BY TO_CHAR(date_sauvgarde, 'YYYY'), TO_CHAR(date_sauvgarde, 'MM'); 
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chargerList")
public class ChargerListController {

    @Autowired
    private ChargeAllEnfantService chargeAllEnfantService;

    @Autowired
    private RapportQuotidienRepository rapportQuotidienRepository;
    
    @Autowired
    private RapportEnfantQuotidienRepository rapportEnfantQuotidienRepository;
    
    @Autowired
    private  EnfantRepository enfantRepository;
    
    Simplification simplifier = new Simplification();
    
    
    @GetMapping("/simplifier")
    public void  simplifierCharge() {
    	System.out.println("Debut");
   	List<Enfant> list = new ArrayList<Enfant>();
   	list =  (List<Enfant>) enfantRepository.findAll();
   	
   	for(Enfant enfant : list) {
   	// Simplification des valeurs d'entrée
	    String nomSimplifie = simplifier.simplify(enfant.getNom());
	    String prenomSimplifie = simplifier.simplify(enfant.getPrenom());
	    String nomPereSimplifie = simplifier.simplify(enfant.getNomPere());
	    
	    String nomGrandPereSimplifie = simplifier.simplify(enfant.getNomGrandPere());
	    String  nomMereSimplifie = simplifier.simplify(enfant.getNomMere());
	    
	    String prenomMereSimplifie = simplifier.simplify(enfant.getPrenomMere());
	    
	    
	    LocalDate dateNaissance = enfant.getDateNaissance();
	     

	    // Conversion du sexe en valeur utilisable (1 pour "masculin", 0 pour "féminin")
	    String sexe;
	    if ("ذكر".equals(enfant.getSexe())) {
	        sexe = "1";
	    } else if ("أنثى".equals(enfant.getSexe())) {
	        sexe = "0";
	    } else {
	        sexe = null; // Sexe inconnu ou non fourni
	    }

	    SimplifierCriteria simp = new SimplifierCriteria();
	     
	    simp.setSimplifierId(enfant.getId());
	    simp.setNomSimplifie(nomSimplifie);
	    simp.setPrenomSimplifie(prenomSimplifie);
	    simp.setNomPereSimplifie(nomPereSimplifie); 
	    simp.setNomMereSimplifie(nomMereSimplifie); 
	    simp.setPrenomMereSimplifie(prenomMereSimplifie); 
	    simp.setNomGrandPereSimplifie(nomGrandPereSimplifie);
	 
	    simp.setDateNaissance(dateNaissance);
	    simp.setSexe(sexe);
	    simp.setLieuNaissance(simplifier.simplify(enfant.getLieuNaissance()));
	    enfant.setSimplifierCriteria(simp);
	    enfantRepository.save(enfant);
   		
   	    }
   	System.out.println("Fin");
    }
    
    
    
    
//    @GetMapping("/all")
//    public void listCharge() {
//    	
////  	  System.out.println("debut");
////  	List<Residence> list = new ArrayList<Residence>();
////  	List<RapportEnfantQuotidien> rapps =  (List<RapportEnfantQuotidien>) rapportEnfantQuotidienRepository.findAll();
////  	for(RapportEnfantQuotidien rapp : rapps ) {
////  		if(rapp.getStatutPenal()=="libre") {
////  			Residence res = convertToResidence(rapp);
////      		rapp.setLiberation(res.getArrestation().getLiberation());
////  		}
////  		
////  	}
////  	rapportEnfantQuotidienRepository.saveAll(rapps);
////  	System.out.println("fin ");
//        System.out.println("debut");
//    	LocalDate localDateJson = LocalDate.parse("2024-08-31");
//    	LocalDateTime localDateTimeJson = localDateJson.atStartOfDay();
////   	 
////    	List<List<Residence>> residences = chargeAllEnfantService.chargeListByDate(localDateJson);
//        List<List<Residence>> residences = chargeAllEnfantService.chargeList();
//        for (List<Residence> list : residences) {
//      	  for (Residence residence : list) {
// 
//      			  
//      		  
//      		  
//      		 ObjectMapper objectMapper = new ObjectMapper();
//             String residenceJson = null;
//             
//             
//           try {
//                 // Assurez-vous que chargeList() retourne une copie immuable.
//            
//                 
//              
//
//        	   RapportEnfantQuotidienId rQId = new RapportEnfantQuotidienId();
//               rQId.setIdEnfant(residence.getResidenceId().getIdEnfant());
//             
//              // rQId.setDateSauvgarde(LocalDateTime.now());
//               rQId.setDateSauvgarde(localDateTimeJson);
//               RapportEnfantQuotidien rQ = new RapportEnfantQuotidien();
//               rQ.setRapportEnfantQuotidienId(rQId);
//               rQ.setStatutPenal(residence.getArrestation().getSituationJudiciaire());
//               rQ.setEtablissement(residence.getEtablissement());
//               rQ.setNationalite(residence.getArrestation().getEnfant().getNationalite());
//               rQ.setSexe(residence.getArrestation().getEnfant().getSexe());
//               
//            // On suppose que `residence.getArrestation().getAffaires()` retourne une liste d'affaires.
//               List<Affaire> affaires = residence.getArrestation().getAffaires(); 
//
//               // On parcourt les affaires pour trouver l'affaire principale.
//               for (Affaire affaire : affaires) {
//                   if (affaire.isAffairePrincipale()) {  // Vérifie si l'affaire est principale
//                       // On récupère le typeAffaire de cette affaire principale
//                       rQ.setTypeAffaire(affaire.getTypeAffaire());
//                       break;  // Une fois l'affaire principale trouvée, on arrête la boucle
//                   }
//               }
//
//               
//               rQ.setResidance(objectMapper.writeValueAsString( residence  ));
//
//                 
//               // Enregistrer  
//                rapportEnfantQuotidienRepository.save(rQ);
//                  
//             } catch (JsonProcessingException e) {
//                 e.printStackTrace();
//             }
//
//             
//
//           
//
//      		 
//      		  
//      	  }
//     }
//        
//        System.out.println("fin... All");
//      //  return residences;
//    }
}




//package com.cgpr.mineur.controllers;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.cgpr.mineur.models.RapportQuotidien;
//import com.cgpr.mineur.models.Residence;
//import com.cgpr.mineur.repository.RapportQuotidienRepository;
//import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@CrossOrigin(origins = "*", maxAge = 3600)
//@RestController
//@RequestMapping("/api/chargerList")
//public class ChargerListController {
//
//    @Autowired
//    private ChargeAllEnfantService chargeAllEnfantService;
//
//    @Autowired
//    private RapportQuotidienRepository rapportQuotidienRepository;
//
//    @GetMapping("/all")
//    public void listCharge() {
//        System.out.println("debut");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String residenceJson = null;
//
//      try {
//            // Assurez-vous que chargeList() retourne une copie immuable.
//       
//            
//          List<List<Residence>> residences = chargeAllEnfantService.chargeList();
//
//       // Utilisation d'une boucle for pour parcourir la liste et afficher la taille de chaque sous-liste
//       for (int i = 0; i < residences.size(); i++) {
//           List<Residence> subList = residences.get(i);
//           System.out.println("Taille de la sous-liste " + i + ": " + subList.size());
//       }
//
//            
//
//            // Sérialisation
//            residenceJson = objectMapper.writeValueAsString( residences );
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        RapportQuotidien rapportQuotidien = new RapportQuotidien();
//        rapportQuotidien.setDateSauvgarde(LocalDateTime.now());
//        rapportQuotidien.setListResidance(residenceJson.toString());
//
//        // Enregistrer  
//         rapportQuotidienRepository.save(rapportQuotidien);
//
//        System.out.println("fin... All");
//      //  return residences;
//    }
//}
// 
//



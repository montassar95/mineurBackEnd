//package com.cgpr.mineur.config;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import com.cgpr.mineur.models.Affaire;
//import com.cgpr.mineur.models.ApiResponse;
//import com.cgpr.mineur.models.RapportQuotidien;
//import com.cgpr.mineur.models.Residence;
//import com.cgpr.mineur.repository.AffaireRepository;
//import com.cgpr.mineur.repository.RapportQuotidienRepository;
//import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//
//@Configuration
//@EnableScheduling
//public class RapportConfig {
//	
//	
//	@Autowired
//	private ChargeAllEnfantService chargeAllEnfantService;
//	
//	@Autowired
//	private RapportQuotidienRepository rapportQuotidienRepository;
//	
//	@Autowired
//	private AffaireRepository affaireRepository;
//	
// 	@Scheduled(cron = "0 22 14 * * *")
// //	@Scheduled(cron = "0 03 11 * * *")
//	public void listCharge() {
//		
// 		
// 		
//		LocalDate localDateJson = LocalDate.parse("2024-10-30");
//
// 		List<List<Residence>> enfantAffiches = chargeAllEnfantService.chargeListByDate(localDateJson);
//		 
//		for (List<Residence> enfantAfficheCentre : enfantAffiches) {
//			for (int i = 0; i < enfantAfficheCentre.size(); i++) {
//
//				 
//			
//					if (enfantAfficheCentre.get(i).getResidenceId().getIdEnfant().equals("11000345")) {
//
//						List<Affaire> lesAffaires = affaireRepository.findAffairePrincipale(
//								enfantAfficheCentre.get(i).getResidenceId().getIdEnfant(),
//								enfantAfficheCentre.get(i).getResidenceId().getNumOrdinaleArrestation());
//
//						chargeAllEnfantService.updateResidence(enfantAfficheCentre.get(i), lesAffaires);
//					}
//				 
//			}
//		}
//		
//		  ObjectMapper objectMapper = new ObjectMapper();
//		//    objectMapper.setAnnotationIntrospector(new IgnoreJsonIgnoreIntrospector());
//		String residenceJson = null;
//		 
//			try {
//				residenceJson = objectMapper.writeValueAsString(enfantAffiches);
//			} catch (JsonProcessingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				 
//			}
//		RapportQuotidien rapportQuotidien = new RapportQuotidien();
//		LocalDateTime localDateTime = LocalDate.parse("2024-10-31").atStartOfDay();
//
//		rapportQuotidien.setDateSauvgarde(localDateTime);
//		rapportQuotidien.setListResidance(residenceJson);
//		
//		rapportQuotidienRepository.save(rapportQuotidien);
//		 
//	}
//
//}

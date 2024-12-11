package com.cgpr.mineur.tools;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.repository.RapportEnfantQuotidienRepository;
import com.cgpr.mineur.repository.StatistcsRepository;
import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatisticsEnfant {
	private int totalMourouj;
    private int totalSidiHani;
    private int totalMghira;
    private int totalSoukJdid;
    private int totalMjaz;
    private int totalCentre;
    private int totalEtrangerMasculin;
    private int totalEtrangerFeminin;
    private int totalTeroristMasculin;
    private int totalTeroristFeminin;
    private int totalJugeMasculin;
    private int totalJugeFeminin;
    private int totalArretMasculin;
    private int totalArretFeminin;
    
    
    public static StatisticsEnfant calculerTotaux(List<List<Residence>> enfantAffiches,
    		  StatistcsRepository statistcsRepository , RapportEnfantQuotidienRepository rapportEnfantQuotidienRepository) {
        StatisticsEnfant totals = new StatisticsEnfant();
        
        
      
        
       
       
       
        List<String> sexes = Arrays.asList("ذكر", "أنثى");
        List<Long> typeAffaires = Arrays.asList(5L);
        List<Long> nationalites = Arrays.asList(1L);
        List<String> statutPenals = Arrays.asList("juge", "arret" );
        List<String> etablissementIds = Arrays.asList("11", "12","13","14","16");
        LocalDate date = LocalDate.parse("2024-11-21");

        // Vérifiez si les listes sont non nulles et non vides
        
        	// Remplacer les listes vides par null
            if (sexes != null && sexes.isEmpty()) sexes = null;
            if (typeAffaires != null && typeAffaires.isEmpty()) typeAffaires = null;
            if (nationalites != null && nationalites.isEmpty()) nationalites = null;
            if (statutPenals != null && statutPenals.isEmpty()) statutPenals = null;
            if (etablissementIds != null && etablissementIds.isEmpty()) etablissementIds = null;

        	int  total = rapportEnfantQuotidienRepository.countByFilters(
        			sexes,
        			typeAffaires,
        			nationalites,
        			statutPenals, 
        			etablissementIds,
        			date);
            	 
            System.out.println("Total stat : " + total);
        

        
        
        if (enfantAffiches.size() > 0) {
        	 totals.setTotalMourouj(enfantAffiches.get(0).size() + enfantAffiches.get(1).size());
            totals.setTotalSidiHani(enfantAffiches.get(3).size() + enfantAffiches.get(4).size());
            totals.setTotalMghira(enfantAffiches.get(9).size() + enfantAffiches.get(10).size());
            totals.setTotalSoukJdid(enfantAffiches.get(6).size() + enfantAffiches.get(7).size());
            totals.setTotalMjaz(enfantAffiches.get(12).size() + enfantAffiches.get(13).size());
            totals.setTotalCentre(totals.getTotalMjaz() + totals.getTotalMghira() + totals.getTotalMourouj()
                    + totals.getTotalSoukJdid() + totals.getTotalSidiHani());
            
          //       Vous devez obtenir les valeurs de chargeAllEnfantService et statistcsRepository à partir de quelque part
	      //		      totals.setTotalEtrangerMasculin(chargeAllEnfantService .masculinEtranger);
		//	       totals.setTotalEtrangerFeminin(chargeAllEnfantService .femininEtranger);
                
//            totals.setTotalEtrangerMasculin(statistcsRepository .calculEtanger("ذكر" ));
//            totals.setTotalEtrangerFeminin(statistcsRepository .calculEtanger("أنثى" ));
//            
//            totals.setTotalTeroristMasculin(statistcsRepository.calculTerorist("ذكر", 5));
//            totals.setTotalTeroristFeminin(statistcsRepository.calculTerorist("أنثى", 5));
            totals.setTotalEtrangerMasculin(0);
            totals.setTotalEtrangerFeminin(0);
            
            totals.setTotalTeroristMasculin(0);
            totals.setTotalTeroristFeminin(0);
            
            
            totals.setTotalJugeMasculin(enfantAffiches.get(0).size() + enfantAffiches.get(3).size()
                    + enfantAffiches.get(6).size() + enfantAffiches.get(12).size());
            totals.setTotalJugeFeminin(enfantAffiches.get(9).size());
            totals.setTotalArretMasculin(enfantAffiches.get(1).size() + enfantAffiches.get(4).size()
                    + enfantAffiches.get(7).size() + enfantAffiches.get(13).size());
            totals.setTotalArretFeminin(enfantAffiches.get(10).size());
        }

        return totals;
    }
    
    
    public   StatisticsEnfant calculerStatRapport(
  		  LocalDate date,
      List<String> statutPenals,
    List<Etablissement> etablissements,
   List<TypeAffaire> typeAffaires,
   List<String> sexes) {
    StatisticsEnfant totals = new StatisticsEnfant();

    
  return totals;
}
}

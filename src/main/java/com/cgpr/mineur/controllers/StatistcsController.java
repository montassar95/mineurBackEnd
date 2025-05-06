package com.cgpr.mineur.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;
import com.cgpr.mineur.dto.StatistiqueMouvementDTO;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.repository.rapport.StatisticsMouvementRepositoryCustom;
import com.cgpr.mineur.repository.rapport.StatisticsRepositoryCustom;
import com.cgpr.mineur.resource.StatisticsDTO;
//import com.cgpr.mineur.service.RapportEnfantQuotidienService;
import com.cgpr.mineur.service.StatistcsService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/statistcs")
public class StatistcsController {

	@Autowired
	private StatistcsService statistcsService;

//	@Autowired
//	private RapportEnfantQuotidienService rapportEnfantQuotidienService;
	
	@Autowired
	private StatisticsRepositoryCustom statisticsRepositoryCustom;
	
	@Autowired
	private StatisticsMouvementRepositoryCustom statisticsMouvementRepositoryCustom;
	
	

	@GetMapping("/calculerStatistiques/{id}")
	public ApiResponse<StatisticsDTO> calculerStatistiques(@PathVariable("id") String id) {

		StatisticsDTO sta = statistcsService.getStatistcs(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", sta);

	}
	
	
	  
	  @GetMapping(value = "/statistiquesParDate/{date1}/{date2}", produces = "application/json")
	    public List<StatistiqueEtablissementDTO> getStatistiquesParDate(@PathVariable("date1") String date1, @PathVariable("date2") String date2) throws IOException {
	      //  return rapportEnfantQuotidienService.getStatistiquesParDate(date);
 		   LocalDate localDate1 = LocalDate.parse(date1);
 		  LocalDate localDate2 = LocalDate.parse(date2);
//		    LocalDate firstDayOfMonth = localDate.withDayOfMonth(1);
	         return statisticsRepositoryCustom.findStatistics(localDate1, localDate2);
	    }
	  @GetMapping(value = "/statistiquesMouvementsParDate/{date1}/{date2}", produces = "application/json")
	    public List<StatistiqueMouvementDTO> statistiquesMouvementsParDate(@PathVariable("date1") String date1, @PathVariable("date2") String date2) throws IOException {
	      //  return rapportEnfantQuotidienService.getStatistiquesParDate(date);
		  LocalDate localDate1 = LocalDate.parse(date1);
 		  LocalDate localDate2 = LocalDate.parse(date2);
		   
//		    LocalDate firstDayOfMonth = localDate.withDayOfMonth(1);
	         return statisticsMouvementRepositoryCustom.findStatistics(localDate1, localDate2);
	    }
}

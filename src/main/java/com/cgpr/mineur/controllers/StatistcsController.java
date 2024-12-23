package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.resource.StatisticsDTO;
import com.cgpr.mineur.service.RapportEnfantQuotidienService;
import com.cgpr.mineur.service.StatistcsService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/statistcs")
public class StatistcsController {

	@Autowired
	private StatistcsService statistcsService;

	@Autowired
	private RapportEnfantQuotidienService rapportEnfantQuotidienService;
	

	@GetMapping("/calculerStatistiques/{id}")
	public ApiResponse<StatisticsDTO> calculerStatistiques(@PathVariable("id") String id) {

		StatisticsDTO sta = statistcsService.getStatistcs(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", sta);

	}
	
	
	  @GetMapping("/statistiquesParDate/{date}")
	    public List<StatistiqueEtablissementDTO> getStatistiquesParDate(@PathVariable("date") String date) {
	        return rapportEnfantQuotidienService.getStatistiquesParDate(date);
	    }

}

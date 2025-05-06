package com.cgpr.mineur.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatistiqueNationaliteDTO {
	 
	    private String nationalite;
	    private Long nbrHommes;
	    private Long nbrFemmes;
	     
}

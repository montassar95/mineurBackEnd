package com.cgpr.mineur.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VisiteDto {
	 

	 
	private long enfantIdVisite;
	

	 
	private int anneeVisite;
	private int moisVisite;
	private int nbrVisite; 
	
 
	private ResidenceDto residenceVisite ;
	
	
 
	    
	    private EnfantDto enfant;

}

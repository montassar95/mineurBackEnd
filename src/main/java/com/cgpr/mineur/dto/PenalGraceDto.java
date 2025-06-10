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
public class PenalGraceDto {

	
	
	   private String tngrace;
	    private String codeChange;
	    private String libelleChange;
	    private int tdura;
	    private int tdurm;
	    private int tdurj;
	    private String dateGrace;
	    private String dateLiberation;
	    private String textDuree;
	    
}

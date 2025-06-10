package com.cgpr.mineur.dto;

 

import java.time.LocalDate;

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
public class AccusationExtraitJugementDTO {

	
	 private String tcodacc;
	    private String libelleFamilleAcc;
	    private String tdatdacc;
	    private String tdatfacc;
	    private int tduraccj;
	    private int tduraccm;
	    private int tduracca;
	    
	    private String periodeTotale;
	
 

}

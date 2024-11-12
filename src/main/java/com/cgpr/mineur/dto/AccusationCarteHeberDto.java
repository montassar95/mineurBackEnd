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
public class AccusationCarteHeberDto {

	 
	private AccusationCarteHeberIdDto accusationCarteHeberId;
                                   
	
	 
	private CarteHeberDto carteHeber;
 
 	
	 
	private TitreAccusationDto titreAccusation;
 

}

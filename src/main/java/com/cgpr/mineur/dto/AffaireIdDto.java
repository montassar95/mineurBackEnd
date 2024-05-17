
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
public class AffaireIdDto {

 

	 
	private String idEnfant;

	 
	private String numAffaire;

	 
	private long idTribunal;

 
	private long numOrdinaleArrestation;

}
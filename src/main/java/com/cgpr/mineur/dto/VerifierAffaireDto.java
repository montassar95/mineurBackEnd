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
public class VerifierAffaireDto {

	
	private boolean nextBoolean;
	private boolean displayAlertAffaireOrigineLier;
	private boolean displayAlertLienAutre;
	private boolean displayAlertLienMeme;
	private boolean displayAlertOrigineExistAvecLien;
	private boolean displayAlertOrigineExistSansLien;
	private boolean displayAlertAffaireLienLier;
	private boolean displayAlertLienAutreArrestation;
	private boolean displayNewAffaireOrigine;
	private boolean displayNext;
	private AffaireDto affaire;
	 
}

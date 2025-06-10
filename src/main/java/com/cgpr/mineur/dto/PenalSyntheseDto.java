package com.cgpr.mineur.dto;

import java.util.List;

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
public class PenalSyntheseDto {
	
	 
	 
     private String tnumseqaff;
	
	private String numAffaire;
	private String tribunal;
	private String dateJugement;
	
	private String situationPenal;
	private String contestation ;
	
	private String typeAffaire;
	private String accusation;
	
	
	private  String totaleJugement;
	private  String typeJugement;
	private  String dateDebutPunition;
	private  String dateFinPunition;
	
	 private int  totalCount;
	//List<PenalAffaireDTO>  penalAffaireDTOs;
	

}

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
public class ApiResultAmenPhotoDto {
	 
	 private String codgou;
	 private String codpr;
	 private String annee;
	 private String mois;
	 private String jour;
	 private String codres;
	 private String image;
 
}

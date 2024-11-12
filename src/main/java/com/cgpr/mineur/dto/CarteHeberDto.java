package com.cgpr.mineur.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
 

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CarteHeberDto extends DocumentDto {

	 

	 private List<TitreAccusationDto>  titreAccusations = new ArrayList<TitreAccusationDto>();
	private String textJugement;

 
	 

}

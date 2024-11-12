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
public class CarteDepotDto extends DocumentDto {

	 
	 

 
	private String textJugement;

	private List<TitreAccusationDto>  titreAccusations = new ArrayList<TitreAccusationDto>();
}

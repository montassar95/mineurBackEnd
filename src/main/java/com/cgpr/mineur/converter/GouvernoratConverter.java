package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.GouvernoratDto;
import com.cgpr.mineur.models.Gouvernorat;
 

 
public class GouvernoratConverter {

	public static GouvernoratDto entityToDto(Gouvernorat gouvernorat) {
	    return GouvernoratDto.builder()
	            .id(gouvernorat.getId())
	            .libelle_gouvernorat(gouvernorat.getLibelle_gouvernorat())
	            .build();
	}

	   public static Gouvernorat dtoToEntity(GouvernoratDto gouvernoratDto) {
	        return Gouvernorat.builder()
	                .id(gouvernoratDto.getId())
	                .libelle_gouvernorat(gouvernoratDto.getLibelle_gouvernorat())
	                .build();
	    }

}

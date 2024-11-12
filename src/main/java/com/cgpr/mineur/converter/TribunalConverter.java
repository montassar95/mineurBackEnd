package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.TribunalDto;
import com.cgpr.mineur.models.Tribunal;

public class TribunalConverter {

	 
	 public static TribunalDto entityToDto(Tribunal tribunal) {
	        return TribunalDto.builder()
	                .id(tribunal.getId())
	                .nom_tribunal(tribunal.getNom_tribunal())
	                .typeTribunal(TypeTribunalConverter.entityToDto(tribunal.getTypeTribunal()))
	                .gouvernorat(GouvernoratConverter.entityToDto(tribunal.getGouvernorat()))
	                .build();
	    }

	    public static Tribunal dtoToEntity(TribunalDto tribunalDto) {
	        return Tribunal.builder()
	                .id(tribunalDto.getId())
	                .nom_tribunal(tribunalDto.getNom_tribunal())
	                .typeTribunal(TypeTribunalConverter.dtoToEntity(tribunalDto.getTypeTribunal()))
	                .gouvernorat(GouvernoratConverter.dtoToEntity(tribunalDto.getGouvernorat()))
	                .build();
	    }
	    
	    
}

package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.TypeTribunalDto;
import com.cgpr.mineur.models.TypeTribunal;

public class TypeTribunalConverter {
	
	 public static TypeTribunalDto entityToDto(TypeTribunal typeTribunal) {
	        return TypeTribunalDto.builder()
	                .id(typeTribunal.getId())
	                .libelleTypeTribunal(typeTribunal.getLibelleTypeTribunal())
	                .statutNiveau(typeTribunal.getStatutNiveau())
	                .build();
	    }

	    public static TypeTribunal dtoToEntity(TypeTribunalDto typeTribunalDto) {
	        return TypeTribunal.builder()
	                .id(typeTribunalDto.getId())
	                .libelleTypeTribunal(typeTribunalDto.getLibelleTypeTribunal())
	                .statutNiveau(typeTribunalDto.getStatutNiveau())
	                .build();
	    } 
    } 
 

package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.SituationDto;
import com.cgpr.mineur.models.Situation;
 
public class SituationConverter {
	 
	   public static SituationDto entityToDto(Situation entity) {
	        return SituationDto.builder()
	                .id(entity.getId())
	                .libelle_situation(entity.getLibelle_situation())
	                .build();
	    }

	    public static Situation dtoToEntity(SituationDto dto) {
	        return Situation.builder()
	                .id(dto.getId())
	                .libelle_situation(dto.getLibelle_situation())
	                .build();
	    }
}

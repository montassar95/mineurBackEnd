package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.MotifArreterlexecutionDto;
import com.cgpr.mineur.models.MotifArreterlexecution;
 
public class MotifArreterlexecutionConverter {

	
	   public static MotifArreterlexecutionDto entityToDto(MotifArreterlexecution entity) {
	        return MotifArreterlexecutionDto.builder()
	                .id(entity.getId())
	                .libelleMotifArretere(entity.getLibelleMotifArretere())
	                .build();
	    }

	    public static MotifArreterlexecution dtoToEntity(MotifArreterlexecutionDto dto) {
	        return MotifArreterlexecution.builder()
	                .id(dto.getId())
	                .libelleMotifArretere(dto.getLibelleMotifArretere())
	                .build();
	    }
}

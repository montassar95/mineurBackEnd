package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.ArrestationIdDto;
import com.cgpr.mineur.models.ArrestationId;

 
public class ArrestationIdConverter  {

	public static ArrestationIdDto entityToDto(ArrestationId entity) {
	    return ArrestationIdDto.builder()
	            .idEnfant(entity.getIdEnfant())
	            .numOrdinale(entity.getNumOrdinale())
	            .build();
	}

	public static ArrestationId dtoToEntity(ArrestationIdDto dto) {
	    return ArrestationId.builder()
	            .idEnfant(dto.getIdEnfant())
	            .numOrdinale(dto.getNumOrdinale())
	            .build();
	}
}
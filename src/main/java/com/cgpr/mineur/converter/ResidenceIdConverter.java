package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.ResidenceIdDto;
import com.cgpr.mineur.models.ResidenceId;

 
public class ResidenceIdConverter{

	  public static ResidenceIdDto entityToDto(ResidenceId entity) {
	        return ResidenceIdDto.builder()
	                .idEnfant(entity.getIdEnfant())
	                .numOrdinaleArrestation(entity.getNumOrdinaleArrestation())
	                .numOrdinaleResidence(entity.getNumOrdinaleResidence())
	                .build();
	    }

	    public static ResidenceId dtoToEntity(ResidenceIdDto dto) {
	        return ResidenceId.builder()
	                .idEnfant(dto.getIdEnfant())
	                .numOrdinaleArrestation(dto.getNumOrdinaleArrestation())
	                .numOrdinaleResidence(dto.getNumOrdinaleResidence())
	                .build();
	    }
}
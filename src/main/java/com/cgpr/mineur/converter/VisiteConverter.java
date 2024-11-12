package com.cgpr.mineur.converter;


import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.VisiteDto;
import com.cgpr.mineur.models.Visite;
 
public class VisiteConverter {
	 

	  public static VisiteDto entityToDto(Visite entity) {
	        if (entity == null) {
	            throw new EntityNotFoundException("Cannot convert null entity");
	        }

	        return VisiteDto.builder()
	                .enfantIdVisite(entity.getEnfantIdVisite())
	                .anneeVisite(entity.getAnneeVisite())
	                .moisVisite(entity.getMoisVisite())
	                .nbrVisite(entity.getNbrVisite())
	                .residenceVisite(ResidenceConverter.entityToDto(entity.getResidenceVisite()))
	                .enfant(EnfantConverter.entityToDto(entity.getEnfant()))
	                .build();
	    }

	    public static Visite dtoToEntity(VisiteDto dto) {
	        if (dto == null) {
	            throw new EntityNotFoundException("Cannot convert null dto");
	        }

	        return Visite.builder()
	                .enfantIdVisite(dto.getEnfantIdVisite())
	                .anneeVisite(dto.getAnneeVisite())
	                .moisVisite(dto.getMoisVisite())
	                .nbrVisite(dto.getNbrVisite())
	                .residenceVisite(ResidenceConverter.dtoToEntity(dto.getResidenceVisite()))
	                .enfant(EnfantConverter.dtoToEntity(dto.getEnfant()))
	                .build();
	    }
	 
}

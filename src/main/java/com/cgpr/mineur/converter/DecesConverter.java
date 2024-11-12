package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.DecesDto;
import com.cgpr.mineur.models.Deces;
 
public class DecesConverter {
	
	 public static DecesDto entityToDto(Deces entity) {
	        if (entity == null) {
	            return null;
	        }

	        return DecesDto.builder()
	                .enfantIdDeces(entity.getEnfantIdDeces())
	                .dateDeces(entity.getDateDeces())
	                .causeDeces(CauseDecesConverter.entityToDto(entity.getCauseDeces()))
	                .lieuDeces(LieuDecesConverter.entityToDto(entity.getLieuDeces()))
	                .remarqueDeces(entity.getRemarqueDeces())
	                .residenceDeces(ResidenceConverter.entityToDto(entity.getResidenceDeces()))
	                .enfant(EnfantConverter.entityToDto(entity.getEnfant()))
	                .build();
	    }

	    public static Deces dtoToEntity(DecesDto dto) {
	        if (dto == null) {
	            return null;
	        }

	        return Deces.builder()
	                .enfantIdDeces(dto.getEnfantIdDeces())
	                .dateDeces(dto.getDateDeces())
	                .causeDeces(CauseDecesConverter.dtoToEntity(dto.getCauseDeces()))
	                .lieuDeces(LieuDecesConverter.dtoToEntity(dto.getLieuDeces()))
	                .remarqueDeces(dto.getRemarqueDeces())
	                .residenceDeces(ResidenceConverter.dtoToEntity(dto.getResidenceDeces()))
	                .enfant(EnfantConverter.dtoToEntity(dto.getEnfant()))
	                .build();
	    }

}

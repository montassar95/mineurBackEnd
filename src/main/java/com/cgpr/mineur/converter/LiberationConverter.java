package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.LiberationDto;
import com.cgpr.mineur.models.Liberation;
 
public class LiberationConverter {
	 
	public static LiberationDto entityToDto(Liberation entity) {
	    // Ensure that entity is not null to prevent NullPointerException
	    if (entity == null) {
	        return null;
	    }

	    return LiberationDto.builder()
	            .liberationId(LiberationIdConverter.entityToDto(entity.getLiberationId()))
	            .date(entity.getDate())
	            .causeLiberation(entity.getCauseLiberation() != null ? 
	                CauseLiberationConverter.entityToDto(entity.getCauseLiberation()) : null)
	            .remarqueLiberation(entity.getRemarqueLiberation())
	            .etabChangeManiere(entity.getEtabChangeManiere() != null ? 
	                EtabChangeManiereConverter.entityToDto(entity.getEtabChangeManiere()) : null)
	            .build();
	}

	public static Liberation dtoToEntity(LiberationDto dto) {
	    Liberation.LiberationBuilder builder = Liberation.builder()
	            .liberationId(LiberationIdConverter.dtoToEntity(dto.getLiberationId()))
	            .date(dto.getDate())
	            .causeLiberation(CauseLiberationConverter.dtoToEntity(dto.getCauseLiberation()))
	            .remarqueLiberation(dto.getRemarqueLiberation());

	    // Check if etabChangeManiere is present before converting
	    if (dto.getEtabChangeManiere() != null) {
	        builder.etabChangeManiere(EtabChangeManiereConverter.dtoToEntity(dto.getEtabChangeManiere()));
	    }

	    return builder.build();
	}
	
}

package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.EchappesDto;
import com.cgpr.mineur.models.Echappes;
 
public class EchappesConverter {
 
	 public static EchappesDto entityToDto(Echappes entity) {
	        return EchappesDto.builder()
	                .echappesId(EchappesIdConverter.entityToDto(entity.getEchappesId()))
	                .dateEchappes(entity.getDateEchappes())
	                .dateTrouver(entity.getDateTrouver())
	                .commentEchapper(CommentEchapperConverter.entityToDto(entity.getCommentEchapper()))
	                .commentTrouver(entity.getCommentTrouver() != null ? CommentTrouverConverter.entityToDto(entity.getCommentTrouver()): null)
	                .remarqueEchappes(entity.getRemarqueEchappes())
	                .remarqueTrouver(entity.getRemarqueTrouver())
	                .residenceEchapper(ResidenceConverter.entityToDto(entity.getResidenceEchapper()))
	                .residenceTrouver(entity.getResidenceTrouver() != null ? ResidenceConverter.entityToDto(entity.getResidenceTrouver()) : null)
	                .build();
	    }

	 public static Echappes dtoToEntity(EchappesDto dto) {
		    if (dto == null) {
		        throw new IllegalArgumentException("EchappesDto cannot be null");
		    }

		    return Echappes.builder()
		            .echappesId(EchappesIdConverter.dtoToEntity(dto.getEchappesId()))
		            .dateEchappes(dto.getDateEchappes() != null ? dto.getDateEchappes() : null) // Default to now if null
		            .dateTrouver(dto.getDateTrouver() != null ? dto.getDateTrouver() : null)
		            .commentEchapper(dto.getCommentEchapper() != null ? CommentEchapperConverter.dtoToEntity(dto.getCommentEchapper()) : null)
		            .commentTrouver(dto.getCommentTrouver() != null ? CommentTrouverConverter.dtoToEntity(dto.getCommentTrouver()) : null)
		            .remarqueEchappes(dto.getRemarqueEchappes() != null ? dto.getRemarqueEchappes() : null)
		            .remarqueTrouver(dto.getRemarqueTrouver() != null ? dto.getRemarqueTrouver() : null )
		            .residenceEchapper(dto.getResidenceEchapper() != null ? ResidenceConverter.dtoToEntity(dto.getResidenceEchapper()) : null)
		            .residenceTrouver(dto.getResidenceTrouver() != null ? ResidenceConverter.dtoToEntity(dto.getResidenceTrouver()) : null)
		            .build();
		}

	
	

}

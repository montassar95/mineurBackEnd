package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.CommentTrouverDto;
import com.cgpr.mineur.models.CommentTrouver;
 
public class CommentTrouverConverter {
 
	  public static CommentTrouverDto entityToDto(CommentTrouver entity) {
	        return CommentTrouverDto.builder()
	                .id(entity.getId())
	                .libelleComment(entity.getLibelleComment())
	                .build();
	    }

	    public static CommentTrouver dtoToEntity(CommentTrouverDto dto) {
	        return CommentTrouver.builder()
	                .id(dto.getId())
	                .libelleComment(dto.getLibelleComment())
	                .build();
	    }
}

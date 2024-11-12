package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.LieuDecesDto;
import com.cgpr.mineur.models.LieuDeces;
 
public class LieuDecesConverter {
	 
	  public static LieuDecesDto entityToDto(LieuDeces entity) {
	        return LieuDecesDto.builder()
	                .id(entity.getId())
	                .libellelieuDeces(entity.getLibellelieuDeces())
	                .build();
	    }

	    public static LieuDeces dtoToEntity(LieuDecesDto dto) {
	        return LieuDeces.builder()
	                .id(dto.getId())
	                .libellelieuDeces(dto.getLibellelieuDeces())
	                .build();
	    }

}

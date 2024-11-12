package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.CauseDecesDto;
import com.cgpr.mineur.models.CauseDeces;
 
public class CauseDecesConverter {

	public static CauseDecesDto entityToDto(CauseDeces entity) {
        return CauseDecesDto.builder()
                .id(entity.getId())
                .libelle_causeDeces(entity.getLibelle_causeDeces())
                .build();
    }

    public static CauseDeces dtoToEntity(CauseDecesDto dto) {
        return CauseDeces.builder()
                .id(dto.getId())
                .libelle_causeDeces(dto.getLibelle_causeDeces())
                .build();
    }
}

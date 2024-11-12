package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.CauseLiberationDto;
import com.cgpr.mineur.models.CauseLiberation;
 
public class CauseLiberationConverter {
 
    public static CauseLiberationDto entityToDto(CauseLiberation entity) {
        return CauseLiberationDto.builder()
                .id(entity.getId())
                .libelleCauseLiberation(entity.getLibelleCauseLiberation())
                .build();
    }

    public static CauseLiberation dtoToEntity(CauseLiberationDto dto) {
        return CauseLiberation.builder()
                .id(dto.getId())
                .libelleCauseLiberation(dto.getLibelleCauseLiberation())
                .build();
    }
}

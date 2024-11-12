package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.LiberationIdDto;
import com.cgpr.mineur.models.LiberationId;

 
public class LiberationIdConverter  {

	public static LiberationIdDto entityToDto(LiberationId entity) {
        if (entity == null) {
            return null;
        }

        return LiberationIdDto.builder()
                .idEnfant(entity.getIdEnfant())
                .numOrdinale(entity.getNumOrdinale())
                .build();
    }

    public static LiberationId dtoToEntity(LiberationIdDto dto) {
        if (dto == null) {
            return null;
        }

        return LiberationId.builder()
                .idEnfant(dto.getIdEnfant())
                .numOrdinale(dto.getNumOrdinale())
                .build();
    } 

}
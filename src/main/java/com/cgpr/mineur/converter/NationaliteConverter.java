package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.NationaliteDto;
import com.cgpr.mineur.models.Nationalite;

public class NationaliteConverter {
	
    public static NationaliteDto entityToDto(Nationalite entity) {
        return NationaliteDto.builder()
                .id(entity.getId())
                .libelle_nationalite(entity.getLibelle_nationalite())
                .build();
    }

    public static Nationalite dtoToEntity(NationaliteDto dto) {
        return Nationalite.builder()
                .id(dto.getId())
                .libelle_nationalite(dto.getLibelle_nationalite())
                .build();
    }
}

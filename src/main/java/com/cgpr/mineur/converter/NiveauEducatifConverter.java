package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.NiveauEducatifDto;
import com.cgpr.mineur.models.NiveauEducatif;
 
public class NiveauEducatifConverter {
	 
	public static NiveauEducatifDto entityToDto(NiveauEducatif entity) {
        return NiveauEducatifDto.builder()
                .id(entity.getId())
                .libelle_niveau_educatif(entity.getLibelle_niveau_educatif())
                .build();
    }

    public static NiveauEducatif dtoToEntity(NiveauEducatifDto dto) {
        return NiveauEducatif.builder()
                .id(dto.getId())
                .libelle_niveau_educatif(dto.getLibelle_niveau_educatif())
                .build();
    }
}

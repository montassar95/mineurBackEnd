package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.SituationFamilialeDto;
import com.cgpr.mineur.models.SituationFamiliale;
 
public class SituationFamilialeConverter {
	 
    public static SituationFamilialeDto entityToDto(SituationFamiliale entity) {
        return SituationFamilialeDto.builder()
                .id(entity.getId())
                .libelle_situation_familiale(entity.getLibelle_situation_familiale())
                .build();
    }

    public static SituationFamiliale dtoToEntity(SituationFamilialeDto dto) {
        return SituationFamiliale.builder()
                .id(dto.getId())
                .libelle_situation_familiale(dto.getLibelle_situation_familiale())
                .build();
    }
}

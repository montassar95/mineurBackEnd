package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.ResultatTransfertDto;
import com.cgpr.mineur.models.ResultatTransfert;

public class ResultatTransfertConverter {
	 

    public static ResultatTransfertDto entityToDto(ResultatTransfert entity) {
        return ResultatTransfertDto.builder()
                .id(entity.getId())
                .libelle_resultat(entity.getLibelle_resultat())
                .build();
    }

    public static ResultatTransfert dtoToEntity(ResultatTransfertDto dto) {
        return ResultatTransfert.builder()
                .id(dto.getId())
                .libelle_resultat(dto.getLibelle_resultat())
                .build();
    }
}
 

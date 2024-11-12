package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.MetierDto;
import com.cgpr.mineur.models.Metier;
 
public class MetierConverter {

    public static MetierDto entityToDto(Metier entity) {
        return MetierDto.builder()
                .id(entity.getId())
                .libelle_metier(entity.getLibelle_metier())
                .build();
    }

    public static Metier dtoToEntity(MetierDto dto) {
        return Metier.builder()
                .id(dto.getId())
                .libelle_metier(dto.getLibelle_metier())
                .build();
    }

}

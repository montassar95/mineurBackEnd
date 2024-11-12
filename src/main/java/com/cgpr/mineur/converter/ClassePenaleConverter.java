package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.ClassePenaleDto;
import com.cgpr.mineur.models.ClassePenale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

 
public class ClassePenaleConverter {



    public static ClassePenaleDto entityToDto(ClassePenale entity) {
        return ClassePenaleDto.builder()
                .id(entity.getId())
                .libelle_classe_penale(entity.getLibelle_classe_penale())
                .build();
    }

    public static ClassePenale dtoToEntity(ClassePenaleDto dto) {
        return ClassePenale.builder()
                .id(dto.getId())
                .libelle_classe_penale(dto.getLibelle_classe_penale())
                .build();
    }

}

package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.CauseMutationDto;
import com.cgpr.mineur.models.CauseMutation;
 
public class CauseMutationConverter {
	 
    public static CauseMutationDto entityToDto(CauseMutation entity) {
        return CauseMutationDto.builder()
                .id(entity.getId())
                .libelle_causeMutation(entity.getLibelle_causeMutation())
                .build();
    }

    public static CauseMutation dtoToEntity(CauseMutationDto dto) {
        return CauseMutation.builder()
                .id(dto.getId())
                .libelle_causeMutation(dto.getLibelle_causeMutation())
                .build();
    }

}

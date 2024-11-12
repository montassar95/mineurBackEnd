package com.cgpr.mineur.converter;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.TitreAccusationDto;
import com.cgpr.mineur.models.TitreAccusation;
 
public class TitreAccusationConverter {

	public static TitreAccusationDto entityToDto(TitreAccusation entity) {
        if (entity == null) {
            throw new EntityNotFoundException("Cannot convert null entity");
        }

        TitreAccusationDto dto = TitreAccusationDto.builder()
                .id(entity.getId())
                .titreAccusation(entity.getTitreAccusation())
                .typeAffaire(TypeAffaireConverter.entityToDto(entity.getTypeAffaire()))
                .build();

        return dto;
    }

    public static TitreAccusation dtoToEntity(TitreAccusationDto dto) {
        if (dto == null) {
            throw new EntityNotFoundException("Cannot convert null dto");
        }

        TitreAccusation entity = new TitreAccusation();
        entity.setId(dto.getId());
        entity.setTitreAccusation(dto.getTitreAccusation());
        entity.setTypeAffaire(TypeAffaireConverter.dtoToEntity(dto.getTypeAffaire()));

        return entity;
    }

	 

}

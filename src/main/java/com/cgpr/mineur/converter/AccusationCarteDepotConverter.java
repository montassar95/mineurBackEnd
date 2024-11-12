package com.cgpr.mineur.converter;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.AccusationCarteDepotDto;
import com.cgpr.mineur.models.AccusationCarteDepot;

public class AccusationCarteDepotConverter {


	
	public static AccusationCarteDepotDto entityToDto(AccusationCarteDepot entity) {
        if (entity == null) {
            throw new EntityNotFoundException("Cannot convert null entity");
        }

        return AccusationCarteDepotDto.builder()
                .accusationCarteDepotId(AccusationCarteDepotIdConverter.entityToDto(entity.getAccusationCarteDepotId()))
                .carteDepot(CarteDepotConverter.entityToDto(entity.getCarteDepot()))
                .titreAccusation(TitreAccusationConverter.entityToDto(entity.getTitreAccusation()))
                .build();
    }

    public static AccusationCarteDepot dtoToEntity(AccusationCarteDepotDto dto) {
        if (dto == null) {
            throw new EntityNotFoundException("Cannot convert null dto");
        }

        return AccusationCarteDepot.builder()
                .accusationCarteDepotId(AccusationCarteDepotIdConverter.dtoToEntity(dto.getAccusationCarteDepotId()))
                .carteDepot(CarteDepotConverter.dtoToEntity(dto.getCarteDepot()))
                .titreAccusation(TitreAccusationConverter.dtoToEntity(dto.getTitreAccusation()))
                .build();
    }
}

package com.cgpr.mineur.converter;
import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.AccusationCarteHeberDto;
import com.cgpr.mineur.models.AccusationCarteHeber;

 
public class AccusationCarteHeberConverter {

	 
	public static AccusationCarteHeberDto entityToDto(AccusationCarteHeber entity) {
        if (entity == null) {
            throw new EntityNotFoundException("Cannot convert null entity");
        }

        return AccusationCarteHeberDto.builder()
                .accusationCarteHeberId(AccusationCarteHeberIdConverter.entityToDto(entity.getAccusationCarteHeberId()))
                .carteHeber(CarteHeberConverter.entityToDto(entity.getCarteHeber()))
                .titreAccusation(TitreAccusationConverter.entityToDto(entity.getTitreAccusation()))
                .build();
    }

    public static AccusationCarteHeber dtoToEntity(AccusationCarteHeberDto dto) {
        if (dto == null) {
            throw new EntityNotFoundException("Cannot convert null dto");
        }

        return AccusationCarteHeber.builder()
                .accusationCarteHeberId(AccusationCarteHeberIdConverter.dtoToEntity(dto.getAccusationCarteHeberId()))
                .carteHeber(CarteHeberConverter.dtoToEntity(dto.getCarteHeber()))
                .titreAccusation(TitreAccusationConverter.dtoToEntity(dto.getTitreAccusation()))
                .build();
    }
 

}

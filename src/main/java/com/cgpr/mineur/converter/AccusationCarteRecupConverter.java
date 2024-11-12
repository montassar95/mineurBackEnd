package com.cgpr.mineur.converter;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.AccusationCarteRecupDto;
import com.cgpr.mineur.models.AccusationCarteRecup;
 
public class AccusationCarteRecupConverter {

                     
	  public static AccusationCarteRecupDto entityToDto(AccusationCarteRecup entity) {
	        if (entity == null) {
	            throw new EntityNotFoundException("Cannot convert null entity");
	        }

	        return AccusationCarteRecupDto.builder()
	                .accusationCarteRecupId(AccusationCarteRecupIdConverter.entityToDto(entity.getAccusationCarteRecupId()))
	                .textAccusation(entity.getTextAccusation())
	                .jour(entity.getJour())
	                .mois(entity.getMois())
	                .annee(entity.getAnnee())
	                .numOridinel(entity.getNumOridinel())
	                .numOridinelLiee(entity.getNumOridinelLiee())
	                .dateDebut(entity.getDateDebut())
	                .dateFin(entity.getDateFin())
//	                .carteRecup(CarteRecupConverter.entityToDto(entity.getCarteRecup()))
	                .titreAccusation(TitreAccusationConverter.entityToDto(entity.getTitreAccusation()))
	                .build();
	    }

	    public static AccusationCarteRecup dtoToEntity(AccusationCarteRecupDto dto) {
	        if (dto == null) {
	            throw new EntityNotFoundException("Cannot convert null dto");
	        }

	        return AccusationCarteRecup.builder()
	                .accusationCarteRecupId(AccusationCarteRecupIdConverter.dtoToEntity(dto.getAccusationCarteRecupId()))
	                .textAccusation(dto.getTextAccusation())
	                .jour(dto.getJour())
	                .mois(dto.getMois())
	                .annee(dto.getAnnee())
	                .numOridinel(dto.getNumOridinel())
	                .numOridinelLiee(dto.getNumOridinelLiee())
	                .dateDebut(dto.getDateDebut())
	                .dateFin(dto.getDateFin())
//	                .carteRecup(dto.getCarteRecup() != null ? CarteRecupConverter.dtoToEntity(dto.getCarteRecup()) : null) 
	                .titreAccusation(TitreAccusationConverter.dtoToEntity(dto.getTitreAccusation()))
	                .build();
	    }
	
}


package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.AccusationCarteRecupIdDto;
import com.cgpr.mineur.models.AccusationCarteRecupId;

 
public class AccusationCarteRecupIdConverter  {
	
	public static AccusationCarteRecupIdDto entityToDto(AccusationCarteRecupId entity) {
	    return AccusationCarteRecupIdDto.builder()
	            .idEnfant(entity.getIdEnfant())
	            .numOrdinalArrestation(entity.getNumOrdinalArrestation())
	            .numOrdinalAffaire(entity.getNumOrdinalAffaire())
	            .numOrdinalDoc(entity.getNumOrdinalDoc())
	            .numOrdinalDocByAffaire(entity.getNumOrdinalDocByAffaire())
	            .idTitreAccusation(entity.getIdTitreAccusation())
	            .build();
	}

	public static AccusationCarteRecupId dtoToEntity(AccusationCarteRecupIdDto dto) {
	    return AccusationCarteRecupId.builder()
	            .idEnfant(dto.getIdEnfant())
	            .numOrdinalArrestation(dto.getNumOrdinalArrestation())
	            .numOrdinalAffaire(dto.getNumOrdinalAffaire())
	            .numOrdinalDoc(dto.getNumOrdinalDoc())
	            .numOrdinalDocByAffaire(dto.getNumOrdinalDocByAffaire())
	            .idTitreAccusation(dto.getIdTitreAccusation())
	            .build();
	}

 
	
	
	
	
 
}
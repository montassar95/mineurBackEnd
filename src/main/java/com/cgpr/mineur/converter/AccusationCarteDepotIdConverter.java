
package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.AccusationCarteDepotIdDto;
import com.cgpr.mineur.models.AccusationCarteDepotId;

 
public class AccusationCarteDepotIdConverter {

	  public static AccusationCarteDepotIdDto entityToDto(AccusationCarteDepotId entity) {
	        if (entity == null) {
	            return null;
	        }

	        return AccusationCarteDepotIdDto.builder()
	                .idEnfant(entity.getIdEnfant())
	                .numOrdinalArrestation(entity.getNumOrdinalArrestation())
	                .numOrdinalAffaire(entity.getNumOrdinalAffaire())
	                .numOrdinalDoc(entity.getNumOrdinalDoc())
	                .numOrdinalDocByAffaire(entity.getNumOrdinalDocByAffaire())
	                .idTitreAccusation(entity.getIdTitreAccusation())
	                .build();
	    }

	    public static AccusationCarteDepotId dtoToEntity(AccusationCarteDepotIdDto dto) {
	        if (dto == null) {
	            return null;
	        }

	        return AccusationCarteDepotId.builder()
	                .idEnfant(dto.getIdEnfant())
	                .numOrdinalArrestation(dto.getNumOrdinalArrestation())
	                .numOrdinalAffaire(dto.getNumOrdinalAffaire())
	                .numOrdinalDoc(dto.getNumOrdinalDoc())
	                .numOrdinalDocByAffaire(dto.getNumOrdinalDocByAffaire())
	                .idTitreAccusation(dto.getIdTitreAccusation())
	                .build();
	    }

}
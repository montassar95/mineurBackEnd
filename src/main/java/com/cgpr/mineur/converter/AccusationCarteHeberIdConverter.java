
package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.AccusationCarteHeberIdDto;
import com.cgpr.mineur.models.AccusationCarteHeberId;

 
public class AccusationCarteHeberIdConverter   {

 

	   public static AccusationCarteHeberIdDto entityToDto(AccusationCarteHeberId entity) {
	        return AccusationCarteHeberIdDto.builder()
	                .idEnfant(entity.getIdEnfant())
	                .numOrdinalArrestation(entity.getNumOrdinalArrestation())
	                .numOrdinalAffaire(entity.getNumOrdinalAffaire())
	                .numOrdinalDoc(entity.getNumOrdinalDoc())
	                .numOrdinalDocByAffaire(entity.getNumOrdinalDocByAffaire())
	                .idTitreAccusation(entity.getIdTitreAccusation())
	                .build();
	    }

	    public static AccusationCarteHeberId dtoToEntity(AccusationCarteHeberIdDto dto) {
	        return AccusationCarteHeberId.builder()
	                .idEnfant(dto.getIdEnfant())
	                .numOrdinalArrestation(dto.getNumOrdinalArrestation())
	                .numOrdinalAffaire(dto.getNumOrdinalAffaire())
	                .numOrdinalDoc(dto.getNumOrdinalDoc())
	                .numOrdinalDocByAffaire(dto.getNumOrdinalDocByAffaire())
	                .idTitreAccusation(dto.getIdTitreAccusation())
	                .build();
	    }
 
	
	
	
	
 
}
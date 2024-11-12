
package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.AffaireIdDto;
import com.cgpr.mineur.models.AffaireId;

 
public class AffaireIdConverter {

 

	public static AffaireIdDto entityToDto(AffaireId entity) {
	    return AffaireIdDto.builder()
	            .idEnfant(entity.getIdEnfant())
	            .numAffaire(entity.getNumAffaire())
	            .idTribunal(entity.getIdTribunal())
	            .numOrdinaleArrestation(entity.getNumOrdinaleArrestation())
	            .build();
	}


	public static AffaireId dtoToEntity(AffaireIdDto dto) {
	    return AffaireId.builder()
	            .idEnfant(dto.getIdEnfant())
	            .numAffaire(dto.getNumAffaire())
	            .idTribunal(dto.getIdTribunal())
	            .numOrdinaleArrestation(dto.getNumOrdinaleArrestation())
	            .build();
	}

}
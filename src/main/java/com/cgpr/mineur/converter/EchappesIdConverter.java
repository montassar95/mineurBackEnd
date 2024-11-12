package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.EchappesIdDto;
import com.cgpr.mineur.models.EchappesId;

 
public class EchappesIdConverter   {

	public static EchappesIdDto entityToDto(EchappesId entity) {
        return EchappesIdDto.builder()
                .idEnfant(entity.getIdEnfant())
                .numOrdinaleArrestation(entity.getNumOrdinaleArrestation())
                .numOrdinaleEchappes(entity.getNumOrdinaleEchappes())
                .build();
    }

    public static EchappesId dtoToEntity(EchappesIdDto dto) {
        return EchappesId.builder()
                .idEnfant(dto.getIdEnfant())
                .numOrdinaleArrestation(dto.getNumOrdinaleArrestation())
                .numOrdinaleEchappes(dto.getNumOrdinaleEchappes())
                .build();
    }
	  
}
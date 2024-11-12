package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.EtabChangeManiereDto;
import com.cgpr.mineur.models.EtabChangeManiere;
 
public class EtabChangeManiereConverter {
	 
	   public static EtabChangeManiereDto entityToDto(EtabChangeManiere entity) {
	        return EtabChangeManiereDto.builder()
	                .id(entity.getId())
	                .libelle_etabChangeManiere(entity.getLibelle_etabChangeManiere())
	                .gouvernorat(GouvernoratConverter.entityToDto(entity.getGouvernorat()))
	                .build();
	    }

	    public static EtabChangeManiere dtoToEntity(EtabChangeManiereDto dto) {
	        return EtabChangeManiere.builder()
	                .id(dto.getId())
	                .libelle_etabChangeManiere(dto.getLibelle_etabChangeManiere())
	                .gouvernorat(GouvernoratConverter.dtoToEntity(dto.getGouvernorat()))
	                .build();
	    }
 
}

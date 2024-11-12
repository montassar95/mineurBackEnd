package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.ArretProvisoireIdDto;
import com.cgpr.mineur.models.ArretProvisoireId;
 
public class ArretProvisoireIdConverter   {

	 
	public static ArretProvisoireIdDto entityToDto(ArretProvisoireId entity) {
	    return ArretProvisoireIdDto.builder()
	            .idEnfant(entity.getIdEnfant())
	            .numOrdinalArrestation(entity.getNumOrdinalArrestation())
	            .numOrdinalAffaire(entity.getNumOrdinalAffaire())
	            .numOrdinalzDoc(entity.getNumOrdinalzDoc())
	            .numOrdinalDocByAffaire(entity.getNumOrdinalDocByAffaire())
	            .numOrdinalArret(entity.getNumOrdinalArret())
	            .build();
	}

	public static ArretProvisoireId dtoToEntity(ArretProvisoireIdDto dto) {
	    return ArretProvisoireId.builder()
	            .idEnfant(dto.getIdEnfant())
	            .numOrdinalArrestation(dto.getNumOrdinalArrestation())
	            .numOrdinalAffaire(dto.getNumOrdinalAffaire())
	            .numOrdinalzDoc(dto.getNumOrdinalzDoc())
	            .numOrdinalDocByAffaire(dto.getNumOrdinalDocByAffaire())
	            .numOrdinalArret(dto.getNumOrdinalArret())
	            .build();
	}

}
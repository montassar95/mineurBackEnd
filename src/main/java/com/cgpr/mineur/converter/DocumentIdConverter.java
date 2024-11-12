package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.DocumentIdDto;
import com.cgpr.mineur.models.DocumentId;

 
public class DocumentIdConverter  {

	   public static DocumentIdDto entityToDto(DocumentId entity) {
	        return DocumentIdDto.builder()
	                .idEnfant(entity.getIdEnfant())
	                .numOrdinalArrestation(entity.getNumOrdinalArrestation())
	                .numOrdinalAffaire(entity.getNumOrdinalAffaire())
	                .numOrdinalDoc(entity.getNumOrdinalDoc())
	                .numOrdinalDocByAffaire(entity.getNumOrdinalDocByAffaire())
	                .build();
	    }

	    public static DocumentId dtoToEntity(DocumentIdDto dto) {
	        return DocumentId.builder()
	                .idEnfant(dto.getIdEnfant())
	                .numOrdinalArrestation(dto.getNumOrdinalArrestation())
	                .numOrdinalAffaire(dto.getNumOrdinalAffaire())
	                .numOrdinalDoc(dto.getNumOrdinalDoc())
	                .numOrdinalDocByAffaire(dto.getNumOrdinalDocByAffaire())
	                .build();
	    }
 

}
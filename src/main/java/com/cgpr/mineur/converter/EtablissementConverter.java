package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.EtablissementDto;
import com.cgpr.mineur.models.Etablissement;
 
public class EtablissementConverter {
	 
	   public static EtablissementDto entityToDto(Etablissement entity) {
	        return EtablissementDto.builder()
	                .id(entity.getId())
	                .libelle_etablissement(entity.getLibelle_etablissement())
	                .gouvernorat(GouvernoratConverter.entityToDto(entity.getGouvernorat()))
	                .statut(entity.getStatut())
	                .build();
	    }

	    public static Etablissement dtoToEntity(EtablissementDto dto) {
	        return Etablissement.builder()
	                .id(dto.getId())
	                .libelle_etablissement(dto.getLibelle_etablissement())
	                .gouvernorat(GouvernoratConverter.dtoToEntity(dto.getGouvernorat()))
	                .statut(dto.getStatut())
	                .build();
	    }
 
}

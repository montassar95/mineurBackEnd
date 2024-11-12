package com.cgpr.mineur.converter;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.ArretProvisoireDto;
import com.cgpr.mineur.models.ArretProvisoire;


 
public class ArretProvisoireConverter {

	 
	   public static ArretProvisoireDto entityToDto(ArretProvisoire entity) {
	        if (entity == null) {
	            throw new EntityNotFoundException("Cannot convert null entity");
	        }

	        return ArretProvisoireDto.builder()
//	        		   .arretProvisoireId(ArretProvisoireIdConverter.entityToDto(entity.getArretProvisoireId()))
	                .jour(entity.getJour())
	                .mois(entity.getMois())
	                .annee(entity.getAnnee())
	                .dateDebut(entity.getDateDebut())
	                .dateFin(entity.getDateFin())
//	                .carteRecup(CarteRecupConverter.entityToDto(entity.getCarteRecup()))
//	                .carteRecup(entity.getCarteRecup() != null ? CarteRecupConverter.entityToDto(entity.getCarteRecup()) : null) 
	                .daysDiff(entity.getDaysDiff())
	                .build();
	    }

	    public static ArretProvisoire dtoToEntity(ArretProvisoireDto dto) {
	        if (dto == null) {
	            throw new EntityNotFoundException("Cannot convert null dto");
	        }

	        return ArretProvisoire.builder()
//	        		 .arretProvisoireId(ArretProvisoireIdConverter.dtoToEntity(dto.getArretProvisoireId()))
	                .jour(dto.getJour())
	                .mois(dto.getMois())
	                .annee(dto.getAnnee())
	                .dateDebut(dto.getDateDebut())
	                .dateFin(dto.getDateFin())
	              //  .carteRecup(dto.getCarteRecup() != null ? CarteRecupConverter.dtoToEntity(dto.getCarteRecup()) : null) 
	                .daysDiff(dto.getDaysDiff())
	                .build();
	    }
	 
}

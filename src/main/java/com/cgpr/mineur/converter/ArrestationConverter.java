package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.ArrestationDto;
import com.cgpr.mineur.models.Arrestation;
 
public class ArrestationConverter {
	 
 
	public static ArrestationDto entityToDto(Arrestation entity) {
	    // Ensure that entity is not null to prevent NullPointerException
	    if (entity == null) {
	        return null;
	    }

	    return ArrestationDto.builder()
	            .arrestationId(ArrestationIdConverter.entityToDto(entity.getArrestationId()))
	            .enfant(entity.getEnfant() != null ? EnfantConverter.entityToDto(entity.getEnfant()) : null)
	            .date(entity.getDate())
	            .statut(entity.getStatut())
	            .etatJuridique(entity.getEtatJuridique())
	            .liberation(entity.getLiberation() != null ? 
	                LiberationConverter.entityToDto(entity.getLiberation()) : null)
	            .numAffairePricipale(entity.getNumAffairePricipale())
	            .tribunalPricipale(entity.getTribunalPricipale() != null ? 
	                TribunalConverter.entityToDto(entity.getTribunalPricipale()) : null)
	            .numOrdinalAffairePricipale(entity.getNumOrdinalAffairePricipale())
	            .typeAffairePricipale(entity.getTypeAffairePricipale() != null ? 
	                TypeAffaireConverter.entityToDto(entity.getTypeAffairePricipale()) : null)
	            .totaleEchappes(entity.getTotaleEchappes())
	            .totaleResidence(entity.getTotaleResidence())
	            .dateDebut(entity.getDateDebut())
	            .dateFin(entity.getDateFin())
	            .echappeDto(entity.getEchappe() != null ? 
	                EchappesConverter.entityToDto(entity.getEchappe()) : null)
	            .situationJudiciaire(entity.getSituationJudiciaire())
	            .age(entity.getAge())
	            //.visite(entity.getVisite()) // Uncomment if visite is needed and handle similarly
	            .build();
	}

	public static Arrestation dtoToEntity(ArrestationDto dto) {
	    if (dto == null) {
	        return null;
	    }

	    // Utilisation du builder pour créer l'entité
	    return Arrestation.builder()
	            .arrestationId(ArrestationIdConverter.dtoToEntity(dto.getArrestationId()))
	            .enfant(dto.getEnfant() != null ? EnfantConverter.dtoToEntity(dto.getEnfant()) : null)
	            .date(dto.getDate())
	            .statut(dto.getStatut())
	            .etatJuridique(dto.getEtatJuridique())
	            .liberation(dto.getLiberation() != null ? LiberationConverter.dtoToEntity(dto.getLiberation()) : null)
	            .numAffairePricipale(dto.getNumAffairePricipale())
	            .tribunalPricipale(dto.getTribunalPricipale() != null ? TribunalConverter.dtoToEntity(dto.getTribunalPricipale()) : null)
	            .numOrdinalAffairePricipale(dto.getNumOrdinalAffairePricipale())
	            .typeAffairePricipale(dto.getTypeAffairePricipale() != null ? TypeAffaireConverter.dtoToEntity(dto.getTypeAffairePricipale()) : null)
	            .totaleEchappes(dto.getTotaleEchappes())
	            .totaleResidence(dto.getTotaleResidence())
	            .dateDebut(dto.getDateDebut())
	            .dateFin(dto.getDateFin())
	            .echappe(dto.getEchappeDto() != null ? EchappesConverter.dtoToEntity(dto.getEchappeDto()) : null)
	            .situationJudiciaire(dto.getSituationJudiciaire())
	            .age(dto.getAge())
	            //.visite(dto.getVisite()) // Décommentez si vous avez un champ 'visite' dans Arrestation
	            .build();
	}
 
}

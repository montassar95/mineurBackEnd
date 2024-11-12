package com.cgpr.mineur.converter;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cgpr.mineur.dto.AffaireDto;
import com.cgpr.mineur.models.Affaire;

 
public class AffaireConverter {

	public static AffaireDto entityToDto(Affaire entity) {
	    // Assurez-vous que l'entité n'est pas nulle
	    if (entity == null) {
	        return null;
	    }

	    return AffaireDto.builder()
	            .affaireId(AffaireIdConverter.entityToDto(entity.getAffaireId()))
	            .arrestation(entity.getArrestation() != null ? ArrestationConverter.entityToDto(entity.getArrestation()) : null)
	            .numOrdinalAffaire(entity.getNumOrdinalAffaire())
	            .numOrdinalAffaireByAffaire(entity.getNumOrdinalAffaireByAffaire())
	            .tribunal(entity.getTribunal() != null ? TribunalConverter.entityToDto(entity.getTribunal()) : null)
	            .affaireLien(entity.getAffaireLien() != null ? entityToDto(entity.getAffaireLien()) : null)
	            .affaireAffecter(entity.getAffaireAffecter() != null ? entityToDto(entity.getAffaireAffecter()) : null)
	            .statut(entity.getStatut())
	            .documents(entity.getDocuments() != null ? 
	             entity.getDocuments().stream().map(DocumentConverter::entityToDto).collect(Collectors.toList()) : null)
	            .titreAccusations(entity.getTitreAccusations() != null ? 
	              entity.getTitreAccusations().stream().map(TitreAccusationConverter::entityToDto).collect(Collectors.toList()) : null)
	            .typeDocument(entity.getTypeDocument())
	            .typeDocumentActuelle(entity.getTypeDocumentActuelle())
	            .dateEmissionDocument(entity.getDateEmissionDocument())
	            .typeAffaire(entity.getTypeAffaire() != null ? TypeAffaireConverter.entityToDto(entity.getTypeAffaire()) : null)
	            .jour(entity.getJour())
	            .mois(entity.getMois())
	            .annee(entity.getAnnee())
	            .jourArret(entity.getJourArret())
	            .moisArret(entity.getMoisArret())
	            .anneeArret(entity.getAnneeArret())
	            .typeFile(entity.getTypeFile())
	            .typeJuge(entity.getTypeJuge() != null ? TypeJugeConverter.entityToDto(entity.getTypeJuge()) : null)
	            .dateEmission(entity.getDateEmission())
	            .dateDebutPunition(entity.getDateDebutPunition())
	            .dateFinPunition(entity.getDateFinPunition())
	            .daysDiffJuge(entity.getDaysDiffJuge())
	            .affairePrincipale(entity.isAffairePrincipale())
	            .build();
	}

	
	public static AffaireDto entityToDtoBasic(Affaire entity) {
	    // Assurez-vous que l'entité n'est pas nulle
	    if (entity == null) {
	        return null;
	    }

	    return AffaireDto.builder()
	    		 .affaireId(AffaireIdConverter.entityToDto(entity.getAffaireId()))
		            .arrestation(entity.getArrestation() != null ? ArrestationConverter.entityToDto(entity.getArrestation()) : null)
		            .numOrdinalAffaire(entity.getNumOrdinalAffaire())
		            .numOrdinalAffaireByAffaire(entity.getNumOrdinalAffaireByAffaire())
		            .tribunal(entity.getTribunal() != null ? TribunalConverter.entityToDto(entity.getTribunal()) : null)
		            .affaireLien(entity.getAffaireLien() != null ? entityToDto(entity.getAffaireLien()) : null)
		            .affaireAffecter(entity.getAffaireAffecter() != null ? entityToDto(entity.getAffaireAffecter()) : null)
		            .statut(entity.getStatut())
//		            .documents(entity.getDocuments() != null ? 
//		                entity.getDocuments().stream().map(DocumentConverter::entityToDto).collect(Collectors.toList()) : null)
		            .titreAccusations(entity.getTitreAccusations() != null ? 
		                entity.getTitreAccusations().stream().map(TitreAccusationConverter::entityToDto).collect(Collectors.toList()) : null)
		            .typeDocument(entity.getTypeDocument())
		            .typeDocumentActuelle(entity.getTypeDocumentActuelle())
		            .dateEmissionDocument(entity.getDateEmissionDocument())
		            .typeAffaire(entity.getTypeAffaire() != null ? TypeAffaireConverter.entityToDto(entity.getTypeAffaire()) : null)
		            .jour(entity.getJour())
		            .mois(entity.getMois())
		            .annee(entity.getAnnee())
		            .jourArret(entity.getJourArret())
		            .moisArret(entity.getMoisArret())
		            .anneeArret(entity.getAnneeArret())
		            .typeFile(entity.getTypeFile())
		            .typeJuge(entity.getTypeJuge() != null ? TypeJugeConverter.entityToDto(entity.getTypeJuge()) : null)
		            .dateEmission(entity.getDateEmission())
		            .dateDebutPunition(entity.getDateDebutPunition())
		            .dateFinPunition(entity.getDateFinPunition())
		            .daysDiffJuge(entity.getDaysDiffJuge())
		            .affairePrincipale(entity.isAffairePrincipale())
		            .build();
	}
	
	
	  public static Affaire dtoToEntitybasic(AffaireDto dto) {
	        return Affaire.builder()
	                .affaireId(AffaireIdConverter.dtoToEntity(dto.getAffaireId()))
	                .arrestation(ArrestationConverter.dtoToEntity(dto.getArrestation()))
	                .numOrdinalAffaire(dto.getNumOrdinalAffaire())
	                .numOrdinalAffaireByAffaire(dto.getNumOrdinalAffaireByAffaire())
	                .tribunal(TribunalConverter.dtoToEntity(dto.getTribunal()))
	                .affaireLien(dto.getAffaireLien() != null ? dtoToEntity(dto.getAffaireLien()) : null)
	                .affaireAffecter(dto.getAffaireAffecter() != null ? dtoToEntity(dto.getAffaireAffecter()) : null)
	                .statut(dto.getStatut())
	                .typeAffaire(dto.getTypeAffaire()!=null   ? TypeAffaireConverter.dtoToEntity(dto.getTypeAffaire()):null)
	                .build();
	    }
	  
	  
    public static Affaire dtoToEntity(AffaireDto dto) {
        return Affaire.builder()
                .affaireId(AffaireIdConverter.dtoToEntity(dto.getAffaireId()))
                .arrestation(ArrestationConverter.dtoToEntity(dto.getArrestation()))
                .numOrdinalAffaire(dto.getNumOrdinalAffaire())
                .numOrdinalAffaireByAffaire(dto.getNumOrdinalAffaireByAffaire())
                .tribunal(TribunalConverter.dtoToEntity(dto.getTribunal()))
                .affaireLien(dto.getAffaireLien() != null ? dtoToEntity(dto.getAffaireLien()) : null)
                .affaireAffecter(dto.getAffaireAffecter() != null ? dtoToEntity(dto.getAffaireAffecter()) : null)
                .statut(dto.getStatut())
//               .documents(dto.getDocuments().stream().map(DocumentConverter::dtoToEntity).collect(Collectors.toList())) // si il exisit
             .documents(Optional.ofNullable(dto.getDocuments())
                    .map(docs -> docs.stream()
                            .map(DocumentConverter::dtoToEntity)
                            .collect(Collectors.toList()))
           .orElse(Collections.emptyList()))
                
             .titreAccusations(Optional.ofNullable(dto.getTitreAccusations())
                     .map(docs -> docs.stream()
                                      .map(TitreAccusationConverter::dtoToEntity)
                                      .collect(Collectors.toList()))
                     .orElse(Collections.emptyList()))
                
//                .titreAccusations(dto.getTitreAccusations().stream().map(TitreAccusationConverter::dtoToEntity).collect(Collectors.toList()))
                .typeDocument(dto.getTypeDocument())
                .typeDocumentActuelle(dto.getTypeDocumentActuelle())
                .dateEmissionDocument(dto.getDateEmissionDocument())
                .typeAffaire(dto.getTypeAffaire()!=null   ? TypeAffaireConverter.dtoToEntity(dto.getTypeAffaire()):null)
                .jour(dto.getJour())
                .mois(dto.getMois())
                .annee(dto.getAnnee())
                .jourArret(dto.getJourArret())
                .moisArret(dto.getMoisArret())
                .anneeArret(dto.getAnneeArret())
                .typeFile(dto.getTypeFile())
                .typeJuge(Optional.ofNullable(dto.getTypeJuge())
                        .map(TypeJugeConverter::dtoToEntity)
                        .orElse(null))
                .dateEmission(dto.getDateEmission())
                .dateDebutPunition(dto.getDateDebutPunition())
                .dateFinPunition(dto.getDateFinPunition())
                .daysDiffJuge(dto.getDaysDiffJuge())
                .affairePrincipale(dto.isAffairePrincipale())
                .build();
    }
    
    
    
}

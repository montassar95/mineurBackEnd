package com.cgpr.mineur.converter;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.ChangementLieuDto;
import com.cgpr.mineur.models.ChangementLieu;
import com.cgpr.mineur.models.Document;

public class ChangementLieuConverter extends DocumentConverter { 
	
  
	public static ChangementLieuDto entityToDto(ChangementLieu entity) {
        if (entity == null) {
            throw new EntityNotFoundException("Cannot convert null entity");
        }

        return ChangementLieuDto.builder()
        		//    .etablissementMutation(EtablissementConverter.entityToDto(entity.getEtablissementMutation())) // si il exist 
        		//        .etabChangeManiere(EtabChangeManiereConverter.entityToDto(entity.getEtabChangeManiere())) // si il exist
                
                
        		.documentId(DocumentIdConverter.entityToDto(entity.getDocumentId()))
        		.typeDocument(entity.getTypeDocument())
	            .typeDocumentActuelle(entity.getTypeDocumentActuelle())
	            .affaire(AffaireConverter.entityToDtoBasic(entity.getAffaire()))
	            .dateEmission(entity.getDateEmission())
	            .dateDepotCarte(entity.getDateDepotCarte())
	            .typeAffaire(Optional.ofNullable(entity.getTypeAffaire())
	                                .map(TypeAffaireConverter::entityToDto)
	                                .orElse(null))
	            .numArrestation(entity.getNumArrestation())
	            .etablissement(EtablissementConverter.entityToDto(entity.getEtablissement()))
//	            .user(UserConverter.entityToDto(entity.getUser()))
	            .dateInsertion(entity.getDateInsertion())
            	.etablissementMutation(Optional.ofNullable(entity.getEtablissementMutation())
                        .map(EtablissementConverter::entityToDto)
                        .orElse(null))
        		
        		
        		 .etabChangeManiere(Optional.ofNullable(entity.getEtabChangeManiere())
                        .map(EtabChangeManiereConverter::entityToDto)
                        .orElse(null))
        		 
        		 
                .jour(entity.getJour())
                .mois(entity.getMois())
                .annee(entity.getAnnee())
                .type(entity.getType())
                .build();
    }

    public static ChangementLieu dtoToEntity(ChangementLieuDto dto) {
        if (dto == null) {
            throw new EntityNotFoundException("Cannot convert null dto");
        }
        Document document = DocumentConverter.dtoToEntity(dto);

        // Créer une instance de CarteDepot en utilisant le builder
        return ChangementLieu.builder()
                // Inclure les attributs hérités de Document
        		  .documentId( document.getDocumentId())
                .typeDocument(document.getTypeDocument())
                .typeDocumentActuelle(document.getTypeDocumentActuelle())
                .affaire(document.getAffaire())
                .dateEmission(document.getDateEmission())
                .dateDepotCarte(document.getDateDepotCarte())
                .typeAffaire(document.getTypeAffaire())
                .numArrestation(document.getNumArrestation())
                .etablissement(document.getEtablissement())
//                .user(document.getUser())
                .dateInsertion(document.getDateInsertion())
       
        		.etablissementMutation(Optional.ofNullable(dto.getEtablissementMutation())
                        .map(EtablissementConverter::dtoToEntity)
                        .orElse(null))
        		
        		
        		 .etabChangeManiere(Optional.ofNullable(dto.getEtabChangeManiere())
                        .map(EtabChangeManiereConverter::dtoToEntity)
                        .orElse(null))
               
                .jour(dto.getJour())
                .mois(dto.getMois())
                .annee(dto.getAnnee())
                .type(dto.getType())
                .build();
    }
}



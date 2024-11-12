package com.cgpr.mineur.converter;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.ArreterlexecutionDto;
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Document;
 
 
 
public class ArreterlexecutionConverter extends DocumentConverter { 
	
	  public static ArreterlexecutionDto entityToDto(Arreterlexecution entity) {
	        if (entity == null) {
	            throw new EntityNotFoundException("Cannot convert null entity");
	        }

	        return ArreterlexecutionDto.builder()
	        		
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
		            .personelle(PersonelleConverter.entityToDto(entity.getPersonelle()))
		            .dateInsertion(entity.getDateInsertion())
		            
	                .typeFile(entity.getTypeFile())
	                .motifArreterlexecution(MotifArreterlexecutionConverter.entityToDto(entity.getMotifArreterlexecution()))
	                .build();
	    }

	    public static Arreterlexecution dtoToEntity(ArreterlexecutionDto dto) {
	        if (dto == null) {
	            throw new EntityNotFoundException("Cannot convert null dto");
	        }
	     // Convertir les attributs hérités
	        Document document = DocumentConverter.dtoToEntity(dto);

	        // Créer une instance de CarteDepot en utilisant le builder
	        return Arreterlexecution.builder()
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
	                .personelle(document.getPersonelle())
	                .dateInsertion(document.getDateInsertion())
	                 
	               
	     
	                .typeFile(dto.getTypeFile())
	                .motifArreterlexecution( MotifArreterlexecutionConverter.dtoToEntity(dto.getMotifArreterlexecution()))
	                .build();
	    }

}



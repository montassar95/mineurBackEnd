package com.cgpr.mineur.converter;

import java.util.Optional;

import com.cgpr.mineur.dto.DocumentDto;
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CartePropagation;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.ChangementLieu;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.RefuseRevue;
import com.cgpr.mineur.models.Transfert;
 
public  class DocumentConverter   {

	 
	public static DocumentDto entityToDto(Document entity) {
	    if (entity == null) {
	        return null; // or throw an IllegalArgumentException
	    }
	    if (entity instanceof CarteRecup) {
            return CarteRecupConverter.entityToDto((CarteRecup) entity) ;
        } 
	    
	    else if (entity instanceof CarteDepot ) {
            return  CarteDepotConverter.entityToDto((CarteDepot) entity) ;
        } 
	    else if (entity instanceof CartePropagation ) {
            return  CartePropagationConverter.entityToDto((CartePropagation) entity) ;
        } 
	    else if (entity instanceof ChangementLieu  ) {
            return  ChangementLieuConverter.entityToDto((ChangementLieu) entity) ;
        }
	    else if (entity instanceof Transfert  ) {
            return  TransfertConverter.entityToDto((Transfert) entity) ;
        }
	    
	    else if (entity instanceof Arreterlexecution  ) {
            return  ArreterlexecutionConverter.entityToDto((Arreterlexecution) entity) ;
        }
	    else if (entity instanceof CarteHeber  ) {
            return  CarteHeberConverter.entityToDto((CarteHeber) entity) ;
        }
	    else if (entity instanceof RefuseRevue  ) {
            return  RefuseRevueConverter.entityToDto((RefuseRevue) entity) ;
        }
	    else {
	    return DocumentDto.builder()
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
	            .build();
        }
	}
	 
//	 public static DocumentDto entityToDtoWithAffaire (Document entity) {
//	        return DocumentDto.builder()
//	                .documentId(DocumentIdConverter.entityToDto(entity.getDocumentId()))
//	                .typeDocument(entity.getTypeDocument())
//	                .typeDocumentActuelle(entity.getTypeDocumentActuelle())
//   	   .affaire(AffaireConverter.entityToDto(entity.getAffaire()))
//	                .dateEmission(entity.getDateEmission())
//	                .dateDepotCarte(entity.getDateDepotCarte())
//	                .typeAffaire(TypeAffaireConverter.entityToDto(entity.getTypeAffaire()))
//	                .numArrestation(entity.getNumArrestation())
//	                .etablissement(EtablissementConverter.entityToDto(entity.getEtablissement()))
//	                .personelle(PersonelleConverter.entityToDto(entity.getPersonelle()))
//	                .dateInsertion(entity.getDateInsertion())
//	                .build();
//	    }

	    public static Document dtoToEntity(DocumentDto dto) {
	        Document document = new Document();
	        document.setDocumentId(DocumentIdConverter.dtoToEntity(dto.getDocumentId()));
	        document.setTypeDocument(dto.getTypeDocument());
	        document.setTypeDocumentActuelle(dto.getTypeDocumentActuelle());
	        document.setAffaire(AffaireConverter.dtoToEntity(dto.getAffaire()));
	        document.setDateEmission(dto.getDateEmission());
	        document.setDateDepotCarte(dto.getDateDepotCarte());
 	        document.setTypeAffaire(dto.getTypeAffaire() != null ? TypeAffaireConverter.dtoToEntity(dto.getTypeAffaire()) : null);
	        
	        document.setTypeAffaire(Optional.ofNullable(dto.getTypeAffaire())
                    .map(TypeAffaireConverter::dtoToEntity)
                    .orElse(null));
	        
	        document.setNumArrestation(dto.getNumArrestation());
	        document.setEtablissement(EtablissementConverter.dtoToEntity(dto.getEtablissement()));
//	        document.setPersonelle(PersonelleConverter.dtoToEntity(dto.getPersonelle()));
//	        document.setUser(UserConverter.dtoToEntity(dto.getUser()));
	        document.setDateInsertion(dto.getDateInsertion());
	        return document;
	    }
//	    public static Document dtoToEntityWithOutAffaire(DocumentDto dto) {
//	        Document document = new Document();
//	        document.setDocumentId(DocumentIdConverter.dtoToEntity(dto.getDocumentId()));
//	        document.setTypeDocument(dto.getTypeDocument());
//	        document.setTypeDocumentActuelle(dto.getTypeDocumentActuelle());
////	        document.setAffaire(AffaireConverter.dtoToEntity(dto.getAffaire()));
//	        document.setDateEmission(dto.getDateEmission());
//	        document.setDateDepotCarte(dto.getDateDepotCarte());
//	        document.setTypeAffaire(TypeAffaireConverter.dtoToEntity(dto.getTypeAffaire()));
//	        document.setNumArrestation(dto.getNumArrestation());
//	        document.setEtablissement(EtablissementConverter.dtoToEntity(dto.getEtablissement()));
//	        document.setPersonelle(PersonelleConverter.dtoToEntity(dto.getPersonelle()));
//	        document.setDateInsertion(dto.getDateInsertion());
//	        return document;
//	    }
	
	 
 
}
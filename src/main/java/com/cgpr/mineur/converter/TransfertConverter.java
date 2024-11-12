package com.cgpr.mineur.converter;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.TransfertDto;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.Transfert;

 
 
public class TransfertConverter extends DocumentConverter {

	   public static TransfertDto entityToDto(Transfert entity) {
	        if (entity == null) {
	            throw new EntityNotFoundException("Cannot convert null entity");
	        }

	        return TransfertDto.builder()
	        		
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
	                .resultatTransfert(ResultatTransfertConverter.entityToDto(entity.getResultatTransfert()))
	                .build();
	    }

	    public static Transfert dtoToEntity(TransfertDto transfertDto) {
	        if (transfertDto == null) {
	            throw new EntityNotFoundException("Cannot convert null dto");
	        }
	        // Convertir les attributs hérités
	        Document document = DocumentConverter.dtoToEntity(transfertDto);
	        // Inclure les attributs hérités de Document
	        return Transfert.builder()  .documentId( document.getDocumentId())
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
          // Ajouter les attributs spécifiques à CarteDepot
	        
	                .typeFile(transfertDto.getTypeFile())
	                .resultatTransfert(ResultatTransfertConverter.dtoToEntity(transfertDto.getResultatTransfert()))
	                .build();
	    }
}

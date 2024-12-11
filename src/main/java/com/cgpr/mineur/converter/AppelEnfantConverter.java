package com.cgpr.mineur.converter;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.AppelEnfantDto;
import com.cgpr.mineur.models.AppelEnfant;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.Transfert;
 
 
 
public class AppelEnfantConverter extends DocumentConverter {

    public static AppelEnfantDto entityToDto(AppelEnfant entity) {
        if (entity == null) {
            throw new EntityNotFoundException("Cannot convert null entity");
        }

        return AppelEnfantDto.builder()
                // Populate fields from entity to DTO
                .build();
    }

    public static AppelEnfant dtoToEntity(AppelEnfantDto dto) {
        if (dto == null) {
            throw new EntityNotFoundException("Cannot convert null dto");
        }
        // Convertir les attributs hérités
        Document document = DocumentConverter.dtoToEntity(dto);
        // Inclure les attributs hérités de Document
        return AppelEnfant .builder()  .documentId( document.getDocumentId())
      .typeDocument(document.getTypeDocument())
      .typeDocumentActuelle(document.getTypeDocumentActuelle())
      .affaire(document.getAffaire())
      .dateEmission(document.getDateEmission())
      .dateDepotCarte(document.getDateDepotCarte())
      .typeAffaire(document.getTypeAffaire())
      .numArrestation(document.getNumArrestation())
      .etablissement(document.getEtablissement())
//      .user(document.getUser())
      .dateInsertion(document.getDateInsertion()) .build();
      // Ajouter les attributs spécifiques à  AppelEnfant
        
    }
 

	 

}

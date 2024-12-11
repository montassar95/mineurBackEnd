package com.cgpr.mineur.converter;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.OppositionDto;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.Opposition;
 
 
 
public class OppositionConverter extends DocumentConverter {

    public static OppositionDto entityToDto(Opposition entity) {
        if (entity == null) {
            throw new EntityNotFoundException("Cannot convert null entity");
        }

        return OppositionDto.builder()
                // Populate fields from entity to DTO
                .build();
    }

    public static Opposition dtoToEntity(OppositionDto dto) {
        if (dto == null) {
            throw new EntityNotFoundException("Cannot convert null dto");
        }
        // Convertir les attributs hérités
        Document document = DocumentConverter.dtoToEntity(dto);
        // Inclure les attributs hérités de Document
        return Opposition .builder()  .documentId( document.getDocumentId())
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
      // Ajouter les attributs spécifiques à  Opposition
        
    }
 

	 

}

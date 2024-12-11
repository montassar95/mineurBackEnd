package com.cgpr.mineur.converter;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.ObservationDto;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.Observation;
 
 
 
public class ObservationConverter extends DocumentConverter {

    public static ObservationDto entityToDto(Observation entity) {
        if (entity == null) {
            throw new EntityNotFoundException("Cannot convert null entity");
        }

        return ObservationDto.builder()
                // Populate fields from entity to DTO
                .build();
    }

    public static Observation dtoToEntity(ObservationDto dto) {
        if (dto == null) {
            throw new EntityNotFoundException("Cannot convert null dto");
        }
        // Convertir les attributs hérités
        Document document = DocumentConverter.dtoToEntity(dto);
        // Inclure les attributs hérités de Document
        return Observation .builder()  .documentId( document.getDocumentId())
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
      // Ajouter les attributs spécifiques à  Observation
        
    }
 

	 

}

package com.cgpr.mineur.converter;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.AppelParquetDto;
import com.cgpr.mineur.models.AppelEnfant;
import com.cgpr.mineur.models.AppelParquet;
import com.cgpr.mineur.models.Document;
 
 
public class AppelParquetConverter extends DocumentConverter {

	 
	public static AppelParquetDto entityToDto(AppelParquet entity) {
        if (entity == null) {
            throw new EntityNotFoundException("Cannot convert null entity");
        }

        return AppelParquetDto.builder()
                // Populate fields from entity to DTO
                .build();
    }

    public static AppelParquet dtoToEntity(AppelParquetDto dto) {
        if (dto == null) {
            throw new EntityNotFoundException("Cannot convert null dto");
        }

        // Convertir les attributs hérités
        Document document = DocumentConverter.dtoToEntity(dto);
        // Inclure les attributs hérités de Document
        return AppelParquet  .builder()  .documentId( document.getDocumentId())
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

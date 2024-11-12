package com.cgpr.mineur.converter;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.RevueDto;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.Revue;
 
 
public class RevueConverter extends DocumentConverter {
	public static RevueDto entityToDto(Revue entity) {
        if (entity == null) {
            throw new EntityNotFoundException("Cannot convert null entity");
        }

        return RevueDto.builder()
                // Populate fields from entity to DTO
        		 .textJugement(entity.getTextJugement())
                .build();
    }

    public static Revue dtoToEntity(RevueDto dto) {
        if (dto == null) {
            throw new EntityNotFoundException("Cannot convert null dto");
        }
            // Convertir les attributs hérités
	        Document document = DocumentConverter.dtoToEntity(dto);

	        // Créer une instance de CarteDepot en utilisant le builder
	        return Revue.builder()
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
	                // Ajouter les attributs spécifiques à CarteDepot
	                .textJugement(dto.getTextJugement())
	                .build();
    }

}

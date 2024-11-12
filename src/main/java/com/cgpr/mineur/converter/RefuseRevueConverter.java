package com.cgpr.mineur.converter;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.RefuseRevueDto;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.RefuseRevue;
import com.cgpr.mineur.models.Revue;
 
 
public class RefuseRevueConverter extends DocumentConverter {

	 
	 public static RefuseRevueDto entityToDto(RefuseRevue entity) {
	        if (entity == null) {
	            throw new EntityNotFoundException("Cannot convert null entity");
	        }

	        return RefuseRevueDto.builder()
	                // Populate fields from entity to DTO
	        		  .textJugement(entity.getTextJugement())
	                .build();
	    }

	    public static RefuseRevue dtoToEntity(RefuseRevueDto dto) {
	        if (dto == null) {
	            throw new EntityNotFoundException("Cannot convert null dto");
	        }
	         // Convertir les attributs hérités
		        Document document = DocumentConverter.dtoToEntity(dto);

		        // Créer une instance de CarteDepot en utilisant le builder
		        return RefuseRevue.builder()
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

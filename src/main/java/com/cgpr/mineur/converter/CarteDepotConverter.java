package com.cgpr.mineur.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.CarteDepotDto;
import com.cgpr.mineur.dto.TitreAccusationDto;
import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.AccusationCarteDepotId;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.Document;
 
 
public class CarteDepotConverter extends DocumentConverter {

	 public static CarteDepotDto entityToDto(CarteDepot entity) {
	        if (entity == null) {
	            throw new EntityNotFoundException("Cannot convert null entity");
	        }

	        return CarteDepotDto.builder()
	                // Populate fields from entity to DTO
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
// 		            .user(UserConverter.entityToDto(entity.getUser()))
		            .dateInsertion(entity.getDateInsertion())
		            
		            
	        		  .textJugement(entity.getTextJugement())
	        		  
	        		  .titreAccusations(entity.getAccusationCarteDepots().stream()
	                          .map(accusation -> {
	                              return Optional.ofNullable(accusation.getTitreAccusation())
	                                             .map(TitreAccusationConverter::entityToDto)
	                                             .orElse(null);
	                          })
	                          .collect(Collectors.toList()))
	        		  .etabChangeManiere(Optional.ofNullable(entity.getEtabChangeManiere())
	                          .map(EtabChangeManiereConverter::entityToDto)
	                          .orElse(null))
	        		  
	        		 
	          		
	          		
	          		
	                .build();
	    }

//	    public static CarteDepot dtoToEntity(CarteDepotDto dto) {
//	        if (dto == null) {
//	            throw new EntityNotFoundException("Cannot convert null dto");
//	        }
//
//	        return CarteDepot.builder()
//	                // Populate fields from DTO to entity
//	        		 .textJugement(dto.getTextJugement())
//	                .build();
//	    
//	    }
	 
	 public static CarteDepot dtoToEntity(CarteDepotDto dto) {
		 
	        if (dto == null) {
	            throw new EntityNotFoundException("Cannot convert null dto");
	        }

	        // Convertir les attributs hérités
	        Document document = DocumentConverter.dtoToEntity(dto);

	        // Créer une instance de CarteDepot en utilisant le builder
	        CarteDepot carteDepot = CarteDepot.builder()
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
//	                .user(document.getUser())
	                .dateInsertion(document.getDateInsertion())
	               
	                .textJugement(dto.getTextJugement()) 
	                
	        
	        .etabChangeManiere(Optional.ofNullable(dto.getEtabChangeManiere())
                    .map(EtabChangeManiereConverter::dtoToEntity)
                    .orElse(null))
	        
	        .build();
	        
//	                // Liste pour stocker les AccusationCarteDepot
//	                List<AccusationCarteDepot> accusations = new ArrayList<>();
//	                AccusationCarteDepotId acdId = new AccusationCarteDepotId();
//                    acdId.setIdEnfant(dto.getDocumentId().getIdEnfant());
//                    acdId.setNumOrdinalAffaire(dto.getDocumentId().getNumOrdinalAffaire());
//                    acdId.setNumOrdinalArrestation(dto.getDocumentId().getNumOrdinalArrestation());
//                    acdId.setNumOrdinalDoc(dto.getDocumentId().getNumOrdinalDoc());
//                    acdId.setNumOrdinalDocByAffaire(dto.getDocumentId().getNumOrdinalDocByAffaire());
//	                // Remplir la liste d'AccusationCarteDepot
//	                for (TitreAccusationDto titreAccusationDto : dto.getTitreAccusations()) {
//	                  
//	                    acdId.setIdTitreAccusation(titreAccusationDto.getId());
//
//	                    // Création d'une nouvelle instance d'AccusationCarteDepot
//	                    AccusationCarteDepot acd = new AccusationCarteDepot();
//	                    acd.setAccusationCarteDepotId(acdId);
//	                    acd.setCarteDepot(carteDepot);
//	                    acd.setTitreAccusation(TitreAccusationConverter.dtoToEntity(titreAccusationDto));
//
//	                    // Ajout à la liste d'accusations
//	                    accusations.add(acd);
//	                }
//
//	                // Assigner la liste d'accusations à la carteDepot
//	                carteDepot.setAccusationCarteDepots(accusations);
	                
	               return carteDepot;
	    }
}

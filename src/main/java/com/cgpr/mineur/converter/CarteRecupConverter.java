package com.cgpr.mineur.converter;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.cgpr.mineur.dto.CarteRecupDto;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Document;


 
public class CarteRecupConverter extends DocumentConverter {
	public static CarteRecupDto entityToDto(CarteRecup entity) {
        if (entity == null) {
            throw new EntityNotFoundException("Cannot convert null entity");
        }

        return CarteRecupDto.builder()
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
	            
	            
                .textJugement(entity.getTextJugement())
                .typeJuge(TypeJugeConverter.entityToDto(entity.getTypeJuge()) )
                .daysDiffJuge(entity.getDaysDiffJuge())
                .jour(entity.getJour())
                .mois(entity.getMois())
                .annee(entity.getAnnee())
                .daysDiffArretProvisoire(entity.getDaysDiffArretProvisoire())
                .jourArretProvisoire(entity.getJourArretProvisoire())
                .moisArretProvisoire(entity.getMoisArretProvisoire())
                .anneeArretProvisoire(entity.getAnneeArretProvisoire())
                .dateDebutPunition(entity.getDateDebutPunition())
                .dateFinPunition(entity.getDateFinPunition())
                .etatJuridiqueAvant(entity.getEtatJuridiqueAvant()) // Nouvel attribut
                
                .accusationCarteRecups(entity.getAccusationCarteRecups().stream()
                        .map(AccusationCarteRecupConverter::entityToDto)
                        .collect(Collectors.toList()))
               
                .arretProvisoires(entity.getArretProvisoires() != null 
                    ? entity.getArretProvisoires().stream()
                        .map(ArretProvisoireConverter::entityToDto)
                        .collect(Collectors.toList())
                    : Collections.emptyList())  // ou null selon ce que vous souhaitez
                
                
                .build();
           
        
                 
    }

    public static CarteRecup dtoToEntity(CarteRecupDto dto) {
        if (dto == null) {
            throw new EntityNotFoundException("Cannot convert null dto");
        }

        // Convertir les attributs hérités
        Document document = DocumentConverter.dtoToEntity(dto);

        // Créer une instance de CarteDepot en utilisant le builder
        return CarteRecup.builder()
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


                
                
                .typeJuge(TypeJugeConverter.dtoToEntity(dto.getTypeJuge()))
                .daysDiffJuge(dto.getDaysDiffJuge())
                .jour(dto.getJour())
                .mois(dto.getMois())
                .annee(dto.getAnnee())
                .daysDiffArretProvisoire(dto.getDaysDiffArretProvisoire())
                .jourArretProvisoire(dto.getJourArretProvisoire())
                .moisArretProvisoire(dto.getMoisArretProvisoire())
                .anneeArretProvisoire(dto.getAnneeArretProvisoire())
                .dateDebutPunition(dto.getDateDebutPunition())
                .dateFinPunition(dto.getDateFinPunition())
                .etatJuridiqueAvant(dto.getEtatJuridiqueAvant()) // Nouvel attribut
                
//                .accusationCarteRecups((dto.getAccusationCarteRecups().stream()
//                        .map(AccusationCarteRecupConverter::dtoToEntity)
//                        .collect(Collectors.toList()))
                .arretProvisoires(dto.getArretProvisoires().stream()
                        .map(ArretProvisoireConverter::dtoToEntity)
                        .collect(Collectors.toList()))
                .build();
        }
 
}

package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.EnfantDto;
import com.cgpr.mineur.models.Enfant;

public class EnfantConverter {

    public static EnfantDto entityToDto(Enfant entity) {
        if (entity == null) {
            return null; // Handle null entity case
        }

        return EnfantDto.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .nomPere(entity.getNomPere())
                .nomGrandPere(entity.getNomGrandPere())
                .nomMere(entity.getNomMere())
                .prenomMere(entity.getPrenomMere())
                .dateNaissance(entity.getDateNaissance())
                .lieuNaissance(entity.getLieuNaissance())
                .sexe(entity.getSexe())
               // .img(entity.getImg())
                .nationalite(entity.getNationalite() != null ? 
                    NationaliteConverter.entityToDto(entity.getNationalite()) : null)
                .niveauEducatif(entity.getNiveauEducatif() != null ? 
                    NiveauEducatifConverter.entityToDto(entity.getNiveauEducatif()) : null)
                .situationFamiliale(entity.getSituationFamiliale() != null ? 
                    SituationFamilialeConverter.entityToDto(entity.getSituationFamiliale()) : null)
                .nombreFreres(entity.getNombreFreres())
                .gouvernorat(entity.getGouvernorat() != null ? 
                    GouvernoratConverter.entityToDto(entity.getGouvernorat()) : null)
                .delegation(entity.getDelegation() != null ? 
                    DelegationConverter.entityToDto(entity.getDelegation()) : null)
                .adresse(entity.getAdresse())
                .surnom(entity.getSurnom())
                .alias(entity.getAlias())
                .classePenale(entity.getClassePenale() != null ? 
                    ClassePenaleConverter.entityToDto(entity.getClassePenale()) : null)
                .situationSocial(entity.getSituationSocial() != null ? 
                    SituationSocialConverter.entityToDto(entity.getSituationSocial()) : null)
                .metier(entity.getMetier() != null ? 
                    MetierConverter.entityToDto(entity.getMetier()) : null)
                .etat(entity.getEtat())
                .nbrEnfant(entity.getNbrEnfant())
                .build();
    }

    public static Enfant dtoToEntity(EnfantDto dto) {
        if (dto == null) {
            return null; // Handle null DTO case
        }

        return Enfant.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .nomPere(dto.getNomPere())
                .nomGrandPere(dto.getNomGrandPere())
                .nomMere(dto.getNomMere())
                .prenomMere(dto.getPrenomMere())
                .dateNaissance(dto.getDateNaissance())
                .lieuNaissance(dto.getLieuNaissance())
                .sexe(dto.getSexe())
              //  .img(dto.getImg())
                .nationalite(dto.getNationalite() != null ? 
                    NationaliteConverter.dtoToEntity(dto.getNationalite()) : null)
                .niveauEducatif(dto.getNiveauEducatif() != null ? 
                    NiveauEducatifConverter.dtoToEntity(dto.getNiveauEducatif()) : null)
                .situationFamiliale(dto.getSituationFamiliale() != null ? 
                    SituationFamilialeConverter.dtoToEntity(dto.getSituationFamiliale()) : null)
                .nombreFreres(dto.getNombreFreres())
                .gouvernorat(dto.getGouvernorat() != null ? 
                    GouvernoratConverter.dtoToEntity(dto.getGouvernorat()) : null)
                .delegation(dto.getDelegation() != null ? 
                    DelegationConverter.dtoToEntity(dto.getDelegation()) : null)
                .adresse(dto.getAdresse())
                .surnom(dto.getSurnom())
                .alias(dto.getAlias())
                .classePenale(dto.getClassePenale() != null ? 
                    ClassePenaleConverter.dtoToEntity(dto.getClassePenale()) : null)
                .situationSocial(dto.getSituationSocial() != null ?  
                    SituationSocialConverter.dtoToEntity(dto.getSituationSocial()) : null)
                .metier(dto.getMetier() != null ? 
                    MetierConverter.dtoToEntity(dto.getMetier()) : null)
                .etat(dto.getEtat())
                .nbrEnfant(dto.getNbrEnfant())
                .build();
    }
}

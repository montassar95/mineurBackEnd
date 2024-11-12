package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.PersonelleDto;
import com.cgpr.mineur.models.Personelle;
 
public class PersonelleConverter {
	public static PersonelleDto entityToDto(Personelle entity) {
	    // Assurez-vous que l'entité n'est pas nulle
	    if (entity == null) {
	        return null;
	    }

	    return PersonelleDto.builder()
	            .id(entity.getId())
	            .matricule(entity.getMatricule())
	            .cnrps(entity.getCnrps())
	            .nom(entity.getNom())
	            .prenom(entity.getPrenom())
	            .nom_pere(entity.getNom_pere())
	            // Convertir uniquement si grade n'est pas null
	            .gradeDto(entity.getGrade() != null ? GradeConverter.entityToDto(entity.getGrade()) : null)
	            // Convertir uniquement si etablissement n'est pas null
	            .etablissement(entity.getEtablissement() != null ? EtablissementConverter.entityToDto(entity.getEtablissement()) : null)
	            // Convertir uniquement si fonction n'est pas null
	            .fonction(entity.getFonction() != null ? FonctionConverter.entityToDto(entity.getFonction()) : null)
	            // Convertir uniquement si situation n'est pas null
	            .situation(entity.getSituation() != null ? SituationConverter.entityToDto(entity.getSituation()) : null)
	            .img(entity.getImg())
	            .build();
	}

	public static Personelle dtoToEntity(PersonelleDto dto) {
	    if (dto == null) {
	        return null;
	    }

	    Personelle.PersonelleBuilder builder = Personelle.builder()
	            .id(dto.getId())
	            .matricule(dto.getMatricule())
	            .cnrps(dto.getCnrps())
	            .nom(dto.getNom())
	            .prenom(dto.getPrenom())
	            .nom_pere(dto.getNom_pere())
	            .img(dto.getImg());

	    // Convertir uniquement si gradeDto n'est pas null
	    if (dto.getGradeDto() != null) {
	        builder.grade(GradeConverter.dtoToEntity(dto.getGradeDto()));
	    }

	    // Convertir uniquement si etablissement n'est pas null
	    if (dto.getEtablissement() != null) {
	        builder.etablissement(EtablissementConverter.dtoToEntity(dto.getEtablissement()));
	    }

	    // Convertir uniquement si fonction n'est pas null
	    if (dto.getFonction() != null) {
	        builder.fonction(FonctionConverter.dtoToEntity(dto.getFonction()));
	    }

	    // Convertir uniquement si situation n'est pas null
	    if (dto.getSituation() != null) {
	        builder.situation(SituationConverter.dtoToEntity(dto.getSituation()));
	    }

	    return builder.build();
	}
}

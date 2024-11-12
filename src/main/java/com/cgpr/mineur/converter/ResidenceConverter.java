package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.ResidenceDto;
import com.cgpr.mineur.models.Residence;
 
public class ResidenceConverter {
	
	 
	public static ResidenceDto entityToDto(Residence entity) {
	    // Ensure that entity is not null to prevent NullPointerException
	    if (entity == null) {
	        return null;
	    }

	    return ResidenceDto.builder()
	            .residenceId(ResidenceIdConverter.entityToDto(entity.getResidenceId()))
	            .numArrestation(entity.getNumArrestation())
	            .dateEntree(entity.getDateEntree())
	            .dateSortie(entity.getDateSortie())
	            .etablissement(EtablissementConverter.entityToDto(entity.getEtablissement()))
	            .etablissementEntree(entity.getEtablissementEntree() != null ? 
	                EtablissementConverter.entityToDto(entity.getEtablissementEntree()) : null)
	            .etablissementSortie(entity.getEtablissementSortie() != null ? 
	                EtablissementConverter.entityToDto(entity.getEtablissementSortie()) : null)
	            .arrestation(ArrestationConverter.entityToDto(entity.getArrestation()))
	            .statut(entity.getStatut())
	            .causeMutation(entity.getCauseMutation() != null ? 
	                CauseMutationConverter.entityToDto(entity.getCauseMutation()) : null)
	            .causeMutationSortie(entity.getCauseMutationSortie() != null ? 
	                CauseMutationConverter.entityToDto(entity.getCauseMutationSortie()) : null)
	            .remarqueMutation(entity.getRemarqueMutation())
	            .nombreEchappes(entity.getNombreEchappes())
	            .dateFin(entity.getDateFin())
	            .etabChangeManiere(entity.getEtabChangeManiere() != null ? 
	                EtabChangeManiereConverter.entityToDto(entity.getEtabChangeManiere()) : null)
	            .build();
	}


	public static Residence dtoToEntity(ResidenceDto dto) {
	    // Check if dto is null to prevent NullPointerException
	    if (dto == null) {
	        return null;
	    }

	    return Residence.builder()
	            .residenceId(ResidenceIdConverter.dtoToEntity(dto.getResidenceId()))
	            .numArrestation(dto.getNumArrestation())
	            .dateEntree(dto.getDateEntree())
	            .dateSortie(dto.getDateSortie())
	            .etablissement(EtablissementConverter.dtoToEntity(dto.getEtablissement()))
	            .etablissementEntree(dto.getEtablissementEntree() != null ? 
	                EtablissementConverter.dtoToEntity(dto.getEtablissementEntree()) : null)
	            .etablissementSortie(dto.getEtablissementSortie() != null ? 
	                EtablissementConverter.dtoToEntity(dto.getEtablissementSortie()) : null)
	            .arrestation(ArrestationConverter.dtoToEntity(dto.getArrestation()))
	            .statut(dto.getStatut())
	            .causeMutation(dto.getCauseMutation() != null ? 
	                CauseMutationConverter.dtoToEntity(dto.getCauseMutation()) : null)
	            .causeMutationSortie(dto.getCauseMutationSortie() != null ? 
	                CauseMutationConverter.dtoToEntity(dto.getCauseMutationSortie()) : null)
	            .remarqueMutation(dto.getRemarqueMutation())
	            .nombreEchappes(dto.getNombreEchappes())
	            .dateFin(dto.getDateFin())
	            .etabChangeManiere(dto.getEtabChangeManiere() != null ? 
	                EtabChangeManiereConverter.dtoToEntity(dto.getEtabChangeManiere()) : null)
	            .build();
	}
	
}

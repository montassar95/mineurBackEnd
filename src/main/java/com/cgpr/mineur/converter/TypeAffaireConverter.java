package com.cgpr.mineur.converter;
import com.cgpr.mineur.dto.TypeAffaireDto;
import com.cgpr.mineur.models.TypeAffaire;

 
public class TypeAffaireConverter {
 
	  public static TypeAffaireDto entityToDto(TypeAffaire typeAffaire) {
	        return TypeAffaireDto.builder()
	                .id(typeAffaire.getId())
	                .libelle_typeAffaire(typeAffaire.getLibelle_typeAffaire())
	                .statutException(typeAffaire.getStatutException())
	                .statutNiveau(typeAffaire.getStatutNiveau())
	                .build();
	    }

	    public static TypeAffaire dtoToEntity(TypeAffaireDto typeAffaireDto) {
	        return TypeAffaire.builder()
	                .id(typeAffaireDto.getId())
	                .libelle_typeAffaire(typeAffaireDto.getLibelle_typeAffaire())
	                .statutException(typeAffaireDto.getStatutException())
	                .statutNiveau(typeAffaireDto.getStatutNiveau())
	                .build();
	    }
}

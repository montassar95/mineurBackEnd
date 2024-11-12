package com.cgpr.mineur.converter;
import com.cgpr.mineur.dto.TypeJugeDto;
import com.cgpr.mineur.models.TypeJuge;

 
public class TypeJugeConverter {
	
	  public static TypeJugeDto entityToDto(TypeJuge typeJuge) {
	        return TypeJugeDto.builder()
	                .id(typeJuge.getId())
	                .libelle_typeJuge(typeJuge.getLibelle_typeJuge())
	                .situation(typeJuge.getSituation())
	                .build();
	    }

	    public static TypeJuge dtoToEntity(TypeJugeDto typeJugeDto) {
	        return TypeJuge.builder()
	                .id(typeJugeDto.getId())
	                .libelle_typeJuge(typeJugeDto.getLibelle_typeJuge())
	                .situation(typeJugeDto.getSituation())
	                .build();
	    }

}

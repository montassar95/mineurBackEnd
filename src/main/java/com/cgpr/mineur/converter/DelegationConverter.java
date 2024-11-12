package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.DelegationDto;
import com.cgpr.mineur.models.Delegation;
 
public class DelegationConverter {
 
	  public static DelegationDto entityToDto(Delegation entity) {
	        return DelegationDto.builder()
	                .id(entity.getId())
	                .libelle_delegation(entity.getLibelle_delegation())
	                .gouvernorat(GouvernoratConverter.entityToDto(entity.getGouvernorat()))
	                .build();
	    }

	    public static Delegation dtoToEntity(DelegationDto dto) {
	        Delegation delegation = new Delegation();
	        delegation.setId(dto.getId());
	        delegation.setLibelle_delegation(dto.getLibelle_delegation());
	        delegation.setGouvernorat(GouvernoratConverter.dtoToEntity(dto.getGouvernorat()));
	        return delegation;
	    }

}

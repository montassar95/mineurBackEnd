package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.SituationSocialDto;
import com.cgpr.mineur.models.SituationSocial;
 
public class SituationSocialConverter {
 
    public static SituationSocialDto entityToDto(SituationSocial entity) {
        SituationSocialDto dto = new SituationSocialDto();
        dto.setId(entity.getId());
        dto.setLibelle_situation_social(entity.getLibelle_situation_social());
        return dto;
    }

    public static SituationSocial dtoToEntity(SituationSocialDto dto) {
        SituationSocial entity = new SituationSocial();
        entity.setId(dto.getId());
        entity.setLibelle_situation_social(dto.getLibelle_situation_social());
        return entity;
    }
	 

}

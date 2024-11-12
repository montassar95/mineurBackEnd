package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.CommentEchapperDto;
import com.cgpr.mineur.models.CommentEchapper;

 
public class CommentEchapperConverter {
	
    public static CommentEchapperDto entityToDto(CommentEchapper entity) {
        return CommentEchapperDto.builder()
                .id(entity.getId())
                .libelleComment(entity.getLibelleComment())
                .build();
    }

    public static CommentEchapper dtoToEntity(CommentEchapperDto dto) {
        return CommentEchapper.builder()
                .id(dto.getId())
                .libelleComment(dto.getLibelleComment())
                .build();
    }
}

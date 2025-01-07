//package com.cgpr.mineur.converter;
//
//import com.cgpr.mineur.dto.GradeDto;
//import com.cgpr.mineur.models.Grade;
// 
//public class GradeConverter {
//	 
//	   public static GradeDto entityToDto(Grade entity) {
//	        return GradeDto.builder()
//	                .id(entity.getId())
//	                .libelle_grade(entity.getLibelle_grade())
//	                .build();
//	    }
//
//	    public static Grade dtoToEntity(GradeDto dto) {
//	        return Grade.builder()
//	                .id(dto.getId())
//	                .libelle_grade(dto.getLibelle_grade())
//	                .build();
//	    }
//}

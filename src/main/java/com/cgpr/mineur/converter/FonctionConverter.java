//package com.cgpr.mineur.converter;
//
//import com.cgpr.mineur.dto.FonctionDto;
//import com.cgpr.mineur.models.Fonction;
// 
//public class FonctionConverter {
//	 
//    public static FonctionDto entityToDto(Fonction entity) {
//        return FonctionDto.builder()
//                .id(entity.getId())
//                .libelle_fonction(entity.getLibelle_fonction())
//                .build();
//    }
//
//    public static Fonction dtoToEntity(FonctionDto dto) {
//        return Fonction.builder()
//                .id(dto.getId())
//                .libelle_fonction(dto.getLibelle_fonction())
//                .build();
//    }
//}

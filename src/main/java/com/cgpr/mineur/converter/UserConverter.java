package com.cgpr.mineur.converter;

import com.cgpr.mineur.dto.UserDto;
import com.cgpr.mineur.modelsSecurity.Role;
import com.cgpr.mineur.modelsSecurity.User;
import com.cgpr.mineur.models.Etablissement;
 
import com.cgpr.mineur.dto.EtablissementDto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserConverter {

    // Convertir une entité User en UserDto
    public static UserDto entityToDto(User entity) {
        // Assurez-vous que l'entité n'est pas nulle
        if (entity == null) {
            return null;
        }

        // Conversion de l'entité en DTO
        return UserDto.builder()
                .id(entity.getId())  // Copie de l'ID
                .username(entity.getUsername())  // Copie du nom d'utilisateur
                .nom(entity.getNom())  // Copie du nom
                .prenom(entity.getPrenom())  // Copie du prénom
                .numAdministratif(entity.getNumAdministratif())  // Copie du numéro administratif
                .etablissement(entity.getEtablissement() != null ? EtablissementConverter.entityToDto(entity.getEtablissement()) : null) // Conversion de l'établissement en DTO
                .roles(getRoleNames(entity))  // Conversion des rôles en Set<String>
                .build();
    }

    // Convertir un UserDto en entité User en utilisant le constructeur et les setters
    public static User dtoToEntity(UserDto dto) {
        // Si le DTO est nul, on retourne null
        if (dto == null) {
            return null;
        }

        // Construction de l'entité User à partir du DTO via le constructeur et les setters
        User user = new User();

        // On utilise les setters pour affecter les valeurs
        user.setId(dto.getId());  // Copie de l'ID
        user.setUsername(dto.getUsername());  // Copie du nom d'utilisateur
        user.setNom(dto.getNom());  // Copie du nom
        user.setPrenom(dto.getPrenom());  // Copie du prénom
        user.setNumAdministratif(dto.getNumAdministratif());  // Copie du numéro administratif

        // Conversion de l'établissement si présent
        if (dto.getEtablissement() != null) {
            user.setEtablissement(EtablissementConverter.dtoToEntity(dto.getEtablissement()));
        }

        return user;  // Retour de l'objet User construit
    }

    // Extraire les noms des rôles d'une entité User (si vous avez une relation avec une entité Role)
    private static Set<String> getRoleNames(User user) {
        Set<String> roleNames = new HashSet<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                // Si Role contient une énumération ERole, utilisez role.getName().name()
                roleNames.add(role.getName().name());  // Cette ligne obtient le nom sous forme de chaîne
            }
        }
        return roleNames;
    }
}

package com.cgpr.mineur.modelsSecurity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cgpr.mineur.models.Etablissement;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "users", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
    }
)
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Size(max = 120)
    @JsonIgnore // Empêche le mot de passe d'apparaître dans les JSON
    @Column(nullable = false)
    private String password;

    @Size(max = 50)
    private String nom;

    @Size(max = 50)
    private String prenom;

    @Column(name = "num_administratif", length = 50)
    private String numAdministratif;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "last_login ")
    private LocalDateTime lastLogin ;

    
    private LocalDateTime lastPasswordModifiedDate;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etaFK")
    private Etablissement etablissement;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

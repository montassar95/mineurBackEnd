package com.cgpr.mineur.modelsSecurity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cgpr.mineur.models.Etablissement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
 
 @Getter
 @Setter
 @ToString
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
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    private String nom;
    private String prenom;
    
    @Column(name = "num_administratif")
    private String numAdministratif;
    
    
 
    @ManyToOne
    @JoinColumn(name = "etaFK")
    private Etablissement etablissement;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

 
    public User() {
	}

	 
 
 
}

package com.cgpr.mineur.modelsSecurity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;

import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Personelle;

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
    
    
//    (fetch = FetchType.LAZY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personelle_userFK")
    private Personelle personelle;

    public User() {
	}

	 
 

    // Les getters et setters sont générés par Lombok grâce à @Data

    // Si vous avez des exigences supplémentaires pour l'égalité ou le hachage, 
    // vous pouvez implémenter equals() et hashCode() ici.
}

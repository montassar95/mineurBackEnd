package com.cgpr.mineur.payload.request;

import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.*;

import com.cgpr.mineur.dto.EtablissementDto;
import lombok.ToString;

@ToString
public class SignupRequestByAdmin {

    // Le username doit contenir entre 3 et 20 caractères,
    // uniquement lettres, chiffres, underscores et points.
    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(
        regexp = "^[a-zA-Z0-9._]{3,20}$",
        message = "يجب أن يحتوي اسم المستخدم على بين 3 و 20 حرفًا أبجديًا رقميًا، نقاطًا أو شرطات سفلية."
    )
    private String username;

    private String nom;
    private String prenom;
    private String telephone;
    private String numAdministratif;
   
   
    private Set<String> role;

    @NotNull
    private String etablissementId; // ✅ remplace l'objet complet

   // private EtablissementDto etablissement;

    // === Constructeurs ===
    public SignupRequestByAdmin() {}

    public SignupRequestByAdmin(String username, Set<String> role,              String nom, String prenom, String telephone ,String numAdministratif ) {
        this.username = username;
        this.role = role;
 
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.numAdministratif = numAdministratif;
       // this.etablissement = etablissement;
    }

    // === Getters & Setters ===
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

 

    public Set<String> getRole() { return role; }
    public void setRole(Set<String> role) { this.role = role; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getNumAdministratif() { return numAdministratif; }
    public void setNumAdministratif(String numAdministratif) { this.numAdministratif = numAdministratif; }

    public String getEtablissementId() { return etablissementId; }
     

	 
}

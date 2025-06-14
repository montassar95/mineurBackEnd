package com.cgpr.mineur.payload.request;

import java.util.Set;
import javax.validation.constraints.*;

import com.cgpr.mineur.dto.EtablissementDto;
import lombok.ToString;

@ToString
public class SignupRequest {

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
    private String numAdministratif;
    private String telephone;
    private Set<String> role;

    // Le mot de passe doit avoir au moins 8 caractères,
    // incluant une majuscule, une minuscule, un chiffre et un caractère spécial.
//    @NotBlank
//    @Size(min = 8, max = 40)
//    @Pattern(
//        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,40}$",
//        message = "يجب أن تحتوي كلمة المرور على بين 8 و 40 حرفًا، بما في ذلك حرف كبير، حرف صغير، رقم، وحرف خاص."
//    )
//    private String password;

  //  private EtablissementDto etablissement;
    @NotNull
    private String etablissementId; // ✅ remplace l'objet complet
    // === Constructeurs ===
    public SignupRequest() {}

    public SignupRequest(String username, Set<String> role,  
                         String nom, String prenom, String numAdministratif ) {
        this.username = username;
        this.role = role;
        
        this.nom = nom;
        this.prenom = prenom;
        this.numAdministratif = numAdministratif;
        
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

    public String getNumAdministratif() { return numAdministratif; }
    public void setNumAdministratif(String numAdministratif) { this.numAdministratif = numAdministratif; }

    public String getEtablissementId() { return etablissementId; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}

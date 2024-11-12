package com.cgpr.mineur.payload.request;

import java.util.Set;

import javax.validation.constraints.*;

import com.cgpr.mineur.dto.EtablissementDto;
import com.cgpr.mineur.models.Personelle;

import lombok.ToString;


 @ToString
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
 
    private Personelle personelle;
    private String nom ;
	private String prenom;
	private String numAdministratif ;
    
    private Set<String> role;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
    private EtablissementDto etablissement;
  
    
    
    
    
    
    
    
    public SignupRequest(@NotBlank @Size(min = 3, max = 20) String username, Personelle personelle, Set<String> role,
			@NotBlank @Size(min = 6, max = 40) String password,
			   String nom ,
				  String prenom,
				  String numAdministratif , EtablissementDto etablissement) {
		super();
		this.username = username;
		this.personelle = personelle;
		this.role = role;
		this.password = password;
		this.nom= nom;
		this.prenom= prenom;
		this.numAdministratif= numAdministratif ;
		this.etablissement = etablissement;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNumAdministratif() {
		return numAdministratif;
	}

	public void setNumAdministratif(String numAdministratif) {
		this.numAdministratif = numAdministratif;
	}

	public SignupRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
 
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Set<String> getRole() {
      return this.role;
    }
    
    public void setRole(Set<String> role) {
      this.role = role;
    }

	public Personelle getPersonelle() {
		return personelle;
	}

	public void setPersonelle(Personelle personelle) {
		this.personelle = personelle;
	}

	public EtablissementDto getEtablissement() {
		return etablissement;
	}

	public void setEtablissement(EtablissementDto etablissement) {
		this.etablissement = etablissement;
	}

	 
    
    
}

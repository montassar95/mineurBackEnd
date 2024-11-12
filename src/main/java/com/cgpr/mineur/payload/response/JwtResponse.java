package com.cgpr.mineur.payload.response;

import java.util.List;

import com.cgpr.mineur.dto.EtablissementDto;


public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String username;
 
	private List<String> roles;
	private EtablissementDto  etablissement ;
	private String nom;
    private String prenom;

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



	public JwtResponse(String accessToken, Long id, String username,
			  List<String> roles ,EtablissementDto etablissement, String nom,String prenom ) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		 
		this.roles = roles;
		this.etablissement = etablissement;
		this.nom=nom;
		this.prenom=prenom;
		
	}

	 

	public EtablissementDto getEtablissement() {
		return etablissement;
	}



	public void setEtablissement(EtablissementDto etablissement) {
		this.etablissement = etablissement;
	}



	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

 

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

 
	
	
}

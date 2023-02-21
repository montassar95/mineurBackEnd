package com.cgpr.mineur.payload.response;

import java.util.List;

import com.cgpr.mineur.models.Personelle;


public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String username;
 
	private List<String> roles;
	private Personelle personelle;
	 

	public JwtResponse(String accessToken, Long id, String username,
			  List<String> roles ,Personelle personelle ) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		 
		this.roles = roles;
		this.personelle = personelle;
		
	}

	public Personelle getPersonelle() {
		return personelle;
	}

	public void setPersonelle(Personelle personelle) {
		this.personelle = personelle;
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

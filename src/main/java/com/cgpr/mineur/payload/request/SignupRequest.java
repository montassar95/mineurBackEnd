package com.cgpr.mineur.payload.request;

import java.util.Set;

import javax.validation.constraints.*;

import com.cgpr.mineur.models.Personelle;


 
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
 
    private Personelle personelle;
     
    
    private Set<String> role;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
  
    
    
    
    
    
    
    
    public SignupRequest(@NotBlank @Size(min = 3, max = 20) String username, Personelle personelle, Set<String> role,
			@NotBlank @Size(min = 6, max = 40) String password) {
		super();
		this.username = username;
		this.personelle = personelle;
		this.role = role;
		this.password = password;
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

	@Override
	public String toString() {
		return "SignupRequest [username=" + username + ", personelle=" + personelle + ", role=" + role + ", password="
				+ password + "]";
	}
    
    
}

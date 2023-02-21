package com.cgpr.mineur.modelsSecurity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cgpr.mineur.models.Personelle;

@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username") 
		})
public class User {
	@Id
	@GeneratedValue
	private Long id;
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	

	@NotBlank
	@Size(max = 20)
	private String username;

	 

	@NotBlank
	@Size(max = 120)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	

	@ManyToOne
	@JoinColumn(name="Personelle_UserFK")
	private Personelle personelle;
	
		public Personelle getPersonelle() {
		return personelle;
	}

	public void setPersonelle(Personelle personelle) {
		this.personelle = personelle;
	}

	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		 
		this.password = password;
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

	 

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

 
	 
	
	
}

package com.cgpr.mineur.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cgpr.mineur.converter.EtablissementConverter;
import com.cgpr.mineur.dto.EtablissementDto;
import com.cgpr.mineur.modelsSecurity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
	
	

	 
	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private EtablissementDto etablissement;

	@JsonIgnore
	private String password;
	
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

	private Collection<? extends GrantedAuthority> authorities;
	
	 

	 
	
	public UserDetailsImpl( Long id, 
			                String username,
							String password,
							Collection<? extends GrantedAuthority> authorities,
							EtablissementDto etablissement,
							String nom,  String prenom) {
		
							this.id = id;
							this.username = username; 
							this.password = password;
							this.authorities = authorities;
							this.etablissement = etablissement;
							this.nom=nom;
							this.prenom=prenom;
	                  }

	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
					user.getId(), 
					user.getUsername(), 
					user.getPassword(), 
					authorities,
					EtablissementConverter.entityToDto(user.getEtablissement()),
					user.getNom(),
					user.getPrenom());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

 
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

	public EtablissementDto getEtablissement() {
		return etablissement;
	}

	public void setEtablissement(EtablissementDto etablissement) {
		this.etablissement = etablissement;
	}

 
	
	
}

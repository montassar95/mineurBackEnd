package com.cgpr.mineur.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cgpr.mineur.models.Etablissement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
	 
	 private Long id;

	  
	    private String username;

	   

	    private String nom;
	    private String prenom;
	    
 
	    private String numAdministratif;
	    
 
	    private EtablissementDto etablissement;
	    private Set<String> roles; 

}

package com.cgpr.mineur.models;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "simp")
public class SimplifierCriteria implements Serializable {
	
	@Id
	private String simplifierId;
	private String nomSimplifie  ;
	private String prenomSimplifie  ;
	private String nomPereSimplifie; 
	private String nomGrandPereSimplifie ; 
	private String nomMereSimplifie;  
	private String prenomMereSimplifie; 
	
	@Column(name = "DATE_NAISSANCE")
	private LocalDate  dateNaissance;
	private String lieuNaissance;
	private String sexe;
	
}

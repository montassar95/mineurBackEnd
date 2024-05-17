package com.cgpr.mineur.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EtabChangeManiereDto {
	 
	private String id;

	 
	private String libelle_etabChangeManiere;

	 
	private GouvernoratDto gouvernorat;
 
}

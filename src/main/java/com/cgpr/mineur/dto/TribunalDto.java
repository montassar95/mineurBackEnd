package com.cgpr.mineur.dto;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TribunalDto {

	 
	private long id;

	private String nom_tribunal;
	
	 
	private TypeTribunalDto typeTribunal;
	
	
	 
	private GouvernoratDto gouvernorat;
}

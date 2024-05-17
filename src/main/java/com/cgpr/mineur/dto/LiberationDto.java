package com.cgpr.mineur.dto;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
public class LiberationDto {
	 
	private LiberationIdDto liberationId;

 
 
	private Date date;
	
	  
	
 
	private ArrestationDto arrestation;
	
	 
	private CauseLiberationDto causeLiberation;

	private String remarqueLiberation;
	
	 
	private EtabChangeManiereDto etabChangeManiere;
	
}

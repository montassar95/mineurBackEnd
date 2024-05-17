package com.cgpr.mineur.dto;

import java.sql.Date;

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
public class EchappesDto {
 
	private EchappesIdDto echappesId;
	
	private Date dateEchappes;
	private Date dateTrouver;
	
 
	private CommentEchapperDto commentEchapper;
 	
 
	private CommentTrouverDto commentTrouver;
	
	
	private String remarqueEchappes;
	
	private String remarqueTrouver;
	  
	 
	private ResidenceDto residenceEchapper;
 
	
 
	private ResidenceDto residenceTrouver;
	
	
	
	
	

}

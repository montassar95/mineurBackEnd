package com.cgpr.mineur.dto;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itextpdf.text.Phrase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArrestationDto {
	 
 
 

 
 
 
 
	 
	private ArrestationIdDto arrestationId;

	 
	
//     @JsonIgnoreProperties({"arrestation" })
//      private List<AffaireDto> affairesDto;
	
	
	
	
	
	
	
	 
	private EnfantDto enfant;

	 
	private Date date;

	 

	private int statut;
	
//	private String etatJuridique;
	
 
	private LiberationDto liberation;
	
	
	 
//	private String numAffairePricipale;
	
	
	 
//	private TribunalDto tribunalPricipale;
	
//	private long numOrdinalAffairePricipale;
	
 
//	private TypeAffaireDto typeAffairePricipale;
	
//	private int totaleEchappes;
//	private int totaleResidence;
	
	
	
	
	
  
   
	 
	  
	  
 	 
	   private   java.util.Date dateDebut;
	   
 	 
	   private   java.util.Date dateFin;
	   
  
	   private EchappesDto echappe  ;
	   
	   
  
//	   private String situationJudiciaire ;
	   
	   
 	 
	   private int  age ;
	   
	   
	  
	   private String  visite ;
	   
 
}

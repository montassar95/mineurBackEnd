package com.cgpr.mineur.models;

import java.io.Serializable;
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
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.FetchType;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "arr")
public class Arrestation  implements Serializable {
	 
 
 

 
 
 
 
	@EmbeddedId
	private ArrestationId arrestationId;

	 
	
     @JsonIgnoreProperties({"arrestation" })
      @Transient
      private List<Affaire> affaires;
	
	
	
	
	
	
	
	@ManyToOne
	@JoinColumn(name = "enfFK")
	private Enfant enfant;

	@Column(name = "dateArrestation")
	private Date date;

	 

	private int statut;
	
	private String etatJuridique;
	
	@OneToOne
	@JoinColumns({ 
		@JoinColumn(name = "l_idEnf", referencedColumnName = "idEnf"),
		@JoinColumn(name = "l_numOrd", referencedColumnName = "numOrd")
			 
	})
	private Liberation liberation;
	
	
	 
	private String numAffairePricipale;
	
	
	@ManyToOne
	@JoinColumn(name = "triPriFK")
	private Tribunal tribunalPricipale;
	
	private long numOrdinalAffairePricipale;
	
	@Transient
	private TypeAffaire typeAffairePricipale;
	
	private int totaleEchappes;
	private int totaleResidence;
	
	
	
	
	
  
   
	 
	  
	  
 	   @Transient
	   private   java.util.Date dateDebut;
	   
 	   @Transient
	   private   java.util.Date dateFin;
	   
 	   @Transient
	   private Echappes echappe ;
	   
	   
 	   @Transient
	   private String situationJudiciaire ;
	   
	   
 	   @Transient
	   private int  age ;
	   
	   
//	   @Transient
//	   private String  visite ;
	   
 
}

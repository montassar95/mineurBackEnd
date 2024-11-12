package com.cgpr.mineur.models;

 

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class RapportEnfantQuotidien  implements Serializable {
	

	@EmbeddedId
	private RapportEnfantQuotidienId rapportEnfantQuotidienId;
	 
	 
	
	
	private String statutPenal;
	
	
	@ManyToOne
	@JoinColumn(name = "etaFK")
	private Etablissement etablissement;
	
	
	@Lob
	private String residance;
}
 

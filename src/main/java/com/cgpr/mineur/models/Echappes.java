package com.cgpr.mineur.models;

import java.sql.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "ech")
public class Echappes {
	
	@EmbeddedId
	private EchappesId echappesId;
	
	private Date dateEchappes;
	private Date dateTrouver;
	
	@ManyToOne
	@JoinColumn(name = "comEchFK")
	private CommentEchapper commentEchapper;
//	
	@ManyToOne
	@JoinColumn(name = "comTroFK")
	private CommentTrouver commentTrouver;
	
	
	private String remarqueEchappes;
	
	private String remarqueTrouver;
	  
	@ManyToOne
	@JoinColumns({
		    @JoinColumn(name = "r_idEnf_e", referencedColumnName = "idEnf"),
			@JoinColumn(name = "r_numOrdArr_e", referencedColumnName = "numOrdArr"),
			@JoinColumn(name = "r_numOrdRes_e", referencedColumnName = "numOrdRes")
		        })
	private Residence residenceEchapper;
//	
	
	@ManyToOne
	@JoinColumns({
		    @JoinColumn(name = "r_idEnf_t", referencedColumnName = "idEnf"),
			@JoinColumn(name = "r_numOrdArr_t", referencedColumnName = "numOrdArr"),
			@JoinColumn(name = "r_numOrdRes_t", referencedColumnName = "numOrdRes")
		        })
	private Residence residenceTrouver;
	
	
	
	
	

}

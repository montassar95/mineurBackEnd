package com.cgpr.mineur.models;

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
public class Arrestation {
	 
 
 

 
 
 
 
	@EmbeddedId
	private ArrestationId arrestationId;

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
	
//	, cascade = CascadeType.REMOVE
	//@OneToMany(mappedBy = "arrestation", fetch = FetchType.EAGER)
	//private List<Affaire> affaires;
}

package com.cgpr.mineur.models;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
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
@Table(name = "Visite")
public class Visite  implements Serializable {
	 

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visite_seq")
    @SequenceGenerator(name = "visite_seq", sequenceName = "VISITE_SEQ", allocationSize = 1)
	private long enfantIdVisite;
	

	 
	private int anneeVisite;
	private int moisVisite;
	private int nbrVisite; 
	
	@ManyToOne
	@JoinColumns({
		    @JoinColumn(name = "r_idEnf_v", referencedColumnName = "idEnf"),
			@JoinColumn(name = "r_numOrdArr_v", referencedColumnName = "numOrdArr"),
			@JoinColumn(name = "r_numOrdResi_v", referencedColumnName = "numOrdRes")
		        })
	private Residence residenceVisite ;
	
	
 
	    @OneToOne(fetch = FetchType.LAZY )
	    @JoinColumn(name = "enfant_id", nullable = false)
	    private Enfant enfant;

}

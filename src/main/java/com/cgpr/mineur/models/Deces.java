package com.cgpr.mineur.models;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "deces")
public class Deces  implements Serializable {
	
	@Id
	private long enfantIdDeces;
	
	private Date dateDeces;
	 
	
	@ManyToOne
	@JoinColumn(name = "cauDecFK")
	private CauseDeces causeDeces;
	
	@ManyToOne
	@JoinColumn(name = "lieDecFK")
	private LieuDeces lieuDeces;
	
	private String remarqueDeces;
	
	 
	  
	@ManyToOne
	@JoinColumns({
		    @JoinColumn(name = "r_idEnf_d", referencedColumnName = "idEnf"),
			@JoinColumn(name = "r_numOrdArr_d", referencedColumnName = "numOrdArr"),
			@JoinColumn(name = "r_numOrdResi_d", referencedColumnName = "numOrdRes")
		        })
	private Residence residenceDeces ;
	
	
 
	 @OneToOne(fetch = FetchType.LAZY )
	    @JoinColumn(name = "enfant_id", nullable = false)
	    private Enfant enfant;

}

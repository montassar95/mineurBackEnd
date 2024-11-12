package com.cgpr.mineur.models;

import java.io.Serializable;
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
import javax.persistence.Entity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "lib")
public class Liberation  implements Serializable {
	@EmbeddedId
	private LiberationId liberationId;

 
	@Column(name = "dateLiberation")
	private Date date;
	
	  
	
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "arr_idEnf", referencedColumnName = "idEnf"),
			@JoinColumn(name = "arr_numOrd", referencedColumnName = "numOrd") })
	private Arrestation arrestation;
	
	@ManyToOne
	@JoinColumn(name = "cauLibFK")
	private CauseLiberation causeLiberation;

	private String remarqueLiberation;
	
	@ManyToOne
	@JoinColumn(name = "etabChange")
	private EtabChangeManiere etabChangeManiere;
	
}

package com.cgpr.mineur.models;
import javax.persistence.Entity;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "accCarDep")
public class AccusationCarteDepot {

	@EmbeddedId
	private AccusationCarteDepotId accusationCarteDepotId;
                                   
	 

	@ManyToOne
	private CarteDepot carteDepot;
 
	@ManyToOne
	@JoinColumn(name = "titreAccusationFK")
	private TitreAccusation titreAccusation;
 

}

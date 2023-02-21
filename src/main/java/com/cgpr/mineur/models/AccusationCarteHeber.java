package com.cgpr.mineur.models;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "accCarHeb")
public class AccusationCarteHeber {

	@EmbeddedId
	private AccusationCarteDepotId accusationCarteHeberId;
                                   
	
	@ManyToOne
	private CarteHeber carteHeber;
 
 	
	@ManyToOne
	@JoinColumn(name = "titreAccHebFK")
	private TitreAccusation titreAccusation;
// 

}

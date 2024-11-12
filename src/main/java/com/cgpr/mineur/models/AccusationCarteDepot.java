package com.cgpr.mineur.models;
import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "accCarDep")
public class AccusationCarteDepot  implements Serializable {

	@EmbeddedId
	private AccusationCarteDepotId accusationCarteDepotId;
                                   
	 

	@ManyToOne
	private CarteDepot carteDepot;
//	(fetch = FetchType.EAGER)
	@ManyToOne
	@JoinColumn(name = "titreAccusationFK")
	private TitreAccusation titreAccusation;
 

}

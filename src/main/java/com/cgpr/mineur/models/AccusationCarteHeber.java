package com.cgpr.mineur.models;
import java.io.Serializable;

import javax.persistence.Column;
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
@Table(name = "accCarHeb")
public class AccusationCarteHeber  implements Serializable {

	@EmbeddedId
	private AccusationCarteHeberId accusationCarteHeberId;
                                   
	
	@ManyToOne
	private CarteHeber carteHeber;
 
 	
	@ManyToOne 
	@JoinColumn(name = "titreAccHebFK")
	private TitreAccusation titreAccusation;
 
//	@Column(name = "type", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'hebergement'")
//	private String type;

}

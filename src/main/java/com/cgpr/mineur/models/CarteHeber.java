package com.cgpr.mineur.models;

import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "carHeb")
@DiscriminatorValue("CarteHeber")
public class CarteHeber extends Document {

	private static final long serialVersionUID = 1L;
 

 
	private String textJugement;

 
	
	@ToString.Exclude
	@OneToMany(mappedBy = "carteHeber"  )
    private List<AccusationCarteHeber> accusationCarteHebers;

}

package com.cgpr.mineur.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;

import com.cgpr.mineur.dto.TitreAccusationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
//@ToString(callSuper = true) 
@Entity
@Table(name = "carDep")
@DiscriminatorValue("CarteDepot")
public class CarteDepot extends Document {

	private static final long serialVersionUID = 1L;

	 

 
	private String textJugement;
	
	@ToString.Exclude
	@OneToMany(mappedBy = "carteDepot"  )
    private List<AccusationCarteDepot> accusationCarteDepots = new ArrayList<AccusationCarteDepot>();
  
}

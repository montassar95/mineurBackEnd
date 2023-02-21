package com.cgpr.mineur.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
//@NoArgsConstructor
//@AllArgsConstructor
@Data
@Entity
@Table(name = "tra")
@DiscriminatorValue("Transfert")
public class Transfert extends Document {

	private static final long serialVersionUID = 1L;

 
	
	private String typeFile;
	@ManyToOne
	@JoinColumn(name = "resTranFK")
	private ResultatTransfert resultatTransfert;
	 

}

package com.cgpr.mineur.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;

import lombok.Data;
import javax.persistence.Entity;
 
@Data
@Entity
@Table(name = "appEnf")
@DiscriminatorValue("AppelEnfant")
public class AppelEnfant extends Document {

	private static final long serialVersionUID = 1L;
 
 

	 

}

package com.cgpr.mineur.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
 
@SuperBuilder
@Data
@NoArgsConstructor  // Add this to ensure a no-args constructor is available
@Entity
@Table(name = "observation")
@DiscriminatorValue("Observation")
public class Observation extends Document {

	private static final long serialVersionUID = 1L;
 
 

	 

}

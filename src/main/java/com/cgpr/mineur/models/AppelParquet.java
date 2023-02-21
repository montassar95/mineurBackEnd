package com.cgpr.mineur.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;

import lombok.Data;

import javax.persistence.Entity;
@Data
@Entity
@Table(name = "appPar")
@DiscriminatorValue("AppelParquet")
public class AppelParquet extends Document {

	private static final long serialVersionUID = 1L;

 

	 

}

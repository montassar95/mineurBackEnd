package com.cgpr.mineur.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Data
@NoArgsConstructor
@Entity
@Table(name = "appPar")
@DiscriminatorValue("AppelParquet")
public class AppelParquet extends Document {

	private static final long serialVersionUID = 1L;

 

	 

}

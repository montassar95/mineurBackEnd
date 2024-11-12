package com.cgpr.mineur.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class RapportEnfantQuotidienId implements Serializable {

	private static final long serialVersionUID = 1L;
	
 
	private String idEnfant;

	 
	private LocalDateTime dateSauvgarde;
	
	 

}
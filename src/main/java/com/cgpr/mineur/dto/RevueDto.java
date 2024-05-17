package com.cgpr.mineur.dto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RevueDto extends DocumentDto {

	 
	

	private String textJugement;



	 

}

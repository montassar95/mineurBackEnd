package com.cgpr.mineur.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EchappesIdDto   {

	 
	 	private String idEnfant;

 	 private long numOrdinaleArrestation;
 	private long numOrdinaleEchappes;

}
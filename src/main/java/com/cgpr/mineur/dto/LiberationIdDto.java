package com.cgpr.mineur.dto;

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
public class LiberationIdDto  {

 

	 
	private String idEnfant;

	 
	private long numOrdinale;

}
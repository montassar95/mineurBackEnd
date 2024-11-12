package com.cgpr.mineur.dto;

import com.cgpr.mineur.models.MotifArreterlexecution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
 
 @SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArreterlexecutionDto extends DocumentDto { 
	
	 
	 
	private String typeFile;
	 
	private MotifArreterlexecutionDto motifArreterlexecution;
	

}



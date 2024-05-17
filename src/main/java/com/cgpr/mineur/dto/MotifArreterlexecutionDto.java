package com.cgpr.mineur.dto;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MotifArreterlexecutionDto {

	
	@Id
	private long id;
	

	private String libelleMotifArretere;
}

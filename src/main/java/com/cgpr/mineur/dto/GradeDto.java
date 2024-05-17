package com.cgpr.mineur.dto;

import javax.persistence.GeneratedValue;
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
public class GradeDto {
	 
	private long id;
	 

	private String libelle_grade;

}

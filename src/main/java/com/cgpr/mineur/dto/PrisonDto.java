package com.cgpr.mineur.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

public class PrisonDto {
 
    private String codeGouvernorat;
	private String codePrison;
	private String  name ;
	 

 
}

package com.cgpr.mineur.dto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

 
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransfertDto extends DocumentDto {

 
 
	
	private String typeFile;
 
	private ResultatTransfertDto resultatTransfert;
	 

}

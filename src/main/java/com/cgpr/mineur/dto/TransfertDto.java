package com.cgpr.mineur.dto;

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
public class TransfertDto extends DocumentDto {

 
 
	
	private String typeFile;
 
	private ResultatTransfertDto resultatTransfert;
	 

}

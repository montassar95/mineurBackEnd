package com.cgpr.mineur.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatistiqueTribunalDTO {
	// private String id;
	 
    private String gouvernorat ;
    private String tribunal;
    private Long nombreArrete;
    
    private Long nombreJuge;
    
    private Long nombreAutre;
     
   
}

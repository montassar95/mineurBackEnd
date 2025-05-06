package com.cgpr.mineur.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatistiqueGouvernoratDTO {
	// private String id;
	 
    private String gouvernorat ;
     
    
    private Long arretemasculin;
    
	private Long arretefeminin;
	    
	private Long jugemasculin;
	    
    private Long jugefeminin; 
}

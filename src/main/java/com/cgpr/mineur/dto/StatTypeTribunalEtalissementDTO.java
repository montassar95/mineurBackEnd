package com.cgpr.mineur.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatTypeTribunalEtalissementDTO {
 
    private String typeTribunal;
    private Long sommeMourouj;
    private Long sommeSidiHeni;
    private Long sommeSoukjdid;
    private Long sommeMghira;
    private Long sommeMjazbeb;
    
    private Long sommeTypeTribunal;
}


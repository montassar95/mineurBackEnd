package com.cgpr.mineur.resource;
 

 

import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Residence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EnfantAddDTO {
 
	private Enfant enfant;
	private Arrestation arrestation;
	private Residence residence;
	private Etablissement etablissement;
	private String img;
}

package com.cgpr.mineur.dto;

import java.util.List;

import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.models.Residence;

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
public class EnfantVerifieDto {
	private Enfant enfant;
	private String situation;
	private String age;
	private String adultDate;
	private List<Arrestation> arrestations;
 
	private boolean allowNewAddArrestation;
	private boolean allowNewCarte;
	private boolean alerte;
	private boolean libre;
	private ResidenceDto residence;
	private ResidenceDto residenceEncour;
 
}

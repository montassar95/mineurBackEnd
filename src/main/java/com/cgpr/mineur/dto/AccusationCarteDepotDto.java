package com.cgpr.mineur.dto;

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
public class AccusationCarteDepotDto {

	private AccusationCarteDepotIdDto accusationCarteDepotId;

	private CarteDepotDto carteDepot;

	private TitreAccusationDto titreAccusation;

}


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
public class AccusationCarteDepotIdDto {

	private String idEnfant;

	private long numOrdinalArrestation;

	private long numOrdinalAffaire;

	private long numOrdinalDoc;

	private long numOrdinalDocByAffaire;

	private long idTitreAccusation;

}
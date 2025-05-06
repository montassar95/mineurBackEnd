package com.cgpr.mineur.dto;

import java.time.LocalDate;
import java.util.List;

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
public class PenaleDetentionInfoDto  {

	   private String numroDetention;
	    private String prisonerId;
	    private String prision;
	    private String numeroEcrou;
	    private String dateEntre;
	    private String dateSortie;
}

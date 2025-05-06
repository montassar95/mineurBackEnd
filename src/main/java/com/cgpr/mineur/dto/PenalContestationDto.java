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
public class PenalContestationDto  {

	 private String tnumide;
	    private String tcoddet;
	    private String tnumseqaff;
	    private String tcodco;
	    private String tdatco;
	    private String numAffaire;
	    private String libelleTribunal;
	    private String tlibtco;
}

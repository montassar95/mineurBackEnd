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
public class PenalTransfertDto  {

	private String tnumide;
    private String tcoddet;
    private String tnumseqaff;
    private String tcodtraf;
    private String tdattran;
    private String tnumjaf;
    private String libelleTribunalDepart;
    private String tnumjafn;
    private String libelleTribunalArrivee;
}

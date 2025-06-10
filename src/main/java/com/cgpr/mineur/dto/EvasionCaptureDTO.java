package com.cgpr.mineur.dto;

import java.sql.Date;
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
public class EvasionCaptureDTO {

	private String tnumide;
    private String tcoddet;
    private String tcodeva;
    private String dateEvasion;
    private String heureEvasion;
    private String procedureEvasion;
    private String numeroEcrouEvasion;
    private String prisonEvasion;

    private String tcodcap;
    private String dateCapture;
    private String dateEntree;
    private String numeroEcrouCapture;
    private String prisonCapture;
}

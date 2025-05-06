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
public class PenalMandatDepotDTO {

    private String tnumide;
    private String tcoddet;
    private String tnumseqaff;
    private String tcodma;
    private String tdatdma;
    private String tdatama;
    private String ttextma;
}

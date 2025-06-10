package com.cgpr.mineur.dto;

import java.sql.Date;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MutationResidenceDTO {
	
	 
	private String numeroLigne;
    private String prisonerId;
    private String numroDetention;
    private String numeroSequentielle;
    private String numeroEcrou;
    private String prision;
    private String dateDebut;
    private String dateFin;
    private String dateMutation;
    private String motifMutation;

}

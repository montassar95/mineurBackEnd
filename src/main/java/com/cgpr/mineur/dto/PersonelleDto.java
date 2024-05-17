package com.cgpr.mineur.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonelleDto {
	@Id
	private long id;
	private long matricule;
	private String cnrps;
	private String nom;
	private String prenom;
	private String nom_pere;

	 
	private GradeDto gradeDto;

 
	private EtablissementDto etablissement;

 
	private FonctionDto fonction;

 
	private SituationDto situation;

 
 
	private String img;

}

package com.cgpr.mineur.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "per")
public class Personelle  implements Serializable {
	@Id
	private long id;
	private long matricule;
	private String cnrps;
	private String nom;
	private String prenom;
	private String nom_pere;

	@ManyToOne
	@JoinColumn(name = "gradeFK")
	private Grade grade;

	@ManyToOne
	@JoinColumn(name = "etaFK")
	private Etablissement etablissement;

	@ManyToOne
	@JoinColumn(name = "fonFK")
	private Fonction fonction;

	@ManyToOne
	@JoinColumn(name = "sitFk")
	private Situation situation;

 
	@Lob
	private String img;

}

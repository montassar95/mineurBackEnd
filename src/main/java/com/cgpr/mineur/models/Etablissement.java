package com.cgpr.mineur.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "eta")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Etablissement implements Serializable {
	@Id
	private String id;

	private String libelle_etablissement;

	@ManyToOne
	@JoinColumn(name = "code_gouv")
	private Gouvernorat gouvernorat;

	@Column(columnDefinition = "integer default 0")
	private int statut;

	public Etablissement(String id) {
		super();
		this.id = id;
	}

}

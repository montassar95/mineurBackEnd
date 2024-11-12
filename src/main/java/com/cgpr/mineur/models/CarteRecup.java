package com.cgpr.mineur.models;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
 
@Entity
@Table(name = "carRec")
@DiscriminatorValue("CarteRecup")
public class CarteRecup extends Document {

	private static final long serialVersionUID = 1L;

	private String textJugement;

	@ManyToOne
	@JoinColumn(name = "typJugFK")
	private TypeJuge typeJuge;

	private long daysDiffJuge;

	private int jour;
	private int mois;
	private int annee;

	private long daysDiffArretProvisoire;

	private int jourArretProvisoire;
	private int moisArretProvisoire;
	private int anneeArretProvisoire;

	private Date dateDebutPunition;

	private Date dateFinPunition;

	private String etatJuridiqueAvant;
	 // Ajout de la relation avec AccusationCarteRecup
//	, cascade = CascadeType.ALL, fetch = FetchType.LAZY
	@ToString.Exclude
    @OneToMany(mappedBy = "carteRecup"  )
    private List<AccusationCarteRecup> accusationCarteRecups;
	
	@ToString.Exclude
    @OneToMany(mappedBy = "carteRecup" )
    private List<ArretProvisoire> arretProvisoires;

}

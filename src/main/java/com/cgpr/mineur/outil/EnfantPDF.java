package com.cgpr.mineur.outil;

import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;

import com.cgpr.mineur.models.ClassePenale;
import com.cgpr.mineur.models.Delegation;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Gouvernorat;
import com.cgpr.mineur.models.Metier;
import com.cgpr.mineur.models.Nationalite;
import com.cgpr.mineur.models.NiveauEducatif;
import com.cgpr.mineur.models.SituationFamiliale;
import com.cgpr.mineur.models.SituationSocial;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.models.Tribunal;
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.models.TypeJuge;

public class EnfantPDF {
	
	
	private TypeAffaire typeAffaire;
	

	
	private String numAffaire;
	
	private Tribunal tribunal;
	
	private TypeJuge typeJuge;
	
	private  int jugeA  ;
	private   int jugeM  ;
	private    int jugeJ ;
	
	private boolean affectBoolean;
	
    private String numAffaireAffect;
	
	private Tribunal tribunalAffect;
	
	private boolean aexBoolean;
	
	private List<TitreAccusation> titreAccusations;
	
	private List<DocumentAndDate> documentAndDates;
	
	private  Date dateDebutPunition;
	
	private  Date dateFinPunition;
	
	
	
	private String id;
	private String nom;
	private String prenom;

	private String nomPere;
	private String nomGrandPere;
	private String nomMere;
	private String prenomMere;

	private Date dateNaissance;
	private String lieuNaissance;
	private String sexe;
	
	
	
	
 
	private Nationalite nationalite;
    private NiveauEducatif niveauEducatif;
    private SituationFamiliale situationFamiliale;
    private Gouvernorat gouvernorat;
    private Delegation delegation;
    private String adresse;
    private ClassePenale classePenale;
	private SituationSocial situationSocial;
	private Metier metier;
	
	
	private String numArrestation;
	private Date dateArrestation;
	
	private Etablissement etablissement;
	
	
	
	

}

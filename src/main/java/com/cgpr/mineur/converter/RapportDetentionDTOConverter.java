package com.cgpr.mineur.converter;

 

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.cgpr.mineur.dto.RapportDetentionDTO;
import com.cgpr.mineur.dto.RapportDetentionDTO.Affaire;
import com.cgpr.mineur.dto.RapportDetentionDTO.EvenementJuridique;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.tools.ToolsForReporting;

public class RapportDetentionDTOConverter {

    // Méthode pour convertir un objet de type Detenu en RapportDetentionDTO
    public static RapportDetentionDTO toRapportDetentionDTO(Residence residence) {
        if (residence == null) {
            return null;
        }

        return RapportDetentionDTO.builder()
                .detentionId(residence.getResidenceId().getIdEnfant())
                .nomComplet(residence.getArrestation().getEnfant().getNom() + " بن " + residence.getArrestation().getEnfant().getNomPere() + " بن " + residence.getArrestation().getEnfant().getNomGrandPere() + " " + residence.getArrestation().getEnfant().getPrenom())  // Combiner nom et prénom
                
                .detailsNaissance("بـــــــ" + residence.getArrestation().getEnfant().getLieuNaissance() + " " + residence.getArrestation().getEnfant().getDateNaissance())
               
                .adresse(residence.getArrestation().getEnfant().getAdresse().toString().trim() + " " + residence.getArrestation().getEnfant().getDelegation().getLibelle_delegation().toString() +
        	            " " + residence.getArrestation().getEnfant().getGouvernorat().getLibelle_gouvernorat().toString())
                
                
                
                .situationFamilialeEtSociale(residence.getArrestation().getEnfant().getSituationSocial().getLibelle_situation_social().toString().trim() + " " +
                		residence.getArrestation().getEnfant().getSituationFamiliale().getLibelle_situation_familiale().toString().trim())
                
                .niveauDeFormationEtProfession(residence.getArrestation().getEnfant().getMetier().getLibelle_metier().toString().trim() + " " +
                		residence.getArrestation().getEnfant().getNiveauEducatif().getLibelle_niveau_educatif().toString().trim())
                
                
                .dateArrestation(residence.getArrestation().getDate())
                
                .dateDebutPeine(residence.getArrestation().getDateDebut())
                .dateLiberation(residence.getArrestation().getDateFin())
                
                
                .mouvementArrivee(residence.getDateEntree() != null && residence.getEtablissementEntree() != null && residence.getCauseMutation() != null
                ? residence.getDateEntree().toString() + " " + "قدم من" + " " +
                  residence.getEtablissementEntree().getLibelle_etablissement() + " (" +
                  residence.getCauseMutation().getLibelle_causeMutation() + ") يوم "
                : "")
            .mouvementSortie(residence.getDateSortie() != null && residence.getEtablissementSortie() != null && residence.getCauseMutationSortie() != null
                ? residence.getDateSortie().toString() + " " + "نقل  إلى" + " " +
                  residence.getEtablissementSortie().getLibelle_etablissement() + " (" +
                  residence.getCauseMutationSortie().getLibelle_causeMutation() + ") يوم "
                : "")
                
                .mutationEnCours(residence.getStatut()==2)
                
                .liberation(
                	    (residence.getArrestation() != null && residence.getArrestation().getLiberation() != null
                	        && residence.getArrestation().getLiberation().getDate() != null
                	        && residence.getArrestation().getLiberation().getCauseLiberation() != null
                	        && residence.getArrestation().getLiberation().getCauseLiberation().getLibelleCauseLiberation() != null)
                	    ? residence.getArrestation().getLiberation().getDate().toString() + " " +
                	      residence.getArrestation().getLiberation().getCauseLiberation().getLibelleCauseLiberation()
                	    : "")

                
                .etablissementActuel(residence.getEtablissement().getLibelle_etablissement())
                .classePenale(calculerClassePenale(residence))
                .age(calculerAge(residence .getArrestation().getEnfant().getDateNaissance().toString())+ "")
               .nombreVisites( residence.getNbVisite())
               
                .nationalite( residence.getArrestation().getEnfant().getNationalite().getLibelle_nationalite())
                
                
                
                .situationJudiciaire(determineStatus(residence))
               
                
                
               .numeroEcrou(residence.getNumArrestation())
                
              
                
            
                  // Assurez-vous que ce champ existe dans Detenu
               .evasion(
            		    residence != null &&
            		    residence.getArrestation() != null &&
            		    residence.getArrestation().getEchappe() != null &&
            		    residence.getArrestation().getEchappe().getDateTrouver() != null
            		)
                .affaires(toAffaireList(residence.getArrestation().getAffaires())) // Convertir la liste des affaires
                .build();
    }

    private static List<RapportDetentionDTO.Affaire> toAffaireList(List<com.cgpr.mineur.models.Affaire> affaires) {
        if (affaires == null) {
            return null;
        }

        // Utilisation d'AtomicInteger pour incrémenter un numéro pour chaque affaire
        AtomicInteger counter = new AtomicInteger(1); // Initialisation à 1 pour le premier numéro

        return affaires.stream()
                .map(affaire -> RapportDetentionDTO.Affaire.builder()
                        .numeroOrdinalAffaire(counter.getAndIncrement()) // Incrément du numéro sans formatage
                        .numeroAffaire(affaire.getAffaireId().getNumAffaire().toString())
                        .tribunal(affaire.getTribunal().getNom_tribunal())
                        .typeAffaire(affaire.getTypeAffaire().getLibelle_typeAffaire())
                        .accusations(ToolsForReporting.buildTitreAccusationString(affaire)) // Accusations
                        .evenementJuridiqueList(processEvenementsJuridique(affaire)) // Liste des événements juridiques
                        .jugement(ToolsForReporting.buildTitreRemarque(affaire)) // Jugement
                        .build())
                .collect(Collectors.toList());
    }



    // Méthode pour convertir une liste d'événements juridiques
    private static List<RapportDetentionDTO.EvenementJuridique> toEvenementJuridiqueList(List<EvenementJuridique> evenementJuridiqueList) {
        if (evenementJuridiqueList == null) {
            return null;
        }

        return evenementJuridiqueList.stream()
                .map(event -> RapportDetentionDTO.EvenementJuridique.builder()
                        .evenement(event.getEvenement())
                        .date(event.getDate())
                        .build())
                .collect(Collectors.toList());
    }
    
    
    public static String calculerClassePenale(Residence  residence ) {
        String classePenale = "--";

        // Vérifier que l'arrestation et son identifiant existent
        if (residence != null && residence.getArrestation() != null
                && residence.getArrestation().getArrestationId() != null) {
            int nbrRetour = (int) residence.getArrestation().getArrestationId().getNumOrdinale();

            // Si nbrRetour est supérieur à 1, construire la classe pénale spécifique
            if (nbrRetour > 1) {
                classePenale = "عــ" + "0" + nbrRetour + "" + "ـــــــــائـد";
            } else {
                // Sinon, prendre le libellé de la classe pénale de l'enfant
                if (residence .getArrestation().getEnfant() != null) {
                    classePenale = residence .getArrestation().getEnfant().getClassePenale().getLibelle_classe_penale();
                }
            }
        }

        return classePenale;
    }
    
    public static int calculerAge(String dateNaissance) {
        // Analyser la chaîne de caractères de date de naissance en LocalDate
        LocalDate dob = LocalDate.parse(dateNaissance);

        // Obtenir la date actuelle
        LocalDate curDate = LocalDate.now();

        // Calculer la période entre la date de naissance et la date actuelle
        Period period = Period.between(dob, curDate);

        // Retourner l'âge en années
        return period.getYears();
    }
    
	public static String determineStatus(Residence residence) {
//		String status;
		if(residence.getArrestation().getSituationJudiciaire()!=null) {
			 switch (residence.getArrestation().getSituationJudiciaire().toString()) {
	         case "juge":
	        	 return"محكـــوم" ;
	             
	         case "arret":
	        	 return "موقـــوف" ;
	             
	         case "libre":
	        	 return "في حالـــة ســراح" ;
	            
	             
	         case "pasInsertionLiberable":
	        	 return "لم يتم إدراج السراح" ;
	              
	         default:
	        	 return "...";
	            
	     }
		};
		return "vide";
		
		
 
	}
	
 
	public static List<EvenementJuridique> processEvenementsJuridique(com.cgpr.mineur.models.Affaire affaire) {
	    List<EvenementJuridique> evenementsJuridique = new ArrayList<>();
	    
	    try {
	        // Traitement du type de document
	        switch (affaire.getTypeDocument()) {
	            case "CD":
	                evenementsJuridique.add(new EvenementJuridique("بطاقة إيداع", affaire.getDateEmission()));
	                break;

	            case "CH":
	                evenementsJuridique.add(new EvenementJuridique("بطاقة إيواء", affaire.getDateEmission()));
	                break;

	            case "AEX":
	                evenementsJuridique.add(new EvenementJuridique("تاريخ الحكم", affaire.getDateEmissionDocument()));
	                String typeFileAEX = affaire.getTypeFile();
	                String typeAEX = (typeFileAEX == null || typeFileAEX.equals("AEX")) ? "إيقاف تنفيذ الحكم" : "ســــــــــــراح";
	                evenementsJuridique.add(new EvenementJuridique(typeAEX, affaire.getDateEmissionDocument()));
	                break;

	            case "CJ":
	                evenementsJuridique.add(new EvenementJuridique("مضمون حكم", affaire.getDateEmissionDocument()));
	                break;

	            case "T":
	                evenementsJuridique.add(new EvenementJuridique("صدور البطاقة", affaire.getDateEmissionDocument()));
	                evenementsJuridique.add(new EvenementJuridique(getTypeFileStatusAbondantAffaire(affaire.getTypeFile()), affaire.getDateEmissionDocument()));
	                break;

	            case "AE":
	                evenementsJuridique.add(new EvenementJuridique("تاريخ الحكم", affaire.getDateEmissionDocument()));
	                evenementsJuridique.add(new EvenementJuridique("استئناف الطفل", affaire.getDateEmissionDocument()));
	                break;

	            case "AP":
	                evenementsJuridique.add(new EvenementJuridique("تاريخ الحكم", affaire.getDateEmissionDocument()));
	                evenementsJuridique.add(new EvenementJuridique("استئناف النيابة", affaire.getDateEmissionDocument()));
	                break;

	            case "CR":
	                evenementsJuridique.add(new EvenementJuridique("تاريخ الحكم", affaire.getDateEmissionDocument()));
	                evenementsJuridique.add(new EvenementJuridique("مراجعة", affaire.getDateEmissionDocument()));
	                break;

	            case "CRR":
	                evenementsJuridique.add(new EvenementJuridique("تاريخ الحكم", affaire.getDateEmissionDocument()));
	                evenementsJuridique.add(new EvenementJuridique("رفض المراجعة", affaire.getDateEmissionDocument()));
	                break;

	            case "CP":
	                evenementsJuridique.add(new EvenementJuridique("صدور البطاقة", affaire.getDateEmissionDocument()));
	                evenementsJuridique.add(new EvenementJuridique("قرار تمديد", affaire.getDateEmissionDocument()));
	                break;

	            case "CHL":
	                evenementsJuridique.add(new EvenementJuridique("تاريخ القضية", affaire.getDateEmissionDocument()));
	                evenementsJuridique.add(new EvenementJuridique("تغير مكان الإيداع", affaire.getDateEmissionDocument()));
	                break;

	            default:
	                evenementsJuridique.add(new EvenementJuridique("-----", affaire.getDateEmission()));
	                break;
	        }

	        // Ajouter les événements supplémentaires selon la date de début et de fin de la punition
	        if (affaire.getDateDebutPunition() != null) {
	            evenementsJuridique.add(new EvenementJuridique("تاريخ البداية", affaire.getDateDebutPunition()));
	        }

	        if (affaire.getDateFinPunition() != null && !"AP".equals(affaire.getTypeDocument())) {
	            evenementsJuridique.add(new EvenementJuridique("تاريخ النهاية", affaire.getDateFinPunition()));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return evenementsJuridique;
	}
	public static String getTypeFileStatusAbondantAffaire(String typeFile) {
	    if (typeFile != null) {
	        switch (typeFile.trim()) {
	            case "T":
	                return "إحــــــالة";
	            case "A":
	                return "تخلــــــي";
	            case "G":
	                return "تعهــــــد";
	            default:
	                return "yyy";
	        }
	    }

	    return "إحــــــالة";
	}
}

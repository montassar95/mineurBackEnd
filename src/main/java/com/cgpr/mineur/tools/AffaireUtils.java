package com.cgpr.mineur.tools;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.dto.AffaireDto;
import com.cgpr.mineur.dto.ArrestationDto;
import com.cgpr.mineur.dto.FicheDeDetentionDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ChangementLieu;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.ResidenceRepository;

public final class AffaireUtils {

    private AffaireUtils() {
        // Le constructeur privé empêche l'instanciation de cette classe.
    }
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static void traiterChangementLieu(AffaireDto element, FicheDeDetentionDto dto, DocumentRepository documentRepository,ResidenceRepository residenceRepository) {
    	 List<Document> documents = documentRepository.getDocumentByAffaire(
    		        element.getArrestation().getArrestationId().getIdEnfant(),
    		        element.getArrestation().getArrestationId().getNumOrdinale(),
    		        element.getNumOrdinalAffaire()
    		    );

    		    if (!documents.isEmpty()) {
    		        Document document1 = documents.get(0);
    		        Document document2 = documents.get(1);

    		        if (document1 instanceof ChangementLieu) {
    		            ChangementLieu changementLieu = (ChangementLieu) document1;

    		            if ("changementEtab".equals(changementLieu.getType()) &&
    		                element.getArrestation() != null &&
    		                element.getArrestation().getLiberation() == null) {
    		                dto.setChangementLieuCh(true);
    		            } else if ("mutation".equals(changementLieu.getType())) {
    		                Residence residence = residenceRepository.findMaxResidence(
    		                    element.getArrestation().getArrestationId().getIdEnfant(),
    		                    element.getArrestation().getArrestationId().getNumOrdinale()
    		                );
                           System.err.println("residence pb ");
                           System.err.println(residence.toString());
    		                if (residence != null &&
    		                    residence.getStatut() != 2 &&
    		                    residence
    		                    .getEtablissement()
    		                    .getId()
    		                    != changementLieu.
    		                    getEtablissementMutation()
    		                    .getId())  
    		                    dto.setChangementLieuMu(true);
    		                 
    		            }
    		        }

    		        if ("AP".equals(document2.getTypeDocument())) {
    		            dto.setDateAppelParquet(dateFormat.format(element.getDateEmissionDocument()));
    		            dto.setAppelParquet(true);
    		            dto.setDateJuge(true);
    		        }

    		        if ("AE".equals(document2.getTypeDocument())) {
    		            dto.setDateAppelEnfant(dateFormat.format(element.getDateEmissionDocument()));
    		            dto.setAppelEnfant(true);
    		            dto.setDateJuge(true);
    		        }
    		    }
    }
    
//    public static void traiterChangementLieuNewVersion(AffaireDto element, CalculeAffaireDto dto, List<DocumentDto> documents,ResidenceRepository residenceRepository) {
////   	 List<Document> documents = documentRepository.getDocumentByAffaire(
////   		        element.getArrestation().getArrestationId().getIdEnfant(),
////   		        element.getArrestation().getArrestationId().getNumOrdinale(),
////   		        element.getNumOrdinalAffaire()
////   		    );
//
//   		    if (!documents.isEmpty()) {
//   		        Document document1 = DocumentConverter.dtoToEntity(documents.get(0));
//   		        Document document2 = DocumentConverter.dtoToEntity(documents.get(1));
//
//   		        if (document1 instanceof ChangementLieu) {
//   		            ChangementLieu changementLieu = (ChangementLieu) document1;
//
//   		            if ("changementEtab".equals(changementLieu.getType()) &&
//   		                element.getArrestation() != null &&
//   		                element.getArrestation().getLiberation() == null) {
//   		                dto.setChangementLieuCh(true);
//   		            } else if ("mutation".equals(changementLieu.getType())) {
//   		                Residence residence = residenceRepository.findMaxResidence(
//   		                    element.getArrestation().getArrestationId().getIdEnfant(),
//   		                    element.getArrestation().getArrestationId().getNumOrdinale()
//   		                );
//                          System.err.println("residence pb ");
//                          System.err.println(residence.toString());
//   		                if (residence != null &&
//   		                    residence.getStatut() != 2 &&
//   		                    residence
//   		                    .getEtablissement()
//   		                    .getId()
//   		                    != changementLieu.
//   		                    getEtablissementMutation()
//   		                    .getId()) {
//   		                    dto.setChangementLieuMu(true);
//   		                }
//   		            }
//   		        }
//
//   		        if ("AP".equals(document2.getTypeDocument())) {
//   		            dto.setDateAppelParquet(dateFormat.format(element.getDateEmissionDocument()));
//   		            dto.setAppelParquet(true);
//   		            dto.setDateJuge(true);
//   		        }
//
//   		        if ("AE".equals(document2.getTypeDocument())) {
//   		            dto.setDateAppelEnfant(dateFormat.format(element.getDateEmissionDocument()));
//   		            dto.setAppelEnfant(true);
//   		            dto.setDateJuge(true);
//   		        }
//   		    }
//   }

    public static void calculerArret(AffaireDto element, FicheDeDetentionDto dto) {
    	dto.setJourArret(dto.getJourArret() + element.getJourArret());
        dto.setMoisArret(dto.getMoisArret() + element.getMoisArret());
        dto.setMoisArret(dto.getMoisArret() + (int) Math.floor(dto.getJourArret() / 30) * 1);
        dto.setJourArret(dto.getJourArret() - (int) Math.floor((dto.getJourArret() % 365) / 30) * 30);
        dto.setJourArret(dto.getJourArret() - (int) Math.floor(dto.getJourArret() / 365) * 365);
        dto.setAnneeArret(dto.getAnneeArret() + (int) Math.floor(element.getAnneeArret()));
        dto.setAnneeArret(dto.getAnneeArret() + (int) Math.floor(dto.getMoisArret() / 12));
        dto.setMoisArret(dto.getMoisArret() - (int) Math.floor(dto.getMoisArret() / 12) * 12);
    }

    public static void calculerPenal(AffaireDto element, FicheDeDetentionDto dto) {
    	dto.setJourPenal(dto.getJourPenal() + element.getJour());
        dto.setMoisPenal(dto.getMoisPenal() + element.getMois());
        dto.setMoisPenal(dto.getMoisPenal() + (int) Math.floor(dto.getJourPenal() / 30) * 1);
        dto.setJourPenal(dto.getJourPenal() - (int) Math.floor((dto.getJourPenal() % 365) / 30) * 30);
        dto.setJourPenal(dto.getJourPenal() - (int) Math.floor(dto.getJourPenal() / 365) * 365);
        dto.setAnneePenal(dto.getAnneePenal() + (int) Math.floor(element.getAnnee()));
        dto.setAnneePenal(dto.getAnneePenal() + (int) Math.floor(dto.getMoisPenal() / 12));
        dto.setMoisPenal(dto.getMoisPenal() - (int) Math.floor(dto.getMoisPenal() / 12) * 12);
    }
    
    
    public static ArrestationDto processArrestationToGetAffairPrincipal(ArrestationDto arrestation,AffaireRepository affaireRepository ) {
       
    	System.err.println(" *** depart *** ");
    	
    	System.out.println("arrestation.getArrestationId().getIdEnfant() "+arrestation.getArrestationId().getIdEnfant());
    	System.out.println("arrestation.getArrestationId().getNumOrdinale() "+arrestation.getArrestationId().getNumOrdinale());
    	
    	List<Affaire> affaireParPriorite = affaireRepository
                .findAffairePrincipale(arrestation.getArrestationId().getIdEnfant(), arrestation.getArrestationId().getNumOrdinale());

   // 	System.out.println("affaireParPriorite "+affaireParPriorite.get(0));
    	System.err.println(" *** fin *** ");
    	
    	
        if (!affaireParPriorite.isEmpty()) {

            boolean isStopped = affaireParPriorite.stream()
                    .anyMatch(x -> {
                        String typeDocument = x.getTypeDocument();
                        try {
                            return typeDocument != null && (typeDocument.equals("AP") || typeDocument.equals("CD")
                                    || typeDocument.equals("CH") || typeDocument.equals("CJA")
                                    || typeDocument.equals("T") || typeDocument.equals("AE")
                                    || typeDocument.equals("CP"));
                        } catch (NullPointerException e) {
                            throw new RuntimeException("typeDocument is null " + x.getArrestation().getEnfant().getId());
                        }
                    });

            
            AffaireDto affairePrincipale = trouverAffairePrincipale(affaireParPriorite.stream().map(AffaireConverter::entityToDto).collect(Collectors.toList())); // Appeler la méthode pour trouver l'affaire principale
         
           
            
            
//            arrestation.setEtatJuridique(isStopped ? "arret" : "juge");
//            arrestation.setNumAffairePricipale(
//                    affairePrincipale != null ? affairePrincipale.getAffaireId().getNumAffaire() : null);
//            arrestation.setTribunalPricipale(affairePrincipale != null ? affairePrincipale.getTribunal() : null);
//            arrestation.setNumOrdinalAffairePricipale(
//                    affairePrincipale != null ? affairePrincipale.getNumOrdinalAffaire() : null);
//            arrestation.setTypeAffairePricipale(affairePrincipale != null ? affairePrincipale.getTypeAffaire() : null);

            boolean doitEtreLibre = affaireParPriorite.stream()
                    .allMatch(x -> x.getTypeDocument().equals("AEX") || x.getTypeDocument().equals("L"));

//            if (arrestation.getLiberation() != null) {
//                arrestation.setEtatJuridique("libre");
//            } else if (doitEtreLibre && arrestation.getLiberation() == null) {
//                arrestation.setEtatJuridique("isAEX");
//            }

        } else {
//            arrestation.setEtatJuridique("vide");
        }

        return arrestation;
    }
    
    public static String determineEtatJuridique(ArrestationDto arrestation, List<AffaireDto> affaires) {
	    if (affaires.isEmpty()) {
	        return "vide";
	    }

	    List<String> typesDocumentsCibles = Arrays.asList("CD", "CH", "T", "CJA", "AP", "CP", "AE","OPP");
	    
	    if (arrestation.getLiberation() == null) {
	        List<AffaireDto> affaireArret = affaires.stream()
	                .filter(a -> typesDocumentsCibles.contains(a.getTypeDocument()))
	                .collect(Collectors.toList());

	        if (affaireArret.isEmpty()) {
	            boolean toutLibre = affaires.stream()
	                    .allMatch(x -> x.getTypeDocument().equals("AEX") || x.getTypeDocument().equals("L"));

	            if (toutLibre) {
	                return "pasInsertinLiberable";
	            }
	            return "juge";
	        } else {
	            return "arret";
	        }
	    } else {
	        return "libre";
	    }
	}
    
    public static void updateAffairePrincipale(List<AffaireDto> lesAffaires) {
	    boolean isAffairePrincipaleMiseAJour = false;

	    for (AffaireDto affaire : lesAffaires) {
	        if (affaire.getTypeDocument() != null && (affaire.getTypeDocument().equals("AP")
	                || affaire.getTypeDocument().equals("CD")
	                || affaire.getTypeDocument().equals("CH")
	                || affaire.getTypeDocument().equals("CJA")
	                || affaire.getTypeDocument().equals("T")
	                || affaire.getTypeDocument().equals("AE")
	                || affaire.getTypeDocument().equals("CP"))) {
	            affaire.setAffairePrincipale(true);
	            isAffairePrincipaleMiseAJour = true;
	            break; // Sortir de la boucle après la première mise à jour
	        }
	    }

	    if (!isAffairePrincipaleMiseAJour) {
	        for (AffaireDto affaire : lesAffaires) {
	            if (affaire.getTypeDocument() != null && affaire.getTypeDocument().equals("CJ")
	                    && affaire.getAffaireAffecter() == null && (affaire.getDaysDiffJuge() > 0)) {
	                affaire.setAffairePrincipale(true);
	                isAffairePrincipaleMiseAJour = true;
	                break; // Sortir de la boucle après la mise à jour
	            }
	        }
	    }

	    if (!isAffairePrincipaleMiseAJour) {
	        for (AffaireDto affaire : lesAffaires) {
	            if (affaire.getAffaireAffecter() == null) {
	                affaire.setAffairePrincipale(true);
	                break; // Sortir de la boucle après la mise à jour
	            }
	        }
	    }
	  
	}
    
    public static ArrestationDto processArrestationToGetAffairPrincipal(ArrestationDto arrestation,List<AffaireDto> affaireParPriorite ) {
        
    	 
    	
    	
    	   AffaireDto affairePrincipale = affaireParPriorite.stream()
                   .filter(x -> {
                       String typeDocument = x.getTypeDocument();
                       try {
                           return typeDocument != null && (typeDocument.equals("AP") || typeDocument.equals("CD")
                                   || typeDocument.equals("CH") || typeDocument.equals("CJA")
                                   || typeDocument.equals("T") || typeDocument.equals("AE")
                                   || typeDocument.equals("CP")|| typeDocument.equals("OPP"));
                       } catch (NullPointerException e) {
                           throw new RuntimeException("typeDocument is null " + x.getArrestation().getEnfant().getId());
                       }
                   })
                   .findFirst()
                   .orElseGet(() -> affaireParPriorite.stream()
                           .filter(x -> {
                               String typeDocument = x.getTypeDocument();
                               try {
                                   return (typeDocument.equals("CJ") && (x.getAffaireAffecter() == null)
                                           && (x.getDaysDiffJuge() > 0));
                               } catch (NullPointerException e) {
                                   throw new RuntimeException("typeDocument is null " + x.getArrestation().getEnfant().getId());
                               }
                           })
                           .findFirst()
                           .orElse(affaireParPriorite.stream()
                                   .filter(x -> (x.getAffaireAffecter() == null))
                                   .findFirst()
                                   .orElse(null)));

           
//            arrestation.setNumAffairePricipale(
//                    affairePrincipale != null ? affairePrincipale.getAffaireId().getNumAffaire() : null);
//            arrestation.setTribunalPricipale(affairePrincipale != null ? affairePrincipale.getTribunal() : null);
//            arrestation.setNumOrdinalAffairePricipale(
//                    affairePrincipale != null ? affairePrincipale.getNumOrdinalAffaire() : null);
//            arrestation.setTypeAffairePricipale(affairePrincipale != null ? affairePrincipale.getTypeAffaire() : null);
 

        

        return arrestation;
    }
 
    public static AffaireDto trouverAffairePrincipale(List<AffaireDto> affaireParPriorite) {
        AffaireDto affairePrincipale = affaireParPriorite.stream()
                .filter(x -> {
                    String typeDocument = x.getTypeDocument();
                    try {
                        return typeDocument != null && (typeDocument.equals("AP") || typeDocument.equals("CD")
                                || typeDocument.equals("CH") || typeDocument.equals("CJA")
                                || typeDocument.equals("T") || typeDocument.equals("AE")
                                || typeDocument.equals("CP")|| typeDocument.equals("OPP"));
                    } catch (NullPointerException e) {
                        throw new RuntimeException("typeDocument is null " + x.getArrestation().getEnfant().getId());
                    }
                })
                .findFirst()
                .orElseGet(() -> affaireParPriorite.stream()
                        .filter(x -> {
                            String typeDocument = x.getTypeDocument();
                            try {
                                return (typeDocument.equals("CJ") && (x.getAffaireAffecter() == null)
                                        && (x.getDaysDiffJuge() > 0));
                            } catch (NullPointerException e) {
                                throw new RuntimeException("typeDocument is null " + x.getArrestation().getEnfant().getId());
                            }
                        })
                        .findFirst()
                        .orElse(affaireParPriorite.stream()
                                .filter(x -> (x.getAffaireAffecter() == null))
                                .findFirst()
                                .orElse(null)));
       
        return affairePrincipale;
    }
}

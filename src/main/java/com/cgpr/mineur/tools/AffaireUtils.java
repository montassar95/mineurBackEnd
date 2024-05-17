package com.cgpr.mineur.tools;

import java.text.SimpleDateFormat;
import java.util.List;

import com.cgpr.mineur.dto.CalculeAffaireDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Arrestation;
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
    public static void traiterChangementLieu(Affaire element, CalculeAffaireDto dto, DocumentRepository documentRepository,ResidenceRepository residenceRepository) {
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

    		                if (residence != null &&
    		                    residence.getStatut() != 2 &&
    		                    residence.getEtablissement().getId() != changementLieu.getEtablissementtMutation().getId()) {
    		                    dto.setChangementLieuMu(true);
    		                }
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

    public static void calculerArret(Affaire element, CalculeAffaireDto dto) {
    	dto.setJourArret(dto.getJourArret() + element.getJourArret());
        dto.setMoisArret(dto.getMoisArret() + element.getMoisArret());
        dto.setMoisArret(dto.getMoisArret() + (int) Math.floor(dto.getJourArret() / 30) * 1);
        dto.setJourArret(dto.getJourArret() - (int) Math.floor((dto.getJourArret() % 365) / 30) * 30);
        dto.setJourArret(dto.getJourArret() - (int) Math.floor(dto.getJourArret() / 365) * 365);
        dto.setAnneeArret(dto.getAnneeArret() + (int) Math.floor(element.getAnneeArret()));
        dto.setAnneeArret(dto.getAnneeArret() + (int) Math.floor(dto.getMoisArret() / 12));
        dto.setMoisArret(dto.getMoisArret() - (int) Math.floor(dto.getMoisArret() / 12) * 12);
    }

    public static void calculerPenal(Affaire element, CalculeAffaireDto dto) {
    	dto.setJourPenal(dto.getJourPenal() + element.getJour());
        dto.setMoisPenal(dto.getMoisPenal() + element.getMois());
        dto.setMoisPenal(dto.getMoisPenal() + (int) Math.floor(dto.getJourPenal() / 30) * 1);
        dto.setJourPenal(dto.getJourPenal() - (int) Math.floor((dto.getJourPenal() % 365) / 30) * 30);
        dto.setJourPenal(dto.getJourPenal() - (int) Math.floor(dto.getJourPenal() / 365) * 365);
        dto.setAnneePenal(dto.getAnneePenal() + (int) Math.floor(element.getAnnee()));
        dto.setAnneePenal(dto.getAnneePenal() + (int) Math.floor(dto.getMoisPenal() / 12));
        dto.setMoisPenal(dto.getMoisPenal() - (int) Math.floor(dto.getMoisPenal() / 12) * 12);
    }
    
    
    public static Arrestation processArrestationToGetAffairPrincipal(Arrestation arrestation,AffaireRepository affaireRepository ) {
        List<Affaire> affaireParPriorite = affaireRepository
                .findAffairePrincipale(arrestation.getArrestationId().getIdEnfant(), arrestation.getArrestationId().getNumOrdinale());

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

            
            Affaire affairePrincipale = trouverAffairePrincipale(affaireParPriorite); // Appeler la méthode pour trouver l'affaire principale
         
           
            
            
            arrestation.setEtatJuridique(isStopped ? "arret" : "juge");
            arrestation.setNumAffairePricipale(
                    affairePrincipale != null ? affairePrincipale.getAffaireId().getNumAffaire() : null);
            arrestation.setTribunalPricipale(affairePrincipale != null ? affairePrincipale.getTribunal() : null);
            arrestation.setNumOrdinalAffairePricipale(
                    affairePrincipale != null ? affairePrincipale.getNumOrdinalAffaire() : null);
            arrestation.setTypeAffairePricipale(affairePrincipale != null ? affairePrincipale.getTypeAffaire() : null);

            boolean doitEtreLibre = affaireParPriorite.stream()
                    .allMatch(x -> x.getTypeDocument().equals("AEX") || x.getTypeDocument().equals("L"));

            if (arrestation.getLiberation() != null) {
                arrestation.setEtatJuridique("libre");
            } else if (doitEtreLibre && arrestation.getLiberation() == null) {
                arrestation.setEtatJuridique("isAEX");
            }

        } else {
            arrestation.setEtatJuridique("vide");
        }

        return arrestation;
    }
    
//    public static Arrestation processArrestationToGetAffairPrincipal(Arrestation arrestation,AffaireRepository affaireRepository ) {
//        List<Affaire> affaireParPriorite = affaireRepository
//                .findAffairePrincipale(arrestation.getArrestationId().getIdEnfant(), arrestation.getArrestationId().getNumOrdinale());
//
//        if (!affaireParPriorite.isEmpty()) {
//
//            boolean isStopped = affaireParPriorite.stream()
//                    .anyMatch(x -> {
//                        String typeDocument = x.getTypeDocument();
//                        try {
//                            return typeDocument != null && (typeDocument.equals("AP") || typeDocument.equals("CD")
//                                    || typeDocument.equals("CH") || typeDocument.equals("CJA")
//                                    || typeDocument.equals("T") || typeDocument.equals("AE")
//                                    || typeDocument.equals("CP"));
//                        } catch (NullPointerException e) {
//                            throw new RuntimeException("typeDocument is null " + x.getArrestation().getEnfant().getId());
//                        }
//                    });
//
//            
//          //je veux separer ce traitement  une autre methode qui prend en parameter un list des affaire et comme retour l'affaire princimpale  debut
//            Affaire affairePrincipale = affaireParPriorite.stream()
//                    .filter(x -> {
//                        String typeDocument = x.getTypeDocument();
//                        try {
//                            return typeDocument != null && (typeDocument.equals("AP") || typeDocument.equals("CD")
//                                    || typeDocument.equals("CH") || typeDocument.equals("CJA")
//                                    || typeDocument.equals("T") || typeDocument.equals("AE")
//                                    || typeDocument.equals("CP"));
//                        } catch (NullPointerException e) {
//                            throw new RuntimeException("typeDocument is null " + x.getArrestation().getEnfant().getId());
//                        }
//                    })
//                    .findFirst()
//                    .orElseGet(() -> affaireParPriorite.stream()
//                            .filter(x -> {
//                                String typeDocument = x.getTypeDocument();
//                                try {
//                                    return (typeDocument.equals("CJ") && (x.getAffaireAffecter() == null)
//                                            && (x.getDaysDiffJuge() > 0));
//                                } catch (NullPointerException e) {
//                                    throw new RuntimeException("typeDocument is null " + x.getArrestation().getEnfant().getId());
//                                }
//                            })
//                            .findFirst()
//                            .orElse(affaireParPriorite.stream()
//                                    .filter(x -> (x.getAffaireAffecter() == null))
//                                    .findFirst()
//                                    .orElse(null)));
//
//// fin
//            arrestation.setEtatJuridique(isStopped ? "arret" : "juge");
//            arrestation.setNumAffairePricipale(
//                    affairePrincipale != null ? affairePrincipale.getAffaireId().getNumAffaire() : null);
//            arrestation.setTribunalPricipale(affairePrincipale != null ? affairePrincipale.getTribunal() : null);
//            arrestation.setNumOrdinalAffairePricipale(
//                    affairePrincipale != null ? affairePrincipale.getNumOrdinalAffaire() : null);
//            arrestation.setTypeAffairePricipale(affairePrincipale != null ? affairePrincipale.getTypeAffaire() : null);
//
//            boolean doitEtreLibre = affaireParPriorite.stream()
//                    .allMatch(x -> x.getTypeDocument().equals("AEX") || x.getTypeDocument().equals("L"));
//
//            if (arrestation.getLiberation() != null) {
//                arrestation.setEtatJuridique("libre");
//            } else if (doitEtreLibre && arrestation.getLiberation() == null) {
//                arrestation.setEtatJuridique("isAEX");
//            }
//
//        } else {
//            arrestation.setEtatJuridique("vide");
//        }
//
//        return arrestation;
//    }
    
    
    public static Affaire trouverAffairePrincipale(List<Affaire> affaireParPriorite) {
        Affaire affairePrincipale = affaireParPriorite.stream()
                .filter(x -> {
                    String typeDocument = x.getTypeDocument();
                    try {
                        return typeDocument != null && (typeDocument.equals("AP") || typeDocument.equals("CD")
                                || typeDocument.equals("CH") || typeDocument.equals("CJA")
                                || typeDocument.equals("T") || typeDocument.equals("AE")
                                || typeDocument.equals("CP"));
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

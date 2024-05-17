package com.cgpr.mineur.serviceReporting;
//	public Residence processResidenceDTOList(Residence residence, PDFListExistDTO pDFListExistDTO) {
//
//		if (residence.getArrestation().getEnfant() != null) {
//
//			List<Affaire> lesAffaires = new ArrayList<Affaire>();
//
//			if (pDFListExistDTO.getCheckUniqueAff() == null) {
//				lesAffaires = affaireRepository.findByArrestationCoroissant(
//						residence.getArrestation().getArrestationId().getIdEnfant(),
//						residence.getArrestation().getArrestationId().getNumOrdinale());
//
//			}
//
//			// -----------------------------------------------------
//
//			else {
//
//				lesAffaires = affaireRepository.findAffairePrincipale(
//
//						residence.getArrestation().getArrestationId().getIdEnfant(),
//						residence.getArrestation().getArrestationId().getNumOrdinale());
//
//				boolean allSameNamex = lesAffaires.stream()
//						.allMatch(x -> x.getTypeDocument().toString().equals("AEX".toString()));
//
//				if (allSameNamex && residence.getArrestation().getLiberation() == null) {
//					residence.getArrestation().setEtatJuridique("isAEX");
//				}
//
//				Affaire a = lesAffaires.stream()
//
//						.filter(x -> x.getTypeDocument().equals("AP") || x.getTypeDocument().equals("CD")
//								|| x.getTypeDocument().equals("CH") || x.getTypeDocument().equals("CJA")
//								|| x.getTypeDocument().equals("T") || x.getTypeDocument().equals("AE")
//								|| x.getTypeDocument().equals("CP")
//
//						)
//
//						.findFirst().orElse(null);
//
//				if (!(lesAffaires.isEmpty())) {
//					if (a != null) {
//						lesAffaires = new ArrayList<Affaire>();
//						lesAffaires.add(a);
//
//					} else {
//						a = lesAffaires.stream()
//
//								.filter(x -> (x.getTypeDocument().equals("CJ") && (x.getAffaireAffecter() == null)
//										&& (x.getDaysDiffJuge() > 0)))
//								.findFirst()
//
//								.orElse(lesAffaires.stream()
//
//										.filter(x -> ((x.getAffaireAffecter() == null))).findFirst().orElse(null));
//
//						lesAffaires = new ArrayList<Affaire>();
//						lesAffaires.add(a);
//
//					}
//				}
//
//			}
//
////			    	  --------------------------------------------------------------       
//
//			lesAffaires = lesAffaires.stream().map(s -> {
//
//				com.cgpr.mineur.models.Document doc = documentRepository.getLastDocumentByAffaire(
//						s.getArrestation().getArrestationId().getIdEnfant(),
//						s.getArrestation().getArrestationId().getNumOrdinale(), s.getNumOrdinalAffaire());
//
//				s.setTypeDocument(doc.getTypeDocument());
//				s.setDateEmissionDocument(doc.getDateEmission());
//				if (doc instanceof Transfert) {
//					Transfert t = (Transfert) doc;
//
//					s.setTypeFile(t.getTypeFile());
//
//				}
//				if (doc instanceof Arreterlexecution) {
//					Arreterlexecution t = (Arreterlexecution) doc;
//
//					s.setTypeFile(t.getTypeFile());
//
//				}
//				List<com.cgpr.mineur.models.Document> accData = documentRepository.getDocumentByAffaireforAccusation(
//						s.getArrestation().getArrestationId().getIdEnfant(),
//						s.getArrestation().getArrestationId().getNumOrdinale(), s.getNumOrdinalAffaire(),
//						PageRequest.of(0, 1));
//
//				List<TitreAccusation> titreAccusations = null;
//
//				if (accData.get(0) instanceof CarteRecup) {
//
//					titreAccusations = accusationCarteRecupRepository
//							.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
//
//					s.setTitreAccusations(titreAccusations);
//					s.setDateEmission(accData.get(0).getDateEmission());
//
//					CarteRecup c = (CarteRecup) accData.get(0);
//					s.setAnnee(c.getAnnee());
//					s.setMois(c.getMois());
//					s.setJour(c.getJour());
//
//					s.setAnneeArret(c.getAnneeArretProvisoire());
//					s.setMoisArret(c.getMoisArretProvisoire());
//					s.setJourArret(c.getJourArretProvisoire());
//
//					s.setTypeJuge(c.getTypeJuge());
//
//				} else if (accData.get(0) instanceof CarteDepot) {
//					titreAccusations = accusationCarteDepotRepository
//							.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
//					s.setTitreAccusations(titreAccusations);
//					s.setDateEmission(accData.get(0).getDateEmission());
//				} else if (accData.get(0) instanceof CarteHeber) {
//					titreAccusations = accusationCarteHeberRepository
//							.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
//					s.setTitreAccusations(titreAccusations);
//					s.setDateEmission(accData.get(0).getDateEmission());
//
//				}
//				 
//				return s;
//			}).collect(Collectors.toList());
//
// 		residence.getArrestation().setAffaires(lesAffaires);
// 		 
//			 
//		}
//
//		return residence;
//	}
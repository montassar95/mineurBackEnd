package com.cgpr.mineur.tools;

public   interface  CommonConditions {

	
	   
	   String buildChildrenCriteria =  
			    "(:classePenale IS NULL OR a.arrestation.enfant.classePenale = :classePenale) AND "
	           + "(:niveauEducatif IS NULL OR a.arrestation.enfant.niveauEducatif = :niveauEducatif) AND "
	           + "(:gouvernorat IS NULL OR a.arrestation.enfant.gouvernorat = :gouvernorat) AND "
	           + "(:situationFamiliale IS NULL OR a.arrestation.enfant.situationFamiliale = :situationFamiliale) AND "
	           + "(:situationSocial IS NULL OR a.arrestation.enfant.situationSocial = :situationSocial) AND "
	           + "(:metier IS NULL OR a.arrestation.enfant.metier = :metier) AND "
	           + "(:delegation IS NULL OR a.arrestation.enfant.delegation = :delegation) AND "
	           + "(:foreign = true OR a.arrestation.enfant.nationalite.id != 1) AND "
	           + "(:nationalite IS NULL OR a.arrestation.enfant.nationalite = :nationalite) AND "
	           + "((:minAge = 0.0F OR :maxAge = 0.0F) OR (TRUNC(months_between(CURRENT_DATE,a.arrestation.enfant.dateNaissance)/12) BETWEEN :minAge AND :maxAge))  ";
	         
	   
	   
	   String buildDetentionCriteria =   "("
          + "    SELECT aff.arrestation FROM Affaire aff WHERE aff.statut = 0 AND "
          + "    (:gouvernoratTribunal IS NULL OR aff.tribunal.gouvernorat = :gouvernoratTribunal) AND "
          + "    (:typeTribunal IS NULL OR aff.tribunal.typeTribunal = :typeTribunal) AND "
          + "    (:typeAffaire IS NULL OR aff.typeAffaire = :typeAffaire)"
          + ") ";

//	     final String buildChildrenCriteria =   buildChildrenCriteria();
 
//	  static   String buildChildrenCriteria() {
//	    StringBuilder conditionsBuilder = new StringBuilder();
//	    
//	    conditionsBuilder.append("(:classePenale IS NULL OR a.arrestation.enfant.classePenale = :classePenale) AND\n")
//	                     .append("(:niveauEducatif IS NULL OR a.arrestation.enfant.niveauEducatif = :niveauEducatif) AND\n")
//	                     .append("(:gouvernorat IS NULL OR a.arrestation.enfant.gouvernorat = :gouvernorat) AND\n")
//	                     .append("(:situationFamiliale IS NULL OR a.arrestation.enfant.situationFamiliale = :situationFamiliale) AND\n")
//	                     .append("(:situationSocial IS NULL OR a.arrestation.enfant.situationSocial = :situationSocial) AND\n")
//	                     .append("(:metier IS NULL OR a.arrestation.enfant.metier = :metier) AND\n")
//	                     .append("(:delegation IS NULL OR a.arrestation.enfant.delegation = :delegation) AND\n")
//	                     .append("(:foreign = true OR a.arrestation.enfant.nationalite.id != 1) AND\n")
//	                     .append("(:nationalite IS NULL OR a.arrestation.enfant.nationalite = :nationalite) AND\n")
//	                     .append("((:minAge = 0.0F OR :maxAge = 0.0F) OR (TRUNC(months_between(CURRENT_DATE, a.arrestation.enfant.dateNaissance) / 12) BETWEEN :minAge AND :maxAge)) AND\n");
//
//	    return conditionsBuilder.toString();
	//}

}

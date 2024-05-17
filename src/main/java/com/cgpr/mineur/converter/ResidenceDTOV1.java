package com.cgpr.mineur.converter;
//package com.cgpr.mineur.dto;
//
//import java.util.Date;
//import java.util.List;
//
//import com.cgpr.mineur.models.Affaire;
//import com.cgpr.mineur.models.Arrestation;
//import com.cgpr.mineur.models.CauseMutation;
//import com.cgpr.mineur.models.EtabChangeManiere;
//import com.cgpr.mineur.models.Etablissement;
//import com.cgpr.mineur.models.Residence;
//import com.cgpr.mineur.models.ResidenceId;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//public class ResidenceDTOV1 {
//	  	
//		private ResidenceId residenceId;
//
//		private String numArrestation;
//
//		private Date dateEntree;
//		private Date dateSortie;
//
//		
//		private Etablissement etablissement;
//		
//		
//		private Etablissement etablissementEntree;
//		
//	
//		private Etablissement etablissementSortie;
//
//		
//		private Arrestation arrestation;
//		
//		private int statut;
//		
//		private CauseMutation causeMutation;
//		
//		
//		
//		private CauseMutation causeMutationSortie;
//		
//		private String remarqueMutation;
//		private int nombreEchappes;
//		
//		 
//		private Date dateFin;
//		
//		
//		
//		 
//		private EtabChangeManiere etabChangeManiere;
//		private List<Affaire> lesAffaires;
//
//		 public static ResidenceDTOV1 fromResidence(ResidenceDTOV1 residence) {
//			 ResidenceDTOV1 residenceDTO = new ResidenceDTOV1();
//		        residenceDTO.setResidenceId(residence.getResidenceId());
//		        residenceDTO.setNumArrestation(residence.getNumArrestation());
//		        residenceDTO.setDateEntree(residence.getDateEntree());
//		        residenceDTO.setDateSortie(residence.getDateSortie());
//		        residenceDTO.setEtablissement(residence.getEtablissement());
//		        residenceDTO.setEtablissementEntree(residence.getEtablissementEntree());
//		        residenceDTO.setEtablissementSortie(residence.getEtablissementSortie());
//		        residenceDTO.setArrestation(residence.getArrestation());
//		        residenceDTO.setStatut(residence.getStatut());
//		        residenceDTO.setCauseMutation(residence.getCauseMutation());
//		        residenceDTO.setCauseMutationSortie(residence.getCauseMutationSortie());
//		        residenceDTO.setRemarqueMutation(residence.getRemarqueMutation());
//		        residenceDTO.setNombreEchappes(residence.getNombreEchappes());
//		        residenceDTO.setDateFin(residence.getDateFin());
//		        residenceDTO.setEtabChangeManiere(residence.getEtabChangeManiere());
//		        return residenceDTO;
//		    }
//		 
//
//}

package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.ActeJudiciaire;
import com.cgpr.mineur.dto.AffairePenaleDto;
import com.cgpr.mineur.dto.ArretExecutionPenalDTO;
import com.cgpr.mineur.dto.EvasionCaptureDTO;
import com.cgpr.mineur.dto.MutationResidenceDTO;
import com.cgpr.mineur.dto.ParticipantAffaireDTO;
import com.cgpr.mineur.dto.PenalAffaireDTO;
import com.cgpr.mineur.dto.PenalContestationDto;
import com.cgpr.mineur.dto.PenalContrainteDTO;
import com.cgpr.mineur.dto.PenalGraceDto;
import com.cgpr.mineur.dto.PenalJugementDTO;
import com.cgpr.mineur.dto.PenalMandatDepotDTO;
import com.cgpr.mineur.dto.PenalSyntheseDto;
import com.cgpr.mineur.dto.PenalTransfertDto;
import com.cgpr.mineur.dto.PenaleDetentionInfoDto;
import com.cgpr.mineur.dto.PrisonerPenaleDto;
import com.cgpr.mineur.dto.SearchDetenuDto;
import com.cgpr.mineur.resource.EnfantDTO;

public interface  PrisonerPenalService {

	public List<SearchDetenuDto>   findPrisonerPenalByCriteria ( EnfantDTO enfantDTO);
	public PrisonerPenaleDto findPrisonerPenalByPrisonerId(String prisonerId ,String tcoddet ) ;
	
	public SearchDetenuDto trouverDetenusParPrisonerIdDansPrisons(String prisonerId) ;
	
	public List<SearchDetenuDto> trouverDetenusParNumeroEcrouDansPrisons(String numArr);
	public List<AffairePenaleDto> findAffairesByNumideAndCoddet(String prisonerId,String numArr);
	
	 public PenalMandatDepotDTO getMandatDepot(  String tnumide, String tcoddet, String tnumseqaff , String tcodma) ;
	 public PenalTransfertDto getTransfert(String tnumide, String tcoddet, String tnumseqaff, String tcodtraf);
	 public  PenalJugementDTO  getAccusationsParDetenu(String numIde, String codDet, String codExtj) ;
	 public List<ActeJudiciaire> getActesJudiciaires(String tnumide, String tcoddet, String tnumseqaff);
	public PenalContestationDto getContestation(String tnumide, String tcoddet, String tnumseqaff, String tcodco, String codeDocumentSecondaire);
	public ArretExecutionPenalDTO getArretExecutionParTypeActe(String tnumide, String tcoddet, String tnumseqaff, String typeActe);
	
	
	public List<PenaleDetentionInfoDto> trouverToutDetentionInfosParPrisonerIdDansPrisons(String prisonerId ) ;
	
	 public List<PenalAffaireDTO> rechercherAffaires(String tnumide, String tcoddet, int minPage , int maxPage) ;
	 public PenalSyntheseDto rechercherPenalSyntheseDetenu(String tnumide, String tcoddet   ) ;
	 
	 
	 public PenalContrainteDTO getContrainte(String tnumide, String tcoddet, String tnumseqaff);
	 
	  public List<PenalGraceDto> getPenalGraces(String tnumide, String tcoddet);
	  
	  public List<MutationResidenceDTO> getMutationResidence(String numide, String coddet);
	  
	  public List<EvasionCaptureDTO> getEvasionsWithCaptures(String tnumide, String tcoddet); 
	  
	  public List<ParticipantAffaireDTO> findParticipantsAffaire(String tnumide, String tcoddet);
}

package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.AccusationExtraitJugementDTO;
import com.cgpr.mineur.dto.ActeJudiciaire;
import com.cgpr.mineur.dto.AffairePenaleDto;
import com.cgpr.mineur.dto.ArretExecutionPenalDTO;
import com.cgpr.mineur.dto.PenalContestationDto;
import com.cgpr.mineur.dto.PenalJugementDTO;
import com.cgpr.mineur.dto.PenalMandatDepotDTO;
import com.cgpr.mineur.dto.PenalTransfertDto;
import com.cgpr.mineur.dto.PenaleDetentionInfoDto;
import com.cgpr.mineur.dto.PrisonerPenaleDto;
import com.cgpr.mineur.dto.SearchDetenuDto;
import com.cgpr.mineur.resource.EnfantDTO;

public interface  PrisonerPenalService {

	public List<SearchDetenuDto>   findPrisonerPenalByCriteria ( EnfantDTO enfantDTO);
	public PrisonerPenaleDto findPrisonerPenalByPrisonerId(String prisonerId) ;
	
	public SearchDetenuDto trouverDetenusParPrisonerIdDansPrisons(String prisonerId) ;
	
	public List<SearchDetenuDto> trouverDetenusParNumeroEcrouDansPrisons(String numArr);
	public List<AffairePenaleDto> findAffairesByNumideAndCoddet(String prisonerId,String numArr);
	
	 public PenalMandatDepotDTO getMandatDepot(  String tnumide, String tcoddet, String tnumseqaff , String tcodma) ;
	 public PenalTransfertDto getTransfert(String tnumide, String tcoddet, String tnumseqaff, String tcodtraf);
	 public  PenalJugementDTO  getAccusationsParDetenu(String numIde, String codDet, String codExtj) ;
	 public List<ActeJudiciaire> getActesJudiciaires(String tnumide, String tcoddet, String tnumseqaff);
	public PenalContestationDto getContestation(String tnumide, String tcoddet, String tnumseqaff, String tcodco, String codeDocumentSecondaire);
	public ArretExecutionPenalDTO getArretExecutionParTypeActe(String tnumide, String tcoddet, String tnumseqaff, String typeActe);
	
	
	public List<PenaleDetentionInfoDto> trouverToutDetentionInfosParPrisonerIdDansPrisons(String prisonerId) ;
}

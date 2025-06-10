package com.cgpr.mineur.service.Impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.config.Simplification;
import com.cgpr.mineur.dto.AccusationExtraitJugementDTO;
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
//import com.cgpr.mineur.repository.PersonelleRepository;
import com.cgpr.mineur.repository.PrisonerPenalRepository;
import com.cgpr.mineur.repository.rapport.RechercherAffairesRepositoryCustom;
import com.cgpr.mineur.resource.EnfantDTO;
import com.cgpr.mineur.service.PrisonerPenalService;

@Service
public class PrisonerPenalServiceImpl implements PrisonerPenalService {
	
	@Autowired
	private PrisonerPenalRepository prisonerPenalRepository;
	@Autowired
	private RechercherAffairesRepositoryCustom rechercherAffairesRepositoryCustom;
	Simplification simplifier = new Simplification();
	
	 private String simplifyOrNull(String value) {
		    String simplified =  value != null ? value.trim() : "" ;
		    return simplified.isEmpty() ? null : simplified;
		}
	@Override
	public List<SearchDetenuDto> findPrisonerPenalByCriteria(EnfantDTO enfantDTO) {
	    if (enfantDTO == null) {
	        throw new IllegalArgumentException("Le paramètre enfantDTO ne peut pas être nul.");
	    }

	    // Simplification des valeurs d'entrée
	    String nomSimplifie = simplifier.simplify(enfantDTO.getNom());
	    String prenomSimplifie = simplifier.simplify(enfantDTO.getPrenom());
	    String nomPereSimplifie = simplifier.simplify(enfantDTO.getNomPere());
	    LocalDate dateNaissance = enfantDTO.getDateNaissance();

	    // Conversion du sexe en valeur utilisable (1 pour "masculin", 0 pour "féminin")
	    String sexe;
	    if ("ذكر".equals(enfantDTO.getSexe())) {
	        sexe = "1";
	    } else if ("أنثى".equals(enfantDTO.getSexe())) {
	        sexe = "0";
	    } else {
	        sexe = null; // Sexe inconnu ou non fourni
	    }

	      nomSimplifie = simplifyOrNull(nomSimplifie);
	      prenomSimplifie = simplifyOrNull(prenomSimplifie);
	      nomPereSimplifie = simplifyOrNull(nomPereSimplifie);
	    
	    
	    System.out.println("firstname : " + nomSimplifie);
	    System.out.println("father_name : " + nomPereSimplifie);
	    System.out.println("lastname : " + prenomSimplifie);
	    System.out.println("birth_date : " + dateNaissance);
	    System.out.println("sex : " + sexe);
	    
	    
	    
	    // Appel au repository
	    List<SearchDetenuDto> prisonerList = prisonerPenalRepository.findPrisonerPenalByCriteria(
	        nomSimplifie,
	        prenomSimplifie,
	        nomPereSimplifie,
	        dateNaissance,
	        sexe
	    );

	    // Retour de la liste des résultats
	    return prisonerList;
	}
	@Override
	public PrisonerPenaleDto findPrisonerPenalByPrisonerId(String prisonerId, String tcoddet) {
		// TODO Auto-generated method stub
		System.err.println("findPrisonerPenalByPrisonerId");
		PrisonerPenaleDto prisonerPenaleDto = prisonerPenalRepository.findPrisonerPenalByPrisonerId(prisonerId,   tcoddet);
		//PenalSyntheseDto penalSyntheseDto = rechercherAffairesRepositoryCustom.rechercherPenalSyntheseDetenu(prisonerId, tcoddet);
		//prisonerPenaleDto.setPenalSyntheseDto(penalSyntheseDto);
		System.err.println(prisonerPenaleDto.toString());
		return prisonerPenaleDto;
	}
	@Override
	public SearchDetenuDto trouverDetenusParPrisonerIdDansPrisons(String prisonerId) {
		// TODO Auto-generated method stub
		return  prisonerPenalRepository.trouverDetenusParPrisonerIdDansPrisons(prisonerId);
	}
	@Override
	public List<SearchDetenuDto> trouverDetenusParNumeroEcrouDansPrisons(String numArr) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.trouverDetenusParNumeroEcrouDansPrisons(numArr);
	}
	@Override
	public List<AffairePenaleDto> findAffairesByNumideAndCoddet(String prisonerId, String numArr) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.findAffairesByNumideAndCoddet(prisonerId, numArr);
	}
	@Override
	public PenalMandatDepotDTO getMandatDepot(String tnumide, String tcoddet, String tnumseqaff , String tcodma) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.getMandatDepot(tnumide, tcoddet, tnumseqaff ,   tcodma);
	}
	@Override
	public  PenalJugementDTO  getAccusationsParDetenu(String numIde, String codDet, String codExtj) {
		// TODO Auto-generated method stub
		PenalJugementDTO	penalJugementDTO =null;
		Optional<PenalJugementDTO> jugement = prisonerPenalRepository.getSinglePenalJugement(numIde, codDet, codExtj);
		if (jugement.isPresent()) {
			 	penalJugementDTO = jugement.get();
			List<AccusationExtraitJugementDTO> list = prisonerPenalRepository.getAccusationsParDetenu(numIde, codDet, codExtj);
			penalJugementDTO.setAccusationExtraitJugementDTOs(list);
		} 
		
		
		return penalJugementDTO;
	}
	@Override
	public PenalTransfertDto getTransfert(String tnumide, String tcoddet, String tnumseqaff, String tcodtraf) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.getTransfert(  tnumide,   tcoddet,   tnumseqaff,   tcodtraf);
	}
	@Override
	public List<ActeJudiciaire> getActesJudiciaires(String tnumide, String tcoddet, String tnumseqaff) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.getActesJudiciaires(tnumide, tcoddet, tnumseqaff);
	}
	@Override
	public PenalContestationDto getContestation(String tnumide, String tcoddet, String tnumseqaff, String tcodco , String codeDocumentSecondaire) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.getContestation(  tnumide,   tcoddet,   tnumseqaff,   tcodco , codeDocumentSecondaire);
	}
	@Override
	public ArretExecutionPenalDTO getArretExecutionParTypeActe(String tnumide, String tcoddet, String tnumseqaff,
			String typeActe) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.getArretExecutionParTypeActe(  tnumide,   tcoddet,   tnumseqaff, typeActe);
	}
	@Override
	public List<PenaleDetentionInfoDto> trouverToutDetentionInfosParPrisonerIdDansPrisons(String prisonerId  ) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.trouverToutDetentionInfosParPrisonerIdDansPrisons(prisonerId  );
	}

	@Override
    public List<PenalAffaireDTO> rechercherAffaires(String tnumide, String tcoddet, int minPage , int maxPage)   {
	  return rechercherAffairesRepositoryCustom.rechercherAffaires(tnumide , tcoddet , minPage, maxPage);
	}
	@Override
	public PenalSyntheseDto rechercherPenalSyntheseDetenu(String tnumide, String tcoddet) {
		// TODO Auto-generated method stub
		return rechercherAffairesRepositoryCustom.rechercherPenalSyntheseDetenu(tnumide, tcoddet);
	}
	@Override
	public PenalContrainteDTO getContrainte(String tnumide, String tcoddet, String tnumseqaff) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.getContrainte(tnumide, tcoddet, tnumseqaff);
	}
	@Override
	public List<PenalGraceDto> getPenalGraces(String tnumide, String tcoddet) {
		// TODO Auto-generated method stub
		return  prisonerPenalRepository.getPenalGraces(tnumide,   tcoddet);
	}
	@Override
	public List<MutationResidenceDTO> getMutationResidence(String numide, String coddet) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.getMutationResidence(numide, coddet);
	}
	@Override
	public List<EvasionCaptureDTO> getEvasionsWithCaptures(String tnumide, String tcoddet) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.getEvasionsWithCaptures(tnumide, tcoddet);
	}
	@Override
	public List<ParticipantAffaireDTO> findParticipantsAffaire(String tnumide, String tcoddet) {
		// TODO Auto-generated method stub
		return prisonerPenalRepository.findParticipantsAffaire(tnumide, tcoddet);
	}
}

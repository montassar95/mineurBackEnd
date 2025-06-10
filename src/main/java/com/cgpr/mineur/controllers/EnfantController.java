package com.cgpr.mineur.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.AccusationExtraitJugementDTO;
import com.cgpr.mineur.dto.ActeJudiciaire;
import com.cgpr.mineur.dto.AffairePenaleDto;
import com.cgpr.mineur.dto.ArretExecutionPenalDTO;
import com.cgpr.mineur.dto.EnfantDto;
import com.cgpr.mineur.dto.EnfantVerifieDto;
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
import com.cgpr.mineur.dto.ResidenceDto;
import com.cgpr.mineur.dto.SearchDetenuDto;
import com.cgpr.mineur.models.ApiResponse;
//import com.cgpr.mineur.repository.RapportEnfantQuotidienRepository;
import com.cgpr.mineur.resource.EnfantAddDTO;
import com.cgpr.mineur.resource.EnfantDTO;
import com.cgpr.mineur.service.EnfantService;
import com.cgpr.mineur.service.PrisonerPenalService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/enfant/")
public class EnfantController {
	@Autowired
	private EnfantService enfantService;

	
	@Autowired
	private PrisonerPenalService prisonerPenalService;

	
	
	
	
	
	
	
	@PostMapping("/trouverResidencesParCriteresDetenu")
	public ApiResponse<List<SearchDetenuDto>> trouverResidencesParCriteresDetenu(@RequestBody EnfantDTO enfantDTO) {
		 
		List<SearchDetenuDto> enfantData = enfantService.trouverResidencesParCriteresDetenu(enfantDTO);
	
		if (enfantData != null) {

			return new ApiResponse<>(HttpStatus.OK.value(), "enfantData fetched suucessfully", enfantData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "enfantData Not FOund", null);
		}
		
		
	}

	
	
	@PostMapping("/trouverDetenusParCriteresDansPrisons")
	public ApiResponse<List<SearchDetenuDto>> trouverDetenusParCriteresDansPrisons(@RequestBody EnfantDTO enfantDTO) {
		 
		List<SearchDetenuDto> enfantData = enfantService.trouverDetenusParCriteresDansPrisons(enfantDTO);
	
		if (enfantData != null) {

			return new ApiResponse<>(HttpStatus.OK.value(), "enfantData fetched suucessfully", enfantData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "enfantData Not FOund", null);
		}
	 
	}

	
	@GetMapping("/trouverDetenusParPrisonerIdDansPrisons/{id}")
	public ApiResponse<SearchDetenuDto> trouverDetenusParPrisonerIdDansPrisons(@PathVariable("id") String id) {
		SearchDetenuDto enfanttData = prisonerPenalService.trouverDetenusParPrisonerIdDansPrisons(id);
		 
		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfanttData);
	}
	
	
	
	
	@GetMapping("/trouverDetenusParNumeroEcrouDansPrisons/{numArr}")
	public ApiResponse<List<SearchDetenuDto>> trouverDetenusParNumeroEcrouDansPrisons(@PathVariable("numArr") String numArr) {
		List<SearchDetenuDto> enfantData = enfantService.trouverDetenusParNumeroEcrouDansPrisons(numArr);

		if (enfantData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfantData);
		}

		else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Enfant Not FOund", null);
		}
	}
	
	
	@GetMapping("/trouverDetenusParDetenuIdMineurDansPrisons/{detenuIdMineur}")
	public ApiResponse<List<SearchDetenuDto>> trouverDetenusParDetenuIdMineurDansPrisons(@PathVariable("detenuIdMineur") String detenuIdMineur) {
		List<SearchDetenuDto> enfantData = enfantService.trouverDetenusParDetenuIdMineurDansPrisons(detenuIdMineur);

		if (enfantData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfantData);
		}

		else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Enfant Not FOund", null);
		}
	}
	
	
	
	@GetMapping("/trouverDetenusParDetenuIdMajeurDansCentres/{detenuIdMajeur}")
	public ApiResponse<List<SearchDetenuDto>> trouverDetenusParDetenuIdMajeurDansCentres(@PathVariable("detenuIdMajeur") String detenuIdMajeur) {
		List<SearchDetenuDto> enfantData = enfantService.trouverDetenusParDetenuIdMajeurDansCentres(detenuIdMajeur);

		if (enfantData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfantData);
		}

		else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Enfant Not FOund", null);
		}
	}
	@GetMapping("/findPrisonerPenalByPrisonerId/{id}/{tcoddet}")
	public ApiResponse<PrisonerPenaleDto> findPrisonerPenalByPrisonerId(@PathVariable("id") String id , @PathVariable("tcoddet") String  tcoddet) {
		PrisonerPenaleDto prisonerPenaleDto = prisonerPenalService.findPrisonerPenalByPrisonerId(id,tcoddet);
		
		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", prisonerPenaleDto);
	}

	
	@GetMapping("/findAffairesByNumideAndCoddet/{prisonerId}/{numArr}")
	public ApiResponse<List<AffairePenaleDto>> findAffairesByNumideAndCoddet(@PathVariable("prisonerId") String prisonerId, @PathVariable("numArr") String numArr) {
		List<AffairePenaleDto> enfanttData = prisonerPenalService.findAffairesByNumideAndCoddet(  prisonerId,  numArr);
		
		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfanttData);
	}
	
	
 

	@GetMapping("/getone/{id}")
	public ApiResponse<EnfantDto> getEnfantById(@PathVariable("id") String id) {
		EnfantDto enfanttData = enfantService.getEnfantById(id);
		
		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfanttData);
	}

	@GetMapping("/trouverDetenuAvecSonStatutActuel/{id}/{idEtab}")
	public ApiResponse<EnfantVerifieDto> trouverDetenuAvecSonStatutActuel(@PathVariable("id") String id,
			@PathVariable("idEtab") String idEtab) {
		EnfantVerifieDto enfanttData = enfantService.trouverDetenuAvecSonStatutActuel(id, idEtab);

		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfanttData);
	}

	@GetMapping("/trouverDerniereResidenceParIdDetenu/{id}")
	public ApiResponse<SearchDetenuDto> trouverDerniereResidenceParIdDetenu(@PathVariable("id") String id) {

		SearchDetenuDto enfantData = enfantService.trouverDerniereResidenceParIdDetenu(id);
		

		
		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfantData);

	}

	@GetMapping("/trouverResidencesParNumeroEcrou/{numArr}")
	public ApiResponse<List<SearchDetenuDto>> trouverResidencesParNumeroEcrou(@PathVariable("numArr") String numArr) {
		List<SearchDetenuDto> enfantData = enfantService.trouverResidencesParNumeroEcrou(numArr);

		if (enfantData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfantData);
		}

		else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Enfant Not FOund", null);
		}
	}

	@PostMapping("/creerAdmissionDetenu")
	public ApiResponse<ResidenceDto> creerAdmissionDetenu(@RequestBody EnfantAddDTO enfantAddDTO) {
		try {

			ResidenceDto newResidence = enfantService.creerAdmissionDetenu(enfantAddDTO);

		 

			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant saved Successfully", newResidence);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Enfant not saved", null);
		}

	}

	@PostMapping("/mettreAJourAdmissionDetenu")
	public ApiResponse<ResidenceDto> mettreAJourAdmissionDetenu(@RequestBody EnfantAddDTO enfantAddDTO) {

		ResidenceDto newResidence = enfantService.mettreAJourAdmissionDetenu(enfantAddDTO);

		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant saved Successfully", newResidence);

	}

	
	@GetMapping("/getMandatDepot/{tnumide}/{tcoddet}/{tnumseqaff}/{tcodma}")
	public ApiResponse<PenalMandatDepotDTO> getMandatDepot( @PathVariable String tnumide,@PathVariable String tcoddet,@PathVariable String tnumseqaff , @PathVariable String tcodma) {
		System.out.println(tnumide + " "+tcoddet+ " "+tnumseqaff + " "+ tcodma + " yes 1 ");
		PenalMandatDepotDTO penalMandatDepotDTO = prisonerPenalService. getMandatDepot(tnumide, tcoddet, tnumseqaff , tcodma);
		
		return new ApiResponse<>(HttpStatus.OK.value(), "textMandatDepot Successfully", penalMandatDepotDTO);
		
		
	}
	
	@GetMapping("/getTransfert/{tnumide}/{tcoddet}/{tnumseqaff}/{tcodma}")
	public ApiResponse<PenalTransfertDto> getTransfert( @PathVariable String tnumide,@PathVariable String tcoddet,@PathVariable String tnumseqaff , @PathVariable String tcodma) {
		System.out.println(tnumide + " "+tcoddet+ " "+tnumseqaff + " "+ tcodma + " yes 3 ");
		PenalTransfertDto penalTransfertDto = prisonerPenalService. getTransfert(tnumide, tcoddet, tnumseqaff , tcodma);
		System.out.println(penalTransfertDto.toString());
		return new ApiResponse<>(HttpStatus.OK.value(), "textMandatDepot Successfully", penalTransfertDto);
		
		
	}
	
	@GetMapping("/getContestation/{tnumide}/{tcoddet}/{tnumseqaff}/{tcodma}/{codeDocumentSecondaire}")
	public ApiResponse<PenalContestationDto> getContestation( @PathVariable String tnumide,@PathVariable String tcoddet,@PathVariable String tnumseqaff , @PathVariable String tcodma , @PathVariable String codeDocumentSecondaire) {
		System.out.println(tnumide + " "+tcoddet+ " "+tnumseqaff + " "+ tcodma + " yes 4 ");
		PenalContestationDto penalContestationDto = prisonerPenalService.getContestation(tnumide, tcoddet, tnumseqaff , tcodma, codeDocumentSecondaire);
		System.out.println(penalContestationDto.toString());
		return new ApiResponse<>(HttpStatus.OK.value(), "Contestation Successfully", penalContestationDto);
		
		
	}
	
	@GetMapping("/getAccusationsParDetenu/{tnumide}/{tcoddet}/{codExtj}")
	public ApiResponse<PenalJugementDTO> getAccusationsParDetenu( @PathVariable String tnumide,@PathVariable String tcoddet,@PathVariable String codExtj   ) {
		System.out.println(tnumide + " "+tcoddet+ " "+tcoddet + " "+ codExtj + " yes 2");
		 PenalJugementDTO penalJugementDTO = prisonerPenalService.getAccusationsParDetenu(tnumide, tcoddet, codExtj);
		return new ApiResponse<>(HttpStatus.OK.value(), "getAccusationsParDetenu Successfully", penalJugementDTO);
		
		
	}
	 
	 
	@GetMapping("/getContrainte/{tnumide}/{tcoddet}/{tnumseqaff}")
	public ApiResponse<PenalContrainteDTO> getContrainte( @PathVariable String tnumide,@PathVariable String tcoddet,@PathVariable String tnumseqaff ) {
		System.out.println(tnumide + " "+tcoddet+ " "+tnumseqaff + " yes 9 ");
		PenalContrainteDTO penalContrainteDTO = prisonerPenalService.getContrainte(tnumide, tcoddet, tnumseqaff  );
		System.out.println(penalContrainteDTO.toString());
		return new ApiResponse<>(HttpStatus.OK.value(), "Contestation Successfully", penalContrainteDTO);
		
		
	}
	
	@GetMapping("/getActesJudiciaires/{tnumide}/{tcoddet}/{tnumseqaff}")
	public ApiResponse<List<ActeJudiciaire>> getActesJudiciaires( @PathVariable String tnumide,@PathVariable String tcoddet,@PathVariable String tnumseqaff   ) {
		 
		List<ActeJudiciaire> list = prisonerPenalService.getActesJudiciaires(tnumide, tcoddet, tnumseqaff);
		return new ApiResponse<>(HttpStatus.OK.value(), "getActesJudiciaires Successfully", list);
		
		
	}
	 
	 @GetMapping("/getArretExecutionParTypeActe/{tnumide}/{tcoddet}/{tnumseqaff}/{typeActe}")
		public ApiResponse<ArretExecutionPenalDTO> getArretExecutionParTypeActe( @PathVariable String tnumide,@PathVariable String tcoddet,@PathVariable String tnumseqaff , @PathVariable String typeActe ) {
			System.out.println(tnumide + " "+tcoddet+ " "+tnumseqaff + " "+ typeActe + " yes 5 ");
			ArretExecutionPenalDTO arretExecutionPenalDTO = prisonerPenalService.getArretExecutionParTypeActe(tnumide, tcoddet, tnumseqaff , typeActe );
			System.out.println(arretExecutionPenalDTO.toString());
			return new ApiResponse<>(HttpStatus.OK.value(), "arretExecutionPenalDTO Successfully", arretExecutionPenalDTO);
			
			
		}
	 
	 
	 @GetMapping("/trouverToutDetentionInfosParPrisonerIdDansPrisons/{prisonerId}")
		public ApiResponse<List<PenaleDetentionInfoDto>> trouverToutDetentionInfosParPrisonerIdDansPrisons( @PathVariable String prisonerId  ) {
			System.out.println(prisonerId    + " yes 6 ");
			List<PenaleDetentionInfoDto> penaleDetentionInfoDtos = prisonerPenalService.trouverToutDetentionInfosParPrisonerIdDansPrisons(prisonerId  );
			System.out.println(penaleDetentionInfoDtos.toString());
			return new ApiResponse<>(HttpStatus.OK.value(), "arretExecutionPenalDTO Successfully", penaleDetentionInfoDtos);
			
			
		}
	 
	 
	 
	 
	 @GetMapping("/rechercherAffaires/{tnumide}/{tcoddet}/{minPage}/{maxPage}")
	 public ApiResponse<List<PenalAffaireDTO>> rechercherAffaires(
	         @PathVariable String tnumide,
	         @PathVariable String tcoddet,
	         @PathVariable int minPage , @PathVariable int maxPage) {

	     System.out.println(tnumide + " yes 7 " + tcoddet);

	     PenalSyntheseDto penalSyntheseDto = new PenalSyntheseDto();

	    
	     List<PenalAffaireDTO> penalAffaireDTOs = prisonerPenalService.rechercherAffaires(tnumide, tcoddet, minPage, maxPage);
	     

	     System.out.println(penalSyntheseDto.toString());

	     return new ApiResponse<>(HttpStatus.OK.value(), "penaleAffaires Successfully", penalAffaireDTOs);
	 }
	 
	 
	 @GetMapping("/rechercherPenalSyntheseDetenu/{tnumide}/{tcoddet}")
	 public ApiResponse<PenalSyntheseDto> rechercherPenalSyntheseDetenu(
	         @PathVariable String tnumide,
	         @PathVariable String tcoddet ) {

	     System.out.println(tnumide + " yes 8 " + tcoddet);

	     
 
	    
	     PenalSyntheseDto penalSyntheseDto = prisonerPenalService.rechercherPenalSyntheseDetenu(tnumide, tcoddet );
	     

	     System.out.println(penalSyntheseDto.toString());

	     return new ApiResponse<>(HttpStatus.OK.value(), "penaleAffaires Successfully", penalSyntheseDto);
	 }
	 
	 
	 @GetMapping("/getPenalGraces/{tnumide}/{tcoddet}")
	 public ApiResponse<List<PenalGraceDto>> getPenalGraces(
	         @PathVariable String tnumide,
	         @PathVariable String tcoddet ) {

	     System.out.println(tnumide + " yes 30 " + tcoddet);

	     
	     List<PenalGraceDto> penalGraceDtos = prisonerPenalService.getPenalGraces(tnumide, tcoddet );
	     

	     return new ApiResponse<>(HttpStatus.OK.value(), "penalGraceDtos Successfully", penalGraceDtos);
	 }
	 
	 
	 
	 @GetMapping("/getMutationResidence/{tnumide}/{tcoddet}")
	 public ApiResponse<List<MutationResidenceDTO>> getMutationResidence(
	         @PathVariable String tnumide,
	         @PathVariable String tcoddet ) {

	     System.out.println(tnumide + " yes 70 " + tcoddet);

	     
 
	    
	     List<MutationResidenceDTO> mutationResidenceDTOs = prisonerPenalService.getMutationResidence(tnumide, tcoddet );
	     

	     

	     return new ApiResponse<>(HttpStatus.OK.value(), "penalGraceDtos Successfully", mutationResidenceDTOs);
	 }
	 
	  
	 
	 @GetMapping("/getEvasionsWithCaptures/{tnumide}/{tcoddet}")
	 public ApiResponse<List<EvasionCaptureDTO>> getEvasionsWithCaptures(
	         @PathVariable String tnumide,
	         @PathVariable String tcoddet ) {

	     System.out.println(tnumide + " yes 90 " + tcoddet);

	     List<EvasionCaptureDTO> evasionCaptureDTOs = prisonerPenalService.getEvasionsWithCaptures(tnumide, tcoddet );
	     

	     return new ApiResponse<>(HttpStatus.OK.value(), "evasionCaptureDTOs Successfully", evasionCaptureDTOs);
	 }
	 
	 @GetMapping("/findParticipantsAffaire/{tnumide}/{tcoddet}")
	 public ApiResponse<List<ParticipantAffaireDTO>> findParticipantsAffaire(
	         @PathVariable String tnumide,
	         @PathVariable String tcoddet ) {

	     System.out.println(tnumide + " yes 100 " + tcoddet);

	     List<ParticipantAffaireDTO> participantAffaireDTOs = prisonerPenalService.findParticipantsAffaire(tnumide, tcoddet );
	     

	     return new ApiResponse<>(HttpStatus.OK.value(), "participantAffaireDTOs Successfully", participantAffaireDTOs);
	 }
	 
	 
}

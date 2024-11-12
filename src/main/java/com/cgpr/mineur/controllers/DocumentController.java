package com.cgpr.mineur.controllers;

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

import com.cgpr.mineur.dto.AffaireDto;
import com.cgpr.mineur.dto.DocumentDto;
import com.cgpr.mineur.dto.DocumentIdDto;
import com.cgpr.mineur.dto.TitreAccusationDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.DocumentService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/document")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	@PostMapping("/trouverDocumentJudiciaireParId")
	public ApiResponse<DocumentDto> trouverDocumentJudiciaireParId(@RequestBody DocumentIdDto documentId) {

		 
		DocumentDto doc = documentService.trouverDocumentJudiciaireParId(documentId);

		return new ApiResponse<>(HttpStatus.OK.value(), "    Successfully", doc);

	}

	@GetMapping("/trouverDocumentsJudiciairesParDetentionEtAffaire/{idEnfant}/{numOrdinalArrestation}/{numOrdinalAffaire}")
	public ApiResponse<List<DocumentDto>> trouverDocumentsJudiciairesParEnfantEtDetentionEtAffaire(
			@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation,
			@PathVariable("numOrdinalAffaire") long numOrdinalAffaire) {
		try {
			List<DocumentDto> aData = documentService.trouverDocumentsJudiciairesParEnfantEtDetentionEtAffaire(idEnfant, numOrdinalArrestation,
					numOrdinalAffaire);

			if (aData.isEmpty()) {
				return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not ok", null);
			} else {
				return new ApiResponse<>(HttpStatus.OK.value(), "  ok", aData);

			}
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not found", null);
		}

	}

 

	@GetMapping("/calculerNombreDocumentsJudiciairesParDetention/{idEnfant}/{numOrdinalArrestation}")
	public ApiResponse<Object> calculerNombreDocumentsJudiciairesParDetention(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation) {

		return new ApiResponse<>(HttpStatus.OK.value(), "ok",
				documentService.calculerNombreDocumentsJudiciairesParDetention(idEnfant, numOrdinalArrestation));
	}

	@GetMapping("/calculerNombreDocumentsJudiciairesParAffaire/{idEnfant}/{numOrdinalArrestation}/{numOrdinalAffaire}")
	public ApiResponse<Object> calculerNombreDocumentsJudiciairesParAffaire(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation,
			@PathVariable("numOrdinalAffaire") long numOrdinalAffaire) {

		return new ApiResponse<>(HttpStatus.OK.value(), "ok",
				documentService.calculerNombreDocumentsJudiciairesParAffaire(idEnfant, numOrdinalArrestation, numOrdinalAffaire));

	}

	@GetMapping("/trouverStatutJudiciaire/{idEnfant}")
	public ApiResponse<AffaireDto> trouverStatutJudiciaire(@PathVariable("idEnfant") String idEnfant) {

		AffaireDto a = documentService.trouverStatutJudiciaire(idEnfant);
		if (a == null) {
			System.out.println("ma7koum");
			return new ApiResponse<>(HttpStatus.OK.value(), "  ma7koum", null);
		} else {
			System.out.println("maw9ouf");
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "maw9ouf", a);
		}

	}



	@PostMapping("/delete/{type}")
	public ApiResponse<Integer> delete(@RequestBody DocumentIdDto documentId, @PathVariable("type") String type) {

		try {
			int ref = 0;
			ref = documentService.delete(documentId, type);

			return new ApiResponse<>(HttpStatus.OK.value(), "saved", ref);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

}

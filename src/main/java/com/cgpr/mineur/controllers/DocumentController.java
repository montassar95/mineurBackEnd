package com.cgpr.mineur.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArretProvisoireRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.DocumentService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/document")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	 

	 
	@GetMapping("/all")
	public ApiResponse<List<Document>> listAffaire() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				(List<Document>) documentService.listAffaire());
	}

	@PostMapping("/findDocumentById")
	public ApiResponse<Document> findDocumentById(@RequestBody DocumentId documentId) {

		System.out.println("==================================documente=========================");
		 Document  doc = documentService.findDocumentById(documentId);

		 
			return new ApiResponse<>(HttpStatus.OK.value(), "    Successfully", doc);
		 

	}

	@GetMapping("/getDocumentByAffaire/{idEnfant}/{numOrdinalArrestation}/{numOrdinalAffaire}")
	public ApiResponse<List<Document>> getDocumentByAffaire(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation,
			@PathVariable("numOrdinalAffaire") long numOrdinalAffaire) {
		try {
			List<Document> aData = documentService.getDocumentByAffaire(idEnfant, numOrdinalArrestation,
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

	@GetMapping("/getTitreAccusation/{idEnfant}/{numOrdinalArrestation}/{numOrdinalAffaire}")
	public ApiResponse<List<TitreAccusation>> getTitreAccusation(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation,
			@PathVariable("numOrdinalAffaire") long numOrdinalAffaire) {

		 
			List<TitreAccusation> titreAccusations =  documentService.getTitreAccusation(idEnfant, numOrdinalArrestation, numOrdinalAffaire);
			return new ApiResponse<>(HttpStatus.OK.value(), "  ok", titreAccusations);

		}
	 

	@GetMapping("/getDocumentByArrestation/{idEnfant}/{numOrdinalArrestation}")
	public ApiResponse<Object> getDocumentByArrestation(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation) {

		return new ApiResponse<>(HttpStatus.OK.value(), "ok",
				documentService.getDocumentByArrestation(idEnfant, numOrdinalArrestation));
	}

	@GetMapping("/countDocumentByAffaire/{idEnfant}/{numOrdinalArrestation}/{numOrdinalAffaire}")
	public ApiResponse<Object> countDocumentByAffaire(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation,
			@PathVariable("numOrdinalAffaire") long numOrdinalAffaire) {

		return new ApiResponse<>(HttpStatus.OK.value(), "ok",
				documentService.countDocumentByAffaire(idEnfant, numOrdinalArrestation, numOrdinalAffaire));

	}

	@GetMapping("/findEtatJuridique/{idEnfant}")
	public ApiResponse<Affaire> findByArrestation(@PathVariable("idEnfant") String idEnfant) {

	 
		Affaire a =   documentService.findByArrestation(idEnfant);
		if (a == null) {
			System.out.println("ma7koum");
			return new ApiResponse<>(HttpStatus.OK.value(), "  ma7koum", null);
		} else {
			System.out.println("maw9ouf");
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "maw9ouf", a);
		}

	}

	@GetMapping("/findDocumentByArrestation/{idEnfant}/{numOrdinalArrestation}")
	public ApiResponse<Object> findDocumentByArrestation(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation) {

		return new ApiResponse<>(HttpStatus.OK.value(), "ok",
				(List<Document>) documentService.findDocumentByArrestation(idEnfant, numOrdinalArrestation));
	}

	@PostMapping("/delete/{type}")
	public ApiResponse<Integer> delete(@RequestBody DocumentId documentId, @PathVariable("type") String type) {

		try {
			int ref = 0;
			ref = documentService.delete(documentId, type);
			 

			 
			return new ApiResponse<>(HttpStatus.OK.value(), "saved", ref);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

 
}

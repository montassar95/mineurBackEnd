package com.cgpr.mineur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.AppelParquet;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.AppelParquetRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/appelParquet")
public class AppelParquetController {

	@Autowired
	private AppelParquetRepository appelParquetRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@PostMapping("/add")
	public ApiResponse<AppelParquet> save(@RequestBody AppelParquet appelParquet) {

		System.out.println(appelParquet.toString());

		System.out.println(appelParquet.getDocumentId().toString());

		System.out.println("================================debut affaire ===========================");
		System.out.println(appelParquet.getAffaire().toString());
		appelParquet.getAffaire().setTypeDocument("AP");
		appelParquet.getAffaire().setTypeAffaire(appelParquet.getAffaire().getTypeAffaire());
		affaireRepository.save(appelParquet.getAffaire());
		System.out.println("==================================fin affaire=========================");
		appelParquet.getAffaire().setNumOrdinalAffaireByAffaire(3);
		appelParquet.setTypeAffaire(appelParquet.getAffaire().getTypeAffaire());
		AppelParquet c = appelParquetRepository.save(appelParquet);

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

}

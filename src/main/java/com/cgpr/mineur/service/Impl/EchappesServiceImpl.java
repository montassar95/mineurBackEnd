package com.cgpr.mineur.service.Impl;


 
import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.service.DocumentService;
import com.cgpr.mineur.service.EchappesService;

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

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Echappes;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.EchappesRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
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

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Echappes;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.EchappesRepository;
import com.cgpr.mineur.repository.ResidenceRepository; 
@Service
public class EchappesServiceImpl implements EchappesService  {

	
	 
	@Autowired
	private EchappesRepository echappesRepository;
	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Override
	public Echappes save(Echappes echappes) {

		System.out.print(echappes.toString());

		try {

			Echappes eData = echappesRepository
					.findByIdEnfantAndResidenceTrouverNull(echappes.getEchappesId().getIdEnfant());

			if (eData == null) {
				echappes.getResidenceEchapper()
						.setNombreEchappes(echappes.getResidenceEchapper().getNombreEchappes() + 1);
				residenceRepository.save(echappes.getResidenceEchapper());
			}

			else {
				if (echappes.getResidenceTrouver() != null) {
					if (!(echappes.getResidenceEchapper().getEtablissement().getId().toString().trim()
							.equals(echappes.getResidenceTrouver().getEtablissement().getId().toString().trim()))) {

						System.err.println(
								"qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
						System.err.println(echappes.getResidenceEchapper().getEtablissement().getId());
						System.err.println(echappes.getResidenceTrouver().getEtablissement().getId());
						System.err.println(
								"qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
						echappes.getResidenceEchapper().setStatut(1);
						echappes.getResidenceEchapper().setDateSortie(echappes.getDateTrouver());
						residenceRepository.save(echappes.getResidenceEchapper());
						echappes.getResidenceTrouver().setStatut(0);
						echappes.getResidenceTrouver().setDateEntree(echappes.getDateTrouver());
						echappes.getResidenceTrouver().setNombreEchappes(0);
						echappes.getResidenceTrouver().getResidenceId().setNumOrdinaleResidence(
								echappes.getResidenceEchapper().getResidenceId().getNumOrdinaleResidence() + 1);
						residenceRepository.save(echappes.getResidenceTrouver());

					}
				}

			}

			Echappes e = echappesRepository.save(echappes);

			return  e;

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Object countByEnfant(String idEnfant, long numOrdinaleArrestation) {

		Echappes eData = echappesRepository.findByIdEnfantAndResidenceTrouverNull(idEnfant);

		if (eData == null) {
			return (echappesRepository.countByEnfantAndArrestation(idEnfant, numOrdinaleArrestation) + 1);

		} else {
			return  eData.getEchappesId().getNumOrdinaleEchappes() ;

		}

	}

	@Override
	public Object countTotaleEchappes( String idEnfant, long numOrdinaleArrestation) {

		return echappesRepository.countByEnfantAndArrestation(idEnfant, numOrdinaleArrestation);

	}

	@Override
	public Echappes findByIdEnfantAndResidenceTrouverNull( String idEnfant) {

		Echappes cData = echappesRepository.findByIdEnfantAndResidenceTrouverNull(idEnfant);
		if (cData != null) {
			return  cData;
		} else {
			return  null;
		}
	}

	@Override
	public List<Echappes> findEchappesByIdEnfant( String idEnfant, long numOrdinaleArrestation) {

		List<Echappes> cData = echappesRepository.findEchappesByIdEnfantAndNumOrdinaleArrestation(idEnfant,
				numOrdinaleArrestation);
		if (cData != null) {
			return  cData;
		} else {
			return  null;
		}
	}
	
	 
}


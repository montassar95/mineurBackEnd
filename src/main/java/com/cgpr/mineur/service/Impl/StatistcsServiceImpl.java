package com.cgpr.mineur.service.Impl;

import com.cgpr.mineur.resource.StatisticsDTO;
import com.cgpr.mineur.service.StatistcsService;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.repository.StatistcsRepository;
import com.cgpr.mineur.resource.StatisticsDTO;

@Service
public class StatistcsServiceImpl implements StatistcsService {

	@Autowired
	private StatistcsRepository statistcsRepository;

	// findByEtablissement
	@Autowired
	private ResidenceRepository residanceRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	// findByEtablissement

	@Override
	public StatisticsDTO getStatistcs(String id) {

		System.out.println(id + " zzzzzzzzzz");

		if (Integer.parseInt(id) == 0) {
			id = null;
		}

		LocalDate localDate = LocalDate.now();
		// Calcul des plages de dates pour chaque tranche d'âge
		LocalDate today = LocalDate.now();
		LocalDate start13 = today.minusYears(13).withDayOfYear(1);
		LocalDate end13 = today.minusYears(12).withDayOfYear(1).minusDays(1);

		LocalDate start14 = today.minusYears(14).withDayOfYear(1);
		LocalDate end14 = today.minusYears(13).withDayOfYear(1).minusDays(1);

		LocalDate start15 = today.minusYears(15).withDayOfYear(1);
		LocalDate end15 = today.minusYears(14).withDayOfYear(1).minusDays(1);

		LocalDate start16 = today.minusYears(16).withDayOfYear(1);
		LocalDate end16 = today.minusYears(15).withDayOfYear(1).minusDays(1);

		LocalDate start17 = today.minusYears(17).withDayOfYear(1);
		LocalDate end17 = today.minusYears(16).withDayOfYear(1).minusDays(1);

		LocalDate start18 = today.minusYears(18).withDayOfYear(1);
		LocalDate end18 = today.minusYears(17).withDayOfYear(1).minusDays(1);

		Calendar cal = Calendar.getInstance();

		int year = Calendar.getInstance().get(Calendar.YEAR);

		// cal.set(Calendar.YEAR, (year - 13) - 1);
		// cal.set(Calendar.MONTH, (localDate.getMonthValue() - 1));
		// cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// start13 = cal.getTime();
		//
		// cal.set(Calendar.YEAR, year - 13);
		// cal.set(Calendar.MONTH, (localDate.getMonthValue() - 1));
		// cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// end13 = cal.getTime();
		//
		// cal.set(Calendar.YEAR, (year - 14) - 1);
		// cal.set(Calendar.MONTH, (localDate.getMonthValue() - 1));
		// cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// start14 = cal.getTime();
		//
		// cal.set(Calendar.YEAR, year - 14);
		// cal.set(Calendar.MONTH, (localDate.getMonthValue() - 1));
		// cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// end14 = cal.getTime();
		//
		// cal.set(Calendar.YEAR, (year - 15) - 1);
		// cal.set(Calendar.MONTH, (localDate.getMonthValue() - 1));
		// cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// start15 = cal.getTime();
		//
		// cal.set(Calendar.YEAR, year - 15);
		// cal.set(Calendar.MONTH, (localDate.getMonthValue() - 1));
		// cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// end15 = cal.getTime();
		//
		// cal.set(Calendar.YEAR, (year - 16) - 1);
		// cal.set(Calendar.MONTH, (localDate.getMonthValue() - 1));
		// cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// start16 = cal.getTime();
		//
		// cal.set(Calendar.YEAR, year - 16);
		// cal.set(Calendar.MONTH, (localDate.getMonthValue() - 1));
		// cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// end16 = cal.getTime();
		//
		// cal.set(Calendar.YEAR, (year - 17) - 1);
		// cal.set(Calendar.MONTH, (localDate.getMonthValue() - 1));
		// cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// start17 = cal.getTime();
		//
		// cal.set(Calendar.YEAR, year - 17);
		// cal.set(Calendar.MONTH, (localDate.getMonthValue() - 1));
		// cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// end17 = cal.getTime();
		//
		// cal.set(Calendar.YEAR, (year - 18) - 1);
		// cal.set(Calendar.MONTH, (localDate.getMonthValue() - 1));
		// cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// start18 = cal.getTime();
		//
		// cal.set(Calendar.YEAR, year - 18);
		// cal.set(Calendar.MONTH, (localDate.getMonthValue() - 1));
		// cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// end18 = cal.getTime();

		StatisticsDTO sta = new StatisticsDTO();

		Map<String, Integer> typeAffairesArrete = new HashMap<String, Integer>();

		Map<String, Integer> typeAffairesJuge = new HashMap<String, Integer>();

		List<Arrestation> arrestationData = statistcsRepository.findArrestationByEtablissement(id);

		List<Arrestation> output = arrestationData.stream().map(s -> {

			List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(s.getArrestationId().getIdEnfant(),
					s.getArrestationId().getNumOrdinale());

			boolean allSameName = affprincipale.stream()
					.allMatch(x -> x.getTypeDocument().toString().equals("ArretEx".toString()));

			Affaire a = affprincipale.stream().peek(num -> System.out.println("aff filter " + num.getTypeDocument()))
					.filter(x -> x.getTypeDocument().equals("AP") || x.getTypeDocument().equals("CD")
							|| x.getTypeDocument().equals("CH") || x.getTypeDocument().equals("CJA")
							|| x.getTypeDocument().equals("T") || x.getTypeDocument().equals("AE")
							|| x.getTypeDocument().equals("CP")

					)

					.findFirst().orElse(null);

			if (!(affprincipale.isEmpty())) {
				if (a != null) {

					// s.setNumAffairePricipale(a.getAffaireId().getNumAffaire());
					// s.setTribunalPricipale(a.getTribunal());
					// s.setNumOrdinalAffairePricipale(a.getNumOrdinalAffaire());
					// s.setTypeAffairePricipale(a.getTypeAffaire());

					Integer j = typeAffairesArrete.get(a.getTypeAffaire().getLibelle_typeAffaire());
					typeAffairesArrete.put(a.getTypeAffaire().getLibelle_typeAffaire(),
							(j == null) ? 1 : j + 1);

				}

				else {
					a = affprincipale.stream().peek(num -> System.out.println("aff filter " + num.getTypeDocument()))
							.filter(x -> (x.getTypeDocument().equals("CJ") && (x.getAffaireAffecter() == null)))
							.findFirst()

							.orElse(affprincipale.stream()
									.peek(num -> System.out.println("aff filter " + num.getTypeDocument()))
									.filter(x -> ((x.getAffaireAffecter() == null))).findFirst().orElse(null));

					// s.setNumAffairePricipale(a.getAffaireId().getNumAffaire());
					// s.setTribunalPricipale(a.getTribunal());
					// s.setNumOrdinalAffairePricipale(a.getNumOrdinalAffaire());
					// s.setTypeAffairePricipale(a.getTypeAffaire());

					Integer j = typeAffairesJuge.get(a.getTypeAffaire().getLibelle_typeAffaire());
					typeAffairesJuge.put(a.getTypeAffaire().getLibelle_typeAffaire(), (j == null) ? 1 : j + 1);

				}
			}

			return s;
		}).collect(Collectors.toList());

		// for (Arrestation i : arrestationData) {
		// Integer j = hm.get(i.getTypeAffairePricipale().getLibelle_typeAffaire());
		// hm.put(i.getTypeAffairePricipale().getLibelle_typeAffaire(), (j == null) ? 1
		// : j + 1);
		// }

		sta.setTypeAffairesArrete(typeAffairesArrete);

		sta.setTypeAffairesJuge(typeAffairesJuge);

		sta.setNbrArret(statistcsRepository.findByAllEnfantExistArret(id));
		sta.setNbrJuge(statistcsRepository.findByAllEnfantExistJuge(id));
		sta.setNbrAll(statistcsRepository.findByAllEnfantExist(id));
		sta.setNbrEntreeMutation(statistcsRepository.findByEntreeMutation(id));
		sta.setNbrSortieMutation(statistcsRepository.findBySortieMutation(id));
		sta.setNbrEtrange(statistcsRepository.findByAllEnfantExistEtranger(id));

		sta.setNbrDebutant(statistcsRepository.findByAllEnfantDebutant(id));
		sta.setNbrAncien(statistcsRepository.findByAllEnfantAncien(id));

		sta.setNbrAge13(statistcsRepository.findByAllByAge(id, start13, end13));
		sta.setNbrAge14(statistcsRepository.findByAllByAge(id, start14, end14));
		sta.setNbrAge15(statistcsRepository.findByAllByAge(id, start15, end15));
		sta.setNbrAge16(statistcsRepository.findByAllByAge(id, start16, end16));
		sta.setNbrAge17(statistcsRepository.findByAllByAge(id, start17, end17));
		sta.setNbrAge18(statistcsRepository.findByAllByAge(id, start18, end18));

		sta.setNbrIgnorant(statistcsRepository.findByAllEnfantNiveauEducatif(id, 20, 20));
		sta.setNbrPrimaire(statistcsRepository.findByAllEnfantNiveauEducatif(id, 6, 11));
		sta.setNbrPrimaire(sta.getNbrPrimaire() + statistcsRepository.findByAllEnfantNiveauEducatif(id, 22, 22));
		sta.setNbrPrepa(statistcsRepository.findByAllEnfantNiveauEducatif(id, 1, 3));
		sta.setNbrSecondaire(statistcsRepository.findByAllEnfantNiveauEducatif(id, 4, 4));
		sta.setNbrFormation(statistcsRepository.findByAllEnfantNiveauEducatif(id, 5, 5));
		sta.setNbrEtudiant(statistcsRepository.findByAllEnfantNiveauEducatif(id, 12, 12));

		sta.setNbrSiFaAvec(statistcsRepository.findByAllEnfantSituationFamiliale(id, 4));
		sta.setNbrSiFaParentSepa(statistcsRepository.findByAllEnfantSituationFamiliale(id, 1));
		sta.setNbrSiFaOrphelinPe(statistcsRepository.findByAllEnfantSituationFamiliale(id, 2));
		sta.setNbrSiFaOrphelinMe(statistcsRepository.findByAllEnfantSituationFamiliale(id, 3));
		sta.setNbrSiFaOrphelinPeMe(statistcsRepository.findByAllEnfantSituationFamiliale(id, 5));
		sta.setNbrSiFaCasSoci(statistcsRepository.findByAllEnfantSituationFamiliale(id, 6));

		return sta;

	}

	// @GetMapping("/countTotaleRecidence/{idEnfant}/{numOrdinaleArrestation}")
	// public ApiResponse<Object> countTotaleRecidence(@PathVariable("idEnfant")
	// String idEnfant,@PathVariable("numOrdinaleArrestation") long
	// numOrdinaleArrestation) {
	//
	// int total
	// =residenceRepository.countTotaleRecidence(idEnfant,numOrdinaleArrestation);
	//
	// if(total==0) {
	// total =0;
	// }
	// else {total=total-1;}
	//
	//
	// return new ApiResponse<>(HttpStatus.OK.value(), "ok", total);
	// }

}

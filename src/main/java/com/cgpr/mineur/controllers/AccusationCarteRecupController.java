package com.cgpr.mineur.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.AccusationCarteRecupId;
import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/accusationCarteRecup")
public class AccusationCarteRecupController {

	@Autowired
	private AccusationCarteRecupRepository accusationCarteRecupRepository;

	@PostMapping("/findByCarteRecup")
	public ApiResponse<List<AccusationCarteRecup>> findByCarteDepot(@RequestBody CarteRecup carteRecup) {

		System.out.println(carteRecup.toString());

		List<AccusationCarteRecup> list = accusationCarteRecupRepository.findByCarteRecup(carteRecup.getDocumentId());
		System.out.println(list.toString());
		if (list.isEmpty()) {

			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not ", null);

		} else {
			try {
				return new ApiResponse<>(HttpStatus.OK.value(), "    Successfully", list);
			} catch (Exception e) {
				return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not  ", null);
			}

		}

	}

	@GetMapping("/calcule/{date}/{duree}")
	public ApiResponse<Object> getArrestationById(@PathVariable("date") String date, @PathVariable("duree") int duree) {

		Date dateC = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateDtring = "";

		try {
			dateC = simpleDateFormat.parse(date);

			Calendar cal = Calendar.getInstance();
			cal.setTime(dateC);

			cal.add(Calendar.DATE, duree);
			Date modifiedDate = cal.getTime();

			dateDtring = simpleDateFormat.format(modifiedDate);
			System.out.println(dateDtring);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", dateDtring);

	}

	@PostMapping("/add")
	public ApiResponse<AccusationCarteRecup> save(@RequestBody AccusationCarteRecup accusationCarteRecup) {

		accusationCarteRecupRepository.save(accusationCarteRecup);

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

}

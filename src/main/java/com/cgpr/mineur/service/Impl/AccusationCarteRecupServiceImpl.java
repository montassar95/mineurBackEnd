package com.cgpr.mineur.service.Impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.service.AccusationCarteRecupService;
@Service
public class AccusationCarteRecupServiceImpl implements AccusationCarteRecupService {

	
	@Autowired
	private AccusationCarteRecupRepository accusationCarteRecupRepository;

	
	@Override
	public List<AccusationCarteRecup> findByCarteRecup(CarteRecup carteRecup) {
		System.out.println(carteRecup.toString() + "eeeeeeeeeeeeeeeeeeeeeeeee");

		List<AccusationCarteRecup> list = accusationCarteRecupRepository.findByCarteRecup(carteRecup.getDocumentId());
		System.out.println(list.toString());
		if (list.isEmpty()) {

			return  null ;

		} else {
			try {
				return   list ;
			} catch (Exception e) {
				return   null ;
			}

		}
	}

	@Override
	public Object getArrestationById(String date, int duree) {
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
		return   dateDtring ;

	}

	@Override
	public AccusationCarteRecup save(AccusationCarteRecup accusationCarteRecup) {
		accusationCarteRecupRepository.save(accusationCarteRecup);

		try {
			return  null ;
		} catch (Exception e) {
			return  null ;
		}

	}
	
	 
}


package com.cgpr.mineur.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.service.AccusationCarteHeberService;
@Service
public class AccusationCarteHeberServiceImpl   implements AccusationCarteHeberService{

	
	@Autowired
	private AccusationCarteHeberRepository accusationCarteHeberRepository;

	
	@Override
	public AccusationCarteHeber save(AccusationCarteHeber accusationCarteHeber) {
		accusationCarteHeberRepository.save(accusationCarteHeber);

		try {
			return  null ;
		} catch (Exception e) {
			return null ;
		}
	}

	@Override
	public List<TitreAccusation> findTitreAccusationbyCarteHeber(CarteHeber carteHeber) {
		List<TitreAccusation> list = accusationCarteHeberRepository
				.getTitreAccusationbyDocument(carteHeber.getDocumentId());
		System.out.println(list.toString());
		if (list.isEmpty()) {

			return    null ;

		} else {
			try {
				return  list ;
			} catch (Exception e) {
				return  null ;
			}

		}

	}
	
 
	 
	 
}


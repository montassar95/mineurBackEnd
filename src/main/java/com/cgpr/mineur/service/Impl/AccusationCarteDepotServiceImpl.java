package com.cgpr.mineur.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.service.AccusationCarteDepotService;


@Service
public class AccusationCarteDepotServiceImpl  implements AccusationCarteDepotService  {

	
	
	
	@Autowired
	private AccusationCarteDepotRepository accusationCarteDepotRepository;

	
	
	@Override
	public AccusationCarteDepot save(AccusationCarteDepot accusationCarteDepot) {
		
		accusationCarteDepotRepository.save(accusationCarteDepot);

		try {
			return   null ;
		} catch (Exception e) {
			return  null ;
		}
	}

	@Override
	public List<TitreAccusation> findTitreAccusationbyCarteDepot(CarteDepot carteDepot) {
		
		List<TitreAccusation> list = accusationCarteDepotRepository
				.getTitreAccusationbyDocument(carteDepot.getDocumentId());
		System.out.println(list.toString()+"iciiiii");
		if (list.isEmpty()) {

			return   null ;

		} else {
			try {
				return  list ;
			} catch (Exception e) {
				return null ;
			}

		}
	}
	
 
}


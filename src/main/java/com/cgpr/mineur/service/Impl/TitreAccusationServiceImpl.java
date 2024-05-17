package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.repository.TitreAccusationRepository;
import com.cgpr.mineur.service.TitreAccusationService;

 
 

@Service
public class TitreAccusationServiceImpl  implements TitreAccusationService{

	

	@Autowired
	private TitreAccusationRepository titreAccusationRepository;

	@Override
	public List<TitreAccusation> findTitreAccusationByIdTypeAffaire( long id) {
		List<TitreAccusation> accusationData = titreAccusationRepository.findTitreAccusationByIdTypeAffaire(id);
		if (accusationData != null) {
			return    accusationData;
		} else {
			return  null;
		}
	}

	@Override
	public List<TitreAccusation> list() {
		return titreAccusationRepository.findAllByOrderByIdAsc();
	}

	@Override
	public TitreAccusation getById( long id) {
		Optional<TitreAccusation> Data = titreAccusationRepository.findById(id);
		if (Data.isPresent()) {
			return  Data.get();
		} else {
			return  null;
		}
	}

	@Override
	public TitreAccusation save(TitreAccusation causeDeces) {

		try {
			return titreAccusationRepository.save(causeDeces);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public TitreAccusation update(TitreAccusation causeDeces) {
		try {

			return titreAccusationRepository.save(causeDeces);
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete( long id) {
		try {
			titreAccusationRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return null;
		}
	}

	
	 
}


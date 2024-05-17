package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.SituationFamiliale;
import com.cgpr.mineur.repository.SituationFamilialeRepository;
import com.cgpr.mineur.service.SituationFamilialeService;

 
 

@Service
public class SituationFamilialeServiceImpl  implements SituationFamilialeService{

	
	@Autowired
	private SituationFamilialeRepository situationFamilialeRepository;

	@Override
	public List<SituationFamiliale> listNationalite() {
		return situationFamilialeRepository.findAllByOrderByIdAsc();
	}

	@Override
	public SituationFamiliale getById( long id) {
		Optional<SituationFamiliale> Data = situationFamilialeRepository.findById(id);
		if (Data.isPresent()) {
			return  Data.get();
		} else {
			return  null;
		}
	}

	@Override
	public SituationFamiliale save(SituationFamiliale situationFamiliale) {

		try {
			return situationFamilialeRepository.save(situationFamiliale);
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public SituationFamiliale update( SituationFamiliale situationFamiliale) {
		try {

			return situationFamilialeRepository.save(situationFamiliale);
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete( long id) {
		try {
			situationFamilialeRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return  null;
		}
	}

	 
}


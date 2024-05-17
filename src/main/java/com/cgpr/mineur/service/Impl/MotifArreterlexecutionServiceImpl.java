package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.MotifArreterlexecution;
import com.cgpr.mineur.repository.MotifArreterlexecutionRepository;
import com.cgpr.mineur.service.MotifArreterlexecutionService;

 
@Service
public class MotifArreterlexecutionServiceImpl implements MotifArreterlexecutionService {

	
	@Autowired
	private MotifArreterlexecutionRepository motifArreterlexecutionRepository;

	@Override
	public List<MotifArreterlexecution> listMotifArreterlexecution() {
		return motifArreterlexecutionRepository.findAllByOrderByIdAsc();
	}

	@Override
	public MotifArreterlexecution getById(long id) {
		Optional<MotifArreterlexecution> Data = motifArreterlexecutionRepository.findById(id);
		if (Data.isPresent()) {
			return  Data.get();
		} else {
			return  null;
		}
	}

	@Override
	public MotifArreterlexecution save( MotifArreterlexecution causeDeces) {

		try {
			return motifArreterlexecutionRepository.save(causeDeces);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public MotifArreterlexecution update(MotifArreterlexecution causeDeces) {
		try {

			return motifArreterlexecutionRepository.save(causeDeces);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Void delete( long id) {
		try {
			motifArreterlexecutionRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return  null;
		}
	}
	 

}
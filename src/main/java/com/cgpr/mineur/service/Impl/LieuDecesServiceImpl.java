package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.LieuDeces;
import com.cgpr.mineur.repository.LieuDecesRepository;
import com.cgpr.mineur.service.LieuDecesService;

 
 

@Service
public class LieuDecesServiceImpl implements LieuDecesService {

	
	 
	@Autowired
	private LieuDecesRepository lieuDecesRepository;

	@Override
	public List<LieuDeces> listCauseMutation() {
		return lieuDecesRepository.findAllByOrderByIdAsc();
	}

	@Override
	public LieuDeces getById(long id) {
		Optional<LieuDeces> Data = lieuDecesRepository.findById(id);
		if (Data.isPresent()) {
			return  Data.get();
		} else {
			return null;
		}
	}

	@Override
	public LieuDeces save( LieuDeces causeDeces) {

		try {
			return lieuDecesRepository.save(causeDeces);
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public LieuDeces update(LieuDeces causeDeces) {
		try {

			return lieuDecesRepository.save(causeDeces);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Void delete( long id) {
		try {
			lieuDecesRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return  null;
		}
	}

	 

}


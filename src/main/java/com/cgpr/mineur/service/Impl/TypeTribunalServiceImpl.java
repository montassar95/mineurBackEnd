package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.TypeTribunal;
import com.cgpr.mineur.repository.TypeTribunalRepository;
import com.cgpr.mineur.service.TypeTribunalService;


 

@Service
public class TypeTribunalServiceImpl implements TypeTribunalService{

	
	@Autowired
	private TypeTribunalRepository typeTribunalRepository;

	@Override
	public List<TypeTribunal> list() {
		return typeTribunalRepository.findAllByOrderByIdAsc();
	}

	@Override
	public TypeTribunal getById( long id) {
		Optional<TypeTribunal> Data = typeTribunalRepository.findById(id);
		if (Data.isPresent()) {
			return  Data.get();
		} else {
			return  null;
		}
	}
	
	
	
	 

	@Override
	public TypeTribunal save( TypeTribunal causeDeces) {

		try {
			return typeTribunalRepository.save(causeDeces);
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public TypeTribunal update(TypeTribunal causeDeces) {
		try {

			return typeTribunalRepository.save(causeDeces);
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete(long id) {
		try {
			typeTribunalRepository.deleteById(id);
			return null;
		} catch (Exception e) {
			return  null;
		}
	}
 
	
 
	 
}


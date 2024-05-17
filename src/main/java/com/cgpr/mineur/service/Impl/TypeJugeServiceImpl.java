package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.TypeJuge;
import com.cgpr.mineur.repository.TypeJugeRepository;
import com.cgpr.mineur.service.TypeJugeService;


 

@Service
public class TypeJugeServiceImpl implements  TypeJugeService{

	
	@Autowired
	private TypeJugeRepository typeJugeRepository;

	@Override
	public List<TypeJuge> listTypeJuge() {
		return typeJugeRepository.findAllByOrderByIdAsc();
	}

	@Override
	public TypeJuge getTypeJugeById( long id) {
		Optional<TypeJuge> typeData = typeJugeRepository.findById(id);
		if (typeData.isPresent()) {
			return  typeData.get();
		} else {
			return  null;
		}
	}

	@Override
	public TypeJuge save(TypeJuge typeJuge) {

		try {
			return typeJugeRepository.save(typeJuge);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public TypeJuge update(TypeJuge typeJuge) {
		try {

			return typeJugeRepository.save(typeJuge);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Void delete(long id) {
		try {
			typeJugeRepository.deleteById(id);
			return null;
		} catch (Exception e) {
			return  null;
		}
	}
	 
}


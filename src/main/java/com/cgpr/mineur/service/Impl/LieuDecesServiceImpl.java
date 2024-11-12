package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.LiberationConverter;
import com.cgpr.mineur.converter.LieuDecesConverter;
import com.cgpr.mineur.dto.LieuDecesDto;
import com.cgpr.mineur.models.LieuDeces;
import com.cgpr.mineur.repository.LieuDecesRepository;
import com.cgpr.mineur.service.LieuDecesService;

 
 

@Service
public class LieuDecesServiceImpl implements LieuDecesService {

	
	 
	@Autowired
	private LieuDecesRepository lieuDecesRepository;

	@Override
	public List<LieuDecesDto> listCauseMutation() {
		 List<LieuDeces > list = lieuDecesRepository.findAllByOrderByIdAsc();
		 
		return list. stream().map(LieuDecesConverter::entityToDto).collect(Collectors.toList());  
	}

	@Override
	public LieuDecesDto getById(long id) {
		Optional<LieuDeces> Data = lieuDecesRepository.findById(id);
		if (Data.isPresent()) {
			return  LieuDecesConverter.entityToDto(Data.get()) ;
		} else {
			return null;
		}
	}

	@Override
	public LieuDecesDto save( LieuDecesDto lieuDecesDto) {

		try {
			return LieuDecesConverter.entityToDto(lieuDecesRepository.save(LieuDecesConverter.dtoToEntity(lieuDecesDto) ));
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public LieuDecesDto update(LieuDecesDto lieuDecesDto) {
		try {

			return LieuDecesConverter.entityToDto(lieuDecesRepository.save(LieuDecesConverter.dtoToEntity(lieuDecesDto) ));
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


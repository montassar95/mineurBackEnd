package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.MetierConverter;
import com.cgpr.mineur.converter.MotifArreterlexecutionConverter;
import com.cgpr.mineur.dto.MotifArreterlexecutionDto;
import com.cgpr.mineur.models.Metier;
import com.cgpr.mineur.models.MotifArreterlexecution;
import com.cgpr.mineur.repository.MotifArreterlexecutionRepository;
import com.cgpr.mineur.service.MotifArreterlexecutionService;

 
@Service
public class MotifArreterlexecutionServiceImpl implements MotifArreterlexecutionService {

	
	@Autowired
	private MotifArreterlexecutionRepository motifArreterlexecutionRepository;

	@Override
	public List<MotifArreterlexecutionDto> listMotifArreterlexecution() {
		
		 List<MotifArreterlexecution > list = motifArreterlexecutionRepository.findAllByOrderByIdAsc();
		 
			return list. stream().map(MotifArreterlexecutionConverter::entityToDto).collect(Collectors.toList()); 
	 
	}

	@Override
	public MotifArreterlexecutionDto getById(long id) {
		Optional<MotifArreterlexecution> Data = motifArreterlexecutionRepository.findById(id);
		if (Data.isPresent()) {
			return  MotifArreterlexecutionConverter .entityToDto(Data.get());
		} else {
			return  null;
		}
	}

	@Override
	public MotifArreterlexecutionDto save( MotifArreterlexecutionDto motifArreterlexecutionDto) {

		try {
			return  MotifArreterlexecutionConverter.entityToDto(motifArreterlexecutionRepository.save(MotifArreterlexecutionConverter.dtoToEntity(motifArreterlexecutionDto) ))  ;
			 
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public MotifArreterlexecutionDto update(MotifArreterlexecutionDto motifArreterlexecutionDto) {
		try {

			return  MotifArreterlexecutionConverter.entityToDto(motifArreterlexecutionRepository.save(MotifArreterlexecutionConverter.dtoToEntity(motifArreterlexecutionDto) ))  ;
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
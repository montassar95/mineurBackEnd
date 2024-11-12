package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.EchappesConverter;
import com.cgpr.mineur.converter.EtabChangeManiereConverter;
import com.cgpr.mineur.dto.EtabChangeManiereDto;
import com.cgpr.mineur.models.EtabChangeManiere;
import com.cgpr.mineur.repository.EtabChangeManiereRepository;
import com.cgpr.mineur.service.EtabChangeManiereService;
 

@Service
public class EtabChangeManiereServiceImpl   implements EtabChangeManiereService{

	 
	@Autowired
	private EtabChangeManiereRepository etabChangeManiereRepository;

	@Override
	public List<EtabChangeManiereDto> listEtablissement() {
		
		List<EtabChangeManiere> list =	(List<EtabChangeManiere>) etabChangeManiereRepository.findAll();
		
		return list.stream().map(EtabChangeManiereConverter::entityToDto).collect(Collectors.toList())  ; 
	}
	 
	@Override
	public EtabChangeManiereDto getEtablissementById(String id) {
		Optional<EtabChangeManiere> etablissementData = etabChangeManiereRepository.findById(id);
		if (etablissementData.isPresent()) {
			return EtabChangeManiereConverter.entityToDto(etabChangeManiereRepository.findById(id).get()) ;
		} else {
			return  null;
		}
	}

	@Override
	public EtabChangeManiereDto save(EtabChangeManiereDto etabChangeManiereDto) {
		System.out.print(etabChangeManiereDto.toString());
		try {
			EtabChangeManiere etabChangeManiere = EtabChangeManiereConverter.dtoToEntity(etabChangeManiereDto);
		
			   etabChangeManiere =etabChangeManiereRepository.save( etabChangeManiere );
			
			return  EtabChangeManiereConverter.entityToDto(etabChangeManiere) ;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public EtabChangeManiereDto update( EtabChangeManiereDto etabChangeManiereDto) {
		try {

			EtabChangeManiere etabChangeManiere = EtabChangeManiereConverter.dtoToEntity(etabChangeManiereDto);
			
			   etabChangeManiere =etabChangeManiereRepository.save( etabChangeManiere );
			
			return  EtabChangeManiereConverter.entityToDto(etabChangeManiere) ;
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete( String id) {
		try {
			etabChangeManiereRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return  null;
		}
	}
	 

}


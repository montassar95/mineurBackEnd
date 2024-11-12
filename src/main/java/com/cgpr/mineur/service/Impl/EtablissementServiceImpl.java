package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.EtabChangeManiereConverter;
import com.cgpr.mineur.converter.EtablissementConverter;
import com.cgpr.mineur.dto.EtablissementDto;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.repository.EtablissementRepository;
import com.cgpr.mineur.service.EtablissementService;

 
 

@Service
public class EtablissementServiceImpl  implements  EtablissementService {

	
	@Autowired
	private EtablissementRepository etablissementRepository;

	@Override
	public  List<EtablissementDto>   listEtablissement() {
		List<Etablissement> list = (List<Etablissement>) etablissementRepository.findAll();
		return  list. stream().map(EtablissementConverter::entityToDto).collect(Collectors.toList())  ;
	}
	@Override
	public List<EtablissementDto> listEtablissementCentre() {
		
		List<Etablissement> allCentre =  etablissementRepository.listEtablissementCentre();
		return allCentre. stream().map(EtablissementConverter::entityToDto).collect(Collectors.toList()) ;
	}
	@Override
	public EtablissementDto getEtablissementById(String id) {
		Optional<Etablissement> etablissementData = etablissementRepository.findById(id);
		if (etablissementData.isPresent()) {
			return  EtablissementConverter .entityToDto(etablissementRepository.findById(id).get()) ;
		} else {
			return  null;
		}
	}

	@Override
	public EtablissementDto save(EtablissementDto etablissementDto) {
		System.out.print(etablissementDto.toString());
		try {
			return EtablissementConverter .entityToDto(etablissementRepository.save(EtablissementConverter .dtoToEntity(etablissementDto)));
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public EtablissementDto update( EtablissementDto etablissementDto) {
		try {

			return EtablissementConverter .entityToDto(etablissementRepository.save(EtablissementConverter .dtoToEntity(etablissementDto)));
		} catch (Exception e) {
			return   null;
		}

	}

	@Override
	public  Void  delete( String id) {
		try {
			etablissementRepository.deleteById(id);
			return  null ;
		} catch (Exception e) {
			return  null ;
		}
	}
 

}


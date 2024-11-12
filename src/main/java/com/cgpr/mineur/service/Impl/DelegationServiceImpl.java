package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.DecesConverter;
import com.cgpr.mineur.converter.DelegationConverter;
import com.cgpr.mineur.dto.DelegationDto;
import com.cgpr.mineur.models.Deces;
import com.cgpr.mineur.models.Delegation;
import com.cgpr.mineur.repository.DelegationRepository;
import com.cgpr.mineur.service.DelegationService;


 

@Service
public class DelegationServiceImpl implements DelegationService{

	
	@Autowired
	private DelegationRepository delegationRepository;

	@Override
	public List<DelegationDto> list() {
		
		List<Delegation > list =  delegationRepository.findAllByOrderByIdAsc();
		return  list.stream().map(DelegationConverter::entityToDto).collect(Collectors.toList())  ;
		
		
		 
	}

	@Override
	public DelegationDto getById(long id) {
		Optional<Delegation> delegation = delegationRepository.findById(id);
		if (delegation.isPresent()) {
			return DelegationConverter.entityToDto(delegation.get())  ;
		} else {
			return  null;
		}
	}

	@Override
	public List<DelegationDto> getDelegationByGouv( long id) {
		
		return delegationRepository.getDelegationByGouv(id).stream().map(DelegationConverter::entityToDto).collect(Collectors.toList());
		 
	}

	@Override
	public DelegationDto findByGouvernorat( long idG, long idD) {

		return DelegationConverter.entityToDto(delegationRepository.findByGouvernorat(idG, idD));
	}

	@Override
	public DelegationDto save( DelegationDto delegationDto) {

		try {
			Delegation delegation = delegationRepository.save(DelegationConverter.dtoToEntity (delegationDto));
			return DelegationConverter.entityToDto(delegation);
		} catch (Exception e) {
			return  null;
		}
	}
	
	@Override
	public DelegationDto update( DelegationDto delegationDto) {
		try {

			Delegation delegation = delegationRepository.save(DelegationConverter.dtoToEntity (delegationDto));
			return DelegationConverter.entityToDto(delegation);
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete(long id ) {
		try {
			delegationRepository.deleteById(id);
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	 
	
	 
}


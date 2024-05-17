package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Delegation;
import com.cgpr.mineur.repository.DelegationRepository;
import com.cgpr.mineur.service.DelegationService;


 

@Service
public class DelegationServiceImpl implements DelegationService{

	
	@Autowired
	private DelegationRepository delegationRepository;

	@Override
	public List<Delegation> list() {
		return delegationRepository.findAllByOrderByIdAsc();
	}

	@Override
	public Delegation getById(long id) {
		Optional<Delegation> Data = delegationRepository.findById(id);
		if (Data.isPresent()) {
			return  Data.get();
		} else {
			return  null;
		}
	}

	@Override
	public List<Delegation> getDelegationByGouv( long id) {
		return delegationRepository.getDelegationByGouv(id);
	}

	@Override
	public Delegation findByGouvernorat( long idG, long idD) {

		return delegationRepository.findByGouvernorat(idG, idD);
	}

	@Override
	public Delegation save( Delegation delegation) {

		try {
			return delegationRepository.save(delegation);
		} catch (Exception e) {
			return  null;
		}
	}
	
	@Override
	public Delegation update( Delegation causeDeces) {
		try {

			return delegationRepository.save(causeDeces);
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


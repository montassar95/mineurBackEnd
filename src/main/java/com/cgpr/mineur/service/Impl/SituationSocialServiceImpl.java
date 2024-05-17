package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.SituationSocial;
import com.cgpr.mineur.repository.SituationSocialRepository;
import com.cgpr.mineur.service.SituationSocialService;

 
 

@Service
public class SituationSocialServiceImpl implements SituationSocialService{

	
	@Autowired
	private SituationSocialRepository situationSocialRepository;

	@Override
	public List<SituationSocial> listNationalite() {
		return  situationSocialRepository.findAllByOrderByIdAsc();
	}

	@Override
	public SituationSocial getById( long id) {
		Optional<SituationSocial> Data = situationSocialRepository.findById(id);
		if (Data.isPresent()) {
			return  Data.get();
		} else {
			return  null;
		}
	}

	@Override
	public SituationSocial save( SituationSocial situationFamiliale) {

		try {
			return situationSocialRepository.save(situationFamiliale);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SituationSocial update( SituationSocial situationFamiliale) {
		try {

			return situationSocialRepository.save(situationFamiliale);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Void delete( long id) {
		try {
			situationSocialRepository.deleteById(id);
			return null;
		} catch (Exception e) {
			 
		}
		return null;
	}

	 
}


package com.cgpr.mineur.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Visite;
import com.cgpr.mineur.repository.VisiteRepository;
import com.cgpr.mineur.service.VisiteService;

 
 

@Service
public class VisiteServiceImpl implements VisiteService{

	
	@Autowired
	private VisiteRepository visiteRepository;



	@Override
	public Visite save( Visite visite) {
		System.out.println(visite.toString());
		try {
			Optional<Visite> v = visiteRepository.findbyEnfantandDate(visite.getEnfant().getId(),
					visite.getAnneeVisite(), visite.getMoisVisite());
			if (v.isPresent()) {

				Visite visiteUpdate = v.get();
				if (visite.getNbrVisite() == 0) {
					visiteRepository.deleteById(visiteUpdate.getEnfantIdVisite());
				} else {
					visiteUpdate.setNbrVisite(visite.getNbrVisite());
					visiteRepository.save(visiteUpdate);
				}

				System.out.println("exist");
			} else {
				System.out.println("first");

				if (visite.getNbrVisite() > 0) {
					visiteRepository.save(visite);
				}

			}

//			visiteRepository.save(visite)
			return  null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Void delete( long id) {
		try {
			visiteRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public Visite getVisite( String id,  int anneeVisite, int moisVisite) {
		Optional<Visite> v = visiteRepository.findbyEnfantandDate(id, anneeVisite, moisVisite);

		if (v.isPresent()) {
			return  v.get();
		} else {
			return  null;
		}
	}
	 
	 
}


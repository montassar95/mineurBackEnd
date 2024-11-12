package com.cgpr.mineur.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.VisiteConverter;
import com.cgpr.mineur.dto.VisiteDto;
import com.cgpr.mineur.models.Visite;
import com.cgpr.mineur.repository.VisiteRepository;
import com.cgpr.mineur.service.VisiteService;

 
 

@Service
public class VisiteServiceImpl implements VisiteService{

	
	@Autowired
	private VisiteRepository visiteRepository;



	@Override
	public VisiteDto save( VisiteDto visiteDto) {
		System.out.println(visiteDto.toString());
		try {
			Optional<Visite> v = visiteRepository.findbyEnfantandDate(visiteDto.getEnfant().getId(),
					visiteDto.getAnneeVisite(), visiteDto.getMoisVisite());
			if (v.isPresent()) {

				Visite visiteUpdate = v.get();
				if (visiteDto.getNbrVisite() == 0) {
					visiteRepository.deleteById(visiteUpdate.getEnfantIdVisite());
				} else {
					visiteUpdate.setNbrVisite(visiteDto.getNbrVisite());
					visiteRepository.save(visiteUpdate);
				}

				System.out.println("exist");
			} else {
				System.out.println("first");

				if (visiteDto.getNbrVisite() > 0) {
					visiteRepository.save( VisiteConverter.dtoToEntity(visiteDto)  );
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
	public VisiteDto getVisite( String id,  int anneeVisite, int moisVisite) {
		Optional<Visite> v = visiteRepository.findbyEnfantandDate(id, anneeVisite, moisVisite);

		if (v.isPresent()) {
			return VisiteConverter.entityToDto(v.get()) ;
		} else {
			return  null;
		}
	}
	 
	 
}


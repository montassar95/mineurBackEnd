package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.ArrestationConverter;
import com.cgpr.mineur.converter.EtabChangeManiereConverter;
import com.cgpr.mineur.converter.LiberationConverter;
import com.cgpr.mineur.dto.ArrestationDto;
import com.cgpr.mineur.dto.LiberationDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.ArrestationId;
import com.cgpr.mineur.models.Liberation;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.LiberationRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.service.ArrestationService;
import com.cgpr.mineur.tools.AffaireUtils;


 

@Service
public class ArrestationServiceImpl implements  ArrestationService {

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private LiberationRepository liberationRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private ResidenceRepository residenceRepository;

 

 

	@Override
	public ArrestationDto trouverDerniereDetentionParIdDetenu(  String id) {
		Arrestation arrestationData = arrestationRepository.findByIdEnfantAndStatut0(id);

//		if (arrestationData.getLiberation() == null) {
//
//			List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(
//					arrestationData.getArrestationId().getIdEnfant(),
//					arrestationData.getArrestationId().getNumOrdinale());
//			boolean allSameName = affprincipale.stream().allMatch(x -> x.getTypeDocument().equals("AEX"));
//			if (allSameName) {
//				arrestationData.setEtatJuridique("isAEX");
//
//			} else {
//				List<Affaire> aData = documentRepository
//						.findStatutJurByArrestation(arrestationData.getArrestationId().getIdEnfant());
//
//				if (aData.isEmpty()) {
//					arrestationData.setEtatJuridique("juge");
//				} else {
//					arrestationData.setEtatJuridique("arret");
//				}
//
//			}
//		} else {
//			arrestationData.setEtatJuridique("libre");
//		}

		if (arrestationData != null) {
		 
			return ArrestationConverter.entityToDto(arrestationData);
		} else {
			return null;
		}
	}
	
	
 

	@Override
	public Object calculerNombreDetentionsParIdDetenu( String id) {

		return   arrestationRepository.countByEnfant(id);

	}

	@Override
	public ArrestationDto save( ArrestationDto arrestationDto) {

		try {
			 
			Arrestation a = null;
//			arrestation.getArrestationId().setNumOrdinale(arrestationRepository.countByEnfant(arrestation.getArrestationId().getIdEnfant())+1);  
			if (arrestationDto.getLiberation() != null) {
				arrestationDto.setStatut(1);
				arrestationDto.setEtatJuridique("libre");
				liberationRepository.save(LiberationConverter.dtoToEntity(arrestationDto.getLiberation()));
				Residence r = residenceRepository.findByIdEnfantAndStatut0(arrestationDto.getArrestationId().getIdEnfant(),
						arrestationDto.getArrestationId().getNumOrdinale());
				r.setStatut(1);
				r.setDateSortie(arrestationDto.getLiberation().getDate());
				if (arrestationDto.getLiberation().getEtabChangeManiere() != null) {
					r.setEtabChangeManiere(EtabChangeManiereConverter.dtoToEntity(arrestationDto.getLiberation().getEtabChangeManiere()) );

				}
				residenceRepository.save(r);
			}
			a = arrestationRepository.save(ArrestationConverter.dtoToEntity(arrestationDto));
			return  ArrestationConverter.entityToDto(a);
		} catch (Exception e) {
			return  null;
		}
	}

 

	@Override
	public ArrestationDto delete( ArrestationDto arrestationDto) {

		try {
		 
			Arrestation a = null;
 			Liberation liberation = null;
 			LiberationDto liberationDto = null;
			if (arrestationDto.getLiberation() != null) {
				arrestationDto.setStatut(0);
				arrestationDto.setEtatJuridique(null);
				liberationDto = arrestationDto.getLiberation();
				arrestationDto.setLiberation(null);
				
				
				arrestationRepository.save(ArrestationConverter.dtoToEntity(arrestationDto));

				liberationRepository.delete(LiberationConverter.dtoToEntity(liberationDto));
				Residence r = residenceRepository.findMaxResidence(arrestationDto.getArrestationId().getIdEnfant(),
						arrestationDto.getArrestationId().getNumOrdinale());
				r.setStatut(0);
				r.setEtabChangeManiere(null);
				r.setDateSortie(null);
				residenceRepository.save(r);
			}

			return  ArrestationConverter.entityToDto(a);
		} catch (Exception e) {
			return  null;
		}
	}	
	 
}


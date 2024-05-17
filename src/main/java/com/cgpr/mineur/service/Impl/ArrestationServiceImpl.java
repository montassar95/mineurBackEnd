package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public List<Arrestation> list() {
		return  (List<Arrestation>) arrestationRepository.findAll() ;
	}

	@Override
	public Arrestation getArrestationById( String idEnfant, long numOrdinale) {
		ArrestationId arrestationId = new ArrestationId(idEnfant, numOrdinale);
		Optional<Arrestation> cData = arrestationRepository.findById(arrestationId);

		if (cData.isPresent()) {
			return   cData.get() ;
		} else {
			return null ;
		}
	}

	@Override
	public Arrestation findByIdEnfantAndStatut0(  String id) {
		Arrestation arrestationData = arrestationRepository.findByIdEnfantAndStatut0(id);

		if (arrestationData.getLiberation() == null) {

			List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(
					arrestationData.getArrestationId().getIdEnfant(),
					arrestationData.getArrestationId().getNumOrdinale());
			boolean allSameName = affprincipale.stream().allMatch(x -> x.getTypeDocument().equals("AEX"));
			if (allSameName) {
				arrestationData.setEtatJuridique("isAEX");

			} else {
				List<Affaire> aData = documentRepository
						.findStatutJurByArrestation(arrestationData.getArrestationId().getIdEnfant());

				if (aData.isEmpty()) {
					arrestationData.setEtatJuridique("juge");
				} else {
					arrestationData.setEtatJuridique("arret");
				}

			}
		} else {
			arrestationData.setEtatJuridique("libre");
		}

		if (arrestationData != null) {
		 
			return arrestationData;
		} else {
			return null;
		}
	}
	
	
//	x.getDocuments().get(x.getDocuments().size()-1)
	@Override
	public List<Arrestation> findByIdEnfant(String id) {

		List<Arrestation> allArrestation = arrestationRepository.findByIdEnfant(id);

		if (allArrestation == null || allArrestation.isEmpty()) {
			return null;
		}

		 return allArrestation.stream()
		            .map(arrestation -> AffaireUtils.processArrestationToGetAffairPrincipal(arrestation, affaireRepository))
		            .collect(Collectors.toList());
	}

	@Override
	public Object countByEnfant( String id) {

		return   arrestationRepository.countByEnfant(id);

	}

	@Override
	public Arrestation save( Arrestation arrestation) {

		try {
			 
			Arrestation a = null;
//			arrestation.getArrestationId().setNumOrdinale(arrestationRepository.countByEnfant(arrestation.getArrestationId().getIdEnfant())+1);  
			if (arrestation.getLiberation() != null) {
				arrestation.setStatut(1);
				arrestation.setEtatJuridique("libre");
				liberationRepository.save(arrestation.getLiberation());
				Residence r = residenceRepository.findByIdEnfantAndStatut0(arrestation.getArrestationId().getIdEnfant(),
						arrestation.getArrestationId().getNumOrdinale());
				r.setStatut(1);
				r.setDateSortie(arrestation.getLiberation().getDate());
				if (arrestation.getLiberation().getEtabChangeManiere() != null) {
					r.setEtabChangeManiere(arrestation.getLiberation().getEtabChangeManiere());

				}
				residenceRepository.save(r);
			}
			a = arrestationRepository.save(arrestation);
			return  a;
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public Arrestation update( Arrestation arrestation) {
		try {

			return arrestationRepository.save(arrestation);
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Arrestation delete( Arrestation arrestation) {

		try {
		 
			Arrestation a = null;
			Liberation l = null;
			if (arrestation.getLiberation() != null) {
				arrestation.setStatut(0);
				arrestation.setEtatJuridique(null);
				l = arrestation.getLiberation();
				arrestation.setLiberation(null);
				arrestationRepository.save(arrestation);

				liberationRepository.delete(l);
				Residence r = residenceRepository.findMaxResidence(arrestation.getArrestationId().getIdEnfant(),
						arrestation.getArrestationId().getNumOrdinale());
				r.setStatut(0);
				r.setEtabChangeManiere(null);
				r.setDateSortie(null);
				residenceRepository.save(r);
			}

			return  a;
		} catch (Exception e) {
			return  null;
		}
	}	
	 
}


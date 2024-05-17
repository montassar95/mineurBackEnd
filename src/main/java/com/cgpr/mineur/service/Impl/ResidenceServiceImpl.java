package com.cgpr.mineur.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.ResidenceId;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.service.ResidenceService;

@Service
public class ResidenceServiceImpl implements ResidenceService{

	
	
	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Override
	public List<Residence> list() {
		return  (List<Residence>) residenceRepository.findAll();
	}

	@Override
	public Residence geById(String id, long numOrdinale) {
		Residence aData = residenceRepository.findByIdEnfantAndStatutEnCour(id, numOrdinale);
		if (aData != null) {
			return  aData;
		} else {
			return  null;
		}
	}

	@Override
	public  List<Residence>  findByEnfantAndArrestation(  String id, long numOrdinale) {
		
		List<Residence> cData = residenceRepository.findByEnfantAndArrestation(id, numOrdinale);
		if (cData != null) {
			return   cData ;
		} else {
			return   null ;
		}

	}

	@Override
	public  Residence  findByArrestationAndStatut0(  String idEnfant,
			  long numOrdinale) {

	 	Residence cData = residenceRepository.findByIdEnfantAndStatut0(idEnfant, numOrdinale);
		if (cData != null) {
			return   cData ;
		} else {
			return   null ;
		}
	}

	@Override
	public Residence  findByArrestationAndMaxResidence(  String idEnfant,  long numOrdinale) {

		Residence cData = residenceRepository.findMaxResidence(idEnfant, numOrdinale);
		 	if (cData != null) {
			return   cData ;
		} else {
			return  null ;
		}
	}

	@Override
	public  List<Residence>  findByIdEnfantAndStatutArrestation0(  String idEnfant) {

		List<Residence> cData = residenceRepository.findByIdEnfantAndStatutArrestation0(idEnfant);
		if (cData != null) {
			return  cData ;
		} else {
			return   null ;
		}
	}

	@Override
	public  Residence  save(  Residence residance) {
		System.out.println(residance.toString());

		try {
			Residence cData = residenceRepository.findByIdEnfantAndStatut0(residance.getResidenceId().getIdEnfant(),
					residance.getArrestation().getArrestationId().getNumOrdinale());
			if (cData == null) {

				Residence r = residenceRepository.save(residance);
				return  r ;

			} else {
				cData.setDateSortie(residance.getDateEntree());
				cData.setEtablissementSortie(residance.getEtablissement());
				cData.setCauseMutationSortie(residance.getCauseMutation());
				residenceRepository.save(cData);
				residance.getResidenceId()
						.setNumOrdinaleResidence(cData.getResidenceId().getNumOrdinaleResidence() + 1);
				residance.setStatut(2);
				residance.setDateEntree(null);
				residance.setEtablissementEntree(cData.getEtablissement());

				return  residenceRepository.save(residance) ;
			}

		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public Residence  accepterResidence(  Residence residance) {
		System.out.println(residance.toString());

		try {
			Residence cData = residenceRepository.findByIdEnfantAndStatut0(residance.getResidenceId().getIdEnfant(),
					residance.getArrestation().getArrestationId().getNumOrdinale());
			cData.setStatut(1);
			residance.setStatut(0);
			residenceRepository.save(cData);
			return  residenceRepository.save(residance) ;
		} catch (Exception e) {
			return  null ;
		}
	}

	@Override
	public Residence  update( Residence residance) {
		try {

			return residenceRepository.save(residance) ;
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public  Object  countTotaleRecidence(  String idEnfant, long numOrdinaleArrestation) {

		int total = residenceRepository.countTotaleRecidence(idEnfant, numOrdinaleArrestation);
		if (total == 0) {
			total = 0;
		} else {
			total = total - 1;
		}

		return   total ;
	}

	@Override
	public Object  countTotaleRecidenceWithetabChangeManiere(  String idEnfant,  long numOrdinaleArrestation) {

		System.out
				.println(residenceRepository.countTotaleRecidenceWithetabChangeManiere(idEnfant, numOrdinaleArrestation)
						+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

		return  residenceRepository.countTotaleRecidenceWithetabChangeManiere(idEnfant, numOrdinaleArrestation);
	}

	@Override
	public  Residence  deleteResidenceStatut2(  ResidenceId residanceId) {

		try {
			residenceRepository.deleteById(residanceId);
			Residence cData = residenceRepository.findByIdEnfantAndStatut0(residanceId.getIdEnfant(),
					residanceId.getNumOrdinaleArrestation());
			cData.setDateSortie(null);
			cData.setEtablissementSortie(null);
			cData.setCauseMutationSortie(null);
			residenceRepository.save(cData);
			return  null ;
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public  Residence  deleteResidenceStatut0( ResidenceId residanceId) {

		try {

			Residence cData = residenceRepository.findByIdEnfantAndStatut0(residanceId.getIdEnfant(),
					residanceId.getNumOrdinaleArrestation());
			cData.setDateSortie(null);
			cData.setDateEntree(null);
			cData.setStatut(2);
			cData.setNumArrestation(null);

			Residence lastData1 = residenceRepository.findMaxResidenceWithStatut1(residanceId.getIdEnfant(),
					residanceId.getNumOrdinaleArrestation());

			lastData1.setStatut(0);
			residenceRepository.save(lastData1);
			residenceRepository.save(cData);

			return   null ;
		} catch (Exception e) {
			return   null ;
		}
	}

 

}

package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.cgpr.mineur.converter.PersonelleConverter;
import com.cgpr.mineur.converter.ResidenceConverter;
import com.cgpr.mineur.converter.ResidenceIdConverter;
import com.cgpr.mineur.dto.ResidenceDto;
import com.cgpr.mineur.dto.ResidenceIdDto;
//import com.cgpr.mineur.models.Personelle;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.ResidenceId;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.service.ResidenceService;

@Service
public class ResidenceServiceImpl implements ResidenceService{

	
	
	@Autowired
	private ResidenceRepository residenceRepository;

	 

	 
	 

	 
	 

	 
	 

	 
	@Override
 	public ResidenceDto  trouverDerniereResidenceParNumDetentionEtIdDetenu(  String idEnfant,  long numOrdinale) {

		Residence residence = residenceRepository.findMaxResidence(idEnfant, numOrdinale);
		 	if (residence != null) {
			return    ResidenceConverter . entityToDto(residence);  
		} else {
			return  null ;
		}
	}

	@Override
	public  List<ResidenceDto>  trouverResidencesDetentionActiveParIdDetenu(  String idEnfant) {

		List<Residence> residences = residenceRepository.findByIdEnfantAndStatutArrestation0(idEnfant);
		if (residences != null) {
			return  residences.stream().map(ResidenceConverter::entityToDto).collect(Collectors.toList());    
		} else {
			return   null ;
		}
	}

 
	
	
	@Override
	public  ResidenceDto   save(  ResidenceDto  residanceDto ) {
		
 		Residence residance = ResidenceConverter.dtoToEntity(residanceDto);
		System.out.println("residance entree"+ residance.toString());

 
			Residence cData = residenceRepository.findByIdEnfantAndStatut0(residance.getResidenceId().getIdEnfant(),
					residance.getArrestation().getArrestationId().getNumOrdinale());
			if (cData == null) {

				Residence r = residenceRepository.save(residance);
 				return  ResidenceConverter.entityToDto(r) ;
 

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

 				return  ResidenceConverter.entityToDto(residenceRepository.save(residance)) ;
				
 
			}

 
	}
	
	
	@Override
	public ResidenceDto  accepterDemandeMutation(  ResidenceDto residanceDto) {
		System.out.println(residanceDto.toString());

		try {
			Residence cData = residenceRepository.findByIdEnfantAndStatut0(residanceDto.getResidenceId().getIdEnfant(),
					residanceDto.getArrestation().getArrestationId().getNumOrdinale());
			cData.setStatut(1);
			residenceRepository.save(cData);
			residanceDto.setStatut(0);
			System.out.println("iciiiiiii");
			System.out.println(ResidenceConverter.dtoToEntity(residanceDto).toString());
		 	return  ResidenceConverter.entityToDto(residenceRepository.save(ResidenceConverter.dtoToEntity(residanceDto)  ))  ;
			 
		} catch (Exception e) {
			return  null ;
		}
	}

	 

	 
	 

	 
	 

	 
	@Override
	public  ResidenceDto  supprimerDemandeMutation(  ResidenceIdDto residanceIdDto) {

		try {
			residenceRepository.deleteById(ResidenceIdConverter.dtoToEntity(residanceIdDto)  );
			Residence cData = residenceRepository.findByIdEnfantAndStatut0(residanceIdDto.getIdEnfant(),
					residanceIdDto.getNumOrdinaleArrestation());
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
	public  ResidenceDto  supprimerAcceptationMutation( ResidenceIdDto residanceIdDto) {

		try {

			Residence cData = residenceRepository.findByIdEnfantAndStatut0(residanceIdDto.getIdEnfant(),
					residanceIdDto.getNumOrdinaleArrestation());
			cData.setDateSortie(null);
			cData.setDateEntree(null);
			cData.setStatut(2);
			cData.setNumArrestation(null);

			Residence lastData1 = residenceRepository.findMaxResidenceWithStatut1(residanceIdDto.getIdEnfant(),
					residanceIdDto.getNumOrdinaleArrestation());

			lastData1.setStatut(0);
			residenceRepository.save(lastData1);
			residenceRepository.save(cData);

			return   null ;
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public Boolean validerNumeroEcrou(String numeroEcrou, String etablissementId) {
	    long count = residenceRepository.validerNumeroEcrou(numeroEcrou, etablissementId);
	    System.out.println(count);
	    return count > 0;
	}


 

}

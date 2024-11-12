package com.cgpr.mineur.service.Impl;


 
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.EchappesConverter;
import com.cgpr.mineur.converter.ResidenceConverter;
import com.cgpr.mineur.dto.EchappesDto;
import com.cgpr.mineur.models.Echappes;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.EchappesRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.service.EchappesService; 
@Service
public class EchappesServiceImpl implements EchappesService  {

	
	 
	@Autowired
	private EchappesRepository echappesRepository;
	@Autowired
	private ResidenceRepository residenceRepository;

 

	@Override
	public EchappesDto save(EchappesDto echappesDto) {

		System.out.print(echappesDto.toString());

 

			Echappes eData = echappesRepository
					.findByIdEnfantAndResidenceTrouverNull(echappesDto.getEchappesId().getIdEnfant());

			if (eData == null) {
				
				echappesDto.getResidenceEchapper()
						.setNombreEchappes(echappesDto.getResidenceEchapper().getNombreEchappes() + 1);
				
				System.err.println("i'm here 1 : ");
				System.err.println(ResidenceConverter.dtoToEntity(echappesDto.getResidenceEchapper()));
				
				residenceRepository.save(ResidenceConverter.dtoToEntity(echappesDto.getResidenceEchapper()));
			}

			else {
				if (echappesDto.getResidenceTrouver() != null) {
					if (!(echappesDto.getResidenceEchapper().getEtablissement().getId().toString().trim()
							.equals(echappesDto.getResidenceTrouver().getEtablissement().getId().toString().trim()))) {

						System.err.println(
								"qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
						System.err.println(echappesDto.getResidenceEchapper().getEtablissement().getId());
						System.err.println(echappesDto.getResidenceTrouver().getEtablissement().getId());
						System.err.println(
								"qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
						echappesDto.getResidenceEchapper().setStatut(1);
						echappesDto.getResidenceEchapper().setDateSortie(echappesDto.getDateTrouver());
						residenceRepository.save(ResidenceConverter.dtoToEntity(echappesDto.getResidenceEchapper()));
						echappesDto.getResidenceTrouver().setStatut(0);
						echappesDto.getResidenceTrouver().setDateEntree(echappesDto.getDateTrouver());
						echappesDto.getResidenceTrouver().setNombreEchappes(0);
						echappesDto.getResidenceTrouver().getResidenceId().setNumOrdinaleResidence(
						 echappesDto.getResidenceEchapper().getResidenceId().getNumOrdinaleResidence() + 1);
						residenceRepository.save(ResidenceConverter.dtoToEntity(echappesDto.getResidenceTrouver()) );

					}
				}

			}
			System.err.println("i'm here 2 : ");
			System.err.println(EchappesConverter.dtoToEntity(echappesDto));
			Echappes e = echappesRepository.save(EchappesConverter.dtoToEntity( echappesDto));

			return EchappesConverter.entityToDto(e) ;

 
	}

 

	@Override
	public List<EchappesDto> trouverEchappesParIdDetenuEtNumDetention( String idEnfant, long numOrdinaleArrestation) {

		List<Echappes> echappes = echappesRepository.findEchappesByIdEnfantAndNumOrdinaleArrestation(idEnfant,
				numOrdinaleArrestation);
		if (echappes != null) {
			return   echappes.stream().map(EchappesConverter::entityToDto).collect(Collectors.toList())  ;
		} else {
			return  null;
		}
	}
	
	 
}


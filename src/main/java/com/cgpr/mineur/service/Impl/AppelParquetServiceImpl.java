package com.cgpr.mineur.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.AppelParquetConverter;
import com.cgpr.mineur.dto.AppelParquetDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.AppelParquet;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.AppelParquetRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.AppelParquetService;

@Service
public class AppelParquetServiceImpl implements AppelParquetService {

	@Autowired
	private AppelParquetRepository appelParquetRepository;

	@Autowired
	private AffaireRepository affaireRepository;

 

	@Override
	public AppelParquetDto save(AppelParquetDto appelParquetDto) {

		appelParquetDto.getAffaire().setTypeDocument("AP");
		appelParquetDto.getAffaire().setTypeAffaire(appelParquetDto.getAffaire().getTypeAffaire());

		Affaire affaireSaved = affaireRepository.save(AffaireConverter.dtoToEntity(appelParquetDto.getAffaire()));

		appelParquetDto.getAffaire().setNumOrdinalAffaireByAffaire(affaireSaved.getNumOrdinalAffaireByAffaire());
		appelParquetDto.setTypeAffaire(appelParquetDto.getAffaire().getTypeAffaire());

		AppelParquet c = appelParquetRepository.save(AppelParquetConverter.dtoToEntity(appelParquetDto));

		try {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

}

package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.NationaliteConverter;
import com.cgpr.mineur.converter.ResultatTransfertConverter;
import com.cgpr.mineur.dto.ResultatTransfertDto;
import com.cgpr.mineur.models.Nationalite;
import com.cgpr.mineur.models.ResultatTransfert;
import com.cgpr.mineur.repository.ResultatTransfertRepository;
import com.cgpr.mineur.service.ResultatTransfertService;

 
 

@Service
public class ResultatTransfertServiceImpl implements  ResultatTransfertService{

	@Autowired
	private ResultatTransfertRepository resultatTransfertRepository;

	@Override
	public  List<ResultatTransfertDto>  listTypeJuge() {
		
		
		 List<ResultatTransfert > list = resultatTransfertRepository.findAllByOrderByIdAsc();
		 
			return list. stream().map(ResultatTransfertConverter::entityToDto).collect(Collectors.toList());  
			
		 
	}

	@Override
	public  ResultatTransfertDto  getTypeJugeById( long id) {
		Optional<ResultatTransfert> resultatTransfert = resultatTransfertRepository.findById(id);
		if (resultatTransfert.isPresent()) {
			return  ResultatTransfertConverter.entityToDto(resultatTransfert.get())  ;
		} else {
			return  null ;
		}
	}

	@Override
	public  ResultatTransfertDto  save(  ResultatTransfertDto resultatTransfertDto) {

		try {
			return ResultatTransfertConverter.entityToDto(resultatTransfertRepository.save(ResultatTransfertConverter.dtoToEntity(resultatTransfertDto)  ))  ;
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public ResultatTransfertDto  update(  ResultatTransfertDto resultatTransfertDto) {
		try {

	 return ResultatTransfertConverter.entityToDto(resultatTransfertRepository.save(ResultatTransfertConverter.dtoToEntity(resultatTransfertDto)  ))  ;
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public  Void delete(  long id) {
		try {
			resultatTransfertRepository.deleteById(id);
			return   null ;
		} catch (Exception e) {
			return   null ;
		}
	}

	
 
}


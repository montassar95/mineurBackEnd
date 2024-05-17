package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.ResultatTransfert;
import com.cgpr.mineur.repository.ResultatTransfertRepository;
import com.cgpr.mineur.service.ResultatTransfertService;

 
 

@Service
public class ResultatTransfertServiceImpl implements  ResultatTransfertService{

	@Autowired
	private ResultatTransfertRepository resultatTransfertRepository;

	@Override
	public  List<ResultatTransfert>  listTypeJuge() {
		return  resultatTransfertRepository.findAllByOrderByIdAsc() ;
	}

	@Override
	public  ResultatTransfert  getTypeJugeById( long id) {
		Optional<ResultatTransfert> typeData = resultatTransfertRepository.findById(id);
		if (typeData.isPresent()) {
			return   typeData.get() ;
		} else {
			return  null ;
		}
	}

	@Override
	public  ResultatTransfert  save(  ResultatTransfert res) {

		try {
			return  resultatTransfertRepository.save(res) ;
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public ResultatTransfert  update(  ResultatTransfert res) {
		try {

			return  resultatTransfertRepository.save(res) ;
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


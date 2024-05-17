package com.cgpr.mineur.service.Impl;

import java.util.List;

import com.cgpr.mineur.models.Metier;
import com.cgpr.mineur.service.MetierService;


 
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.ApiResponse;

import com.cgpr.mineur.models.Metier;

import com.cgpr.mineur.repository.MetierRepository;
 
@Service
public class MetierServiceImpl implements MetierService{

	
	@Autowired
	private MetierRepository metierRepository;

	@Override
	public  List<Metier>  list() {
		return  metierRepository.findAllByOrderByIdAsc( );
	}

	@Override
	public  Metier  getById(@PathVariable("id") long id) {
		Optional<Metier> Data = metierRepository.findById(id);
		if (Data.isPresent()) {
			return   Data.get() ;
		} else {
			return  null ;
		}
	}

	
	
	
	@Override
	public  Metier  save(@RequestBody Metier gouv) {

		try {
			return  metierRepository.save(gouv) ;
		} catch (Exception e) {
			return  null ;
		}
	}

	@Override
	public  Metier  update(@RequestBody Metier gouv) {
		try {

			return  metierRepository.save(gouv );
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public  Void   delete( long id) {
		try {
			metierRepository.deleteById(id);
			return  null ;
		} catch (Exception e) {
			return   null ;
		}
	}
 
	 
	 
}


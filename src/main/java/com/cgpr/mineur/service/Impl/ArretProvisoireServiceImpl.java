package com.cgpr.mineur.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.repository.ArretProvisoireRepository;
import com.cgpr.mineur.service.ArretProvisoireService;


 

@Service
public class ArretProvisoireServiceImpl implements  ArretProvisoireService{

	@Autowired
	private ArretProvisoireRepository arretProvisoireRepository;

	@Override
	public  List<ArretProvisoire>  list() {
		return  (List<ArretProvisoire>) arretProvisoireRepository.findAll() ;
	}

 

	@Override
	public  ArretProvisoire  save(  ArretProvisoire arretProvisoire) {

		try {
			return  arretProvisoireRepository.save(arretProvisoire) ;
		} catch (Exception e) {
			return   null ;
		}
	}

	@Override
	public  ArretProvisoire  update(  ArretProvisoire arretProvisoire) {
		try {

			return  arretProvisoireRepository.save(arretProvisoire) ;
		} catch (Exception e) {
			return   null ;
		}

	}
	
	
	
	
	@Override
	public   List<ArretProvisoire>  getDocumentByArrestation(  String idEnfant,   long numOrdinalArrestation) {
		 List<ArretProvisoire> list = arretProvisoireRepository.getArretProvisoirebyArrestation(idEnfant, numOrdinalArrestation);
		 
		 if(list.isEmpty()) {
			 return  null ;
		 }
		 else {
		 
			 return  list ;
			
		 }
		 
		 
		
	}
	
	

	@Override
	public  List<ArretProvisoire>  findArretProvisoireByCarteRecup(  CarteRecup carteRecup) {
	
	System.out.println(carteRecup.toString());
	
	
	
	List<ArretProvisoire> list = arretProvisoireRepository.findArretProvisoireByCarteRecup(carteRecup.getDocumentId());
	System.out.println(list.toString());
	if(list.isEmpty()) {
		
		
		return   null ;
		
		}
	else {
			try {
			return  list ;
		} catch (Exception e) {
			return   null ;
		}
	
	}
	}
}


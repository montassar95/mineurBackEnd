package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.EtabChangeManiere;
import com.cgpr.mineur.service.EtabChangeManiereService;

 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.cgpr.mineur.models.EtabChangeManiere;
import com.cgpr.mineur.repository.EtabChangeManiereRepository;
 

@Service
public class EtabChangeManiereServiceImpl   implements EtabChangeManiereService{

	 
	@Autowired
	private EtabChangeManiereRepository etabChangeManiereRepository;

	@Override
	public List<EtabChangeManiere> listEtablissement() {
		
		return (List<EtabChangeManiere>) etabChangeManiereRepository.findAll();
	}
	 
	@Override
	public EtabChangeManiere getEtablissementById(String id) {
		Optional<EtabChangeManiere> etablissementData = etabChangeManiereRepository.findById(id);
		if (etablissementData.isPresent()) {
			return etabChangeManiereRepository.findById(id).get();
		} else {
			return  null;
		}
	}

	@Override
	public EtabChangeManiere save(EtabChangeManiere etablissement) {
		System.out.print(etablissement.toString());
		try {
			return etabChangeManiereRepository.save(etablissement);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public EtabChangeManiere update( EtabChangeManiere etablissement) {
		try {

			return etabChangeManiereRepository.save(etablissement);
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete( String id) {
		try {
			etabChangeManiereRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return  null;
		}
	}
	 

}


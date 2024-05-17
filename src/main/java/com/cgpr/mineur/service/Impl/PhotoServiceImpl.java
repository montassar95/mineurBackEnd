package com.cgpr.mineur.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Photo;
import com.cgpr.mineur.models.PhotoId;
import com.cgpr.mineur.repository.ChangementLieuRepository;
import com.cgpr.mineur.repository.PhotoRepository;
import com.cgpr.mineur.service.PhotoService;


@Service
public class   PhotoServiceImpl  implements PhotoService  {

	
	
	@Autowired
	private PhotoRepository photoRepository;
	
	
	@Override
	public Optional<Photo> getPhotoById(PhotoId photoId) {
		// TODO Auto-generated method stub
		return photoRepository.findById(photoId);
	}


	@Override
	public void save (Photo photos) {
		// TODO Auto-generated method stub
		photoRepository.save(photos);
		
	}

	 
	 
}


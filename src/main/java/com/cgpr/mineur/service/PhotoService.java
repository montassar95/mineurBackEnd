package com.cgpr.mineur.service;

import java.util.Optional;

import com.cgpr.mineur.models.Photo;
import com.cgpr.mineur.models.PhotoId;



public interface PhotoService   {
	 
 	 Optional<Photo>  getPhotoById(PhotoId photoId);
 	 
 	void  save (Photo photos);
	 
}
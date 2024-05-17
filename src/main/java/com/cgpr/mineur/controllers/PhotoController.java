package com.cgpr.mineur.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.PhotoId;
import com.cgpr.mineur.models.Photo;
import com.cgpr.mineur.service.PhotoService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/photos")
public class PhotoController {
	@Autowired
	private PhotoService photoService;

	@GetMapping("/getone/{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<Object> getone(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {
		PhotoId photoId = new PhotoId();
		photoId.setIdEnfant(idEnfant);
		photoId.setNumOrdinaleArrestation(numOrdinaleArrestation);
	    Optional<Photo> photo = photoService.getPhotoById(photoId);
	    
	    if (photo.isPresent()) {
	        return new ApiResponse<>(HttpStatus.OK.value(), "Photo récupérée avec succès", photo.get());
	    } else {
	        return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Photo non trouvée pour l'ID spécifié", null);
	    }
	}

	
	 

	 
}

package com.cgpr.mineur.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.ApiResponseAmenPhotoDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Photo;
import com.cgpr.mineur.models.PhotoId;
import com.cgpr.mineur.service.PhotoService;
import com.cgpr.mineur.service.Impl.AmenPhotoServiceImpl;

import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/photos")
public class PhotoController {
	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private AmenPhotoServiceImpl amenPhotoServiceImpl;
	
	
	@GetMapping("/trouverPhotoByIdDetenuEtNumDetention/{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<Object> trouverPhotoByIdDetenuEtNumDetention(@PathVariable("idEnfant") String idEnfant,
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

	
	@GetMapping("/trouverAmenPhoto/{parameter}")
	public ResponseEntity<ApiResponseAmenPhotoDto> trouverAmenPhoto(@PathVariable String parameter) {
		System.out.println(parameter);
	    // Logger log = LoggerFactory.getLogger(getClass()); // Si tu veux utiliser SLF4J

	    try {
	        ApiResponseAmenPhotoDto response = amenPhotoServiceImpl
	            .callAmenPhotoAPI(parameter.trim())
	            .block(); // attention : bloquant !

	        if (response == null) {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	        }
	        
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        // log.error("Erreur lors de l'appel à AmenPhotoAPI", e);
	        e.printStackTrace(); // à remplacer par du vrai logging en prod
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
}

package com.cgpr.mineur.service.Impl;

import java.util.Base64;

import org.springframework.http.HttpHeaders;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder; // Si vous utilisez NoOpPasswordEncoder
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.cgpr.mineur.dto.ApiResponseAmenPhotoDto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AmenPhotoServiceImpl   {

	 
	
 

	 
	 
	public Mono<ApiResponseAmenPhotoDto> callAmenPhotoAPI(String parameter) {
        String apiUrl = "http://192.168.100.2:8181/AppWebServiceCgprSiege/api/cgpr/photo/IdentiteAmenPhoto/IdentiteAmenImgByGouvPrAnneeMoisJourCodres/" + parameter;

        WebClient client = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + getBasicAuthCredentials())
                .build();

        return client.get()
        	    .accept(MediaType.APPLICATION_JSON)
        	    .retrieve()
        	    .bodyToMono(ApiResponseAmenPhotoDto.class)
        	    .doOnError(error -> {
        	        throw new RuntimeException("Erreur lors de l'appel à l'API ApiResponseAmenPhotoDto : " + error.getMessage());
        	    });
    } 

 

 
	
	   private String getBasicAuthCredentials() {
	        String username = "photo";
	        String password = "photoAxYPhoto2024";
	        String credentials = username + ":" + password;
	        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());
	        return base64Credentials;
	    }
}

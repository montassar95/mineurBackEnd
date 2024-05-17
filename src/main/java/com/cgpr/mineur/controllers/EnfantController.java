package com.cgpr.mineur.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.EnfantVerifieDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.resource.EnfantAddDTO;
import com.cgpr.mineur.resource.EnfantDTO;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.resource.PDFPenaleDTO;
import com.cgpr.mineur.service.EnfantService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/enfant")
public class EnfantController {
	@Autowired
	private EnfantService enfantService;
	
	
	
	
	
	
	
	
////////////////////////////////////////debut redis /////////////////
	 @Autowired
		RedisTemplate redisTemplate;
		
		private static String KEY = "MINEUR";

	@GetMapping("/allredis")
	public List<Residence> getAllEmployee() {
		List<Residence> residanceList;
		residanceList = redisTemplate.opsForHash().values(KEY);
//		System.out.println(residanceList.get(0)); //l'errur ici 
		return residanceList;
	}
	
	
	
	
	//////////////////////////////////////// fin redis /////////////////
 
	@PostMapping("/exportAllEtat/pdf")
	public ResponseEntity<InputStreamResource> exportAllEtat(@RequestBody PDFListExistDTO pDFListExistDTO)
			  {
		 
				ResponseEntity<InputStreamResource> response = enfantService.exportAllEtat(pDFListExistDTO);
				return response;
	}

	@PostMapping("/searchEnfant")
	public ApiResponse<List<Residence>> getEnfants(@RequestBody EnfantDTO enfantDTO) {

		List<Residence> enfantData = enfantService.getEnfants(enfantDTO);
		 

		if (enfantData != null) {

			return new ApiResponse<>(HttpStatus.OK.value(), "enfantData fetched suucessfully", enfantData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "enfantData Not FOund", null);
		}
	}

 

	@PostMapping("/export/pdf")
	public ResponseEntity<InputStreamResource> exportToPDF(@RequestBody PDFPenaleDTO pDFPenaleDTO)   {

		ResponseEntity<InputStreamResource> response = enfantService.exportToPDF(pDFPenaleDTO);
		return response;
	}

	@PostMapping("/exportEtat/pdf")
	public ResponseEntity<InputStreamResource> exportEtatToPDF(@RequestBody PDFListExistDTO pDFListExistDTO)  {

		ResponseEntity<InputStreamResource> response = null;
		try {
			response = enfantService.exportEtatToPDF(pDFListExistDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}

//    private Cache<String, Integer> cache1 = CacheBuilder.newBuilder()
//            .expireAfterWrite(1, TimeUnit.MINUTES) // Définir une durée d'expiration pour les entrées du cache
//            .build();
//
//    @GetMapping("/test")
//    public int test() throws IOException {
//        String cacheKey1 = "myCacheKey1";
//
//        Integer value = cache1.getIfPresent(cacheKey1);
//        if (value != null) {
//        	value++;
//        }
//        if (value == null) {
//            value = 8;
//            cache1.put(cacheKey1, value);
//        }
//
//        return value;
//    }

	@GetMapping("/all")
	public ApiResponse<List<Enfant>> listEtablissement() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant List Fetched Successfully.",
				enfantService.listEtablissement());
	}

	@GetMapping("/charge")
	public ApiResponse<List<List<Residence>>> listCharge() {
		List<List<Residence>> chargeList = enfantService.listCharge();
		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant List Fetched Successfully.", chargeList);
	}

//	@GetMapping("/charge")
//	public ApiResponse<List<List<Residence>>> listCharge() {
//		
//		System.out.println("hi");
//		  ObjectMapper objectMapper = new ObjectMapper();
//		//    objectMapper.setAnnotationIntrospector(new IgnoreJsonIgnoreIntrospector());
//		String residenceJson = null;
//		 
//			try {
//				 
//				residenceJson = objectMapper.writeValueAsString(chargeAllEnfantService.chargeList());
//			} catch (JsonProcessingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return new ApiResponse<>(HttpStatus.OK.value(), "JsonProcessingException.", null);
//			}
//		RapportQuotidien rapportQuotidien = new RapportQuotidien();
//		rapportQuotidien.setDateSauvgarde(LocalDateTime.now());
//		rapportQuotidien.setListResidance(residenceJson);
//		
//		rapportQuotidienRepository.save(rapportQuotidien);
//		return new ApiResponse<>(HttpStatus.OK.value(), " pas des exeption verifier la base .", null);
//	}

	@GetMapping("/getone/{id}")
	public ApiResponse<Enfant> getEnfantById(@PathVariable("id") String id) {
		 Enfant  enfanttData = enfantService.getEnfantById(id);
		 return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfanttData  );
	}
	
	// important!!!!
	@GetMapping("/chercherEnfantAvecVerification/{id}")
	public ApiResponse<EnfantVerifieDto> chercherEnfantAvecVerification(@PathVariable("id") String id) {
		EnfantVerifieDto  enfanttData = enfantService.chercherEnfantAvecVerification(id);
		 
		 return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfanttData  );
	}


	@GetMapping("/getoneInResidence/{id}")
	public ApiResponse<Residence> getoneInResidence(@PathVariable("id") String id) {
		 
		 Residence  enfantData = enfantService.getoneInResidence(id);
	 

	 
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfantData );
		 

		 
	}

	@GetMapping("/getResidenceByNum/{numArr}")
	public ApiResponse<List<Residence>> getResidenceByNum(@PathVariable("numArr") String numArr) {
		List<Residence> enfantData = enfantService.getResidenceByNum(numArr);

		if (enfantData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfantData);
		}

		else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Enfant Not FOund", null);
		}
	}

	@PostMapping("/addEnfant/{idEta}")
	public ApiResponse<Enfant> save(@RequestBody Enfant enfant, @PathVariable("idEta") String idEta) {

		try {

			 

			Enfant e = enfantService.save(enfant, idEta);

			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant saved Successfully", e);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Enfant not saved", null);
		}
	}

	@PostMapping("/addEnfantDTO")
	public ApiResponse<Residence>  save(@RequestBody EnfantAddDTO enfantAddDTO) {
		  try {
       System.out.println(enfantAddDTO.getEnfant().toString());
       System.out.println(enfantAddDTO.getEtablissement().toString());
       System.out.println(enfantAddDTO.getArrestation().toString());
       System.out.println(enfantAddDTO.getResidence().toString());
//       System.out.println(enfantAddDTO.getImg().toString().length());
       
     

			 

       Residence  newResidence  =  enfantService.save(enfantAddDTO);
//System.err.println(newResidence.toString());
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant saved Successfully", newResidence);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Enfant not saved", null);
		}
       
       
       
	}

	
	@PostMapping("/updateEnfantDTO")
	public ApiResponse<Residence> update(@RequestBody EnfantAddDTO enfantAddDTO) {
     
       
       
       
// 	  try {
 	       System.out.println(enfantAddDTO.getEnfant().toString());
 	       System.out.println(enfantAddDTO.getEtablissement().toString());
 	       System.out.println(enfantAddDTO.getArrestation().toString());
 	       System.out.println(enfantAddDTO.getResidence().toString());
// 	       System.out.println(enfantAddDTO.getImg().toString().length());
 	       
 	     

 				 

 	       Residence  newResidence  =  enfantService.update(enfantAddDTO);
 	//System.err.println(newResidence.toString());
 				return new ApiResponse<>(HttpStatus.OK.value(), "Enfant saved Successfully", newResidence);
// 			} catch (Exception e) {
// 				return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Enfant not saved", null);
// 			}
	}
	
	
	
	@PutMapping("/update")
	public ApiResponse<Enfant> update(@RequestBody Enfant enfant) {
		try {

			Enfant e = enfantService.update(enfant);
			return new ApiResponse<>(HttpStatus.OK.value(), "enfant updated successfully.", e);
		} catch (Exception e) {

			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "enfant not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") String id) {
		try {
			enfantService.delete (id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "enfant  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "enfant not Deleted", null);
		}
	}

}

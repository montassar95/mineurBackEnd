//package com.cgpr.mineur.controllers;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.cgpr.mineur.dto.PersonelleDto;
//import com.cgpr.mineur.models.ApiResponse;
//import com.cgpr.mineur.service.PersonelleService;
//
//@CrossOrigin(origins = "*", maxAge = 3600)
//@RestController
//@RequestMapping("/api/personelle")
//public class PersonelleController {
//
//	@Autowired
//	private PersonelleService personelleService;
//
//	@GetMapping("/all")
//	public ApiResponse<List<PersonelleDto>> listPersonelle() {
//		return new ApiResponse<>(HttpStatus.OK.value(), "Personelle List Fetched Successfully.",
//				personelleService.listPersonelle());
//	}
//
//	@PostMapping("/add")
//
//	public ApiResponse<PersonelleDto> save(@RequestBody PersonelleDto personelle) {
//
//		try {
//			return new ApiResponse<>(HttpStatus.OK.value(), "personelle saved Successfully",
//					personelleService.save(personelle));
//		} catch (Exception e) {
//			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "personelle not saved", null);
//		}
//	}
//
//}

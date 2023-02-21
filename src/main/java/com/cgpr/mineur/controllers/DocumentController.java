package com.cgpr.mineur.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArretProvisoireRepository;
import com.cgpr.mineur.repository.DocumentRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/document")
public class DocumentController {

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private AccusationCarteRecupRepository accusationCarteRecupRepository;
	
	@Autowired
	private AccusationCarteDepotRepository accusationCarteDepotRepository;
	
	
	@Autowired
	private AccusationCarteHeberRepository accusationCarteHeberRepository;
	
	@Autowired
	private AccusationCarteHeberRepository accusationCarteHebroRepository;
//	
	
	@Autowired
	private  ArretProvisoireRepository arretProvisoireRepository;
	
	
	@Autowired
	private  AffaireRepository affaireRepository;
	
	
	
	
	
	@GetMapping("/all")
	public ApiResponse<List<Document>> listAffaire() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				(List<Document>) documentRepository.findAll());
	}

	@PostMapping("/findDocumentById")
	public ApiResponse<Document> findDocumentById(@RequestBody DocumentId documentId) {

		System.out.println("==================================documente=========================");
		 Optional<Document>  doc = documentRepository.findById(documentId);

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "    Successfully", doc);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not found", null);
		}

	}

	@GetMapping("/getDocumentByAffaire/{idEnfant}/{numOrdinalArrestation}/{numOrdinalAffaire}")
	public ApiResponse<List<Document>> getDocumentByAffaire(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation,
			@PathVariable("numOrdinalAffaire") long numOrdinalAffaire) {
		try {
			List<Document> aData =  documentRepository.getDocumentByAffaire(idEnfant, numOrdinalArrestation,
                 	numOrdinalAffaire);
			
				System.out.println("*****************************************************");
				System.out.println(aData.toString());
				System.out.println("*****************************************************");
		 
			if (aData.isEmpty()) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not ok", null);
			} else {
			return new ApiResponse<>(HttpStatus.OK.value(), "  ok", aData);
			
			}
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not found", null);
		}
		
	}

	
	
	
	
	
	
	
	
	
	 
	
	@GetMapping("/getTitreAccusation/{idEnfant}/{numOrdinalArrestation}/{numOrdinalAffaire}")
	public ApiResponse<List<TitreAccusation>> getTitreAccusation(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation,
			@PathVariable("numOrdinalAffaire") long numOrdinalAffaire) {
 
		List<Document> aData = documentRepository.getDocumentByAffaireforAccusation(idEnfant, numOrdinalArrestation,
			                                                         	numOrdinalAffaire ,PageRequest.of(0,1) );
		if (aData.isEmpty()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  not ok", null);
		} else {
			 List<TitreAccusation> titreAccusations=null;
		 
			if(aData.get(0) instanceof CarteRecup) {
				System.out.println("CarteRecup.."+aData.get(0).getDocumentId());
				titreAccusations=  accusationCarteRecupRepository.getTitreAccusationbyDocument(aData.get(0).getDocumentId());
//				for (TitreAccusation titreAccusation : titreAccusations) {
//					System.out.println(titreAccusation.getTitreAccusation());
//				}
			}
			else if(aData.get(0) instanceof CarteDepot) {
				titreAccusations=  accusationCarteDepotRepository.getTitreAccusationbyDocument(aData.get(0).getDocumentId());
//				for (TitreAccusation titreAccusation : titreAccusations) {
//					System.out.println(titreAccusation.getTitreAccusation());
//				}
			}
             else if(aData.get(0) instanceof CarteHeber) {
//            	 System.out.println("CarteHeber.."+aData.get(0).getDocumentId());
            		titreAccusations=  accusationCarteHebroRepository.getTitreAccusationbyDocument(aData.get(0).getDocumentId());
			}
			return new ApiResponse<>(HttpStatus.OK.value(), "  ok", titreAccusations);

		}
	}
	
	
	
	
	
	
	@GetMapping("/getDocumentByArrestation/{idEnfant}/{numOrdinalArrestation}")
	public ApiResponse<Object> getDocumentByArrestation(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation) {

		return new ApiResponse<>(HttpStatus.OK.value(), "ok",
				documentRepository.getDocumentByArrestation(idEnfant, numOrdinalArrestation));
	}

	@GetMapping("/countDocumentByAffaire/{idEnfant}/{numOrdinalArrestation}/{numOrdinalAffaire}")
	public ApiResponse<Object> countDocumentByAffaire(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinalArrestation") long numOrdinalArrestation,
			@PathVariable("numOrdinalAffaire") long numOrdinalAffaire) {

		return new ApiResponse<>(HttpStatus.OK.value(), "ok",
				documentRepository.countDocumentByAffaire(idEnfant, numOrdinalArrestation, numOrdinalAffaire));

	}
	
	
	@GetMapping("/findEtatJuridique/{idEnfant}")
	public ApiResponse<Affaire> findByArrestation(@PathVariable("idEnfant") String idEnfant) {
		
		
		 
		List<Affaire> aData = documentRepository.findByArrestation(idEnfant );
		Affaire a = aData.stream()
		            .peek(num -> System.out.println("will filter " + num.getTypeDocument()))
								              .filter(x -> x.getTypeDocument().equals("CD")    || 
									            		   x.getTypeDocument().equals("CH")    ||
									              		   x.getTypeDocument().equals("T")     || 
									              		   x.getTypeDocument().equals("AP")    ||
									              	  	   x.getTypeDocument().equals("AE")   
								              	  )
		              .findFirst()
		              .orElse(null);
		System.out.println(a);
		if (a==null) {
			System.out.println("ma7koum");
			return new ApiResponse<>(HttpStatus.OK.value(), "  ma7koum", null);
		} else {
			System.out.println("maw9ouf");
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "maw9ouf", a);
		}
		
		
		
		
		
		
		
	}
	
	@GetMapping("/findDocumentByArrestation/{idEnfant}/{numOrdinalArrestation}")
	public ApiResponse<Object> findDocumentByArrestation(@PathVariable("idEnfant") String idEnfant,
		                      	@PathVariable("numOrdinalArrestation") long numOrdinalArrestation) {

		return new ApiResponse<>(HttpStatus.OK.value(), "ok",
				(List<Document>)documentRepository.findDocumentByArrestation(idEnfant, numOrdinalArrestation));
	}
	
	
	
	
	
	
	
	@PostMapping("/delete/{type}")
	public ApiResponse<Integer> delete(@RequestBody DocumentId documentId ,@PathVariable("type") String type) {

		 
		try {
			int ref=0;
	System.out.println(documentId.toString());
			
			System.out.println(type);
			
			if(type.contentEquals("CJ")) {
				System.out.println("yess");
				 List<AccusationCarteRecup> listacc =accusationCarteRecupRepository.findByCarteRecup( documentId  );
				 List<ArretProvisoire> listarr =arretProvisoireRepository.findArretProvisoireByCarteRecup( documentId  );
				 
				 if(!listacc.isEmpty()) {
					 
					 for(AccusationCarteRecup acc :  listacc){
						 accusationCarteRecupRepository.delete(acc);
					 }
					
				 }
				 
				 
		         if(!listarr.isEmpty()) {
					 
					 for(ArretProvisoire arr :  listarr){
						 arretProvisoireRepository.delete(arr);
					 }
					
				 }
				 
				
			}
			
			if(type.contentEquals("CD")) {
				 List<AccusationCarteDepot> listacc =accusationCarteDepotRepository.findByCarteDepot( documentId  );
					
				 
				 if(!listacc.isEmpty()) {
					 
					 for(AccusationCarteDepot acc :  listacc){
						 accusationCarteDepotRepository.delete(acc);
					 }
					
				 }
				
			}
			if(type.contentEquals("CH")) {
				 List<AccusationCarteHeber> listaccH =accusationCarteHeberRepository.findByCarteHeber( documentId  );
					
				 
				 if(!listaccH.isEmpty()) {
					 
					 for(AccusationCarteHeber acc :  listaccH){
						 accusationCarteHeberRepository.delete(acc);
					 }
					
				 }
				
			}
			Document thisDoc = documentRepository.getDocument(documentId.getIdEnfant(),
																documentId.getNumOrdinalArrestation(), 
																documentId.getNumOrdinalAffaire(), 
																documentId.getNumOrdinalDocByAffaire());
			
			Document lastDoc = documentRepository.getDocument(documentId.getIdEnfant(),
																	documentId.getNumOrdinalArrestation(), 
																	documentId.getNumOrdinalAffaire(), 
																	documentId.getNumOrdinalDocByAffaire()-1);
			
			System.out.println("111");
			
			if(lastDoc == null) {
					System.out.println("222");
					documentRepository.deleteById(documentId);
					affaireRepository.deleteById(thisDoc.getAffaire().getAffaireId());
					ref=1;
				
			}
 			 
			
			else {
				System.out.println("333");
				if(lastDoc.getAffaire().getAffaireId().equals(thisDoc.getAffaire().getAffaireId()))
				{
						System.out.println("444");
						
						lastDoc.getAffaire().setTypeAffaire(lastDoc.getTypeAffaire());
						lastDoc.getAffaire().setTypeDocument(lastDoc.getTypeDocument());
						
						if(lastDoc.getTypeDocument().contentEquals("CJ")) {
							
							lastDoc.getAffaire().setDaysDiffJuge(((CarteRecup) lastDoc).getDaysDiffJuge());
							lastDoc.getAffaire().setDateDebutPunition(((CarteRecup) lastDoc).getDateDebutPunition());
							lastDoc.getAffaire().setDateFinPunition(((CarteRecup) lastDoc).getDateFinPunition());
								
						}
						else {
//							if(lastDoc.getTypeDocument().contentEquals("T")) {
//								
//									lastDoc.getAffaire().setStatut(2);
//							}
							lastDoc.getAffaire().setDaysDiffJuge(0);
							lastDoc.getAffaire().setDateDebutPunition(null );
							lastDoc.getAffaire().setDateFinPunition(null);
							
							
						}
						
						affaireRepository.save(lastDoc.getAffaire());
						documentRepository.deleteById(documentId);
				}
				else
					
				{
					System.out.println("555");
					documentRepository.deleteById(documentId);
					affaireRepository.deleteById(thisDoc.getAffaire().getAffaireId());
					lastDoc.getAffaire().setStatut(0);
					affaireRepository.save(lastDoc.getAffaire());
					ref=1;
					
				}
			}
		
			System.out.println("666");
			return new ApiResponse<>(HttpStatus.OK.value(), "saved", ref);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}
	
//	@DeleteMapping("/delete/{id}")
//	public ApiResponse<Void> delete(@PathVariable("id") long id) {
//		try {
//			affaireRepository.deleteById(id);
//			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "   Deleted", null);
//		} catch (Exception e) {
//			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not Deleted", null);
//		}
//	}
}

package com.cgpr.mineur.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.cgpr.mineur.config.EnfantPDFExporter;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.EnfantRepository;
import com.cgpr.mineur.resource.EnfantAddDTO;
import com.cgpr.mineur.resource.EnfantDTO;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.resource.PDFPenaleDTO;
import com.cgpr.mineur.service.EnfantService;
import com.ibm.icu.text.ArabicShapingException;
 

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/enfant")
public class EnfantController {
	@Autowired
	private EnfantRepository enfantRepository;  
 

	  private final EnfantService enfantService;

		@Autowired
		private DocumentRepository documentRepository;

  

    public EnfantController(EnfantService enfanttService) {
        this.enfantService = enfanttService;
    }
    
    
    @PostMapping("/searchEnfant")
    public ApiResponse<List<Enfant>> getEnfants(@RequestBody EnfantDTO enfantDTO){
    	System.out.println(enfantDTO.toString());
     
    	List<Residence> enfantData=null ;
    	if(enfantDTO != null) {
    		if(enfantDTO.getDateNaissance() != null) {
    	    	 enfantData = enfantRepository.search(   enfantDTO.getNom().trim(),
    									    			 enfantDTO.getPrenom().trim(),  
    									    			 enfantDTO.getNomPere().trim(),
    									    			 enfantDTO.getNomGrandPere().trim(),
    									    			 enfantDTO.getNomMere().trim(),
    									    			 enfantDTO.getPrenomMere().trim(),
    									    			 enfantDTO.getDateNaissance(),
    									    			 enfantDTO.getSexe().trim());
    	    	}
    	    	else {
    	    		 enfantData = enfantRepository.searchSansDate(  
    	    				                                          enfantDTO.getNom().trim().toString(),
    												    			 enfantDTO.getPrenom().trim().toString(),  
    												    			 enfantDTO.getNomPere().trim().toString(),
    												    			 enfantDTO.getNomGrandPere().trim().toString(),
    												    			 enfantDTO.getNomMere().trim().toString(),
    												    			 enfantDTO.getPrenomMere().trim().toString(),
    												    		 
    												    			 enfantDTO.getSexe().trim().toString());
    	    	 
    	    	}
    	}
    
    	
    		
    	
    	 
        if (enfantData != null) {
         
			return new ApiResponse<>(HttpStatus.OK.value(), "enfantData fetched suucessfully", enfantData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "enfantData Not FOund", null);
		}
    }
    
//    
//	@PostMapping("/search")
//	public ApiResponse<List<Enfant>> search(@RequestBody Enfant enfant) {
//
//		List<Enfant> enfantData = enfantRepository.search(enfant.getNom(), enfant.getPrenom(), enfant.getNomPere(),
//				enfant.getNomGrandPere());
//		if (enfantData != null) {
//			return new ApiResponse<>(HttpStatus.OK.value(), "enfantData fetched suucessfully", enfantData);
//		} else {
//			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "enfantData Not FOund", null);
//		}
//	}
    
    
    
    
    
    
    @PostMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportToPDF(@RequestBody  PDFPenaleDTO pDFPenaleDTO) throws  IOException {
        
    	 //Optional<Enfant>  enfanttData = enfantRepository.findById(id);
         
       
        try {
        	System.out.println(pDFPenaleDTO.toString());
        	 EnfantPDFExporter exporter = new EnfantPDFExporter( );
        	 ByteArrayInputStream bais =	enfantService.export(pDFPenaleDTO);
        	 HttpHeaders headers = new HttpHeaders();
        	 headers.add("Content-Disposition","inline; filename=enfnat.pdf");
        	 
			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bais) );
	 
		} catch (IOException e) {
		 
			e.printStackTrace();
		} catch (ArabicShapingException e) {
			 
			e.printStackTrace();
		} catch (com.itextpdf.text.DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (ResponseEntity<InputStreamResource>) ResponseEntity.status(444);
         
    }
    
    @PostMapping("/exportEtat/pdf")
    public ResponseEntity<InputStreamResource> exportEtatToPDF(@RequestBody  PDFListExistDTO pDFListExistDTO ) throws  IOException {
        
    	   try {
           
           	 EnfantPDFExporter exporter = new EnfantPDFExporter( );
           	 ByteArrayInputStream bais =	enfantService.exportEtat(pDFListExistDTO);
           	 HttpHeaders headers = new HttpHeaders();
           	 headers.add("Content-Disposition","inline; filename=enfnat.pdf");
           	 
   			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bais) );
   	 
   		} catch (IOException e) {
   		 
   			e.printStackTrace();
   		} catch (ArabicShapingException e) {
   			 
   			e.printStackTrace();
   		} catch (com.itextpdf.text.DocumentException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
   		return (ResponseEntity<InputStreamResource>) ResponseEntity.status(444);
            
    }
    
    
    
    
	@GetMapping("/all")
	public ApiResponse<List<Enfant>> listEtablissement() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant List Fetched Successfully.",
				enfantRepository.findAll());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<Enfant> getEnfantById(@PathVariable("id") String id) {
		 Optional<Enfant>  enfanttData = enfantRepository.findById(id);
		if (enfanttData.isPresent()) {
			List<Affaire> aData = documentRepository.findByArrestation(id );
    		Affaire a = aData.stream()
    		            .peek(num -> System.out.println("will filter " + num.getTypeDocument()))
    								              .filter(x -> x.getTypeDocument().equals("CD")  || 
    									            		   x.getTypeDocument().equals("CH")   ||
    									              		   x.getTypeDocument().equals("T")   || 
    									              		   x.getTypeDocument().equals("AP")   ||
    									              	  	   x.getTypeDocument().equals("AE")   ||
    									              	  	   x.getTypeDocument().equals("CP")
    								              	  )
    		              .findFirst()
    		              .orElse(null);
    		System.out.println(a);
    		if (a==null) {
    			System.out.println("ma7koum");
    			enfanttData.get().setEtat("محكوم");
    		} else {
    			System.out.println("maw9ouf");
    			enfanttData.get().setEtat("موقوف");
    		}
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfanttData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Enfant Not FOund", null);
		}
	}
	@GetMapping("/getoneInResidence/{id}")
	public ApiResponse<Enfant> getoneInResidence(@PathVariable("id") String id) {
		System.out.println(id);
		 Optional<Residence>  enfantData = enfantRepository.getoneInResidence(id);
		 System.out.println("getoneInResidence");
		 
			System.out.println(enfantData.toString());
		if (enfantData.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfantData);
    		}
			
		  else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Enfant Not FOund", null);
		}
	}
	
	
	@GetMapping("/getResidenceByNum/{numArr}")
	public ApiResponse<Enfant> getResidenceByNum(@PathVariable("numArr") String numArr) {
		List<Residence>    enfantData = enfantRepository.getResidenceByNum(numArr);
	
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
			 
			String id = enfantRepository.maxId(idEta);
			if(id != null) {
	 
				String aux=(Long.parseLong(id)+1)+"";
				String idResult = (aux.length()==7) ? aux="0"+aux : aux;
			 
				enfant.setId(idResult);
			}
			else {
				enfant.setId(idEta+"000001");
			}
			
			Enfant e = enfantRepository.save(enfant);
		 
			 
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant saved Successfully",e );
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Enfant not saved", null);
		}
	}

	
	
	@PostMapping("/addEnfantDTO")
	public  void save(@RequestBody EnfantAddDTO enfantAddDTO ) {

		 
			
//		 mise a jour code add enfant 
			 
	 
	}  
	
	
	
	@PutMapping("/update")
	public ApiResponse<Enfant> update(@RequestBody Enfant enfant) {
		try {
			System.out.println(enfant.toString());
Enfant e = enfantRepository.save(enfant);
			return new ApiResponse<>(HttpStatus.OK.value(), "enfant updated successfully.",
				e	);
		} catch (Exception e) {
			System.out.println(e.toString());
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "enfant not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") String id) {
		try {
			enfantRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "enfant  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "enfant not Deleted", null);
		}
	}


	
	 
}

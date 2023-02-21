//package com.cgpr.mineur.service;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.net.URL;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javax.persistence.criteria.Predicate;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.PathVariable;
// 
//import com.cgpr.mineur.config.ConfigShaping;
//import com.cgpr.mineur.models.Affaire;
//import com.cgpr.mineur.models.ApiResponse;
//import com.cgpr.mineur.models.Arrestation;
//import com.cgpr.mineur.models.CarteDepot;
//import com.cgpr.mineur.models.CarteHeber;
//import com.cgpr.mineur.models.CarteRecup;
//import com.cgpr.mineur.models.Enfant;
//import com.cgpr.mineur.models.Residence;
//import com.cgpr.mineur.models.TitreAccusation;
//import com.cgpr.mineur.modelsSecurity.ERole;
//import com.cgpr.mineur.modelsSecurity.Role;
//import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
//import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
//import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
//import com.cgpr.mineur.repository.AffaireRepository;
//import com.cgpr.mineur.repository.ArrestationRepository;
//import com.cgpr.mineur.repository.DocumentRepository;
//import com.cgpr.mineur.repository.EnfantRepository;
//import com.cgpr.mineur.repository.ResidenceRepository;
//import com.cgpr.mineur.resource.EnfantDTO;
//import com.cgpr.mineur.resource.PDFListExistDTO;
//import com.cgpr.mineur.resource.PDFPenaleDTO;
//import com.ibm.icu.text.ArabicShapingException;
//import com.itextpdf.layout.borders.DottedBorder;
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.Font.FontFamily;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.pdf.codec.Base64;
// 
//import com.itextpdf.text.Image;
// 
//
//@Service
//public class EnfantServiceImpl implements EnfantService {
//
//    private  final EnfantRepository enfantRepository;
//
//	@Autowired
//	private AffaireRepository affaireRepository;
//
//	
//	@Autowired
//	private DocumentRepository documentRepository;
//	
//	@Autowired
//	private AccusationCarteRecupRepository accusationCarteRecupRepository;
//	
//	@Autowired
//	private AccusationCarteDepotRepository accusationCarteDepotRepository;
//	
//	@Autowired
//	private AccusationCarteHeberRepository accusationCarteHeberRepository;
//
//	@Autowired
//	private ResidenceRepository residenceRepository;
//
//    public EnfantServiceImpl(EnfantRepository enfantRepository) {
//        this.enfantRepository = enfantRepository;
//    }
//
//    public List<Affaire> findByArrestation(  String idEnfant, long numOrdinale) {
//
//		List<Affaire> lesAffaires = affaireRepository.findByArrestationCoroissant(idEnfant, numOrdinale);
//		
//		System.out.println(idEnfant);
//		System.out.println(numOrdinale);
//		System.out.println(lesAffaires.size());
//		
//		List<Affaire> output = 
//				lesAffaires.stream()
//			        .map(s-> {
//			        	
//			        	
//			        	 
//			        	com.cgpr.mineur.models.Document  doc = documentRepository.getLastDocumentByAffaireforAccusation(idEnfant, numOrdinale, s.getNumOrdinalAffaire() );
//	 							
//			        	 
//			        	s.setTypeDocument(doc.getTypeDocument());
//			        	s.setDateEmissionDocument(doc.getDateEmission());
//			        	
//			        	List<com.cgpr.mineur.models.Document> accData = documentRepository.getDocumentByAffaireforAccusation(idEnfant, numOrdinale,
// 							s.getNumOrdinalAffaire() ,PageRequest.of(0,1) );
//			        	
//						List<TitreAccusation> titreAccusations=null;
//						
//						if(accData.get(0) instanceof CarteRecup) {
//						 
//						titreAccusations=  accusationCarteRecupRepository.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
//						
//						
//						s.setTitreAccusations(titreAccusations);
//						s.setDateEmission(accData.get(0).getDateEmission());
//						
//						 CarteRecup c =(CarteRecup) accData.get(0);
//						s.setAnnee(c.getAnnee());
//						s.setMois(c.getMois()); 
//						s.setJour(c.getJour()); 
//						
//						s.setAnneeArret(c.getAnneeArretProvisoire());
//						s.setMoisArret(c.getMoisArretProvisoire()); 
//						s.setJourArret(c.getJourArretProvisoire()); 
//						
//						
//						s.setTypeJuge(c.getTypeJuge());
//					 
//						}
//						else if(accData.get(0) instanceof CarteDepot) {
//						titreAccusations=  accusationCarteDepotRepository.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
//						s.setTitreAccusations(titreAccusations);
//						s.setDateEmission(accData.get(0).getDateEmission());
//						}
//						else if(accData.get(0) instanceof CarteHeber) {
//							titreAccusations=  accusationCarteHeberRepository.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
//							s.setTitreAccusations(titreAccusations);
//							s.setDateEmission(accData.get(0).getDateEmission());	 
//						System.out.println("CarteHeber.."+accData.get(0).getDocumentId());
//						}
//		 	
//			        	
//			                     return s;  
//			                 })
//			       .collect(Collectors.toList());
//		
//	 
//			return  output ;
//		   
//			
//			
//			
//			
//			
//			
//			
//			
//			
//		
//	}
//    
//
//	 
////	public List<Enfant> getEnfants(EnfantDTO e) {
////		  List<Enfant> students = enfantRepository.findAll((Specification<Enfant>) (root, cq, cb) -> {
////	            Predicate p = cb.conjunction();
////	           
////	            if (!StringUtils.isEmpty(e.getNom())) {
////	                p = cb.and(p, cb.like(root.get("nom"), "%" + e.getNom() + "%"));
////	            }
////	            if (!StringUtils.isEmpty(e.getPrenom())) {
////	                p = cb.and(p, cb.like(root.get("prenom"), "%" + e.getPrenom() + "%"));
////	            }
////	            if (!StringUtils.isEmpty(e.getNomPere())) {
////	                p = cb.and(p, cb.like(root.get("nomPere"), "%" + e.getNomPere() + "%"));
////	            }
////	           
////	            if (!StringUtils.isEmpty(e.getNomGrandPere())) {
////	                p = cb.and(p, cb.like(root.get("nomGrandPere"), "%" + e.getNomGrandPere() + "%"));
////	            }
////	            if (!StringUtils.isEmpty(e.getNomMere())) {
////	                p = cb.and(p, cb.like(root.get("nomMere"), "%" + e.getNomMere() + "%"));
////	            }
////	            if (!StringUtils.isEmpty(e.getPrenomMere())) {
////	                p = cb.and(p, cb.like(root.get("prenomMere"), "%" + e.getPrenomMere() + "%"));
////	            }
////	            if (!StringUtils.isEmpty(e.getSexe())) {
////	                p = cb.and(p, cb.like(root.get("sexe"), "%" + e.getSexe() + "%"));
////	            }
////	            return p;
////	        });
////	        return  students;
////	}
//
//	  public static final Font FONT = new Font();
//	    public static final Font BOLD = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
//	    @Override
//	    public ByteArrayInputStream export(  PDFPenaleDTO pDFPenaleDTO  ) throws DocumentException, IOException, ArabicShapingException {
//	    	
//	    	
//	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
//	    	
//	       // Document document = new Document(PageSize.A4 );
////	    	  Rectangle layout = new Rectangle(PageSize.A4.rotate() );
//	        Rectangle layout = new Rectangle(PageSize.A4);
//	        //layout.setBackgroundColor(new BaseColor(100, 200, 180)); //Background color
//	        
////	        layout.setBorderColor(BaseColor.DARK_GRAY);  //Border color
////	        layout.setBorderWidth(7);      //Border width  
////	        layout.setBorder(Rectangle.BOX);  //Border on 4 sides
//	        
//	        Document document = new Document(layout);
//	        PdfWriter.getInstance(document, out );
//
//	        
//	        
//	        
//	        
//	        
//	        
//	     
//	        document.open();
//	        
////	        PdfContentByte canvas =  PdfWriter.getInstance(document, out ).getDirectContentUnder();
////	        Image imageP = Image.getInstance("/images/page.jpg");
////	        imageP.scaleAbsolute(PageSize.A4.rotate());
////	        imageP.setAbsolutePosition(0, 0);
////	        canvas.addImage(imageP);
//
//	        
//	        ConfigShaping boldConf = new ConfigShaping();
//	        
//	        Font boldfontLabelAmiriIndex = boldConf.getFontForArabicAmiri(14);
//	        Font boldfontLabelTop = boldConf.getFontForArabicAmiri1(12);
//	        Font boldfontLabelAmiriDate = boldConf.getFontForArabicAmiri(12);
//	        Font boldfontLabelAmirietoile = boldConf.getFontForArabicAmiri(8);
//	        PdfPTable tab  = new PdfPTable(1);
//		    tab.setWidthPercentage(100);
//	        
//		    Phrase ptab;
//	        PdfPCell ctab;
//	        
//	        ptab =  new Phrase(  boldConf.format("الجمهورية التونسية ") ,boldfontLabelTop);
//	        ctab = new PdfPCell(ptab);
//	        ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	        ctab.setBorder(0);
//	         ctab.setPaddingRight(27f);
//		    tab.addCell(ctab);
//	        
//	        
//		    
//		    
//		    ptab =  new Phrase(  boldConf.format("وزارة العدل ") ,boldfontLabelTop);
//		    ctab = new PdfPCell(ptab);
//		    ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		    ctab.setBorder(0);
//		    ctab.setPaddingRight(36f); 
//		     tab.addCell(ctab);
//		    
//		    
//		     ptab =  new Phrase(  boldConf.format("الهيئة العامة للسجون والإصلاح ") ,boldfontLabelTop);
//		     ctab = new PdfPCell(ptab);
//		     ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		     ctab.setBorder(0);
//		     tab.addCell(ctab);
//		    
//		    
//		    
//		    
//		    
//		    
//		    
//	        PdfPTable tableTop = new PdfPTable(3);
//		    tableTop.setWidthPercentage(100);
//		        
//		        
//		        Phrase p1Top;
//		        PdfPCell c1Top;
//		         
////		        PdfPCell spaceCellTop = new PdfPCell(new Phrase("  "));
////		        spaceCellTop.setBorder(0);
//		      
////		       ---------------  nom --------------------
//		        
//		        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
//		        LocalDate localDate = LocalDate.now();
//		        
//		         p1Top =  new Phrase( boldConf.format(dtf.format(localDate))+" "+boldConf.format("تونس في") ,boldfontLabelTop);
//			     c1Top = new PdfPCell(p1Top);
//			     c1Top.setHorizontalAlignment(Element.ALIGN_LEFT);
//			     c1Top.setBorder(0);
//			     tableTop.addCell(c1Top);
//			  
//		         
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     URL resource = EnfantService.class.getResource("/images/cgpr.png");
//			        
//			     Image image = Image.getInstance(resource);
////		          image.setAbsolutePosition(237f, 750f);
//		          image.scaleAbsolute(70f,70f);
//		          c1Top = new PdfPCell(image);
//				     c1Top.setHorizontalAlignment(Element.ALIGN_CENTER);
//				     
//				     c1Top.setBorder(0);;
//				     
//				     tableTop.addCell(c1Top);
//				  
//				     
// 
//			  
//				     c1Top = new PdfPCell(tab);
//				     c1Top.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				     c1Top.setBorder(0);
//				     tableTop.addCell(c1Top);
//		      
//				     tableTop.setSpacingAfter(18f);
//			     
//				     
//	        
//	        
//	        
//	        
//	        
//	        
//	        
//	        
//	        
//	    
//	        
//	        
//	        
//	        
//	        
//	        
//		    //Titre     
//		   Font boldfontTitle = boldConf.getFontForArabicAmiri(30);
//		   Font boldfontLabel = boldConf.getFontForArabic(16);
//		   Font boldfontFamielle = boldConf.getFontForArabic(14);
//		   Font boldfontLabelEtat = boldConf.getFontForArabic(18);
//		   Font boldfontLabelAmiri = boldConf.getFontForArabicAmiri(17);
//		   Font boldfontLabelAmirix = boldConf.getFontForArabicAmiri(16);
//         PdfPTable tTitre = new PdfPTable(1);	   
//         
//	       Phrase pTitre =  new Phrase(boldConf.format("مذكرة شخصية لطفل جانح" ),boldfontTitle);
//	       PdfPCell cTitre = new PdfPCell(pTitre);
//	       cTitre.setPaddingBottom(20f); 
//	       cTitre.setBorder(Rectangle.BOX);
//	       cTitre.setBorderWidth(2);
//	     //  cTitre.setPaddingLeft(150f); 
//	       
//	       cTitre.setBackgroundColor(new BaseColor(210, 210, 210));
//	      
//	       cTitre.setBorderColor(BaseColor.BLACK);
//	       //cTitre.setBorderWidth(0);
//	      
//	       cTitre.setHorizontalAlignment(Element.ALIGN_CENTER);
//	       
//	      
//	       tTitre.addCell(cTitre);
//	       tTitre.setWidthPercentage(60);
//	       
//	      
//		  if(pDFPenaleDTO.getEnfant().getImg() != null) {
//			  final String base64Data = pDFPenaleDTO.getEnfant().getImg().substring( pDFPenaleDTO.getEnfant().getImg().indexOf(",") + 1);
//			  Image ima = Image.getInstance(Base64.decode(base64Data));
// 	          ima.setAbsolutePosition(30f, 540f);
//	          ima.scaleAbsolute(120f,120f);
//	         	          document.add(ima);
//		  }
////		   tTitre.addCell(ima);
//	       
//	       tTitre.setSpacingAfter(15f);
//	       
//	       
//	       
//	       
//		    //arre    
//			   Font boldfontArr = boldConf.getFontForArabicArr(16);
//			 
//			   
//	         PdfPTable tArr = new PdfPTable(100);	   
//	         
//	         tArr.setWidthPercentage(80);
//		        
//		        
//	            Phrase p1;
//		        PdfPCell c1;
//		         
//		        PdfPCell spaceCell = new PdfPCell(new Phrase("  "));
//		        spaceCell.setBorder(0);
//		      
////		       ---------------  nom --------------------
//		      if(pDFPenaleDTO.getNbrArrestation()<10) {
//		    	  p1 =  new Phrase(boldConf.format("0"+pDFPenaleDTO.getNbrArrestation() +"  /  "+ pDFPenaleDTO.getEnfant().getId() ),boldfontLabelAmiri );
//		      }
//		         
//		         else {
//		        	 p1 =  new Phrase(boldConf.format(pDFPenaleDTO.getNbrArrestation() +"  /  "+ pDFPenaleDTO.getEnfant().getId() ),boldfontLabelAmiri ); 
//		         }
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			     c1.setBorder(0);
//			     c1.setColspan(55);
//			      
//			     tArr.addCell(c1);
//			     p1 =  new Phrase(  boldConf.format(":") ,boldfontArr);
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//			     c1.setBorder(0);
//			     c1.setColspan(5);
//			    
//			     
//			     tArr.addCell(c1);
//		         
//		         p1 =  new Phrase(  boldConf.format("المعــــرف الوحيـــد") ,boldfontArr);
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			     c1.setBorder(0);
//			     c1.setColspan(40);
//			    
//			     
//			     tArr.addCell(c1);
//			     p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getCentre()),boldfontArr);
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			     c1.setBorder(0);
//			     c1.setColspan(55);
//			      
//			     tArr.addCell(c1);
//			     p1 =  new Phrase(  boldConf.format(":") ,boldfontArr);
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//			     c1.setBorder(0);
//			     c1.setColspan(5);
//			    
//			     
//			     tArr.addCell(c1);
//		         
//		         p1 =  new Phrase(  boldConf.format("مــــــركز الإصــــــلاح") ,boldfontArr);
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			     c1.setBorder(0);
//			     c1.setColspan(40);
//			    
//			 
//			     tArr.addCell(c1);
//			    
//			  
//			    
//			     
//			     p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getNumArrestation()),boldfontLabelAmiri);
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			     c1.setBorder(0);
//			     c1.setColspan(55);
//			      
//			     tArr.addCell(c1);
//			  
//			     p1 =  new Phrase(  boldConf.format(":") ,boldfontArr);
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//			     c1.setBorder(0);
//			     c1.setColspan(5);
//			    
//			     
//			     tArr.addCell(c1);
//		         p1 =  new Phrase(  boldConf.format("عـــدد الإيقـــــــــاف") ,boldfontArr);
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			     c1.setBorder(0);
//			     c1.setColspan(40);
//			    
//			     
//			     tArr.addCell(c1);
//			     p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getArrestation().getNumAffairePricipale().toString()),boldfontLabelAmiri);
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			     c1.setBorder(0);
//			     c1.setColspan(55);
//			      
//			     tArr.addCell(c1);
//			     p1 =  new Phrase(  boldConf.format(":") ,boldfontArr);
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//			     c1.setBorder(0);
//			     c1.setColspan(5);
//			    
//			     
//			     tArr.addCell(c1);
//		         
//			     if(pDFPenaleDTO.getNbrAffaires()<10) {
//			    	 p1 =  new Phrase(  boldConf.format("ــدد القضيــــــة") + boldConf.format("0"+pDFPenaleDTO.getNbrAffaires())+boldConf.format("عـــــ")  ,boldfontLabelAmirix);
//					   
//			     }
//			     else {
//			    	 p1 =  new Phrase(  boldConf.format("ـــــدد القضيــــــة") + boldConf.format(pDFPenaleDTO.getNbrAffaires()+"")+boldConf.format("عـــــ")  ,boldfontLabelAmirix);
//					    	 
//			     }
//		          c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			     c1.setBorder(0);
//			     c1.setColspan(40);
//			    
//			     
//			     tArr.addCell(c1);
//			    
//			     
//			     p1 =  new Phrase(  
//			    		 pDFPenaleDTO.getAnneeAge().toString() +" "+
//			    		          pDFPenaleDTO.getMoisAge().toString() ,boldfontLabelAmiri);
//			     
//		         c1 = new PdfPCell(p1);
//		         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			     
//			     c1.setBorder(0);
//			    
//			     c1.setColspan(49);
//			      
//			     tArr.addCell(c1);
//			     
//			     p1 =  new Phrase(  
//			                      pDFPenaleDTO.getJourAge().toString(),boldfontLabelAmiri);
//			     
//		         c1 = new PdfPCell(p1);
//		         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			     
//			     c1.setBorder(0);
//			    
//			     c1.setColspan(06);
//			      
//			     tArr.addCell(c1);
//			     
//			     p1 =  new Phrase(  boldConf.format(":") ,boldfontArr);
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//			     c1.setBorder(0);
//			     c1.setColspan(5);
//			    
//			     
//			     tArr.addCell(c1);
//			     
//			     
//			     
// 
//			     
//			//     p1 =  new Phrase(  boldConf.format(" ســــن الرشـــــــد") ,boldfontArr);
//		    	 p1 =  new Phrase(  boldConf.format("ـــن الرشـــــــــد") + boldConf.format(pDFPenaleDTO.getAgeEnfant().toString().trim())+boldConf.format("ســـــ")  ,boldfontLabelAmirix);
//		 		
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			     c1.setBorder(0);
//			     c1.setColspan(40);
//			    
//			     
//			     tArr.addCell(c1);
//			      
//			     
//			    
//			     tArr.setSpacingAfter(15f);
//			   
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//	         PdfPTable table = new PdfPTable(100);
//	       // table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//	        table.setWidthPercentage(100);
//	        
//	        
//	      
//	      
////	       ---------------  nom --------------------
//	      
//	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getNom()+" بن "+ pDFPenaleDTO.getEnfant().getNomPere()+" بن "+ pDFPenaleDTO.getEnfant().getNomGrandPere()+" "+ pDFPenaleDTO.getEnfant().getPrenom()),boldfontLabelAmiri);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		     c1.setBorder(0);
//		     c1.setColspan(65);  
//		     c1.setPaddingBottom(7f); 
//		     table.addCell(c1); 
//		  
//	         
//		     p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     
//		     table.addCell(c1);
//		     
//	         p1 =  new Phrase(  boldConf.format("الهـــــــــــــــــــــــــــــــــــوية") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		     c1.setBorder(0);
//		     c1.setColspan(30);
//		    
//		     c1.setPaddingBottom(7f); 
//		     table.addCell(c1);
//		                                       
//		     
//		 
//		    
//		     
////		       ---------------  mere --------------------  
//		     
//	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getNomMere()+" "+ pDFPenaleDTO.getEnfant().getPrenomMere()),boldfontLabelAmiri);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//		     
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//		     
//	         p1 =  new Phrase(boldConf.format("إبــــــــــــــــــــــــــــــــــــــن " ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	          
//		 
//
////		       ---------------   Lieu et lieu --------------------	    
//	         
//	         p1 =  new Phrase(boldConf.format("بــــــــــــــــــــ"+ pDFPenaleDTO.getEnfant().getLieuNaissance()  )+" "+ pDFPenaleDTO.getEnfant().getDateNaissance(),boldfontLabelAmiri);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         
//	         p1 =  new Phrase(boldConf.format("تــــــاريخ الـولادة ومكانهـــــا" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	          
//		   
//	         
//	         
////		       ---------------  nationnalite --------------------      
//	         
//	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getNationalite().getLibelle_nationalite().toString() ),boldfontLabelAmiri);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         
//	         p1 =  new Phrase(boldConf.format("الجنسيــــــــــــــــــــــــــــــة" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
////		       ---------------  situation fam --------------------      
//	         
//	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getSituationFamiliale().getLibelle_situation_familiale().toString() ),boldfontLabelAmiri);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         
//	         p1 =  new Phrase(boldConf.format("الحـــــــــالة العائليـــــــــــــة" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);	   
//	         
////		       --------------- adress --------------------      
//	         System.out.println(pDFPenaleDTO.getEnfant().getAdresse().toString().trim());
//	         p1 =  new Phrase(  
//	                   
//	        	     	
//	        	     	 
//	        	     	 pDFPenaleDTO.getEnfant().getAdresse().toString().trim() 
//	        	     	 +" "+
//	        	     	 pDFPenaleDTO.getEnfant().getDelegation().getLibelle_delegation().toString()
//	        	     	 +" "+
//	        	     	 pDFPenaleDTO.getEnfant().getGouvernorat().getLibelle_gouvernorat().toString()	
//	        		 
//	        		  ,boldfontLabelAmiri);
//	         c1 = new PdfPCell(p1);
//	         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//		        c1 .setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         p1 =  new Phrase(boldConf.format("مكان الإقــامة قبـل الإيقــاف" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);	 
//	         
////		       --------------- prof --------------------      
//	         
////	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getLieuNaissance().toString() ),boldfontLabelAmiri);
////	         c1 = new PdfPCell(p1);
////	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
////	         c1.setBorder(0);
////	         c1.setColspan(65);
////	         c1.setPaddingBottom(7f); 
////	         table.addCell(c1);
////	         
////	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
////		     c1 = new PdfPCell(p1);
////		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
////		     c1.setBorder(0);
////		     c1.setColspan(5);
////		    
////		     table.addCell(c1);
////		     
////	         
////	         p1 =  new Phrase(boldConf.format("المهنـــــــــــــــــــــــــــــــــة" ),boldfontLabel);
////	         c1 = new PdfPCell(p1);
////	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
////	         c1.setBorder(0);
////	         c1.setColspan(30);
////	         c1.setPaddingBottom(7f); 
////	         table.addCell(c1);	 		
//
////		       --------------- niveau edu --------------------      
//	         
//	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getNiveauEducatif().getLibelle_niveau_educatif().toString() ),boldfontLabelAmiri);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         
//	         p1 =  new Phrase(boldConf.format("المستــوى التعليمـــــــــــي" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);	 	         
//	         
////		       --------------- situation penal --------------------      
//	       
//	         if(pDFPenaleDTO.getArrestation().getEtatJuridique().equals("juge") ){
//	        	  p1 =  new Phrase(boldConf.format( "محكــــــــــــــوم"  ),boldfontLabelEtat);
//	        		 }
//	         else if(pDFPenaleDTO.getArrestation().getEtatJuridique().equals("arret") ) {
//	        	 
//	        	 if(pDFPenaleDTO.isAppelParquet()) {
//	        		 p1 =  new Phrase(boldConf.format(pDFPenaleDTO.getDateAppelParquet())+boldConf.format("موقـــوف  طعــن النيابـة بالاستئناف "),boldfontLabelEtat);
//	        	 }
//	        	 else if(pDFPenaleDTO.isAppelEnfant() && !pDFPenaleDTO.isAppelParquet()) {
//	        		 p1 =  new Phrase(boldConf.format(pDFPenaleDTO.getDateAppelEnfant())+boldConf.format("موقـــوف   طعــن الطفــل بالاستئناف "),boldfontLabelEtat);
//	        	 }
//         else if(!pDFPenaleDTO.isAppelParquet() && !pDFPenaleDTO.isAppelEnfant()) {
//        	 p1 =  new Phrase(boldConf.format("موقــــــــــــــوف" ),boldfontLabelEtat);
//	        	 }
//	        	 
//	        	 
//	        	 
//	         }
//	         
//	         else if(pDFPenaleDTO.getArrestation().getEtatJuridique().equals("libre")) {
//	        	  p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getArrestation().getLiberation().getCauseLiberation().getLibelleCauseLiberation().toString() ),boldfontLabelEtat);
//	         }
//	         else {
//	        	  p1 =  new Phrase(boldConf.format("--"),boldfontLabelEtat);
//	         }
//	        
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         p1 =  new Phrase(boldConf.format("الوضعيــــــة الجزائيــــــــــة" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(12f); 
//	         table.addCell(c1);	         
//	         
////		       --------------- jugee--------------------      
//  if(! (pDFPenaleDTO.getAnneePenal()==0 && pDFPenaleDTO.getMoisPenal()==0 && pDFPenaleDTO.getJourPenal()==0)) {
//	        	 
//	        
//	         String jugeA=" " ;
//	         String jugeM=" " ;
//	         String jugeJ=" " ;
//	       
//	        	 
//	        	 if(pDFPenaleDTO.getAnneePenal()!=0 ){
//	        		 if(pDFPenaleDTO.getAnneePenal() == 1 ) {
//	        			 jugeA= " "+"عام"+" ";
//	        		 }
//	        		 else if(pDFPenaleDTO.getAnneePenal() == 2) {
//	        			 jugeA= " "+"عامين"+" ";
//	        		 }
//	        		 else if((pDFPenaleDTO.getAnneePenal() >= 3) && (pDFPenaleDTO.getAnneePenal() <= 10)) {
//	        			 jugeA=pDFPenaleDTO.getAnneePenal()+" "+"أعوام"+" ";
//	        		 }
//	        		 else {
//	        			 jugeA=pDFPenaleDTO.getAnneePenal()+" "+"عام"+" ";
//	        		 }
//	        	
//	        	 if(pDFPenaleDTO.getMoisPenal()!=0 || pDFPenaleDTO.getJourPenal() !=0) {
//	        		 jugeA=jugeA+ " و ";
//	        	 }
//	              }
//	        	 if(pDFPenaleDTO.getMoisPenal()!=0 ){
//	        		 if(pDFPenaleDTO.getMoisPenal()==1  ) {
//	        			 jugeM= " "+"شهر"+" ";
//	        		 }
//	        		 else if(pDFPenaleDTO.getMoisPenal()==2) {
//	        			 jugeM= " "+"شهرين"+" ";
//	        		 }
//	        		 else if((pDFPenaleDTO.getMoisPenal() >= 3) && (pDFPenaleDTO.getMoisPenal() <= 10)) {
//	        			 jugeM=pDFPenaleDTO.getMoisPenal()+" "+"أشهر"+" ";
//	        		 }
//	        		 else {
//	        			 jugeM=pDFPenaleDTO.getMoisPenal()+"  "+"شهر"+" ";
//	        		 }
//	        	
//		        	
//		        	 if( pDFPenaleDTO.getJourPenal() !=0) {
//		        		 jugeM=jugeM+ " و ";
//		        	 }
//		              }
//	        	 if(pDFPenaleDTO.getJourPenal()!=0 ){
//	        		 if(pDFPenaleDTO.getJourPenal()==1 ) {
//	        			 jugeJ= " "+"يوم "+" ";
//	        		 }
//	        		 else if(pDFPenaleDTO.getJourPenal()==2) {
//	        			 jugeJ= " "+"يومين"+" ";
//	        		 }
//	        		 else if((pDFPenaleDTO.getJourPenal() >= 3) && (pDFPenaleDTO.getJourPenal() <= 10)) {
//	        			 jugeJ=pDFPenaleDTO.getJourPenal()+" "+"أيام"+" ";
//	        		 }
//	        		 else {
//	        			 jugeJ=pDFPenaleDTO.getJourPenal()+"  "+"يوم"+" ";
//	        		 }
//		        	
//		              }
//	         
//	         
//	         
//	         
//	         
//	         
//	         
//	         
//	       //  juge=  pDFPenaleDTO.getAnneePenal()+" "+pDFPenaleDTO.getMoisPenal()+" "+pDFPenaleDTO.getJourPenal()+" ";
//	         
//	         p1 =  new Phrase(boldConf.format( jugeJ)+ boldConf.format( jugeM) + boldConf.format( jugeA) ,boldfontLabelAmiri);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         p1 =  new Phrase(boldConf.format("الحكــــــــــــــــــــــــــــــــــم" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);	 	         
//  }
////		       --------------- Lieu jugee--------------------      
//	         
//	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getDateJugementPrincipale().toString() ),boldfontLabelAmiri);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		    if((pDFPenaleDTO.getAnneePenal()==0 && pDFPenaleDTO.getMoisPenal()==0 && pDFPenaleDTO.getJourPenal()==0) &&(pDFPenaleDTO.getArrestation().getEtatJuridique().equals("arret") )) {
//		    
//		    	 p1 =  new Phrase(boldConf.format("تـــــــــــاريخ الإيقـــــــــــــاف" ),boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		         c1.setBorder(0);
//		         c1.setColspan(30);
//		         c1.setPaddingBottom(7f); 
//		         table.addCell(c1);
//		    }
//		    else {
//		    	 p1 =  new Phrase(boldConf.format("تـــــــــــاريخ الحكــــــــــــــم" ),boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		         c1.setBorder(0);
//		         c1.setColspan(30);
//		         c1.setPaddingBottom(7f); 
//		         table.addCell(c1);
//		    }
//	        		   
//	         
////		       --------------- tribunal--------------------    
//		   
////		   int sizeTribunal=  pDFPenaleDTO.getArrestation().getTribunalPricipale().getNom_tribunal().length() ;
////		   System.err.println(sizeTribunal);
// 		    p1 =  new Phrase("("+pDFPenaleDTO.getArrestation().getTypeAffairePricipale().getLibelle_typeAffaire()+")",boldfontFamielle);
//		       c1 = new PdfPCell(p1);
//	         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(25);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         p1 =  new Phrase( pDFPenaleDTO.getArrestation().getTribunalPricipale().getNom_tribunal().toString() ,boldfontLabelAmiri);
//	         c1 = new PdfPCell(p1);
//	         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(40);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         
//	         p1 =  new Phrase(boldConf.format("المحكمـــــــــــــــــــــــــــــة" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);		   
//
//	         
////		       --------------- tribunal--------------------      
//	     System.out.println(pDFPenaleDTO.getAccu().toString().trim().length());
//	     System.out.println(pDFPenaleDTO.getAccu().toString().trim());
//	     
//	     
//	     
//	     
//	         p1 =  new Phrase(  pDFPenaleDTO.getAccu().toString().trim()  ,boldfontLabelAmirix);
//	      
//	         c1 = new PdfPCell(p1);
//	         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//	        c1 .setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         
//	         p1 =  new Phrase(boldConf.format("التهمـــــــــــــــــــــــــــــــــة" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);		
//	         
//	         
//	         
////		       --------------- tribunal--------------------      
//	         String string = pDFPenaleDTO.getArrestation().getDate().toString();
//	         String[] parts = string.split("-");
//	         String part1 = parts[0];  
//	         String part2 = parts[1];  
//	         String part3 = parts[2]; 
//	         String date = part3+"-"+part2+"-"+part1;
//	         p1 =  new Phrase(boldConf.format( date ),boldfontLabelAmiri);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         p1 =  new Phrase(boldConf.format("تــــاريخ الإيـــداع بالمــــركز" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);		
//	         
//	         
//	         
////		       --------------- tribunal--------------------      
//	         
//	         if(pDFPenaleDTO.getDateDebut() != null) {
//	        	 p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getDateDebut().toString() ),boldfontLabelAmiri);
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		         c1.setBorder(0);
//		         c1.setColspan(65);
//		         c1.setPaddingBottom(7f); 
//		         table.addCell(c1);
//		         
//	        
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         p1 =  new Phrase(boldConf.format("تاريخ بداية العقـــــــــــــــاب" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);		
//	         }
//	         
////		       --------------- tribunal--------------------      
//	         
//	       if( pDFPenaleDTO.getDateFin() != null && !pDFPenaleDTO.isAppelParquet()) {
//	    	   p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getDateFin().toString()  ),boldfontLabelAmiri);
//		         
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		         c1.setBorder(0);
//		         c1.setColspan(65);
//		         c1.setPaddingBottom(7f); 
//		         
//		         table.addCell(c1);
//	      
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         
//	         p1 =  new Phrase(boldConf.format("تاريخ الســـــــــــــــــــــــراح" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);		
//	       }
//	         
////		       --------------- tribunal--------------------      
//	         
//	         
//	         if( !(pDFPenaleDTO.getAnneeArret()==0 && pDFPenaleDTO.getMoisArret()==0 && pDFPenaleDTO.getJourArret()==0)) {
//	        	  
//	         
//	         String arretA=" " ;
//	         String arretM=" " ;
//	         String arretJ=" " ;
//	    
//	        	 
//	        	 if(pDFPenaleDTO.getAnneeArret()!=0 ){
//	        		 if(pDFPenaleDTO.getAnneeArret()==1   ) {
//	        			 arretA= " "+"عام"+" ";
//	        		 }
//	        		 else if(pDFPenaleDTO.getAnneeArret()==2) {
//	        			 arretA= " "+"عامين"+" ";
//	        		 }
//	        		 else if((pDFPenaleDTO.getAnneeArret() >= 3) && (pDFPenaleDTO.getAnneeArret() <= 10)) {
//	        			 arretA=pDFPenaleDTO.getAnneeArret()+" "+"أعوام"+" ";
//	        		 }
//	        		 else {
//	        			 arretA=pDFPenaleDTO.getAnneeArret()+" "+"عام"+" ";
//	        		 }
//	        	
//	        	 if(pDFPenaleDTO.getMoisArret()!=0 || pDFPenaleDTO.getJourArret() !=0) {
//	        		 arretA=arretA+ " و ";
//	        	 }
//	              }
//	        	 
//	        	 if(pDFPenaleDTO.getMoisArret()!=0 ){
//	        		 if(pDFPenaleDTO.getMoisArret()==1  ) {
//	        			 arretM= " "+"شهر"+" ";
//	        		 }
//	        		 else if(pDFPenaleDTO.getMoisArret()==2) {
//	        			 arretM= " "+"شهرين"+" ";
//	        		 }
//	        		 else if((pDFPenaleDTO.getMoisArret() >= 3) && (pDFPenaleDTO.getMoisArret() <= 10)) {
//	        			 arretM=pDFPenaleDTO.getAnneeArret()+" "+"أشهر"+" ";
//	        		 }
//	        		 else {
//	        			 arretM=pDFPenaleDTO.getMoisArret()+"  "+"شهر"+" ";
//	        		 }
//	        	
//		        	
//		        	 if( pDFPenaleDTO.getJourArret() !=0) {
//		        		 arretM=arretM+ " و ";
//		        	 }
//		              }
//	        	 
//	        	 if(pDFPenaleDTO.getJourArret()!=0 ){
//	        		 if(pDFPenaleDTO.getJourArret()==1 ) {
//	        			 arretJ= " "+"يوم "+" ";
//	        		 }
//	        		 else if(pDFPenaleDTO.getJourArret()==2) {
//	        			 arretJ= " "+"يومين"+" ";
//	        		 }
//	        		 else if((pDFPenaleDTO.getJourArret() >= 3) && (pDFPenaleDTO.getJourArret() <= 10)) {
//	        			 arretJ=pDFPenaleDTO.getAnneeArret()+" "+"أيام"+" ";
//	        		 }
//	        		 else {
//	        			 arretJ=pDFPenaleDTO.getJourArret()+"  "+"يوم"+" ";
//	        		 }
//		        	
//		              }
//	         
//	         
//	         
//	         
//	         
//	         
//	         
//	         
//	       //  arret=  pDFPenaleDTO.getAnneePenal()+" "+pDFPenaleDTO.getMoisPenal()+" "+pDFPenaleDTO.getJourPenal()+" ";
//	         
//	         p1 =  new Phrase(boldConf.format( arretJ)+boldConf.format( arretM)+boldConf.format( arretA),boldfontLabelAmiri);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         p1 =  new Phrase(boldConf.format("إيقــــــاف تحفظـــــــــــــــي" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);		
//	         
//	         
//	         }
////	         else {
////	        	 p1 =  new Phrase(boldConf.format( "لا يــــوجــــــــــــــــد "),boldfontLabelAmiri);
////		         c1 = new PdfPCell(p1);
////		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
////		         c1.setBorder(0);
////		         c1.setColspan(65);
////		         c1.setPaddingBottom(7f); 
////		         table.addCell(c1);
////		         
////		         
////		         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
////			     c1 = new PdfPCell(p1);
////			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
////			     c1.setBorder(0);
////			     c1.setColspan(5);
////			    
////			     table.addCell(c1);
////			     
////		         p1 =  new Phrase(boldConf.format("إيقــــــاف تحفظـــــــــــــــي" ),boldfontLabel);
////		         c1 = new PdfPCell(p1);
////		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
////		         c1.setBorder(0);
////		         c1.setColspan(30);
////		         c1.setPaddingBottom(7f); 
////		         table.addCell(c1);	
////		        
////	         }
//	         
//	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getClassePenale().getLibelle_classe_penale().toString() ),boldfontLabelAmiri);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         
//	         p1 =  new Phrase(boldConf.format("العقوبــــــات السابقــــــــــة" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);		
//	         
//	         
////		       --------------- tribunal--------------------    
//	        if(pDFPenaleDTO.getNumTotaleEchappe() !=0 && pDFPenaleDTO.getNumTotaleRecidence() !=0) {
//	        	 
//	        	  p1 =  new Phrase(boldConf.format( "فــــــرار " )+ boldConf.format(" "+pDFPenaleDTO.getNumTotaleEchappe()+" ")+ boldConf.format(" و ")+ boldConf.format( "نقــــــل " )+ boldConf.format(" "+pDFPenaleDTO.getNumTotaleRecidence()+" "),boldfontLabelAmiri);
//	         
//	       
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(65);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);
//	         
//	         
//	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     c1.setBorder(0);
//		     c1.setColspan(5);
//		    
//		     table.addCell(c1);
//		     
//	         p1 =  new Phrase(boldConf.format("المـــــــلاحظـــــــات " ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorder(0);
//	         c1.setColspan(30);
//	         c1.setPaddingBottom(7f); 
//	         table.addCell(c1);	
//	         
//	        }
//	         
//	         PdfPTable tableAffaire = new PdfPTable(100);
//	        
//	         tableAffaire.setWidthPercentage(100);
//	        
//	        
//	         PdfPTable tableLien = new PdfPTable(100);
//		       // table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//	         tableLien.setWidthPercentage(80);
//	          
//		        
//		      
//		      
////		       ---------------  nom --------------------
//		      
//		         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getNom()+" بن "+ pDFPenaleDTO.getEnfant().getNomPere()+" بن "+ pDFPenaleDTO.getEnfant().getNomGrandPere()+" "+ pDFPenaleDTO.getEnfant().getPrenom()),boldfontLabelAmiri);
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//			     
//			     c1.setColspan(100);  
//			     c1.setBorderWidth(2);
//			     c1.setBorder(Rectangle.BOX);
//			     c1.setBackgroundColor(new BaseColor(210, 210, 210));
//			     c1.setPaddingBottom(7f); 
//			      
//			     tableLien.addCell(c1);
//			     
//			     p1 =  new Phrase(" ")  ;
//			     c1 = new PdfPCell(p1);
//			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//			     c1.setBorder(0);
//			     c1.setColspan(100);  
//			     
//			     
//			      
//			     tableLien.addCell(c1);
//	         List<Affaire> affaireAffiche= findByArrestation(pDFPenaleDTO.getEnfant().getId(),   pDFPenaleDTO.getArrestation().getArrestationId().getNumOrdinale());
//	         
//	         for (int i=0;i<affaireAffiche.size();i++) {
//	        	 p1 =  new Phrase(boldConf.format(" "),boldfontLabel);
////		         c1 = new PdfPCell(p1);
////		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
////		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
////		         c1.setRowspan(6);
////		         tableAffaire.addCell(c1);
//		         
//	        	 p1 =  new Phrase( " " ,boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setBorder(0);
//		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		        // c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(70);
//		         tableAffaire.addCell(c1);
//		       
//
//		         p1 =  new Phrase( (i+1) +" "+boldConf.format(" العدد الرتبي للقضية : ") ,boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(30);
//		         tableAffaire.addCell(c1);
//		         
//		         p1 =  new Phrase(boldConf.format( affaireAffiche.get(i).getAffaireId().getNumAffaire()) +" "+ affaireAffiche.get(i).getTribunal().getNom_tribunal()  ,boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		         // c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(70);
//		         tableAffaire.addCell(c1);
//		         p1 =  new Phrase( boldConf.format("  القضية") ,boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(30);
//		         tableAffaire.addCell(c1);
//		         
////		         p1 =  new Phrase(  boldConf.format(affaireAffiche.get(i).getTribunal().getNom_tribunal())  ,boldfontLabelAmiri);
////		         c1 = new PdfPCell(p1);
////		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
////		       //  c1.setBackgroundColor(new BaseColor(210, 210, 210));
////		         c1.setColspan(70);
////		         tableAffaire.addCell(c1);
////		         
////		         p1 =  new Phrase( boldConf.format("المحكمة")  ,boldfontLabel);
////		         c1 = new PdfPCell(p1);
////		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
////		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
////		         c1.setColspan(30);
////		         tableAffaire.addCell(c1);
//		         switch (affaireAffiche.get(i).getTypeDocument()) {
//					case "CD":
//						 p1 =  new Phrase( boldConf.format("بطاقة إيداع")  ,boldfontLabelAmiri);
//						 
//                      break;
//                
//					case "CH":
//						 p1 =  new Phrase( boldConf.format("بطاقة إيواء")  ,boldfontLabelAmiri);
//
//						break;
//						
//					case "AEX":
//						 p1 =  new Phrase( affaireAffiche.get(i).getDateEmissionDocument().toString()  +" "+ boldConf.format("إيقاف تنفيذ الحكم") ,boldfontLabelAmiri);
//
//						break;
//					case "CJ":
//						 p1 =  new Phrase( boldConf.format("مضمون حكم")  ,boldfontLabelAmiri);
//						 
//                     break;
//               
//					case "T":
//						 p1 =  new Phrase(  affaireAffiche.get(i).getDateEmissionDocument().toString() +" "+boldConf.format("إحالة")    ,boldfontLabelAmiri);
//
//						break;
//						
//					case "AE":
//						 p1 =  new Phrase( affaireAffiche.get(i).getDateEmissionDocument().toString()  +" "+  boldConf.format("طعن الطفل بالاستئناف")  ,boldfontLabelAmiri);
//
//						break;	
//						
//					case "AP":
//						 p1 =  new Phrase(  affaireAffiche.get(i).getDateEmissionDocument().toString()+" "+ boldConf.format("طعن النيابة بالاستئناف")    ,boldfontLabelAmiri);
//
//						break;
//						
//					case "CR":
//						 p1 =  new Phrase(  affaireAffiche.get(i).getDateEmissionDocument().toString()   +" "+boldConf.format("مراجعة")  ,boldfontLabelAmiri);
//
//						break;	
//					default:
//						 p1 =  new Phrase( boldConf.format("--")  ,boldfontLabelAmiri);
//					 
//					}
//		         
//		         
//		        
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     //    c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(70);
//		         tableAffaire.addCell(c1);
//		         
//		         p1 =  new Phrase( boldConf.format("  نوع الوثيقة")  ,boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(30);
//		         tableAffaire.addCell(c1);
//		         
//		         
//		         
//		         
//		         p1 =  new Phrase(  affaireAffiche.get(i).getDateEmission().toString()   ,boldfontLabelAmiri);
//		         c1 = new PdfPCell(p1);
//		         
//		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		     //    c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(70);
//		         tableAffaire.addCell(c1);
//		         
//		         String labelAffair="";
//		         if(affaireAffiche.get(i).getTypeDocument().toString().equals("CD")   || affaireAffiche.get(i).getTypeDocument().toString().equals("CH") 
//		        		 || affaireAffiche.get(i).getTypeDocument().toString().equals("T") ) {
//		        	 labelAffair="تاريخ صدور البطاقة ";
//	        	 }
//		         else{
//		        	 labelAffair="تاريخ الحكم";
//		         }
//		         
//		         p1 =  new Phrase( boldConf.format(labelAffair)  ,boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(30);
//		         tableAffaire.addCell(c1);
//		         
//		        
//		         
//		         
//		         
//		         String titreAccusationSring = " ";
//		         for (TitreAccusation  titreAccusation : affaireAffiche.get(i).getTitreAccusations()) {
//		        	 titreAccusationSring=titreAccusationSring+titreAccusation.getTitreAccusation()+" ";
//		        	 
//				}
//		         p1 =  new Phrase( titreAccusationSring.trim()   ,boldfontLabelAmirix);
//		        
//		         c1 = new PdfPCell(p1);
//		         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		         //  c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(70);
//		         tableAffaire.addCell(c1);
//		         p1 =  new Phrase( boldConf.format("التهمة ")  ,boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(30);
//		         tableAffaire.addCell(c1);
//		         
//		         
//		         if(! (affaireAffiche.get(i).getAnnee()==0 && affaireAffiche.get(i).getMois()==0 && affaireAffiche.get(i).getJour()==0)) {
//		        	 
//		 	        
//			         String jugeA=" " ;
//			         String jugeM=" " ;
//			         String jugeJ=" " ;
//			       
//			        	 
//			        	 if(affaireAffiche.get(i).getAnnee()!=0 ){
//			        		 if(affaireAffiche.get(i).getAnnee() == 1 ) {
//			        			 jugeA= " "+"عام"+" ";
//			        		 }
//			        		 else if(affaireAffiche.get(i).getAnnee() == 2) {
//			        			 jugeA= " "+"عامين"+" ";
//			        		 }
//			        		 else if((affaireAffiche.get(i).getAnnee() >= 3) && (affaireAffiche.get(i).getAnnee() <= 10)) {
//			        			 jugeA=affaireAffiche.get(i).getAnnee()+" "+"أعوام"+" ";
//			        		 }
//			        		 else {
//			        			 jugeA=affaireAffiche.get(i).getAnnee()+" "+"عام"+" ";
//			        		 }
//			        	
//			        	 if(affaireAffiche.get(i).getMois()!=0 || affaireAffiche.get(i).getJour() !=0) {
//			        		 jugeA=jugeA+ " و ";
//			        	 }
//			              }
//			        	 if(affaireAffiche.get(i).getMois()!=0 ){
//			        		 if(affaireAffiche.get(i).getMois()==1  ) {
//			        			 jugeM= " "+"شهر"+" ";
//			        		 }
//			        		 else if(affaireAffiche.get(i).getMois()==2) {
//			        			 jugeM= " "+"شهرين"+" ";
//			        		 }
//			        		 else if((affaireAffiche.get(i).getMois() >= 3) && (affaireAffiche.get(i).getMois() <= 10)) {
//			        			 jugeM=affaireAffiche.get(i).getMois()+" "+"أشهر"+" ";
//			        		 }
//			        		 else {
//			        			 jugeM=affaireAffiche.get(i).getMois()+"  "+"شهر"+" ";
//			        		 }
//			        	
//				        	
//				        	 if( affaireAffiche.get(i).getJour() !=0) {
//				        		 jugeM=jugeM+ " و ";
//				        	 }
//				              }
//			        	 if(affaireAffiche.get(i).getJour()!=0 ){
//			        		 if(affaireAffiche.get(i).getJour()==1 ) {
//			        			 jugeJ= " "+"يوم "+" ";
//			        		 }
//			        		 else if(affaireAffiche.get(i).getJour()==2) {
//			        			 jugeJ= " "+"يومين"+" ";
//			        		 }
//			        		 else if((affaireAffiche.get(i).getJour() >= 3) && (affaireAffiche.get(i).getJour() <= 10)) {
//			        			 jugeJ=affaireAffiche.get(i).getJour()+" "+"أيام"+" ";
//			        		 }
//			        		 else {
//			        			 jugeJ=affaireAffiche.get(i).getJour()+"  "+"يوم"+" ";
//			        		 }
//				        	
//				              }
//			        	 String remarque=" ";
//			        	 if(affaireAffiche.get(i).getTypeJuge()!=null) {
//			        		 remarque=remarque+"("+affaireAffiche.get(i).getTypeJuge().getLibelle_typeJuge()+")";
//			        	 }
//			        	 if(affaireAffiche.get(i).getTypeDocument().toString().equals("AEX")) {
//			        		 remarque=remarque+"(إيقاف   الحكم)";
//			        	 }
//			        	 if(affaireAffiche.get(i).getAffaireAffecter()!=null) {
//			        		 remarque=remarque+"(إيتم الضم إلى القضية عدد :  "+affaireAffiche.get(i).getAffaireAffecter().getAffaireId().getNumAffaire()+ ")";
//			        	 }
//			        	 p1 =  new Phrase( boldConf.format( remarque) +boldConf.format( jugeJ)+boldConf.format( jugeM)+boldConf.format( jugeA)
//			        	 
//			        	
//			        	 ,boldfontLabelAmiri);
//				         c1 = new PdfPCell(p1);
//				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//				         //  c1.setBackgroundColor(new BaseColor(210, 210, 210));
//				         c1.setColspan(70);
//				         tableAffaire.addCell(c1);
//		         }
//		         else {
//		        	  
//		        	 if(affaireAffiche.get(i).getTypeDocument().toString().equals("AEX") ) {
//				        		 
//				        		 String remarque =" إيقاف الحكم  (سراح)";
//					        	 
//					        	 
//					        		 
//					        	 
//					        	 if(affaireAffiche.get(i).getAffaireAffecter()!=null) {
//					        		 remarque=remarque+"(إيتم الضم إلى القضية عدد :  "+affaireAffiche.get(i).getAffaireAffecter().getAffaireId().getNumAffaire()+ ")";
//					        	 } 
//					        	 p1 =  new Phrase(boldConf.format(remarque),boldfontLabelAmiri);
//						         c1 = new PdfPCell(p1);
//						         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//						         //  c1.setBackgroundColor(new BaseColor(210, 210, 210));
//						         c1.setColspan(70);
//						         tableAffaire.addCell(c1);
//		        	 }
//		        	 else {
//		        		 
//		        		 String remarque=" ";
//		        		 if(!affaireAffiche.get(i).getTypeDocument().toString().equals("CJ")) {
//			        		 remarque=remarque+"موقوف";
//			        	 }
//			        	 if(affaireAffiche.get(i).getTypeJuge()!=null) {
//			        		 remarque=remarque+"("+affaireAffiche.get(i).getTypeJuge().getLibelle_typeJuge()+")";
//			        	 }
//			        	 
//			        	 if(affaireAffiche.get(i).getAffaireAffecter()!=null) {
//			        		 remarque=remarque+"(إيتم الضم إلى القضية عدد :  "+affaireAffiche.get(i).getAffaireAffecter().getAffaireId().getNumAffaire()+ ")";
//			        	 } 
//			        	 p1 =  new Phrase(boldConf.format(remarque),boldfontLabelAmiri);
//				         c1 = new PdfPCell(p1);
//				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//				         //  c1.setBackgroundColor(new BaseColor(210, 210, 210));
//				         c1.setColspan(70);
//				         tableAffaire.addCell(c1); 
//		        		 
//		        	 }
//		        	 
//		        	
//		        	 
//		         }
//		        
//		         
//		         p1 =  new Phrase( boldConf.format("نص الحكم")  ,boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(30);
//		         tableAffaire.addCell(c1);
//		         
//		        
//	        	
//		        // p1 =  new Phrase( boldConf.format("****")  ,boldfontLabelAmirietoile);
//		         p1 =  new Phrase( boldConf.format("  ")  ,boldfontLabelAmiri);
//		         c1 = new PdfPCell(p1);
//		         
//		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		         c1.setBorder(0);
//		         //  c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(70);
//		         tableAffaire.addCell(c1);
//		         p1 =  new Phrase( boldConf.format("  ")  ,boldfontLabelAmiri);
//		         c1 = new PdfPCell(p1);
//		         
//		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		         c1.setBorder(0);
//		         //  c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(30);
//		         tableAffaire.addCell(c1);
//		         
//			}
//	         
//	         Rectangle rect = new Rectangle(15, 20, 580, 690);
//	         rect.setBorder(Rectangle.BOX);
//	         rect.setBorderWidth(2);
//	         
//	         
//	         document.add(tableTop);
//	        document.add(tTitre);
//	        document.add(rect);
//	        document.add(tArr);
//	    
//	        document.add(table);
//	        document.newPage();
//	        Rectangle rect2 = new Rectangle(15, 20, 580, 793);
//	         rect2.setBorder(Rectangle.BOX);
//	         rect2.setBorderWidth(2);
//	         
//	        document.add(tableLien);
//	        document.add(rect2);
//	        document.add(tableAffaire);
//	        // step 5
//	        document.close();
//	       
//			return new ByteArrayInputStream(out.toByteArray());
//	            
//	   
//	    }
//
//		@Override
//		public ByteArrayInputStream exportEtat(PDFListExistDTO pDFListExistDTO ) throws DocumentException, IOException, ArabicShapingException {
//			
//	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
//	        Rectangle layout = new Rectangle( PageSize.A4.rotate() );
//            Document document = new Document(PageSize.A4.rotate() , 10f, 10f,  10f, 0f);
//         
//	        PdfWriter.getInstance(document, out );
//            document.open();
//            ConfigShaping boldConf = new ConfigShaping();
//	        Font boldfontLabelTop = boldConf.getFontForArabicAmiri1(12);
// 
//	       
//	        Font boldfontTitle = boldConf.getFontForArabicAmiri(30);
//	         PdfPTable tableTop = new PdfPTable(3);
//		      tableTop.setWidthPercentage(100);
//		 
//		       
//		        Phrase p1Top;
//		        PdfPCell c1Top;
//		         
////		        PdfPCell spaceCellTop = new PdfPCell(new Phrase("  "));
////		        spaceCellTop.setBorder(0);
//		      
////		       ---------------  nom --------------------
//		        PdfPTable tab  = new PdfPTable(1);
//			    tab.setWidthPercentage(100);
//		        
//			    Phrase ptab;
//		        PdfPCell ctab;
//		        
//		        ptab =  new Phrase(  boldConf.format("الجمهورية التونسية ") ,boldfontLabelTop);
//		        ctab = new PdfPCell(ptab);
//		        ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        ctab.setBorder(0);
//		         ctab.setPaddingRight(27f);
//			    tab.addCell(ctab);
//		        
//		        
//			    
//			    
//			    ptab =  new Phrase(  boldConf.format("وزارة العدل ") ,boldfontLabelTop);
//			    ctab = new PdfPCell(ptab);
//			    ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			    ctab.setBorder(0);
//			    ctab.setPaddingRight(36f); 
//			     tab.addCell(ctab);
//			    
//			    
//			     ptab =  new Phrase(  boldConf.format("الهيئة العامة للسجون والإصلاح ") ,boldfontLabelTop);
//			     ctab = new PdfPCell(ptab);
//			     ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			     ctab.setBorder(0);
//			     tab.addCell(ctab);
//		        
//		        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
//		        LocalDate localDate = LocalDate.now();
//		        
//		         p1Top =  new Phrase( boldConf.format(dtf.format(localDate))+" "+boldConf.format("تونس في") ,boldfontLabelTop);
//			     c1Top = new PdfPCell(p1Top);
//			     c1Top.setHorizontalAlignment(Element.ALIGN_LEFT);
//			     c1Top.setBorder(0);
//			     tableTop.addCell(c1Top);
//			  
//		         
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     
//			     URL resource = EnfantService.class.getResource("/images/cgpr.png");
//			        
//			     Image image = Image.getInstance(resource);
////		          image.setAbsolutePosition(237f, 750f);
//		          image.scaleAbsolute(65f,60f);
//		          c1Top = new PdfPCell(image);
//				     c1Top.setHorizontalAlignment(Element.ALIGN_CENTER);
//				     
//				     c1Top.setBorder(0);;
//				     
//				     tableTop.addCell(c1Top);
//				  
//				     
//
//			  
//				     c1Top = new PdfPCell(tab);
//				     c1Top.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				     c1Top.setBorder(0);
//				     tableTop.addCell(c1Top);
//				     PdfPTable tTitre = new PdfPTable(1);	   
//			         
//				       Phrase pTitre =  new Phrase(boldConf.format("قائمة إسمية للأطفال المقيمين  بمركز المروج" ),boldfontTitle);
//				       PdfPCell cTitre = new PdfPCell(pTitre);
//				       cTitre.setPaddingBottom(10f); 
//				       cTitre.setBorder(Rectangle.BOX);
//				       cTitre.setBorderWidth(2);
//				     //  cTitre.setPaddingLeft(150f); 
//				       
//				       cTitre.setBackgroundColor(new BaseColor(210, 210, 210));
//				      
//				       cTitre.setBorderColor(BaseColor.BLACK);
//				       //cTitre.setBorderWidth(0);
//				      
//				       cTitre.setHorizontalAlignment(Element.ALIGN_CENTER);
//				       tTitre.setSpacingAfter(15f);
////				       tTitre.setWidthPercentage(60);
//			              tTitre.addCell(cTitre);
//					    
//	         
//				         Rectangle rect = new Rectangle(2, 5, 835, 493);
//				         rect.setBorder(Rectangle.BOX);
//				         rect.setBorderWidth(2);
////	         
//	         
//	 
//	      
//	     
//	        
//
//            Phrase p1;
//	        PdfPCell c1;
//	        PdfPTable tableNumSeq = new PdfPTable(100);
//	        
//	        tableNumSeq.setWidthPercentage(100);
//	         
//	        
//	        PdfPTable tableAffaire = new PdfPTable(100);
//	        
//	         tableAffaire.setWidthPercentage(100);
//	         Font boldfontLabel = boldConf.getFontForArabic(16);
//	     
//			   Font boldfontFamielle = boldConf.getFontForArabic(14);
//			   Font boldfontLabelEtat = boldConf.getFontForArabic(18);
//			   Font boldfontLabelAmiri = boldConf.getFontForArabicAmiri(17);
//			   Font boldfontLabelAmiri14 = boldConf.getFontForArabicAmiri(14);
//			   Font boldfontLabelAmirix = boldConf.getFontForArabicAmiri(16);
//	        
//			   
//			   
//			   p1 =  new Phrase( boldConf.format("القضايا") ,boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(50);
//		         tableAffaire.addCell(c1);
//		         
//		         p1 =  new Phrase( boldConf.format("الهوية ") ,boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(35);
//		         tableAffaire.addCell(c1);
//		         
//			   p1 =  new Phrase( boldConf.format("عدد  الإيقاف") ,boldfontLabel);
//		         c1 = new PdfPCell(p1);
//		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
//		         c1.setColspan(15);
//		         tableAffaire.addCell(c1);
//		         
//		     
//		         
//		         List<Residence>  enfantAffiche=   (List<Residence>) residenceRepository.findByAllEnfantExist(pDFListExistDTO.getEtablissement().getId());
//		         System.err.println(enfantAffiche.size());
//	   
//		         for (int i=0;i<enfantAffiche.size();i++) {
//		        
//		        	 if(enfantAffiche.get(i).getArrestation().getEnfant()!=null && enfantAffiche.get(i).getArrestation().getLiberation()==null) {
//
//		        	
////		        		   p1 =  new Phrase(    " "    ,boldfontFamielle);
////					         c1 = new PdfPCell(p1);
////					         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
////					         c1.setBorder(0);
////					         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
////					     
////					         c1.setColspan(90);
////					         tableAffaire.addCell(c1);
////					         
////					         
////					     
////					     
////					         p1 =  new Phrase( ( i+1)+"العدد الرتبي"    ,boldfontFamielle);
////					       
////					         c1 = new PdfPCell(p1);
////					         c1.setBackgroundColor(new BaseColor(210, 210, 210));
////					         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
////					         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
////					     
////					         c1.setColspan(10);
////					         tableAffaire.addCell(c1);
//					         
//					      
//		        		 List<Affaire> aData = documentRepository.findStatutJurByArrestation(enfantAffiche.get(i).getResidenceId().getIdEnfant() );
//			    		 
//				    		if (aData.isEmpty()) {
//				    			  p1 =  new Phrase(  "محكـــوم"   ,boldfontFamielle);
//				    		          	
//				    		} else {
//				    			   p1 =  new Phrase( "موقـــوف"  ,boldfontFamielle);	
//								    
//				    		}
//				    		
//				    	     c1 = new PdfPCell(p1);
//					         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//					         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//					     
//					         c1.setColspan(10);
//					         tableNumSeq.addCell(c1);
//				    		
//			        	  p1 =  new Phrase( enfantAffiche.get(i).getArrestation().getEnfant().getNom()+" بن "+ enfantAffiche.get(i).getArrestation().getEnfant().getNomPere()+" بن "+ enfantAffiche.get(i).getArrestation().getEnfant().getNomGrandPere()+" "+ enfantAffiche.get(i).getArrestation().getEnfant().getPrenom()    ,boldfontFamielle);
//				         c1 = new PdfPCell(p1);
//				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//				         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				     
//				         c1.setColspan(75);
//				         tableNumSeq.addCell(c1);
//				         
//				         
//				         
//				         p1 =  new Phrase( enfantAffiche.get(i).getNumArrestation()   ,boldfontFamielle);
//				         c1 = new PdfPCell(p1);
//				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//				         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				     
//				         c1.setColspan(15);
//				         tableNumSeq.addCell(c1);
//				         
//				         
//				         
////				         p1 =  new Phrase( ( i+1)+""    ,boldfontFamielle);
////				         c1 = new PdfPCell(p1);
////				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
////				         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
////				     
////				         c1.setColspan(3);
////				         tableAffaire.addCell(c1);
//		        
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 
//		        		 List<Affaire> lesAffaires = affaireRepository.findByArrestationCoroissant(enfantAffiche.get(i).getArrestation().getArrestationId().getIdEnfant(), 
//		        				 enfantAffiche.get(i).getArrestation().getArrestationId().getNumOrdinale());
//		        		
//		        		 PdfPTable tableAffByEnfant = new PdfPTable(100);
//					     tableAffByEnfant.setWidthPercentage(100);
//					     
//		        		 if(lesAffaires.size()!=0) {
//		        			 for (int j=0;j<lesAffaires.size();j++) {
//		        			 p1 =  new Phrase(   lesAffaires.get(j).getTypeAffaire().getLibelle_typeAffaire()   ,boldfontLabelAmiri14);
//		        			 c1 = new PdfPCell(p1);
//					         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//					         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					         c1.setBorder(0);
//					         c1.setColspan(30);
//					         tableAffByEnfant.addCell(c1);
//					         
//					         
//					         p1 =  new Phrase(  lesAffaires.get(j).getTribunal().getNom_tribunal()   ,boldfontLabelAmiri14);
//		        			 c1 = new PdfPCell(p1);
//					         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//					         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					         c1.setBorder(0);
//					         c1.setColspan(50);
//					         tableAffByEnfant.addCell(c1);
//					         
//					         
//						     p1 =  new Phrase(boldConf.format(lesAffaires.get(j).getAffaireId().getNumAffaire().toString()),boldfontLabelAmiri14);
//						     c1 = new PdfPCell(p1);
//						     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					         c1.setBorder(0);
//					         c1.setColspan(20);
//					         tableAffByEnfant.addCell(c1);
//		        			 }
//		        			   c1 = new PdfPCell(tableAffByEnfant);
//		        			   
//		        			   c1.setColspan(100);
//		        			   c1.setBorderWidth(1);
//		        			   tableNumSeq.addCell(c1);
//		        		 }
//		        		 else {
//		        			 p1 =  new Phrase( lesAffaires.size()+""     ,boldfontFamielle);
//		        		 
//		        		
//				         c1 = new PdfPCell(p1);
//				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//				         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				     
//				         c1.setColspan(100);
//				         tableNumSeq.addCell(c1);
//		        		 }
//		        		 
//		        		 
//		        		 
//		        		 c1 = new PdfPCell(tableNumSeq);
//	        			  c1.setColspan(97);
//	        			 c1.setBorderWidth(1);
//	        			 c1.setBorder(Rectangle.BOX);
//				        
//					     
//					       
//				        
//					      
//				         c1.setBorderColor(BaseColor.BLACK);
//	        			 tableAffaire.addCell(c1);
//		        		 
//				         p1 =  new Phrase( ( i+1)+""    ,boldfontLabelAmiri14);
//				         c1 = new PdfPCell(p1);
//				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//				         c1.setVerticalAlignment(Element.ALIGN_TOP);
//				         
//				          
//					     
//					       
//				         c1.setBackgroundColor(new BaseColor(210, 210, 210));
//					     
//				         c1.setColspan(3);
//				         tableAffaire.addCell(c1);
//		        		 
//				    	 p1 =  new Phrase( boldConf.format("  ")  ,boldfontLabelAmiri);
//				         c1 = new PdfPCell(p1);
//				         
//				         c1.setBorder(0);
//				          // c1.setBackgroundColor(new BaseColor(210, 210, 210));
//				         c1.setColspan(100);
//				         tableAffaire.addCell(c1);
//		        		 
//		        	
//	        			 
//	        			 tableNumSeq = new PdfPTable(100);
//	        		     tableNumSeq.setWidthPercentage(100);
//		        			 }
//		        	 
//		        
//				}
//	    	 
////	    	 for (int i=0;i<enfantAffiche.size();i++) {
////	        	 p1 =  new Phrase(enfantAffiche.get(i).toString(),boldfontLabel);
//// 
////		         
////	         
////		         
////		     //  p1 =  new Phrase(boldConf.format(enfantAffiche.get(i).getEnfant().getNom()+" بن "+ enfantAffiche.get(i).getEnfant().getNomPere()+" بن "+ enfantAffiche.get(i).getEnfant().getNomGrandPere()+" "+ enfantAffiche.get(i).getEnfant().getPrenom())   ,boldfontLabel);
////		         c1 = new PdfPCell(p1);
////		         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
////		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
////		     
////		         c1.setColspan(100);
////		         tableAffaire.addCell(c1);
////		            
////			}
//	 
//	        
//	         
//	         document.add(tableTop);
//	         document.add(tTitre);
//	        // document.add(rect);
//	         document.add(tableAffaire);
//	   
//	        document.close();
//	       
//			return new ByteArrayInputStream(out.toByteArray());
//	     
//		}
//	      
//}

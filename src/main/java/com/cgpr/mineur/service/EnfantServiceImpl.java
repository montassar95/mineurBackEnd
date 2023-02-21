package com.cgpr.mineur.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.config.ConfigShaping;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Echappes;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.EchappesRepository;
import com.cgpr.mineur.repository.EnfantRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.resource.PDFPenaleDTO;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
 

@Service
public class EnfantServiceImpl implements EnfantService {

    private  final EnfantRepository enfantRepository;

	@Autowired
	private AffaireRepository affaireRepository;
    
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private AccusationCarteRecupRepository accusationCarteRecupRepository;
	
	@Autowired
	private AccusationCarteDepotRepository accusationCarteDepotRepository;
	
	@Autowired
	private AccusationCarteHeberRepository accusationCarteHeberRepository;

	@Autowired
	private ResidenceRepository residenceRepository;
	
	@Autowired
	private EchappesRepository echappesRepository;
	

    public EnfantServiceImpl(EnfantRepository enfantRepository) {
        this.enfantRepository = enfantRepository;
    }

    public List<Affaire> findByArrestation(  String idEnfant, long numOrdinale) {

		List<Affaire> lesAffaires = affaireRepository.findByArrestationCoroissant(idEnfant, numOrdinale);
		
		System.out.println(idEnfant);
		System.out.println(numOrdinale);
		System.out.println(lesAffaires.size());
		
		List<Affaire> output = 
				lesAffaires.stream()
			        .map(s-> {
			        	
			        	
			        	 
			        	com.cgpr.mineur.models.Document  doc = documentRepository.getLastDocumentByAffaireforAccusation(idEnfant, numOrdinale, s.getNumOrdinalAffaire() );
	 							
			        	 
			        	s.setTypeDocument(doc.getTypeDocument());
			        	s.setDateEmissionDocument(doc.getDateEmission());
			        	 if (doc instanceof Transfert) {
			 				Transfert t= (Transfert) doc  ;
			 			 
			 				s.setTypeFile(t.getTypeFile());
			 				System.out.println("88888888888888888888888");
			 				System.out.println(t.getTypeFile());
			 			}
			        	 if (doc instanceof Arreterlexecution) {
							  Arreterlexecution t= (Arreterlexecution) doc  ;
							 
								s.setTypeFile(t.getTypeFile());
								System.out.println("88888888888888888888888");
								System.out.println(t.getTypeFile());
							}
			        	List<com.cgpr.mineur.models.Document> accData = documentRepository.getDocumentByAffaireforAccusation(idEnfant, numOrdinale,
 							s.getNumOrdinalAffaire() ,PageRequest.of(0,1) );
			        	
						List<TitreAccusation> titreAccusations=null;
						
						if(accData.get(0) instanceof CarteRecup) {
						 
						titreAccusations=  accusationCarteRecupRepository.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
						
						
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
						
						 CarteRecup c =(CarteRecup) accData.get(0);
						s.setAnnee(c.getAnnee());
						s.setMois(c.getMois()); 
						s.setJour(c.getJour()); 
						
						s.setAnneeArret(c.getAnneeArretProvisoire());
						s.setMoisArret(c.getMoisArretProvisoire()); 
						s.setJourArret(c.getJourArretProvisoire()); 
						
						
						s.setTypeJuge(c.getTypeJuge());
					 
						}
						else if(accData.get(0) instanceof CarteDepot) {
						titreAccusations=  accusationCarteDepotRepository.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
						s.setTitreAccusations(titreAccusations);
						s.setDateEmission(accData.get(0).getDateEmission());
						}
						else if(accData.get(0) instanceof CarteHeber) {
							titreAccusations=  accusationCarteHeberRepository.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
							s.setTitreAccusations(titreAccusations);
							s.setDateEmission(accData.get(0).getDateEmission());	 
						System.out.println("CarteHeber.."+accData.get(0).getDocumentId());
						}
		 	
			        	
			                     return s;  
			                 })
			       .collect(Collectors.toList());
		
	 
			return  output ;
		   
			
			
			
			
			
			
			
			
			
		
	}
    

	 
 

	  public static final Font FONT = new Font();
	    public static final Font BOLD = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
	    @Override
	    public ByteArrayInputStream export(  PDFPenaleDTO pDFPenaleDTO  ) throws DocumentException, IOException, ArabicShapingException {
	    	
	    	
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	
 
	        Rectangle layout = new Rectangle(PageSize.A4);
	 
	        
	        Document document = new Document(layout);
	        PdfWriter p =PdfWriter.getInstance(document, out );

	        
	        
		     
		    
	        
	     
	        document.open();
	        
 
	        URL xx = EnfantService.class.getResource("/images/page.jpg");
	        
		     Image cc = Image.getInstance(xx);

           float width = cc.getWidth();
           float height = cc.getHeight();
           System.out.println("ff");
           System.out.println(width);
           System.out.println(height);
//           cc.scaleAbsolute(1100f, 75f);// image width,height
//           cc.setAbsolutePosition(30, 40);
           p.getDirectContentUnder().addImage(cc, 600, 0, 0, 500, 0, 120);
	        
	        
	        
	        
	        ConfigShaping boldConf = new ConfigShaping();
	 
	        Font boldfontLabelTop = boldConf.getFontForArabicAmiri1(12);
	 
	        PdfPTable tab  = new PdfPTable(1);
		    tab.setWidthPercentage(100);
	        
		    Phrase ptab;
	        PdfPCell ctab;
	        
	        ptab =  new Phrase(  boldConf.format("الجمهورية التونسية ") ,boldfontLabelTop);
	        ctab = new PdfPCell(ptab);
	        ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        ctab.setBorder(0);
	         ctab.setPaddingRight(27f);
		    tab.addCell(ctab);
	        
	        
		    
		    
		    ptab =  new Phrase(  boldConf.format("وزارة العدل ") ,boldfontLabelTop);
		    ctab = new PdfPCell(ptab);
		    ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
		    ctab.setBorder(0);
		    ctab.setPaddingRight(36f); 
		     tab.addCell(ctab);
		    
		    
		     ptab =  new Phrase(  boldConf.format("الهيئة العامة للسجون والإصلاح ") ,boldfontLabelTop);
		     ctab = new PdfPCell(ptab);
		     ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
		     ctab.setBorder(0);
		     tab.addCell(ctab);
		    
		    
		    
		    
		    
		    
		    
	        PdfPTable tableTop = new PdfPTable(3);
		    tableTop.setWidthPercentage(100);
		        
		        
		        Phrase p1Top;
		        PdfPCell c1Top;
		         
 
		      
//		       ---------------  nom --------------------
		        
		        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
		        LocalDate localDate = LocalDate.now();
		        
		         p1Top =  new Phrase( boldConf.format(dtf.format(localDate))+" "+boldConf.format("تونس في") ,boldfontLabelTop);
			     c1Top = new PdfPCell(p1Top);
			     c1Top.setHorizontalAlignment(Element.ALIGN_LEFT);
			     c1Top.setBorder(0);
			     tableTop.addCell(c1Top);
			  
		         
			     
			     
			     
			     
			     
			     
			     
			     
			     
			     
			     
			     
			     URL resource = EnfantService.class.getResource("/images/cgpr.png");
			        
			     Image image = Image.getInstance(resource);
 
		          image.scaleAbsolute(70f,70f);
		          c1Top = new PdfPCell(image);
				     c1Top.setHorizontalAlignment(Element.ALIGN_CENTER);
				     
				     c1Top.setBorder(0);;
				     
				     tableTop.addCell(c1Top);
				  
				     
 
			  
				     c1Top = new PdfPCell(tab);
				     c1Top.setHorizontalAlignment(Element.ALIGN_RIGHT);
				     c1Top.setBorder(0);
				     tableTop.addCell(c1Top);
		      
				     tableTop.setSpacingAfter(18f);
			     
				     
	        
	        
	        
	        
	        
	        
	        
	        
	        
	    
	        
	        
	        
	        
	        
	        
		    //Titre     
		   Font boldfontTitle = boldConf.getFontForArabicAmiri(30);
		   Font boldfontLabel18 = boldConf.getFontForArabic(20);
		   Font boldfontLabel = boldConf.getFontForArabic(16);
		   Font boldfontFamielle = boldConf.getFontForArabic(14);
		   Font boldfontLabelEtat = boldConf.getFontForArabic(18);
		   Font boldfontLabelAmiri = boldConf.getFontForArabicAmiri(17);
		   Font boldfontLabelAmirix = boldConf.getFontForArabicAmiri(16);
         PdfPTable tTitre = new PdfPTable(1);	   
         
	       Phrase pTitre =  new Phrase(boldConf.format("مذكرة شخصية لطفل جانح" ),boldfontTitle);
	       PdfPCell cTitre = new PdfPCell(pTitre);
	       cTitre.setPaddingBottom(20f); 
	       cTitre.setBorder(Rectangle.BOX);
	       
	       cTitre.setBorderWidth(2);
	 
	       
	       cTitre.setBackgroundColor(new BaseColor(210, 210, 210));
	      
	       cTitre.setBorderColor(BaseColor.BLACK);
	     
	      
	       cTitre.setHorizontalAlignment(Element.ALIGN_CENTER);
	       
	      
	       tTitre.addCell(cTitre);
	       tTitre.setWidthPercentage(60);
	       
	      
		  if(pDFPenaleDTO.getEnfant().getImg() != null && !pDFPenaleDTO.isSansImage()) {
			  final String base64Data = pDFPenaleDTO.getEnfant().getImg().substring( pDFPenaleDTO.getEnfant().getImg().indexOf(",") + 1);
			  Image ima = Image.getInstance(Base64.decode(base64Data));
 	          ima.setAbsolutePosition(30f, 540f);
	          ima.scaleAbsolute(120f,120f);
	         	          document.add(ima);
		  }
 
	       
	       tTitre.setSpacingAfter(15f);
	       
	       
	       
	       
		    //arre    
			   Font boldfontArr = boldConf.getFontForArabicArr(16);
			 
			   
	         PdfPTable tArr = new PdfPTable(100);	   
	         
	         tArr.setWidthPercentage(80);
		        
		        
	            Phrase p1;
		        PdfPCell c1;
		         
		        PdfPCell spaceCell = new PdfPCell(new Phrase("  "));
		        spaceCell.setBorder(0);
		      
//		       ---------------  nom --------------------
		      if(pDFPenaleDTO.getNbrArrestation()<10) {
		    	  p1 =  new Phrase(boldConf.format("0"+pDFPenaleDTO.getNbrArrestation() +"  /  "+ pDFPenaleDTO.getEnfant().getId() ),boldfontLabelAmiri );
		      }
		         
		         else {
		        	 p1 =  new Phrase(boldConf.format(pDFPenaleDTO.getNbrArrestation() +"  /  "+ pDFPenaleDTO.getEnfant().getId() ),boldfontLabelAmiri ); 
		         }
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			     c1.setBorder(0);
			     c1.setColspan(55);
			      
			     tArr.addCell(c1);
			     p1 =  new Phrase(  boldConf.format(":") ,boldfontArr);
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			     c1.setBorder(0);
			     c1.setColspan(5);
			    
			     
			     tArr.addCell(c1);
		         
		         p1 =  new Phrase(  boldConf.format("المعــــرف الوحيـــد") ,boldfontArr);
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			     c1.setBorder(0);
			     c1.setColspan(40);
			    
			     
			     tArr.addCell(c1);
			     p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getCentre()),boldfontArr);
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			     c1.setBorder(0);
			     c1.setColspan(55);
			      
			     tArr.addCell(c1);
			     p1 =  new Phrase(  boldConf.format(":") ,boldfontArr);
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			     c1.setBorder(0);
			     c1.setColspan(5);
			    
			     
			     tArr.addCell(c1);
		         
		         p1 =  new Phrase(  boldConf.format("مــــــركز الإصــــــلاح") ,boldfontArr);
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			     c1.setBorder(0);
			     c1.setColspan(40);
			    
			 
			     tArr.addCell(c1);
			    
			  
			    
			     
			     p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getNumArrestation()),boldfontLabelAmiri);
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			     c1.setBorder(0);
			     c1.setColspan(55);
			      
			     tArr.addCell(c1);
			  
			     p1 =  new Phrase(  boldConf.format(":") ,boldfontArr);
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			     c1.setBorder(0);
			     c1.setColspan(5);
			    
			     
			     tArr.addCell(c1);
		         p1 =  new Phrase(  boldConf.format("عـــدد الإيقـــــــــاف") ,boldfontArr);
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			     c1.setBorder(0);
			     c1.setColspan(40);
			    
			     
			     tArr.addCell(c1);
			     p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getArrestation().getNumAffairePricipale().toString()),boldfontLabelAmiri);
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			     c1.setBorder(0);
			     c1.setColspan(55);
			      
			     tArr.addCell(c1);
			     p1 =  new Phrase(  boldConf.format(":") ,boldfontArr);
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			     c1.setBorder(0);
			     c1.setColspan(5);
			    
			     
			     tArr.addCell(c1);
		         
			     if(pDFPenaleDTO.getNbrAffaires()<10) {
			    	 p1 =  new Phrase(  boldConf.format("ــدد القضيــــــة") + boldConf.format("0"+pDFPenaleDTO.getNbrAffaires())+boldConf.format("عـــــ")  ,boldfontLabelAmirix);
					   
			     }
			     else {
			    	 p1 =  new Phrase(  boldConf.format("ـــــدد القضيــــــة") + boldConf.format(pDFPenaleDTO.getNbrAffaires()+"")+boldConf.format("عـــــ")  ,boldfontLabelAmirix);
					    	 
			     }
		          c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			     c1.setBorder(0);
			     c1.setColspan(40);
			    
			     
			     tArr.addCell(c1);
			    
			     
			     p1 =  new Phrase(  
			    		 pDFPenaleDTO.getAnneeAge().toString() +" "+
			    		          pDFPenaleDTO.getMoisAge().toString() ,boldfontLabelAmiri);
			     
		         c1 = new PdfPCell(p1);
		         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			     
			     c1.setBorder(0);
			    
			     c1.setColspan(49);
			      
			     tArr.addCell(c1);
			     
			     p1 =  new Phrase(  
			                      pDFPenaleDTO.getJourAge().toString(),boldfontLabelAmiri);
			     
		         c1 = new PdfPCell(p1);
		         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			     
			     c1.setBorder(0);
			    
			     c1.setColspan(06);
			      
			     tArr.addCell(c1);
			     
			     p1 =  new Phrase(  boldConf.format(":") ,boldfontArr);
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			     c1.setBorder(0);
			     c1.setColspan(5);
			    
			     
			     tArr.addCell(c1);
			     
			     
			     
 
 
		    	 p1 =  new Phrase(  boldConf.format("ـــن الرشـــــــــد") + boldConf.format(pDFPenaleDTO.getAgeEnfant().toString().trim())+boldConf.format("ســـــ")  ,boldfontLabelAmirix);
		 		
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			     c1.setBorder(0);
			     c1.setColspan(40);
			    
			     
			     tArr.addCell(c1);
			      
			     
			    
			     tArr.setSpacingAfter(15f);
			   
			     
			     
			     
			     
			     
			     
			     
			     
			     
	         PdfPTable table = new PdfPTable(100);
	  
	        table.setWidthPercentage(100);
	        
	        
	      
	      
//	       ---------------  nom --------------------
	      
	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getNom()+" بن "+ pDFPenaleDTO.getEnfant().getNomPere()+" بن "+ pDFPenaleDTO.getEnfant().getNomGrandPere()+" "+ pDFPenaleDTO.getEnfant().getPrenom()),boldfontLabelAmiri);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		     c1.setBorder(0);
		     c1.setColspan(65);  
		     c1.setPaddingBottom(7f); 
		     table.addCell(c1); 
		  
	         
		     p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     
		     table.addCell(c1);
		     
	         p1 =  new Phrase(  boldConf.format("الهـــــــــــــــــــــــــــــــــــوية") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		     c1.setBorder(0);
		     c1.setColspan(30);
		    
		     c1.setPaddingBottom(7f); 
		     table.addCell(c1);
		                                       
		     
		 
		    
		     
//		       ---------------  mere --------------------  
		     
	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getNomMere()+" "+ pDFPenaleDTO.getEnfant().getPrenomMere()),boldfontLabelAmiri);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
		     
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
		     
	         p1 =  new Phrase(boldConf.format("إبــــــــــــــــــــــــــــــــــــــن " ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	          
		 

//		       ---------------   Lieu et lieu --------------------	    
	         
	         p1 =  new Phrase(boldConf.format("بــــــــــــــــــــ"+ pDFPenaleDTO.getEnfant().getLieuNaissance()  )+" "+ pDFPenaleDTO.getEnfant().getDateNaissance(),boldfontLabelAmiri);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         
	         p1 =  new Phrase(boldConf.format("تــــــاريخ الـولادة ومكانهـــــا" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	          
		   
	         
	         
//		       ---------------  nationnalite --------------------      
	         
	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getNationalite().getLibelle_nationalite().toString() ),boldfontLabelAmiri);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         
	         p1 =  new Phrase(boldConf.format("الجنسيــــــــــــــــــــــــــــــة" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
//		       ---------------  situation fam --------------------      
	         
	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getSituationFamiliale().getLibelle_situation_familiale().toString() ),boldfontLabelAmiri);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         
	         p1 =  new Phrase(boldConf.format("الحـــــــــالة العائليـــــــــــــة" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);	   
	         
//		       --------------- adress --------------------      
	        	         p1 =  new Phrase(  
	        	     	 pDFPenaleDTO.getEnfant().getAdresse().toString().trim() 
	        	     	 +" "+
	        	     	 pDFPenaleDTO.getEnfant().getDelegation().getLibelle_delegation().toString()
	        	     	 +" "+
	        	     	 pDFPenaleDTO.getEnfant().getGouvernorat().getLibelle_gouvernorat().toString()	
	        		 
	        		  ,boldfontLabelAmiri);
	         c1 = new PdfPCell(p1);
	         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		        c1 .setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         p1 =  new Phrase(boldConf.format("مكان الإقــامة قبـل الإيقــاف" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);	 
	         
 	

//		       --------------- niveau edu --------------------      
	         
	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getNiveauEducatif().getLibelle_niveau_educatif().toString() ),boldfontLabelAmiri);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         
	         p1 =  new Phrase(boldConf.format("المستــوى التعليمـــــــــــي" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);	 	         
	         
//		       --------------- situation penal --------------------      
	       
	         if(pDFPenaleDTO.getArrestation().getEtatJuridique().equals("juge") ){
	        	  p1 =  new Phrase(boldConf.format( "محكــــــــــــــوم"  ),boldfontLabelEtat);
	        		 }
	         else if(pDFPenaleDTO.getArrestation().getEtatJuridique().equals("arret") ) {
	        	 
	        	 if(pDFPenaleDTO.isAppelParquet()) {
	        		 p1 =  new Phrase(boldConf.format(pDFPenaleDTO.getDateAppelParquet())+boldConf.format("موقـــوف  طعــن النيابـة بالاستئناف "),boldfontLabelEtat);
	        	 }
	        	 else if(pDFPenaleDTO.isAppelEnfant() && !pDFPenaleDTO.isAppelParquet()) {
	        		 p1 =  new Phrase(boldConf.format(pDFPenaleDTO.getDateAppelEnfant())+boldConf.format("موقـــوف   طعــن الطفــل بالاستئناف "),boldfontLabelEtat);
	        	 }
         else if(!pDFPenaleDTO.isAppelParquet() && !pDFPenaleDTO.isAppelEnfant()) {
        	 p1 =  new Phrase(boldConf.format("موقــــــــــــــوف" ),boldfontLabelEtat);
	        	 }
	        	 
	        	 
	        	 
	         }
 
	         else if(pDFPenaleDTO.getArrestation().getLiberation()!=null) {
	        	 
	        	 
	        	 if(pDFPenaleDTO.getArrestation().getLiberation().getEtabChangeManiere()==null) {
	        		 p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getArrestation().getLiberation().getCauseLiberation().getLibelleCauseLiberation().toString() ),boldfontLabelEtat);
	       	      
	        	 }
	        	 else {
	        		 p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getArrestation().getLiberation().getCauseLiberation().getLibelleCauseLiberation().toString().trim() +" إلى "+
	        				 pDFPenaleDTO.getArrestation().getLiberation().getEtabChangeManiere().getLibelle_etabChangeManiere().toString().trim()),boldfontLabelEtat);
	       	       
	        	 }
	        	 
	         
	         }
	         else {
	        	  p1 =  new Phrase(boldConf.format("--"),boldfontLabelEtat);
	         }
	        
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         p1 =  new Phrase(boldConf.format("الوضعيــــــة الجزائيــــــــــة" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(12f); 
	         table.addCell(c1);	         
	         
//		       --------------- jugee--------------------      
  if(! (pDFPenaleDTO.getAnneePenal()==0 && pDFPenaleDTO.getMoisPenal()==0 && pDFPenaleDTO.getJourPenal()==0)) {
	        	 
	        
	         String jugeA=" " ;
	         String jugeM=" " ;
	         String jugeJ=" " ;
	       
	        	 
	        	 if(pDFPenaleDTO.getAnneePenal()!=0 ){
	        		 if(pDFPenaleDTO.getAnneePenal() == 1 ) {
	        			 jugeA= " "+"عام"+" ";
	        		 }
	        		 else if(pDFPenaleDTO.getAnneePenal() == 2) {
	        			 jugeA= " "+"عامين"+" ";
	        		 }
	        		 else if((pDFPenaleDTO.getAnneePenal() >= 3) && (pDFPenaleDTO.getAnneePenal() <= 10)) {
	        			 jugeA=pDFPenaleDTO.getAnneePenal()+" "+"أعوام"+" ";
	        		 }
	        		 else {
	        			 jugeA=pDFPenaleDTO.getAnneePenal()+" "+"عام"+" ";
	        		 }
	        	
	        	 if(pDFPenaleDTO.getMoisPenal()!=0 || pDFPenaleDTO.getJourPenal() !=0) {
	        		 jugeA=jugeA+ " و ";
	        	 }
	              }
	        	 if(pDFPenaleDTO.getMoisPenal()!=0 ){
	        		 if(pDFPenaleDTO.getMoisPenal()==1  ) {
	        			 jugeM= " "+"شهر"+" ";
	        		 }
	        		 else if(pDFPenaleDTO.getMoisPenal()==2) {
	        			 jugeM= " "+"شهرين"+" ";
	        		 }
	        		 else if((pDFPenaleDTO.getMoisPenal() >= 3) && (pDFPenaleDTO.getMoisPenal() <= 10)) {
	        			 jugeM=pDFPenaleDTO.getMoisPenal()+" "+"أشهر"+" ";
	        		 }
	        		 else {
	        			 jugeM=pDFPenaleDTO.getMoisPenal()+"  "+"شهر"+" ";
	        		 }
	        	
		        	
		        	 if( pDFPenaleDTO.getJourPenal() !=0) {
		        		 jugeM=jugeM+ " و ";
		        	 }
		              }
	        	 if(pDFPenaleDTO.getJourPenal()!=0 ){
	        		 if(pDFPenaleDTO.getJourPenal()==1 ) {
	        			 jugeJ= " "+"يوم "+" ";
	        		 }
	        		 else if(pDFPenaleDTO.getJourPenal()==2) {
	        			 jugeJ= " "+"يومين"+" ";
	        		 }
	        		 else if((pDFPenaleDTO.getJourPenal() >= 3) && (pDFPenaleDTO.getJourPenal() <= 10)) {
	        			 jugeJ=pDFPenaleDTO.getJourPenal()+" "+"أيام"+" ";
	        		 }
	        		 else {
	        			 jugeJ=pDFPenaleDTO.getJourPenal()+"  "+"يوم"+" ";
	        		 }
		        	
		              }
	         
	         
	         
	         
	         
	         
	         
 
	        	 List<Affaire> affprincipale=  affaireRepository.findAffairePrincipale(pDFPenaleDTO.getArrestation().getArrestationId().getIdEnfant(),
	        			 pDFPenaleDTO.getArrestation().getArrestationId().getNumOrdinale());
	        	 boolean  allSameName = affprincipale.stream().allMatch(x -> x.getTypeDocument().equals("AEX"));
	        if(pDFPenaleDTO.isAgeAdulte()) {
	        	if(!allSameName) {
	        		 p1 =  new Phrase( boldConf.format( jugeJ)+ boldConf.format( jugeM) + boldConf.format( jugeA)+ boldConf.format(" الإيداع لبلوغ سن الرشد  و") ,boldfontLabelAmiri);
	        	}
	        	else {
	        		 p1 =  new Phrase(   boldConf.format(" إيقاف تنفيذ الحكم") +boldConf.format( jugeJ)+ boldConf.format( jugeM) + boldConf.format( jugeA)+ boldConf.format(" الإيداع لبلوغ سن الرشد  و") ,boldfontLabelAmiri);
	        	} 
	        	
	        }
	        	 
	        else {
	        	if(!allSameName) {
	        		 p1 =  new Phrase(boldConf.format( jugeJ)+ boldConf.format( jugeM) + boldConf.format( jugeA) ,boldfontLabelAmiri);
	        	}
	        	else {
	        		 p1 =  new Phrase(boldConf.format(" تم إيقاف تنفيذ الحكم") +boldConf.format( jugeJ)+ boldConf.format( jugeM) + boldConf.format( jugeA) ,boldfontLabelAmiri);
	        	} 
	        	
	        }
	        
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         p1 =  new Phrase(boldConf.format("الحكــــــــــــــــــــــــــــــــــم" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);	 	         
  }
  
  
  if(  (pDFPenaleDTO.getAnneePenal()==0 && pDFPenaleDTO.getMoisPenal()==0 
		  && pDFPenaleDTO.getJourPenal()==0) && (pDFPenaleDTO.isAgeAdulte())) {
	  
	  
	  p1 =  new Phrase(boldConf.format(" الإيداع لبلوغ سن الرشد ") ,boldfontLabelAmiri);
	  c1 = new PdfPCell(p1);
      c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
      c1.setBorder(0);
      c1.setColspan(65);
      c1.setPaddingBottom(7f); 
      table.addCell(c1);
      
      
      p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
	     c1 = new PdfPCell(p1);
	     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	     c1.setBorder(0);
	     c1.setColspan(5);
	    
	     table.addCell(c1);
	     
      p1 =  new Phrase(boldConf.format("الحكــــــــــــــــــــــــــــــــــم" ),boldfontLabel);
      c1 = new PdfPCell(p1);
      c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
      c1.setBorder(0);
      c1.setColspan(30);
      c1.setPaddingBottom(7f); 
      table.addCell(c1);	 
  }
//		       --------------- Lieu jugee--------------------      
	         
	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getDateJugementPrincipale().toString() ),boldfontLabelAmiri);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		    if((pDFPenaleDTO.getAnneePenal()==0 && pDFPenaleDTO.getMoisPenal()==0 && pDFPenaleDTO.getJourPenal()==0) &&(pDFPenaleDTO.getArrestation().getEtatJuridique().equals("arret") )) {
		    
		    	 p1 =  new Phrase(boldConf.format("تـــــــــــاريخ الإيقـــــــــــــاف" ),boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		         c1.setBorder(0);
		         c1.setColspan(30);
		         c1.setPaddingBottom(7f); 
		         table.addCell(c1);
		    }
		    else {
		    	
		    	
		    	if(pDFPenaleDTO.getAnneePenal()==0 && pDFPenaleDTO.getMoisPenal()==0 && pDFPenaleDTO.getJourPenal()==0) {
		    		p1 =  new Phrase(boldConf.format("تاريـــخ صـــدور البطاقــــــة" ),boldfontLabel);
			         c1 = new PdfPCell(p1);
			         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			         c1.setBorder(0);
			         c1.setColspan(30);
			         c1.setPaddingBottom(7f); 
			         table.addCell(c1);
		    	}
		    	else {
		    		p1 =  new Phrase(boldConf.format("تـــــــــــاريخ الحكــــــــــــــم" ),boldfontLabel);
			         c1 = new PdfPCell(p1);
			         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			         c1.setBorder(0);
			         c1.setColspan(30);
			         c1.setPaddingBottom(7f); 
			         table.addCell(c1);
		    	}
		    	
		    	 
		    }
	        		   
	         
//		       --------------- tribunal--------------------    
		   
 
 		    p1 =  new Phrase("("+pDFPenaleDTO.getArrestation().getTypeAffairePricipale().getLibelle_typeAffaire()+")",boldfontFamielle);
		       c1 = new PdfPCell(p1);
	         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(25);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         p1 =  new Phrase( pDFPenaleDTO.getArrestation().getTribunalPricipale().getNom_tribunal().toString() ,boldfontLabelAmiri);
	         c1 = new PdfPCell(p1);
	         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(40);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         
	         p1 =  new Phrase(boldConf.format("المحكمـــــــــــــــــــــــــــــة" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);		   

	         
//		       --------------- tribunal--------------------      
	     
	     
	     
	     
	     
	         p1 =  new Phrase(  pDFPenaleDTO.getAccu().toString().trim()  ,boldfontLabelAmirix);
	      
	         c1 = new PdfPCell(p1);
	         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
	        c1 .setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         
	         p1 =  new Phrase(boldConf.format("التهمـــــــــــــــــــــــــــــــــة" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);		
	         
	         
	         
//		       --------------- tribunal--------------------      
	         String string = pDFPenaleDTO.getArrestation().getDate().toString();
	         String[] parts = string.split("-");
	         String part1 = parts[0];  
	         String part2 = parts[1];  
	         String part3 = parts[2]; 
	         String date = part3+"-"+part2+"-"+part1;
	         p1 =  new Phrase(boldConf.format( date ),boldfontLabelAmiri);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         p1 =  new Phrase(boldConf.format("تــــاريخ الإيـــداع بالمــــركز" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);		
	         
	         
	         
//		       --------------- tribunal--------------------      
	         
	         if(pDFPenaleDTO.getDateDebut() != null) {
	        	 p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getDateDebut().toString() ),boldfontLabelAmiri);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		         c1.setBorder(0);
		         c1.setColspan(65);
		         c1.setPaddingBottom(7f); 
		         table.addCell(c1);
		         
	        
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         p1 =  new Phrase(boldConf.format("تاريخ بداية العقـــــــــــــــاب" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);		
	         }
	         
//		       --------------- tribunal--------------------      
	         
	       if( pDFPenaleDTO.getDateFin() != null && !pDFPenaleDTO.isAppelParquet()) {
	    	   p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getDateFin().toString()  ),boldfontLabelAmiri);
		         
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		         c1.setBorder(0);
		         c1.setColspan(65);
		         c1.setPaddingBottom(7f); 
		         
		         table.addCell(c1);
	      
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         
	         p1 =  new Phrase(boldConf.format("تاريخ الســـــــــــــــــــــــراح" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);		
	       }
	         
//		       --------------- tribunal--------------------      
	         
	         
	         if( !(pDFPenaleDTO.getAnneeArret()==0 && pDFPenaleDTO.getMoisArret()==0 && pDFPenaleDTO.getJourArret()==0)) {
	        	  
	         
	         String arretA=" " ;
	         String arretM=" " ;
	         String arretJ=" " ;
	    
	        	 
	        	 if(pDFPenaleDTO.getAnneeArret()!=0 ){
	        		 if(pDFPenaleDTO.getAnneeArret()==1   ) {
	        			 arretA= " "+"عام"+" ";
	        		 }
	        		 else if(pDFPenaleDTO.getAnneeArret()==2) {
	        			 arretA= " "+"عامين"+" ";
	        		 }
	        		 else if((pDFPenaleDTO.getAnneeArret() >= 3) && (pDFPenaleDTO.getAnneeArret() <= 10)) {
	        			 arretA=pDFPenaleDTO.getAnneeArret()+" "+"أعوام"+" ";
	        		 }
	        		 else {
	        			 arretA=pDFPenaleDTO.getAnneeArret()+" "+"عام"+" ";
	        		 }
	        	
	        	 if(pDFPenaleDTO.getMoisArret()!=0 || pDFPenaleDTO.getJourArret() !=0) {
	        		 arretA=arretA+ " و ";
	        	 }
	              }
	        	 
	        	 if(pDFPenaleDTO.getMoisArret()!=0 ){
	        		 if(pDFPenaleDTO.getMoisArret()==1  ) {
	        			 arretM= " "+"شهر"+" ";
	        		 }
	        		 else if(pDFPenaleDTO.getMoisArret()==2) {
	        			 arretM= " "+"شهرين"+" ";
	        		 }
	        		 else if((pDFPenaleDTO.getMoisArret() >= 3) && (pDFPenaleDTO.getMoisArret() <= 10)) {
	        			 arretM=pDFPenaleDTO.getAnneeArret()+" "+"أشهر"+" ";
	        		 }
	        		 else {
	        			 arretM=pDFPenaleDTO.getMoisArret()+"  "+"شهر"+" ";
	        		 }
	        	
		        	
		        	 if( pDFPenaleDTO.getJourArret() !=0) {
		        		 arretM=arretM+ " و ";
		        	 }
		              }
	        	 
	        	 if(pDFPenaleDTO.getJourArret()!=0 ){
	        		 if(pDFPenaleDTO.getJourArret()==1 ) {
	        			 arretJ= " "+"يوم "+" ";
	        		 }
	        		 else if(pDFPenaleDTO.getJourArret()==2) {
	        			 arretJ= " "+"يومين"+" ";
	        		 }
	        		 else if((pDFPenaleDTO.getJourArret() >= 3) && (pDFPenaleDTO.getJourArret() <= 10)) {
	        			 arretJ=pDFPenaleDTO.getAnneeArret()+" "+"أيام"+" ";
	        		 }
	        		 else {
	        			 arretJ=pDFPenaleDTO.getJourArret()+"  "+"يوم"+" ";
	        		 }
		        	
		              }
	         
	         
	         
	         
	         
	         
	         
	         
	       //  arret=  pDFPenaleDTO.getAnneePenal()+" "+pDFPenaleDTO.getMoisPenal()+" "+pDFPenaleDTO.getJourPenal()+" ";
	         
	         p1 =  new Phrase(boldConf.format( arretJ)+boldConf.format( arretM)+boldConf.format( arretA),boldfontLabelAmiri);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         p1 =  new Phrase(boldConf.format("إيقــــــاف تحفظـــــــــــــــي" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);		
	         
	         
	         }
 
	         
	         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getClassePenale().getLibelle_classe_penale().toString() ),boldfontLabelAmiri);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         
	         p1 =  new Phrase(boldConf.format("العقوبــــــات السابقــــــــــة" ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);		
	         
	         
//		       --------------- tribunal--------------------    
	        if(pDFPenaleDTO.getNumTotaleEchappe() !=0 ||  pDFPenaleDTO.getNumTotaleRecidence() !=0 ||  pDFPenaleDTO.getNumTotaleRecidenceWithetabChangeManiere() !=0) {
	        	   p1 =  new Phrase( 
	        			           
	        			             boldConf.format( "نقــــــل " )+ boldConf.format(" "+pDFPenaleDTO.getNumTotaleRecidence()+" "),boldfontLabelAmiri);
	         
	        	   if(pDFPenaleDTO.getNumTotaleEchappe() !=0 ) {
	        		   p1.add( (  boldConf.format(" و ")+boldConf.format( "فــــــرار " )+ boldConf.format(" "+pDFPenaleDTO.getNumTotaleEchappe()+" ")));
	        	   }
	        	   if(pDFPenaleDTO.getNumTotaleRecidenceWithetabChangeManiere() !=0 ) {
	        		   p1.add( (  boldConf.format(" و ")+boldConf.format( "تغير وسيلة " )+ boldConf.format(" "+pDFPenaleDTO.getNumTotaleRecidenceWithetabChangeManiere()+" ")));
	        	   }
	      
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(65);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);
	         
	         
	         p1 =  new Phrase(  boldConf.format(":") ,boldfontLabel);
		     c1 = new PdfPCell(p1);
		     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     c1.setBorder(0);
		     c1.setColspan(5);
		    
		     table.addCell(c1);
		     
	         p1 =  new Phrase(boldConf.format("المـــــــلاحظـــــــات " ),boldfontLabel);
	         c1 = new PdfPCell(p1);
	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	         c1.setBorder(0);
	         c1.setColspan(30);
	         c1.setPaddingBottom(7f); 
	         table.addCell(c1);	
	         
	         
	         
	     
	         
	         Residence r = residenceRepository.findMaxResidence(pDFPenaleDTO.getEnfant().getId(), pDFPenaleDTO.getArrestation().getArrestationId().getNumOrdinale());
	         
	         if(r.getDateEntree()!=null  && r.getEtablissementEntree()!=null  && r.getCauseMutation()!=null ) {
			       
			      p1 =  new Phrase( r.getDateEntree().toString()  +" "+ "قدم من"+" "+ r.getEtablissementEntree().getLibelle_etablissement().toString() +" من أجل  ("+ r.getCauseMutation().getLibelle_causeMutation().toString()+") يوم "  ,boldfontLabel);
       			 c1 = new PdfPCell(p1);
			         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			         c1.setBorder(0);
			         
			         c1.setBackgroundColor( BaseColor.WHITE);
			         c1.setColspan(100);
			         table.addCell(c1);
			      }
			      
			    if(r.getStatut()==2 ) {
				       
				      p1 =  new Phrase(   "لم يتم الإستقبال بعد " +" "+ "قدم من"+" "+ r.getEtablissementEntree().getLibelle_etablissement().toString() +"   من أجل "+ r.getCauseMutation().getLibelle_causeMutation().toString()+"  "    ,boldfontLabel);
	        			 c1 = new PdfPCell(p1);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setBorder(0);
				         
				         c1.setBackgroundColor( BaseColor.WHITE);
				         c1.setColspan(100);
				         table.addCell(c1);
				      }
			    
			    
			    Echappes e = echappesRepository.findByIdEnfantAndResidenceTrouverNull(pDFPenaleDTO.getEnfant().getId());
			    Echappes eLast = echappesRepository.findMaxEchappes(pDFPenaleDTO.getEnfant().getId(), pDFPenaleDTO.getArrestation().getArrestationId().getNumOrdinale());
			    
		         if(e!=null  ) {
		        	  p1 =  new Phrase(  e.getDateEchappes().toString() +":"+"في حالة فرار " +" "+  " فر من"+" "+e.getResidenceEchapper().getEtablissement().getLibelle_etablissement().toString() +" "+
		                                             e.getCommentEchapper().getLibelleComment().toString()       ,boldfontLabel);
	     			 c1 = new PdfPCell(p1);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setBorder(0);
				         
				         c1.setBackgroundColor( BaseColor.WHITE);
				         c1.setColspan(100);
				         table.addCell(c1);
		        	 
		         }
		         else if(eLast!=null  ) {
		        	  p1 =  new Phrase(  eLast.getDateTrouver().toString() + " "+  "تم القبض عليه يوم "         ,boldfontLabel);
						c1 = new PdfPCell(p1);
						  c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						  c1.setHorizontalAlignment(Element.ALIGN_LEFT);
						  c1.setBorder(0);
						  
						  c1.setBackgroundColor( BaseColor.WHITE);
						  c1.setColspan(50);
						  table.addCell(c1);
		         
		        	  p1 =  new Phrase(  eLast.getDateEchappes().toString() + "   "+  " فر من"+" "+eLast.getResidenceEchapper().getEtablissement().getLibelle_etablissement().toString() +" "+
		        			  eLast.getCommentEchapper().getLibelleComment().toString()       ,boldfontLabel);
						c1 = new PdfPCell(p1);
						  c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						  c1.setHorizontalAlignment(Element.ALIGN_LEFT);
						  c1.setBorder(0);
						  
						  c1.setBackgroundColor( BaseColor.WHITE);
						  c1.setColspan(50);
						  table.addCell(c1);
						  
						

                 }
		         
		         
		         if(pDFPenaleDTO.getNumTotaleRecidenceWithetabChangeManiere() !=0) {
		        	  Residence c = residenceRepository.findMaxResidenceWithEtabChangeManiere(pDFPenaleDTO.getEnfant().getId(), pDFPenaleDTO.getArrestation().getArrestationId().getNumOrdinale()); 
		        	
		        	  if(c !=null && c.getDateSortie()!=null) {  
		        	//	  System.out.println(c.toString());
		        		  c.getResidenceId().setNumOrdinaleResidence( c.getResidenceId().getNumOrdinaleResidence()+1);
		        		  Residence resFinChangeManier =residenceRepository.retourChangeManier(c.getResidenceId());
		        		  
		        		  if(resFinChangeManier!=null) {
							  p1 =  new Phrase(   resFinChangeManier.getDateEntree().toString() +" "+  "عاد إلى "+ 
									  resFinChangeManier.getEtablissement().getLibelle_etablissement().toString()    ,boldfontLabel);
										c1 = new PdfPCell(p1);
										  c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
										  c1.setHorizontalAlignment(Element.ALIGN_LEFT);
										  c1.setBorder(0);
										  
										  c1.setBackgroundColor( BaseColor.WHITE);
										  c1.setColspan(50);
										  table.addCell(c1);
						  }
		        		  
		        		  
		        	  p1 =  new Phrase(   c.getDateSortie().toString() +" "  +  "تغير وسيلة إلى "+ c.getEtabChangeManiere().getLibelle_etabChangeManiere().toString()    ,boldfontLabel);
						c1 = new PdfPCell(p1);
						  c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						  c1.setHorizontalAlignment(Element.ALIGN_LEFT);
						  c1.setBorder(0);
						  
						  c1.setBackgroundColor( BaseColor.WHITE);
						  c1.setColspan(50);
						  table.addCell(c1);
						
						  
		        	 
		        	  } 
		        	  
		         }
	         
	        }
	         
	         PdfPTable tableAffaire = new PdfPTable(100);
	        
	         tableAffaire.setWidthPercentage(100);
	        
	        
	         PdfPTable tableLien = new PdfPTable(100);
		       // table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
	         tableLien.setWidthPercentage(80);
	          
		        
		      if(  ! pDFPenaleDTO.isSansDetail()) {
		      
//		       ---------------  nom --------------------
		      
		         p1 =  new Phrase(boldConf.format( pDFPenaleDTO.getEnfant().getNom()+" بن "+ pDFPenaleDTO.getEnfant().getNomPere()+" بن "+ pDFPenaleDTO.getEnfant().getNomGrandPere()+" "+ pDFPenaleDTO.getEnfant().getPrenom()),boldfontLabelAmiri);
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			     
			     c1.setColspan(100);  
			     c1.setBorderWidth(2);
			     c1.setBorder(Rectangle.BOX);
			     c1.setBackgroundColor(new BaseColor(210, 210, 210));
			     c1.setPaddingBottom(7f); 
			      
			     tableLien.addCell(c1);
			     
			     p1 =  new Phrase(" ")  ;
			     c1 = new PdfPCell(p1);
			     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			     c1.setBorder(0);
			     c1.setColspan(100);  
			     
			     
			      
			     tableLien.addCell(c1);
	         List<Affaire> affaireAffiche= findByArrestation(pDFPenaleDTO.getEnfant().getId(),   pDFPenaleDTO.getArrestation().getArrestationId().getNumOrdinale());
	         
	         for (int i=0;i<affaireAffiche.size();i++) {
	        	 
	        	 
	        	 p1 =  new Phrase(boldConf.format(" "),boldfontLabel);
 ;
		         
	        	 p1 =  new Phrase( " " ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setBorder(0);
		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	 
		         c1.setColspan(70);
		         tableAffaire.addCell(c1);
		       

		         p1 =  new Phrase( (i+1) +" "+boldConf.format(" العدد الرتبي للقضية : ") ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(30);
		         tableAffaire.addCell(c1);
		         
		         p1 =  new Phrase(boldConf.format( affaireAffiche.get(i).getAffaireId().getNumAffaire()) +" "+ affaireAffiche.get(i).getTribunal().getNom_tribunal()  ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		       
		         c1.setColspan(70);
		         tableAffaire.addCell(c1);
		         p1 =  new Phrase( boldConf.format("  القضية") ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(30);
		         tableAffaire.addCell(c1);
		         
 
		         
		         System.out.println(affaireAffiche.get(i).getTypeDocument().toString()+" "+
                         affaireAffiche.get(i).getAffaireId().getIdEnfant().toString());
		         
		         
		         
		         switch (affaireAffiche.get(i).getTypeDocument().toString().trim()) {
					case "CD":
						 p1 =  new Phrase( boldConf.format("بطاقة إيداع")  ,boldfontLabelAmiri);
						 
                      break;
                
					case "CH":
						 p1 =  new Phrase( boldConf.format("بطاقة إيواء")  ,boldfontLabelAmiri);

						break;
						
					case "AEX":
						
							if(affaireAffiche.get(i).getTypeFile()!= null)
							{
								if(affaireAffiche.get(i).getTypeFile().toString().equals("AEX".toString()) ) {
									 
									
									 p1 =  new Phrase( affaireAffiche.get(i).getDateEmissionDocument().toString()  +" "+ boldConf.format("إيقاف تنفيذ الحكم") ,boldfontLabelAmiri);
								}else if(affaireAffiche.get(i).getTypeFile().toString().equals("L".toString())) {
								 
									
									 p1 =  new Phrase( affaireAffiche.get(i).getDateEmissionDocument().toString()  +" "+ boldConf.format("ســــــــــــراح") ,boldfontLabelAmiri);
							 
							       }
								}
								else {
									 
									 p1 =  new Phrase( affaireAffiche.get(i).getDateEmissionDocument().toString()  +" "+ boldConf.format("إيقاف تنفيذ الحكم") ,boldfontLabelAmiri);
						   }
						break;
					case "CJ":
						 p1 =  new Phrase( boldConf.format("مضمون حكم")  ,boldfontLabelAmiri);
						 
                     break;
               
					case "T":
						if(affaireAffiche.get(i).getTypeFile()!= null)
					{
						if(affaireAffiche.get(i).getTypeFile().toString().equals("T".toString()) ) {
							 p1 =  new Phrase(  affaireAffiche.get(i).getDateEmissionDocument().toString() +" "+boldConf.format("إحــــــالة")    ,boldfontLabelAmiri);
						}else if(affaireAffiche.get(i).getTypeFile().toString().equals("A".toString())) {
							 p1 =  new Phrase(  affaireAffiche.get(i).getDateEmissionDocument().toString() +" "+boldConf.format("تخلــــــي")    ,boldfontLabelAmiri);
						}else if(affaireAffiche.get(i).getTypeFile().toString().equals("G".toString())) {
							 p1 =  new Phrase(  affaireAffiche.get(i).getDateEmissionDocument().toString() +" "+boldConf.format("تعهــــــد")    ,boldfontLabelAmiri);
						}
					}
						else {
							 p1 =  new Phrase(  affaireAffiche.get(i).getDateEmissionDocument().toString() +" "+boldConf.format("إحــــــالة")    ,boldfontLabelAmiri);
						}
						 
						break;
						
					case "AE":
						 p1 =  new Phrase( affaireAffiche.get(i).getDateEmissionDocument().toString()  +" "+  boldConf.format("طعن الطفل بالاستئناف")  ,boldfontLabelAmiri);

						break;	
						
					case "AP":
						 p1 =  new Phrase(  affaireAffiche.get(i).getDateEmissionDocument().toString()+" "+ boldConf.format("طعن النيابة بالاستئناف")    ,boldfontLabelAmiri);

						break;
						
					case "CR":
						 p1 =  new Phrase(  affaireAffiche.get(i).getDateEmissionDocument().toString()   +" "+boldConf.format("مراجعة")  ,boldfontLabelAmiri);

						break;	
					case "CRR":
						 p1 =  new Phrase(  affaireAffiche.get(i).getDateEmissionDocument().toString()   +" "+boldConf.format("قرار رفض المراجعة")  ,boldfontLabelAmiri);

						break;	
					case "CP":
						 p1 =  new Phrase(  affaireAffiche.get(i).getDateEmissionDocument().toString()  +" "+boldConf.format("قرار تمديد")  ,boldfontLabelAmiri);

						break;
					case "CHL":
						 p1 =  new Phrase(  affaireAffiche.get(i).getDateEmissionDocument().toString()   +" "+boldConf.format("قرار تغير مكان الإيداع ")  ,boldfontLabelAmiri);

						break;
				 
					default:
						 p1 =  new Phrase( boldConf.format("--")  ,boldfontLabelAmiri);
					 
					}
		         
		         
		        
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     //    c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(70);
		         tableAffaire.addCell(c1);
		         
		         p1 =  new Phrase( boldConf.format("  نوع الوثيقة")  ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(30);
		         tableAffaire.addCell(c1);
		         
		         
		         
		         
		         p1 =  new Phrase(  affaireAffiche.get(i).getDateEmission().toString()   ,boldfontLabelAmiri);
		         c1 = new PdfPCell(p1);
		         
		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		     //    c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(70);
		         tableAffaire.addCell(c1);
		         
		         String labelAffair="";
		         if(affaireAffiche.get(i).getTypeDocument().toString().equals("CD")   || affaireAffiche.get(i).getTypeDocument().toString().equals("CH") 
		        		 || affaireAffiche.get(i).getTypeDocument().toString().equals("T") || affaireAffiche.get(i).getTypeDocument().toString().equals("CP") ) {
		        	 labelAffair="تاريخ صدور البطاقة ";
	        	 }
		         else{
		        	 if(affaireAffiche.get(i).getTypeDocument().toString().equals("CHL")){
		        		 labelAffair="تاريخ القضية ";
		        		 
		        	 }
		        	 else {
		        	 labelAffair="تاريخ الحكم";
		        	 }
		         }
		         
		         p1 =  new Phrase( boldConf.format(labelAffair)  ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(30);
		         tableAffaire.addCell(c1);
		         
		        
		         
		         
		         
		         String titreAccusationSring = " ";
		         for (TitreAccusation  titreAccusation : affaireAffiche.get(i).getTitreAccusations()) {
		        	 titreAccusationSring=titreAccusationSring+titreAccusation.getTitreAccusation()+" ";
		        	 
				}
		         p1 =  new Phrase( titreAccusationSring.trim()   ,boldfontLabelAmirix);
		        
		         c1 = new PdfPCell(p1);
		         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		         //  c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(70);
		         tableAffaire.addCell(c1);
		         p1 =  new Phrase( boldConf.format("التهمة ")  ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(30);
		         tableAffaire.addCell(c1);
		         
		         
		         if(! (affaireAffiche.get(i).getAnnee()==0 && affaireAffiche.get(i).getMois()==0 && affaireAffiche.get(i).getJour()==0)) {
		        	 
		 	        
			         String jugeA=" " ;
			         String jugeM=" " ;
			         String jugeJ=" " ;
			       
			        	 
			        	 if(affaireAffiche.get(i).getAnnee()!=0 ){
			        		 if(affaireAffiche.get(i).getAnnee() == 1 ) {
			        			 jugeA= " "+"عام"+" ";
			        		 }
			        		 else if(affaireAffiche.get(i).getAnnee() == 2) {
			        			 jugeA= " "+"عامين"+" ";
			        		 }
			        		 else if((affaireAffiche.get(i).getAnnee() >= 3) && (affaireAffiche.get(i).getAnnee() <= 10)) {
			        			 jugeA=affaireAffiche.get(i).getAnnee()+" "+"أعوام"+" ";
			        		 }
			        		 else {
			        			 jugeA=affaireAffiche.get(i).getAnnee()+" "+"عام"+" ";
			        		 }
			        	
			        	 if(affaireAffiche.get(i).getMois()!=0 || affaireAffiche.get(i).getJour() !=0) {
			        		 jugeA=jugeA+ " و ";
			        	 }
			              }
			        	 if(affaireAffiche.get(i).getMois()!=0 ){
			        		 if(affaireAffiche.get(i).getMois()==1  ) {
			        			 jugeM= " "+"شهر"+" ";
			        		 }
			        		 else if(affaireAffiche.get(i).getMois()==2) {
			        			 jugeM= " "+"شهرين"+" ";
			        		 }
			        		 else if((affaireAffiche.get(i).getMois() >= 3) && (affaireAffiche.get(i).getMois() <= 10)) {
			        			 jugeM=affaireAffiche.get(i).getMois()+" "+"أشهر"+" ";
			        		 }
			        		 else {
			        			 jugeM=affaireAffiche.get(i).getMois()+"  "+"شهر"+" ";
			        		 }
			        	
				        	
				        	 if( affaireAffiche.get(i).getJour() !=0) {
				        		 jugeM=jugeM+ " و ";
				        	 }
				              }
			        	 if(affaireAffiche.get(i).getJour()!=0 ){
			        		 if(affaireAffiche.get(i).getJour()==1 ) {
			        			 jugeJ= " "+"يوم "+" ";
			        		 }
			        		 else if(affaireAffiche.get(i).getJour()==2) {
			        			 jugeJ= " "+"يومين"+" ";
			        		 }
			        		 else if((affaireAffiche.get(i).getJour() >= 3) && (affaireAffiche.get(i).getJour() <= 10)) {
			        			 jugeJ=affaireAffiche.get(i).getJour()+" "+"أيام"+" ";
			        		 }
			        		 else {
			        			 jugeJ=affaireAffiche.get(i).getJour()+"  "+"يوم"+" ";
			        		 }
				        	
				              }
			        	 String remarque=" ";
			        	 if(affaireAffiche.get(i).getTypeJuge()!=null) {
			        		 remarque=remarque+" "+affaireAffiche.get(i).getTypeJuge().getLibelle_typeJuge()+" ";
			        	 }
			        	 if(affaireAffiche.get(i).getTypeDocument().toString().equals("AEX")) {
			        		 remarque=remarque+"(إيقاف   الحكم)";
			        	 }
			        	 if(affaireAffiche.get(i).getAffaireAffecter()!=null) {
			        		 remarque=remarque+" تم الضم إلى القضية عدد :  "+affaireAffiche.get(i).getAffaireAffecter().getAffaireId().getNumAffaire()+ " ";
			        	 }
			        	 p1 =  new Phrase(   remarque +" "+  jugeJ +" "+ jugeM +" "+  jugeA 
			        	 
			        	
			        	 ,boldfontLabelAmiri);
				         c1 = new PdfPCell(p1);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
	        			 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         //  c1.setBackgroundColor(new BaseColor(210, 210, 210));
				         c1.setColspan(70);
				         tableAffaire.addCell(c1);
		         }
		         else {
		        	  
		        	 if(affaireAffiche.get(i).getTypeDocument().toString().equals("AEX") ) {
				        		 
				        		 String remarque =" إيقاف الحكم   سراح ";
					        	 
					        	 
					        		 
					        	 
					        	 if(affaireAffiche.get(i).getAffaireAffecter()!=null) {
					        		 remarque=remarque+" تم الضم إلى القضية عدد :  "+affaireAffiche.get(i).getAffaireAffecter().getAffaireId().getNumAffaire()+ " ";
					        	 } 
					        	 p1 =  new Phrase( remarque ,boldfontLabelAmiri);
						         c1 = new PdfPCell(p1);
						         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			        			 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						         //  c1.setBackgroundColor(new BaseColor(210, 210, 210));
						         c1.setColspan(70);
						         tableAffaire.addCell(c1);
		        	 }
		        	 else {
		        		 
		        		 String remarque=" ";
		        		 if(!affaireAffiche.get(i).getTypeDocument().toString().equals("CJ")) {
			        		 remarque=remarque+"موقوف";
			        	 }
			        	 if(affaireAffiche.get(i).getTypeJuge()!=null) {
			        		 remarque=remarque+" "+affaireAffiche.get(i).getTypeJuge().getLibelle_typeJuge()+" ";
			        	 }
			        	 
			        	 if(affaireAffiche.get(i).getAffaireAffecter()!=null) {
			        		 remarque=remarque+"  تم الضم إلى القضية عدد :  "+affaireAffiche.get(i).getAffaireAffecter().getAffaireId().getNumAffaire()+ " ";
			        	 } 
			        	 p1 =  new Phrase( remarque ,boldfontLabelAmiri);
				         c1 = new PdfPCell(p1);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
	        			 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         //  c1.setBackgroundColor(new BaseColor(210, 210, 210));
				         c1.setColspan(70);
				         tableAffaire.addCell(c1); 
		        		 
		        	 }
		        	 
		        	
		        	 
		         }
		        
		         
		         p1 =  new Phrase( boldConf.format("نص الحكم")  ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(30);
		         tableAffaire.addCell(c1);
		         
		        
	        	
		         
		         p1 =  new Phrase( boldConf.format("  ")  ,boldfontLabelAmiri);
		         c1 = new PdfPCell(p1);
		         
		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		         c1.setBorder(0);
		 
		         c1.setColspan(70);
		         tableAffaire.addCell(c1);
		         p1 =  new Phrase( boldConf.format("  ")  ,boldfontLabelAmiri);
		         c1 = new PdfPCell(p1);
		         
		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		         c1.setBorder(0);
		      
		         c1.setColspan(30);
		         tableAffaire.addCell(c1);
		         
		         
		         
			}
	         
		      }
	         Rectangle rect = new Rectangle(15, 20, 580, 690);
	         rect.setBorder(Rectangle.BOX);
	         rect.setBorderWidth(2);
	         
	         
	         document.add(tableTop);
	        document.add(tTitre);
	        document.add(rect);
	        document.add(tArr);
	    
	        document.add(table);
	       if(!pDFPenaleDTO.isSansDetail()) {document.newPage();
	        Rectangle rect2 = new Rectangle(15, 20, 580, 793);
	         rect2.setBorder(Rectangle.BOX);
	         rect2.setBorderWidth(2);
	         
	        document.add(tableLien);
	        document.add(rect2);
	        document.add(tableAffaire);}
	        // step 5
	        document.close();
	       
			return new ByteArrayInputStream(out.toByteArray());
	            
	   
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    

		@Override
		public ByteArrayInputStream exportEtat(PDFListExistDTO pDFListExistDTO ) throws DocumentException, IOException, ArabicShapingException {
			
			
			
 
			
			System.out.println(pDFListExistDTO.getCheckEtranger() +"qqqqqqqqqqqqqqqqqqqqqqqqqqqq");
			
 
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
	        LocalDate localDate = LocalDate.now();
	        
			Date start = null;
			Date end = null;
			if(pDFListExistDTO.getAge1()!=0 && pDFListExistDTO.getAge2()!=0){
				Calendar cal = Calendar.getInstance();
				
				
				int year = Calendar.getInstance().get(Calendar.YEAR);
				
				
				cal.set(Calendar.YEAR, (year-pDFListExistDTO.getAge2())-1);
				cal.set(Calendar.MONTH, (localDate.getMonthValue()-1));  
				cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());  
				start =cal.getTime();
				 
				cal.set(Calendar.YEAR, year-pDFListExistDTO.getAge1());
				cal.set(Calendar.MONTH, (localDate.getMonthValue()-1));  
				cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());  
                end =cal.getTime();
                
                
                
			}
			
		 
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate  date1 =null;
			LocalDate  date2 = null;	
			 Date dateDebutGlobale= null;
			 Date dateFinGlobale= null;
			 
			if( pDFListExistDTO.getDateDebutGlobale() != null   && pDFListExistDTO.getDateFinGlobale() != null ) {
				
				 date1 = LocalDate .parse(pDFListExistDTO.getDateDebutGlobale(), formatter);
		             dateDebutGlobale = java.util.Date.from(date1.atStartOfDay()
		            	      .atZone(ZoneId.systemDefault())
		            	      .toInstant());
		            
		           date2 = LocalDate .parse(pDFListExistDTO.getDateFinGlobale(), formatter);
		            dateFinGlobale = java.util.Date.from(date2.atStartOfDay()
		            	      .atZone(ZoneId.systemDefault())
		            	      .toInstant()); 
		             
			}
           
            
            
			 
           
			String titreString=" ";
					
			 List<Residence>  enfantAffiche = null; 
             switch (pDFListExistDTO.getEtatJuridiue()) {
					case "arret":
						titreString="الموقوفين ";
					  enfantAffiche =   (List<Residence>) residenceRepository.findByAllEnfantExistArret(pDFListExistDTO.getClassePenale().getId() ,
								pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
								pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,pDFListExistDTO.getEtablissement().getId()
								,pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,pDFListExistDTO.getTypeAffaire().getId(),start,end ,pDFListExistDTO.getCheckEtranger());
	            
						 
                  break;
					case "juge":
						titreString="المحكومين";
						    enfantAffiche=   (List<Residence>) residenceRepository.findByAllEnfantExistJuge(
						    		pDFListExistDTO.getClassePenale().getId() ,
									pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
									pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,pDFListExistDTO.getEtablissement().getId()
									,pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,pDFListExistDTO.getTypeAffaire().getId() ,start,end ,pDFListExistDTO.getCheckEtranger());
		                 
						    
						   
						    
						    break;
		                   
					case "attetAP":
						titreString="الموقوفين ( إستئناف النيابة )";
						  enfantAffiche=   (List<Residence>) residenceRepository.findByAllEnfantExistArretAP(
								  pDFListExistDTO.getClassePenale().getId() ,
									pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
									pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,pDFListExistDTO.getEtablissement().getId()
									,pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,pDFListExistDTO.getTypeAffaire().getId() ,start,end,pDFListExistDTO.getCheckEtranger());
		                   break;
						
					case "attetAE":
						titreString="الموقوفين ( إستئناف الطفل )";
						  enfantAffiche=   (List<Residence>) residenceRepository.findByAllEnfantExistArretAE(
								  pDFListExistDTO.getClassePenale().getId() ,
									pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
									pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,pDFListExistDTO.getEtablissement().getId()
									,pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,pDFListExistDTO.getTypeAffaire().getId(),start,end,pDFListExistDTO.getCheckEtranger() );
		                   break;
					case "attetT":
						titreString="الموقوفين ( إحالة )";
						 enfantAffiche=   (List<Residence>) residenceRepository.findByAllEnfantExistArretT(pDFListExistDTO.getClassePenale().getId() ,
									pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
									pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,pDFListExistDTO.getEtablissement().getId()
									,pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,pDFListExistDTO.getTypeAffaire().getId(),start,end,pDFListExistDTO.getCheckEtranger() );
		                   break;
						
					case "jugeR":
						titreString="المحكومين ( مراجعة )";
						enfantAffiche=   (List<Residence>) residenceRepository.findByAllEnfantExistJugeR(pDFListExistDTO.getClassePenale().getId() ,
								pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
								pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,pDFListExistDTO.getEtablissement().getId()
								,pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,pDFListExistDTO.getTypeAffaire().getId() ,start,end,pDFListExistDTO.getCheckEtranger());
	                   break;
						
	                   
					case "all":
						titreString="المقيمين";
						
//					 
						enfantAffiche=   (List<Residence>) residenceRepository.findByAllEnfantExist(pDFListExistDTO.getClassePenale().getId() ,
								pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
								pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,
								pDFListExistDTO.getEtablissement(),pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,
								pDFListExistDTO.getTypeAffaire().getId(),start,end,pDFListExistDTO.getCheckEtranger() ); 

						break;
						
						
					case "devenuMajeur":
						titreString="البالغين لسن الرشد";
						if(dateDebutGlobale==null&&dateFinGlobale==null) {
					 
							  dateDebutGlobale = new Date();
							  dateFinGlobale = new Date();
								dateDebutGlobale.setYear(dateDebutGlobale.getYear()-18);
								dateFinGlobale.setYear(dateFinGlobale.getYear()-18);
							  dateFinGlobale.setMonth(dateFinGlobale.getMonth()+1);
								System.out.println(dateDebutGlobale);
								System.out.println(dateFinGlobale);
						}
						else {
					 
							dateDebutGlobale.setYear(dateDebutGlobale.getYear()-18);
							dateFinGlobale.setYear(dateFinGlobale.getYear()-18);
							System.out.println(dateDebutGlobale);
							System.out.println(dateFinGlobale);
							
						}
 					 
						enfantAffiche=   (List<Residence>) residenceRepository.findByAllEnfantDevenuMajeur(pDFListExistDTO.getClassePenale().getId() ,
								pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
								pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,
								pDFListExistDTO.getEtablissement(),pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,
								pDFListExistDTO.getTypeAffaire().getId(),dateDebutGlobale,dateFinGlobale,pDFListExistDTO.getCheckEtranger() ); 

						break;
					case "entreReelle":
						titreString="الداخلون";
						
//					 
						enfantAffiche=   (List<Residence>) residenceRepository.findByAllEnfantEntreReelle(pDFListExistDTO.getClassePenale().getId() ,
								pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
								pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,
								pDFListExistDTO.getEtablissement(),pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,pDFListExistDTO.getTypeAffaire().getId(),
								start,end ,dateDebutGlobale,dateFinGlobale,pDFListExistDTO.getCheckEtranger()); 

						break;
						
					case "libere":
						titreString="السراحات";
						
//					 

						enfantAffiche=   (List<Residence>) residenceRepository.findByAllEnfantLibere(pDFListExistDTO.getClassePenale().getId() ,
								pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
								pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,
								pDFListExistDTO.getEtablissement(),pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,pDFListExistDTO.getTypeAffaire().getId(),
								start,end ,dateDebutGlobale,dateFinGlobale,pDFListExistDTO.getCheckEtranger()); 

						break;
						
						
					case "seraLibere":
						titreString="المفرج عنهم";


						enfantAffiche=   (List<Residence>) residenceRepository.findByAllEnfantSeraLibere(
					    		pDFListExistDTO.getClassePenale().getId() ,
								pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
								pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,pDFListExistDTO.getEtablissement().getId()
								,pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,pDFListExistDTO.getTypeAffaire().getId() ,
								start,end,dateDebutGlobale,dateFinGlobale,pDFListExistDTO.getCheckEtranger());
	                 	
  
								enfantAffiche.stream()
							        .map(s-> {
							        	
							        	
							        	s.setDateFin(affaireRepository.getDateFinPunition(s.getArrestation().getArrestationId().getIdEnfant(),
			 		    						s.getArrestation().getArrestationId().getNumOrdinale()));         	 
							         
							        	 if(s.getDateFin()==null) {
							        		 List<Affaire> affprincipale=  affaireRepository.findAffairePrincipale(s.getArrestation().getArrestationId().getIdEnfant(),
								        			 s.getArrestation().getArrestationId().getNumOrdinale());
	                         	        		boolean  allSameName = affprincipale.stream().allMatch(x -> x.getTypeDocument().equals("AEX"));
	                         	        		if (allSameName) {
	                         	        			s.setDateFin(affprincipale.get(0).getDocuments().get(affprincipale.get(0).getDocuments().size()-1).getDateEmission() );
	                         		    			 
	                         		    		}
                                      }
							         
							                     return s; 
							                     
							                 }) 
							       .collect(Collectors.toList());
							
								enfantAffiche.sort(Comparator.comparing(Residence::getDateFin));
						break;
						
					case "sortieMutation":
						titreString="الواقع نقلتهم";
						
//					 
						enfantAffiche=   (List<Residence>) residenceRepository.findBySortieMutation(pDFListExistDTO.getClassePenale().getId() ,
								pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
								pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,
								pDFListExistDTO.getEtablissement(),pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,pDFListExistDTO.getTypeAffaire().getId(),
								start,end  ,dateDebutGlobale,dateFinGlobale,pDFListExistDTO.getCheckEtranger()); 

						break;
						
					case "entreeMutation":
						titreString="الوافدون";
						
//					 
						enfantAffiche=   (List<Residence>) residenceRepository.findByEntreeMutation(pDFListExistDTO.getClassePenale().getId() ,
								pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
								pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,
								pDFListExistDTO.getEtablissement(),pDFListExistDTO.getGouvernoratTribunal().getId() ,pDFListExistDTO.getTypeTribunal().getId() ,
								pDFListExistDTO.getTypeAffaire().getId(),start,end  ,dateDebutGlobale,dateFinGlobale,pDFListExistDTO.getCheckEtranger()); 

						break;	
						
						
					case "nonAff":
						titreString=" المقيمين دون قضايا";
						
//					 
						enfantAffiche=   (List<Residence>) residenceRepository.findByAllEnfantNonExist(pDFListExistDTO.getClassePenale().getId() ,
								pDFListExistDTO.getNiveauEducatif().getId() ,pDFListExistDTO.getGouvernorat().getId() ,pDFListExistDTO.getSituationFamiliale().getId() ,
								pDFListExistDTO.getSituationSocial().getId() ,pDFListExistDTO.getMetier().getId() ,pDFListExistDTO.getDelegation().getId() ,
								pDFListExistDTO.getEtablissement(),start,end ); 

						break;
					default:
						titreString="المقيمين";
 
					 
					}
		        
			 
			
			
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
	    
            Document document = new Document(PageSize.A4.rotate() , 10f, 10f,  10f, 0f);
	        PdfWriter.getInstance(document, out );
            document.open();
            ConfigShaping boldConf = new ConfigShaping();
	        Font boldfontLabelTop = boldConf.getFontForArabicAmiri1(12);
 
	       
	        Font boldfontTitle = boldConf.getFontForArabicAmiri(30);
	        Font boldfontLabel = boldConf.getFontForArabic(16);
		    Font boldfontFamielle = boldConf.getFontForArabic(14);
			Font boldfontFamielle11 = boldConf.getFontForArabic(11);
			Font boldfontFamielle13 = boldConf.getFontForArabic(13);
			Font boldfontLabelAmiri = boldConf.getFontForArabicAmiri(17);
			Font boldfontLabelAmiri14 = boldConf.getFontForArabicAmiri(13);
			Font boldfontLabelAmiri13 = boldConf.getFontForArabicAmiri(13);
			Font boldfontLabelAmiri11 = boldConf.getFontForArabicAmiri(12);
			Font boldfontLabelAmiri9 = boldConf.getFontForArabicAmiri(9);
			
	 
			
		
	        
	        URL resource = EnfantService.class.getResource("/images/cgpr.png");
	        Image image = Image.getInstance(resource);
            image.scaleAbsolute(65f,60f);
	          
	          
	         PdfPTable tableTop = new PdfPTable(3);
		     tableTop.setWidthPercentage(100);
		 
		       
		        Phrase p1Top;
		        PdfPCell c1Top;
		         
 
		      
//		       ---------------------------------------------------
		        PdfPTable tab  = new PdfPTable(1);
			    tab.setWidthPercentage(100);
		        
			    Phrase ptab;
		        PdfPCell ctab;
		        
		        ptab =  new Phrase(  boldConf.format("الجمهورية التونسية ") ,boldfontLabelTop);
		        ctab = new PdfPCell(ptab);
		        ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        ctab.setBorder(0);
		        ctab.setPaddingRight(27f);
			    tab.addCell(ctab);
		        
		        
			    ptab =  new Phrase(  boldConf.format("وزارة العدل ") ,boldfontLabelTop);
			    ctab = new PdfPCell(ptab);
			    ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
			    ctab.setBorder(0);
			    ctab.setPaddingRight(36f); 
			    tab.addCell(ctab);
			    
			    
			     ptab =  new Phrase(  boldConf.format("الهيئة العامة للسجون والإصلاح ") ,boldfontLabelTop);
			     ctab = new PdfPCell(ptab);
			     ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
			     ctab.setBorder(0);
			     tab.addCell(ctab);
		        
		        
		        
		         p1Top =  new Phrase( boldConf.format(dtf.format(localDate))+" "+boldConf.format( pDFListExistDTO.getEtablissement().getGouvernorat().getLibelle_gouvernorat().toString().trim()+" "+"في") ,boldfontLabelTop);
			     c1Top = new PdfPCell(p1Top);
			     c1Top.setHorizontalAlignment(Element.ALIGN_LEFT);
			     c1Top.setBorder(0);
			     tableTop.addCell(c1Top);
			  
		         
		         c1Top = new PdfPCell(image);
				 c1Top.setHorizontalAlignment(Element.ALIGN_CENTER);
				 c1Top.setBorder(0);;
				 tableTop.addCell(c1Top);
				  
				     

			  
				     c1Top = new PdfPCell(tab);
				     c1Top.setHorizontalAlignment(Element.ALIGN_RIGHT);
				     c1Top.setBorder(0);
				     tableTop.addCell(c1Top);
				     PdfPTable tTitre = new PdfPTable(1);	   
			      
				     
				     
				     
				       Phrase pTitre =  new Phrase(boldConf.format("قائمة إسمية للأطفال "+titreString +" بمركز"+pDFListExistDTO.getEtablissement().getLibelle_etablissement().toString().trim() ),boldfontTitle);
				       PdfPCell cTitre = new PdfPCell(pTitre);
				       cTitre.setPaddingBottom(10f); 
				       cTitre.setBorder(Rectangle.BOX);
				       cTitre.setBorderWidth(2);
				       cTitre.setBackgroundColor(new BaseColor(210, 210, 210));
				       cTitre.setBorderColor(BaseColor.BLACK);
                       cTitre.setHorizontalAlignment(Element.ALIGN_CENTER);
                       
				       tTitre.setSpacingAfter(15f);
                       tTitre.addCell(cTitre);
					    
	         
				         Rectangle rect = new Rectangle(2, 5, 835, 493);
				         rect.setBorder(Rectangle.BOX);
				         rect.setBorderWidth(2);
         
	         
	 
	      
	     
	        

            Phrase p1;
	        PdfPCell c1;
	        
	        
	        PdfPTable tableAffaire = new PdfPTable(100);
	        tableAffaire.setWidthPercentage(100);
	        
			   
	        
			   
			   
			  
			   p1 =  new Phrase( boldConf.format("القضايا") ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(41);
              tableAffaire.addCell(c1);
		         
              
              
		         p1 =  new Phrase( boldConf.format("تــــــاريخ") ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(19);
		         
              tableAffaire.addCell(c1);
		         
              
		         p1 =  new Phrase( boldConf.format("الهوية ") ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(30);
              tableAffaire.addCell(c1);
		         
              
			   p1 =  new Phrase( boldConf.format("ع.لإيقاف") ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(7);
             tableAffaire.addCell(c1);
             
             
		         
		         p1 =  new Phrase( boldConf.format("ع.ر") ,boldfontLabel);
		         c1 = new PdfPCell(p1);
		         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		         c1.setBackgroundColor(new BaseColor(210, 210, 210));
		         c1.setColspan(3);
              tableAffaire.addCell(c1);
              
              
              //--------------------------------------------------------------------------------------------------------------------
		         
             
 
	   
		         for (int i=0;i<enfantAffiche.size();i++) {
		        
		        	  if(enfantAffiche.get(i).getArrestation().getEnfant()!=null ) {
		        	// if(enfantAffiche.get(i).getArrestation().getEnfant()!=null) {
		        		 
		        		  List<Affaire> lesAffaires = new ArrayList<Affaire>();
		        		 //autre methode vraii
		        		// List<Affaire> lesAffaires =  enfantAffiche.get(i).getArrestation().getAffaires();
		        		 
		        		 if(pDFListExistDTO.getCheckUniqueAff()==null ) {
		        		 lesAffaires = affaireRepository.findByArrestationCoroissant(enfantAffiche.get(i).getArrestation().getArrestationId().getIdEnfant(), 
	 		        				 enfantAffiche.get(i).getArrestation().getArrestationId().getNumOrdinale());
	         		 
		        		 }
 		        		
		        		 
		        		 
		        		 
		        		 
		        		 
		        		//-----------------------------------------------------
		    	         
 		        		 else  {
		    	         
		    	       lesAffaires = affaireRepository.findAffairePrincipale(
		    	        		 
		    	        		 enfantAffiche.get(i).getArrestation().getArrestationId().getIdEnfant(), 
 		        				 enfantAffiche.get(i).getArrestation().getArrestationId().getNumOrdinale()
		    	        		 );
		    				
		    			 
		    				boolean allSameNamex = lesAffaires.stream().allMatch(x -> x.getTypeDocument().toString().equals("AEX".toString()));
		    						
		    				if (allSameNamex && enfantAffiche.get(i).getArrestation().getLiberation() == null) {
		    					enfantAffiche.get(i).getArrestation().setEtatJuridique("isAEX");
		    				}
		    			 
		    				for (Affaire affaire : lesAffaires) {
		    					System.out.println(affaire.getAffaireId() + " " + affaire.getTypeAffaire().getStatutException() + " "
		    							+ affaire.getTribunal().getTypeTribunal().getStatutNiveau() + " " + affaire.getDaysDiffJuge()
		    							+ " " + affaire.getTypeAffaire().getStatutNiveau() + " " + affaire.getTypeDocument());

		    				}
		    				Affaire a = lesAffaires.stream().peek(num -> System.out.println("aff filter " + num.getTypeDocument()))
		    						.filter(x -> x.getTypeDocument().equals("AP") || x.getTypeDocument().equals("CD")
		    														|| x.getTypeDocument().equals("CH") || x.getTypeDocument().equals("CJA")
		    														|| x.getTypeDocument().equals("T") || x.getTypeDocument().equals("AE")
		    														|| x.getTypeDocument().equals("CP")

		    						)
		    	 
		    	 				.findFirst().orElse(null);

		    				 
		    				if (!(lesAffaires.isEmpty())) {
		    					if (a != null) {
		    						 lesAffaires = new ArrayList<Affaire>();
		    						 lesAffaires.add(a);

		    					} else {
		    						a = lesAffaires.stream().peek(num -> System.out.println("aff filter " + num.getTypeDocument()))
		    								.filter(x -> ( x.getTypeDocument().equals("CJ") &&(x.getAffaireAffecter() == null)) ).findFirst()
		    								
		    								.orElse(lesAffaires.stream().peek(num -> System.out.println("aff filter " + num.getTypeDocument()))
		    										.filter(x -> ((x.getAffaireAffecter() == null)) ).findFirst().orElse(null));
		    						
		    						
		    						 lesAffaires = new ArrayList<Affaire>();
		    						 lesAffaires.add(a);

		    					}
		    				}      
		    	         
		    	         
		    	         
		    	         
		    	         
		        	  }
		    	         
//		    	  --------------------------------------------------------------       
		    	         
		    	         
		    	         
		        		 lesAffaires = 
		        					lesAffaires.stream()
		        				        .map(s-> {
		        				        	
		        				        	
		        				        	 
		        				        	com.cgpr.mineur.models.Document   doc = documentRepository.getLastDocumentByAffaireforAccusation(s.getArrestation().getArrestationId().getIdEnfant(), 
		        				        			s.getArrestation().getArrestationId().getNumOrdinale(), s.getNumOrdinalAffaire() );
		        		 							
		        				        	 
		        				        	s.setTypeDocument(doc.getTypeDocument());
		        				        	s.setDateEmissionDocument(doc.getDateEmission());
		        				        	  if (doc instanceof Transfert) {
		        				  				Transfert t= (Transfert) doc  ;
		        				  			 
		        				  				s.setTypeFile(t.getTypeFile());
		        				  				System.out.println("99999999999999999999999");
		        				  				System.out.println(t.getTypeFile());
		        				  			}
		        				        	  if (doc instanceof Arreterlexecution) {
		        								  Arreterlexecution t= (Arreterlexecution) doc  ;
		        								 
		        									s.setTypeFile(t.getTypeFile());
		        									System.out.println("99999999999999999999999");
		        									System.out.println(t.getTypeFile());
		        								}
		        				        	List<com.cgpr.mineur.models.Document> accData = documentRepository.getDocumentByAffaireforAccusation(
		        				        			s.getArrestation().getArrestationId().getIdEnfant(), 
		        				        			s.getArrestation().getArrestationId().getNumOrdinale(),
		        	 							  s.getNumOrdinalAffaire() ,PageRequest.of(0,1) );
		        				        	
		        							List<TitreAccusation> titreAccusations=null;
		        							
		        							if(accData.get(0) instanceof CarteRecup) {
		        							 
		        							titreAccusations=  accusationCarteRecupRepository.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
		        							
		        							
		        							s.setTitreAccusations(titreAccusations);
		        							s.setDateEmission(accData.get(0).getDateEmission());
		        							
		        							 CarteRecup c =(CarteRecup) accData.get(0);
		        							s.setAnnee(c.getAnnee());
		        							s.setMois(c.getMois()); 
		        							s.setJour(c.getJour()); 
		        							
		        							s.setAnneeArret(c.getAnneeArretProvisoire());
		        							s.setMoisArret(c.getMoisArretProvisoire()); 
		        							s.setJourArret(c.getJourArretProvisoire()); 
		        							
		        							
		        							s.setTypeJuge(c.getTypeJuge());
		        						 
		        							}
		        							else if(accData.get(0) instanceof CarteDepot) {
		        							titreAccusations=  accusationCarteDepotRepository.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
		        							s.setTitreAccusations(titreAccusations);
		        							s.setDateEmission(accData.get(0).getDateEmission());
		        							}
		        							else if(accData.get(0) instanceof CarteHeber) {
		        								titreAccusations=  accusationCarteHeberRepository.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
		        								s.setTitreAccusations(titreAccusations);
		        								s.setDateEmission(accData.get(0).getDateEmission());	 
		        							System.out.println("CarteHeber.."+accData.get(0).getDocumentId());
		        							}
		        						 
		        				        	
		        				                     return s;  
		        				                 })
		        				       .collect(Collectors.toList());
		        		 
		        		 
		        		 
		        		 
		        		 
		        		 
		        		 
		        		 
		        		 
		        		 
		        		 
		        		 
		        		 
		        		 
		        		 
		        		 
		        		 				 PdfPTable tableTypeDocument = new PdfPTable(100);
		        		 				tableTypeDocument.setWidthPercentage(100);
		        		 
		        		 
		        		 PdfPTable tableAffByEnfant = new PdfPTable(100);
					     tableAffByEnfant.setWidthPercentage(100);
					     
					     PdfPTable tableNumArr = new PdfPTable(100);
					     tableNumArr.setWidthPercentage(100);
					     
					     
					     PdfPTable tableTypeAff = new PdfPTable(100);
					     tableTypeAff.setWidthPercentage(100);
					     
					     
					     
					     PdfPTable tableNumAff = new PdfPTable(100);
					     tableNumAff.setWidthPercentage(100);
					     
		        		 if(lesAffaires.size()!=0) {
		        			 for (int j=0;j<lesAffaires.size();j++) {
		        				 
		        	 
		        				 if(lesAffaires.get(j).getTypeAffaire() !=null ) {
		        					 p1 =  new Phrase(  lesAffaires.get(j).getTypeAffaire().getLibelle_typeAffaire()   ,boldfontLabelAmiri13);
		        				 }
		        				 else {
		        					 p1 =  new Phrase(  ".."   ,boldfontLabelAmiri13);
		        				 }
		        				
			        			 c1 = new PdfPCell(p1);
			        			 c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			        			 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
								    
 
	 						     c1.setBorder(0);
	 					         c1.setColspan(35);
	 					        c1.setBackgroundColor(new BaseColor(240, 240, 240));
						         tableNumAff.addCell(c1);
						        
						        
						        
						      
					         
					         p1 =  new Phrase(  lesAffaires.get(j).getTribunal().getNom_tribunal()   ,boldfontFamielle13);
		        			 c1 = new PdfPCell(p1);
					         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
					         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					     
 
					         c1.setBorder(0);
 					         c1.setColspan(41);
 					        c1.setBackgroundColor(new BaseColor(240, 240, 240));
					         tableNumAff.addCell(c1);
					         
					         
						     p1 =  new Phrase(boldConf.format(lesAffaires.get(j).getAffaireId().getNumAffaire().toString()),boldfontLabelAmiri13);
						     c1 = new PdfPCell(p1);
						     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
						    
 
 						     c1.setBorder(0);
 					         c1.setColspan(24);
 					        c1.setBackgroundColor(new BaseColor(240, 240, 240));
					         tableNumAff.addCell(c1);
					         
					         
					         
				
					         
		        			   String titreAccusationSring = " ";
						         for (TitreAccusation  titreAccusation : lesAffaires.get(j).getTitreAccusations()) {
						        	 titreAccusationSring=titreAccusationSring+titreAccusation.getTitreAccusation()+" ";
						        	 
								}
						         p1 =  new Phrase(   titreAccusationSring.trim()  ,boldfontLabelAmiri11);
			        			 c1 = new PdfPCell(p1);
						         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
 
						         c1.setBorder(0);
						         c1.setColspan(100);
						         tableNumAff.addCell(c1);
						         
						         
						       
						         
						         
						         
							     
							     if(! (lesAffaires.get(j).getAnnee()==0 && lesAffaires.get(j).getMois()==0 && lesAffaires.get(j).getJour()==0)) {
						        	 
							 	        
							         String jugeA=" " ;
							         String jugeM=" " ;
							         String jugeJ=" " ;
							       
							        	 
							        	 if(lesAffaires.get(j).getAnnee()!=0 ){
							        		 if(lesAffaires.get(j).getAnnee() == 1 ) {
							        			 jugeA= " "+"عام"+" ";
							        		 }
							        		 else if(lesAffaires.get(j).getAnnee() == 2) {
							        			 jugeA= " "+"عامين"+" ";
							        		 }
							        		 else if((lesAffaires.get(j).getAnnee() >= 3) && (lesAffaires.get(j).getAnnee() <= 10)) {
							        			 jugeA=lesAffaires.get(j).getAnnee()+" "+"أعوام"+" ";
							        		 }
							        		 else {
							        			 jugeA=lesAffaires.get(j).getAnnee()+" "+"عام"+" ";
							        		 }
							        	
							        	 if(lesAffaires.get(j).getMois()!=0 || lesAffaires.get(j).getJour() !=0) {
							        		 jugeA=jugeA+ " و ";
							        	 }
							              }
							        	 if(lesAffaires.get(j).getMois()!=0 ){
							        		 if(lesAffaires.get(j).getMois()==1  ) {
							        			 jugeM= " "+"شهر"+" ";
							        		 }
							        		 else if(lesAffaires.get(j).getMois()==2) {
							        			 jugeM= " "+"شهرين"+" ";
							        		 }
							        		 else if((lesAffaires.get(j).getMois() >= 3) && (lesAffaires.get(j).getMois() <= 10)) {
							        			 jugeM=lesAffaires.get(j).getMois()+" "+"أشهر"+" ";
							        		 }
							        		 else {
							        			 jugeM=lesAffaires.get(j).getMois()+"  "+"شهر"+" ";
							        		 }
							        	
								        	
								        	 if( lesAffaires.get(j).getJour() !=0) {
								        		 jugeM=jugeM+ " و ";
								        	 }
								              }
							        	 if(lesAffaires.get(j).getJour()!=0 ){
							        		 if(lesAffaires.get(j).getJour()==1 ) {
							        			 jugeJ= " "+"يوم "+" ";
							        		 }
							        		 else if(lesAffaires.get(j).getJour()==2) {
							        			 jugeJ= " "+"يومين"+" ";
							        		 }
							        		 else if((lesAffaires.get(j).getJour() >= 3) && (lesAffaires.get(j).getJour() <= 10)) {
							        			 jugeJ=lesAffaires.get(j).getJour()+" "+"أيام"+" ";
							        		 }
							        		 else {
							        			 jugeJ=lesAffaires.get(j).getJour()+"  "+"يوم"+" ";
							        		 }
								        	
								              }
							        	 String remarque=" ";
							        	 if(lesAffaires.get(j).getAffaireAffecter()!=null) {
							        		 remarque=remarque+"  تم الضم إلى القضية عدد :  "+lesAffaires.get(j).getAffaireAffecter().getAffaireId().getNumAffaire()+ " ";
							        	 }
							        	 else {
							        		 if(lesAffaires.get(j).getTypeJuge()!=null) {
								        		 remarque=remarque+" "+lesAffaires.get(j).getTypeJuge().getLibelle_typeJuge()+" ";
								        	 }
								        	 if(lesAffaires.get(j).getTypeDocument().toString().equals("AEX")) {
								        		 remarque=remarque+"(إيقاف   الحكم)";
								        	 }
							        	 }
							        	 
							        	
							        	 p1 =  new Phrase(   remarque  +" " +  jugeJ+" " +  jugeM +" " +  jugeA 
							        	 
							        	
							        	 ,boldfontLabelAmiri14);
								         c1 = new PdfPCell(p1);
								         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
					        			 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
								  
								         c1.setBorder(0);
								         c1.setColspan(100);
								         tableTypeAff.addCell(c1);
						         }
						         else {
						        	  
						        	 if(lesAffaires.get(j).getTypeDocument().toString().equals("AEX") ) {
								        		 
								        		 String remarque =" إيقاف الحكم   سراح)";
									        	 
									        	 
									        		 
									        	 
									        	 if(lesAffaires.get(j).getAffaireAffecter()!=null) {
									        		 remarque=remarque+"  تم الضم إلى القضية عدد :  "+lesAffaires.get(j).getAffaireAffecter().getAffaireId().getNumAffaire() ;
									        	 } 
									        	 p1 =  new Phrase( remarque ,boldfontLabelAmiri14);
										         c1 = new PdfPCell(p1);
										         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
							        			 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
										         c1.setBorder(0);
										       
										         c1.setColspan(100);
										         tableTypeAff.addCell(c1);
						        	 }
						        	 else {
						        		 
						        		 String remarque=" ";
 
							        	
							        	 
							        	 if(lesAffaires.get(j).getAffaireAffecter()!=null) {
							        		 remarque=remarque+"  تم الضم إلى القضية عدد :  "+lesAffaires.get(j).getAffaireAffecter().getAffaireId().getNumAffaire() ;
							        		 p1 =  new Phrase( remarque ,boldfontLabelAmiri14);
									         c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						        			 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setBorder(0);
							 
									         c1.setColspan(100);
									         tableTypeAff.addCell(c1);
							        	 
							        	 } 
							        	 
							        	 else if(lesAffaires.get(j).getTypeJuge()!=null) {
							        		 remarque=remarque   +lesAffaires.get(j).getTypeJuge().getLibelle_typeJuge() ;
							        		 p1 =  new Phrase(boldConf.format(remarque),boldfontLabelAmiri14);
									         c1 = new PdfPCell(p1);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setBorder(0);
									 
									         c1.setColspan(100);
									         tableTypeAff.addCell(c1);
							        	 
							        	 }
							        	 
						        		 
						        	 }
						        	 
						        	
						        	 
						         }		     
							     
							     
							     
							     
							     
							     
							     
							     c1 = new PdfPCell(tableTypeAff);
							        
							        c1.setColspan(100);
							        c1.setBorder(0);
							        c1.setBackgroundColor(new BaseColor(245,245,245)); 
							        c1.setBorderColor( BaseColor.BLACK);
				        			  
				      
							        tableNumAff.addCell(c1);
		        				 
							      
			        				 
 
							        
							        
							        tableTypeAff = new PdfPTable(100);
								     tableTypeAff.setWidthPercentage(100);
						         
						         
						         
						         
						         
						         
						         
						         
						         
						         
						         
						         
						         
						         
						         c1 = new PdfPCell(tableNumAff);
					 
			        			 c1.setColspan(69);
                              tableAffByEnfant.addCell(c1);
                              
                           
						         
		        			   tableNumAff = new PdfPTable(100);
							     tableNumAff.setWidthPercentage(100);
					         
					         
					         
							     
							     
		        				 
		        				
						         
		        				   p1 =  new Phrase(  lesAffaires.get(j).getDateEmission().toString()   ,boldfontLabelAmiri13);
				        			 c1 = new PdfPCell(p1);
				        			 c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
							         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
							         c1.setColspan(50);
							         c1.setBorder(0);
							         tableTypeDocument.addCell(c1);
							         switch (lesAffaires.get(j).getTypeDocument()) {
				       					case "CD":
				       						 p1 =  new Phrase(  "بطاقة إيداع"   ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
				       						 
				                             break;
				                       
				       					case "CH":
				       						 p1 =  new Phrase(  "بطاقة إيواء"   ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);

				       						break;
				       						
				       					case "AEX":
				       						p1 =  new Phrase(    "تاريخ  الحكم"     ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
									         p1 =  new Phrase(   lesAffaires.get(j).getDateEmissionDocument().toString()  ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
									     	if(lesAffaires.get(j).getTypeFile()!= null)
											{
												if(lesAffaires.get(j).getTypeFile().toString().equals("AEX".toString()) ) {
													 
													
													 p1 =  new Phrase(   "إيقاف تنفيذ الحكم"  ,boldfontLabelAmiri13);
												}else if(lesAffaires.get(j).getTypeFile().toString().equals("L".toString())) {
													 p1 =  new Phrase(   " ســــــــــــراح"  ,boldfontLabelAmiri13);
													
													 
											 
											       }
												}
												else {
													 
													 p1 =  new Phrase(   "إيقاف تنفيذ الحكم"  ,boldfontLabelAmiri13);
										   }
									     	
				       						 
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);

				       						break;
				       					case "CJ":
				       						 p1 =  new Phrase(  "مضمون حكم"   ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
				       						 
				                            break;
				                      
				       					case "T":
				       						p1 =  new Phrase(    "صدور البطاقة"     ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
									         p1 =  new Phrase(   lesAffaires.get(j).getDateEmissionDocument().toString()  ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
									     	if(lesAffaires.get(j).getTypeFile()!= null)
											{
												if(lesAffaires.get(j).getTypeFile().toString().equals("T".toString()) ) {
													 
													 p1 =  new Phrase(    "إحــــــالة"     ,boldfontLabelAmiri13);
												}else if(lesAffaires.get(j).getTypeFile().toString().equals("A".toString())) {
													p1 =  new Phrase(    "تخلــــــي"     ,boldfontLabelAmiri13);
													
													 
												}else if(lesAffaires.get(j).getTypeFile().toString().equals("G".toString())) {
													 p1 =  new Phrase(    "تعهــــــد"     ,boldfontLabelAmiri13);
													
													 
												}
											}
												else {
													 
													 p1 =  new Phrase(    "إحــــــالة"     ,boldfontLabelAmiri13);
												}
				       						  
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);

				       						break;
				       						
				       					case "AE":
				       						p1 =  new Phrase(    "تاريخ  الحكم"     ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
									         p1 =  new Phrase(   lesAffaires.get(j).getDateEmissionDocument().toString()  ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
				       						 p1 =  new Phrase( "استئناف الطفل"   ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);

				       						break;	
				       						
				       					case "AP":
				       						p1 =  new Phrase(    "تاريخ  الحكم"     ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
									         p1 =  new Phrase(   lesAffaires.get(j).getDateEmissionDocument().toString()  ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
				       						 p1 =  new Phrase(   "استئناف النيابة"     ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);

				       						break;
 
				       					case "CR":
				       						p1 =  new Phrase(    "تاريخ  الحكم"     ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
									         p1 =  new Phrase(   lesAffaires.get(j).getDateEmissionDocument().toString()  ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
				       						 p1 =  new Phrase(    "مراجعة"   ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);

				       						break;	
				       						
				       					case "CRR":
				       						p1 =  new Phrase(    "تاريخ  الحكم"     ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
									         p1 =  new Phrase(   lesAffaires.get(j).getDateEmissionDocument().toString()  ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
				       						 p1 =  new Phrase(    "رفض المراجعة"   ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);

				       						break;
				       						
				       					case "CP":
				       						p1 =  new Phrase(      "صدور البطاقة"      ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
									         p1 =  new Phrase(   lesAffaires.get(j).getDateEmissionDocument().toString()  ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
				       						 p1 =  new Phrase(    "قرار تمديد"   ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);

				       						break;
				       						
				       					case "CHL":
				       						p1 =  new Phrase(      "تاريخ القضية"      ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
									         p1 =  new Phrase(   lesAffaires.get(j).getDateEmissionDocument().toString()  ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
				       						 p1 =  new Phrase(    "تغير مكان الإيداع"   ,boldfontLabelAmiri13);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);

				       						break;
				       					default:
				       						 p1 =  new Phrase( boldConf.format("--")  ,boldfontLabelAmiri);
				       						 c1 = new PdfPCell(p1);
									         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
									         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
									         c1.setColspan(50);
									         c1.setBorder(0);
									         tableTypeDocument.addCell(c1);
				       					 
				       					}
						        ;
						         
						         
						         
						         if(lesAffaires.get(j).getDateDebutPunition()!=null) {
						        	
						        	
						        	 p1 =  new Phrase(  lesAffaires.get(j).getDateDebutPunition().toString()   ,boldfontLabelAmiri13);
				        			 c1 = new PdfPCell(p1);
				        			 c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
							         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
							         c1.setColspan(50);
							         c1.setBorder(0);
							         tableTypeDocument.addCell(c1);
							         
							         p1 =  new Phrase(  "تاريخ البداية"   ,boldfontLabelAmiri13);
				        			 c1 = new PdfPCell(p1);
				        			 c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
							         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
							         c1.setColspan(50);
							         c1.setBorder(0);
							         tableTypeDocument.addCell(c1);
							         
						        	
						         }
						       
						         if(lesAffaires.get(j).getDateFinPunition()!=null &&  !lesAffaires.get(j).getTypeDocument().toString().equals("AP")) {
						        	
								         
						        	 p1 =  new Phrase(  lesAffaires.get(j).getDateFinPunition().toString()   ,boldfontLabelAmiri13);
				        			 c1 = new PdfPCell(p1);
				        			 c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
							         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
							         c1.setColspan(50);
							         c1.setBorder(0);
							         tableTypeDocument.addCell(c1);
							         
							         p1 =  new Phrase(  "تاريخ النهاية"  ,boldfontLabelAmiri13);
				        			 c1 = new PdfPCell(p1);
				        			 c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
							         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
							         c1.setColspan(50);
							         c1.setBorder(0);
							         tableTypeDocument.addCell(c1);
							         
						           
						         }
						         
						  
						         
						         if(lesAffaires.size()>1) {
						        	 c1 = new PdfPCell(tableTypeDocument);
							         c1.setBorderWidth(1);
							         c1.setBackgroundColor(new BaseColor(240, 240, 240));
				        			   c1.setColspan(28);
				        			 tableAffByEnfant.addCell(c1);
				        			 
				        			 
				        			 if(j<9) {
				        				 p1 =  new Phrase(  "0"+(j+1) ,boldfontLabelAmiri9 ); 
				        			 }
				        			 else {
				        				 p1 =  new Phrase(  (j+1)+"" ,boldfontLabelAmiri9 ); 
				        			 }
				        			 
				        			
								     c1 = new PdfPCell(p1);
								     c1.setColspan(3);
				        			 tableAffByEnfant.addCell(c1);
							         
				        			 tableTypeDocument = new PdfPTable(100);
				        			 tableTypeDocument.setWidthPercentage(100);	 
						         }
						         else {
						        	 c1 = new PdfPCell(tableTypeDocument);
							         c1.setBorderWidth(1);
							         c1.setBackgroundColor(new BaseColor(240, 240, 240));
				        			   c1.setColspan(31);
				        			 tableAffByEnfant.addCell(c1);
						         }
						         
						         
					         
					      
		        			 }
		        			   c1 = new PdfPCell(tableAffByEnfant);
		        			   
		        			   c1.setColspan(60);
		        			   c1.setBorderColor( BaseColor.BLACK);
		        			  
		        			   c1.setBorderWidth(1);
						       tableAffaire.addCell(c1);
						       
						     
		        		 }
		        		 else {
		        			 p1 =  new Phrase(  "لم يتم إدراج أي قضية "     ,boldfontFamielle);
		        		 
		        		
				         c1 = new PdfPCell(p1);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				     
				         c1.setColspan(60);
				         tableAffaire.addCell(c1);
		        		 }
		        		 
		        		 
		        		 LocalDate dob = LocalDate.parse(enfantAffiche.get(i).getArrestation().getEnfant().getDateNaissance().toString());
				         
				          LocalDate curDate = LocalDate.now();  
				         Period period = Period.between(dob, curDate);
					         
					         PdfPTable tableIdentite = new PdfPTable(100);
					         tableIdentite.setWidthPercentage(100); 
						     
						     
					       
						         
						         
						        	 p1 =  new Phrase(boldConf.format(  enfantAffiche.get(i).getArrestation().getEnfant().getId() ),boldfontLabelAmiri14 ); 
						        
							     c1 = new PdfPCell(p1);
							     c1.setBackgroundColor(new BaseColor(225, 225, 225));
							     c1.setBorder(3);
						         c1.setBorderColor(new BaseColor(190,190,190));
							     c1.setHorizontalAlignment(Element.ALIGN_CENTER);
							     c1.setBorderColorTop(BaseColor.BLACK);
						         c1.setBorderWidthTop(1);
						         c1.setColspan(100);
						         tableIdentite.addCell(c1);
					         
					         
					         
					         
					         
					         
			        	  p1 =  new Phrase( enfantAffiche.get(i).getArrestation().getEnfant().getNom()+" بن "+ enfantAffiche.get(i).getArrestation().getEnfant().getNomPere()+" بن "+ enfantAffiche.get(i).getArrestation().getEnfant().getNomGrandPere()+" "+ enfantAffiche.get(i).getArrestation().getEnfant().getPrenom()    ,boldfontFamielle);
				         c1 = new PdfPCell(p1);
				        
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setColspan(100);
				         c1.setBorder(3);
				         c1.setBorderColor(new BaseColor(190,190,190));
				         tableIdentite.addCell(c1);
				         
				            
				          
				         p1 =  new Phrase(  
				                 enfantAffiche.get(i).getArrestation().getEnfant().getAdresse().toString().trim() 
			        	     	 +" "+
			        	     	enfantAffiche.get(i).getArrestation().getEnfant().getDelegation().getLibelle_delegation().toString()
			        	     	 +" "+
			        	     	enfantAffiche.get(i).getArrestation().getEnfant().getGouvernorat().getLibelle_gouvernorat().toString()	
			        		 
			        		  ,boldfontLabelAmiri14);
			         c1 = new PdfPCell(p1);
			         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);        
			              c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			         c1.setColspan(100);
			         c1.setBorder(3);
			         c1.setBorderColor(new BaseColor(190,190,190));
			       
			         tableIdentite.addCell(c1);
				         
				         p1 =  new Phrase(boldConf.format("بـــــــ"+ enfantAffiche.get(i).getArrestation().getEnfant().getLieuNaissance()  )+" "+ enfantAffiche.get(i).getArrestation().getEnfant().getDateNaissance(),boldfontLabelAmiri14);
				         c1 = new PdfPCell(p1);
				         c1.setBorder(3);
				         c1.setBorderColor(new BaseColor(190,190,190));
				       
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setColspan(100);
				         tableIdentite.addCell(c1);
				        
				         
				         if(enfantAffiche.get(i).getArrestation().getEnfant().getSituationSocial()!=null) {
				        	  p1 =  new Phrase( enfantAffiche.get(i).getArrestation().getEnfant().getSituationSocial().getLibelle_situation_social().toString().trim() +" "+
				        			  enfantAffiche.get(i).getArrestation().getEnfant().getSituationFamiliale().getLibelle_situation_familiale().toString().trim() 
					        		 ,boldfontLabelAmiri14);   
				        	  }
				         
				           else {
				        	   p1 =  new Phrase(enfantAffiche.get(i).getArrestation().getEnfant().getSituationFamiliale().getLibelle_situation_familiale().toString().trim() 
						        		 ,boldfontLabelAmiri14);
				           }
				         
				         
				         c1 = new PdfPCell(p1);
				         c1.setBorder(3);
				         c1.setBorderColor(new BaseColor(190,190,190));
				       
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setColspan(100);
				         tableIdentite.addCell(c1);
				        
				         if(enfantAffiche.get(i).getArrestation().getEnfant().getMetier()!=null) {
				        	  p1 =  new Phrase(enfantAffiche.get(i).getArrestation().getEnfant().getMetier().getLibelle_metier().toString().trim()+" "+
				        			  enfantAffiche.get(i).getArrestation().getEnfant().getNiveauEducatif().getLibelle_niveau_educatif().toString().trim() 
						        		 ,boldfontLabelAmiri14);
				         }
				        
				         else{
				        	 p1 =  new Phrase(" "+enfantAffiche.get(i).getArrestation().getEnfant().getNiveauEducatif().getLibelle_niveau_educatif().toString().trim() 
					        		 ,boldfontLabelAmiri14);
				         }
				         c1 = new PdfPCell(p1);
				         c1.setBorder(3);
				         c1.setBorderColor(new BaseColor(190,190,190));
				       
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setColspan(100);
				         tableIdentite.addCell(c1);
				         
 
				        	  p1 =  new Phrase( enfantAffiche.get(i).getArrestation().getDate() +" "+ "تاريخ الإيقاف "  ,boldfontFamielle13);
			        			 c1 = new PdfPCell(p1);
						         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						         c1.setBorder(0);
						         
						         c1.setBackgroundColor( BaseColor.WHITE);
						         c1.setColspan(100);
	 					       tableIdentite.addCell(c1);
	 					       
	 					       
	 					       
	 					      if(enfantAffiche.get(i).getDateEntree()!=null  && enfantAffiche.get(i).getEtablissementEntree()!=null  && enfantAffiche.get(i).getCauseMutation()!=null ) {
	 					       
	 					      p1 =  new Phrase( enfantAffiche.get(i).getDateEntree().toString()  +" "+ "قدم من"+" "+ enfantAffiche.get(i).getEtablissementEntree().getLibelle_etablissement().toString() +" ("+ enfantAffiche.get(i).getCauseMutation().getLibelle_causeMutation().toString()+") يوم "  ,boldfontFamielle13);
			        			 c1 = new PdfPCell(p1);
						         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						         c1.setBorder(0);
						         
						         c1.setBackgroundColor( BaseColor.WHITE);
						         c1.setColspan(100);
	 					       tableIdentite.addCell(c1);
	 					      }
	 					     if(enfantAffiche.get(i).getDateSortie()!=null && enfantAffiche.get(i).getEtablissementSortie()!=null  && enfantAffiche.get(i).getCauseMutationSortie()!=null ) {
		 					       
		 					      p1 =  new Phrase( enfantAffiche.get(i).getDateSortie().toString()  +" "+ "نقل  إلى"+" "+ enfantAffiche.get(i).getEtablissementSortie().getLibelle_etablissement().toString()+" ("+ enfantAffiche.get(i).getCauseMutationSortie().getLibelle_causeMutation().toString()+") يوم "  ,boldfontFamielle13);
				        			 c1 = new PdfPCell(p1);
							         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
							         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
							         c1.setBorder(0);
							         
							         c1.setBackgroundColor( BaseColor.WHITE);
							         c1.setColspan(100);
		 					       tableIdentite.addCell(c1);
		 					      }
	 					    if(enfantAffiche.get(i).getStatut()==2 ) {
		 					       
		 					      p1 =  new Phrase(   "لم يتم الإستقبال بعد "   ,boldfontLabel);
				        			 c1 = new PdfPCell(p1);
							         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
							         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
							         c1.setBorder(0);
							         
							         c1.setBackgroundColor( BaseColor.WHITE);
							         c1.setColspan(100);
		 					       tableIdentite.addCell(c1);
		 					      }
	 					     
	 					      if(enfantAffiche.get(i).getArrestation().getLiberation()==null) {
	 					      p1 =  new Phrase(   " "  ,boldfontFamielle13);
			        			 c1 = new PdfPCell(p1);
						         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						         c1.setBorder(0);
						        c1.setBorderColorBottom(BaseColor.BLACK);
						         c1.setBorderWidthBottom(1);
						         c1.setBackgroundColor(BaseColor.WHITE);
						         c1.setColspan(100);
	 					       tableIdentite.addCell(c1);
	 					      }
//				         }  
				        
			         
				         if(enfantAffiche.get(i).getArrestation().getLiberation()!=null) {
				        	  p1 =  new Phrase(  enfantAffiche.get(i).getArrestation().getLiberation().getDate().toString() +" "+enfantAffiche.get(i).getArrestation().getLiberation().getCauseLiberation().getLibelleCauseLiberation()   ,boldfontFamielle13);
			        			 c1 = new PdfPCell(p1);
						         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						         c1.setBorder(0);
						         
						         c1.setBackgroundColor( BaseColor.WHITE);
						         c1.setColspan(100);
	 					       tableIdentite.addCell(c1);
	 					      p1 =  new Phrase(   " "  ,boldfontFamielle13);
			        			 c1 = new PdfPCell(p1);
						         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						         c1.setBorder(0);
						        c1.setBorderColorBottom(BaseColor.BLACK);
						         c1.setBorderWidthBottom(1);
						         c1.setBackgroundColor( BaseColor.WHITE);
						         c1.setColspan(100);
	 					       tableIdentite.addCell(c1);
	 					     
				         }
				         
				         
				         
				         
				         if(pDFListExistDTO.getEtatJuridiue().toString().equals("seraLibere")) {
 
 				        	  p1 =  new Phrase( enfantAffiche.get(i).getDateFin().toString() +" "+ "يفرج عنه بتاريخ"  ,boldfontFamielle13);
			        			 c1 = new PdfPCell(p1);
						         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						         c1.setBorder(0);
						         
						         c1.setBackgroundColor( BaseColor.WHITE);
						         c1.setColspan(100);
	 					       tableIdentite.addCell(c1);
	 					      p1 =  new Phrase(   " "  ,boldfontFamielle13);
			        			 c1 = new PdfPCell(p1);
						         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						         c1.setBorder(0);
						        c1.setBorderColorBottom(BaseColor.BLACK);
						         c1.setBorderWidthBottom(1);
						         c1.setBackgroundColor(BaseColor.WHITE);
						         c1.setColspan(100);
	 					       tableIdentite.addCell(c1);
	 					     
				         }
   
				         
				        
 
				         
				       
				        	
				       
				         
				         c1 = new PdfPCell(tableIdentite);
				         c1.setBorderWidth(1);
	        			   c1.setColspan(30);
	        			   c1.setBorderWidth(1);
	        			  c1.setBackgroundColor( new BaseColor(250, 250, 250));
					       tableAffaire.addCell(c1);
					       
					       
					       
				         
				         p1 =  new Phrase( enfantAffiche.get(i).getNumArrestation()   ,boldfontLabelAmiri14);
				         c1 = new PdfPCell(p1);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setBackgroundColor(new BaseColor(225, 225, 225));
				        c1.setBorder(0);
				         c1.setColspan(100);
				         tableNumArr.addCell(c1);
				        
				      
				         
				         
				         
				         p1 =  new Phrase( enfantAffiche.get(i).getEtablissement().getLibelle_etablissement() ,boldfontLabelAmiri14);
				         c1 = new PdfPCell(p1);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setBackgroundColor(new BaseColor(225, 225, 225));
				         c1.setBorder(0);
				         c1.setColspan(100);
				         tableNumArr.addCell(c1);
				         
				         p1 =  new Phrase( enfantAffiche.get(i).getArrestation().getEnfant().getClassePenale().getLibelle_classe_penale()  ,boldfontLabelAmiri14);
				         c1 = new PdfPCell(p1);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setColspan(100);
				         tableNumArr.addCell(c1);
				         
				         p1 =  new Phrase( enfantAffiche.get(i).getArrestation().getEnfant().getNationalite().getLibelle_nationalite() ,boldfontLabelAmiri14);
				         c1 = new PdfPCell(p1);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setColspan(100);
				         c1.setBackgroundColor(new BaseColor(245,245,245)); 
				         tableNumArr.addCell(c1);
				        
				         p1 =  new Phrase("عام"   ,boldfontLabelAmiri9);
				         c1 = new PdfPCell(p1);
				         c1.setBorder(0);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setColspan(25);
				         tableNumArr.addCell(c1);
				         p1 =  new Phrase(period.getYears()+""  ,boldfontFamielle13);
				         c1 = new PdfPCell(p1);
				         c1.setBorder(0);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setColspan(30);
				         tableNumArr.addCell(c1);
				         
				         p1 =  new Phrase("السن"   ,boldfontLabelAmiri14);
				         c1 = new PdfPCell(p1);
				         c1.setBorder(0);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				         c1.setColspan(45);
				         tableNumArr.addCell(c1);
				         if(enfantAffiche.get(i).getArrestation().getLiberation()==null) {
				         List<Affaire> aData = documentRepository.findStatutJurByArrestation(enfantAffiche.get(i).getResidenceId().getIdEnfant() );
			    		 
				    		if (aData.isEmpty()) {
				    			
				    			if(lesAffaires.size()>0) {
				    				p1 =  new Phrase(  "محكـــوم"   ,boldfontFamielle);
				    				boolean  allSameName = lesAffaires.stream().allMatch(x -> x.getTypeDocument().equals("AEX"));
					        		if (allSameName) {
					        			p1 =  new Phrase(  "لم يتم إدراج السراح"   ,boldfontFamielle);
						    		} 
				    			}
				    			else {
				    				p1 =  new Phrase(  "..."   ,boldfontFamielle);
				    			}
				    		          	
				    		} else {
				    			   p1 =  new Phrase( "موقـــوف"  ,boldfontFamielle);	
								    
				    		}
				         }
				         else {
				        	  p1 =  new Phrase( "في حالـــة ســراح"  ,boldfontFamielle);	
				         }
				    	     c1 = new PdfPCell(p1);
					         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
					         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					         c1.setColspan(100);
					         c1.setBorder(1);
					         tableNumArr.addCell(c1);
				        
					         Echappes e = echappesRepository.findByIdEnfantAndResidenceTrouverNull(enfantAffiche.get(i).getResidenceId().getIdEnfant());
					         if(e!=null) {
					        	 p1 =  new Phrase( "في حالــــــة فـــرار"  ,boldfontFamielle);	
					        	 c1 = new PdfPCell(p1);
						         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						         c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						         c1.setColspan(100);
						         c1.setBorder(1);
						         tableNumArr.addCell(c1);
					         }
				        
				        c1 = new PdfPCell(tableNumArr);
				        c1.setBackgroundColor( BaseColor.WHITE);
				        c1.setColspan(7);
	        		    tableAffaire.addCell(c1);
				         
				         
				         
				         p1 =  new Phrase( ( i+1)+""    ,boldfontFamielle11);
				         c1 = new PdfPCell(p1);
				         c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				         c1.setBackgroundColor(new BaseColor(225, 225, 225));
				         c1.setBorderWidth(1);
				         c1.setColspan(3);
				         tableAffaire.addCell(c1);
		        	 }
		        	 
 
		       // 	  } 
		        	 
				}
	    	 
 
	 
	        
	         
	         document.add(tableTop);
	         document.add(tTitre);
	       
	          document.add(tableAffaire);
	   
	        document.close();
	       
			return new ByteArrayInputStream(out.toByteArray());
	     
		}
	      
}

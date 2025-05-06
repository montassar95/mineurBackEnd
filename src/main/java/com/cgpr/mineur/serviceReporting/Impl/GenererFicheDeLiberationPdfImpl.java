package com.cgpr.mineur.serviceReporting.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.config.ConfigShaping;
import com.cgpr.mineur.converter.TitreAccusationConverter;
import com.cgpr.mineur.converter.TypeJugeConverter;
import com.cgpr.mineur.dto.AffaireDto;
import com.cgpr.mineur.dto.ArretProvisoireDto;
import com.cgpr.mineur.dto.FicheDeDetentionDto;
import com.cgpr.mineur.dto.DocumentSearchCriteriaDto;
import com.cgpr.mineur.dto.FicheDeDetentionDto;
import com.cgpr.mineur.dto.TitreAccusationDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Echappes;
import com.cgpr.mineur.models.Photo;
import com.cgpr.mineur.models.PhotoId;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.EchappesRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.resource.PDFPenaleDTO;
import com.cgpr.mineur.service.PhotoService;
import com.cgpr.mineur.service.Impl.AffaireServiceImpl;
import com.cgpr.mineur.serviceReporting.GenererFicheDeDetentionPdfService;
import com.cgpr.mineur.serviceReporting.GenererFicheDeLiberationPdfService;
import com.cgpr.mineur.tools.ToolsForReporting;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.kernel.colors.Color;
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
 
public class GenererFicheDeLiberationPdfImpl implements GenererFicheDeLiberationPdfService {

 

	@Autowired
	private AffaireRepository affaireRepository;
 

	 

	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private EchappesRepository echappesRepository;
	
	@Autowired
	private AffaireServiceImpl affaireServiceImpl;
 
	
	@Autowired
	private PhotoService photoService;

 

	public static final Font FONT = new Font();
	public static final Font BOLD = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
	SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
	public static   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	@Override
	public ByteArrayInputStream genererFicheDeLiberationPdf(PDFPenaleDTO pDFPenaleDTO)
			throws DocumentException, IOException, ArabicShapingException {
		
		 
		FicheDeDetentionDto ficheDeDetentionDto =affaireServiceImpl.obtenirInformationsDeDetentionParIdDetention(pDFPenaleDTO.getIdEnfant(), pDFPenaleDTO.getNumOrdinale());
		AffaireDto affairePricipale = trouverAffairePrincipale(ficheDeDetentionDto.getAffaires());
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Rectangle layout = new Rectangle(PageSize.A4);

		Document document = new Document(layout);
		PdfWriter pdf = PdfWriter.getInstance(document, out);
		
 
        
		document.open();

		URL backgroundUrl = GenererFicheDeDetentionPdfService.class.getResource("/images/page.jpg");

		Image backgroundImage = Image.getInstance(backgroundUrl);

		 

		pdf.getDirectContentUnder().addImage(backgroundImage, 600, 0, 0, 500, 0, 120);

		ConfigShaping boldConf = new ConfigShaping();

		Font boldfontLabelTop = boldConf.getFontForArabicAmiri1(12);
		PdfPTable tab = new PdfPTable(1);
		tab.setWidthPercentage(100);

		Phrase ptab;
		PdfPCell ctab;

		ptab = new Phrase("الجمهورية التونسية ", boldfontLabelTop);
		ctab = new PdfPCell(ptab);
		ctab.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
		ctab.setBorder(0);
		ctab.setPaddingRight(27f);
		tab.addCell(ctab);

		ptab = new Phrase("وزارة العدل ", boldfontLabelTop);
		ctab = new PdfPCell(ptab);
		ctab.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
		ctab.setBorder(0);
		ctab.setPaddingRight(36f);
		tab.addCell(ctab);

		ptab = new Phrase("الهيئة العامة للسجون والإصلاح ", boldfontLabelTop);
		ctab = new PdfPCell(ptab);
		ctab.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
		ctab.setBorder(0);
		tab.addCell(ctab);
		
		ptab = new Phrase("مركز إصلاح الأطفال الجانحين", boldfontLabelTop);
		ctab = new PdfPCell(ptab);
		ctab.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
		ctab.setBorder(0);
		tab.addCell(ctab);
		       
		ptab = new Phrase("بـــــ"+ficheDeDetentionDto. getResidences().get(0).getEtablissement().getLibelle_etablissement(), boldfontLabelTop);
		ctab = new PdfPCell(ptab);
		ctab.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
		ctab.setBorder(0);
		ctab.setPaddingRight(30f);
		tab.addCell(ctab);
		
		
		ptab = new Phrase("عــــدد/......................../ص", boldfontLabelTop);
		ctab = new PdfPCell(ptab);
		ctab.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		ctab.setHorizontalAlignment(Element.ALIGN_RIGHT);
		ctab.setBorder(0);
		tab.addCell(ctab);
		
		
		PdfPTable tableTop = new PdfPTable(3);
		tableTop.setWidthPercentage(100);

		Phrase p1Top;
		PdfPCell c1Top;

//		       ---------------  nom --------------------

		LocalDate localDate = LocalDate.now();

		p1Top = new Phrase(dtf.format(localDate) + " " + "تونس في", boldfontLabelTop);
		c1Top = new PdfPCell(p1Top);
		c1Top.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1Top.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1Top.setBorder(0);
		tableTop.addCell(c1Top);

		URL resource = GenererFicheDeDetentionPdfService.class.getResource("/images/cgpr.png");

		Image image = Image.getInstance(resource);

		image.scaleAbsolute(70f, 70f);
		c1Top = new PdfPCell(image);
		c1Top.setHorizontalAlignment(Element.ALIGN_CENTER);

		c1Top.setBorder(0);

		tableTop.addCell(c1Top);
		c1Top = new PdfPCell(tab);
		c1Top.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1Top.setBorder(0);
		tableTop.addCell(c1Top);

		tableTop.setSpacingAfter(18f);

		// Titre
		Font boldfontTitle = boldConf.getFontForArabicAmiri1(30);
		Font boldfontLabel18 = boldConf.getFontForArabic(20);
		Font boldfontLabel = boldConf.getFontForArabic(16);
		Font boldfontFamielle = boldConf.getFontForArabic(14);
		Font boldfontLabelEtat = boldConf.getFontForArabic(18);
		Font boldfontLabelAmiri = boldConf.getFontForArabicAmiri(17);
		Font boldfontLabelAmirix = boldConf.getFontForArabicAmiri(16);
		PdfPTable tTitre = new PdfPTable(1);

		String titre = "بطاقـــــــــــــــــة خـــــــــــــــروج";
		 

		Phrase pTitre = new Phrase(titre, boldfontTitle);

		PdfPCell cTitre = new PdfPCell(pTitre);
		cTitre.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

		cTitre.setPaddingBottom(20f);
 	cTitre.setBorder(Rectangle.BOX);

		cTitre.setBorderWidth(  0.5F);

		cTitre.setBackgroundColor(new BaseColor(240, 240, 240)); // Couleur de fond plus claire

		cTitre.setBorderColor(BaseColor.BLACK);

		cTitre.setHorizontalAlignment(Element.ALIGN_CENTER);

		tTitre.addCell(cTitre);
		tTitre.setWidthPercentage(60);
		
		
		
		

	 

		tTitre.setSpacingAfter(15f);

		// arre
		Font boldfontArr = boldConf.getFontForArabicArr(16);

		 

		 

		Phrase p1;
		PdfPCell c1;

		PdfPCell spaceCell = new PdfPCell(new Phrase("  "));
		spaceCell.setBorder(0);
		 

		PdfPTable table = new PdfPTable(100);

		table.setWidthPercentage(100);

//	       ---------------  nom --------------------

		 
		p1 = new Phrase(
				ficheDeDetentionDto.getArrestation().getEnfant().getNom() + " بن " + ficheDeDetentionDto.getArrestation().getEnfant().getNomPere() + " بن "
						+ ficheDeDetentionDto.getArrestation().getEnfant().getNomGrandPere() + " " + ficheDeDetentionDto.getArrestation().getEnfant().getPrenom(),
				boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(65);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase(":", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase("الهـــــــــــــــــــــــــــــــــــوية", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);

		c1.setColspan(30);

		c1.setPaddingBottom(7f);
		table.addCell(c1);

//		       ---------------  mere --------------------  

		p1 = new Phrase(ficheDeDetentionDto.getArrestation().getEnfant().getNomMere() + " " + ficheDeDetentionDto.getArrestation().getEnfant().getPrenomMere(),
				boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(65);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase(":", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase("إبــــــــــــــــــــــــــــــــــــــن ", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

//		       ---------------   Lieu et lieu --------------------	    
		
		
     p1 = new Phrase(   "بــــــــــ" + ficheDeDetentionDto.getArrestation().getEnfant().getLieuNaissance() 
				
				, boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(48);
		c1.setPaddingBottom(7f);
		table.addCell(c1);
		p1 = new Phrase( 
				 ficheDeDetentionDto.getArrestation().getEnfant().getDateNaissance().toString() +"  " 
				, boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(17);
		c1.setPaddingBottom(7f);
		table.addCell(c1);
		
		p1 = new Phrase(":", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase("تــــــاريخ الـولادة ومكانهـــــا", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

//		       ---------------  nationnalite --------------------      

		p1 = new Phrase(ficheDeDetentionDto.getArrestation().getEnfant().getNationalite().getLibelle_nationalite().toString(),
				boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(65);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase(":", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase("الجنسيــــــــــــــــــــــــــــــة", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

 
 
 

//		       --------------- situation penal --------------------      
		

		
		
     

 
	    
		 
		p1 = new Phrase( (ficheDeDetentionDto.getArrestation().getDate().toString()), boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(65);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase( (":"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase( ("تــــاريخ الإيـــداع بالمــــركز"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

 

//		       --------------- tribunal--------------------      
	 
		 
			
			 
			p1 = new Phrase( (ficheDeDetentionDto.getArrestation().getLiberation().getDate().toString()), boldfontLabelAmiri);
	 		c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(65);
			c1.setPaddingBottom(7f);

			table.addCell(c1);

			p1 = new Phrase( (":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);

			table.addCell(c1);
			  
			p1 = new Phrase( ("تاريخ المغــــــــادرة"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
			   
			String libelleChangeManiere = (ficheDeDetentionDto.getArrestation().getLiberation().getEtabChangeManiere() != null 
					&& ficheDeDetentionDto.getArrestation().getLiberation().getEtabChangeManiere().getLibelle_etabChangeManiere() != null)
				    ?" إلى "+ ficheDeDetentionDto.getArrestation().getLiberation().getEtabChangeManiere().getLibelle_etabChangeManiere() 
				    : "";

				    String statutJudiciaire = ficheDeDetentionDto.getArrestation().getLiberation().getCauseLiberation().getLibelleCauseLiberation() + " " 
				    + libelleChangeManiere ;
			
			  
			 
				p1 = new Phrase(statutJudiciaire, boldfontLabelEtat);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBorder(0);
				c1.setColspan(65);
				c1.setPaddingBottom(7f);
				table.addCell(c1);

				p1 = new Phrase(":", boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(0);
				c1.setColspan(5);

				table.addCell(c1);

				p1 = new Phrase("  المــــوجــــــــــــب  ", boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBorder(0);
				c1.setColspan(30);
				c1.setPaddingBottom(12f);
				table.addCell(c1);
				

//		       --------------- tribunal--------------------      

		if (!(ficheDeDetentionDto.getAnneeArret() == 0 && ficheDeDetentionDto.getMoisArret() == 0 && ficheDeDetentionDto.getJourArret() == 0)) {

			String arretA = " ";
			String arretM = " ";
			String arretJ = " ";

			if (ficheDeDetentionDto.getAnneeArret() != 0) {
				if (ficheDeDetentionDto.getAnneeArret() == 1) {
					arretA = " " + "عام" + " ";
				} else if (ficheDeDetentionDto.getAnneeArret() == 2) {
					arretA = " " + "عامين" + " ";
				} else if ((ficheDeDetentionDto.getAnneeArret() >= 3) && (ficheDeDetentionDto.getAnneeArret() <= 10)) {
					arretA = ficheDeDetentionDto.getAnneeArret() + " " + "أعوام" + " ";
				} else {
					arretA = ficheDeDetentionDto.getAnneeArret() + " " + "عام" + " ";
				}

				if (ficheDeDetentionDto.getMoisArret() != 0 || ficheDeDetentionDto.getJourArret() != 0) {
					arretA = arretA + " و ";
				}
			}

			if (ficheDeDetentionDto.getMoisArret() != 0) {
				if (ficheDeDetentionDto.getMoisArret() == 1) {
					arretM = " " + "شهر" + " ";
				} else if (ficheDeDetentionDto.getMoisArret() == 2) {
					arretM = " " + "شهرين" + " ";
				} else if ((ficheDeDetentionDto.getMoisArret() >= 3) && (ficheDeDetentionDto.getMoisArret() <= 10)) {
					arretM = ficheDeDetentionDto.getAnneeArret() + " " + "أشهر" + " ";
				} else {
					arretM = ficheDeDetentionDto.getMoisArret() + "  " + "شهر" + " ";
				}

				if (ficheDeDetentionDto.getJourArret() != 0) {
					arretM = arretM + " و ";
				}
			}

			if (ficheDeDetentionDto.getJourArret() != 0) {
				if (ficheDeDetentionDto.getJourArret() == 1) {
					arretJ = " " + "يوم " + " ";
				} else if (ficheDeDetentionDto.getJourArret() == 2) {
					arretJ = " " + "يومين" + " ";
				} else if ((ficheDeDetentionDto.getJourArret() >= 3) && (ficheDeDetentionDto.getJourArret() <= 10)) {
					arretJ = ficheDeDetentionDto.getJourArret() + " " + "أيام" + " ";
				} else {
					arretJ = ficheDeDetentionDto.getJourArret() + "  " + "يوم" + " ";
				}

			}

			 
			for (ArretProvisoireDto ap : ficheDeDetentionDto.getArretProvisoires()) {
				String duree = "";
				duree = duree + "من  " + outputFormat.format( ap.getDateDebut() )+ " إلى  " + outputFormat.format (ap.getDateFin()) + "\n";
				p1 = new Phrase(duree, boldfontFamielle);
			}

			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(40);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
			p1 = new Phrase( (arretA) +  (arretM) +  (arretJ),
					boldfontLabelAmiri);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(25);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			p1 = new Phrase( (":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);

			table.addCell(c1);

			p1 = new Phrase( ("إيقــــــاف تحفظـــــــــــــــي"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

		}
		
		table.setSpacingAfter(30f);

		PdfPTable tableAffaire = new PdfPTable(100);

		tableAffaire.setWidthPercentage(100);

		 
		if (!pDFPenaleDTO.isSansDetail()) {

//		       ---------------  nom --------------------

			 
			List<AffaireDto> affaireAffiche =ficheDeDetentionDto.getAffaires();

			
			p1 = new Phrase( "المحكمـــــــــــــــة ", boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(new BaseColor(210, 210, 210));
			c1.setColspan(70);
			tableAffaire.addCell(c1);
			
			
			

			p1 = new Phrase( ("عدد القضية") , boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(new BaseColor(210, 210, 210));
			c1.setColspan(20);
			tableAffaire.addCell(c1);
			
			
			
			p1 = new Phrase("ع/ر"  , boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBackgroundColor(new BaseColor(210, 210, 210));
			c1.setColspan(10);
			tableAffaire.addCell(c1);
			for (int i = 0; i < affaireAffiche.size(); i++) {

				 

				
				
				p1 = new Phrase(  affaireAffiche.get(i).getTribunal().getNom_tribunal(), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);

				c1.setColspan(70);
				tableAffaire.addCell(c1);
				
				
				

				p1 = new Phrase( (affaireAffiche.get(i).getAffaireId().getNumAffaire()) , boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);

				c1.setColspan(20);
				tableAffaire.addCell(c1);
				
				
				
				p1 = new Phrase((i + 1) + " "  , boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				//c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(10);
				tableAffaire.addCell(c1);
			


				 

			 
				 
				
				 
 

			}

		}
		Rectangle rect = new Rectangle(15, 20, 580, 690);
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(1);
      
		document.add(tableTop);
		document.add(tTitre);
		document.add(rect);
 

		document.add(table);
		document.add(tableAffaire);
		// step 5
		document.close();

		return new ByteArrayInputStream(out.toByteArray());

	}

 

	 

	 

 

 
	 public AffaireDto trouverAffairePrincipale(List<AffaireDto> liste) {
	        for (AffaireDto affaire : liste) {
	            if (affaire.isAffairePrincipale()) {
	                return affaire; // Retourne la première affaire principale trouvée
	            }
	        }
	        return null; // Retourne null si aucune affaire principale n'est trouvée
	    }
	 

 

	 

 

	 

	 
 

 
	 

}

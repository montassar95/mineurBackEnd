package com.cgpr.mineur.serviceReporting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.config.ConfigShaping;
import com.cgpr.mineur.dto.CalculeAffaireDto;
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
import com.cgpr.mineur.tools.ToolsForReporting;
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
 
public class EnfantServiceImpl1 implements EnfantService1 {

 

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
	
	@Autowired
	private AffaireServiceImpl affaireServiceImpl;
 
	
	@Autowired
	private PhotoService photoService;

	public List<Affaire> findByArrestationOnePage(String idEnfant, long numOrdinale) {
		
		

		List<Affaire> lesAffaires = affaireRepository.findByArrestationCoroissant(idEnfant, numOrdinale);

		System.out.println(idEnfant);
		System.out.println(numOrdinale);
		System.out.println(lesAffaires.size());

		List<Affaire> output = lesAffaires.stream().map(s -> {

			com.cgpr.mineur.models.Document doc = documentRepository.getLastDocumentByAffaire(idEnfant, numOrdinale,
					s.getNumOrdinalAffaire());

			s.setTypeDocument(doc.getTypeDocument());
			s.setDateEmissionDocument(doc.getDateEmission());
			if (doc instanceof Transfert) {
				Transfert t = (Transfert) doc;

				s.setTypeFile(t.getTypeFile());
				System.out.println("88888888888888888888888");
				System.out.println(t.getTypeFile());
			}
			if (doc instanceof Arreterlexecution) {
				Arreterlexecution t = (Arreterlexecution) doc;

				s.setTypeFile(t.getTypeFile());
				System.out.println("88888888888888888888888");
				System.out.println(t.getTypeFile());
			}
			List<com.cgpr.mineur.models.Document> accData = documentRepository.getDocumentByAffaireforAccusation(
					idEnfant, numOrdinale, s.getNumOrdinalAffaire(), PageRequest.of(0, 1));

			List<TitreAccusation> titreAccusations = null;

			if (accData.get(0) instanceof CarteRecup) {

				titreAccusations = accusationCarteRecupRepository
						.getTitreAccusationbyDocument(accData.get(0).getDocumentId());

				s.setTitreAccusations(titreAccusations);
				s.setDateEmission(accData.get(0).getDateEmission());

				CarteRecup c = (CarteRecup) accData.get(0);
				s.setAnnee(c.getAnnee());
				s.setMois(c.getMois());
				s.setJour(c.getJour());

				s.setAnneeArret(c.getAnneeArretProvisoire());
				s.setMoisArret(c.getMoisArretProvisoire());
				s.setJourArret(c.getJourArretProvisoire());

				s.setTypeJuge(c.getTypeJuge());

			} else if (accData.get(0) instanceof CarteDepot) {
				titreAccusations = accusationCarteDepotRepository
						.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
				s.setTitreAccusations(titreAccusations);
				s.setDateEmission(accData.get(0).getDateEmission());
			} else if (accData.get(0) instanceof CarteHeber) {
				titreAccusations = accusationCarteHeberRepository
						.getTitreAccusationbyDocument(accData.get(0).getDocumentId());
				s.setTitreAccusations(titreAccusations);
				s.setDateEmission(accData.get(0).getDateEmission());
				System.out.println("CarteHeber.." + accData.get(0).getDocumentId());
			}

			return s;
		}).collect(Collectors.toList());

		return output;

	}

	public static final Font FONT = new Font();
	public static final Font BOLD = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
	public static   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	@Override
	public ByteArrayInputStream export(PDFPenaleDTO pDFPenaleDTO)
			throws DocumentException, IOException, ArabicShapingException {
		
		 
		CalculeAffaireDto calculeAffaireDto =affaireServiceImpl.calculerAffaire(pDFPenaleDTO.getIdEnfant(), pDFPenaleDTO.getNumOrdinale());
		
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Rectangle layout = new Rectangle(PageSize.A4);

		Document document = new Document(layout);
		PdfWriter p = PdfWriter.getInstance(document, out);

		document.open();

		URL xx = EnfantService1.class.getResource("/images/page.jpg");

		Image cc = Image.getInstance(xx);

		 

		p.getDirectContentUnder().addImage(cc, 600, 0, 0, 500, 0, 120);

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
		
		

		PdfPTable tableTop = new PdfPTable(3);
		tableTop.setWidthPercentage(100);
		 
		
		
		Phrase p1Top;
		PdfPCell c1Top;

//		       ---------------  nom --------------------

		LocalDate localDate = LocalDate.now();

		p1Top = new Phrase(boldConf.format(dtf.format(localDate)) + " " + "تونس في", boldfontLabelTop);
		c1Top = new PdfPCell(p1Top);
		c1Top.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1Top.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1Top.setBorder(0);
		tableTop.addCell(c1Top);

		URL resource = EnfantService1.class.getResource("/images/cgpr.png");

		Image image = Image.getInstance(resource);

		image.scaleAbsolute(70f, 70f);
		c1Top = new PdfPCell(image);
		c1Top.setHorizontalAlignment(Element.ALIGN_CENTER);

		c1Top.setBorder(0);
		;

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

		Phrase pTitre = new Phrase("مذكرة شخصية لطفل جانح", boldfontTitle);
		PdfPCell cTitre = new PdfPCell(pTitre);
		cTitre.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

		cTitre.setPaddingBottom(20f);
		cTitre.setBorder(Rectangle.BOX);

		cTitre.setBorderWidth(2);

		cTitre.setBackgroundColor(new BaseColor(210, 210, 210));

		cTitre.setBorderColor(BaseColor.BLACK);

		cTitre.setHorizontalAlignment(Element.ALIGN_CENTER);

		tTitre.addCell(cTitre);
		tTitre.setWidthPercentage(60);

		if (  !pDFPenaleDTO.isSansImage()) {
			 
			PhotoId photoId = new PhotoId();
			photoId.setIdEnfant(calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getId());
			photoId.setNumOrdinaleArrestation(calculeAffaireDto.getAffaires().get(0).getArrestation().getArrestationId().getNumOrdinale());
		    Optional<Photo> photo = photoService.getPhotoById(photoId);
		    if (photo.isPresent() && photo.get().getImg() != null) {
			  
			 
			   final String base64Data = photo.get().getImg()
						.substring( photo.get().getImg().indexOf(",") + 1);
				Image ima = Image.getInstance(Base64.decode(base64Data));
				ima.setAbsolutePosition(30f, 540f);
				ima.scaleAbsolute(120f, 120f);
				document.add(ima);
		   }
		    
			
		}

		tTitre.setSpacingAfter(15f);

		// arre
		Font boldfontArr = boldConf.getFontForArabicArr(16);

		PdfPTable tArr = new PdfPTable(100);

		tArr.setWidthPercentage(80);

		Phrase p1;
		PdfPCell c1;

		PdfPCell spaceCell = new PdfPCell(new Phrase("  "));
		spaceCell.setBorder(0);

//		       ---------------  nom --------------------
		if (calculeAffaireDto.getAffaires().get(0).getArrestation().getNumOrdinalAffairePricipale() < 10) {
			p1 = new Phrase(

					"0" + calculeAffaireDto.getAffaires().get(0).getArrestation().getArrestationId().getNumOrdinale() + "  /  " + calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getId(),
					boldfontLabelAmiri);
		}

		else {
			p1 = new Phrase(calculeAffaireDto.getAffaires().get(0).getArrestation().getArrestationId().getNumOrdinale() + "  /  " + calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getId(),
					boldfontLabelAmiri);
		}
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(55);

		tArr.addCell(c1);
		p1 = new Phrase(":", boldfontArr);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		tArr.addCell(c1);

		p1 = new Phrase("المعــــرف الوحيـــد", boldfontArr);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(40);

		tArr.addCell(c1);
		p1 = new Phrase(calculeAffaireDto. getResidences().get(0).getEtablissement().getLibelle_etablissement(), boldfontArr);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(55);

		tArr.addCell(c1);
		p1 = new Phrase(":", boldfontArr);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		tArr.addCell(c1);

		p1 = new Phrase("مــــــركز الإصــــــلاح", boldfontArr);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(40);

		tArr.addCell(c1);

		p1 = new Phrase(boldConf.format(calculeAffaireDto. getResidences().get(0).getNumArrestation()), boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(55);

		tArr.addCell(c1);

		p1 = new Phrase(":", boldfontArr);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		tArr.addCell(c1);
		p1 = new Phrase("عـــدد الإيقـــــــــاف", boldfontArr);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(40);

		tArr.addCell(c1);
		p1 = new Phrase(calculeAffaireDto.getAffaires().get(0).getArrestation().getNumAffairePricipale().toString(), boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(55);

		tArr.addCell(c1);
		p1 = new Phrase(":", boldfontArr);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		tArr.addCell(c1);

		if (calculeAffaireDto.getAffaires().size() < 10) {
			p1 = new Phrase("عـــــ" + "0" + calculeAffaireDto.getAffaires().size() + "ــدد القضيــــــة", boldfontLabelAmirix);

		} else {
			p1 = new Phrase("عـــــ" + calculeAffaireDto.getAffaires().size() + "" + "ـــــدد القضيــــــة",
					boldfontLabelAmirix);

		}
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(40);

		tArr.addCell(c1);

		
		String  arabicMajorityAgeDate =ToolsForReporting.getArabicMajorityAgeDate(calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getDateNaissance().toString()); 
		System.err.println(arabicMajorityAgeDate);
		 String[] partsDate = arabicMajorityAgeDate.split(" ");
		    String day = partsDate[0];
		    String month = partsDate[1];
		    String year = partsDate[2];
		    
		p1 = new Phrase(year.toString() + " " + month.toString(),
				boldfontLabelAmiri);

		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);

		c1.setBorder(0);

		c1.setColspan(49);

		tArr.addCell(c1);

		p1 = new Phrase(day.toString(), boldfontLabelAmiri);

		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);

		c1.setBorder(0);

		c1.setColspan(06);

		tArr.addCell(c1);

		p1 = new Phrase(":", boldfontArr);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(5);

		tArr.addCell(c1);
		
		p1 = new Phrase("ســـــ" + ToolsForReporting.calculerAge(calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getDateNaissance().toString().trim()) + "ـــن الرشـــــــــد",
				boldfontLabelAmirix);

		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(40);

		tArr.addCell(c1);

		tArr.setSpacingAfter(15f);

		PdfPTable table = new PdfPTable(100);

		table.setWidthPercentage(100);

//	       ---------------  nom --------------------

		p1 = new Phrase(
				calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getNom() + " بن " + calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getNomPere() + " بن "
						+ calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getNomGrandPere() + " " + calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getPrenom(),
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

		p1 = new Phrase(calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getNomMere() + " " + calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getPrenomMere(),
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

		p1 = new Phrase(boldConf.format("بــــــــــــــــــــ" + calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getLieuNaissance()) + " "
				+ calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getDateNaissance(), boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
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

		p1 = new Phrase("تــــــاريخ الـولادة ومكانهـــــا", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

//		       ---------------  nationnalite --------------------      

		p1 = new Phrase(calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getNationalite().getLibelle_nationalite().toString(),
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

//		       ---------------  situation fam --------------------      

		p1 = new Phrase(

				calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getSituationFamiliale().getLibelle_situation_familiale().toString(),
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
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase("الحـــــــــالة العائليـــــــــــــة", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

//		       --------------- adress --------------------      
		p1 = new Phrase(calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getAdresse().toString().trim() + " "
				+ calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getDelegation().getLibelle_delegation().toString() + " "
				+ calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getGouvernorat().getLibelle_gouvernorat().toString()

				, boldfontLabelAmiri);
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

		p1 = new Phrase("مكان الإقــامة قبـل الإيقــاف", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

//		       --------------- niveau edu --------------------      

		p1 = new Phrase(
				boldConf.format(calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getNiveauEducatif().getLibelle_niveau_educatif().toString()),
				boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
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

		p1 = new Phrase("المستــوى التعليمـــــــــــي", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

//		       --------------- situation penal --------------------      

		if (calculeAffaireDto.getAffaires().get(0).getArrestation().getEtatJuridique().equals("juge")) {
			p1 = new Phrase(boldConf.format("محكــــــــــــــوم"), boldfontLabelEtat);
		} else if (calculeAffaireDto.getAffaires().get(0).getArrestation().getEtatJuridique().equals("arret")) {

			if (calculeAffaireDto.isAppelParquet()) {
				p1 = new Phrase(boldConf.format(calculeAffaireDto.getDateAppelParquet())
						      + boldConf.format("موقـــوف  طعــن النيابـة بالاستئناف "), boldfontLabelEtat);
				calculeAffaireDto.setDateFin(null);
			} else if (!calculeAffaireDto.isAppelParquet() && !calculeAffaireDto.isAppelEnfant()) {
				p1 = new Phrase(boldConf.format("موقــــــــــــــوف"), boldfontLabelEtat);
				calculeAffaireDto.setDateFin(null);
			}

			else if (calculeAffaireDto.isAppelEnfant() && !calculeAffaireDto.isAppelParquet()) {
				p1 = new Phrase(boldConf.format(calculeAffaireDto.getDateAppelEnfant())
						       + boldConf.format("موقـــوف   طعــن الطفــل بالاستئناف "), boldfontLabelEtat);
			}

		}

		else if (calculeAffaireDto.getAffaires().get(0).getArrestation().getLiberation() != null) {

			if (calculeAffaireDto.getAffaires().get(0).getArrestation().getLiberation().getEtabChangeManiere() == null) {
				p1 = new Phrase(boldConf.format(calculeAffaireDto.getAffaires().get(0).getArrestation().getLiberation().getCauseLiberation()
						.getLibelleCauseLiberation().toString()), boldfontLabelEtat);

			} else {
				String stringL = calculeAffaireDto.getAffaires().get(0).getArrestation().getLiberation().getDate().toString();
				String[] partsL = stringL.split("-");
				String part1L = partsL[0];
				String part2L = partsL[1];
				String part3L = partsL[2];
				String dateL = part3L + "-" + part2L + "-" + part1L;
				
				p1 = new Phrase(
						boldConf.format("  "+dateL+"  ")+
						
						boldConf.format(calculeAffaireDto.getAffaires().get(0).getArrestation().getLiberation().getCauseLiberation()
						.getLibelleCauseLiberation().toString().trim() + " إلى "
						+ calculeAffaireDto.getAffaires().get(0).getArrestation().getLiberation().getEtabChangeManiere()
								.getLibelle_etabChangeManiere().toString().trim()  )  ,
						boldfontLabelEtat);
				

			}

		} else {
			p1 = new Phrase("--", boldfontLabelEtat);
		}

		c1 = new PdfPCell(p1);
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

		p1 = new Phrase("الوضعيــــــة الجزائيــــــــــة", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(12f);
		table.addCell(c1);

//		       --------------- jugee--------------------      
		if (!(calculeAffaireDto.getAnneePenal() == 0 && calculeAffaireDto.getMoisPenal() == 0
				&& calculeAffaireDto.getJourPenal() == 0)) {

			String jugeA = " ";
			String jugeM = " ";
			String jugeJ = " ";

			if (calculeAffaireDto.getAnneePenal() != 0) {
				if (calculeAffaireDto.getAnneePenal() == 1) {
					jugeA = " " + "عام" + " ";
				} else if (calculeAffaireDto.getAnneePenal() == 2) {
					jugeA = " " + "عامين" + " ";
				} else if ((calculeAffaireDto.getAnneePenal() >= 3) && (calculeAffaireDto.getAnneePenal() <= 10)) {
					jugeA = calculeAffaireDto.getAnneePenal() + " " + "أعوام" + " ";
				} else {
					jugeA = calculeAffaireDto.getAnneePenal() + " " + "عام" + " ";
				}

				if (calculeAffaireDto.getMoisPenal() != 0 || calculeAffaireDto.getJourPenal() != 0) {
					jugeA = jugeA + " و ";
				}
			}
			if (calculeAffaireDto.getMoisPenal() != 0) {
				if (calculeAffaireDto.getMoisPenal() == 1) {
					jugeM = " " + "شهر" + " ";
				} else if (calculeAffaireDto.getMoisPenal() == 2) {
					jugeM = " " + "شهرين" + " ";
				} else if ((calculeAffaireDto.getMoisPenal() >= 3) && (calculeAffaireDto.getMoisPenal() <= 10)) {
					jugeM = calculeAffaireDto.getMoisPenal() + " " + "أشهر" + " ";
				} else {
					jugeM = calculeAffaireDto.getMoisPenal() + "  " + "شهر" + " ";
				}

				if (calculeAffaireDto.getJourPenal() != 0) {
					jugeM = jugeM + " و ";
				}
			}
			if (calculeAffaireDto.getJourPenal() != 0) {
				if (calculeAffaireDto.getJourPenal() == 1) {
					jugeJ = " " + "يوم " + " ";
				} else if (calculeAffaireDto.getJourPenal() == 2) {
					jugeJ = " " + "يومين" + " ";
				} else if ((calculeAffaireDto.getJourPenal() >= 3) && (calculeAffaireDto.getJourPenal() <= 10)) {
					jugeJ = calculeAffaireDto.getJourPenal() + " " + "أيام" + " ";
				} else {
					jugeJ = calculeAffaireDto.getJourPenal() + "  " + "يوم" + " ";
				}

			}

			List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(
					calculeAffaireDto.getAffaires().get(0).getArrestation().getArrestationId().getIdEnfant(),
					calculeAffaireDto.getAffaires().get(0).getArrestation().getArrestationId().getNumOrdinale());
			boolean allSameName = affprincipale.stream().allMatch(x -> x.getTypeDocument().equals("AEX"));
			if (calculeAffaireDto.isAgeAdulte()) {
				if (!allSameName) {
					p1 = new Phrase(boldConf.format(jugeJ) + boldConf.format(jugeM) + boldConf.format(jugeA)
							+ boldConf.format(" الإيداع لبلوغ سن الرشد  و"), boldfontLabelAmiri);
				} else {
					p1 = new Phrase(
							boldConf.format(" إيقاف تنفيذ الحكم") + boldConf.format(jugeJ) + boldConf.format(jugeM)
									+ boldConf.format(jugeA) + boldConf.format(" الإيداع لبلوغ سن الرشد  و"),
							boldfontLabelAmiri);
				}

			}

			else {
				if (!allSameName) {
					p1 = new Phrase(boldConf.format(jugeJ) + boldConf.format(jugeM) + boldConf.format(jugeA),
							boldfontLabelAmiri);
				} else {
					p1 = new Phrase(boldConf.format(" تم إيقاف تنفيذ الحكم") + boldConf.format(jugeJ)
							+ boldConf.format(jugeM) + boldConf.format(jugeA), boldfontLabelAmiri);
				}

			}

			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(65);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			p1 = new Phrase(boldConf.format(":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);

			table.addCell(c1);

			p1 = new Phrase(boldConf.format("الحكــــــــــــــــــــــــــــــــــم"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
		}

		if ((calculeAffaireDto.getAnneePenal() == 0 && calculeAffaireDto.getMoisPenal() == 0 && calculeAffaireDto.getJourPenal() == 0)
				&& (calculeAffaireDto.isAgeAdulte())) {

			p1 = new Phrase(boldConf.format(" الإيداع لبلوغ سن الرشد "), boldfontLabelAmiri);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(65);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			p1 = new Phrase(boldConf.format(":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);

			table.addCell(c1);

			p1 = new Phrase(boldConf.format("الحكــــــــــــــــــــــــــــــــــم"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
		}
//		       --------------- Lieu jugee--------------------      
		
		String stringP = calculeAffaireDto.getDateJugementPrincipale().toString();
		String[] partsP = stringP.split("-");
		String part1P = partsP[0];
		String part2P = partsP[1];
		String part3P = partsP[2];
		String dateP = part3P + "-" + part2P + "-" + part1P;
		p1 = new Phrase(boldConf.format(dateP), boldfontLabelAmiri);
		
		
		p1 = new Phrase(boldConf.format(dateP), boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(65);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase(boldConf.format(":"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);
		if ((calculeAffaireDto.getAnneePenal() == 0 && calculeAffaireDto.getMoisPenal() == 0 && calculeAffaireDto.getJourPenal() == 0)
				&& (calculeAffaireDto.getAffaires().get(0).getArrestation().getEtatJuridique().equals("arret"))) {

			p1 = new Phrase(boldConf.format("تـــــــــــاريخ الإيقـــــــــــــاف"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
		} else {

			if (calculeAffaireDto.getAnneePenal() == 0 && calculeAffaireDto.getMoisPenal() == 0
					&& calculeAffaireDto.getJourPenal() == 0) {
				p1 = new Phrase(boldConf.format("تاريـــخ صـــدور البطاقــــــة"), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBorder(0);
				c1.setColspan(30);
				c1.setPaddingBottom(7f);
				table.addCell(c1);
			} else {
				p1 = new Phrase(boldConf.format("تـــــــــــاريخ الحكــــــــــــــم"), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBorder(0);
				c1.setColspan(30);
				c1.setPaddingBottom(7f);
				table.addCell(c1);
			}

		}

//		       --------------- tribunal--------------------    

		p1 = new Phrase("(" + calculeAffaireDto.getAffaires().get(0).getTypeAffaire().getLibelle_typeAffaire()+ ")",
				boldfontFamielle);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(25);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase(calculeAffaireDto.getAffaires().get(0).getArrestation().getTribunalPricipale().getNom_tribunal().toString(),
				boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(40);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase(boldConf.format(":"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase(boldConf.format("المحكمـــــــــــــــــــــــــــــة"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

//		       --------------- tribunal--------------------      

		
		String titreAccusationSring1 = " ";
//		for (TitreAccusation titreAccusation : calculeAffaireDto.getAffaires().get(0).getTitreAccusations() ) {
//			titreAccusationSring1 = titreAccusationSring1 + titreAccusation.getTitreAccusation() + " و ";
//
//		}
		List<TitreAccusation> titreAccusations = calculeAffaireDto.getAffaires().get(0).getTitreAccusations();
		for (int i = 0; i < titreAccusations.size(); i++) {
			titreAccusationSring1 +=  titreAccusations.get(i).getTitreAccusation();
		    if (i != titreAccusations.size() - 1) {
		         titreAccusationSring1 += " و ";
		    }
		}
		
		
		
		
		p1 = new Phrase(titreAccusationSring1.trim(), boldfontLabelAmirix);

		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(65);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase(boldConf.format(":"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase(boldConf.format("التهمـــــــــــــــــــــــــــــــــة"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

//		       --------------- tribunal--------------------      
		String string = calculeAffaireDto.getAffaires().get(0).getArrestation().getDate().toString();
		String[] parts = string.split("-");
		String part1 = parts[0];
		String part2 = parts[1];
		String part3 = parts[2];
		String date = part3 + "-" + part2 + "-" + part1;
		p1 = new Phrase(boldConf.format(date), boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(65);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase(boldConf.format(":"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase(boldConf.format("تــــاريخ الإيـــداع بالمــــركز"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

//		       --------------- tribunal--------------------      

		if (calculeAffaireDto.getDateDebut() != null) {
			String stringD = calculeAffaireDto.getDateDebut().toString();
			String[] partsD = stringD.split("-");
			String part1D = partsD[0];
			String part2D = partsD[1];
			String part3D = partsD[2];
			String dateD = part3D + "-" + part2D + "-" + part1D;
			p1 = new Phrase(boldConf.format(dateD), boldfontLabelAmiri);
			
//			p1 = new Phrase(boldConf.format(calculeAffaireDto.getDateDebut().toString()), boldfontLabelAmiri);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(65);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			p1 = new Phrase(boldConf.format(":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);

			table.addCell(c1);

			p1 = new Phrase(boldConf.format("تاريخ بداية العقـــــــــــــــاب"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
		}

//		       --------------- tribunal--------------------      

		if (calculeAffaireDto.getDateFin() != null) {
			
			String stringF = calculeAffaireDto.getDateFin().toString();
			String[] partsF = stringF.split("-");
			String part1F = partsF[0];
			String part2F = partsF[1];
			String part3F = partsF[2];
			String dateF = part3F + "-" + part2F + "-" + part1F;
			p1 = new Phrase(boldConf.format(dateF), boldfontLabelAmiri);
			
//			p1 = new Phrase(boldConf.format(calculeAffaireDto.getDateFin().toString()), boldfontLabelAmiri);

			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(65);
			c1.setPaddingBottom(7f);

			table.addCell(c1);

			p1 = new Phrase(boldConf.format(":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);

			table.addCell(c1);

			p1 = new Phrase(boldConf.format("تاريخ الســـــــــــــــــــــــراح"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
		}

//		       --------------- tribunal--------------------      

		if (!(calculeAffaireDto.getAnneeArret() == 0 && calculeAffaireDto.getMoisArret() == 0
				&& calculeAffaireDto.getJourArret() == 0)) {

			String arretA = " ";
			String arretM = " ";
			String arretJ = " ";

			if (calculeAffaireDto.getAnneeArret() != 0) {
				if (calculeAffaireDto.getAnneeArret() == 1) {
					arretA = " " + "عام" + " ";
				} else if (calculeAffaireDto.getAnneeArret() == 2) {
					arretA = " " + "عامين" + " ";
				} else if ((calculeAffaireDto.getAnneeArret() >= 3) && (calculeAffaireDto.getAnneeArret() <= 10)) {
					arretA = calculeAffaireDto.getAnneeArret() + " " + "أعوام" + " ";
				} else {
					arretA = calculeAffaireDto.getAnneeArret() + " " + "عام" + " ";
				}

				if (calculeAffaireDto.getMoisArret() != 0 || calculeAffaireDto.getJourArret() != 0) {
					arretA = arretA + " و ";
				}
			}

			if (calculeAffaireDto.getMoisArret() != 0) {
				if (calculeAffaireDto.getMoisArret() == 1) {
					arretM = " " + "شهر" + " ";
				} else if (calculeAffaireDto.getMoisArret() == 2) {
					arretM = " " + "شهرين" + " ";
				} else if ((calculeAffaireDto.getMoisArret() >= 3) && (calculeAffaireDto.getMoisArret() <= 10)) {
					arretM = calculeAffaireDto.getAnneeArret() + " " + "أشهر" + " ";
				} else {
					arretM = calculeAffaireDto.getMoisArret() + "  " + "شهر" + " ";
				}

				if (calculeAffaireDto.getJourArret() != 0) {
					arretM = arretM + " و ";
				}
			}

			if (calculeAffaireDto.getJourArret() != 0) {
				if (calculeAffaireDto.getJourArret() == 1) {
					arretJ = " " + "يوم " + " ";
				} else if (calculeAffaireDto.getJourArret() == 2) {
					arretJ = " " + "يومين" + " ";
				} else if ((calculeAffaireDto.getJourArret() >= 3) && (calculeAffaireDto.getJourArret() <= 10)) {
					arretJ = calculeAffaireDto.getAnneeArret() + " " + "أيام" + " ";
				} else {
					arretJ = calculeAffaireDto.getJourArret() + "  " + "يوم" + " ";
				}

			}

			// arret= pDFPenaleDTO.getAnneePenal()+" "+pDFPenaleDTO.getMoisPenal()+"
			// "+pDFPenaleDTO.getJourPenal()+" ";
			for (ArretProvisoire ap : calculeAffaireDto.getArretProvisoires()) {
				String duree = "";
				duree = duree + "من  " + ap.getDateDebut() + " إلى  " + ap.getDateFin() + "\n";
				p1 = new Phrase(duree, boldfontFamielle);
			}

			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(40);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
			p1 = new Phrase(boldConf.format(arretJ) + boldConf.format(arretM) + boldConf.format(arretA),
					boldfontLabelAmiri);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(25);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			p1 = new Phrase(boldConf.format(":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);

			table.addCell(c1);

			p1 = new Phrase(boldConf.format("إيقــــــاف تحفظـــــــــــــــي"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

		}
		String classePenale = "--";
		int nbrRetour = (int) calculeAffaireDto.getAffaires().get(0).getArrestation().getArrestationId().getNumOrdinale();
		if (nbrRetour > 1) {
			classePenale = "عـــــ" + "0" + nbrRetour + " " + "ـــــــــائـــد";
		} else {
			classePenale = calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getClassePenale().getLibelle_classe_penale().toString();
		}

		p1 = new Phrase(classePenale, boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(65);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase(boldConf.format(":"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase(boldConf.format("العقوبــــــات السابقــــــــــة"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

//		       --------------- tribunal--------------------    
		if (calculeAffaireDto.getTotaleEchappe() != 0 || calculeAffaireDto.getTotaleRecidence() != 0
				|| calculeAffaireDto.getTotaleRecidenceWithetabChangeManiere() != 0) {
			
//			if (calculeAffaireDto.getTotaleRecidence() != 0) {
			p1 = new Phrase(

					boldConf.format("نقــــــل ") + boldConf.format(" " + calculeAffaireDto.getTotaleRecidence() + " "),
					boldfontLabelAmiri);
//			}
			
			
			if (calculeAffaireDto.getTotaleEchappe() != 0) {
				p1.add((boldConf.format(" و ") + boldConf.format("فــــــرار ")
						+ boldConf.format(" " + calculeAffaireDto.getTotaleEchappe() + " ")));
			}
			if (calculeAffaireDto.getTotaleRecidenceWithetabChangeManiere() != 0) {
				p1.add((boldConf.format(" و ") + boldConf.format("تغير وسيلة ")
						+ boldConf.format(" " + calculeAffaireDto.getTotaleRecidenceWithetabChangeManiere() + " ")));
			}

			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(65);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			p1 = new Phrase(boldConf.format(":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);

			table.addCell(c1);

			p1 = new Phrase(boldConf.format("المـــــــلاحظـــــــات "), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			Residence r = residenceRepository.findMaxResidence(calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getId(),
					calculeAffaireDto.getAffaires().get(0).getArrestation().getArrestationId().getNumOrdinale());

			if (r.getDateEntree() != null && r.getEtablissementEntree() != null && r.getCauseMutation() != null) {

				p1 = new Phrase(r.getDateEntree().toString() + " " + "قدم من" + " "
						+ r.getEtablissementEntree().getLibelle_etablissement().toString() + " من أجل  ("
						+ r.getCauseMutation().getLibelle_causeMutation().toString() + ") يوم ", boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(0);

				c1.setBackgroundColor(BaseColor.WHITE);
				c1.setColspan(100);
				table.addCell(c1);
			}

			if (r.getStatut() == 2) {

				p1 = new Phrase("لم يتم الإستقبال بعد " + " " + "قدم من" + " "
						+ r.getEtablissementEntree().getLibelle_etablissement().toString() + "   من أجل "
						+ r.getCauseMutation().getLibelle_causeMutation().toString() + "  ", boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(0);

				c1.setBackgroundColor(BaseColor.WHITE);
				c1.setColspan(100);
				table.addCell(c1);
			}

			Echappes e = echappesRepository.findByIdEnfantAndResidenceTrouverNull(calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getId());
			Echappes eLast = echappesRepository.findMaxEchappes(calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getId(),
					calculeAffaireDto.getAffaires().get(0).getArrestation().getArrestationId().getNumOrdinale());

			if (e != null) {
				p1 = new Phrase(e.getDateEchappes().toString() + ":" + "في حالة فرار " + " " + " فر من" + " "
						+ e.getResidenceEchapper().getEtablissement().getLibelle_etablissement().toString() + " "
						+ e.getCommentEchapper().getLibelleComment().toString(), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(0);

				c1.setBackgroundColor(BaseColor.WHITE);
				c1.setColspan(100);
				table.addCell(c1);

			} else if (eLast != null) {
				p1 = new Phrase(eLast.getDateTrouver().toString() + " " + "تم القبض عليه يوم ", boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(0);

				c1.setBackgroundColor(BaseColor.WHITE);
				c1.setColspan(50);
				table.addCell(c1);

				p1 = new Phrase(eLast.getDateEchappes().toString() + "   " + " فر من" + " "
						+ eLast.getResidenceEchapper().getEtablissement().getLibelle_etablissement().toString() + " "
						+ eLast.getCommentEchapper().getLibelleComment().toString(), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(0);

				c1.setBackgroundColor(BaseColor.WHITE);
				c1.setColspan(50);
				table.addCell(c1);

			}

			if (calculeAffaireDto.getTotaleRecidenceWithetabChangeManiere() != 0) {
				Residence c = residenceRepository.findMaxResidenceWithEtabChangeManiere(
						calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getId(),
						calculeAffaireDto.getAffaires().get(0).getArrestation().getArrestationId().getNumOrdinale());

				if (c != null && c.getDateSortie() != null) {
					// System.out.println(c.toString());
					c.getResidenceId().setNumOrdinaleResidence(c.getResidenceId().getNumOrdinaleResidence() + 1);
					Residence resFinChangeManier = residenceRepository.retourChangeManier(c.getResidenceId());

					if (resFinChangeManier != null) {
						p1 = new Phrase(
								resFinChangeManier.getDateEntree().toString() + " " + "عاد إلى "
										+ resFinChangeManier.getEtablissement().getLibelle_etablissement().toString(),
								boldfontLabel);
						c1 = new PdfPCell(p1);
						c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						c1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1.setBorder(0);

						c1.setBackgroundColor(BaseColor.WHITE);
						c1.setColspan(50);
						table.addCell(c1);
					}

					p1 = new Phrase(
							c.getDateSortie().toString() + " " + "تغير وسيلة إلى "
									+ c.getEtabChangeManiere().getLibelle_etabChangeManiere().toString(),
							boldfontLabel);
					c1 = new PdfPCell(p1);
					c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
					c1.setHorizontalAlignment(Element.ALIGN_LEFT);
					c1.setBorder(0);

					c1.setBackgroundColor(BaseColor.WHITE);
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

		if (!pDFPenaleDTO.isSansDetail()) {

//		       ---------------  nom --------------------

			p1 = new Phrase(boldConf
					.format(calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getNom() + " بن " + calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getNomPere() + " بن "
							+ calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getNomGrandPere() + " " + calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getPrenom()),
					boldfontLabelAmiri);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);

			c1.setColspan(100);
			c1.setBorderWidth(2);
			c1.setBorder(Rectangle.BOX);
			c1.setBackgroundColor(new BaseColor(210, 210, 210));
			c1.setPaddingBottom(7f);

			tableLien.addCell(c1);

			p1 = new Phrase(" ");
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(100);

			tableLien.addCell(c1);
			List<Affaire> affaireAffiche = findByArrestationOnePage(calculeAffaireDto.getAffaires().get(0).getArrestation().getEnfant().getId(),
					calculeAffaireDto.getAffaires().get(0).getArrestation().getArrestationId().getNumOrdinale());

			for (int i = 0; i < affaireAffiche.size(); i++) {

				p1 = new Phrase(boldConf.format(" "), boldfontLabel);
				;

				p1 = new Phrase(" ", boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setBorder(0);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);

				c1.setColspan(70);
				tableAffaire.addCell(c1);

				p1 = new Phrase((i + 1) + " " + boldConf.format(" العدد الرتبي للقضية : "), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(30);
				tableAffaire.addCell(c1);

				p1 = new Phrase(boldConf.format(affaireAffiche.get(i).getAffaireId().getNumAffaire()) + " "
						+ affaireAffiche.get(i).getTribunal().getNom_tribunal(), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);

				c1.setColspan(70);
				tableAffaire.addCell(c1);
				p1 = new Phrase(boldConf.format("  القضية"), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(30);
				tableAffaire.addCell(c1);

				System.out.println(affaireAffiche.get(i).getTypeDocument().toString() + " "
						+ affaireAffiche.get(i).getAffaireId().getIdEnfant().toString());

				switch (affaireAffiche.get(i).getTypeDocument().toString().trim()) {
				case "CD":
					p1 = new Phrase(boldConf.format("بطاقة إيداع"), boldfontLabelAmiri);

					break;

				case "CH":
					p1 = new Phrase(boldConf.format("بطاقة إيواء"), boldfontLabelAmiri);

					break;

				case "AEX":

					if (affaireAffiche.get(i).getTypeFile() != null) {
						if (affaireAffiche.get(i).getTypeFile().toString().equals("AEX".toString())) {

							p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
									+ boldConf.format("إيقاف تنفيذ الحكم"), boldfontLabelAmiri);
						} else if (affaireAffiche.get(i).getTypeFile().toString().equals("L".toString())) {

							p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
									+ boldConf.format("ســــــــــــراح"), boldfontLabelAmiri);

						}
					} else {

						p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
								+ boldConf.format("إيقاف تنفيذ الحكم"), boldfontLabelAmiri);
					}
					break;
				case "CJ":
					p1 = new Phrase(boldConf.format("مضمون حكم"), boldfontLabelAmiri);

					break;

				case "T":
					if (affaireAffiche.get(i).getTypeFile() != null) {
						if (affaireAffiche.get(i).getTypeFile().toString().equals("T".toString())) {
							p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
									+ boldConf.format("إحــــــالة"), boldfontLabelAmiri);
						} else if (affaireAffiche.get(i).getTypeFile().toString().equals("A".toString())) {
							p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
									+ boldConf.format("تخلــــــي"), boldfontLabelAmiri);
						} else if (affaireAffiche.get(i).getTypeFile().toString().equals("G".toString())) {
							p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
									+ boldConf.format("تعهــــــد"), boldfontLabelAmiri);
						}
					} else {
						p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
								+ boldConf.format("إحــــــالة"), boldfontLabelAmiri);
					}

					break;

				case "AE":
					p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
							+ boldConf.format("طعن الطفل بالاستئناف"), boldfontLabelAmiri);

					break;

				case "AP":
					p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
							+ boldConf.format("طعن النيابة بالاستئناف"), boldfontLabelAmiri);

					break;

				case "CR":
					p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
							+ boldConf.format("مراجعة"), boldfontLabelAmiri);

					break;
				case "CRR":
					p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
							+ boldConf.format("قرار رفض المراجعة"), boldfontLabelAmiri);

					break;
				case "CP":
					p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
							+ boldConf.format("قرار تمديد"), boldfontLabelAmiri);

					break;
				case "CHL":
					p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
							+ boldConf.format("قرار تغير مكان الإيداع "), boldfontLabelAmiri);

					break;

				default:
					p1 = new Phrase(boldConf.format("--"), boldfontLabelAmiri);

				}

				c1 = new PdfPCell(p1);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				// c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(70);
				tableAffaire.addCell(c1);

				p1 = new Phrase(boldConf.format("  نوع الوثيقة"), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(30);
				tableAffaire.addCell(c1);

				p1 = new Phrase(affaireAffiche.get(i).getDateEmission().toString(), boldfontLabelAmiri);
				c1 = new PdfPCell(p1);

				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				// c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(70);
				tableAffaire.addCell(c1);

				String labelAffair = "";
				if (affaireAffiche.get(i).getTypeDocument().toString().equals("CD")
						|| affaireAffiche.get(i).getTypeDocument().toString().equals("CH")
						|| affaireAffiche.get(i).getTypeDocument().toString().equals("T")
						|| affaireAffiche.get(i).getTypeDocument().toString().equals("CP")) {
					labelAffair = "تاريخ صدور البطاقة ";
				} else {
					if (affaireAffiche.get(i).getTypeDocument().toString().equals("CHL")) {
						labelAffair = "تاريخ القضية ";

					} else {
						labelAffair = "تاريخ الحكم";
					}
				}

				p1 = new Phrase(boldConf.format(labelAffair), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(30);
				tableAffaire.addCell(c1);

				String titreAccusationSring = " ";
				for (TitreAccusation titreAccusation : affaireAffiche.get(i).getTitreAccusations()) {
					titreAccusationSring = titreAccusationSring + titreAccusation.getTitreAccusation() + " ";

				}
				p1 = new Phrase(titreAccusationSring.trim(), boldfontLabelAmirix);

				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				// c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(70);
				tableAffaire.addCell(c1);
				p1 = new Phrase(boldConf.format("التهمة "), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(30);
				tableAffaire.addCell(c1);

				if (!(affaireAffiche.get(i).getAnnee() == 0 && affaireAffiche.get(i).getMois() == 0
						&& affaireAffiche.get(i).getJour() == 0)) {

					String jugeA = " ";
					String jugeM = " ";
					String jugeJ = " ";

					if (affaireAffiche.get(i).getAnnee() != 0) {
						if (affaireAffiche.get(i).getAnnee() == 1) {
							jugeA = " " + "عام" + " ";
						} else if (affaireAffiche.get(i).getAnnee() == 2) {
							jugeA = " " + "عامين" + " ";
						} else if ((affaireAffiche.get(i).getAnnee() >= 3)
								&& (affaireAffiche.get(i).getAnnee() <= 10)) {
							jugeA = affaireAffiche.get(i).getAnnee() + " " + "أعوام" + " ";
						} else {
							jugeA = affaireAffiche.get(i).getAnnee() + " " + "عام" + " ";
						}

						if (affaireAffiche.get(i).getMois() != 0 || affaireAffiche.get(i).getJour() != 0) {
							jugeA = jugeA + " و ";
						}
					}
					if (affaireAffiche.get(i).getMois() != 0) {
						if (affaireAffiche.get(i).getMois() == 1) {
							jugeM = " " + "شهر" + " ";
						} else if (affaireAffiche.get(i).getMois() == 2) {
							jugeM = " " + "شهرين" + " ";
						} else if ((affaireAffiche.get(i).getMois() >= 3) && (affaireAffiche.get(i).getMois() <= 10)) {
							jugeM = affaireAffiche.get(i).getMois() + " " + "أشهر" + " ";
						} else {
							jugeM = affaireAffiche.get(i).getMois() + "  " + "شهر" + " ";
						}

						if (affaireAffiche.get(i).getJour() != 0) {
							jugeM = jugeM + " و ";
						}
					}
					if (affaireAffiche.get(i).getJour() != 0) {
						if (affaireAffiche.get(i).getJour() == 1) {
							jugeJ = " " + "يوم " + " ";
						} else if (affaireAffiche.get(i).getJour() == 2) {
							jugeJ = " " + "يومين" + " ";
						} else if ((affaireAffiche.get(i).getJour() >= 3) && (affaireAffiche.get(i).getJour() <= 10)) {
							jugeJ = affaireAffiche.get(i).getJour() + " " + "أيام" + " ";
						} else {
							jugeJ = affaireAffiche.get(i).getJour() + "  " + "يوم" + " ";
						}

					}
					String remarque = " ";
					if (affaireAffiche.get(i).getTypeJuge() != null) {
						remarque = remarque + " " + affaireAffiche.get(i).getTypeJuge().getLibelle_typeJuge() + " ";
					}
					if (affaireAffiche.get(i).getTypeDocument().toString().equals("AEX")) {
						remarque = remarque + "(إيقاف   الحكم)";
					}
					if (affaireAffiche.get(i).getAffaireAffecter() != null) {
						remarque = remarque + " تم الضم إلى القضية عدد :  "
								+ affaireAffiche.get(i).getAffaireAffecter().getAffaireId().getNumAffaire() + " ";
					}
					p1 = new Phrase(remarque + " " + jugeJ + " " + jugeM + " " + jugeA

							, boldfontLabelAmiri);
					c1 = new PdfPCell(p1);
					c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					// c1.setBackgroundColor(new BaseColor(210, 210, 210));
					c1.setColspan(70);
					tableAffaire.addCell(c1);
				} else {

					if (affaireAffiche.get(i).getTypeDocument().toString().equals("AEX")) {

						String remarque = " إيقاف الحكم   سراح ";

						if (affaireAffiche.get(i).getAffaireAffecter() != null) {
							remarque = remarque + " تم الضم إلى القضية عدد :  "
									+ affaireAffiche.get(i).getAffaireAffecter().getAffaireId().getNumAffaire() + " ";
						}
						p1 = new Phrase(remarque, boldfontLabelAmiri);
						c1 = new PdfPCell(p1);
						c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						// c1.setBackgroundColor(new BaseColor(210, 210, 210));
						c1.setColspan(70);
						tableAffaire.addCell(c1);
					} else {

						String remarque = " ";
						if (!affaireAffiche.get(i).getTypeDocument().toString().equals("CJ")) {
							remarque = remarque + "موقوف";
						}
						if (affaireAffiche.get(i).getTypeJuge() != null) {
							remarque = remarque + " " + affaireAffiche.get(i).getTypeJuge().getLibelle_typeJuge() + " ";
						}

						if (affaireAffiche.get(i).getAffaireAffecter() != null) {
							remarque = remarque + "  تم الضم إلى القضية عدد :  "
									+ affaireAffiche.get(i).getAffaireAffecter().getAffaireId().getNumAffaire() + " ";
						}
						p1 = new Phrase(remarque, boldfontLabelAmiri);
						c1 = new PdfPCell(p1);
						c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
						c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						// c1.setBackgroundColor(new BaseColor(210, 210, 210));
						c1.setColspan(70);
						tableAffaire.addCell(c1);

					}

				}

				p1 = new Phrase(boldConf.format("نص الحكم"), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(30);
				tableAffaire.addCell(c1);

				p1 = new Phrase(boldConf.format("  "), boldfontLabelAmiri);
				c1 = new PdfPCell(p1);

				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(0);

				c1.setColspan(70);
				tableAffaire.addCell(c1);
				p1 = new Phrase(boldConf.format("  "), boldfontLabelAmiri);
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
		if (!pDFPenaleDTO.isSansDetail()) {
			document.newPage();
			Rectangle rect2 = new Rectangle(15, 20, 580, 793);
			rect2.setBorder(Rectangle.BOX);
			rect2.setBorderWidth(2);

			document.add(tableLien);
			document.add(rect2);
			document.add(tableAffaire);
		}
		// step 5
		document.close();

		return new ByteArrayInputStream(out.toByteArray());

	}

 

	 

	 

 

 

	 

 

	 

 

	 

	 
 

	 

	 
 

	 

	 

	 

}

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

public class GenererFicheDeDetentionPdfImpl implements GenererFicheDeDetentionPdfService {

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
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public ByteArrayInputStream genererFicheDeDetentionPdf(PDFPenaleDTO pDFPenaleDTO)
			throws DocumentException, IOException, ArabicShapingException {

		FicheDeDetentionDto ficheDeDetentionDto = affaireServiceImpl.obtenirInformationsDeDetentionParIdDetention(
				pDFPenaleDTO.getIdEnfant(), pDFPenaleDTO.getNumOrdinale());
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

		PdfPTable tableTop = new PdfPTable(3);
		tableTop.setWidthPercentage(100);

		Phrase p1Top;
		PdfPCell c1Top;

		// --------------- nom --------------------

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

		String titre = "مذكرة شخصية لطفل جانح";
		if (ficheDeDetentionDto.getArrestation().getEnfant().getSexe().equals("أنثى")) {
			titre = "مذكرة شخصية لطفلة جانحة";
		}

		Phrase pTitre = new Phrase(titre, boldfontTitle);

		PdfPCell cTitre = new PdfPCell(pTitre);
		cTitre.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

		cTitre.setPaddingBottom(20f);
		cTitre.setBorder(Rectangle.BOX);

		cTitre.setBorderWidth(0.5F);

		cTitre.setBackgroundColor(new BaseColor(240, 240, 240)); // Couleur de fond plus claire

		cTitre.setBorderColor(BaseColor.BLACK);

		cTitre.setHorizontalAlignment(Element.ALIGN_CENTER);

		tTitre.addCell(cTitre);
		tTitre.setWidthPercentage(60);

		if (!pDFPenaleDTO.isSansImage()) {

			PhotoId photoId = new PhotoId();
			photoId.setIdEnfant(ficheDeDetentionDto.getArrestation().getEnfant().getId());
			photoId.setNumOrdinaleArrestation(ficheDeDetentionDto.getArrestation().getArrestationId().getNumOrdinale());
			Optional<Photo> photo = photoService.getPhotoById(photoId);
			if (photo.isPresent() && photo.get().getImg() != null) {

				final String base64Data = photo.get().getImg()
						.substring(photo.get().getImg().indexOf(",") + 1);
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

		// --------------- nom --------------------
		if (ficheDeDetentionDto.getArrestation().getArrestationId().getNumOrdinale() < 10) {
			p1 = new Phrase(

					"0" + ficheDeDetentionDto.getArrestation().getArrestationId().getNumOrdinale() + "  /  "
							+ ficheDeDetentionDto.getArrestation().getEnfant().getId(),
					boldfontLabelAmiri);
		}

		else {
			p1 = new Phrase(
					ficheDeDetentionDto.getArrestation().getArrestationId().getNumOrdinale() + "  /  "
							+ ficheDeDetentionDto.getArrestation().getEnfant().getId(),
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
		p1 = new Phrase(ficheDeDetentionDto.getResidences().get(0).getEtablissement().getLibelle_etablissement(),
				boldfontArr);
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

		p1 = new Phrase(ficheDeDetentionDto.getResidences().get(0).getNumArrestation(), boldfontLabelAmiri);
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
		p1 = new Phrase("عـــدد الإيقـــــــــاف", boldfontArr);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(40);

		tArr.addCell(c1);
		p1 = new Phrase(affairePricipale.getAffaireId().getNumAffaire().toString(), boldfontLabelAmiri);
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

		if (ficheDeDetentionDto.getAffaires().size() < 10) {
			p1 = new Phrase("عـــــ" + "0" + ficheDeDetentionDto.getAffaires().size() + "ــدد القضيــــــة",
					boldfontLabelAmirix);

		} else {
			p1 = new Phrase("عـــــ" + ficheDeDetentionDto.getAffaires().size() + "" + "ـــــدد القضيــــــة",
					boldfontLabelAmirix);

		}
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(40);

		tArr.addCell(c1);

		String arabicMajorityAgeDate = ToolsForReporting.getArabicMajorityAgeDate(
				ficheDeDetentionDto.getArrestation().getEnfant().getDateNaissance().toString());
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

		p1 = new Phrase(
				"ســـــ" + ToolsForReporting.calculerAge(
						ficheDeDetentionDto.getArrestation().getEnfant().getDateNaissance().toString().trim())
						+ "ـــن الرشـــــــــد",
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

		// --------------- nom --------------------

		boldfontFamielle.setColor(255, 0, 0);
		p1 = new Phrase(affairePricipale.getTypeAffaire().getLibelle_typeAffaire(),
				boldfontFamielle);

		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(0);
		c1.setColspan(20);
		c1.setPaddingBottom(7f);

		table.addCell(c1);
		p1 = new Phrase(
				ficheDeDetentionDto.getArrestation().getEnfant().getNom() + " بن "
						+ ficheDeDetentionDto.getArrestation().getEnfant().getNomPere() + " بن "
						+ ficheDeDetentionDto.getArrestation().getEnfant().getNomGrandPere() + " "
						+ ficheDeDetentionDto.getArrestation().getEnfant().getPrenom(),
				boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(45);
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

		// --------------- mere --------------------

		p1 = new Phrase(
				ficheDeDetentionDto.getArrestation().getEnfant().getNomMere() + " "
						+ ficheDeDetentionDto.getArrestation().getEnfant().getPrenomMere(),
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

		// --------------- Lieu et lieu --------------------

		p1 = new Phrase("بــــــــــ" + ficheDeDetentionDto.getArrestation().getEnfant().getLieuNaissance()

				, boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(48);
		c1.setPaddingBottom(7f);
		table.addCell(c1);
		p1 = new Phrase(
				ficheDeDetentionDto.getArrestation().getEnfant().getDateNaissance().toString() + "  ",
				boldfontLabelAmiri);
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

		// --------------- nationnalite --------------------

		p1 = new Phrase(
				ficheDeDetentionDto.getArrestation().getEnfant().getNationalite().getLibelle_nationalite().toString(),
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

		// --------------- situation fam --------------------

		p1 = new Phrase(
				ficheDeDetentionDto.getArrestation().getEnfant().getSituationFamiliale()
						.getLibelle_situation_familiale().toString(),
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

		// --------------- adress --------------------

		String address = ficheDeDetentionDto.getArrestation().getEnfant().getAdresse().toString().trim() + " ";
		if (ficheDeDetentionDto.getArrestation().getEnfant().getDelegation().getId() != Long.valueOf(900)) {
			address += ficheDeDetentionDto.getArrestation().getEnfant().getDelegation().getLibelle_delegation()
					.toString() + " ";
		}
		if (ficheDeDetentionDto.getArrestation().getEnfant().getGouvernorat().getId() != Long.valueOf(30)) {
			address += ficheDeDetentionDto.getArrestation().getEnfant().getGouvernorat().getLibelle_gouvernorat()
					.toString();
		}
		p1 = new Phrase(address, boldfontLabelAmiri);
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

		// --------------- niveau edu --------------------

		p1 = new Phrase(
				ficheDeDetentionDto.getArrestation().getEnfant().getNiveauEducatif().getLibelle_niveau_educatif()
						.toString(),
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

		p1 = new Phrase("المستــوى التعليمـــــــــــي", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		// --------------- situation penal --------------------

		String statutJudiciaire = ecrireStatut(ficheDeDetentionDto);

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

		p1 = new Phrase("الوضعيــــــة الجزائيــــــــــة", boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(12f);
		table.addCell(c1);

		// --------------- jugee--------------------
		if (!(ficheDeDetentionDto.getAnneePenal() == 0 && ficheDeDetentionDto.getMoisPenal() == 0
				&& ficheDeDetentionDto.getJourPenal() == 0)) {

			String jugeA = " ";
			String jugeM = " ";
			String jugeJ = " ";

			if (ficheDeDetentionDto.getAnneePenal() != 0) {
				if (ficheDeDetentionDto.getAnneePenal() == 1) {
					jugeA = " " + "عام" + " ";
				} else if (ficheDeDetentionDto.getAnneePenal() == 2) {
					jugeA = " " + "عامين" + " ";
				} else if ((ficheDeDetentionDto.getAnneePenal() >= 3) && (ficheDeDetentionDto.getAnneePenal() <= 10)) {
					jugeA = ficheDeDetentionDto.getAnneePenal() + " " + "أعوام" + " ";
				} else {
					jugeA = ficheDeDetentionDto.getAnneePenal() + " " + "عام" + " ";
				}

				if (ficheDeDetentionDto.getMoisPenal() != 0 || ficheDeDetentionDto.getJourPenal() != 0) {
					jugeA = jugeA + " و ";
				}
			}
			if (ficheDeDetentionDto.getMoisPenal() != 0) {
				if (ficheDeDetentionDto.getMoisPenal() == 1) {
					jugeM = " " + "شهر" + " ";
				} else if (ficheDeDetentionDto.getMoisPenal() == 2) {
					jugeM = " " + "شهرين" + " ";
				} else if ((ficheDeDetentionDto.getMoisPenal() >= 3) && (ficheDeDetentionDto.getMoisPenal() <= 10)) {
					jugeM = ficheDeDetentionDto.getMoisPenal() + " " + "أشهر" + " ";
				} else {
					jugeM = ficheDeDetentionDto.getMoisPenal() + "  " + "شهر" + " ";
				}

				if (ficheDeDetentionDto.getJourPenal() != 0) {
					jugeM = jugeM + " و ";
				}
			}
			if (ficheDeDetentionDto.getJourPenal() != 0) {
				if (ficheDeDetentionDto.getJourPenal() == 1) {
					jugeJ = " " + "يوم " + " ";
				} else if (ficheDeDetentionDto.getJourPenal() == 2) {
					jugeJ = " " + "يومين" + " ";
				} else if ((ficheDeDetentionDto.getJourPenal() >= 3) && (ficheDeDetentionDto.getJourPenal() <= 10)) {
					jugeJ = ficheDeDetentionDto.getJourPenal() + " " + "أيام" + " ";
				} else {
					jugeJ = ficheDeDetentionDto.getJourPenal() + "  " + "يوم" + " ";
				}

			}

			List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(
					ficheDeDetentionDto.getArrestation().getArrestationId().getIdEnfant(),
					ficheDeDetentionDto.getArrestation().getArrestationId().getNumOrdinale());
			boolean allSameName = affprincipale.stream().allMatch(x -> x.getTypeDocument().equals("ArretEx"));
			if (ficheDeDetentionDto.isAgeAdulte()) {
				if (!allSameName) {
					p1 = new Phrase((" الإيداع لبلوغ سن الرشد  و") + (jugeA) + (jugeM) + (jugeJ), boldfontLabelAmiri);
				} else {
					p1 = new Phrase(
							(" الإيداع لبلوغ سن الرشد  و") + (jugeA) + (jugeM) + (jugeJ) + (" إيقاف تنفيذ الحكم"),
							boldfontLabelAmiri);
				}

			}

			else {
				if (!allSameName) {
					p1 = new Phrase((jugeA) + (jugeM) + (jugeJ),
							boldfontLabelAmiri);
				} else {
					p1 = new Phrase((jugeA) + (jugeM) + (jugeJ) + (" تم إيقاف تنفيذ الحكم"), boldfontLabelAmiri);
				}

			}

			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(65);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			p1 = new Phrase((":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);

			table.addCell(c1);

			p1 = new Phrase(("الحكــــــــــــــــــــــــــــــــــم"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
		}

		if ((ficheDeDetentionDto.getAnneePenal() == 0 && ficheDeDetentionDto.getMoisPenal() == 0
				&& ficheDeDetentionDto.getJourPenal() == 0)
				&& (ficheDeDetentionDto.isAgeAdulte())) {

			p1 = new Phrase((" الإيداع لبلوغ سن الرشد "), boldfontLabelAmiri);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(65);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			p1 = new Phrase((":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);
			table.addCell(c1);

			p1 = new Phrase(("الحكــــــــــــــــــــــــــــــــــم"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
		}
		// --------------- Lieu jugee--------------------

		p1 = new Phrase((ficheDeDetentionDto.getDateJugementPrincipale().toString()), boldfontLabelAmiri);

		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(65);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase((":"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);
		if ((ficheDeDetentionDto.getAnneePenal() == 0 && ficheDeDetentionDto.getMoisPenal() == 0
				&& ficheDeDetentionDto.getJourPenal() == 0)
				&& (ficheDeDetentionDto.getEtatJuridique().equals("arret"))) {

			p1 = new Phrase(("تـــــــــــاريخ الإيقـــــــــــــاف"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
		} else {

			if (ficheDeDetentionDto.getAnneePenal() == 0 && ficheDeDetentionDto.getMoisPenal() == 0
					&& ficheDeDetentionDto.getJourPenal() == 0) {
				p1 = new Phrase(("تاريـــخ صـــدور البطاقــــــة"), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBorder(0);
				c1.setColspan(30);
				c1.setPaddingBottom(7f);
				table.addCell(c1);
			} else {
				p1 = new Phrase(("تـــــــــــاريخ الحكــــــــــــــم"), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBorder(0);
				c1.setColspan(30);
				c1.setPaddingBottom(7f);
				table.addCell(c1);
			}

		}

		// --------------- tribunal--------------------

		p1 = new Phrase(affairePricipale.getTribunal().getNom_tribunal().toString(),
				boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(65);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase((":"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase(("المحكمـــــــــــــــــــــــــــــة"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		// --------------- tribunal--------------------

		String titreAccusationSring1 = " ";

		List<TitreAccusationDto> titreAccusationsDto = affairePricipale.getTitreAccusations();

		List<TitreAccusation> titreAccusations = titreAccusationsDto.stream().map(TitreAccusationConverter::dtoToEntity)
				.collect(Collectors.toList());
		for (int i = 0; i < titreAccusations.size(); i++) {
			titreAccusationSring1 += titreAccusations.get(i).getTitreAccusation();
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

		p1 = new Phrase((":"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase(("التهمـــــــــــــــــــــــــــــــــة"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		// --------------- tribunal--------------------

		p1 = new Phrase((ficheDeDetentionDto.getArrestation().getDate().toString()), boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(65);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase((":"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase(("تــــاريخ الإيـــداع بالمــــركز"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		// --------------- tribunal--------------------

		if (ficheDeDetentionDto.getDateDebut() != null) {

			p1 = new Phrase((ficheDeDetentionDto.getDateDebut().toString()), boldfontLabelAmiri);

			// p1 = new Phrase( (ficheDeDetentionDto.getDateDebut().toString()),
			// boldfontLabelAmiri);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(65);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			p1 = new Phrase((":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);

			table.addCell(c1);

			p1 = new Phrase(("تاريخ بداية العقـــــــــــــــاب"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
		}

		// --------------- tribunal--------------------
		if (ficheDeDetentionDto.getArrestation().getLiberation() == null) {
			if (ficheDeDetentionDto.getDateFin() != null) {

				p1 = new Phrase((ficheDeDetentionDto.getDateFin().toString()), boldfontLabelAmiri);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBorder(0);
				c1.setColspan(65);
				c1.setPaddingBottom(7f);

				table.addCell(c1);

				p1 = new Phrase((":"), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(0);
				c1.setColspan(5);

				table.addCell(c1);

				p1 = new Phrase(("تاريخ الســـــــــــــــــــــــراح"), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBorder(0);
				c1.setColspan(30);
				c1.setPaddingBottom(7f);
				table.addCell(c1);
			}
		}

		// --------------- tribunal--------------------

		if (!(ficheDeDetentionDto.getAnneeArret() == 0 && ficheDeDetentionDto.getMoisArret() == 0
				&& ficheDeDetentionDto.getJourArret() == 0)) {

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
				duree = duree + "من  " + outputFormat.format(ap.getDateDebut()) + " إلى  "
						+ outputFormat.format(ap.getDateFin()) + "\n";
				p1 = new Phrase(duree, boldfontFamielle);
			}

			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(40);
			c1.setPaddingBottom(7f);
			table.addCell(c1);
			p1 = new Phrase((arretA) + (arretM) + (arretJ),
					boldfontLabelAmiri);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(25);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			p1 = new Phrase((":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);

			table.addCell(c1);

			p1 = new Phrase(("إيقــــــاف تحفظـــــــــــــــي"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

		}
		String classePenale = "--";
		int nbrRetour = (int) ficheDeDetentionDto.getArrestation().getArrestationId().getNumOrdinale();
		if (nbrRetour > 1) {
			classePenale = "عـــــ" + "0" + nbrRetour + " " + "ـــــــــائـــد";
		} else {
			classePenale = ficheDeDetentionDto.getArrestation().getEnfant().getClassePenale().getLibelle_classe_penale()
					.toString();
		}

		if (ficheDeDetentionDto.getArrestation().getEnfant().getSexe().equals("أنثى")) {
			classePenale += "ة";
		}
		p1 = new Phrase(classePenale, boldfontLabelAmiri);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setBorder(0);
		c1.setColspan(65);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		p1 = new Phrase((":"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);
		c1.setColspan(5);

		table.addCell(c1);

		p1 = new Phrase(("العقوبــــــات السابقــــــــــة"), boldfontLabel);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setBorder(0);
		c1.setColspan(30);
		c1.setPaddingBottom(7f);
		table.addCell(c1);

		// --------------- tribunal--------------------
		if (ficheDeDetentionDto.getTotaleEchappe() != 0 || ficheDeDetentionDto.getTotaleRecidence() != 0
				|| ficheDeDetentionDto.getTotaleRecidenceWithetabChangeManiere() != 0) {

			// if (ficheDeDetentionDto.getTotaleRecidence() != 0) {
			p1 = new Phrase(

					("نقــــــل ") + (" " + ficheDeDetentionDto.getTotaleRecidence() + " "),
					boldfontLabelAmiri);
			// }

			if (ficheDeDetentionDto.getTotaleEchappe() != 0) {
				p1.add(((" و ") + ("فــــــرار ")
						+ (" " + ficheDeDetentionDto.getTotaleEchappe() + " ")));
			}
			if (ficheDeDetentionDto.getTotaleRecidenceWithetabChangeManiere() != 0) {
				p1.add(((" و ") + ("تغير وسيلة ")
						+ (" " + ficheDeDetentionDto.getTotaleRecidenceWithetabChangeManiere() + " ")));
			}

			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(65);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			p1 = new Phrase((":"), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(5);

			table.addCell(c1);

			p1 = new Phrase(("المـــــــلاحظـــــــات "), boldfontLabel);
			c1 = new PdfPCell(p1);
			c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(30);
			c1.setPaddingBottom(7f);
			table.addCell(c1);

			Residence r = residenceRepository.findMaxResidence(ficheDeDetentionDto.getArrestation().getEnfant().getId(),
					ficheDeDetentionDto.getArrestation().getArrestationId().getNumOrdinale());

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

			Echappes e = echappesRepository
					.findByIdEnfantAndResidenceTrouverNull(ficheDeDetentionDto.getArrestation().getEnfant().getId());
			Echappes eLast = echappesRepository.findMaxEchappes(
					ficheDeDetentionDto.getArrestation().getEnfant().getId(),
					ficheDeDetentionDto.getArrestation().getArrestationId().getNumOrdinale());

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

			if (ficheDeDetentionDto.getTotaleRecidenceWithetabChangeManiere() != 0) {
				Residence c = residenceRepository.findMaxResidenceWithEtabChangeManiere(
						ficheDeDetentionDto.getArrestation().getEnfant().getId(),
						ficheDeDetentionDto.getArrestation().getArrestationId().getNumOrdinale());

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

			// --------------- nom --------------------

			p1 = new Phrase(boldConf
					.format(ficheDeDetentionDto.getArrestation().getEnfant().getNom() + " بن "
							+ ficheDeDetentionDto.getArrestation().getEnfant().getNomPere() + " بن "
							+ ficheDeDetentionDto.getArrestation().getEnfant().getNomGrandPere() + " "
							+ ficheDeDetentionDto.getArrestation().getEnfant().getPrenom()),
					boldfontLabelAmiri);
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);

			c1.setColspan(100);
			c1.setBorderWidth(1);

			c1.setBorder(Rectangle.BOX);
			c1.setBackgroundColor(new BaseColor(240, 240, 240)); // Couleur de fond plus claire
			c1.setPaddingBottom(7f);

			tableLien.addCell(c1);

			p1 = new Phrase(" ");
			c1 = new PdfPCell(p1);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setColspan(100);

			tableLien.addCell(c1);
			List<AffaireDto> affaireAffiche = ficheDeDetentionDto.getAffaires();

			for (int i = 0; i < affaireAffiche.size(); i++) {

				p1 = new Phrase((" "), boldfontLabel);

				p1 = new Phrase(" ", boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setBorder(0);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);

				c1.setColspan(70);
				tableAffaire.addCell(c1);

				p1 = new Phrase((i + 1) + " " + (" العدد الرتبي للقضية   "), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(30);
				tableAffaire.addCell(c1);

				p1 = new Phrase((affaireAffiche.get(i).getAffaireId().getNumAffaire()) + " "
						+ affaireAffiche.get(i).getTribunal().getNom_tribunal(), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);

				c1.setColspan(70);
				tableAffaire.addCell(c1);
				p1 = new Phrase(("  القضية"), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(30);
				tableAffaire.addCell(c1);

				System.out.println(affaireAffiche.get(i).getTypeDocument().toString() + " "
						+ affaireAffiche.get(i).getAffaireId().getIdEnfant().toString());

				switch (affaireAffiche.get(i).getTypeDocument().toString().trim()) {
					case "CD":
						p1 = new Phrase(("بطاقة إيداع"), boldfontLabelAmiri);

						break;

					case "CH":
						p1 = new Phrase(("بطاقة إيواء"), boldfontLabelAmiri);

						break;

					case "ArretEx":

						if (affaireAffiche.get(i).getTypeFile() != null) {
							if (affaireAffiche.get(i).getTypeFile().toString().equals("ArretEx".toString())) {

								p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
										+ ("إيقاف تنفيذ الحكم"), boldfontLabelAmiri);
							} else if (affaireAffiche.get(i).getTypeFile().toString().equals("L".toString())) {

								p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
										+ ("ســــــــــــراح"), boldfontLabelAmiri);

							}
						} else {

							p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
									+ ("إيقاف تنفيذ الحكم"), boldfontLabelAmiri);
						}
						break;
					case "CJ":
						p1 = new Phrase(("مضمون حكم"), boldfontLabelAmiri);

						break;

					case "T":
						if (affaireAffiche.get(i).getTypeFile() != null) {
							if (affaireAffiche.get(i).getTypeFile().toString().equals("T".toString())) {
								p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
										+ ("إحــــــالة"), boldfontLabelAmiri);
							} else if (affaireAffiche.get(i).getTypeFile().toString().equals("A".toString())) {
								p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
										+ ("تخلــــــي"), boldfontLabelAmiri);
							} else if (affaireAffiche.get(i).getTypeFile().toString().equals("G".toString())) {
								p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
										+ ("تعهــــــد"), boldfontLabelAmiri);
							}
						} else {
							p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
									+ ("إحــــــالة"), boldfontLabelAmiri);
						}

						break;

					case "AE":
						p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
								+ ("طعن الطفل بالاستئناف"), boldfontLabelAmiri);

						break;

					case "AP":
						p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
								+ ("طعن النيابة بالاستئناف"), boldfontLabelAmiri);

						break;

					case "CR":
						p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
								+ ("مراجعة"), boldfontLabelAmiri);

						break;
					case "CRR":
						p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
								+ ("قرار رفض المراجعة"), boldfontLabelAmiri);

						break;
					case "CP":
						p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
								+ ("قرار تمديد"), boldfontLabelAmiri);

						break;
					case "CHL":
						p1 = new Phrase(affaireAffiche.get(i).getDateEmissionDocument().toString() + " "
								+ ("قرار تغير مكان الإيداع "), boldfontLabelAmiri);

						break;

					default:
						p1 = new Phrase(("--"), boldfontLabelAmiri);

				}

				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				// c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(70);
				tableAffaire.addCell(c1);

				p1 = new Phrase(("  نوع الوثيقة"), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
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
						|| affaireAffiche.get(i).getTypeDocument().toString().equals("CP")
						|| (affaireAffiche.get(i).getAnnee() == 0 && affaireAffiche.get(i).getMois() == 0
								&& affaireAffiche.get(i).getJour() == 0)) {
					labelAffair = "تاريخ صدور البطاقة ";
				} else {
					if (affaireAffiche.get(i).getTypeDocument().toString().equals("CHL")) {
						labelAffair = "تاريخ القضية ";

					} else {
						labelAffair = "تاريخ الحكم";
					}
				}

				p1 = new Phrase((labelAffair), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(30);
				tableAffaire.addCell(c1);

				String titreAccusationSring = " ";
				for (TitreAccusationDto titreAccusation : affaireAffiche.get(i).getTitreAccusations()) {
					titreAccusationSring = titreAccusationSring + titreAccusation.getTitreAccusation() + " ";

				}
				p1 = new Phrase(titreAccusationSring.trim(), boldfontLabelAmirix);

				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				// c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(70);
				tableAffaire.addCell(c1);
				p1 = new Phrase(("التهمة "), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(30);
				tableAffaire.addCell(c1);

				if (affaireAffiche.get(i).getAffaireAffecter() != null) {
					String remarque = " ";
					// remarque = affaireAffiche.get(i).getTypeJuge().getLibelle_typeJuge() + " إلى
					// القضية عدد : "
					// + affaireAffiche.get(i).getAffaireAffecter().getAffaireId().getNumAffaire() +
					// " ";
					p1 = new Phrase((affaireAffiche.get(i).getAffaireId().getNumAffaire())
							+ affaireAffiche.get(i).getTypeJuge().getLibelle_typeJuge() + " إلى القضية عدد   ",
							boldfontLabelAmiri);
					c1 = new PdfPCell(p1);
					c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);

					c1.setColspan(70);
					tableAffaire.addCell(c1);
				}

				else {
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
							} else if ((affaireAffiche.get(i).getMois() >= 3)
									&& (affaireAffiche.get(i).getMois() <= 10)) {
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
							} else if ((affaireAffiche.get(i).getJour() >= 3)
									&& (affaireAffiche.get(i).getJour() <= 10)) {
								jugeJ = affaireAffiche.get(i).getJour() + " " + "أيام" + " ";
							} else {
								jugeJ = affaireAffiche.get(i).getJour() + "  " + "يوم" + " ";
							}

						}
						String remarque = " ";
						if (affaireAffiche.get(i).getTypeJuge() != null) {
							remarque = remarque + " " + affaireAffiche.get(i).getTypeJuge().getLibelle_typeJuge() + " ";
						}
						if (affaireAffiche.get(i).getTypeDocument().toString().equals("ArretEx")) {
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

						if (affaireAffiche.get(i).getTypeDocument().toString().equals("ArretEx")) {

							String remarque = " إيقاف الحكم   سراح ";

							if (affaireAffiche.get(i).getAffaireAffecter() != null) {
								remarque = remarque + " تم الضم إلى القضية عدد :  "
										+ affaireAffiche.get(i).getAffaireAffecter().getAffaireId().getNumAffaire()
										+ " ";
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
								remarque = remarque + "إيقــــاف";
							}
							if (affaireAffiche.get(i).getTypeJuge() != null) {
								remarque = remarque + " " + affaireAffiche.get(i).getTypeJuge().getLibelle_typeJuge()
										+ " ";
							}

							if (affaireAffiche.get(i).getAffaireAffecter() != null) {
								remarque = remarque + "  تم الضم إلى القضية عدد :  "
										+ affaireAffiche.get(i).getAffaireAffecter().getAffaireId().getNumAffaire()
										+ " ";
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
				}
				p1 = new Phrase(("نص الحكم"), boldfontLabel);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBackgroundColor(new BaseColor(210, 210, 210));
				c1.setColspan(30);
				tableAffaire.addCell(c1);

				p1 = new Phrase(("  "), boldfontLabelAmiri);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(0);

				c1.setColspan(70);
				tableAffaire.addCell(c1);
				p1 = new Phrase(("  "), boldfontLabelAmiri);
				c1 = new PdfPCell(p1);
				c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(0);

				c1.setColspan(30);
				tableAffaire.addCell(c1);

			}

		}
		Rectangle rect = new Rectangle(15, 20, 580, 690);
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(1);

		document.add(tableTop);
		document.add(tTitre);
		document.add(rect);
		document.add(tArr);

		document.add(table);
		if (!pDFPenaleDTO.isSansDetail()) {
			document.newPage();
			Rectangle rect2 = new Rectangle(15, 20, 580, 793);
			rect2.setBorder(Rectangle.BOX);
			rect2.setBorderWidth(1);

			document.add(tableLien);
			document.add(rect2);
			document.add(tableAffaire);
		}
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

	private String ecrireStatut(FicheDeDetentionDto ficheDeDetentionDto) {
		String statutJudiciaire = "**";
		if (ficheDeDetentionDto.getEtatJuridique().equals("juge")) {
			statutJudiciaire = "محكــــــــــــــوم";
			if (ficheDeDetentionDto.getArrestation().getEnfant().getSexe().equals("أنثى")) {
				statutJudiciaire = "محكــــــــــــــومة";
			}

		} else if (ficheDeDetentionDto.getEtatJuridique().equals("arret")) {

			if (ficheDeDetentionDto.isAppelParquet()) {
				statutJudiciaire = ficheDeDetentionDto.getDateAppelParquet()
						+ "موقـــوف  طعــن النيابـة بالاستئناف ";
				if (ficheDeDetentionDto.getArrestation().getEnfant().getSexe().equals("أنثى")) {
					statutJudiciaire = ficheDeDetentionDto.getDateAppelParquet()
							+ "موقـــوفة  طعــن النيابـة بالاستئناف ";
				}
				ficheDeDetentionDto.setDateFin(null);
			} else if (!ficheDeDetentionDto.isAppelParquet() && !ficheDeDetentionDto.isAppelEnfant()) {
				statutJudiciaire = "موقــــــــــــــوف";
				if (ficheDeDetentionDto.getArrestation().getEnfant().getSexe().equals("أنثى")) {
					statutJudiciaire = "موقــــــــــــــوفة";
				}
				ficheDeDetentionDto.setDateFin(null);
			}

			else if (ficheDeDetentionDto.isAppelEnfant() && !ficheDeDetentionDto.isAppelParquet()) {
				statutJudiciaire = ficheDeDetentionDto.getDateAppelEnfant()
						+ "موقـــوف   طعــن الطفــل بالاستئناف ";
				if (ficheDeDetentionDto.getArrestation().getEnfant().getSexe().equals("أنثى")) {
					statutJudiciaire = ficheDeDetentionDto.getDateAppelEnfant()
							+ "موقـــوفة   طعــن الطفــل بالاستئناف ";
				}
			}

		}

		else if (ficheDeDetentionDto.getArrestation().getLiberation() != null) {

			if (ficheDeDetentionDto.getArrestation().getLiberation().getEtabChangeManiere() == null) {
				statutJudiciaire = "  " + ficheDeDetentionDto.getArrestation().getLiberation().getDate().toString()
						+ "  " +

						ficheDeDetentionDto.getArrestation().getLiberation().getCauseLiberation()
								.getLibelleCauseLiberation().toString();

			} else {

				statutJudiciaire = "  " + ficheDeDetentionDto.getArrestation().getLiberation().getDate().toString()
						+ "  " +

						ficheDeDetentionDto.getArrestation().getLiberation().getCauseLiberation()
								.getLibelleCauseLiberation().toString().trim()
						+ " إلى "
						+ ficheDeDetentionDto.getArrestation().getLiberation().getEtabChangeManiere()
								.getLibelle_etabChangeManiere().toString().trim();

			}

		} else {
			statutJudiciaire = "--";

		}
		return statutJudiciaire;
	}

	String getDocumentDescription(AffaireDto affaireAffiche) {
		String typeDocument = affaireAffiche.getTypeDocument().toString().trim();
		String dateEmission = affaireAffiche.getDateEmissionDocument().toString();
		String typeFile = affaireAffiche.getTypeFile() != null ? affaireAffiche.getTypeFile().toString() : null;

		switch (typeDocument) {
			case "CD":
				return "بطاقة إيداع";

			case "CH":
				return "بطاقة إيواء";

			case "ArretEx":
				if ("ArretEx".equals(typeFile)) {
					return dateEmission + " إيقاف تنفيذ الحكم";
				} else if ("L".equals(typeFile)) {
					return dateEmission + " ســــــــــــراح";
				} else {
					return dateEmission + " إيقاف تنفيذ الحكم";
				}

			case "CJ":
				return "مضمون حكم";

			case "T":
				if ("T".equals(typeFile)) {
					return dateEmission + " إحــــــالة";
				} else if ("A".equals(typeFile)) {
					return dateEmission + " تخلــــــي";
				} else if ("G".equals(typeFile)) {
					return dateEmission + " تعهــــــد";
				} else {
					return dateEmission + " إحــــــالة";
				}

			case "AE":
				return dateEmission + " طعن الطفل بالاستئناف";

			case "AP":
				return dateEmission + " طعن النيابة بالاستئناف";

			case "CR":
				return dateEmission + " مراجعة";

			case "CRR":
				return dateEmission + " قرار رفض المراجعة";

			case "CP":
				return dateEmission + " قرار تمديد";

			case "CHL":
				return dateEmission + " قرار تغير مكان الإيداع ";

			default:
				return "--";
		}
	}

}

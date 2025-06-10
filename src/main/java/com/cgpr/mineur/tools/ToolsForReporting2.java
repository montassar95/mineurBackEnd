
package com.cgpr.mineur.tools;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import com.cgpr.mineur.config.ConfigShaping;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Echappes;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.serviceReporting.GenererRapportPdfActuelService;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ToolsForReporting2 {

	public static ConfigShaping boldConf = new ConfigShaping();
	public static Font boldfontLabelTop = boldConf.getFontForArabicAmiri1(12);
	public static Font boldfontTitle = boldConf.getFontForArabicAmiri(30);
	public static Font boldfontTitleStatique = boldConf.getFontForArabic(30);
	public static Font boldfontLabel20 = boldConf.getFontForArabic(20);
	public static Font boldfontLabel = boldConf.getFontForArabic(16);
	public static Font boldfontFamielle = boldConf.getFontForArabic(14);
	public static Font boldfontFamielle11 = boldConf.getFontForArabic(11);
	public static Font boldfontFamielle13 = boldConf.getFontForArabic(13);
	public static Font boldfontFamielle12 = boldConf.getFontForArabic(12);
	public static Font boldfontLabelAmiri = boldConf.getFontForArabicAmiri(17);
	public static Font boldfontLabelAmiri14 = boldConf.getFontForArabicAmiri(13);
	public static Font boldfontLabelAmiri13 = boldConf.getFontForArabicAmiri(13);
	public static Font boldfontLabelAmiri11 = boldConf.getFontForArabicAmiri(12);
	public static Font boldfontLabelAmiri9 = boldConf.getFontForArabicAmiri(9);

	static {
		boldfontFamielle.setColor(BaseColor.BLUE);
		boldfontFamielle11.setColor(new BaseColor(51, 0, 0));
	}

	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");

	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static String getCurrentArabicMonth(int currentMonth) {
		String[] arabicMonths = { "جانفي", "فيفري", "مارس", "أفريل", "ماي", "جوان", "جويلية", "أوت", "سبتمبر", "أكتوبر",
				"نوفمبر", "ديسمبر" };

		int currentMonthIndex = currentMonth - 1;

		return arabicMonths[currentMonthIndex];
	}

	public static Predicate<Affaire> isTypeJugeMatch(long typeId) {
		return aff -> aff.getTypeJuge() != null && aff.getTypeJuge().getId() == typeId;
	}

	public static Date calculateStartDate(PDFListExistDTO pDFListExistDTO) {
		if (pDFListExistDTO.getAge1() != 0 && pDFListExistDTO.getAge2() != 0) {
			LocalDate localDate = LocalDate.now();
			Calendar cal = Calendar.getInstance();
			int year = Calendar.getInstance().get(Calendar.YEAR);

			cal.set(Calendar.YEAR, (year - pDFListExistDTO.getAge2()) - 1);
			cal.set(Calendar.MONTH, localDate.getMonthValue() - 1);
			cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());

			return cal.getTime();
		}
		return null;
	}

	public static Date calculateEndDate(PDFListExistDTO pDFListExistDTO) {
		if (pDFListExistDTO.getAge1() != 0 && pDFListExistDTO.getAge2() != 0) {
			LocalDate localDate = LocalDate.now();
			Calendar cal = Calendar.getInstance();
			int year = Calendar.getInstance().get(Calendar.YEAR);

			cal.set(Calendar.YEAR, year - pDFListExistDTO.getAge1());
			cal.set(Calendar.MONTH, localDate.getMonthValue() - 1);
			cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());

			return cal.getTime();
		}
		return null;
	}

	public static Date parseDate(String dateString) {
		if (dateString != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return formatter.parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace(); // Handle the exception according to your needs
			}
		}
		return null;
	}

	public static String determineStatus(Residence residence) {
		// String status;
		if (residence.getArrestation().getSituationJudiciaire() != null) {
			switch (residence.getArrestation().getSituationJudiciaire().toString()) {
				case "juge":
					return "محكـــوم";

				case "arret":
					return "موقـــوف";

				case "libre":
					return "في حالـــة ســراح";

				case "pasInsertionLiberable":
					return "لم يتم إدراج السراح";

				default:
					return "...";

			}
		}
		;
		return "vide";

	}

	public static String generateTimeUnitString(int value, String singular, String dual, String plural,
			String fallback) {
		if (value == 1) {
			return " " + singular + " ";
		} else if (value == 2) {
			return " " + dual + " ";
		} else if (value >= 3 && value <= 10) {
			return value + " " + plural + " ";
		} else {
			return value + " " + fallback + " ";
		}
	}
	    
	public static String generateLegalCaseString(
			Integer totalAnnees,
		      Integer totalMois,
		      Integer totalJours ) {
		StringBuilder result = new StringBuilder();
	 
		if (totalAnnees != 0) {
			result.append(generateTimeUnitString(totalAnnees, "عام", "عامين", "أعوام", "عام"));
			if (totalMois != 0 || totalJours != 0) {
				result.append(" و ");
			}
		}

		if (totalMois != 0) {
			result.append(generateTimeUnitString(totalMois, "شهر", "شهرين", "أشهر", "شهر"));
			if (totalJours != 0) {
				result.append(" و ");
			}
		}

		if (totalJours != 0) {
			result.append(generateTimeUnitString(totalJours, "يوم", "يومين", "أيام", "يوم"));
		}

		 

		return result.toString().trim();
	}
	public static String generateLegalCaseString(Affaire affaire) {
		StringBuilder result = new StringBuilder();
		result.append("مدة الحكم: ");
		if (affaire.getAnnee() != 0) {
			result.append(generateTimeUnitString(affaire.getAnnee(), "عام", "عامين", "أعوام", "عام"));
			if (affaire.getMois() != 0 || affaire.getJour() != 0) {
				result.append(" و ");
			}
		}

		if (affaire.getMois() != 0) {
			result.append(generateTimeUnitString(affaire.getMois(), "شهر", "شهرين", "أشهر", "شهر"));
			if (affaire.getJour() != 0) {
				result.append(" و ");
			}
		}

		if (affaire.getJour() != 0) {
			result.append(generateTimeUnitString(affaire.getJour(), "يوم", "يومين", "أيام", "يوم"));
		}

		if (affaire.getAffaireAffecter() != null) {
			result.append("  تم الضم إلى القضية عدد : ")
					.append(affaire.getAffaireAffecter().getAffaireId().getNumAffaire()).append(" ");
		} else {
			if (affaire.getTypeJuge() != null) {
				result.append(affaire.getTypeJuge().getLibelle_typeJuge()).append(" ");
			}
			if (affaire.getTypeDocument().toString().equals("ArretEx")) {
				result.append(" تم  إيقاف الحكم   ");
			}
		}

		return result.toString().trim();
	}
	public static String generateRemarqueString(Affaire affaire) {
		if (affaire.getTypeDocument().toString().equals("ArretEx")) {
			return "    تم  إيقاف الحكم  : سراح " + (affaire.getAffaireAffecter() != null
					? "  تم الضم إلى القضية عدد : " + affaire.getAffaireAffecter().getAffaireId().getNumAffaire()
					: "");
		} else {
			StringBuilder remarque = new StringBuilder();

			if (affaire.getAffaireAffecter() != null) {
				remarque.append("  تم الضم إلى القضية عدد : ")
						.append(affaire.getAffaireAffecter().getAffaireId().getNumAffaire());
			} else if (affaire.getTypeJuge() != null) {
				remarque.append(affaire.getTypeJuge().getLibelle_typeJuge());
			} else {
				remarque.append("---");
			}

			return remarque.toString();
		}
	}

	public static String getTitreString(String etatJuridiue) {
		String titreString = " ";

		switch (etatJuridiue) {
			case "arret":
				titreString = "الموقوفين ";

				break;
			case "juge":
				titreString = "المحكومين";

				break;

			case "audience":
				titreString = "تغيرات و جلسات ";

				break;

			case "attetAP":
				titreString = "الموقوفين ( إستئناف النيابة )";

				break;

			case "attetAE":
				titreString = "الموقوفين ( إستئناف الطفل )";

				break;
			case "attetT":
				titreString = "الموقوفين ( إحالة )";

				break;

			case "jugeR":
				titreString = "المحكومين ( مراجعة )";

				break;

			case "all":
				titreString = "المقيمين";

				break;

			case "devenuMajeur":
				titreString = "البالغين لسن الرشد";

				break;
			case "entreReelle":
				titreString = "الداخلون";

				//

				break;

			case "libere":
				titreString = "السراحات";

				//

				break;

			case "seraLibere":
				titreString = "المفرج عنهم";

				break;

			case "sortieMutation":
				titreString = "الواقع نقلتهم";

				//

				break;

			case "entreeMutation":
				titreString = "الوافدون";

				//

				break;

			case "nonAff":
				titreString = " المقيمين دون قضايا";

				//
				break;

			case "detenusDeMemeAffaire":
				titreString = " المودعين في  القضية عدد   ";

				//
				break;

			default:
				titreString = "المقيمين";

		}

		return titreString;
	}

	public static String getTypeFileStatusAbondantAffaire(String typeFile) {
		if (typeFile != null) {
			switch (typeFile.trim()) {
				case "T":
					return "إحــــــالة";
				case "A":
					return "تخلــــــي";
				case "G":
					return "تعهــــــد";
				default:
					return "yyy";
			}
		}

		return "إحــــــالة";
	}

	public static void addCellulHistorique(Phrase p1, PdfPCell c1, PdfPTable tableTypeDocument, String titre) {
		p1 = new Phrase(titre, boldfontLabelAmiri13);
		c1 = new PdfPCell(p1);
		c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setColspan(50);
		c1.setBorder(0);
		tableTypeDocument.addCell(c1);
	}

	public static void processTableTypeDocument(Phrase p1, PdfPCell c1, PdfPTable tableTypeDocument, Affaire affaire) {

		try {
			addCellulHistorique(
					p1,
					c1,
					tableTypeDocument,
					affaire.getDateEmission().toString().trim());

		} catch (Exception e) {
			System.out.println("ici erreur : ");
			System.out.println(affaire.getAffaireId().toString());
		}

		switch (affaire.getTypeDocument()) {
			case "CD":

				addCellulHistorique(p1, c1, tableTypeDocument, "بطاقة إيداع");
				break;

			case "CH":

				addCellulHistorique(p1, c1, tableTypeDocument, "بطاقة إيواء");

				break;

			case "ArretEx":

				addCellulHistorique(p1, c1, tableTypeDocument, "تاريخ  الحكم");

				addCellulHistorique(p1, c1, tableTypeDocument, affaire.getDateEmissionDocument().toString().trim());

				String typeFileArretEx = affaire.getTypeFile();
				String typeArretEx = (typeFileArretEx == null || typeFileArretEx.equals("ArretEx"))
						? "إيقاف تنفيذ الحكم"
						: "ســــــــــــراح";

				addCellulHistorique(p1, c1, tableTypeDocument, typeArretEx);

				break;
			case "CJ":

				addCellulHistorique(p1, c1, tableTypeDocument, "مضمون حكم");

				break;

			case "T":

				addCellulHistorique(p1, c1, tableTypeDocument, "صدور البطاقة");

				addCellulHistorique(p1, c1, tableTypeDocument, affaire.getDateEmissionDocument().toString().trim());

				addCellulHistorique(p1, c1, tableTypeDocument,
						getTypeFileStatusAbondantAffaire(affaire.getTypeFile()));

				break;

			case "AE":

				addCellulHistorique(p1, c1, tableTypeDocument, "تاريخ  الحكم");

				addCellulHistorique(p1, c1, tableTypeDocument, affaire.getDateEmissionDocument().toString().trim());

				addCellulHistorique(p1, c1, tableTypeDocument, "استئناف الطفل");

				break;

			case "AP":

				addCellulHistorique(p1, c1, tableTypeDocument, "تاريخ  الحكم");

				addCellulHistorique(p1, c1, tableTypeDocument, affaire.getDateEmissionDocument().toString().trim());

				addCellulHistorique(p1, c1, tableTypeDocument, "استئناف النيابة");

				break;

			case "CR":
				addCellulHistorique(p1, c1, tableTypeDocument, "تاريخ  الحكم");

				addCellulHistorique(p1, c1, tableTypeDocument, affaire.getDateEmissionDocument().toString().trim());

				addCellulHistorique(p1, c1, tableTypeDocument, "مراجعة");

				break;

			case "CRR":

				addCellulHistorique(p1, c1, tableTypeDocument, "تاريخ  الحكم");

				addCellulHistorique(p1, c1, tableTypeDocument, affaire.getDateEmissionDocument().toString().trim());

				addCellulHistorique(p1, c1, tableTypeDocument, "رفض المراجعة");

				break;

			case "CP":

				addCellulHistorique(p1, c1, tableTypeDocument, "صدور البطاقة");

				addCellulHistorique(p1, c1, tableTypeDocument, affaire.getDateEmissionDocument().toString().trim());

				addCellulHistorique(p1, c1, tableTypeDocument, "قرار تمديد");

				break;

			case "CHL":
				addCellulHistorique(p1, c1, tableTypeDocument, "تاريخ القضية");

				addCellulHistorique(p1, c1, tableTypeDocument, affaire.getDateEmissionDocument().toString().trim());

				addCellulHistorique(p1, c1, tableTypeDocument, "تغير مكان الإيداع");

				break;
			default:
				addCellulHistorique(p1, c1, tableTypeDocument, "-----");

		}

		if (affaire.getDateDebutPunition() != null) {

			addCellulHistorique(p1, c1, tableTypeDocument, affaire.getDateDebutPunition().toString().trim());

			addCellulHistorique(p1, c1, tableTypeDocument, "تاريخ البداية");

		}

		if (affaire.getDateFinPunition() != null && !affaire.getTypeDocument().toString().equals("AP")) {
			addCellulHistorique(p1, c1, tableTypeDocument, affaire.getDateFinPunition().toString().trim());

			addCellulHistorique(p1, c1, tableTypeDocument, "تاريخ النهاية");

		}
	}

	public static void addCellToHeaderTable(PdfPTable table, String text, int colspan) {
		PdfPCell cell = new PdfPCell(new Phrase(text, boldfontLabel));
		cell.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(new BaseColor(210, 210, 210));
		cell.setColspan(colspan);
		table.addCell(cell);
	}

	public static PdfPTable createPdfTable(int numColumns, float widthPercentage) {
		PdfPTable table = new PdfPTable(numColumns);
		table.setWidthPercentage(widthPercentage);

		return table;
	}

	public static PdfPTable createTopTable(String gouvernorat, String date) throws Exception {
		PdfPTable tableTop = new PdfPTable(3);
		tableTop.setWidthPercentage(100);

		Image image = getImage();

		PdfPCell c1Top;

		PdfPTable tab = new PdfPTable(1);
		tab.setWidthPercentage(100);

		addCellToTable(tab, "الجمهورية التونسية ", Element.ALIGN_RIGHT, 27f);
		addCellToTable(tab, "وزارة العدل ", Element.ALIGN_RIGHT, 36f);
		addCellToTable(tab, "الهيئة العامة للسجون والإصلاح ", Element.ALIGN_RIGHT);

		Phrase p1Top = new Phrase(boldConf.format(date) + " "
				+ boldConf.format(
						gouvernorat.trim()
								+ " " + "في"),
				boldfontLabelTop);
		c1Top = new PdfPCell(p1Top);
		c1Top.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1Top.setBorder(0);
		tableTop.addCell(c1Top);

		c1Top = new PdfPCell(image);
		c1Top.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1Top.setBorder(0);
		tableTop.addCell(c1Top);

		c1Top = new PdfPCell(tab);
		c1Top.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1Top.setBorder(0);
		tableTop.addCell(c1Top);

		return tableTop;
	}

	public static PdfPTable createTitleTable(String titreString) {
		PdfPTable tTitre = new PdfPTable(1);

		Phrase pTitre = new Phrase(titreString, boldfontTitle);

		PdfPCell cTitre = new PdfPCell(pTitre);
		cTitre.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

		cTitre.setPadding(70f);
		cTitre.setBorder(Rectangle.BOX);

		cTitre.setBackgroundColor(new BaseColor(230, 230, 230));
		cTitre.setBorderColor(BaseColor.BLACK);
		cTitre.setHorizontalAlignment(Element.ALIGN_CENTER);

		tTitre.setSpacingBefore(50f);
		tTitre.addCell(cTitre);

		cTitre.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

		return tTitre;
	}

	public static Image getImage() throws Exception {
		URL resource = GenererRapportPdfActuelService.class.getResource("/images/cgpr.png");
		Image image = Image.getInstance(resource);
		image.scaleAbsolute(110f, 100f);
		return image;
	}

	public static String buildTitreAccusationString(Affaire affaire) {
		String titreAccusationString = " ";
		try {
			for (TitreAccusation titreAccusation : affaire.getTitreAccusations()) {
				titreAccusationString = titreAccusationString + titreAccusation.getTitreAccusation() + " ";
			}
		} catch (Exception ex) {
			titreAccusationString = "EEE";
		}
		return titreAccusationString;
	}

	public static String buildTitreRemarque(Affaire affaire) {
		if (!(affaire.getAnnee() == 0 && affaire.getMois() == 0 && affaire.getJour() == 0)) {

			return generateLegalCaseString(affaire);

		}

		return generateRemarqueString(affaire);

	}

	public static String buildTitreTypeAffaire(Affaire affaire) {
		if (affaire.getTypeAffaire() != null) {
			return affaire.getTypeAffaire().getLibelle_typeAffaire();
		}
		return "...";

	}

	public static String buildTitreIndice(int j) {
		if (j < 9) {
			return "0" + (j + 1);
		}
		return (j + 1) + "";

	}

	public static void addEmptyAffaireCell(PdfPTable tableAffaire) {
		Phrase emptyPhrase = new Phrase("  قضية ", ToolsForReporting.boldfontFamielle);
		PdfPCell emptyCell = ToolsForReporting.createPdfPCell(emptyPhrase, 60, Element.ALIGN_RIGHT,
				new BaseColor(240, 240, 240));
		tableAffaire.addCell(emptyCell);
	}

	public static void buildNumArrestationTables(Residence residenceDTO, PdfPTable tableAffaire, int i) {
		PdfPTable tableNumArr = ToolsForReporting.createPdfTable(100, 100);

		Phrase p1;
		PdfPCell c1;

		p1 = new Phrase(residenceDTO.getNumArrestation(), ToolsForReporting.boldfontLabelAmiri14);
		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, new BaseColor(225, 225, 225));

		c1.setBorder(1);

		tableNumArr.addCell(c1);

		p1 = new Phrase(residenceDTO.getEtablissement().getLibelle_etablissement(),
				ToolsForReporting.boldfontLabelAmiri14);

		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, new BaseColor(225, 225, 225));

		c1.setBorder(1);
		tableNumArr.addCell(c1);
		String classePenale = "--";
		int nbrRetour = (int) residenceDTO.getArrestation().getArrestationId().getNumOrdinale();
		if (nbrRetour > 1) {
			classePenale = "عــ" + "0" + nbrRetour + "" + "ـــــــــائـد";

		} else {
			classePenale = residenceDTO.getArrestation().getEnfant().getClassePenale()
					.getLibelle_classe_penale();
		}
		p1 = new Phrase(classePenale, ToolsForReporting.boldfontLabelAmiri14);

		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, null);
		tableNumArr.addCell(c1);

		p1 = new Phrase(
				residenceDTO.getArrestation().getEnfant().getNationalite().getLibelle_nationalite(),
				ToolsForReporting.boldfontLabelAmiri14);

		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, new BaseColor(245, 245, 245));

		tableNumArr.addCell(c1);

		p1 = new Phrase("عام", ToolsForReporting.boldfontLabelAmiri9);

		c1 = ToolsForReporting.createPdfPCell(p1, 25, Element.ALIGN_CENTER, null);

		c1.setBorder(0);
		tableNumArr.addCell(c1);
		p1 = new Phrase(calculerAge(residenceDTO.getArrestation().getEnfant().getDateNaissance().toString()) + "",
				ToolsForReporting.boldfontFamielle13);
		// p1 = new Phrase("--");
		c1 = ToolsForReporting.createPdfPCell(p1, 30, Element.ALIGN_CENTER, null);

		c1.setBorder(0);
		tableNumArr.addCell(c1);

		p1 = new Phrase("السن", ToolsForReporting.boldfontLabelAmiri14);

		c1 = ToolsForReporting.createPdfPCell(p1, 45, Element.ALIGN_CENTER, null);

		c1.setBorder(0);
		tableNumArr.addCell(c1);

		p1 = new Phrase(residenceDTO.getNbVisite() + " عدد الزيارات", ToolsForReporting.boldfontFamielle12);
		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_RIGHT, null);

		c1.setBorder(1);

		tableNumArr.addCell(c1);

		p1 = new Phrase(ToolsForReporting.determineStatus(residenceDTO), ToolsForReporting.boldfontFamielle);

		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, null);
		c1.setBorder(1);
		tableNumArr.addCell(c1);

		Echappes e = residenceDTO.getArrestation().getEchappe();
		if (e != null) {
			p1 = new Phrase("في حالــــــة فـــرار", ToolsForReporting.boldfontFamielle);

			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, null);

			c1.setBorder(1);
			tableNumArr.addCell(c1);
		}

		c1 = new PdfPCell(tableNumArr);
		c1.setBackgroundColor(BaseColor.WHITE);
		c1.setColspan(9);
		tableAffaire.addCell(c1);

		p1 = new Phrase((i + 1) + "", ToolsForReporting.boldfontFamielle11);

		c1 = ToolsForReporting.createPdfPCell(p1, 3, Element.ALIGN_RIGHT, new BaseColor(225, 225, 225));

		c1.setBorderWidth(1);

		tableAffaire.addCell(c1);

	}

	public static void buildIdentiteTables(PdfPTable tableAffaire,
			Residence residenceDTO, String etatJuridiue, int i) {

		Phrase p1;
		PdfPCell c1;

		PdfPTable tableIdentite = new PdfPTable(100);
		tableIdentite.setWidthPercentage(100);

		p1 = new Phrase(residenceDTO.getArrestation().getEnfant().getId(), ToolsForReporting.boldfontLabelAmiri14);

		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, new BaseColor(190, 190, 190));

		c1.setBackgroundColor(new BaseColor(220, 220, 220));
		c1.setBorder(3);

		c1.setBorderColorTop(BaseColor.BLACK);
		c1.setBorderWidthTop(1);

		tableIdentite.addCell(c1);
		p1 = buildEnfantDetailsPhrase(residenceDTO.getArrestation().getEnfant());

		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, new BaseColor(225, 225, 225));

		c1.setBorder(3);
		c1.setBorderColor(new BaseColor(190, 190, 190));

		tableIdentite.addCell(c1);

		p1 = buildEnfantAddressPhrase(residenceDTO.getArrestation().getEnfant());

		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, new BaseColor(225, 225, 225));

		c1.setBorder(3);
		c1.setBorderColor(new BaseColor(190, 190, 190));

		tableIdentite.addCell(c1);

		p1 = buildEnfantBirthDetailsPhrase(residenceDTO.getArrestation().getEnfant());

		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, new BaseColor(225, 225, 225));

		c1.setBorder(3);
		c1.setBorderColor(new BaseColor(190, 190, 190));

		tableIdentite.addCell(c1);

		p1 = buildEnfantSocialDetailsPhrase(residenceDTO.getArrestation().getEnfant());

		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, new BaseColor(225, 225, 225));

		c1.setBorder(3);
		c1.setBorderColor(new BaseColor(190, 190, 190));

		tableIdentite.addCell(c1);

		p1 = buildEnfantOccupationDetailsPhrase(residenceDTO.getArrestation().getEnfant());
		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, new BaseColor(225, 225, 225));

		c1.setBorder(3);

		c1.setBorderColor(new BaseColor(190, 190, 190));
		tableIdentite.addCell(c1);

		p1 = new Phrase(residenceDTO.getArrestation().getDate() + " " + "تــاريخ الإيقـاف ",
				ToolsForReporting.boldfontFamielle13);

		c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, BaseColor.WHITE);

		c1.setBorder(0);

		tableIdentite.addCell(c1);

		Date dateDebut = residenceDTO.getArrestation().getDateDebut();
		if (dateDebut != null) {
			// Instant instant = dateDebut.toInstant();
			// LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

			p1 = new Phrase(simpleDateFormat.format(dateDebut) + " " + "  تــاريخ بــداية العقــاب  ",
					ToolsForReporting.boldfontFamielle13);

			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, BaseColor.WHITE);

			c1.setBorder(0);

			tableIdentite.addCell(c1);
		}

		Date dateFin = residenceDTO.getArrestation().getDateFin();
		if (dateFin != null) {
			// Instant instant = dateFin.toInstant();
			// LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

			p1 = new Phrase(simpleDateFormat.format(dateFin) + " " + "  تـــاريخ الســـــــــــراح  ",
					ToolsForReporting.boldfontFamielle13);

			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, BaseColor.WHITE);
			c1.setBorder(0);

			tableIdentite.addCell(c1);
		}

		if (hasEntryMutationData(residenceDTO)) {

			p1 = buildEntryMutationPhrase(residenceDTO);

			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, BaseColor.WHITE);
			c1.setBorder(0);

			tableIdentite.addCell(c1);
		}
		if (hasExitMutationData(residenceDTO)) {

			p1 = buildExitMutationPhrase(residenceDTO);

			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, BaseColor.WHITE);
			c1.setBorder(0);

			tableIdentite.addCell(c1);
		}
		if (residenceDTO.getStatut() == 2) {

			p1 = new Phrase("لم يتم الإستقبال بعد ", ToolsForReporting.boldfontLabel);

			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, BaseColor.WHITE);
			c1.setBorder(0);

			tableIdentite.addCell(c1);
		}

		if (residenceDTO.getArrestation().getLiberation() == null) {
			p1 = new Phrase(" ", ToolsForReporting.boldfontFamielle13);

			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, BaseColor.WHITE);
			c1.setBorder(0);
			c1.setBorderColorBottom(BaseColor.BLACK);
			c1.setBorderWidthBottom(1);

			tableIdentite.addCell(c1);
		}

		if (residenceDTO.getArrestation().getLiberation() != null) {
			p1 = new Phrase(residenceDTO.getArrestation().getLiberation().getDate().toString() + " " +
					residenceDTO.getArrestation().getLiberation().getCauseLiberation()
							.getLibelleCauseLiberation(),
					ToolsForReporting.boldfontFamielle13);

			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, BaseColor.WHITE);
			c1.setBorder(0);

			tableIdentite.addCell(c1);
			p1 = new Phrase(" ", ToolsForReporting.boldfontFamielle13);
			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, BaseColor.WHITE);

			c1.setBorder(0);
			c1.setBorderColorBottom(BaseColor.BLACK);
			c1.setBorderWidthBottom(1);

			tableIdentite.addCell(c1);

		}

		if (etatJuridiue != null && etatJuridiue.toString().equals("seraLibere")) {

			p1 = new Phrase(residenceDTO.getDateFin().toString() + " " + "يفرج عنه بتاريخ",
					ToolsForReporting.boldfontFamielle13);
			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, BaseColor.WHITE);

			c1.setBorder(0);

			tableIdentite.addCell(c1);
			p1 = new Phrase(" ", ToolsForReporting.boldfontFamielle13);
			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, BaseColor.WHITE);

			c1.setBorder(0);
			c1.setBorderColorBottom(BaseColor.BLACK);
			c1.setBorderWidthBottom(1);

			tableIdentite.addCell(c1);

		}

		c1 = new PdfPCell(tableIdentite);
		c1.setBorderWidth(1);
		c1.setColspan(28);
		c1.setBorderWidth(1);
		c1.setBackgroundColor(new BaseColor(250, 250, 250));
		tableAffaire.addCell(c1);

		buildNumArrestationTables(residenceDTO, tableAffaire, i);

	}

	public static PdfPCell createPdfPCell(Phrase phrase, int colSpan, int horizontalAlignment,
			BaseColor backgroundColor) {
		PdfPCell cell = new PdfPCell(phrase);
		cell.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		cell.setHorizontalAlignment(horizontalAlignment);
		cell.setColspan(colSpan);
		if (backgroundColor != null) {
			cell.setBackgroundColor(backgroundColor);
		}
		return cell;
	}

	// OverLoad methode addCellToTable version 1
	public static void addCellToTable(PdfPTable table, String text, int colspan,
			int horizontalAlignment, BaseColor backgroundColor, int fixedHeight) {

		Phrase phrase = new Phrase(text, ToolsForReporting.boldfontLabel20);
		PdfPCell cell = new PdfPCell(phrase);
		cell.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(colspan);
		cell.setFixedHeight(fixedHeight);

		if (backgroundColor != null) {
			cell.setBackgroundColor(backgroundColor);
		}

		table.addCell(cell);
	}

	// OverLoad methode addCellToTable version 1
	public static void addCellToTable(PdfPTable table, String text, int alignment, float paddingRight) {
		Phrase phrase = null;
		try {
			phrase = new Phrase(boldConf.format(text), boldfontLabelTop);
		} catch (ArabicShapingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PdfPCell cell = new PdfPCell(phrase);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(0);
		cell.setPaddingRight(paddingRight);
		table.addCell(cell);
	}

	// OverLoad methode addCellToTable version 1
	public static void addCellToTable(PdfPTable table, String text, int alignment) {
		addCellToTable(table, text, alignment, 0);
	}

	public static void buildAffaireTables(List<Affaire> affaires, PdfPTable tableAffaire) {

		PdfPTable tableNumAff = ToolsForReporting.createPdfTable(100, 100);
		PdfPTable tableTypeDocument = ToolsForReporting.createPdfTable(100, 100);
		PdfPTable tableAffByEnfant = ToolsForReporting.createPdfTable(100, 100);
		PdfPTable tableTypeAff = ToolsForReporting.createPdfTable(100, 100);

		Phrase p1;
		PdfPCell c1;
		for (int j = 0; j < affaires.size(); j++) {

			p1 = new Phrase(ToolsForReporting2.buildTitreTypeAffaire(affaires.get(j)),
					ToolsForReporting.boldfontLabelAmiri13);
			c1 = ToolsForReporting.createPdfPCell(p1, 35, Element.ALIGN_CENTER, new BaseColor(240, 240, 240));

			c1.setBorder(0);

			tableNumAff.addCell(c1);

			p1 = new Phrase(affaires.get(j).getTribunal().getNom_tribunal(),
					ToolsForReporting.boldfontFamielle13);
			c1 = ToolsForReporting.createPdfPCell(p1, 41, Element.ALIGN_CENTER, new BaseColor(240, 240, 240));

			c1.setBorder(0);

			tableNumAff.addCell(c1);

			p1 = new Phrase(affaires.get(j).getAffaireId()
					.getNumAffaire().toString(), ToolsForReporting.boldfontLabelAmiri13);
			c1 = ToolsForReporting.createPdfPCell(p1, 24, Element.ALIGN_RIGHT, new BaseColor(240, 240, 240));

			c1.setBorder(0);

			tableNumAff.addCell(c1);

			String titreAccusationSring = ToolsForReporting.buildTitreAccusationString(
					affaires.get(j));
			p1 = new Phrase(titreAccusationSring.trim(), ToolsForReporting.boldfontLabelAmiri11);
			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, null);

			c1.setBorder(0);

			tableNumAff.addCell(c1);

			String remarque = ToolsForReporting.buildTitreRemarque(affaires.get(j));
			p1 = new Phrase(remarque, ToolsForReporting.boldfontLabelAmiri14);
			c1 = ToolsForReporting.createPdfPCell(p1, 100, Element.ALIGN_CENTER, new BaseColor(245, 245, 245));

			c1.setBorder(0);

			tableTypeAff.addCell(c1);
			c1 = new PdfPCell(tableTypeAff);
			c1.setColspan(100);
			c1.setBorder(0);

			c1.setBorderColor(BaseColor.BLACK);
			tableNumAff.addCell(c1);

			tableTypeAff = new PdfPTable(100);
			tableTypeAff.setWidthPercentage(100);
			c1 = new PdfPCell(tableNumAff);
			c1.setColspan(69);
			tableAffByEnfant.addCell(c1);
			tableNumAff = new PdfPTable(100);
			tableNumAff.setWidthPercentage(100);

			ToolsForReporting2.processTableTypeDocument(p1, c1, tableTypeDocument,
					affaires.get(j));

			if (affaires.size() > 1) {
				c1 = new PdfPCell(tableTypeDocument);
				c1.setBorderWidth(1);
				c1.setBackgroundColor(new BaseColor(240, 240, 240));
				c1.setColspan(28);
				tableAffByEnfant.addCell(c1);

				p1 = new Phrase(ToolsForReporting.buildTitreIndice(j), ToolsForReporting.boldfontLabelAmiri9);
				c1 = new PdfPCell(p1);
				c1.setColspan(3);
				tableAffByEnfant.addCell(c1);

				tableTypeDocument = new PdfPTable(100);
				tableTypeDocument.setWidthPercentage(100);
			} else {
				c1 = new PdfPCell(tableTypeDocument);
				c1.setBorderWidth(1);
				c1.setBackgroundColor(new BaseColor(240, 240, 240));
				c1.setColspan(31);
				tableAffByEnfant.addCell(c1);
			}

		}
		c1 = new PdfPCell(tableAffByEnfant);
		c1.setColspan(60);
		c1.setBorderColor(BaseColor.BLACK);
		c1.setBorderWidth(1);
		tableAffaire.addCell(c1);
	}

	public static void processTablePrencipal(Residence residenceDTO, PdfPTable tableAffaire, String etatJuridiue,
			int i) {

		if (residenceDTO.getArrestation().getEnfant() != null) {

			List<Affaire> affaires = residenceDTO.getArrestation().getAffaires();
			if (affaires != null && affaires.size() != 0) {

				buildAffaireTables(affaires, tableAffaire);

			} else {
				ToolsForReporting.addEmptyAffaireCell(tableAffaire);

			}
			buildIdentiteTables(tableAffaire, residenceDTO, etatJuridiue, i);

		}
	}

	public static Phrase buildEnfantDetailsPhrase(Enfant enfant) {
		return new Phrase(enfant.getNom() + " بن " + enfant.getNomPere() + " بن " + enfant.getNomGrandPere() + " "
				+ enfant.getPrenom(), ToolsForReporting.boldfontFamielle);
	}

	public static Phrase buildEnfantAddressPhrase(Enfant enfant) {
		return new Phrase(
				enfant.getAdresse().toString().trim() + " " + enfant.getDelegation().getLibelle_delegation().toString()
						+
						" " + enfant.getGouvernorat().getLibelle_gouvernorat().toString(),
				ToolsForReporting.boldfontLabelAmiri14);
	}

	public static Phrase buildEnfantBirthDetailsPhrase(Enfant enfant) {
		return new Phrase("بـــــــ" + enfant.getLieuNaissance() + " " + enfant.getDateNaissance(),
				ToolsForReporting.boldfontLabelAmiri14);
	}

	public static Phrase buildEnfantSocialDetailsPhrase(Enfant enfant) {
		if (enfant.getSituationSocial() != null) {
			return new Phrase(enfant.getSituationSocial().getLibelle_situation_social().toString().trim() + " " +
					enfant.getSituationFamiliale().getLibelle_situation_familiale().toString().trim(),
					ToolsForReporting.boldfontLabelAmiri14);
		} else {
			return new Phrase(enfant.getSituationFamiliale().getLibelle_situation_familiale().toString().trim(),
					ToolsForReporting.boldfontLabelAmiri14);
		}
	}

	public static Phrase buildEnfantOccupationDetailsPhrase(Enfant enfant) {
		if (enfant.getMetier() != null) {
			return new Phrase(enfant.getMetier().getLibelle_metier().toString().trim() + " " +
					enfant.getNiveauEducatif().getLibelle_niveau_educatif().toString().trim(),
					ToolsForReporting.boldfontLabelAmiri14);
		} else {
			return new Phrase(" " + enfant.getNiveauEducatif().getLibelle_niveau_educatif().toString().trim(),
					ToolsForReporting.boldfontLabelAmiri14);
		}
	}

	public static Phrase buildEntryMutationPhrase(Residence residenceDTO) {
		return new Phrase(residenceDTO.getDateEntree().toString() + " " + "قدم من" + " " +
				residenceDTO.getEtablissementEntree().getLibelle_etablissement().toString() +
				" (" + residenceDTO.getCauseMutation().getLibelle_causeMutation().toString() +
				") يوم ", ToolsForReporting.boldfontFamielle13);
	}

	public static Phrase buildExitMutationPhrase(Residence residenceDTO) {
		return new Phrase(residenceDTO.getDateSortie().toString() + " " + "نقل  إلى" + " " +
				residenceDTO.getEtablissementSortie().getLibelle_etablissement().toString() +
				" (" +
				residenceDTO.getCauseMutationSortie().getLibelle_causeMutation().toString() +
				") يوم ", ToolsForReporting.boldfontFamielle13);
	}

	private static boolean hasEntryMutationData(Residence residenceDTO) {
		return residenceDTO.getDateEntree() != null && residenceDTO.getEtablissementEntree() != null
				&& residenceDTO.getCauseMutation() != null;
	}

	private static boolean hasExitMutationData(Residence residenceDTO) {
		return residenceDTO.getDateSortie() != null && residenceDTO.getEtablissementSortie() != null
				&& residenceDTO.getCauseMutationSortie() != null;
	}

	public static PdfPCell createPdfCell(Phrase phrase, float padding, int border) {
		PdfPCell cell = new PdfPCell(phrase);
		cell.setPadding(padding);
		cell.setBorder(border);
		cell.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		return cell;
	}

	public static PdfPTable addTitleToPdfTable(String title, Font font, int colSpan, BaseColor backgroundColor,
			int border) {
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		PdfPCell cell = createPdfCell(new Phrase(title, font), 70f, Rectangle.BOX);
		if (backgroundColor != null) {
			cell.setBackgroundColor(backgroundColor);
		}
		cell.setBorder(border);
		cell.setColspan(colSpan);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		table.addCell(cell);
		return table;
	}

	private void addTableCells(PdfPTable table, String label, int valueFemale, int valueMale) {
		ToolsForReporting.addCellToTable(table, label, 40, Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
		ToolsForReporting.addCellToTable(table, String.valueOf(valueFemale), 30, Element.ALIGN_CENTER, null, 30);
		ToolsForReporting.addCellToTable(table, String.valueOf(valueMale), 30, Element.ALIGN_CENTER, null, 30);
	}

	private void addTableHeader(PdfPTable table, String headerText) {
		ToolsForReporting.addCellToTable(table, headerText, 100, Element.ALIGN_CENTER, new BaseColor(210, 210, 210),
				30);
	}

	public static String getArabicMajorityAgeDate(String date) {

		LocalDate d = LocalDate.parse(date, formatter);
		LocalDate c = d.plusYears(18);

		String monthInArabic = getCurrentArabicMonth(c.getMonthValue());

		String day = String.valueOf(c.getDayOfMonth());
		if (c.getDayOfMonth() < 10) {
			day = "0" + day;
		}

		return day.toString() + " " + monthInArabic.toString() + " " + c.getYear() + "";
	}

	public static int calculerAge(String dateNaissance) {
		// Analyser la chaîne de caractères de date de naissance en LocalDate
		LocalDate dob = LocalDate.parse(dateNaissance);

		// Obtenir la date actuelle
		LocalDate curDate = LocalDate.now();

		// Calculer la période entre la date de naissance et la date actuelle
		Period period = Period.between(dob, curDate);

		// Retourner l'âge en années
		return period.getYears();
	}

	public static java.sql.Date getFirstDayOfNextMonth() {
		// Get the current date
		LocalDate currentDate = LocalDate.now();

		// Get the first day of the next month
		LocalDate firstDayOfNextMonth = currentDate.with(TemporalAdjusters.firstDayOfNextMonth());

		// Convert LocalDate to java.sql.Date
		return java.sql.Date.valueOf(firstDayOfNextMonth);
	}

	public static Date getFirstDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		return calendar.getTime();
	}

}

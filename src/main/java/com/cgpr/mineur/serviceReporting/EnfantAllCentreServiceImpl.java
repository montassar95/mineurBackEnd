package com.cgpr.mineur.serviceReporting;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.config.ConfigShaping;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.repository.StatistcsRepository;
import com.cgpr.mineur.repository.VisiteRepository;
import com.cgpr.mineur.tools.StatisticsEnfant;
import com.cgpr.mineur.tools.ToolsForReporting;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class EnfantAllCentreServiceImpl implements EnfantAllCentreService {

	private final ChargeAllEnfantService chargeAllEnfantService;
	private final StatistcsRepository statistcsRepository;
	private final VisiteRepository visiteRepository;

	@Autowired
	public EnfantAllCentreServiceImpl(ChargeAllEnfantService chargeAllEnfantService,
			StatistcsRepository statistcsRepository, VisiteRepository visiteRepository) {
		this.chargeAllEnfantService = chargeAllEnfantService;
		this.visiteRepository = visiteRepository;
		this.statistcsRepository = statistcsRepository;
	}

	 

	ConfigShaping boldConf = new ConfigShaping();

	 
	 
	@Override
	public byte[] exportEtatAllCentre(String dateString) throws DocumentException, IOException, ArabicShapingException {

		LocalDateTime localDate = LocalDateTime.now();
		System.out.println("localDate : " + LocalDate.now());
		int yearsVisite = 0;
		int moisVisite = 0;
		String dateAujourdhui="";
		if (dateString == null) {
			yearsVisite = localDate.getYear();
			moisVisite = localDate.getMonthValue();

		} else {

			LocalDate localDateJson = LocalDate.parse(dateString);
			yearsVisite = localDateJson.getYear();
			moisVisite = localDateJson.getMonthValue();
			
			// Créer un formateur de date avec le format spécifié
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        
	        // Obtenir la date actuelle
	        LocalDate aujourdhui = LocalDate.now();
	        
	        // Formater la date au format spécifié
	          dateAujourdhui = aujourdhui.format(formatter);

		}

		String titreString = " ";
		List<List<Residence>> enfantAffiches = new ArrayList<List<Residence>>();
		if (dateString == null || dateString.equals(dateAujourdhui)) {

			enfantAffiches = chargeAllEnfantService.chargeList();

		} else {
 
			LocalDate localDateJson = LocalDate.parse(dateString);

			enfantAffiches = chargeAllEnfantService.chargeListByDate(localDateJson);

		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 25f, 0f);

		PdfWriter.getInstance(document, out);
		document.open();

 		PdfPTable tableTop = new PdfPTable(3);  
		String gouvernorat = "تونس".toString();
        
		String date  =  LocalTime.now().getMinute() + ":" + LocalTime.now().getHour()+ " " +ToolsForReporting.dtf.format(LocalDate.now());
		
		if (dateString != null) {
			date = dateString;
		}

		try {
			tableTop = ToolsForReporting.createTopTable(gouvernorat, date);
		} catch (Exception e1) {

			e1.printStackTrace();
		}

		 	
		titreString= "الأطفال المودعين بمراكز إصلاح الأطفال الجانحين و بالوحدات السجنية خلال شهر "
 				+ ToolsForReporting.getCurrentArabicMonth(moisVisite) + " " + localDate.getYear();
			    PdfPTable tTitre = ToolsForReporting.createTitleTable(titreString);
 	
		
		tTitre.setSpacingBefore(50f);
		
 
		PdfPTable tTitreStatiqueGenerale=ToolsForReporting.addTitleToPdfTable(  "  الإحـصــــــــــائية العــــــــــــامة  ",
		        ToolsForReporting.boldfontTitleStatique, 1, null,0);
 
 		PdfPTable tableStatiqueGenerale = ToolsForReporting.createPdfTable(100, 80);
 
		StatisticsEnfant statisticsEnfant = StatisticsEnfant.calculerTotaux(enfantAffiches, chargeAllEnfantService,
				statistcsRepository);

		if (enfantAffiches.size() > 0) {
			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					"إحـصـــائية الأطفال المودعين بمراكز إصلاح الأطفال الجانحين  ", 100, Element.ALIGN_CENTER,
					new BaseColor(210, 210, 210), 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "إنــاث", 30, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "ذكـور ", 30, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "الأصنـاف ", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);

			// Utiliser les getters pour accéder aux valeurs
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, statisticsEnfant.getTotalJugeFeminin() + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, statisticsEnfant.getTotalJugeMasculin() + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "المحكومين ", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);

			ToolsForReporting.addCellToTable(tableStatiqueGenerale, statisticsEnfant.getTotalArretFeminin() + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, statisticsEnfant.getTotalArretMasculin() + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "الموقوفين ", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);

			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					statisticsEnfant.getTotalArretFeminin() + statisticsEnfant.getTotalJugeFeminin() + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					statisticsEnfant.getTotalArretMasculin() + statisticsEnfant.getTotalJugeMasculin() + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "المجموع", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);

			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					"  إرهاب  " + statisticsEnfant.getTotalTeroristFeminin(), 15, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					"أجنبي " + statisticsEnfant.getTotalEtrangerFeminin(), 15, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					"  إرهاب  " + statisticsEnfant.getTotalTeroristMasculin(), 15, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					statisticsEnfant.getTotalEtrangerMasculin() + "أجنبي", 15, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "من بينهم", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);

			//----------------------------------------------------

			PdfPTable tTitreStatique = new PdfPTable(1);

			Phrase pTitreStatique = new Phrase(boldConf.format("  الإحـصــــــــــائية    "),
					ToolsForReporting.boldfontTitleStatique);
			PdfPCell cTitreStatique = new PdfPCell(pTitreStatique);
			cTitreStatique.setPadding(60f);
			cTitreStatique.setBorder(Rectangle.BOX);

			cTitreStatique.setBorderColor(BaseColor.WHITE);
			cTitreStatique.setHorizontalAlignment(Element.ALIGN_CENTER);

			tTitreStatique.setSpacingBefore(100f);
			tTitreStatique.addCell(cTitreStatique);

			PdfPTable tableStatique = new PdfPTable(100);
			tableStatique.setWidthPercentage(80);

			// Ajout des cellules pour "عدد الأطفال"
			ToolsForReporting.addCellToTable(tableStatique, "عدد الأطفال", 50, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, "المراكز الإصلاحية", 50, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, statisticsEnfant.getTotalMourouj() + "", 50,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, "المروج", 50, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, statisticsEnfant.getTotalSidiHani() + "", 50,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, "سيدي الهاني", 50, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, statisticsEnfant.getTotalSoukJdid() + "", 50,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, "سوق الجديد", 50, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, statisticsEnfant.getTotalMghira() + "", 50,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, "المغيرة", 50, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, statisticsEnfant.getTotalMjaz() + "", 50,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, "مجاز الباب", 50, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, statisticsEnfant.getTotalCentre() + "", 50,
					Element.ALIGN_CENTER, new BaseColor(210, 210, 210), 30);
			ToolsForReporting.addCellToTable(tableStatique, "المجموع", 50, Element.ALIGN_CENTER,
					new BaseColor(210, 210, 210), 30);

			
			
			
			
			
			
			
			
			PdfPTable tTitreStatiqueMourouj = new PdfPTable(1);

			Phrase pTitreStatiqueMourouj = new Phrase("  مركز إصلاح الأطفال الجانحين المروج     ",
					ToolsForReporting.boldfontTitleStatique);

			PdfPCell cTitreStatiqueMourouj = new PdfPCell(pTitreStatiqueMourouj);
			cTitreStatiqueMourouj.setPadding(60f);
			cTitreStatiqueMourouj.setBorder(Rectangle.BOX);

			cTitreStatiqueMourouj.setBorderColor(BaseColor.WHITE);
			cTitreStatiqueMourouj.setHorizontalAlignment(Element.ALIGN_CENTER);

			cTitreStatiqueMourouj.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

			tTitreStatiqueMourouj.setSpacingBefore(100f);
			tTitreStatiqueMourouj.addCell(cTitreStatiqueMourouj);

			PdfPTable tableStatiqueMourouj = new PdfPTable(100);
			tableStatiqueMourouj.setWidthPercentage(80);

			PdfPTable tableStatiqueLiberable = new PdfPTable(100);
			tableStatiqueLiberable.setWidthPercentage(80);
			int stat = 0;
			if (enfantAffiches.size() > 0) {

				// Adding cells for tableStatiqueMourouj
				ToolsForReporting.addCellToTable(tableStatiqueMourouj, enfantAffiches.get(stat).size() + "", 50,
						Element.ALIGN_CENTER, null, 30);
				ToolsForReporting.addCellToTable(tableStatiqueMourouj, "محكومين", 50, Element.ALIGN_CENTER,
						new BaseColor(230, 230, 230), 30);
				ToolsForReporting.addCellToTable(tableStatiqueMourouj, enfantAffiches.get(stat + 1).size() + "", 50,
						Element.ALIGN_CENTER, null, 30);
				ToolsForReporting.addCellToTable(tableStatiqueMourouj, "الموقوفين", 50, Element.ALIGN_CENTER,
						new BaseColor(230, 230, 230), 30);
				ToolsForReporting.addCellToTable(tableStatiqueMourouj,
						enfantAffiches.get(stat).size() + enfantAffiches.get(stat + 1).size() + "", 50,
						Element.ALIGN_CENTER, new BaseColor(210, 210, 210), 30);
				ToolsForReporting.addCellToTable(tableStatiqueMourouj, "المجموع", 50, Element.ALIGN_CENTER,
						new BaseColor(210, 210, 210), 30);

				// Adding cells for tableStatiqueLiberable
				ToolsForReporting.addCellToTable(tableStatiqueLiberable, enfantAffiches.get(stat + 2).size() + "", 50,
						Element.ALIGN_CENTER, null, 30);
				ToolsForReporting.addCellToTable(tableStatiqueLiberable, "السراحات", 50, Element.ALIGN_CENTER,
						new BaseColor(230, 230, 230), 30);

				tableStatiqueLiberable.setSpacingBefore(30f);

				stat = stat + 3;

			}
			// ----------------------------------------------------------------------------------------------------------------

			document.add(tableTop);
			document.add(tTitre);
			document.newPage();
			document.add(tTitreStatiqueGenerale);
			document.add(tableStatiqueGenerale);
			document.newPage();
			document.add(tTitreStatique);
			document.add(tableStatique);
			document.newPage();

			String idEtab = enfantAffiches.get(0).get(0).getEtablissement().getId().toString();
			document.add(tTitreStatiqueMourouj);
			document.add(tableStatiqueMourouj);
			document.add(tableStatiqueLiberable);
			document.newPage();
			System.out.println(enfantAffiches.size() + " la plus grand ");
			for (List<Residence> enfantAfficheCentre : enfantAffiches) {

				PdfPTable tableAffaire = new PdfPTable(100);
				tableAffaire.setWidthPercentage(100);

				ToolsForReporting.addCellToHeaderTable(tableAffaire, "القضايا", 41);
				ToolsForReporting.addCellToHeaderTable(tableAffaire, "تــــــاريخ", 19);
				ToolsForReporting.addCellToHeaderTable(tableAffaire, "الهوية", 28);
				ToolsForReporting.addCellToHeaderTable(tableAffaire, "ع.لإيقاف", 9);
				ToolsForReporting.addCellToHeaderTable(tableAffaire, "ع.ر", 3);

			 
				for (int i = 0; i < enfantAfficheCentre.size(); i++) {

					ToolsForReporting.processTablePrencipal(enfantAfficheCentre.get(i), tableAffaire, null, i);

				}
				document.newPage();

				if (enfantAfficheCentre.size() != 0) {

					if (enfantAfficheCentre.get(0) != null) {
						if (!enfantAfficheCentre.get(0).getEtablissement().getId().toString().equals(idEtab)) {
							// Création du titre statique Mourouj
							tTitreStatiqueMourouj = new PdfPTable(1);
							pTitreStatiqueMourouj = new Phrase(
									"  مركز إصلاح الأطفال الجانحين       "
											+ enfantAfficheCentre.get(0).getEtablissement().getLibelle_etablissement(),
									ToolsForReporting.boldfontTitleStatique);
							cTitreStatiqueMourouj = new PdfPCell(pTitreStatiqueMourouj);
							cTitreStatiqueMourouj.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
							cTitreStatiqueMourouj.setPadding(60f);
							cTitreStatiqueMourouj.setBorder(Rectangle.BOX);
							cTitreStatiqueMourouj.setBorderColor(BaseColor.WHITE);
							cTitreStatiqueMourouj.setHorizontalAlignment(Element.ALIGN_CENTER);
							tTitreStatiqueMourouj.setSpacingBefore(100f);
							tTitreStatiqueMourouj.addCell(cTitreStatiqueMourouj);

							// Création des tables statiques Mourouj et Liberables
							tableStatiqueMourouj = new PdfPTable(100);
							tableStatiqueMourouj.setWidthPercentage(80);
							tableStatiqueLiberable = new PdfPTable(100);
							tableStatiqueLiberable.setWidthPercentage(80);

							// For tableStatiqueMourouj
							ToolsForReporting.addCellToTable(tableStatiqueMourouj, enfantAffiches.get(stat).size() + "",
									50, Element.ALIGN_CENTER, null, 30);
							ToolsForReporting.addCellToTable(tableStatiqueMourouj, "محكومين", 50, Element.ALIGN_CENTER,
									new BaseColor(230, 230, 230), 30);
							ToolsForReporting.addCellToTable(tableStatiqueMourouj,
									enfantAffiches.get(stat + 1).size() + "", 50, Element.ALIGN_CENTER, null, 30);
							ToolsForReporting.addCellToTable(tableStatiqueMourouj, "الموقوفين", 50,
									Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
							ToolsForReporting.addCellToTable(tableStatiqueMourouj,
									enfantAffiches.get(stat).size() + enfantAffiches.get(stat + 1).size() + "", 50,
									Element.ALIGN_CENTER, new BaseColor(210, 210, 210), 30);
							ToolsForReporting.addCellToTable(tableStatiqueMourouj, "المجموع", 50, Element.ALIGN_CENTER,
									new BaseColor(210, 210, 210), 30);

							// For tableStatiqueLiberable
							ToolsForReporting.addCellToTable(tableStatiqueLiberable,
									enfantAffiches.get(stat + 2).size() + "", 50, Element.ALIGN_CENTER, null, 30);
							ToolsForReporting.addCellToTable(tableStatiqueLiberable, "السراحات", 50,
									Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);

							tableStatiqueLiberable.setSpacingBefore(30f);

							stat = stat + 3;

							// Ajout des tables au document
							document.add(tTitreStatiqueMourouj);
							document.add(tableStatiqueMourouj);
							document.add(tableStatiqueLiberable);
							document.newPage();

							idEtab = enfantAfficheCentre.get(0).getEtablissement().getId().toString();
						}

						switch (enfantAfficheCentre.get(0).getArrestation().getSituationJudiciaire()) {
						case "juge":
							titreString = "المحكومين بمركز";
							break;
						case "arret":
							titreString = "الموقوفين بمركز";
							break;
						case "libre":
							titreString = "المغادرون من مركز ";
							break;
						default:
							titreString = "ا --- ";
							break;
						}

						Phrase titreCentre = new Phrase(
								boldConf.format(
										"قائمة إسمية للأطفال " + titreString
												+ enfantAfficheCentre.get(0).getEtablissement()
														.getLibelle_etablissement().trim()),
								ToolsForReporting.boldfontTitle);

						Paragraph paragraph = new Paragraph();
						paragraph.setAlignment(Element.ALIGN_CENTER);
						paragraph.add(titreCentre);
						paragraph.setSpacingBefore(10f);
						paragraph.setSpacingAfter(10f);
						document.add(paragraph);

					}
				}
				document.add(tableAffaire);
				document.newPage();
			}
			document.close();
		}
		return out.toByteArray();

	}
 
	
}

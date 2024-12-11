package com.cgpr.mineur.serviceReporting.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.config.ConfigShaping;
import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.Visite;
import com.cgpr.mineur.repository.RapportEnfantQuotidienRepository;
import com.cgpr.mineur.repository.StatistcsRepository;
import com.cgpr.mineur.repository.VisiteRepository;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.service.RapportEnfantQuotidienService;
import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;
import com.cgpr.mineur.serviceReporting.GenererStatistiquePdfMensuelService;
import com.cgpr.mineur.tools.StatisticsEnfant;
import com.cgpr.mineur.tools.ToolsForReporting;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class GenererStatistiquPdfMensuelImpl implements GenererStatistiquePdfMensuelService {

	private final ChargeAllEnfantService chargeAllEnfantService;
	private final StatistcsRepository statistcsRepository;
	private final VisiteRepository visiteRepository;
	private final RapportEnfantQuotidienRepository rapportEnfantQuotidienRepository;
	private final RapportEnfantQuotidienService rapportEnfantQuotidienService;
	 
	@Autowired
	public GenererStatistiquPdfMensuelImpl(ChargeAllEnfantService chargeAllEnfantService,
			StatistcsRepository statistcsRepository, VisiteRepository visiteRepository ,
			 RapportEnfantQuotidienRepository rapportEnfantQuotidienRepository,RapportEnfantQuotidienService rapportEnfantQuotidienService) {
		
					this.chargeAllEnfantService = chargeAllEnfantService;
					this.visiteRepository = visiteRepository;
					this.statistcsRepository = statistcsRepository;
					this.rapportEnfantQuotidienRepository=rapportEnfantQuotidienRepository;
					this.rapportEnfantQuotidienService =rapportEnfantQuotidienService;
	 
	}

	 

	ConfigShaping boldConf = new ConfigShaping();

	 
	 
	@Override
	public ByteArrayInputStream genererStatistiquePdfMensuel(PDFListExistDTO pDFListExistDTO) throws DocumentException, IOException, ArabicShapingException {
       
		
		
		
		System.out.println(pDFListExistDTO.toString());
		LocalDateTime localDate = LocalDateTime.now();
		System.out.println("localDate : " + LocalDate.now());
		int yearsVisite = 0;
		int moisVisite = 0;
		String dateAujourdhui="";
		String dateString = pDFListExistDTO.getDatePrintAllCentre();
		 System.out.println(dateString);
		List<StatistiqueEtablissementDTO> statEta =null;
		if (dateString == null) {
			yearsVisite = localDate.getYear();
			moisVisite = localDate.getMonthValue();

		} else {

			LocalDate localDateJson = LocalDate.parse(dateString);
		  statEta = rapportEnfantQuotidienService.getStatistiquesParDate(dateString);
		  
			yearsVisite = localDateJson.getYear();
			moisVisite = localDateJson.getMonthValue();
			
			// Créer un formateur de date avec le format spécifié
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        
	        // Obtenir la date actuelle
	        LocalDate aujourdhui = LocalDate.now();
	        
	        // Formater la date au format spécifié
	          dateAujourdhui = aujourdhui.format(formatter);

		}

 		List<String> etablissementIds = Arrays.asList("11","12","13","14","16"  );
//		List<String> statutPenals = Arrays.asList("arrete", "juge", "libre");
//
//		LocalDate date1 = LocalDate.now(); // ou une autre date souhaitée
//
//		List<List<Residence>> result = 
//				chargeAllEnfantService.getResidencesGroupedByEtablissementAndStatutPenal(date1, etablissementIds, statutPenals); 
		
		String titreString = " ";
//		List<List<Residence>> enfantAffiches = new ArrayList<List<Residence>>();
//		|| dateString.equals(dateAujourdhui)
//		if (dateString == null ) {
//
//			enfantAffiches = chargeAllEnfantService.chargeList();
//
//		} else {
// System.out.println("cette methode ");
//			LocalDate localDateJson = LocalDate.parse(dateString);
//
//			enfantAffiches = chargeAllEnfantService.chargeListByDate(localDateJson);
 
	 
	List<String> statutPenals = Arrays.asList("juge", "arret", "libre");
	LocalDate date1 = LocalDate.parse(pDFListExistDTO.getDatePrintAllCentre());
//	LocalDate date1 = LocalDate.now(); // ou une autre date souhaitée

//	 enfantAffiches = 
//			chargeAllEnfantService.getResidencesGroupedByEtablissementAndStatutPenal(date1, etablissementIds, statutPenals); 
//	System.err.println("totale " + enfantAffiches.get(0).size());
////		}

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

		 	
		titreString= "    حوصلـــة حولـــة مـــراكز  إصــــلاح الأطفــــال الجــــانحين "+ " خلال شهر "
 				+ ToolsForReporting.getCurrentArabicMonth(moisVisite) + " " + localDate.getYear();
			    PdfPTable tTitre = ToolsForReporting.createTitleTable(titreString);
 	
		
		tTitre.setSpacingBefore(50f);
		
 
		PdfPTable tTitreStatiqueGenerale=ToolsForReporting.addTitleToPdfTable(  "  الإحـصــــــــــائية العــــــــــــامة  ",
		        ToolsForReporting.boldfontTitleStatique, 1, null,0);
 
 		PdfPTable tableStatiqueGenerale = ToolsForReporting.createPdfTable(100, 80);
 
		 

		  {
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
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, statEta.get(2).getNbrStatutPenalJuge() + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale,(statEta.get(0).getNbrStatutPenalJuge()+
					statEta.get(1).getNbrStatutPenalJuge()+
					statEta.get(3).getNbrStatutPenalJuge()+
					statEta.get(4).getNbrStatutPenalJuge()) + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "المحكومين ", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);

			ToolsForReporting.addCellToTable(tableStatiqueGenerale, statEta.get(2).getNbrStatutPenalArrete() + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, (statEta.get(0).getNbrStatutPenalArrete()+
					statEta.get(1).getNbrStatutPenalArrete()+
					statEta.get(3).getNbrStatutPenalArrete()+
					statEta.get(4).getNbrStatutPenalArrete()) + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "الموقوفين ", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);

			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					statEta.get(2).getNbrStatutPenalArrete() + statEta.get(2).getNbrStatutPenalJuge() + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					(statEta.get(0).getNbrStatutPenalArrete()+
							statEta.get(1).getNbrStatutPenalArrete()+
							statEta.get(3).getNbrStatutPenalArrete()+
							statEta.get(4).getNbrStatutPenalArrete()+
							statEta.get(0).getNbrStatutPenalJuge()+
							statEta.get(1).getNbrStatutPenalJuge()+
							statEta.get(3).getNbrStatutPenalJuge()+
							statEta.get(4).getNbrStatutPenalJuge()) + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "المجموع", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);

//			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
//					"  إرهاب  " + statisticsEnfant.getTotalTeroristFeminin(), 15, Element.ALIGN_CENTER, null, 30);
//			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
//					"أجنبي " + statisticsEnfant.getTotalEtrangerFeminin(), 15, Element.ALIGN_CENTER, null, 30);
//			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
//					"  إرهاب  " + statisticsEnfant.getTotalTeroristMasculin(), 15, Element.ALIGN_CENTER, null, 30);
//			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
//					statisticsEnfant.getTotalEtrangerMasculin() + "أجنبي", 15, Element.ALIGN_CENTER, null, 30);
//			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "من بينهم", 40, Element.ALIGN_CENTER,
//					new BaseColor(230, 230, 230), 30);
			
			
			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					"  إرهاب  " +statEta.get(2).getNbrTypeAffaireId5F(), 15, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					"أجنبي "+statEta.get(2).getNbrNationaliteDifferentDeJuge1F(), 15, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					"  إرهاب  "+ (statEta.get(0).getNbrTypeAffaireId5M()+
					statEta.get(1).getNbrTypeAffaireId5M()+
					statEta.get(3).getNbrTypeAffaireId5M()+
					statEta.get(4).getNbrTypeAffaireId5M()) , 15, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale,
					  "أجنبي " + (statEta.get(0).getNbrNationaliteDifferentDeJuge1M()+
								statEta.get(1).getNbrNationaliteDifferentDeJuge1M()+
								statEta.get(3).getNbrNationaliteDifferentDeJuge1M()+
								statEta.get(4).getNbrNationaliteDifferentDeJuge1M()) , 15, Element.ALIGN_CENTER, null, 30);
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
			ToolsForReporting.addCellToTable(tableStatique,statEta.get(4).getNbrStatutPenalArrete()+statEta.get(4).getNbrStatutPenalJuge() + "", 50,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, "المروج", 50, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, statEta.get(0).getNbrStatutPenalArrete()+statEta.get(0).getNbrStatutPenalJuge() + "", 50,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, "سيدي الهاني", 50, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique,  statEta.get(1).getNbrStatutPenalArrete()+statEta.get(1).getNbrStatutPenalJuge() + "", 50,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, "سوق الجديد", 50, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique,  statEta.get(2).getNbrStatutPenalArrete()+statEta.get(2).getNbrStatutPenalJuge() + "", 50,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, "المغيرة", 50, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique,statEta.get(3).getNbrStatutPenalArrete()+statEta.get(3).getNbrStatutPenalJuge() + "", 50,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, "مجاز الباب", 50, Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatique, (statEta.get(0).getNbrStatutPenalArrete()+
					statEta.get(1).getNbrStatutPenalArrete()+statEta.get(2).getNbrStatutPenalArrete()+
					statEta.get(3).getNbrStatutPenalArrete()+statEta.get(4).getNbrStatutPenalArrete()+
					statEta.get(0).getNbrStatutPenalJuge()+
					statEta.get(1).getNbrStatutPenalJuge()+statEta.get(2).getNbrStatutPenalJuge()+
					statEta.get(3).getNbrStatutPenalJuge()+statEta.get(4).getNbrStatutPenalJuge())+ "", 50,
					Element.ALIGN_CENTER, new BaseColor(210, 210, 210), 30);
			ToolsForReporting.addCellToTable(tableStatique, "المجموع", 50, Element.ALIGN_CENTER,
					new BaseColor(210, 210, 210), 30);

			
			
			
			
			
			
			
			
			
			
			
			  
		     
		     
			 
			 
			
			
			
			 
		 
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
			
//			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//            dataset.addValue(statisticsEnfant.getTotalMourouj(), "المروج", "المروج");
//            dataset.addValue(statisticsEnfant.getTotalSidiHani(), "سيدي الهاني", "سيدي الهاني");
//         
//            dataset.addValue(statisticsEnfant.getTotalSoukJdid(), "سوق الجديد",  "سوق الجديد");
//            dataset.addValue(statisticsEnfant.getTotalMjaz(), "مجاز الباب", "مجاز الباب");
//            dataset.addValue(statisticsEnfant.getTotalMghira(), "المغيرة",  "المغيرة");
            

//            // Création de l'histogramme
//            JFreeChart chart = ChartFactory.createBarChart(
//                    "الإحـصــــــــــائية", // Titre du graphique
//                    "مراكز الإصلاح", // Axe X
//                    "عدد الأطفال ", // Axe Y
//                    dataset // Données
//            );
//
//            // Personnalisation du graphique
//            chart.setBackgroundPaint(Color.white);
//
//            // Conversion du graphique en image
//            java.awt.Image awtImage = chart.createBufferedImage(1000, 300);
//
//            // Ajout de l'image dans le document PDF
//            com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(awtImage, null);
//            pdfImage.setAlignment(Element.ALIGN_CENTER);
//          
//            PdfPTable tableBar = new PdfPTable(1);
//            PdfPCell cellBar = new PdfPCell(pdfImage, true);
//             
//            
//            cellBar.setBorder(0);
//            tableBar.addCell(cellBar);
//           
//           
//            document.add(tableBar);
            
            
            
//            PdfPTable table1 = new PdfPTable(2);
//            table1.setWidthPercentage(100);
//            PdfPCell cell = new PdfPCell(pdfImage, true);
//            cell.setBorder(0);
//            table1.addCell(cell);   
	            
 
            
//            // Create a pie dataset 1
//            DefaultPieDataset datasetPie1 = new DefaultPieDataset();
//            datasetPie1.setValue("محكومين",  statisticsEnfant.getTotalJugeMasculin() +statisticsEnfant.getTotalJugeFeminin());
//            datasetPie1.setValue("الموقوفين", statisticsEnfant.getTotalArretMasculin() +statisticsEnfant.getTotalArretFeminin());
//
//            // Create the chart for dataset 1
//            JFreeChart chartPie1 = ChartFactory.createPieChart(" ", datasetPie1, true, true, false);
//            java.awt.Image chartImage1 = chartPie1.createBufferedImage(300, 200);
//            com.itextpdf.text.Image pdfImagePie1 = com.itextpdf.text.Image.getInstance(chartImage1, null);
//
//            // Create a pie dataset 2
//            DefaultPieDataset datasetPie2 = new DefaultPieDataset();
//            datasetPie2.setValue("إنــاث", statisticsEnfant.getTotalJugeFeminin() +statisticsEnfant.getTotalArretFeminin());
//            datasetPie2.setValue("ذكـور", statisticsEnfant.getTotalJugeMasculin() +statisticsEnfant.getTotalArretMasculin());
//
//            // Create the chart for dataset 2
//            JFreeChart chartPie2 = ChartFactory.createPieChart(" ", datasetPie2, true, true, false);
//            java.awt.Image chartImage2 = chartPie2.createBufferedImage(300, 200);
//            com.itextpdf.text.Image pdfImagePie2 = com.itextpdf.text.Image.getInstance(chartImage2, null);
//
//            // Add images to table
//            PdfPTable table2 = new PdfPTable(2);
//            table1.setWidthPercentage(100);
//
//            PdfPCell cell1 = new PdfPCell(pdfImagePie1, true);
//            PdfPCell cell2 = new PdfPCell(pdfImagePie2, true);
//            cell1.setBorder(0);
//            cell2.setBorder(0);
//            table2.addCell(cell1);
//            table2.addCell(cell2);
//          
//            document.add(table2);
//            document.add(table1);
//            document.newPage();
			  
//            System.out.println(enfantAffiches.size() + "  etat "); 
//          
//        	 // Utilisation d'une boucle for pour parcourir la liste et afficher la taille de chaque sous-liste
//            for (int i = 0; i < enfantAffiches.size(); i++) {
//                List<Residence> subList = enfantAffiches.get(i);
//                System.out.println("Taille de la sous-liste castt  " + i + ": " + subList.size());
//            }
//
//			String idEtab = enfantAffiches
//					.get(0)
//					.get(0)
//					.getEtablissement().
//					getId().toString();
			 
			document.newPage();
		 
			
		///	enfantAffiches	= enfantAffiches.subList(0, Math.min(3, enfantAffiches.size()));
			
		 
			document.close();
		}
		return new ByteArrayInputStream(out.toByteArray());

	}
 
	private void updateVisiteCount(Residence residence) {
	    Optional<Visite> visiteOpt = visiteRepository.findbyEnfantandDate(residence.getResidenceId().getIdEnfant(), 2024, 9);
	    String nbVisite = visiteOpt.map(visite -> String.valueOf(visite.getNbrVisite())).orElse("0.");
	    residence.setNbVisite(nbVisite);
	}
}

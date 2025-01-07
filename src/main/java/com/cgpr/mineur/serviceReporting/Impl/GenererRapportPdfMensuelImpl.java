package com.cgpr.mineur.serviceReporting.Impl;

import java.awt.Color;
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
import java.util.stream.IntStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.config.ConfigShaping;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.Visite;
import com.cgpr.mineur.repository.EnfantRepository;
import com.cgpr.mineur.repository.RapportEnfantQuotidienRepository;
import com.cgpr.mineur.repository.StatistcsRepository;
import com.cgpr.mineur.repository.VisiteRepository;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;
import com.cgpr.mineur.serviceReporting.GenererRapportPdfMensuelService;
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
public class GenererRapportPdfMensuelImpl implements GenererRapportPdfMensuelService {

	private final ChargeAllEnfantService chargeAllEnfantService;
	private final StatistcsRepository statistcsRepository;
	private final VisiteRepository visiteRepository;
	private final RapportEnfantQuotidienRepository rapportEnfantQuotidienRepository;
	private final EnfantRepository enfantRepository;
	 
	@Autowired
	public GenererRapportPdfMensuelImpl(ChargeAllEnfantService chargeAllEnfantService,
			StatistcsRepository statistcsRepository, VisiteRepository visiteRepository ,
			 RapportEnfantQuotidienRepository rapportEnfantQuotidienRepository, EnfantRepository enfantRepository) {
		
					this.chargeAllEnfantService = chargeAllEnfantService;
					this.visiteRepository = visiteRepository;
					this.statistcsRepository = statistcsRepository;
					this.rapportEnfantQuotidienRepository=rapportEnfantQuotidienRepository;
					this.enfantRepository = enfantRepository;
	 
	}

	 

	ConfigShaping boldConf = new ConfigShaping();

	 
	 
	@Override
	public ByteArrayInputStream genererRapportPdfMensuel(PDFListExistDTO pDFListExistDTO) throws DocumentException, IOException, ArabicShapingException {
       
		System.out.println(pDFListExistDTO.toString());
		LocalDateTime localDate = LocalDateTime.now();
		System.out.println("localDate : " + LocalDate.now());
		int yearsVisite = 0;
		int moisVisite = 0;
		String dateAujourdhui="";
		String dateString = pDFListExistDTO.getDatePrintAllCentre();
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
 
 
	List<String> etablissementIds = new ArrayList<String>();
	  for (Etablissement etablissement : pDFListExistDTO.getEtablissements()) {
          etablissementIds.add(etablissement.getId());
      }
	List<String> statutPenals = Arrays.asList("juge", "arret", "libre");
	System.out.println(pDFListExistDTO.getDatePrintAllCentre());
	LocalDate date1 = LocalDate.parse(pDFListExistDTO.getDatePrintAllCentre());
//	LocalDate date1 = LocalDate.now(); // ou une autre date souhaitée

	 enfantAffiches = 
			chargeAllEnfantService.getResidencesGroupedByEtablissementAndStatutPenal(date1, etablissementIds, statutPenals); 
	System.err.println("totale " + enfantAffiches.get(0).size());
//		}

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

		 	
		titreString= " تقـــريـــر خاصــــة  بمركز إصــــلاح الأطفــــال الجــــانحين "+pDFListExistDTO.getEtablissements().get(0).getLibelle_etablissement()+" خلال شهر "
 				+ ToolsForReporting.getCurrentArabicMonth(moisVisite) + " " + 2024; //localDate.getYear()
			    PdfPTable tTitre = ToolsForReporting.createTitleTable(titreString);
 	
		
		tTitre.setSpacingBefore(50f);
		
 
 

		if (enfantAffiches.size() > 0) {
 

			PdfPTable tTitreStatique = new PdfPTable(1);
 

			
			
			
			
			
			
			
			
			
			
			
			  
		     
		     
			 
			 
			
			
			
			 
			
			PdfPTable tTitreStatiqueMourouj = new PdfPTable(1);

			Phrase pTitreStatiqueMourouj = new Phrase( "  إحصــــائية خاصــــة  بمركز إصــــلاح الأطفــــال الجــــانحين "+pDFListExistDTO.getEtablissements().get(0).getLibelle_etablissement(),
					ToolsForReporting.boldfontTitleStatique);

			PdfPCell cTitreStatiqueMourouj = new PdfPCell(pTitreStatiqueMourouj);
			cTitreStatiqueMourouj.setPadding(80f);
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
			
 
			  
            System.out.println(enfantAffiches.size() + "  etat "); 
          
        	 // Utilisation d'une boucle for pour parcourir la liste et afficher la taille de chaque sous-liste
            for (int i = 0; i < enfantAffiches.size(); i++) {
                List<Residence> subList = enfantAffiches.get(i);
                System.out.println("Taille de la sous-liste castt  " + i + ": " + subList.size());
            }

			String idEtab = enfantAffiches
					.get(0)
					.get(0)
					.getEtablissement().
					getId().toString();
			document.add(tTitreStatiqueMourouj);
			document.add(tableStatiqueMourouj);
			document.add(tableStatiqueLiberable);
			document.newPage();
		 
			
		///	enfantAffiches	= enfantAffiches.subList(0, Math.min(3, enfantAffiches.size()));
			
			for (List<Residence> enfantAfficheCentre : enfantAffiches) {

				PdfPTable tableAffaire = new PdfPTable(100);
				tableAffaire.setWidthPercentage(100);

				ToolsForReporting.addCellToHeaderTable(tableAffaire, "القضايا", 41);
				ToolsForReporting.addCellToHeaderTable(tableAffaire, "تــــــاريخ", 19);
				ToolsForReporting.addCellToHeaderTable(tableAffaire, "الهوية", 28);
				ToolsForReporting.addCellToHeaderTable(tableAffaire, "ع.لإيقاف", 9);
				ToolsForReporting.addCellToHeaderTable(tableAffaire, "ع.ر", 3);
				
 
				
				
				IntStream.range(0, enfantAfficheCentre.size()).forEach(i -> {
				    Residence residence = enfantAfficheCentre.get(i);
				    updateVisiteCount(residence);

				    if (residence != null && 
				    	    residence.getArrestation() != null && 
				    	    residence.getArrestation().getAffaires() != null && 
				    	    residence.getArrestation().getAffaires().isEmpty()) {
				    	    System.err.println("L'enfant avec un problème : " + residence.toString());
				    	}

				    ToolsForReporting.processTablePrencipal(residence, tableAffaire, null, i);
				});
				
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

						
						
						if(enfantAfficheCentre.get(0).getArrestation().getSituationJudiciaire()!=null) {
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
						}
						else {
							titreString = "اvide--";
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
		return new ByteArrayInputStream(out.toByteArray());

	}
 
	private void updateVisiteCount(Residence residence) {
		System.out.println(residence.getResidenceId().getIdEnfant());
	    Optional<Visite> visiteOpt = visiteRepository.findbyEnfantandDate(residence.getResidenceId().getIdEnfant(), 2024, 12);
	    String nbVisite = visiteOpt.map(visite -> String.valueOf(visite.getNbrVisite())).orElse("0.");
	    residence.setNbVisite(nbVisite);
	}
}

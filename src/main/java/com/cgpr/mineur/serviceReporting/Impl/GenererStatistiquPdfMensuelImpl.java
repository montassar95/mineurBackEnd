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
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cgpr.mineur.config.ConfigShaping;
import com.cgpr.mineur.dto.StatTypeTribunalEtalissementDTO;
import com.cgpr.mineur.dto.StatTypeTribunalPeriodeDTO;
import com.cgpr.mineur.dto.StatTypeTribunalPeriodeResidenceDTO;
import com.cgpr.mineur.dto.StatTypeTribunalPeriodeResidenceGenericDTO;
import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;
import com.cgpr.mineur.dto.StatistiqueGlobaleDTO;
import com.cgpr.mineur.dto.StatistiqueGouvernoratDTO;
import com.cgpr.mineur.dto.StatistiqueNationaliteDTO;
import com.cgpr.mineur.dto.StatistiqueTerrorismeDTO;
import com.cgpr.mineur.dto.StatistiqueTribunalDTO;
import com.cgpr.mineur.dto.StatistiqueTypeAffaireDTO;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.Visite;
//import com.cgpr.mineur.repository.RapportEnfantQuotidienRepository;
import com.cgpr.mineur.repository.StatistcsRepository;
import com.cgpr.mineur.repository.VisiteRepository;
import com.cgpr.mineur.repository.rapport.StatTypeTribunalEtablissementRepository;
import com.cgpr.mineur.repository.rapport.StatTypeTribunalPeriodeRepository;
import com.cgpr.mineur.repository.rapport.StatTypeTribunalPeriodeResidenceGenericRepository;
import com.cgpr.mineur.repository.rapport.StatTypeTribunalPeriodeResidenceRepository;
import com.cgpr.mineur.repository.rapport.StatisticsGlobaleRepositoryCustom;
import com.cgpr.mineur.repository.rapport.StatisticsGouvernoratRepositoryCustom;
import com.cgpr.mineur.repository.rapport.StatisticsNationaliteRepositoryCustom;
import com.cgpr.mineur.repository.rapport.StatisticsRepositoryCustom;
import com.cgpr.mineur.repository.rapport.StatisticsTerrorismeRepositoryCustom;
import com.cgpr.mineur.repository.rapport.StatisticsTribunalRepositoryCustom;
import com.cgpr.mineur.repository.rapport.StatisticsTypeAffaireRepositoryCustom;
import com.cgpr.mineur.resource.PDFListExistDTO;
//import com.cgpr.mineur.service.RapportEnfantQuotidienService;
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
//	private final RapportEnfantQuotidienRepository rapportEnfantQuotidienRepository;
//	private final RapportEnfantQuotidienService rapportEnfantQuotidienService;
	private final StatisticsRepositoryCustom statisticsRepositoryCustom;
	private final  StatisticsNationaliteRepositoryCustom statisticsNationaliteRepositoryCustom;
	private final StatisticsTerrorismeRepositoryCustom statisticsTerrorismeRepositoryCustom;
	private final StatisticsTypeAffaireRepositoryCustom statisticsTypeAffaireRepositoryCustom; 
	private final StatisticsTribunalRepositoryCustom statisticsTribunalRepositoryCustom; 
	private final StatisticsGlobaleRepositoryCustom statisticsGlobaleRepositoryCustom; 
	private final StatisticsGouvernoratRepositoryCustom statisticsGouvernoratRepositoryCustom;
	private final StatTypeTribunalEtablissementRepository statTypeTribunalEtablissementRepository;
	 private final  StatTypeTribunalPeriodeRepository statTypeTribunalPeriodeRepository;
	 private final  StatTypeTribunalPeriodeResidenceRepository statTypeTribunalPeriodeResidenceRepository;
	 
	 private final StatTypeTribunalPeriodeResidenceGenericRepository statTypeTribunalPeriodeResidenceGenericRepository;
	@Autowired
	public GenererStatistiquPdfMensuelImpl(ChargeAllEnfantService chargeAllEnfantService,
			StatistcsRepository statistcsRepository, VisiteRepository visiteRepository ,
//			 RapportEnfantQuotidienRepository rapportEnfantQuotidienRepository,
//			 RapportEnfantQuotidienService rapportEnfantQuotidienService,
			 StatisticsRepositoryCustom statisticsRepositoryCustom,
			 StatisticsNationaliteRepositoryCustom statisticsNationaliteRepositoryCustom,
			 StatisticsTerrorismeRepositoryCustom statisticsTerrorismeRepositoryCustom,
			 StatisticsTypeAffaireRepositoryCustom statisticsTypeAffaireRepositoryCustom,
			 StatisticsGlobaleRepositoryCustom statisticsGlobaleRepositoryCustom,
			 StatisticsTribunalRepositoryCustom statisticsTribunalRepositoryCustom,
			 StatisticsGouvernoratRepositoryCustom statisticsGouvernoratRepositoryCustom,
			 StatTypeTribunalEtablissementRepository statTypeTribunalEtablissementRepository,
			 StatTypeTribunalPeriodeRepository statTypeTribunalPeriodeRepository,
			 StatTypeTribunalPeriodeResidenceRepository statTypeTribunalPeriodeResidenceRepository,
			 StatTypeTribunalPeriodeResidenceGenericRepository statTypeTribunalPeriodeResidenceGenericRepository
			 ) {
		
					this.chargeAllEnfantService = chargeAllEnfantService;
					this.visiteRepository = visiteRepository;
					this.statistcsRepository = statistcsRepository;
//					this.rapportEnfantQuotidienRepository=rapportEnfantQuotidienRepository;
//					this.rapportEnfantQuotidienService =rapportEnfantQuotidienService;
					this.statisticsRepositoryCustom = statisticsRepositoryCustom;
					this.statisticsNationaliteRepositoryCustom = statisticsNationaliteRepositoryCustom;
					this.statisticsTerrorismeRepositoryCustom=statisticsTerrorismeRepositoryCustom;
					this.statisticsTypeAffaireRepositoryCustom=statisticsTypeAffaireRepositoryCustom;
					this.statisticsGlobaleRepositoryCustom = statisticsGlobaleRepositoryCustom;
					this.statisticsTribunalRepositoryCustom = statisticsTribunalRepositoryCustom;
					this.statisticsGouvernoratRepositoryCustom = statisticsGouvernoratRepositoryCustom;
					this.statTypeTribunalEtablissementRepository = statTypeTribunalEtablissementRepository;
					this.statTypeTribunalPeriodeRepository = statTypeTribunalPeriodeRepository;
					this.statTypeTribunalPeriodeResidenceRepository = statTypeTribunalPeriodeResidenceRepository;
					this.statTypeTribunalPeriodeResidenceGenericRepository = statTypeTribunalPeriodeResidenceGenericRepository;
	 
	}

	 

	ConfigShaping boldConf = new ConfigShaping();

	 
	 
	@Override
	public ByteArrayInputStream genererStatistiquePdfMensuel(PDFListExistDTO pDFListExistDTO) throws DocumentException, IOException, ArabicShapingException {
       
		   
		   // LocalDate localDate = LocalDate.parse(date);
		   
		
		 
		LocalDateTime localDate = LocalDateTime.now();
		 
		int yearsVisite = 0;
		int moisVisite = 0;
		String dateAujourdhui="";
		String dateString = pDFListExistDTO.getDatePrintAllCentre();
		 
		List<StatistiqueEtablissementDTO> statEta =null;
		if (dateString == null) {
			yearsVisite = localDate.getYear();
			moisVisite = localDate.getMonthValue();

		} else {

			LocalDate localDateJson = LocalDate.parse(dateString);
//		  statEta = rapportEnfantQuotidienService.getStatistiquesParDate(dateString);
		  
			yearsVisite = localDateJson.getYear();
			moisVisite = localDateJson.getMonthValue();
			
			// Créer un formateur de date avec le format spécifié
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        
	        // Obtenir la date actuelle
	        LocalDate aujourdhui = LocalDate.now();
	        
	        // Formater la date au format spécifié
	          dateAujourdhui = aujourdhui.format(formatter);

		}
		LocalDate date1 = LocalDate.parse(pDFListExistDTO.getDatePrintAllCentre());
	 //   LocalDate firstDayOfMonth = date1.withDayOfMonth(1);
	    List<StatistiqueEtablissementDTO> list =     statisticsRepositoryCustom.findStatistics(date1, date1);
	    List<StatistiqueNationaliteDTO> listNationalite =     statisticsNationaliteRepositoryCustom.findStatisticNationalites (date1, date1);
	    List<StatistiqueTerrorismeDTO> listTerrorisme =  statisticsTerrorismeRepositoryCustom.findStatisticTerrorismes(date1, date1);
	    List<StatistiqueTypeAffaireDTO> listTypeAffaire =  statisticsTypeAffaireRepositoryCustom.findStatisticTypeAffaires(  date1);
	    List<StatistiqueTribunalDTO> listTribunal =  statisticsTribunalRepositoryCustom.findStatistics(  date1);
	 	
	    List<StatistiqueGlobaleDTO> listGlobale =   statisticsGlobaleRepositoryCustom.findStatisticsGlobale(date1, date1);
	    
	    List<StatistiqueGouvernoratDTO> listGouvernorat  =   statisticsGouvernoratRepositoryCustom.findStatistics(date1 );
	    
	    List<StatTypeTribunalEtalissementDTO> listTypeTribunalEtalissement   =statTypeTribunalEtablissementRepository.findStatistics(date1);
	    
	    List<StatTypeTribunalPeriodeDTO> listTypeTribunalPeriode   =statTypeTribunalPeriodeRepository.findStatistics(date1);
	    
	    List<StatTypeTribunalPeriodeResidenceDTO> listTypeTribunalPeriodeResidence = statTypeTribunalPeriodeResidenceRepository.findStatistics(date1);
	    
	    List<StatTypeTribunalPeriodeResidenceGenericDTO> listTypeTribunalPeriodeResidenceGeneric = statTypeTribunalPeriodeResidenceGenericRepository.findStatistics(date1);
	    
	    List<String> etablissementIds = Arrays.asList("11","12","13","14","16"  );
 		
		String titreString = " ";
 
	 
	List<String> statutPenals = Arrays.asList("juge", "arret", "libre");
//	LocalDate date1 = LocalDate.parse(pDFListExistDTO.getDatePrintAllCentre());
 

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 25f, 0f);

		PdfWriter.getInstance(document, out);
		document.open();

 		PdfPTable tableTop = new PdfPTable(3);  
		String gouvernorat = "تونس".toString();
        
		 String date = ToolsForReporting.dtf.format(LocalDate.now());

		try {
			tableTop = ToolsForReporting.createTopTable(gouvernorat, date);
		} catch (Exception e1) {

			e1.printStackTrace();
		}

		 	
		titreString= "    حوصلـــة حولـــة مـــراكز  إصــــلاح الأطفــــال الجــــانحين "+ " خلال شهر "  
 				+ ToolsForReporting.getCurrentArabicMonth(moisVisite) + " " + yearsVisite; //localDate.getYear()
			    PdfPTable tTitre = ToolsForReporting.createTitleTable(titreString);
 	
		
		tTitre.setSpacingBefore(50f);
		
 
		PdfPTable tTitreStatiqueGenerale=ToolsForReporting.addTitleToPdfTable(  "  الإحـصــــــــــائية العــــــــــــامة  ",
		        ToolsForReporting.boldfontTitleStatique, 1, null,0);
 
 		PdfPTable tableStatiqueGenerale = ToolsForReporting.createPdfTable(100, 80);
 
 		
 	 

 		
 		Long jugeM =list.get(0).getNbrStatutPenalJuge()
		 +list.get(2).getNbrStatutPenalJuge()
		 +list.get(3).getNbrStatutPenalJuge()
		 +list.get(4).getNbrStatutPenalJuge();
 		Long jugeF =list.get(1).getNbrStatutPenalJuge();
 		
 		Long arretM =list.get(0).getNbrStatutPenalArrete()
 				 +list.get(2).getNbrStatutPenalArrete()
 				 +list.get(3).getNbrStatutPenalArrete()
 				 +list.get(4).getNbrStatutPenalArrete();
 		Long arretF =list.get(1).getNbrStatutPenalArrete();
 		 		
 				 

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
			ToolsForReporting.addCellToTable(tableStatiqueGenerale,jugeF + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale ,
					jugeM
					   + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "المحكومين ", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);

			ToolsForReporting.addCellToTable(tableStatiqueGenerale, arretF + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, arretM + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "الموقوفين ", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);

			ToolsForReporting.addCellToTable(tableStatiqueGenerale,  arretF+jugeF  + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, arretM+jugeM + "", 30,
					Element.ALIGN_CENTER, null, 30);
			ToolsForReporting.addCellToTable(tableStatiqueGenerale, "المجموع", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);

			ToolsForReporting.addCellToTable(tableStatiqueGenerale,arretM+jugeM+arretF+jugeF + "", 60,
					Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellVideToTable(tableStatiqueGenerale, " ", 40, Element.ALIGN_CENTER,
			        null, 30); 
			//----------------------------------------------------

			PdfPTable tTitreStatique = new PdfPTable(1);

			Phrase pTitreStatique = new Phrase(boldConf.format("  إحـصــــــــــائية مفصلة  حسب مراكز الإصلاح     "),
					ToolsForReporting.boldfontTitleStatique);
			PdfPCell cTitreStatique = new PdfPCell(pTitreStatique);
			cTitreStatique.setPadding(60f);
			cTitreStatique.setBorder(Rectangle.BOX);

			cTitreStatique.setBorderColor(BaseColor.WHITE);
			cTitreStatique.setHorizontalAlignment(Element.ALIGN_CENTER);

			tTitreStatique.setSpacingBefore(100f);
			tTitreStatique.addCell(cTitreStatique);

			PdfPTable tableStatique = new PdfPTable(120);
			tableStatique.setWidthPercentage(100);

			
			
			 
			ToolsForReporting.addCellVideToTable(tableStatique, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			ToolsForReporting.addCellToTable(tableStatique,     "المحكومين  ", 40,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique,     "الموقوفين ", 40,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			
			ToolsForReporting.addCellVideToTable(tableStatique, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			
			
			ToolsForReporting.addCellVideToTable(tableStatique, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			ToolsForReporting.addCellToTable(tableStatique,     "العائدين", 20,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique,     "المبتدئين", 20,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique,     "العائدين", 20,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique,     "المبتدئين", 20,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			
			ToolsForReporting.addCellVideToTable(tableStatique, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			
			
			ToolsForReporting.addCellVideToTable(tableStatique, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			
			ToolsForReporting.addCellToTable(tableStatique, "إناث   ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, "ذكور   ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, "إناث   ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, "ذكور   ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, "إناث   ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, "ذكور   ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, "  إناث ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, "ذكور", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
//			ToolsForReporting.addCellToTable(tableStatique, "المراكز الإصلاحية", 20, Element.ALIGN_CENTER,
//					new BaseColor(230, 230, 230), 30);
			
			ToolsForReporting.addCellVideToTable(tableStatique, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			 
			Long totalEnfants = 0L;
		    // Initialiser les totaux pour chaque catégorie
		    Long totalArretePrimaire = 0L;
		    Long totalArreteRevenant = 0L;
		    Long totalJugePrimaire = 0L;
		    Long totalJugeRevenant = 0L;
		    
		    Long totalArrete  = 0L;
		    Long totalJuge  = 0L;
		    
			for (StatistiqueGlobaleDTO st : listGlobale) {
			    // Récupérer les statistiques de l'établissement
			    Map<String, Integer> stats = st.getStatistics();

			    
			    
		

			    // Calcul des totaux pour chaque catégorie
			    for (Map.Entry<String, Integer> entry : stats.entrySet()) {
			        String key = entry.getKey();
			        Integer value = entry.getValue();

			        // Somme pour chaque catégorie
			        if (key.startsWith("arrete_primaire")) {
			            totalArretePrimaire += (value != null ? value : 0);
			        } else if (key.startsWith("arrete_revenant")) {
			            totalArreteRevenant += (value != null ? value : 0);
			        } else if (key.startsWith("juge_primaire")) {
			            totalJugePrimaire += (value != null ? value : 0);
			        } else if (key.startsWith("juge_revenant")) {
			            totalJugeRevenant += (value != null ? value : 0);
			        }
			        
			        if (key.startsWith("arrete")) {
			        	totalArrete += (value != null ? value : 0);
			        } else if (key.startsWith("juge")) {
			        	totalJuge += (value != null ? value : 0);
			        }
			    }
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			 // Initialiser le total par établissement
			    Long totalParEtablissement = 0L;

			    // Calculer le total pour cet établissement en additionnant toutes les valeurs
			    totalParEtablissement += (stats.get("juge_revenant_femme") != null ? stats.get("juge_revenant_femme") : 0);
			    totalParEtablissement += (stats.get("juge_revenant_homme") != null ? stats.get("juge_revenant_homme") : 0);
			    totalParEtablissement += (stats.get("juge_primaire_femme") != null ? stats.get("juge_primaire_femme") : 0);
			    totalParEtablissement += (stats.get("juge_primaire_homme") != null ? stats.get("juge_primaire_homme") : 0);
			    totalParEtablissement += (stats.get("arrete_revenant_femme") != null ? stats.get("arrete_revenant_femme") : 0);
			    totalParEtablissement += (stats.get("arrete_revenant_homme") != null ? stats.get("arrete_revenant_homme") : 0);
			    totalParEtablissement += (stats.get("arrete_primaire_femme") != null ? stats.get("arrete_primaire_femme") : 0);
			    totalParEtablissement += (stats.get("arrete_primaire_homme") != null ? stats.get("arrete_primaire_homme") : 0);

			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    // Ajouter le total à la table (au lieu de 123, on met totalParEtablissement)
			    ToolsForReporting.addCellToTable(tableStatique, totalParEtablissement + "", 20,
			            Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			    
			    ToolsForReporting.addCellToTable(tableStatique, (stats.get("juge_revenant_femme") != null ? stats.get("juge_revenant_femme") : "") + "", 10,
			            Element.ALIGN_CENTER, null, 30);
			    ToolsForReporting.addCellToTable(tableStatique, (stats.get("juge_revenant_homme") != null ? stats.get("juge_revenant_homme") : "") + "", 10,
			            Element.ALIGN_CENTER, null, 30);
			    ToolsForReporting.addCellToTable(tableStatique, (stats.get("juge_primaire_femme") != null ? stats.get("juge_primaire_femme") : "") + "", 10,
			            Element.ALIGN_CENTER, null, 30);
			    ToolsForReporting.addCellToTable(tableStatique, (stats.get("juge_primaire_homme") != null ? stats.get("juge_primaire_homme") : "") + "", 10,
			            Element.ALIGN_CENTER, null, 30);
			    ToolsForReporting.addCellToTable(tableStatique, (stats.get("arrete_revenant_femme") != null ? stats.get("arrete_revenant_femme") : "") + "", 10,
			            Element.ALIGN_CENTER, null, 30);
			    ToolsForReporting.addCellToTable(tableStatique, (stats.get("arrete_revenant_homme") != null ? stats.get("arrete_revenant_homme") : "") + "", 10,
			            Element.ALIGN_CENTER, null, 30);
			    ToolsForReporting.addCellToTable(tableStatique, (stats.get("arrete_primaire_femme") != null ? stats.get("arrete_primaire_femme") : "") + "", 10,
			            Element.ALIGN_CENTER, null, 30);
			    ToolsForReporting.addCellToTable(tableStatique, (stats.get("arrete_primaire_homme") != null ? stats.get("arrete_primaire_homme") : "") + "", 10,
			            Element.ALIGN_CENTER, null, 30);
			    ToolsForReporting.addCellToTable(tableStatique, st.getLibelleEtablissement(), 20,
			            Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
		 
			}
			// Ajout du total général
			
			
			ToolsForReporting.addCellVideToTable(tableStatique, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			ToolsForReporting.addCellToTable(tableStatique, totalJugeRevenant + "", 20,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, totalJugePrimaire + "", 20,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, totalArreteRevenant + "", 20,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, totalArretePrimaire + "", 20,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellVideToTable(tableStatique, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			
			
			
			
			ToolsForReporting.addCellVideToTable(tableStatique, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			ToolsForReporting.addCellToTable(tableStatique, totalJuge + "", 40,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique, totalArrete + "", 40,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			
			ToolsForReporting.addCellVideToTable(tableStatique, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			
			
			ToolsForReporting.addCellVideToTable(tableStatique, " ", 20, Element.ALIGN_CENTER,
					null, 30); 
			ToolsForReporting.addCellToTable(tableStatique, totalArrete+totalJuge + "", 80,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellVideToTable(tableStatique, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			
			
			
		    
			// ----------------------------------------------------------------------------------------------------------------
	
			
			
			
//			PdfPTable tTitreStatique1 = new PdfPTable(1);
//
//			Phrase pTitreStatique1 = new Phrase(boldConf.format("الإحـصــــــــــائية حسب الجنسية  و الجنس  "),
//					ToolsForReporting.boldfontTitleStatique);
//			PdfPCell cTitreStatique1 = new PdfPCell(pTitreStatique);
//			cTitreStatique1.setPadding(0f);
//			cTitreStatique1.setBorder(Rectangle.BOX);
//
//			cTitreStatique1.setBorderColor(BaseColor.WHITE);
//			cTitreStatique1.setHorizontalAlignment(Element.ALIGN_CENTER);
//
//			tTitreStatique1.setSpacingBefore(0f);
//			tTitreStatique1.addCell(cTitreStatique1);
			
			
			PdfPTable tTitreStatique1 = new PdfPTable(1);

			Phrase pTitreStatique1 = new Phrase(boldConf.format("إحـصــــــــــائية حسب الجنسية  و الجنس"),
					ToolsForReporting.boldfontTitleStatique);
			PdfPCell cTitreStatique1 = new PdfPCell(pTitreStatique1);
			cTitreStatique1.setPadding(50f);
			cTitreStatique1.setBorder(Rectangle.BOX);

			cTitreStatique1.setBorderColor(BaseColor.WHITE);
			cTitreStatique1.setHorizontalAlignment(Element.ALIGN_CENTER);

			tTitreStatique1.setSpacingBefore(30f);
			tTitreStatique1.addCell(cTitreStatique1);
			
		     
			PdfPTable tableStatique1 = new PdfPTable(100);
			tableStatique1.setWidthPercentage(80);
//			ToolsForReporting.addCellToTable(tableStatique1, "إحـصــــــــــائية حسب الجنسية  و الجنس  ", 100, Element.ALIGN_CENTER,
//					null, 30);
			 
		
			ToolsForReporting.addCellToTable(tableStatique1, "أنثى  ", 30, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			ToolsForReporting.addCellToTable(tableStatique1, "ذكر  ", 30, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			ToolsForReporting.addCellToTable(tableStatique1, "الجنسية  ", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			
			Long totale=0L;
			
			for(StatistiqueNationaliteDTO st : listNationalite) {
				totale=st.getNbrFemmes()+st.getNbrHommes();
				
			 
				ToolsForReporting.addCellToTableforStat(tableStatique1,st.getNbrFemmes()+"", 30, Element.ALIGN_CENTER,
						null, 25);
				 
				ToolsForReporting.addCellToTableforStat(tableStatique1, st.getNbrHommes()+"", 30, Element.ALIGN_CENTER,
						null, 25);
				ToolsForReporting.addCellToTableforStat(tableStatique1, st.getNationalite(), 40, Element.ALIGN_CENTER,
						null, 25);
				
			
			}
			ToolsForReporting.addCellToTable(tableStatique1,totale + "", 60,
					Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellVideToTable(tableStatique1, " ", 40, Element.ALIGN_CENTER,
			        null, 30); 
			 
			//---------------------------------
			
//			PdfPTable tTitreStatique2 = new PdfPTable(1);
//
//			Phrase pTitreStatique2 = new Phrase(boldConf.format("       إحـصــــــــــائية الإرهــــــــاب   "),
//					ToolsForReporting.boldfontTitleStatique);
//			PdfPCell cTitreStatique2 = new PdfPCell(pTitreStatique2);
//			//cTitreStatique2.setPadding(50f);
//			cTitreStatique2.setBorder(Rectangle.BOX);
//
//			cTitreStatique2.setBorderColor(BaseColor.WHITE);
//			cTitreStatique2.setHorizontalAlignment(Element.ALIGN_CENTER);
//
//			//tTitreStatique2.setSpacingBefore(30f);
//			tTitreStatique2.addCell(cTitreStatique2);
			 
			PdfPTable tableStatique2 = new PdfPTable(100);
			tableStatique2.setWidthPercentage(80);
			ToolsForReporting.addCellToTable(tableStatique2, "   إحـصــــــــــائية الإرهــــــــاب   ", 100, Element.ALIGN_CENTER,
					null, 30);
			ToolsForReporting.addCellToTable(tableStatique2, "  المجموع   ", 20, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
		
			 
			ToolsForReporting.addCellToTable(tableStatique2, "الوضعية   ", 20, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique2, "القضية  ", 20, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique2, "الجنسية  ", 20, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique2, "المركز  ", 20, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 30);
			
			for(StatistiqueTerrorismeDTO st : listTerrorisme) {
				
				
				 
				ToolsForReporting.addCellToTableforStat(tableStatique2,st.getTotale()+"", 20, Element.ALIGN_CENTER,
						null, 30);
				 
				ToolsForReporting.addCellToTableforStat(tableStatique2, st.getSituationJudiciaire()+"", 20, Element.ALIGN_CENTER,
						null, 30);
				ToolsForReporting.addCellToTableforStat(tableStatique2, st.getTypeAffaire(), 20, Element.ALIGN_CENTER,
						null, 30);
				ToolsForReporting.addCellToTableforStat(tableStatique2, st.getNationalite()+"", 20, Element.ALIGN_CENTER,
						null, 30);
				ToolsForReporting.addCellToTableforStat(tableStatique2, st.getEtablissement(), 20, Element.ALIGN_CENTER,
						null, 30);
				
			
			}
			// ----------------------------------------------------------------------------------------------------------------
			 
			
			
//			PdfPTable tTitreStatique3 = new PdfPTable(1);
//
//			Phrase pTitreStatique3 = new Phrase(boldConf.format("إحصــــــــائية      حسب نوع القضيــــــــة الرئسيــــــــة "),
//					ToolsForReporting.boldfontTitleStatique);
//			PdfPCell cTitreStatique3 = new PdfPCell(pTitreStatique3);
//			cTitreStatique3.setPadding(50f);
//			cTitreStatique3.setBorder(Rectangle.BOX);
//
//			cTitreStatique3.setBorderColor(BaseColor.WHITE);
//			cTitreStatique3.setHorizontalAlignment(Element.ALIGN_CENTER);
//
//			tTitreStatique3.setSpacingBefore(30f);
//			tTitreStatique3.addCell(cTitreStatique3);
//			
			
			
			PdfPTable tableStatique3 = new PdfPTable(100);
			tableStatique3.setWidthPercentage(80);
 			ToolsForReporting.addCellToTable(tableStatique3, "   إحصــــــــائية حسب نوع القضيــــــــة الرئسيــــــــة         ", 100, Element.ALIGN_CENTER,
 					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique3, "  محكومين    ", 30, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 26);
		
			 
			ToolsForReporting.addCellToTable(tableStatique3, "موقوفين    ", 30, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 26);
			ToolsForReporting.addCellToTable(tableStatique3, "القضية  ", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 23);
			Long totaleAffairePrincipale=0L;
			
			for(StatistiqueTypeAffaireDTO st : listTypeAffaire) {
				
				totaleAffairePrincipale=st.getJuge()+st.getArrete();
				 
				ToolsForReporting.addCellToTableforStat(tableStatique3,st.getJuge()+"", 30, Element.ALIGN_CENTER,
						null, 23);
				 
				ToolsForReporting.addCellToTableforStat(tableStatique3, st.getArrete()+"", 30, Element.ALIGN_CENTER,
						null, 23);
				ToolsForReporting.addCellToTableforStat(tableStatique3, st.getTypeAffaire(), 60, Element.ALIGN_CENTER,
						null, 23);
			 
				
			
			}
			
			ToolsForReporting.addCellToTable(tableStatique3,totaleAffairePrincipale + "", 60,
					Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellVideToTable(tableStatique3, " ", 40, Element.ALIGN_CENTER,
			        null, 30); 
			// ----------------------------------------------------------------------------------------------------------------
		
			
			PdfPTable tTitreStatique4 = new PdfPTable(1);

			Phrase pTitreStatique4 = new Phrase(boldConf.format("إحصــــــــائية حسب    المحــــــاكم "),
					ToolsForReporting.boldfontTitleStatique);
			PdfPCell cTitreStatique4 = new PdfPCell(pTitreStatique4);
			cTitreStatique4.setPadding(60f);
			cTitreStatique4.setBorder(Rectangle.BOX);

			cTitreStatique4.setBorderColor(BaseColor.WHITE);
			cTitreStatique4.setHorizontalAlignment(Element.ALIGN_CENTER);

			tTitreStatique4.setSpacingBefore(100f);
			tTitreStatique4.addCell(cTitreStatique4);
			PdfPTable tableStatique4 = new PdfPTable(100);
			tableStatique4.setWidthPercentage(80);
//			ToolsForReporting.addCellToTable(tableStatique4, "إحصــــــــائية حسب    المحــــــاكم  ", 100, Element.ALIGN_CENTER,
//					new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique4, "  محكومين    ", 30, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 26);
		
			 
			ToolsForReporting.addCellToTable(tableStatique4, "موقوفين    ", 30, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 26);
			ToolsForReporting.addCellToTable(tableStatique4, "المحكمــــــة    ", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 23);
			Long totaleAffairePrincipale1=0L;
			
			
			String gouvernoratActuel =  listTribunal.get(0).getGouvernorat();
		//	ToolsForReporting.addCellVideToTable(tableStatique4, " ", 60, Element.ALIGN_CENTER, null, 30);
	        ToolsForReporting.addCellToTableforStat(tableStatique4, gouvernoratActuel +"", 100, Element.ALIGN_CENTER,
	        		new BaseColor(230, 230, 230), 23);
	for(StatistiqueTribunalDTO st : listTribunal) {
		
		if (st.getGouvernorat() != null && !st.getGouvernorat().equals(gouvernoratActuel)  ) {
	        // Ajouter une ligne vide pour marquer la séparation
			
			//ToolsForReporting.addCellVideToTable(tableStatique4, " ", 100, Element.ALIGN_CENTER, null, 30);
	   //     ToolsForReporting.addCellVideToTable(tableStatique4, " ", 60, Element.ALIGN_CENTER, null, 30);
	        ToolsForReporting.addCellToTableforStat(tableStatique4, st.getGouvernorat() +"", 100, Element.ALIGN_CENTER,
	        		new BaseColor(230, 230, 230), 23);
	        // Mettre à jour le gouvernorat actuel
	        gouvernoratActuel = st.getGouvernorat();
	    }
		 
		 
		 
		 
		 
				totaleAffairePrincipale1=st.getNombreJuge()+st.getNombreArrete();
				 
				ToolsForReporting.addCellToTableforStat(tableStatique4,st.getNombreJuge()+"", 30, Element.ALIGN_CENTER,
						null, 23);
				 
				ToolsForReporting.addCellToTableforStat(tableStatique4, st.getNombreArrete()+"", 30, Element.ALIGN_CENTER,
						null, 23);
				ToolsForReporting.addCellToTableforStat(tableStatique4, st.getTribunal(), 60, Element.ALIGN_CENTER,
						null, 23);
			 
				
			
			}
			
			ToolsForReporting.addCellToTable(tableStatique4,totaleAffairePrincipale1 + "", 60,
					Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellVideToTable(tableStatique4, " ", 40, Element.ALIGN_CENTER,
			        null, 30); 
			// ----------------------------------------------------------------------------------------------------------------

			
			
			
			
			
			
			
			
			
			
			
			
			
			PdfPTable tTitreStatique5 = new PdfPTable(1);

			Phrase pTitreStatique5 = new Phrase(boldConf.format(" إحـصــــــــــائية مفصلة حسب الولايات "),
					ToolsForReporting.boldfontTitleStatique);
			PdfPCell cTitreStatique5 = new PdfPCell(pTitreStatique5);
			cTitreStatique5.setPadding(60f);
			cTitreStatique5.setBorder(Rectangle.BOX);

			cTitreStatique5.setBorderColor(BaseColor.WHITE);
			cTitreStatique5.setHorizontalAlignment(Element.ALIGN_CENTER);

			tTitreStatique5.setSpacingBefore(100f);
			tTitreStatique5.addCell(cTitreStatique5);

			PdfPTable tableStatique5 = new PdfPTable(120);
			tableStatique5.setWidthPercentage(100);

			
			
			 
			ToolsForReporting.addCellVideToTable(tableStatique5, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			ToolsForReporting.addCellToTable(tableStatique5,     "المحكومين  ", 40,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique5,     "الموقوفين ", 40,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			
			ToolsForReporting.addCellVideToTable(tableStatique5, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			
			
			ToolsForReporting.addCellVideToTable(tableStatique5, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			ToolsForReporting.addCellToTable(tableStatique5,     "إنــاث", 20,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique5,     "ذكـور", 20,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique5,     "إنــاث", 20,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			ToolsForReporting.addCellToTable(tableStatique5,     "ذكـور", 20,
			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			
			ToolsForReporting.addCellVideToTable(tableStatique5, " ", 20, Element.ALIGN_CENTER,
			        null, 30); 
			
			
//			ToolsForReporting.addCellVideToTable(tableStatique5, " ", 20, Element.ALIGN_CENTER,
//			        null, 30); 
//			
//		 
//			ToolsForReporting.addCellToTable(tableStatique5, "ذكور   ", 20, Element.ALIGN_CENTER,
//					new BaseColor(230, 230, 230), 30);
//			 
//			ToolsForReporting.addCellToTable(tableStatique5, "ذكور   ", 20, Element.ALIGN_CENTER,
//					new BaseColor(230, 230, 230), 30);
//	 
//			ToolsForReporting.addCellToTable(tableStatique5, "ذكور   ", 20, Element.ALIGN_CENTER,
//					new BaseColor(230, 230, 230), 30);
//		 
//			ToolsForReporting.addCellToTable(tableStatique5, "ذكور", 20, Element.ALIGN_CENTER,
//					new BaseColor(230, 230, 230), 30);
////			ToolsForReporting.addCellToTable(tableStatique, "المراكز الإصلاحية", 20, Element.ALIGN_CENTER,
////					new BaseColor(230, 230, 230), 30);
//			
//			ToolsForReporting.addCellVideToTable(tableStatique5, " ", 20, Element.ALIGN_CENTER,
//			        null, 30); 
//			 
			Long totalEnfants5 = 0L;
		    // Initialiser les totaux pour chaque catégorie
		    Long totalArretePrimaire5 = 0L;
		    Long totalArreteRevenant5 = 0L;
		    Long totalJugePrimaire5 = 0L;
		    Long totalJugeRevenant5 = 0L;
		    
		    Long totalArrete5  = 0L;
		    Long totalJuge5  = 0L;
		    
			for (StatistiqueGouvernoratDTO st : listGouvernorat) {
			  
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			 // Initialiser le total par établissement
			    Long totalParEtablissement5 = 0L;

			    // Calculer le total pour cet établissement en additionnant toutes les valeurs
			    
			    totalParEtablissement5  = st.getJugefeminin()+st.getJugemasculin() +st.getArretefeminin()+st.getArretemasculin() ;
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    // Ajouter le total à la table (au lieu de 123, on met totalParEtablissement)
			    ToolsForReporting.addCellToTable(tableStatique5, totalParEtablissement5 + "", 20,
			            Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
			    
			    
			    ToolsForReporting.addCellToTable(tableStatique5, st.getJugefeminin() != null ? st.getJugefeminin()+"" : "" , 20,
			            Element.ALIGN_CENTER, null, 30);
			     
			    ToolsForReporting.addCellToTable(tableStatique5, st.getJugemasculin()  != null ?st.getJugemasculin()+"" : "" + "", 20,
			            Element.ALIGN_CENTER, null, 30);
			    
			    ToolsForReporting.addCellToTable(tableStatique5, st.getArretefeminin() != null ?  st.getArretefeminin()+"" : "" + "", 20,
			            Element.ALIGN_CENTER, null, 30);
			   
			    ToolsForReporting.addCellToTable(tableStatique5, st.getArretemasculin()   != null ? st.getArretemasculin()+"" : "" + "", 20,
			            Element.ALIGN_CENTER, null, 30);
			    ToolsForReporting.addCellToTable(tableStatique5, st.getGouvernorat(), 20,
			            Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
		 
			}
			// Ajout du total général
			
			
//			ToolsForReporting.addCellVideToTable(tableStatique5, " ", 20, Element.ALIGN_CENTER,
//			        null, 30); 
//			ToolsForReporting.addCellToTable(tableStatique5, totalJugeRevenant5 + "", 20,
//			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
//			ToolsForReporting.addCellToTable(tableStatique5, totalJugePrimaire5 + "", 20,
//			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
//			ToolsForReporting.addCellToTable(tableStatique5, totalArreteRevenant5 + "", 20,
//			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
//			ToolsForReporting.addCellToTable(tableStatique5, totalArretePrimaire5 + "", 20,
//			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
//			ToolsForReporting.addCellVideToTable(tableStatique5, " ", 20, Element.ALIGN_CENTER,
//			        null, 30); 
//			
//			
//			
//			
//			ToolsForReporting.addCellVideToTable(tableStatique5, " ", 20, Element.ALIGN_CENTER,
//			        null, 30); 
//			ToolsForReporting.addCellToTable(tableStatique5, totalJuge5 + "", 40,
//			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
//			ToolsForReporting.addCellToTable(tableStatique5, totalArrete5 + "", 40,
//			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
//			
//			ToolsForReporting.addCellVideToTable(tableStatique5, " ", 20, Element.ALIGN_CENTER,
//			        null, 30); 
//			
//			
//			ToolsForReporting.addCellVideToTable(tableStatique5, " ", 20, Element.ALIGN_CENTER,
//					null, 30); 
//			ToolsForReporting.addCellToTable(tableStatique5, totalArrete+totalJuge5 + "", 80,
//			        Element.ALIGN_CENTER, new BaseColor(230, 230, 230), 30);
//			ToolsForReporting.addCellVideToTable(tableStatique5, " ", 20, Element.ALIGN_CENTER,
//			        null, 30); 
			
			
			
		    
			// ----------------------------------------------------------------------------------------------------------------
			PdfPTable tTitreStatique6 = new PdfPTable(1);

			Phrase pTitreStatique6 = new Phrase(boldConf.format("إحـصــــــــــائية الموقوفين  حسب أنواع المحاكم و المراكز"),
					ToolsForReporting.boldfontTitleStatique);
			PdfPCell cTitreStatique6 = new PdfPCell(pTitreStatique6);
			cTitreStatique6.setPadding(60f);
			cTitreStatique6.setBorder(Rectangle.BOX);

			cTitreStatique6.setBorderColor(BaseColor.WHITE);
			cTitreStatique6.setHorizontalAlignment(Element.ALIGN_CENTER);

			tTitreStatique6.setSpacingBefore(100f);
			tTitreStatique6.addCell(cTitreStatique6);
			
			PdfPTable tableStatique6 = new PdfPTable(100);
			tableStatique6.setWidthPercentage(80);
			 
			 
			ToolsForReporting.addCellToTable(tableStatique6, "المجموع  ", 10, Element.ALIGN_CENTER,
					new BaseColor(210, 210, 210), 25);
			ToolsForReporting.addCellToTable(tableStatique6, "مجاز الباب	  ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			ToolsForReporting.addCellToTable(tableStatique6, "المغيرة  ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			ToolsForReporting.addCellToTable(tableStatique6, "السوق الجديد	  ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			ToolsForReporting.addCellToTable(tableStatique6, "سيدي الهاني	  ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			ToolsForReporting.addCellToTable(tableStatique6, "المــروج  ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			ToolsForReporting.addCellToTable(tableStatique6, "أنواع المحاكم   ", 50, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			
			 
			
			for(StatTypeTribunalEtalissementDTO st : listTypeTribunalEtalissement) {
				 
				
				  
				
				
				ToolsForReporting.addCellToTableforStat(tableStatique6, st.getSommeTypeTribunal()+"", 10, Element.ALIGN_CENTER,
						new BaseColor(230, 230, 230), 25);
				ToolsForReporting.addCellToTableforStat(tableStatique6, st.getSommeMjazbeb()+"", 10, Element.ALIGN_CENTER,
						null, 25);
				ToolsForReporting.addCellToTableforStat(tableStatique6,st.getSommeMghira()+"", 10, Element.ALIGN_CENTER,
						null, 25);
				ToolsForReporting.addCellToTableforStat(tableStatique6,st.getSommeSoukjdid()+"", 10, Element.ALIGN_CENTER,
						null, 25);
				ToolsForReporting.addCellToTableforStat(tableStatique6, st.getSommeSidiHeni()+"", 10, Element.ALIGN_CENTER,
						null, 25);
				ToolsForReporting.addCellToTableforStat(tableStatique6, st.getSommeMourouj()+"", 10, Element.ALIGN_CENTER,
						null, 25);
				ToolsForReporting.addCellToTableforStat(tableStatique6, st.getTypeTribunal(), 50, Element.ALIGN_RIGHT,
						null, 25);
				
			
			}
			 
			 
			 
			// ----------------------------------------------------------------------------------------------------------------
			// ----------------------------------------------------------------------------------------------------------------
			 
			
			PdfPTable tTitreStatique7 = new PdfPTable(1);

			
//			Phrase pTitreStatique7 = new Phrase(boldConf.format("الموقوفون حسب نوع المحكمة والفترة الزمنية منذ آخر تحديث للملف"),
//					ToolsForReporting.boldfontTitleStatique);
			Phrase pTitreStatique7 = new Phrase(boldConf.format("الموقوفون حسب الفترة الزمنية منذ آخر تحديث للملف"),
					ToolsForReporting.boldfontTitleStatique);
			PdfPCell cTitreStatique7 = new PdfPCell(pTitreStatique7);
			cTitreStatique7.setPadding(60f);
			cTitreStatique7.setBorder(Rectangle.BOX);

			cTitreStatique7.setBorderColor(BaseColor.WHITE);
			cTitreStatique7.setHorizontalAlignment(Element.ALIGN_CENTER);

			tTitreStatique7.setSpacingBefore(100f);
			tTitreStatique7.addCell(cTitreStatique7);
			
			PdfPTable tableStatique7 = new PdfPTable(100);
			tableStatique7.setWidthPercentage(100);
			 
			 
			ToolsForReporting.addCellToTable(tableStatique7, "المجموع  ", 10, Element.ALIGN_CENTER,
					new BaseColor(210, 210, 210), 25);
			ToolsForReporting.addCellToTable(tableStatique7, "أكثر من 12 شهراً  ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			ToolsForReporting.addCellToTable(tableStatique7, "بين 9 و 12 شهراً  ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			ToolsForReporting.addCellToTable(tableStatique7, "بين 6 و 9 أشهر  	  ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			ToolsForReporting.addCellToTable(tableStatique7, "  بين 3 و 6 أشهر	  ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			ToolsForReporting.addCellToTable(tableStatique7, "أقل من 3 أشهر  ", 10, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			ToolsForReporting.addCellToTable(tableStatique7, "أنواع المحاكم   ", 40, Element.ALIGN_CENTER,
					new BaseColor(230, 230, 230), 25);
			
			 
			
			for(StatTypeTribunalPeriodeDTO st : listTypeTribunalPeriode) {
				 
				
				  
				
				
				ToolsForReporting.addCellToTableforStat(tableStatique7, st.getTotal_typeTribunal()+"", 10, Element.ALIGN_CENTER,
						new BaseColor(230, 230, 230), 25);
				ToolsForReporting.addCellToTableforStat(tableStatique7, st.getPlus_12_mois()+"", 10, Element.ALIGN_CENTER,
						null, 25);
				ToolsForReporting.addCellToTableforStat(tableStatique7,st.getEntre_9_12_mois()+"", 10, Element.ALIGN_CENTER,
						null, 25);
				ToolsForReporting.addCellToTableforStat(tableStatique7,st.getEntre_6_9_mois()+"", 10, Element.ALIGN_CENTER,
						null, 25);
				ToolsForReporting.addCellToTableforStat(tableStatique7, st.getEntre_3_6_mois()+"", 10, Element.ALIGN_CENTER,
						null, 25);
				ToolsForReporting.addCellToTableforStat(tableStatique7, st.getMoins_3_mois()+"", 10, Element.ALIGN_CENTER,
						null, 25);
				ToolsForReporting.addCellToTableforStat(tableStatique7, st.getTypeTribunal(), 40, Element.ALIGN_RIGHT,
						null, 25);
				
			
			}
			 
			 
			 
			// ----------------------------------------------------------------------------------------------------------------
			 
			
			// ----------------------------------------------------------------------------------------------------------------
			 
			
						PdfPTable tTitreStatique8 = new PdfPTable(1);

						Phrase pTitreStatique8 = new Phrase(boldConf.format(" الموقوفين وفق نوع المحكمة ومدة الإيقاف منذ بدئه"),
								ToolsForReporting.boldfontTitleStatique);
						PdfPCell cTitreStatique8 = new PdfPCell(pTitreStatique8);
						cTitreStatique8.setPadding(60f);
						cTitreStatique8.setBorder(Rectangle.BOX);

						cTitreStatique8.setBorderColor(BaseColor.WHITE);
						cTitreStatique8.setHorizontalAlignment(Element.ALIGN_CENTER);

						tTitreStatique8.setSpacingBefore(100f);
						tTitreStatique8.addCell(cTitreStatique8);
						
						PdfPTable tableStatique8 = new PdfPTable(100);
						tableStatique8.setWidthPercentage(100);
						 
						 
						ToolsForReporting.addCellToTable(tableStatique8, "المجموع  ", 10, Element.ALIGN_CENTER,
								new BaseColor(210, 210, 210), 25);
						ToolsForReporting.addCellToTable(tableStatique8, "أكثر من 18 شهراً  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique8, "بين 15 و 18 شهراً  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique8, "بين 12 و 15 شهراً  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique8, "بين 9 و 12 شهراً  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique8, "بين 6 و 9 أشهر  	  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique8, "بين 3 و 6 أشهر	  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique8, "أقل من 3 أشهر  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique8, "أنواع المحاكم   ", 20, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						
						 
						
						for(StatTypeTribunalPeriodeResidenceDTO st : listTypeTribunalPeriodeResidence) {
							 
							
							  
							
							
							ToolsForReporting.addCellToTableforStat(tableStatique8, st.getTotal_typeTribunal()+"", 10, Element.ALIGN_CENTER,
									new BaseColor(230, 230, 230), 25);
							ToolsForReporting.addCellToTableforStat(tableStatique8, st.getPlus_18_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique8,st.getEntre_15_18_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique8,st.getEntre_12_15_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique8,st.getEntre_9_12_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique8,st.getEntre_6_9_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique8, st.getEntre_3_6_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique8, st.getMoins_3_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique8, st.getTypeTribunal(), 20, Element.ALIGN_RIGHT,
									null, 25);
							
						
						}
						 
						 
						 
						// ----------------------------------------------------------------------------------------------------------------
						 
						// ----------------------------------------------------------------------------------------------------------------
						 
						
						PdfPTable tTitreStatique9 = new PdfPTable(1);

						Phrase pTitreStatique9 = new Phrase(boldConf.format(" الموقوفين وفق نوع المحكمة و المركز ومدة الإيقاف منذ بدئه"),
								ToolsForReporting.boldfontTitleStatique);
						PdfPCell cTitreStatique9 = new PdfPCell(pTitreStatique9);
						cTitreStatique9.setPadding(20f);
						cTitreStatique9.setBorder(Rectangle.BOX);

						cTitreStatique9.setBorderColor(BaseColor.WHITE);
						cTitreStatique9.setHorizontalAlignment(Element.ALIGN_CENTER);

						tTitreStatique9.setSpacingBefore(100f);
						tTitreStatique9.addCell(cTitreStatique9);
						
						PdfPTable tableStatique9 = new PdfPTable(100);
						tableStatique9.setWidthPercentage(100);
						 
						 
						ToolsForReporting.addCellToTable(tableStatique9, "المجموع  ", 10, Element.ALIGN_CENTER,
								new BaseColor(210, 210, 210), 25);
						ToolsForReporting.addCellToTable(tableStatique9, "أكثر من 18 شهراً  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique9, "بين 15 و 18 شهراً  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique9, "بين 12 و 15 شهراً  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique9, "بين 9 و 12 شهراً  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique9, "بين 6 و 9 أشهر  	  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique9, "بين 3 و 6 أشهر	  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique9, "أقل من 3 أشهر  ", 10, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						ToolsForReporting.addCellToTable(tableStatique9, "أنواع المحاكم   ", 20, Element.ALIGN_CENTER,
								new BaseColor(230, 230, 230), 25);
						
						 
						String etabActuel =  listTypeTribunalPeriodeResidenceGeneric.get(0).getLibelleEtablissement();
						 ToolsForReporting.addCellToTableforStat(tableStatique9, etabActuel +"", 100, Element.ALIGN_CENTER,
					        		new BaseColor(230, 230, 230), 23);
						for(StatTypeTribunalPeriodeResidenceGenericDTO st : listTypeTribunalPeriodeResidenceGeneric) {
							 
							if (st.getLibelleEtablissement() != null && !st.getLibelleEtablissement().equals(etabActuel)  ) {
						        // Ajouter une ligne vide pour marquer la séparation
								
								//ToolsForReporting.addCellVideToTable(tableStatique4, " ", 100, Element.ALIGN_CENTER, null, 30);
						   //     ToolsForReporting.addCellVideToTable(tableStatique4, " ", 60, Element.ALIGN_CENTER, null, 30);
						        ToolsForReporting.addCellToTableforStat(tableStatique9, st.getLibelleEtablissement() +"", 100, Element.ALIGN_CENTER,
						        		new BaseColor(230, 230, 230), 23);
						        // Mettre à jour le gouvernorat actuel
						        etabActuel = st.getLibelleEtablissement();
						    }
							  
							
							
							ToolsForReporting.addCellToTableforStat(tableStatique9, st.getTotal_typeTribunal()+"", 10, Element.ALIGN_CENTER,
									new BaseColor(230, 230, 230), 25);
							ToolsForReporting.addCellToTableforStat(tableStatique9, st.getPlus_18_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique9,st.getEntre_15_18_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique9,st.getEntre_12_15_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique9,st.getEntre_9_12_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique9,st.getEntre_6_9_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique9, st.getEntre_3_6_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique9, st.getMoins_3_mois()+"", 10, Element.ALIGN_CENTER,
									null, 25);
							ToolsForReporting.addCellToTableforStat(tableStatique9, st.getTypeTribunal(), 20, Element.ALIGN_RIGHT,
									null, 25);
							
						
						}
						 
						 
						 
						// ----------------------------------------------------------------------------------------------------------------
						 
			//-----------------------------------------------------------------------
			
			document.add(tableTop);
 			document.add(tTitre);
			document.newPage();
			document.add(tTitreStatiqueGenerale);
			document.add(tableStatiqueGenerale);
			document.newPage();
			document.add(tTitreStatique);
			document.add(tableStatique);
			document.add(tTitreStatique1);
			document.add(tableStatique1);
			
			document.newPage();
//			document.add(cTitreStatique2);
			document.add(tableStatique2);
			document.newPage();
//			document.add(cTitreStatique3);
			document.add(tableStatique3);
			document.newPage();
			
			document.add(tTitreStatique4);
			document.add(tableStatique4);
		 
			
			 
			document.newPage();
			document.add(tTitreStatique5);
			
			document.add(tableStatique5);
			
              document.newPage();
              document.add(tTitreStatique6);
			
			document.add(tableStatique6);
			
			  document.newPage();
              document.add(tTitreStatique7);
			
			document.add(tableStatique7);
			
			 document.newPage();
             document.add(tTitreStatique8);
			
			document.add(tableStatique8);
			
			 document.newPage();
             document.add(tTitreStatique9);
			
			document.add(tableStatique9);
		 
			document.close();
		}
		return new ByteArrayInputStream(out.toByteArray());

	}
 
	 
}

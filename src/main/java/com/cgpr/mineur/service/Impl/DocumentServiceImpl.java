package com.cgpr.mineur.service.Impl;


 
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.dto.CalculeAffaireDto;
import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.ChangementLieu;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArretProvisoireRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.service.DocumentService;
 
import com.ibm.icu.text.DateFormat;

@Service
public class DocumentServiceImpl implements DocumentService{
	
	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private AccusationCarteRecupRepository accusationCarteRecupRepository;

	@Autowired
	private AccusationCarteDepotRepository accusationCarteDepotRepository;

	@Autowired
	private AccusationCarteHeberRepository accusationCarteHeberRepository;

	@Autowired
	private AccusationCarteHeberRepository accusationCarteHebroRepository;
//	

	@Autowired
	private ArretProvisoireRepository arretProvisoireRepository;

	@Autowired
	private AffaireRepository affaireRepository;
	
	

	@Override
	public List<Document> listAffaire() {
		return (List<Document>) documentRepository.findAll();
	}

	@Override
	public Document findDocumentById( DocumentId documentId) {

		System.out.println("==================================documente=========================");
		Optional<Document> doc = documentRepository.findById(documentId);

		try {
			return doc.get();
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public List<Document> getDocumentByAffaire(String idEnfant,
			 long numOrdinalArrestation,
			 long numOrdinalAffaire) {
		try {
			List<Document> aData = documentRepository.getDocumentByAffaire(idEnfant, numOrdinalArrestation,
					numOrdinalAffaire);

			System.out.println("*****************************************************");
			System.out.println(aData.toString());
			System.out.println("*****************************************************");

			if (aData.isEmpty()) {
				return  null;
			} else {
				return  aData ;

			}
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public List<TitreAccusation> getTitreAccusation(String idEnfant,
			 long numOrdinalArrestation,
			 long numOrdinalAffaire) {

		List<Document> aData = documentRepository.getDocumentByAffaireforAccusation(idEnfant, numOrdinalArrestation,
				numOrdinalAffaire, PageRequest.of(0, 1));
		if (aData.isEmpty()) {
			return  null;
		} else {
			List<TitreAccusation> titreAccusations = null;

			if (aData.get(0) instanceof CarteRecup) {
				System.out.println("CarteRecup.."  );
				titreAccusations = accusationCarteRecupRepository
						.getTitreAccusationbyDocument(aData.get(0).getDocumentId());
				for (TitreAccusation titreAccusation : titreAccusations) {
					System.out.println(titreAccusation.getTitreAccusation());
				}
			} else if (aData.get(0) instanceof CarteDepot) {
				System.out.println("CarteDepot.."  );
				titreAccusations = accusationCarteDepotRepository
						.getTitreAccusationbyDocument(aData.get(0).getDocumentId());
				for (TitreAccusation titreAccusation : titreAccusations) {
					System.out.println(titreAccusation.getTitreAccusation());
				}
			} else if (aData.get(0) instanceof CarteHeber) {
				System.out.println("CarteHeber.."  );
 
				titreAccusations = accusationCarteHebroRepository
						.getTitreAccusationbyDocument(aData.get(0).getDocumentId());
				for (TitreAccusation titreAccusation : titreAccusations) {
					System.out.println(titreAccusation.getTitreAccusation());
				}
			}
			return  titreAccusations;

		}
	}

	@Override
	public Object getDocumentByArrestation( String idEnfant,
			 long numOrdinalArrestation) {

		return documentRepository.getDocumentByArrestation(idEnfant, numOrdinalArrestation);
	}

	@Override
	public Object countDocumentByAffaire( String idEnfant, long numOrdinalArrestation, long numOrdinalAffaire) {

		return documentRepository.countDocumentByAffaire(idEnfant, numOrdinalArrestation, numOrdinalAffaire);

	}

	@Override
	public Affaire findByArrestation( String idEnfant) {

		List<Affaire> aData = documentRepository.findByArrestation(idEnfant);
		Affaire a = aData.stream().peek(num -> System.out.println("will filter " + num.getTypeDocument()))
				.filter(x -> x.getTypeDocument().equals("CD") || x.getTypeDocument().equals("CH")
						|| x.getTypeDocument().equals("T") || x.getTypeDocument().equals("AP")
						|| x.getTypeDocument().equals("AE"))
				.findFirst().orElse(null);
		System.out.println(a);
		if (a == null) {
			System.out.println("ma7koum");
			return null;  // message = "ma7koum"
		} else {
			System.out.println("maw9ouf");
			return  a;  // message = "maw9ouf"
		}

	}

	@Override
	public  Object findDocumentByArrestation(String idEnfant,
			 long numOrdinalArrestation) {

		return (List<Document>) documentRepository.findDocumentByArrestation(idEnfant, numOrdinalArrestation);
	}

	@Override
	public Integer delete(DocumentId documentId,String type) {

		try {
			int ref = 0;

			if (type.contentEquals("CJ")) {
				System.out.println("yess");
				List<AccusationCarteRecup> listacc = accusationCarteRecupRepository.findByCarteRecup(documentId);
				List<ArretProvisoire> listarr = arretProvisoireRepository.findArretProvisoireByCarteRecup(documentId);

				if (!listacc.isEmpty()) {

					for (AccusationCarteRecup acc : listacc) {
						accusationCarteRecupRepository.delete(acc);
					}

				}

				if (!listarr.isEmpty()) {

					for (ArretProvisoire arr : listarr) {
						arretProvisoireRepository.delete(arr);
					}

				}

			}

			if (type.contentEquals("CD")) {
				List<AccusationCarteDepot> listacc = accusationCarteDepotRepository.findByCarteDepot(documentId);

				if (!listacc.isEmpty()) {

					for (AccusationCarteDepot acc : listacc) {
						accusationCarteDepotRepository.delete(acc);
					}

				}

			}
			if (type.contentEquals("CH")) {
				List<AccusationCarteHeber> listaccH = accusationCarteHeberRepository.findByCarteHeber(documentId);

				if (!listaccH.isEmpty()) {

					for (AccusationCarteHeber acc : listaccH) {
						accusationCarteHeberRepository.delete(acc);
					}

				}

			}
			Document thisDoc = documentRepository.getDocument(documentId.getIdEnfant(),
					documentId.getNumOrdinalArrestation(), documentId.getNumOrdinalAffaire(),
					documentId.getNumOrdinalDocByAffaire());

			Document lastDoc = documentRepository.getDocument(documentId.getIdEnfant(),
					documentId.getNumOrdinalArrestation(), documentId.getNumOrdinalAffaire(),
					documentId.getNumOrdinalDocByAffaire() - 1);

			System.out.println("111");

			if (lastDoc == null) {
				System.out.println("222");
				documentRepository.deleteById(documentId);
				affaireRepository.deleteById(thisDoc.getAffaire().getAffaireId());
				ref = 1;

			}

			else {
				System.out.println("333");
				if (lastDoc.getAffaire().getAffaireId().equals(thisDoc.getAffaire().getAffaireId())) {
					System.out.println("444");

					lastDoc.getAffaire().setTypeAffaire(lastDoc.getTypeAffaire());
					lastDoc.getAffaire().setTypeDocument(lastDoc.getTypeDocument());
					lastDoc.getAffaire().setTypeDocumentActuelle(lastDoc.getTypeDocumentActuelle());
					if (lastDoc.getTypeDocument().contentEquals("CJ")) {

						lastDoc.getAffaire().setDaysDiffJuge(((CarteRecup) lastDoc).getDaysDiffJuge());
						lastDoc.getAffaire().setDateDebutPunition(((CarteRecup) lastDoc).getDateDebutPunition());
						lastDoc.getAffaire().setDateFinPunition(((CarteRecup) lastDoc).getDateFinPunition());

					} else {
//							if(lastDoc.getTypeDocument().contentEquals("T")) {
//								
//									lastDoc.getAffaire().setStatut(2);
//							}
						lastDoc.getAffaire().setDaysDiffJuge(0);
						lastDoc.getAffaire().setDateDebutPunition(null);
						lastDoc.getAffaire().setDateFinPunition(null);

					}

					affaireRepository.save(lastDoc.getAffaire());
					documentRepository.deleteById(documentId);
				} else

				{
					System.out.println("555");
					documentRepository.deleteById(documentId);
					affaireRepository.deleteById(thisDoc.getAffaire().getAffaireId());
					lastDoc.getAffaire().setStatut(0);
					affaireRepository.save(lastDoc.getAffaire());
					ref = 1;

				}
			}

			System.out.println("666");
			return ref;
		} catch (Exception e) {
			return  null;
		}
	}


}


package com.cgpr.mineur.service.Impl;


 
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.DelegationConverter;
import com.cgpr.mineur.converter.DocumentConverter;
import com.cgpr.mineur.converter.DocumentIdConverter;
import com.cgpr.mineur.converter.TitreAccusationConverter;
import com.cgpr.mineur.dto.AffaireDto;
import com.cgpr.mineur.dto.DocumentDto;
import com.cgpr.mineur.dto.DocumentIdDto;
import com.cgpr.mineur.dto.DocumentSearchCriteriaDto;
import com.cgpr.mineur.dto.TitreAccusationDto;
import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Delegation;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArretProvisoireRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.DocumentService;

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
 	

	@Autowired
	private ArretProvisoireRepository arretProvisoireRepository;

	@Autowired
	private AffaireRepository affaireRepository;
	
	

	 

	@Override
	public DocumentDto trouverDocumentJudiciaireParId( DocumentIdDto documentIdDto) {

		System.out.println("==================================documente=========================");
		Optional<Document> document = documentRepository.findById(DocumentIdConverter.dtoToEntity(documentIdDto)  );

		try {
			return DocumentConverter.entityToDto( document.get() );
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public List<DocumentDto> trouverDocumentsJudiciairesParEnfantEtDetentionEtAffaire(String idEnfant,
			 long numOrdinalArrestation,
			 long numOrdinalAffaire) {
		
		System.out.println("departtt");
		
		
		try {
			List<Document> documents = documentRepository.getDocumentByAffaire(idEnfant, numOrdinalArrestation,
					numOrdinalAffaire);
 

			if (documents.isEmpty()) {
				return  null;
			} else {
				return  documents.stream().map(DocumentConverter::entityToDto).collect(Collectors.toList()) ;

			}
		} catch (Exception e) {
			return  null;
		}

	}

 

	@Override
	public Object calculerNombreDocumentsJudiciairesParDetention( String idEnfant,
			 long numOrdinalArrestation) {

		return documentRepository.getDocumentByArrestation(idEnfant, numOrdinalArrestation);
	}

	@Override
	public Object calculerNombreDocumentsJudiciairesParAffaire( String idEnfant, long numOrdinalArrestation, long numOrdinalAffaire) {

		return documentRepository.countDocumentByAffaire(idEnfant, numOrdinalArrestation, numOrdinalAffaire);

	}

	@Override
	public AffaireDto trouverStatutJudiciaire( String idEnfant) {

		List<Affaire> aData = documentRepository.findByArrestation(idEnfant);
		Affaire a = aData.stream().peek(num -> System.out.println("will filter " + num.getTypeDocument()))
				.filter(x -> x.getTypeDocument().equals("CD") || x.getTypeDocument().equals("CH")
						|| x.getTypeDocument().equals("T") || x.getTypeDocument().equals("AP")
						|| x.getTypeDocument().equals("AE") || x.getTypeDocument().equals("OPP"))
				.findFirst().orElse(null);
		System.out.println(a);
		if (a == null) {
			System.out.println("ma7koum");
			return null;  // message = "ma7koum"
		} else {
			System.out.println("maw9ouf");
			return  AffaireConverter. entityToDto(a)  ; // message = "maw9ouf"
		}

	}

	 

	@Override
	public Integer delete(DocumentIdDto documentIdDto,String type) {

		try {
			int ref = 0;

			if (type.contentEquals("CJ")) {
				System.out.println("yess");
				List<AccusationCarteRecup> listacc = accusationCarteRecupRepository.findByCarteRecup(DocumentIdConverter.dtoToEntity(documentIdDto));
				List<ArretProvisoire> listarr = arretProvisoireRepository.findArretProvisoireByCarteRecup(DocumentIdConverter.dtoToEntity(documentIdDto));

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
				List<AccusationCarteDepot> listacc = accusationCarteDepotRepository.findByCarteDepot(DocumentIdConverter.dtoToEntity(documentIdDto));

				if (!listacc.isEmpty()) {

					for (AccusationCarteDepot acc : listacc) {
						accusationCarteDepotRepository.delete(acc);
					}

				}

			}
			if (type.contentEquals("CH")) {
				List<AccusationCarteHeber> listaccH = accusationCarteHeberRepository.findByCarteHeber(DocumentIdConverter.dtoToEntity(documentIdDto));

				if (!listaccH.isEmpty()) {

					for (AccusationCarteHeber acc : listaccH) {
						accusationCarteHeberRepository.delete(acc);
					}

				}

			}
			Document thisDoc = documentRepository.getDocument(documentIdDto.getIdEnfant(),
					documentIdDto.getNumOrdinalArrestation(), documentIdDto.getNumOrdinalAffaire(),
					documentIdDto.getNumOrdinalDocByAffaire());

			Document lastDoc = documentRepository.getDocument(documentIdDto.getIdEnfant(),
					documentIdDto.getNumOrdinalArrestation(), documentIdDto.getNumOrdinalAffaire(),
					documentIdDto.getNumOrdinalDocByAffaire() - 1);

			System.out.println("111");

			if (lastDoc == null) {
				System.out.println("222");
				documentRepository.deleteById(DocumentIdConverter.dtoToEntity(documentIdDto));
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
					documentRepository.deleteById(DocumentIdConverter.dtoToEntity(documentIdDto));
				} else

				{
					System.out.println("555");
					documentRepository.deleteById(DocumentIdConverter.dtoToEntity(documentIdDto));
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


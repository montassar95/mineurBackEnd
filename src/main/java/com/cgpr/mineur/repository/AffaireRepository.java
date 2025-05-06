package com.cgpr.mineur.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.dto.SimpleTuple;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.AffaireId;
import com.cgpr.mineur.models.ArrestationId;

import org.springframework.data.domain.Pageable;

@Repository
public interface AffaireRepository extends PagingAndSortingRepository<Affaire, AffaireId> {

	@Query("select max(a.numOrdinalAffaire) from Affaire a where a.arrestation.arrestationId= ?1 and a.statut = 0 ")
	Integer trouverMaxAffaireParArrestationIdEtStatutActif(ArrestationId arrestationId);

	@Query("SELECT a FROM Affaire a WHERE a.arrestation.arrestationId.idEnfant = ?1 and a.arrestation.arrestationId.numOrdinale = ?2 and a.statut = 0 order by a.numOrdinalAffaire desc ")
	List<Affaire> findByArrestation(String idEnfant, long numOrdinale);

	@Query("SELECT a FROM Affaire a WHERE a.arrestation.arrestationId.idEnfant = ?1 and a.arrestation.arrestationId.numOrdinale = ?2 and a.statut = 0 order by a.numOrdinalAffaire  ")
	List<Affaire> findByArrestationCoroissant(String idEnfant, long numOrdinale);

	@Query("SELECT a FROM Affaire a WHERE a.arrestation.arrestationId.idEnfant = ?1 and a.arrestation.arrestationId.numOrdinale = ?2 and (a.typeDocument = 'CJ'   or a.typeDocument = 'CR' or a.typeDocument = 'CRR') and a.statut = 0 order by a.numOrdinalAffaire desc ")
	List<Affaire> trouverAffairesAAppelerOuReviser(String idEnfant, long numOrdinale);

	@Query("SELECT a FROM Affaire a WHERE a.arrestation.arrestationId.idEnfant = ?1 and a.arrestation.arrestationId.numOrdinale = ?2 and "

			+ " (a.typeDocument = 'CD'"
			+ "or  a.typeDocument = 'CH'"
			+ "or  a.typeDocument = 'T'"
			+ "or  a.typeDocument = 'CJA'"
			+ "or  a.typeDocument = 'AP'"
			+ "or  a.typeDocument = 'CP'"
			+ "or  a.typeDocument = 'AE')"

			+ "and a.statut = 0 order by a.numOrdinalAffaire desc ")
	List<Affaire> findByArrestationToPropaga(String idEnfant, long numOrdinale);

	@Query("SELECT a FROM Affaire a WHERE a.arrestation.arrestationId.idEnfant = ?1 and a.arrestation.arrestationId.numOrdinale = ?2 and (a.typeDocument = 'CH' or a.typeDocument = 'CD' or a.typeDocument = 'CP' or a.typeDocument = 'T' or a.typeDocument = 'CJ' or a.typeDocument = 'CJA' or a.typeDocument = 'CHL') and a.statut = 0 order by a.numOrdinalAffaire desc ")
	List<Affaire> findByArrestationToTransfert(String idEnfant, long numOrdinale);

	@Query("SELECT a FROM Affaire a WHERE a.arrestation.arrestationId.idEnfant = ?1 and a.arrestation.arrestationId.numOrdinale = ?2 and (a.typeDocument != 'ArretEx' ) and a.statut = 0 order by a.numOrdinalAffaire desc ")
	List<Affaire> findByArrestationToArret(String idEnfant, long numOrdinale);

	@Query("SELECT a FROM Affaire a WHERE a.arrestation.arrestationId.idEnfant = ?1 and a.arrestation.arrestationId.numOrdinale = ?2 and a.numOrdinalAffaire = ?3 order by numOrdinalAffaireByAffaire desc")
	List<Affaire> findByNumOrdinalAffaire(String idEnfant, long numOrdinale, long numOrdinalAffaire);

	@Query("SELECT a FROM Affaire a WHERE a.affaireLien.affaireId.idEnfant = ?1 and a.affaireLien.affaireId.numAffaire = ?2 and a.affaireLien.affaireId.idTribunal = ?3")
	Optional<Affaire> findAffaireByAffaireLien(String idEnfant, String numAffaire, long idTribunal);

	@Query("SELECT a FROM Affaire a WHERE a.affaireId.idEnfant = ?1 and a.affaireId.numAffaire = ?2 and a.affaireId.idTribunal = ?3")
	List<Affaire> findAffaireByAnyArrestation(String idEnfant, String numAffaire, long idTribunal);

	// @Query("SELECT COUNT(a) > 0 FROM Affaire a WHERE a.affaireId.numAffaire =
	// :numAffaire AND a.tribunal.id = :tribunalId")
	// boolean trouverDetenusParNumAffaireEtTribunalId(@Param("numAffaire") String
	// numAffaire, @Param("tribunalId") long tribunalId);

	@Query("SELECT a FROM Affaire a WHERE a.statut = 0 "
			+ "AND (a.arrestation.arrestationId.idEnfant IN :idsEnfant "
			+ "AND a.arrestation.arrestationId.numOrdinale IN :numOrdinales) "
			+ "ORDER BY a.typeAffaire.statutException DESC, "
			+ "a.tribunal.typeTribunal.statutNiveau  DESC, "
			+ "a.daysDiffJuge  DESC, "
			+ "a.typeAffaire.statutNiveau DESC ") // DESC
	List<Affaire> findAffairesPrincipales(@Param("idsEnfant") List<String> idsEnfant,
			@Param("numOrdinales") List<Long> numOrdinales);

	@Query("SELECT a FROM Affaire a WHERE a.affaireId.idEnfant = ?1  "
			+ "and  a.affaireId.numOrdinaleArrestation = ?2 "
			+ "and  a.statut = 0 "
			+ "ORDER BY a.typeAffaire.statutException DESC, "
			+ "a.tribunal.typeTribunal.statutNiveau  DESC, "
			+ "a.daysDiffJuge  DESC, "
			+ "a.typeAffaire.statutNiveau DESC ")
	List<Affaire> findAffairePrincipale(String idEnfant, long numOrdinale);

	// + "order by a.typeAffaire.statutException desc,
	// a.tribunal.typeTribunal.statutNiveau desc ,a.daysDiffJuge desc ,
	// a.typeAffaire.statutNiveau desc"

	@Query("SELECT a FROM Affaire a WHERE a.arrestation.arrestationId.idEnfant = ?1  "
			+ "and  a.statut = 0"
			+ "and  a.arrestation.arrestationId.numOrdinale = ?2 "
			+ "and  a.typeDocument != 'ArretEx' "
			+ "and  a.typeAffaire.statutException = (select max(af.typeAffaire.statutException) from Affaire af where "
			+ "af.arrestation.arrestationId.idEnfant = ?1 and  af.statut = 0 "
			+ "and  af.arrestation.arrestationId.numOrdinale = ?2"
			+ "and a.typeDocument != 'ArretEx' ) "

			+ "and  a.tribunal.typeTribunal.statutNiveau = (select max(af.tribunal.typeTribunal.statutNiveau) from Affaire af where "
			+ "af.arrestation.arrestationId.idEnfant = ?1 and  af.statut = 0 "
			+ "and  af.arrestation.arrestationId.numOrdinale = ?2  "
			+ "and af.typeDocument != 'ArretEx' "
			+ "and  af.typeAffaire.statutException = (select max(at.typeAffaire.statutException) from Affaire at where at.arrestation.arrestationId.idEnfant = ?1 and  at.statut = 0  and  at.arrestation.arrestationId.numOrdinale = ?2 and at.typeDocument != 'ArretEx'  ))"

			+ "and  a.daysDiffJuge = (select max(af.daysDiffJuge) from Affaire af  where "
			+ "af.arrestation.arrestationId.idEnfant = ?1 and  af.statut = 0 "
			+ "and  af.arrestation.arrestationId.numOrdinale = ?2  "
			+ "and af.typeDocument != 'ArretEx' "
			+ "and  af.typeAffaire.statutException = (select max(at.typeAffaire.statutException) from Affaire at where at.arrestation.arrestationId.idEnfant = ?1 and  at.statut = 0  and  at.arrestation.arrestationId.numOrdinale = ?2 and at.typeDocument != 'ArretEx' "

			+ "and  af.tribunal.typeTribunal.statutNiveau = (select max(aff.tribunal.typeTribunal.statutNiveau) from Affaire aff where "
			+ "aff.arrestation.arrestationId.idEnfant = ?1 and  aff.statut = 0 "
			+ "and  aff.arrestation.arrestationId.numOrdinale = ?2  and aff.typeDocument != 'ArretEx' "
			+ "and  aff.typeAffaire.statutException = (select max(at.typeAffaire.statutException) from Affaire at where at.arrestation.arrestationId.idEnfant = ?1 and  at.statut = 0  and  at.arrestation.arrestationId.numOrdinale = ?2 and at.typeDocument != 'ArretEx'  ))))"

			+ "order by a.typeAffaire.statutNiveau desc")
	List<Affaire> findAffairePrincipaleSansArretEx(String idEnfant, long numOrdinale, Pageable pageable);

	@Query("select min(a.dateDebutPunition)  FROM Affaire a WHERE a.arrestation.arrestationId.idEnfant = ?1 and "
			+ "a.arrestation.arrestationId.numOrdinale = ?2 and a.statut = 0 and a.affaireAffecter = null and a.daysDiffJuge > 0")
	Date getDateDebutPunition(String idEnfant, long numOrdinale);

	@Query("select max(a.dateFinPunition)  FROM Affaire a WHERE a.arrestation.arrestationId.idEnfant = ?1 and a.arrestation.arrestationId.numOrdinale = ?2 and a.statut = 0 and a.affaireAffecter = null and a.typeDocument != 'ArretEx' and a.typeDocument != 'CJA' ")
	Date getDateFinPunition(String idEnfant, long numOrdinale);

}

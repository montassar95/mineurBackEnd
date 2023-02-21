package com.cgpr.mineur.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Personelle;
 
 
@Repository
public interface PersonelleRepository extends PagingAndSortingRepository<Personelle, Long> {
	@Query("SELECT p FROM Personelle p WHERE p.matricule = ?1")
	Personelle findByMatricule (Long matricule);
	
	@Query("SELECT p FROM Personelle p WHERE p.etablissement.id = ?1")
	List<Personelle> getPersonelleByEta (Long id, Pageable pageable);
	
	 
}
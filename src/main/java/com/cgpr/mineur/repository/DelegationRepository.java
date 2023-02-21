package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Delegation;
import com.cgpr.mineur.models.Gouvernorat;


 

@Repository
public interface DelegationRepository extends CrudRepository<Delegation, Long> {
	@Query("SELECT d FROM Delegation d WHERE d.gouvernorat.id = ?1 and d.id = ?2")
	Delegation findByGouvernorat (Long idG, Long idD);
	
	@Query("SELECT d FROM Delegation d WHERE d.gouvernorat.id = ?1 order by d.id")
	List<Delegation> getDelegationByGouv (Long id);
	
	
	public List<Delegation> findAllByOrderByIdAsc();
	
	 
}


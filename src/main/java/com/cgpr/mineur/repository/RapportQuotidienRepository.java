package com.cgpr.mineur.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.RapportQuotidien;

 
 

@Repository
public interface RapportQuotidienRepository  extends CrudRepository<RapportQuotidien, Long> {

	 @Query("SELECT r FROM RapportQuotidien r WHERE DATE(r.dateSauvgarde) = :date")
	    List<RapportQuotidien> findByDate(@Param("date") LocalDate date);
	 
	 @Query("SELECT r FROM RapportQuotidien r WHERE TRUNC(r.dateSauvgarde) = TRUNC(:date) ORDER BY r.dateSauvgarde DESC")
	    Optional<RapportQuotidien> findLatestByDate(@Param("date") LocalDate date);
//	 @Query(value = "SELECT * FROM rapport_quotidien r WHERE TRUNC(r.date_sauvgarde) = TRUNC(:date) ORDER BY r.date_sauvgarde DESC LIMIT 1", nativeQuery = true)
//	    Optional<RapportQuotidien> findLatestByDate(@Param("date") LocalDate date);
}


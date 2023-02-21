package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Tribunal;
import com.cgpr.mineur.models.TypeTribunal;

 
 

@Repository
public interface TribunalRepository extends CrudRepository< Tribunal, Long> {
	
	
    @Query("SELECT t FROM Tribunal t WHERE t.gouvernorat.id = ?1 order by t.id")
    List<Tribunal> findByIdGouv (long idGouv);
    
    @Query("SELECT t FROM Tribunal t WHERE t.typeTribunal.id = ?1  order by t.id")
    List<Tribunal> findByIdType (long idType);
    
    @Query("SELECT t FROM Tribunal t WHERE t.gouvernorat.id = ?1 and t.typeTribunal.id = ?2  order by t.id")
    List<Tribunal> findByIdGouvAndIdType (long idGouv,long idType);
    
    public List<Tribunal> findAllByOrderByIdAsc();
}


package com.cgpr.mineur.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
 
import com.cgpr.mineur.models.Nationalite;
import com.cgpr.mineur.models.NiveauEducatif;
import com.cgpr.mineur.models.SituationFamiliale;
import com.cgpr.mineur.models.SituationSocial;

 
 

@Repository
public interface SituationSocialRepository extends CrudRepository<SituationSocial, Long> {

	public List<SituationSocial> findAllByOrderByIdAsc();
}


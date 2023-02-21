package com.cgpr.mineur.repository;


 
import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
 
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CartePropagation;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.DocumentId;
 
 
 
@Repository
public interface CartePropagationRepository  extends CrudRepository<CartePropagation, DocumentId> {
	 
	 
	 
	 
	 
	 
	
}
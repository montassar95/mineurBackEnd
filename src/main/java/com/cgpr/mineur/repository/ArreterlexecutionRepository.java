package com.cgpr.mineur.repository;

import org.springframework.data.repository.CrudRepository;

 
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.models.DocumentId;

public interface ArreterlexecutionRepository  extends   CrudRepository<Arreterlexecution, DocumentId>{

}

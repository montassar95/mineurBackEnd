package com.cgpr.mineur.repository;


 
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Echappes;
import com.cgpr.mineur.models.EchappesId;
import com.cgpr.mineur.models.Residence;


 

@Repository
public interface EchappesRepository extends CrudRepository<Echappes, EchappesId> {
	
	@Query("SELECT a FROM Echappes a WHERE a.echappesId.idEnfant = ?1  and a.residenceTrouver = null")
	Echappes findByIdEnfantAndResidenceTrouverNull (String idEnfant );
	
	@Query("select count(a) from Echappes a where a.echappesId.idEnfant = ?1 and  a.echappesId.numOrdinaleArrestation = ?2")
    int countByEnfantAndArrestation(String idEnfant,long numOrdinaleArrestation);
	 
	@Query("SELECT a FROM Echappes a WHERE a.echappesId.idEnfant = ?1  and a.residenceEchapper.residenceId.numOrdinaleArrestation = ?2 order by a.echappesId.numOrdinaleEchappes desc")
	 List<Echappes>findEchappesByIdEnfantAndNumOrdinaleArrestation(String idEnfant, long numOrdinaleArrestation );
	
	
	@Query("SELECT a FROM Echappes a WHERE a.echappesId.idEnfant = ?1  and a.echappesId.numOrdinaleArrestation  = ?2"
			   + " and a.echappesId.numOrdinaleEchappes = (select max(a.echappesId.numOrdinaleEchappes) from Echappes a "
				
		+ "    where a.echappesId.idEnfant = ?1   and a.echappesId.numOrdinaleArrestation  = ?2 and a.residenceTrouver != null)")
	Echappes findMaxEchappes (String idEnfant, long numOrdinale);
	
	 
}


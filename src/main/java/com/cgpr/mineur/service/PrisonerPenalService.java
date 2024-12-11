package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.PersonelleDto;
import com.cgpr.mineur.dto.PrisonerDto;
import com.cgpr.mineur.dto.SearchDetenuDto;
import com.cgpr.mineur.resource.EnfantDTO;

public interface  PrisonerPenalService {

	public List<SearchDetenuDto>   findPrisonerPenalByCriteria ( EnfantDTO enfantDTO);
}

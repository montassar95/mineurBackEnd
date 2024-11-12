package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.CommentTrouverConverter;
import com.cgpr.mineur.dto.CommentTrouverDto;
import com.cgpr.mineur.models.CommentTrouver;
import com.cgpr.mineur.repository.CommentTrouverRepository;
import com.cgpr.mineur.service.CommentTrouverService;
 
 
 

@Service
public class CommentTrouverServiceImpl implements CommentTrouverService  {

	
	@Autowired
	private CommentTrouverRepository commentTrouverRepository;

	@Override
	public List<CommentTrouverDto> listTrouver() {
		List<CommentTrouver > list = commentTrouverRepository.findAllByOrderByIdAsc();
		return  list.stream().map(CommentTrouverConverter::entityToDto).collect(Collectors.toList())  ;
		 
	}

	@Override
	public CommentTrouverDto getTypeAffaireById( long id) {
		Optional<CommentTrouver> commentTrouver = commentTrouverRepository.findById(id);
		if (commentTrouver.isPresent()) {
			return CommentTrouverConverter.entityToDto(commentTrouver.get()) ;
		} else {
			return  null;
		}
	}

	@Override
	public CommentTrouverDto save(CommentTrouverDto commentTrouverDto) {

		try {
			
			CommentTrouver commentTrouver =commentTrouverRepository.save(CommentTrouverConverter.dtoToEntity(commentTrouverDto));
			
			return CommentTrouverConverter.entityToDto(commentTrouver);
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public CommentTrouverDto update( CommentTrouverDto commentTrouverDto) {
		try {

	   CommentTrouver commentTrouver =commentTrouverRepository.save(CommentTrouverConverter.dtoToEntity(commentTrouverDto));
			
			return CommentTrouverConverter.entityToDto(commentTrouver);
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete(long id) {
		try {
			commentTrouverRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return null;
		}
	}
 
	
	 

}


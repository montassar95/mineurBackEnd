package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.ClassePenaleConverter;
import com.cgpr.mineur.converter.CommentEchapperConverter;
import com.cgpr.mineur.dto.CommentEchapperDto;
import com.cgpr.mineur.models.ClassePenale;
import com.cgpr.mineur.models.CommentEchapper;
import com.cgpr.mineur.repository.CommentEchapperRepository;
import com.cgpr.mineur.service.CommentEchapperService;

 
 

@Service
public class CommentEchapperServiceImpl implements CommentEchapperService  {

	
	@Autowired
	private CommentEchapperRepository commentEchapperRepository;

	
	
	@Override
	public List<CommentEchapperDto> listCommentEchapper() {
		List<CommentEchapper > list = commentEchapperRepository.findAllByOrderByIdAsc();
		return  list.stream().map(CommentEchapperConverter::entityToDto).collect(Collectors.toList())  ;
		 
	}

	@Override
	public CommentEchapperDto getTypeAffaireById( long id) {
		Optional<CommentEchapper> commentEchapper = commentEchapperRepository.findById(id);
		if (commentEchapper.isPresent()) {
			return CommentEchapperConverter.entityToDto(commentEchapper.get()) ;
		} else {
			return  null;
		}
	}

	@Override
	public CommentEchapperDto save(CommentEchapperDto commentEchapperDto) {

		try {
			
			CommentEchapper commentEchapper =commentEchapperRepository.save(CommentEchapperConverter.dtoToEntity(commentEchapperDto));
			
			return CommentEchapperConverter.entityToDto(commentEchapper);
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public CommentEchapperDto update( CommentEchapperDto commentEchapperDto) {
		try {

	   CommentEchapper commentEchapper =commentEchapperRepository.save(CommentEchapperConverter.dtoToEntity(commentEchapperDto));
			
			return CommentEchapperConverter.entityToDto(commentEchapper);
		} catch (Exception e) {
			return  null;
		}

	}

	@Override
	public Void delete(long id) {
		try {
			commentEchapperRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return null;
		}
	}
 
	
 
	 
	 

}


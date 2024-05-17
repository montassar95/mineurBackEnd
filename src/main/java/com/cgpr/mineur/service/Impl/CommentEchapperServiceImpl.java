package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.CommentEchapper;
import com.cgpr.mineur.repository.CommentEchapperRepository;
import com.cgpr.mineur.service.CommentEchapperService;

 
 

@Service
public class CommentEchapperServiceImpl implements CommentEchapperService  {

	
	@Autowired
	private CommentEchapperRepository commentEchapperRepository;

	
	
	@Override
	public List<CommentEchapper> listCommentEchapper() {
		return commentEchapperRepository.findAllByOrderByIdAsc();
	}

	@Override
	public CommentEchapper getTypeAffaireById( long id) {
		Optional<CommentEchapper> typeData = commentEchapperRepository.findById(id);
		if (typeData.isPresent()) {
			return  typeData.get();
		} else {
			return  null;
		}
	}

	@Override
	public CommentEchapper save(CommentEchapper causeDeces) {

		try {
			return commentEchapperRepository.save(causeDeces);
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public CommentEchapper update( CommentEchapper causeDeces) {
		try {

			return commentEchapperRepository.save(causeDeces);
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


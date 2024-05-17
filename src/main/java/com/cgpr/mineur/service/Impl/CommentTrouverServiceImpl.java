package com.cgpr.mineur.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.CommentTrouver;
import com.cgpr.mineur.repository.CommentTrouverRepository;
import com.cgpr.mineur.service.CommentTrouverService;
 
 
 

@Service
public class CommentTrouverServiceImpl implements CommentTrouverService  {

	
	@Autowired
	private CommentTrouverRepository commentTrouverRepository;

	@Override
	public List<CommentTrouver> listTrouver() {
		return commentTrouverRepository.findAllByOrderByIdAsc();
	}

	@Override
	public CommentTrouver getTypeAffaireById( long id) {
		Optional<CommentTrouver> typeData = commentTrouverRepository.findById(id);
		if (typeData.isPresent()) {
			return typeData.get();
		} else {
			return null;
		}
	}

	@Override
	public CommentTrouver save(CommentTrouver causeDeces) {

		try {
			return commentTrouverRepository.save(causeDeces);
		} catch (Exception e) {
			return  null;
		}
	}

	@Override
	public CommentTrouver update( CommentTrouver causeDeces) {
		try {

			return commentTrouverRepository.save(causeDeces);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Void delete(long id) {
		try {
			commentTrouverRepository.deleteById(id);
			return  null;
		} catch (Exception e) {
			return  null;
		}
	}
	
 
	
	 

}


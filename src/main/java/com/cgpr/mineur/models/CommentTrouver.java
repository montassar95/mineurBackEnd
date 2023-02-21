package com.cgpr.mineur.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "comTro")
public class CommentTrouver {
	@Id
	//@GeneratedValue
	private long id;
	 

	private String libelleComment;

}
package com.cgpr.mineur.models;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "photo")
public class Photo  implements Serializable {

	
	@EmbeddedId
	private PhotoId photoId;

	
//     @Lob
// 	 private byte[] image;
	 
	 @Lob
	 private String img;
	 
	 
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "a_idEnf_photo", referencedColumnName = "idEnf"),
			@JoinColumn(name = "a_numOrd_photo", referencedColumnName = "numOrd") })
	private Arrestation arrestation;
}

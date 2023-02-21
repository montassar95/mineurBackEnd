package com.cgpr.mineur.models;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "typTri")
public class TypeTribunal {
	@Id 
	private long id;
 
	private String libelleTypeTribunal;
	 
	private int statutNiveau;

}

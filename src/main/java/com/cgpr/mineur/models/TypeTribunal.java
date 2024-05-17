package com.cgpr.mineur.models;
import java.io.Serializable;

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
public class TypeTribunal  implements Serializable {
	@Id 
	private long id;
 
	private String libelleTypeTribunal;
	 
	private int statutNiveau;

}

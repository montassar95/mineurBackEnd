package com.cgpr.mineur.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cgpr.mineur.dto.GouvernoratDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "gou")
public class Gouvernorat  implements Serializable {

	@Id
	private long id;

 

	private String libelle_gouvernorat;

}

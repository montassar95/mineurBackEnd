package com.cgpr.mineur.models;

import javax.persistence.GeneratedValue;


import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "cauLib")
public class CauseLiberation  implements Serializable {
	@Id
	@GeneratedValue
	private long id;
 

	private String libelleCauseLiberation;

}

package com.cgpr.mineur.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
//@AllArgsConstructor
@ToString
public class ApiResponseAmenPhotoDto {
	 
	    private int status;
	    private String message;
	    private  ApiResultAmenPhotoDto  result;

	 
}

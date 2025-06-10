package com.cgpr.mineur.payload.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    private List<String> warnings = new ArrayList<>(); // éviter null
    

    // Constructeur minimal avec token uniquement
    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}

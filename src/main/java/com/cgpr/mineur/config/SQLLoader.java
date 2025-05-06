package com.cgpr.mineur.config;

import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
public class SQLLoader {

//    public static String loadSQL(String filePath) throws IOException {
//        return new String(Files.readAllBytes(Paths.get(ResourceUtils.getFile(filePath).toURI())));
//    }
	
	  public static String loadSQL(String filePath) throws IOException {
	        // Utilise le ClassLoader pour obtenir le fichier à partir du classpath
	        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	        try (InputStream inputStream = classLoader.getResourceAsStream(filePath);
	             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
	            if (inputStream == null) {
	                throw new IOException("Fichier introuvable : " + filePath);
	            }
	            return reader.lines().collect(Collectors.joining("\n"));
	        }
	    }
}

package com.cgpr.mineur.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.cgpr.mineur.service.AiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*") // autoriser Angular
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping
    public Mono<String> ask(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");
        System.out.println("question : " + question);

        return aiService.askLlm(question.trim())
            .flatMap(response -> {
                // Parser la réponse JSON pour extraire content
                ObjectMapper mapper = new ObjectMapper();
                try {
                    JsonNode root = mapper.readTree(response);
                    String answer = root.path("choices").get(0).path("message").path("content").asText();
                    System.out.println("réponse extraite : " + answer);
                    return Mono.just(answer);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Mono.just("Erreur lors de la lecture de la réponse");
                }
            });
    }


}

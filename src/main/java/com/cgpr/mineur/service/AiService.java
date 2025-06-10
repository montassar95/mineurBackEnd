package com.cgpr.mineur.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AiService {

    private final WebClient webClient;

    public AiService(@Value("${openrouter.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
            .baseUrl("https://openrouter.ai/api/v1/chat/completions")
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    public Mono<String> askLlm(String question) {
    	String prompt = "استخرج من النص التالي نوع البحث (مثل طفل أو سجين)، والحالة القضائية فقط (مثل موقوف أو محكوم أو سراح)، ونوع القضية (مثل قتل، سرقة، عنف...).\n\n"
    	        + "أعد الرد في صيغة JSON صحيحة تماماً فقط باللغة العربية بدون أي ترجمة أو شرح.\n\n"
    	        + "إذا كانت الحالة القضائية في النص هي 'مفرج عنه' أو 'مطلق سراح'، فاستخدم 'سراح' فقط.\n"
    	        + "لا تفترض أن الشخص محكوم إذا لم يتم ذكر ذلك صراحةً بكلمات مثل 'محكوم' أو 'صدر الحكم'.\n"
    	        + "إذا لم يتم ذكر نوع القضية صراحةً في النص، لا تضعها في الناتج.\n"
    	        + "النص: \"" + question.replace("\"", "\\\"") + "\"";



        String requestBody = "{"
            + "\"model\": \"nousresearch/nous-hermes-2-mixtral-8x7b-dpo\","
            + "\"messages\": ["
            + "  { \"role\": \"user\", \"content\": \"" + prompt.replace("\n", "\\n").replace("\"", "\\\"") + "\" }"
            + "],"
            + "\"temperature\": 0,"
            + "\"max_tokens\": 200"
            + "}";

        return webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class);
    }






}


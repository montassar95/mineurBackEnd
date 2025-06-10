package com.cgpr.mineur.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class EnvoiSmsServiceImpl implements EnvoiSmsService {


	
	 

	private final WebClient webClient; // Ne pas initialiser à null
//--	http://172.27.28.221:13002/cgi-bin/sendsms?username=cgpr-direction1&password=5412az&from=SOUJOUN&to=21695472986&text=Bonjour
    @Autowired
    public EnvoiSmsServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://172.27.28.221:13002/cgi-bin/sendsms").build();
    }
    @Override
    public Mono<Boolean> envoyerSms(String from, String to, String text) {
        // Construisez l'URL avec les paramètres
        String url = String.format(
                "?username=%s&password=%s&from=%s&to=%s&text=%s&charset=%s&coding=%s&fbclid=%s",
                "cgpr-direction1", "5412az", from, to, text, "utf-8", "2", "IwAR0Rhg7jZX8ZSYh0Nw1QK4ZthHijseuFXXZz5V_-ewydBZt1wuYYppo8D3A");

        // Créez la requête HTTP GET pour envoyer le SMS
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .map(responseBody -> {
                    System.out.println("Réponse de l'API SMS : " + responseBody);

                    // Vous pouvez ajouter ici la logique de traitement de la réponse

                    // Si la réponse contient un certain texte ou suit un modèle particulier, vous pouvez retourner true
                    // sinon, retournez false pour indiquer l'échec
System.err.println("URL SMS "+ url);
                    return responseBody.contains("0: Accepted for delivery");
                })
                .onErrorResume(throwable -> {
                    System.err.println("Erreur lors de l'envoi du SMS : " + throwable.getMessage());
                    // Vous pouvez gérer les erreurs ici
                    return Mono.just(false); // En cas d'erreur, retournez false pour indiquer l'échec
                });
    }


 

}

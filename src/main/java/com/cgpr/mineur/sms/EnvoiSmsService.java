package com.cgpr.mineur.sms;

import reactor.core.publisher.Mono;

public interface EnvoiSmsService {

	Mono<Boolean> envoyerSms(String from, String to, String text);

}

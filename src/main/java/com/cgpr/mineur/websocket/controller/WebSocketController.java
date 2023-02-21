package com.cgpr.mineur.websocket.controller;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @Autowired
    WebSocketController(SimpMessagingTemplate template){
        this.template = template;
    }

    @MessageMapping("/send/message")
    public void sendMessage(String message){
       
        if(message.length()>3) {
        	 System.out.println(message);
        	 this.template.convertAndSend("/message",  message);
        }
        else {
        	this.template.convertAndSend("/message",  "R.A.S.");
        	 System.err.println(message);
        }
    }

    
}
package com.petros.chatapp.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.petros.chatapp.model.ChatMessage;

/*
 * We’ll define the message handling methods in our controller. 
 * These methods will be responsible for receiving messages from 
 * one client and then broadcasting it to others.
 * */

@Controller
public class ChatController {
	
	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		return chatMessage;
	}
	
	@MessageMapping("/chat.adduser")
	@SendTo("/topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		// Add user in websocket session
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}
}

/*
 * If you recall from the websocket configuration, all the messages 
 * sent from clients with a destination starting with /app will be 
 * routed to these message handling methods annotated with @MessageMapping.
 * For example, a message with destination /app/chat.sendMessage will 
 * be routed to the sendMessage() method, and a message with destination 
 * /app/chat.addUser will be routed to the addUser() method.
 * */
